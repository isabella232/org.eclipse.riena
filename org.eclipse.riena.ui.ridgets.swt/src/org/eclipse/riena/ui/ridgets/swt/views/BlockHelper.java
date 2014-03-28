/*******************************************************************************
 * Copyright (c) 2007, 2014 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets.swt.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.controller.AbstractWindowController.Blocker;
import org.eclipse.riena.ui.ridgets.controller.ControllerHelper;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Manages the blocking behavior of navigation nodes and dialogs.
 * 
 * @since 6.0
 */
public abstract class BlockHelper implements Blocker {
	private boolean blocked;
	private Cursor oldCursor;
	/**
	 * Keep a reference to the control that was last focused for a given controller id.
	 * 
	 * @see #getControllerId()
	 * @see #canRestoreFocus()
	 */
	private final Map<Integer, Control> focusControlMap = new HashMap<Integer, Control>(1);
	private Composite contentComposite;

	@Override
	public void setBlocked(final boolean blocked) {
		if (!getParentComposite().isDisposed()) {
			if (blocked) {
				blockView();
			} else {
				unBlockView();
			}
		}
	}

	@Override
	public boolean isBlocked() {
		return blocked;
	}

	/**
	 * @param contentComposite
	 */
	public void registerOnContentComposite(final Composite contentComposite) {
		this.contentComposite = contentComposite;

		final FocusListener focusListener = new FocusListener();

		contentComposite.getDisplay().addFilter(SWT.FocusIn, focusListener);
		contentComposite.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(final DisposeEvent event) {
				event.widget.getDisplay().removeFilter(SWT.FocusIn, focusListener);
			}
		});
	}

	private void blockView() {
		if (!blocked) {
			oldCursor = getParentComposite().getCursor();

			if (getController() != null) {
				for (final IRidget ridget : getController().getRidgets()) {
					if (ridget.hasFocus()) {
						final Object uiControl = ridget.getUIControl();
						if (uiControl instanceof Control) {
							saveFocus((Control) uiControl);
						}
					}
				}
			}
			getParentComposite().setCursor(getWaitCursor());
			contentComposite.setEnabled(false);
			blocked = true;
		}
	}

	private void unBlockView() {
		blocked = false;
		getParentComposite().setCursor(oldCursor);
		contentComposite.setEnabled(true);

		contentComposite.setRedraw(false);
		contentComposite.setRedraw(true);
		if (shouldRestoreFocus() && canRestoreFocus()) {
			setFocus();
		}
		oldCursor = null;
		// Update markers here, because for some controls (ChoiceComposite, CComboRidget, DatePickerComposite) the mandatory marker was not visualized
		// after unblock when set while block
		// ruv 356
		if (getController() != null) {
			for (final IRidget ridget : getController().getRidgets()) {
				if (ridget instanceof IMarkableRidget) {
					((IMarkableRidget) ridget).updateMarkers();
				}
			}
			ControllerHelper.restoreFocusRequestFromRidget(getController().getRidgets(), blocked);
			//				getController().restoreFocusRequestFromRidget(getController().getRidgets());
		}
	}

	/**
	 * This implementation will automatically focus on the control that had previously the focus, or, the first focusable control.
	 * <p>
	 * You may overwrite it, but it typically is not necessary to do so. If you still want to use the 'restore focus to last control' functionality, check
	 * {@link #canRestoreFocus()} and the invoke this method.
	 */
	public void setFocus() {
		if (canRestoreFocus()) {
			final Integer id = Integer.valueOf(getControllerId());
			final Control lastFocusedControl = focusControlMap.get(id);
			lastFocusedControl.setFocus();
		} else if (canFocusOnRidget()) {
			getFocusRidget().requestFocus();
		} else {
			contentComposite.setFocus();
		}
	}

	protected abstract IRidgetContainer getController();

	protected abstract Control getParentComposite();

	protected abstract boolean shouldRestoreFocus();

	protected abstract IRidget getFocusRidget();

	private boolean canFocusOnRidget() {
		boolean result = false;
		final IRidget ridget = getFocusRidget();
		if (ridget != null) {
			result = ridget.isFocusable() && ridget.isEnabled() && ridget.isVisible();
			if (ridget instanceof IMarkableRidget) {
				result &= !((IMarkableRidget) ridget).isOutputOnly();
			}
		}
		return result;
	}

	private void saveFocus(final Control control) {
		final int id = getControllerId();
		if (id != 0) {
			focusControlMap.put(Integer.valueOf(id), control);
		}
	}

	/**
	 * Returns the id (hashcode) of the controller if available, or zero.
	 */
	private int getControllerId() {
		final IRidgetContainer controller = getController();
		return controller == null ? 0 : controller.hashCode();
	}

	/**
	 * Returns true if {@link #setFocus()} can restore the focus to the control that last had the focus in this view; false otherwise.
	 * 
	 */
	public final boolean canRestoreFocus() {
		final Integer id = Integer.valueOf(getControllerId());
		final Control control = focusControlMap.get(id);
		return !SwtUtilities.isDisposed(control);
	}

	private Cursor getWaitCursor() {
		return contentComposite.getDisplay().getSystemCursor(SWT.CURSOR_WAIT);
	}

	/**
	 * Keeps track of the last focused control within this view.
	 */
	private final class FocusListener implements Listener {
		public void handleEvent(final Event event) {
			if (contentComposite.isVisible() && event.widget instanceof Control) {
				final Control control = (Control) event.widget;
				if (contains(contentComposite, control)) {
					saveFocus(control);
				}
			}
		}

		private boolean contains(final Composite container, final Control control) {
			boolean result = false;
			Composite parent = control.getParent();
			// what if the contentComposite fires the event itself?
			while (!result && parent != null) {
				result = container == parent;
				parent = parent.getParent();
			}
			return result;
		}
	}
}