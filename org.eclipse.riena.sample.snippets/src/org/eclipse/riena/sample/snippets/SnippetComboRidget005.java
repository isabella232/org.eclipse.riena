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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.beans.common.ListBean;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.CompletionCombo;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Demonstrates how to use a Combo with autocompletion (i.e. Riena's
 * CompletionCombo).
 */
public class SnippetComboRidget005 {

	public static void main(final String[] args) {
		final Display display = Display.getDefault();

		try {
			final Shell shell = UIControlsFactory.createShell(display);
			GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(true).spacing(10, 5).applyTo(shell);
			shell.setText(SnippetComboRidget005.class.getSimpleName());

			UIControlsFactory.createLabel(shell, "Combo (autocompletion):"); //$NON-NLS-1$
			final CompletionCombo combo = UIControlsFactory.createCompletionCombo(shell);
			GridDataFactory.fillDefaults().applyTo(combo);

			UIControlsFactory.createLabel(shell, "Selection:"); //$NON-NLS-1$
			final Label label = UIControlsFactory.createLabel(shell, ""); //$NON-NLS-1$
			GridDataFactory.fillDefaults().grab(true, false).applyTo(label);

			// ridgets

			final IComboRidget comboRidget = (IComboRidget) SwtRidgetFactory.createRidget(combo);
			final ListBean input = new ListBean("Aachen", "Athens", "Austin", "Arkansas", "Ashland", "London", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
					"Moskow", "New York", "Paris", "Portland", "Potsdam"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			comboRidget.bindToModel(input, ListBean.PROPERTY_VALUES, String.class, null, label, "text"); //$NON-NLS-1$
			comboRidget.updateFromModel();

			comboRidget.setMarkSelectionMismatch(true);

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
}
