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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.swt.DefaultRealm;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Designating a 'placeholder' item that is equivalent to no selection.
 */
public class SnippetComboRidget003 {

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		new DefaultRealm();

		try {
			final Shell shell = UIControlsFactory.createShell(display);
			shell.setText(SnippetComboRidget003.class.getSimpleName());
			GridLayoutFactory.swtDefaults().numColumns(2).spacing(5, 5).applyTo(shell);

			UIControlsFactory.createLabel(shell, "CCombo:"); //$NON-NLS-1$
			final CCombo ccombo = UIControlsFactory.createCCombo(shell);

			UIControlsFactory.createLabel(shell, "Selection:"); //$NON-NLS-1$
			final Label lblSelection = UIControlsFactory.createLabel(shell, ""); //$NON-NLS-1$

			final Button button = UIControlsFactory.createButton(shell, "&Reset"); //$NON-NLS-1$
			GridDataFactory.fillDefaults().span(2, 1).applyTo(button);

			// data model
			final String emptySelection = "<no selection>"; //$NON-NLS-1$
			final IObservableList input = createInput(emptySelection);

			final IComboRidget ccomboRidget = (IComboRidget) SwtRidgetFactory.createRidget(ccombo);
			ccomboRidget.setMandatory(true);
			ccomboRidget.setEmptySelectionItem(emptySelection);
			ccomboRidget.bindToModel(input, String.class, null, SWTObservables.observeText(lblSelection));
			ccomboRidget.updateFromModel();

			final IActionRidget actionRidget = (IActionRidget) SwtRidgetFactory.createRidget(button);
			actionRidget.addListener(new IActionListener() {
				public void callback() {
					ccomboRidget.setSelection(emptySelection);
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

	private static IObservableList createInput(final String emptySelection) {
		final List<String> days = new ArrayList<String>();
		final String[] values = new String[] { emptySelection,
				"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
				"Sunday" }; //$NON-NLS-1$
		for (final String day : values) {
			days.add(day);
		}
		return new WritableList(days, String.class);
	}

}
