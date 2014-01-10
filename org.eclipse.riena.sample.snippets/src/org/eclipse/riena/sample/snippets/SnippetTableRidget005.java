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
import org.eclipse.riena.ui.ridgets.listener.ISelectionListener;
import org.eclipse.riena.ui.ridgets.listener.SelectionEvent;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Demonstrates binding a table ridget's current selection to a model, and
 * displaying the model state in a label through another binding.
 * 
 * @see SnippetTableRidget006
 */
public class SnippetTableRidget005 {

	public DayPojo currentSelection = null;

	public SnippetTableRidget005(final Shell shell) {
		// UI for table
		GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 10).equalWidth(false).spacing(20, 10).applyTo(shell);

		final Composite tableComposite = UIControlsFactory.createComposite(shell, SWT.NONE);
		final Table table = UIControlsFactory.createTable(tableComposite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		GridDataFactory.fillDefaults().grab(true, true).hint(400, 200).applyTo(tableComposite);

		// Output labels for displaying model state
		UIControlsFactory.createLabel(shell, "The English name of the selected day is:"); //$NON-NLS-1$
		final Label outputValueLabel = UIControlsFactory.createLabel(shell, "[nothing selected]"); //$NON-NLS-1$
		outputValueLabel.setBackground(outputValueLabel.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridDataFactory.fillDefaults().grab(true, false).applyTo(outputValueLabel);

		// Table Ridget
		final ITableRidget tableRidget = (ITableRidget) SwtRidgetFactory.createRidget(table);
		final String[] columnPropertyNames = { "english", "german", "french", "spanish", "italian" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		final String[] columnHeaders = { "English", "German", "French", "Spanish", "Italian" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		final IObservableList input = new WritableList(DayPojo.createWeek(), DayPojo.class);
		tableRidget.bindToModel(input, DayPojo.class, columnPropertyNames, columnHeaders);
		tableRidget.updateFromModel();

		tableRidget.bindSingleSelectionToModel(this, "currentSelection"); //$NON-NLS-1$

		// Label Ridget
		final ILabelRidget outputRidget = (ILabelRidget) SwtRidgetFactory.createRidget(outputValueLabel);
		// use "currentSelection" to bind the label to the whole MyNode object:  
		outputRidget.bindToModel(this, "currentSelectionEnglish"); //$NON-NLS-1$

		tableRidget.addSelectionListener(new ISelectionListener() {
			public void ridgetSelected(final SelectionEvent event) {
				outputRidget.updateFromModel();
			}
		});
	}

	public DayPojo getCurrentSelection() {
		return currentSelection;
	}

	public void setCurrentSelection(final DayPojo currentSelection) {
		this.currentSelection = currentSelection;
	}

	public String getCurrentSelectionEnglish() {
		return currentSelection.getEnglish();
	}

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = UIControlsFactory.createShell(display);
			shell.setText(SnippetTableRidget005.class.getSimpleName());
			new SnippetTableRidget005(shell);
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
