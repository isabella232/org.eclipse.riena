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
package org.eclipse.riena.sample.snippets;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.beans.common.ListBean;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Demonstrates binding a ComboRidget's input and selection.
 */
public class SnippetComboRidget001 {

	public static void main(final String[] args) {
		final Display display = Display.getDefault();

		try {
			final Shell shell = UIControlsFactory.createShell(display);
			shell.setLayout(new GridLayout(2, false));
			shell.setText(SnippetComboRidget001.class.getSimpleName());

			UIControlsFactory.createLabel(shell, "ComboRidget:"); //$NON-NLS-1$
			final Combo combo = UIControlsFactory.createCombo(shell);

			UIControlsFactory.createLabel(shell, "Selection:"); //$NON-NLS-1$
			final Label label = UIControlsFactory.createLabel(shell, "Monday"); //$NON-NLS-1$
			GridDataFactory.fillDefaults().grab(true, false).applyTo(label);

			final IComboRidget comboRidget = (IComboRidget) SwtRidgetFactory.createRidget(combo);
			comboRidget.bindToModel(createInput(), ListBean.PROPERTY_VALUES, String.class, null, label, "text"); //$NON-NLS-1$
			comboRidget.updateFromModel();

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
		return new ListBean("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
	}

}
