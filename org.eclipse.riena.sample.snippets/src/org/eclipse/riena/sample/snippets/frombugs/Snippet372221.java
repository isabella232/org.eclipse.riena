/*******************************************************************************
 * Copyright (c) 2007, 2013 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.sample.snippets.frombugs;

import java.util.List;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.beans.common.ListBean;
import org.eclipse.riena.ui.ridgets.ICComboRidget;
import org.eclipse.riena.ui.ridgets.holder.SelectableListHolder;
import org.eclipse.riena.ui.ridgets.listener.ISelectionListener;
import org.eclipse.riena.ui.ridgets.listener.SelectionEvent;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * A text field with validation
 */
public class Snippet372221 {

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			shell.setLayout(new GridLayout());

			final CCombo c = UIControlsFactory.createCCombo(shell);
			c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			final ICComboRidget r = (ICComboRidget) SwtRidgetFactory.createRidget(c);
			r.bindToModel(new ListBean("one", "two", "three"), ListBean.PROPERTY_VALUES, String.class, "toString", new SelectableListHolder<String>(),
					SelectableListHolder.SELECTION_PROPERTY);
			r.updateFromModel();

			r.addSelectionListener(new ISelectionListener() {
				private boolean breakRecurrence = false;

				public void ridgetSelected(final SelectionEvent event) {
					// breakRecurrence will break the recurrence if the oldSelection will be activated in Combo.
					if (breakRecurrence) {
						breakRecurrence = false;
						r.updateFromModel();
						return;
					}

					if (!validation()) {
						breakRecurrence = true;
						final List<Object> oldSelection = event.getOldSelection();
						if (!oldSelection.isEmpty()) {
							display.asyncExec(new Runnable() {
								public void run() {
									r.setSelection(oldSelection.get(0));
								}
							});
						}
					}
				}

				private boolean validation() {
					//At this point a validation will be made. This may be a dialog with interaction with the user.
					//In my example the validation will be false, because the oldSelection has to be activated.
					return false;
				}
			});

			shell.setSize(400, 400);
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
