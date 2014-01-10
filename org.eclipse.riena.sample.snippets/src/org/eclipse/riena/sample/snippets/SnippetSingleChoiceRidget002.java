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
package org.eclipse.riena.sample.snippets;

import org.eclipse.core.databinding.observable.list.AbstractObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.ui.ridgets.IElementComparer;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Demonstrates the usage of an {@link IElementComparer} in a {@link ISingleChoiceRidget}.
 */
public final class SnippetSingleChoiceRidget002 {
	private static final String ONE = "one"; //$NON-NLS-1$
	private static final String TWO = "two"; //$NON-NLS-1$

	private static class Model extends AbstractObservableList {
		private Object[] elements = { 1, 2 };

		public Object getElementType() {
			return Object.class;
		}

		@Override
		protected int doGetSize() {
			return elements.length;
		}

		@Override
		public Object get(final int index) {
			return elements[index];
		}

		public void swapElements() {
			if (elements[0] instanceof Integer) {
				elements = new Object[] { ONE, TWO };
			} else {
				elements = new Object[] { 1, 2 };
			}
		}

	}

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = UIControlsFactory.createShell(display);
			shell.setText(SnippetSingleChoiceRidget002.class.getSimpleName());
			shell.setLayout(new GridLayout(1, false));

			UIControlsFactory.createLabel(shell, "Click the button to swap the model element without loosing the selection.", SWT.WRAP).setLayoutData( //$NON-NLS-1$
					new GridData(GridData.FILL_HORIZONTAL));

			final ChoiceComposite c = UIControlsFactory.createChoiceComposite(shell, SWT.NONE, false);
			c.setLayoutData(new GridData(GridData.FILL_BOTH));
			final Button b = UIControlsFactory.createButton(shell, "click"); //$NON-NLS-1$

			final ISingleChoiceRidget r = (ISingleChoiceRidget) SwtRidgetFactory.createRidget(c);
			r.setComparer(new IElementComparer() {
				public boolean equals(final Object a, final Object b) {
					return a == null && b == null || a != null && a.equals(b) || areOne(a, b) || areTwo(a, b);
				}

				private boolean areOne(final Object a, final Object b) {
					final String str = ONE;
					final int i = 1;
					return eq(str, i, a, b) || eq(str, i, b, a);
				}

				private boolean areTwo(final Object a, final Object b) {
					final String str = TWO;
					final int i = 2;
					return eq(str, i, a, b) || eq(str, i, b, a);
				}

				protected boolean eq(final String str, final int i, final Object o1, final Object o2) {
					return str.equals(o1) && o2 instanceof Integer && (Integer) o2 == i;
				}
			});
			final IObservableValue selectionValue = new WritableValue(null, Object.class);
			final Model model = new Model();
			r.bindToModel(model, selectionValue);
			r.updateFromModel();

			b.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					model.swapElements();
					r.updateFromModel();
				}
			});

			shell.setSize(300, 300);
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} finally {
			display.dispose();
		}
	}
}
