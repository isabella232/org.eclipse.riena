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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import org.eclipse.riena.beans.common.AbstractBean;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.swt.ColumnFormatter;
import org.eclipse.riena.ui.ridgets.swt.DateColumnFormatter;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Demonstrates using a custom {@link ColumnFormatter} to provide
 * date-formatting, background- and foreground colors for a column.
 */
public class SnippetTableRidget003 {

	public SnippetTableRidget003(final Shell shell) {
		final Table table = new Table(shell, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		final TableColumn[] columns = new TableColumn[2];
		for (int i = 0; i < columns.length; i++) {
			columns[i] = new TableColumn(table, SWT.LEFT);
		}
		final TableColumnLayout layout = new TableColumnLayout();
		for (final TableColumn column : columns) {
			layout.setColumnData(column, new ColumnWeightData(50, 200));
		}
		shell.setLayout(layout);

		final ITableRidget tableRidget = (ITableRidget) SwtRidgetFactory.createRidget(table);
		final String[] columnPropertyNames = { "description", "date" }; //$NON-NLS-1$ //$NON-NLS-2$
		final String[] columnHeaders = { "Description", "Date" }; //$NON-NLS-1$ //$NON-NLS-2$
		final List<Holiday> input = createInput();
		tableRidget.setColumnFormatter(1, new DateColumnFormatter("MM/dd/yyyy") { //$NON-NLS-1$
					@Override
					protected Date getDate(final Object element) {
						return ((Holiday) element).getDate();
					}

					@Override
					public Color getForeground(final Object element) {
						return shell.getDisplay().getSystemColor(SWT.COLOR_RED);
					}
				});
		tableRidget.bindToModel(new WritableList(input, Holiday.class), Holiday.class, columnPropertyNames,
				columnHeaders);
		tableRidget.updateFromModel();
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = UIControlsFactory.createShell(display);
			shell.setText(SnippetTableRidget003.class.getSimpleName());
			new SnippetTableRidget003(shell);
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

	// helping methods
	//////////////////

	private List<Holiday> createInput() {
		final List<Holiday> result = new ArrayList<Holiday>(7);
		result.add(new Holiday("New Year's Day", createDate(2009, 1, 1))); //$NON-NLS-1$
		result.add(new Holiday("Martin Luther King Day", createDate(2009, 1, 19))); //$NON-NLS-1$
		result.add(new Holiday("Memorial Day", createDate(2009, 5, 25))); //$NON-NLS-1$
		result.add(new Holiday("Independence Day", createDate(2009, 7, 4))); //$NON-NLS-1$
		result.add(new Holiday("Labor Day", createDate(2009, 9, 7))); //$NON-NLS-1$
		result.add(new Holiday("Thanksgiving Day", createDate(2009, 11, 26))); //$NON-NLS-1$
		result.add(new Holiday("Christmas Eve", createDate(2009, 12, 24))); //$NON-NLS-1$
		result.add(new Holiday("Christmas Day", createDate(2009, 12, 25))); //$NON-NLS-1$
		return result;
	}

	private Date createDate(final int year, final int month, final int day) {
		final Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(year, month - 1, day); // month value is 0-based
		return calendar.getTime();
	}

	// helping classes
	//////////////////

	/**
	 * Holiday bean with a description and a date.
	 */
	private static final class Holiday extends AbstractBean {
		private final String descr;
		private final Date date;

		public Holiday(final String descr, final Date date) {
			this.descr = descr;
			this.date = date;
		}

		@SuppressWarnings("unused")
		public String getDescription() {
			return descr;
		}

		public Date getDate() {
			return date;
		}
	}

}
