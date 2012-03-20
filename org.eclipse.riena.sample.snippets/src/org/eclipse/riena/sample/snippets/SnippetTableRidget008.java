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

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import org.eclipse.riena.beans.common.SuperHero;
import org.eclipse.riena.beans.common.SuperHeroFactory;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.listener.ClickEvent;
import org.eclipse.riena.ui.ridgets.listener.IClickListener;
import org.eclipse.riena.ui.ridgets.swt.ColumnFormatter;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Demostrates how to use an {@link IClickListener} to react to a click of a
 * specific column.
 */
public class SnippetTableRidget008 {

	public SnippetTableRidget008(final Shell shell) {
		final Composite compTable = UIControlsFactory.createComposite(shell);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(compTable);
		final Table table = UIControlsFactory.createTable(compTable, SWT.SINGLE | SWT.FULL_SELECTION);

		final Label lblMessage = UIControlsFactory.createLabel(shell,
				"Click on a checkbox to toggle that row's the state", SWT.CENTER); //$NON-NLS-1$
		GridDataFactory.fillDefaults().applyTo(lblMessage);

		final ITableRidget tableRidget = (ITableRidget) SwtRidgetFactory.createRidget(table);
		final String[] columnPropertyNames = { "pseudonym", "name", "active" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
		final String[] columnHeaders = { "Pseudonym", "Name", "Active" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
		final IObservableList input = new WritableList(SuperHeroFactory.createInput(), SuperHero.class);
		tableRidget.bindToModel(input, SuperHero.class, columnPropertyNames, columnHeaders);
		tableRidget.setMoveableColumns(true);

		final ColumnFormatter bgFormatter = new ColumnFormatter() {
			@Override
			public Color getForeground(final Object element) {
				final SuperHero hero = (SuperHero) element;
				final int colorId = hero.getActive() ? SWT.COLOR_BLACK : SWT.COLOR_GRAY;
				return table.getDisplay().getSystemColor(colorId);
			}
		};
		tableRidget.setColumnFormatter(0, bgFormatter);
		tableRidget.setColumnFormatter(1, bgFormatter);

		tableRidget.setColumnFormatter(2, new ColumnFormatter() {
			@Override
			public String getText(final Object element) {
				return ""; //$NON-NLS-1$
			}
		});

		tableRidget.addClickListener(new IClickListener() {
			public void callback(final ClickEvent event) {
				if (event.getColumnIndex() == 2 && event.getButton() == 1) {
					final SuperHero selection = (SuperHero) event.getRow();
					selection.setActive(!selection.getActive());
					tableRidget.refresh(selection);
				}
			}
		});

		tableRidget.updateFromModel();
	}

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = UIControlsFactory.createShell(display);
			shell.setText(SnippetTableRidget008.class.getSimpleName());
			GridLayoutFactory.fillDefaults().applyTo(shell);

			new SnippetTableRidget008(shell);
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
