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
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import org.eclipse.riena.beans.common.SuperHero;
import org.eclipse.riena.beans.common.SuperHeroFactory;
import org.eclipse.riena.beans.common.TypedComparator;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * A table ridget with sorting.
 */
public class SnippetTableRidget002 {

	public SnippetTableRidget002(final Shell shell) {
		final Table table = new Table(shell, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		final TableColumn[] columns = new TableColumn[3];
		for (int i = 0; i < 3; i++) {
			columns[i] = new TableColumn(table, SWT.LEFT);
		}
		final TableColumnLayout layout = new TableColumnLayout();
		layout.setColumnData(columns[0], new ColumnWeightData(125, 125));
		layout.setColumnData(columns[1], new ColumnWeightData(125, 125));
		layout.setColumnData(columns[2], new ColumnWeightData(100, 100));
		shell.setLayout(layout);

		final ITableRidget tableRidget = (ITableRidget) SwtRidgetFactory.createRidget(table);
		final String[] columnPropertyNames = { "pseudonym", "name", "appearance" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
		final String[] columnHeaders = { "Pseudonym", "Name", "First appearance" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
		final IObservableList input = new WritableList(SuperHeroFactory.createInput(), SuperHero.class);
		tableRidget.bindToModel(input, SuperHero.class, columnPropertyNames, columnHeaders);
		tableRidget.setComparator(0, new TypedComparator<String>());
		tableRidget.setColumnSortable(1, false);
		tableRidget.setComparator(2, new TypedComparator<Integer>());
		tableRidget.updateFromModel();

	}

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = UIControlsFactory.createShell(display);
			shell.setText(SnippetTableRidget002.class.getSimpleName());
			new SnippetTableRidget002(shell);
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
