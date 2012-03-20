/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.sample.snippets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.beans.common.ListBean;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Demonstrates how to show an error marker, if the ComboRidget's selection is
 * no longer available.
 */
public class SnippetComboRidget004 {

	public static void main(final String[] args) {
		final Display display = Display.getDefault();

		try {
			final Shell shell = UIControlsFactory.createShell(display);
			GridLayoutFactory.swtDefaults().numColumns(2).spacing(10, 5).applyTo(shell);
			shell.setText(SnippetComboRidget004.class.getSimpleName());

			UIControlsFactory.createLabel(shell, "ComboRidget:"); //$NON-NLS-1$
			final Combo combo = UIControlsFactory.createCombo(shell);

			UIControlsFactory.createLabel(shell, "Selection:"); //$NON-NLS-1$
			final Label label = UIControlsFactory.createLabel(shell, "Monday"); //$NON-NLS-1$
			GridDataFactory.fillDefaults().grab(true, false).applyTo(label);

			final Button buttonDel = new Button(shell, SWT.PUSH);
			buttonDel.setText("Delete selection - shows error"); //$NON-NLS-1$
			GridDataFactory.fillDefaults().span(2, 1).applyTo(buttonDel);

			final Button buttonSel = new Button(shell, SWT.PUSH);
			buttonSel.setText("Select element - hides error"); //$NON-NLS-1$
			GridDataFactory.fillDefaults().span(2, 1).applyTo(buttonSel);

			// ridgets

			final IComboRidget comboRidget = (IComboRidget) SwtRidgetFactory.createRidget(combo);
			final ListBean input = createInput();
			comboRidget.bindToModel(input, ListBean.PROPERTY_VALUES, String.class, null, label, "text"); //$NON-NLS-1$
			comboRidget.updateFromModel();

			comboRidget.setMarkSelectionMismatch(true);

			final IActionRidget actionDel = (IActionRidget) SwtRidgetFactory.createRidget(buttonDel);
			actionDel.addListener(new IActionListener() {
				public void callback() {
					final Object sel = comboRidget.getSelection();
					if (sel != null) {
						System.out.println(String.format("Removing '%s' from model", sel)); //$NON-NLS-1$
						input.getValues().remove(sel);
						comboRidget.updateFromModel();
					}
				}
			});

			final IActionRidget actionSel = (IActionRidget) SwtRidgetFactory.createRidget(buttonSel);
			actionSel.addListener(new IActionListener() {
				public void callback() {
					if (!input.getValues().isEmpty()) {
						comboRidget.setSelection(0);
					}
				}
			});

			shell.pack();
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

	private static ListBean createInput() {
		final List<String> values = new ArrayList<String>();
		for (final String day : new String[] {
				"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" }) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			values.add(day);
		}
		return new ListBean(values);
	}
}
