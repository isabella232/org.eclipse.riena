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
package org.eclipse.riena.sample.snippets.frombugs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import org.eclipse.riena.beans.common.AbstractBean;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;

/**
 * Demonstrates binding a table ridget to a model.
 */
public class Snippet301682 {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			shell.setLayout(new FillLayout());

			final Table table = new Table(shell, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			// TableColumn[] columns = new TableColumn[5];
			// for (int i = 0; i < 5; i++) {
			// columns[i] = new TableColumn(table, SWT.LEFT);
			// }
			// table.setLayout(new TableLayout()); // <========= ADDED THIS LINE

			final ITableRidget tableRidget = (ITableRidget) SwtRidgetFactory.createRidget(table);
			final String[] columnPropertyNames = { "english", "german", "french", "spain", "italian" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			final String[] columnHeaders = { "English", "German", "French", "Spain", "Italian" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			final List<MyNode> input = createInput();
			tableRidget.bindToModel(new WritableList(input, MyNode.class), MyNode.class, columnPropertyNames, columnHeaders);
			tableRidget.updateFromModel();

			tableRidget.setColumnWidths(new ColumnLayoutData[] { new ColumnWeightData(3), new ColumnWeightData(2), new ColumnWeightData(1),
					new ColumnWeightData(1), new ColumnWeightData(1) });

			shell.setSize(400, 400);
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

	private static List<MyNode> createInput() {

		final List<MyNode> nodes = new ArrayList<MyNode>(7);
		nodes.add(new MyNode("Monday", "Montag", "lundi", "lunes", "lunedě")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		nodes.add(new MyNode("Tuesday", "Dienstag", "mardi", "martes", "martedě")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		nodes.add(new MyNode("Wednesday", "Mittwoch", "mercredi", "miércoles", "mercoledě")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		nodes.add(new MyNode("Thursday", "Donnerstag", "jeudi", "jueves", "giovedě")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		nodes.add(new MyNode("Friday", "Freitag", "vendredi", "viernes", "venerdě")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		nodes.add(new MyNode("Saturday", "Samstag", "samedi", "sábado", "sabato")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		nodes.add(new MyNode("Sunday", "Sonntag", "dimanche", "domingo", "domenica")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		return nodes;

	}

	private static class MyNode extends AbstractBean {

		private final String english;
		private final String german;
		private final String french;
		private final String spain;
		private final String italian;

		public MyNode(final String english, final String german, final String french, final String spain, final String italian) {
			this.english = english;
			this.german = german;
			this.french = french;
			this.spain = spain;
			this.italian = italian;
		}

		@SuppressWarnings("unused")
		public String getEnglish() {
			return english;
		}

		@SuppressWarnings("unused")
		public String getGerman() {
			return german;
		}

		@SuppressWarnings("unused")
		public String getFrench() {
			return french;
		}

		@SuppressWarnings("unused")
		public String getSpain() {
			return spain;
		}

		@SuppressWarnings("unused")
		public String getItalian() {
			return italian;
		}

	}

}