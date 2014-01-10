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

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import org.eclipse.riena.beans.common.DayPojo;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Demonstrates binding a table ridget's current selection to a model, with the
 * output label ridget's <tt>text</tt> property serving as the model.
 * 
 * @see SnippetTableRidget005
 */
public class SnippetTableRidget006 {

	public SnippetTableRidget006(final Shell shell) {
		// UI for table
		GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 10).equalWidth(false).spacing(20, 10).applyTo(shell);

		final Composite tableComposite = UIControlsFactory.createComposite(shell, SWT.NONE);
		final Table table = UIControlsFactory.createTable(tableComposite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		GridDataFactory.fillDefaults().grab(true, true).hint(400, 200).applyTo(tableComposite);

		// Output labels for displaying model state
		UIControlsFactory.createLabel(shell, "You selected:"); //$NON-NLS-1$
		final Label outputValueLabel = UIControlsFactory.createLabel(shell, "[nothing selected]"); //$NON-NLS-1$
		outputValueLabel.setBackground(outputValueLabel.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridDataFactory.fillDefaults().grab(true, false).applyTo(outputValueLabel);

		// Label Ridget
		final ILabelRidget outputRidget = (ILabelRidget) SwtRidgetFactory.createRidget(outputValueLabel);

		// Table Ridget
		final ITableRidget tableRidget = (ITableRidget) SwtRidgetFactory.createRidget(table);
		final String[] columnPropertyNames = { "english", "german", "french", "spanish", "italian" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		final String[] columnHeaders = { "English", "German", "French", "Spanish", "Italian" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		final IObservableList input = new WritableList(DayPojo.createWeek(), DayPojo.class);
		tableRidget.bindToModel(input, DayPojo.class, columnPropertyNames, columnHeaders);
		tableRidget.updateFromModel();

		// Bind table ridget to output ridget
		tableRidget.bindSingleSelectionToModel(outputRidget, ILabelRidget.PROPERTY_TEXT);
	}

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = UIControlsFactory.createShell(display);
			shell.setText(SnippetTableRidget006.class.getSimpleName());
			new SnippetTableRidget006(shell);
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