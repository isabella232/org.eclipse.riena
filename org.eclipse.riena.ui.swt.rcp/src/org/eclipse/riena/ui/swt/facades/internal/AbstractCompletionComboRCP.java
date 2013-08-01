/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation (CCombo)
 *     compeople AG    - adjustments for autocompletion
 *******************************************************************************/
package org.eclipse.riena.ui.swt.facades.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleTextAdapter;
import org.eclipse.swt.accessibility.AccessibleTextEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.ui.swt.CompletionCombo;

/**
 * RCP specific implementation of a {@link CompletionCombo}. There are two
 * concrete subclasses:
 * <ul>
 * <li>CompletionComboRCP &ndash; a CompletionCombo with a text field and a list
 * widget</li>
 * <li>CompletionComboWithImageRCP &ndash; a CompletionCombo with a text field,
 * with an optional image on the left, and a table widget (which can show an
 * image next to each item)</li>
 * </ul>
 */
abstract class AbstractCompletionComboRCP extends CompletionCombo {

	protected AbstractCompletionComboRCP(final Composite parent, final int style) {
		super(parent, style);
		initAccessible();
	}

	@Override
	public boolean traverse(final int event) {
		/*
		 * When the traverse event is sent to the CCombo, it will create a list
		 * of controls to tab to next. Since the CCombo is a composite, the next
		 * control is the Text field which is a child of the CCombo. It will set
		 * focus to the text field which really is itself. So, call the traverse
		 * next events directly on the text.
		 */
		if (event == SWT.TRAVERSE_ARROW_NEXT || event == SWT.TRAVERSE_TAB_NEXT) {
			return getTextControl().traverse(event);
		}
		return super.traverse(event);
	}

	void initAccessible() {
		final Text text = getTextControl();
		final Button arrow = getButtonControl();
		final Control list = getListControl();
		final AccessibleAdapter accessibleAdapter = new AccessibleAdapter() {
			@Override
			public void getName(final AccessibleEvent e) {
				String name = null;
				final Label label = getAssociatedLabel();
				if (label != null) {
					name = stripMnemonic(label.getText());
				}
				e.result = name;
			}

			@Override
			public void getKeyboardShortcut(final AccessibleEvent e) {
				String shortcut = null;
				final Label label = getAssociatedLabel();
				if (label != null) {
					final String labelText = label.getText();
					if (labelText != null) {
						final char mnemonic = _findMnemonic(labelText);
						if (mnemonic != '\0') {
							shortcut = "Alt+" + mnemonic; //$NON-NLS-1$
						}
					}
				}
				e.result = shortcut;
			}

			@Override
			public void getHelp(final AccessibleEvent e) {
				e.result = getToolTipText();
			}
		};
		getAccessible().addAccessibleListener(accessibleAdapter);
		text.getAccessible().addAccessibleListener(accessibleAdapter);
		list.getAccessible().addAccessibleListener(accessibleAdapter);

		arrow.getAccessible().addAccessibleListener(new AccessibleAdapter() {
			@Override
			public void getName(final AccessibleEvent e) {
				e.result = isDropped() ? SWT.getMessage("SWT_Close") : SWT.getMessage("SWT_Open"); //$NON-NLS-1$ //$NON-NLS-2$
			}

			@Override
			public void getKeyboardShortcut(final AccessibleEvent e) {
				e.result = "Alt+Down Arrow"; //$NON-NLS-1$
			}

			@Override
			public void getHelp(final AccessibleEvent e) {
				e.result = getToolTipText();
			}
		});

		getAccessible().addAccessibleTextListener(new AccessibleTextAdapter() {
			@Override
			public void getCaretOffset(final AccessibleTextEvent e) {
				e.offset = text.getCaretPosition();
			}

			@Override
			public void getSelectionRange(final AccessibleTextEvent e) {
				final Point sel = text.getSelection();
				e.offset = sel.x;
				e.length = sel.y - sel.x;
			}
		});

		getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {
			@Override
			public void getChildAtPoint(final AccessibleControlEvent e) {
				final Point testPoint = toControl(e.x, e.y);
				if (getBounds().contains(testPoint)) {
					e.childID = ACC.CHILDID_SELF;
				}
			}

			@Override
			public void getLocation(final AccessibleControlEvent e) {
				final Rectangle location = getBounds();
				final Point pt = getParent().toDisplay(location.x, location.y);
				e.x = pt.x;
				e.y = pt.y;
				e.width = location.width;
				e.height = location.height;
			}

			@Override
			public void getChildCount(final AccessibleControlEvent e) {
				e.detail = 0;
			}

			@Override
			public void getRole(final AccessibleControlEvent e) {
				e.detail = ACC.ROLE_COMBOBOX;
			}

			@Override
			public void getState(final AccessibleControlEvent e) {
				e.detail = ACC.STATE_NORMAL;
			}

			@Override
			public void getValue(final AccessibleControlEvent e) {
				e.result = getText();
			}
		});

		text.getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {
			@Override
			public void getRole(final AccessibleControlEvent e) {
				e.detail = text.getEditable() ? ACC.ROLE_TEXT : ACC.ROLE_LABEL;
			}
		});

		arrow.getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {
			@Override
			public void getDefaultAction(final AccessibleControlEvent e) {
				e.result = isDropped() ? SWT.getMessage("SWT_Close") : SWT.getMessage("SWT_Open"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		});
	}

	/*
	 * Return the lowercase of the first non-'&' character following an '&'
	 * character in the given string. If there are no '&' characters in the
	 * given string, return '\0'.
	 */
	char _findMnemonic(final String string) {
		if (string == null) {
			return '\0';
		}
		int index = 0;
		final int length = string.length();
		do {
			while (index < length && string.charAt(index) != '&') {
				index++;
			}
			if (++index >= length) {
				return '\0';
			}
			if (string.charAt(index) != '&') {
				return Character.toLowerCase(string.charAt(index));
			}
			index++;
		} while (index < length);
		return '\0';
	}

	/*
	 * Return the Label immediately preceding the receiver in the z-order, or
	 * null if none.
	 */
	Label getAssociatedLabel() {
		final Control[] siblings = getParent().getChildren();
		for (int i = 0; i < siblings.length; i++) {
			if (siblings[i] == this) {
				if (i > 0 && siblings[i - 1] instanceof Label) {
					return (Label) siblings[i - 1];
				}
			}
		}
		return null;
	}

	String stripMnemonic(final String string) {
		int index = 0;
		final int length = string.length();
		do {
			while ((index < length) && (string.charAt(index) != '&')) {
				index++;
			}
			if (++index >= length) {
				return string;
			}
			if (string.charAt(index) != '&') {
				return string.substring(0, index - 1) + string.substring(index, length);
			}
			index++;
		} while (index < length);
		return string;
	}

}