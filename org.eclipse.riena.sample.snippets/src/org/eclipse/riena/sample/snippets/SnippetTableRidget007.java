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

import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import org.eclipse.riena.beans.common.SuperHero;
import org.eclipse.riena.beans.common.SuperHeroFactory;
import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.ui.core.marker.RowErrorMessageMarker;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Demostrates how to use a {@link RowErrorMessageMarker} to mark errors in the
 * rows of a table ridget.
 */
public class SnippetTableRidget007 {

	public SnippetTableRidget007(final Shell shell) {
		final Composite compTable = UIControlsFactory.createComposite(shell);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(compTable);
		final Table table = UIControlsFactory.createTable(compTable, SWT.SINGLE | SWT.FULL_SELECTION);

		final Composite compButton = UIControlsFactory.createComposite(shell);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(compButton);
		GridLayoutFactory.swtDefaults().numColumns(3).applyTo(compButton);
		final Button btnMark = UIControlsFactory.createButton(compButton, "&Mark"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.FILL).grab(true, false).applyTo(btnMark);
		final Button btnUnmark = UIControlsFactory.createButton(compButton, "&Unmark"); //$NON-NLS-1$
		final Button btnLines = UIControlsFactory.createButton(compButton, "Toggle &Lines"); //$NON-NLS-1$

		final ITableRidget tableRidget = (ITableRidget) SwtRidgetFactory.createRidget(table);
		final String[] columnPropertyNames = { "pseudonym", "name", "appearance" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
		final String[] columnHeaders = { "Pseudonym", "Name", "First appearance" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
		final IObservableList input = new WritableList(SuperHeroFactory.createInput(), SuperHero.class);
		tableRidget.bindToModel(input, SuperHero.class, columnPropertyNames, columnHeaders);
		tableRidget.updateFromModel();
		tableRidget.setToolTipText("The ORIGINAL table tooltip!"); //$NON-NLS-1$

		final IActionRidget ridgetMark = (IActionRidget) SwtRidgetFactory.createRidget(btnMark);
		ridgetMark.addListener(new IActionListener() {
			public void callback() {
				final List<Object> selection = tableRidget.getSelection();
				if (!selection.isEmpty()) {
					final Object value = selection.get(0);
					System.out.println("mark: " + value); //$NON-NLS-1$
					final IMarker marker = new RowErrorMessageMarker("There is an error with: " + value, value); //$NON-NLS-1$
					tableRidget.addMarker(marker);
				}
			}
		});

		final IActionRidget ridgetUnmark = (IActionRidget) SwtRidgetFactory.createRidget(btnUnmark);
		ridgetUnmark.addListener(new IActionListener() {
			public void callback() {
				final List<Object> selection = tableRidget.getSelection();
				if (!selection.isEmpty()) {
					final Object value = selection.get(0);
					System.out.println("unmark: " + value); //$NON-NLS-1$
					final IMarker marker = new RowErrorMessageMarker(null, value);
					tableRidget.removeMarker(marker);
				}
			}
		});

		final IActionRidget ridgetLines = (IActionRidget) SwtRidgetFactory.createRidget(btnLines);
		ridgetLines.addListener(new IActionListener() {
			public void callback() {
				table.setLinesVisible(!table.getLinesVisible());
			}
		});
	}

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = UIControlsFactory.createShell(display);
			shell.setText(SnippetTableRidget007.class.getSimpleName());
			GridLayoutFactory.fillDefaults().applyTo(shell);

			new SnippetTableRidget007(shell);
			shell.setSize(400, 250);
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
