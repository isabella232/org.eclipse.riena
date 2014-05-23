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
package org.eclipse.riena.ui.swt.layout;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Widget;

/**
 * DpiTableColumnLayout is a (almost) copy of the class {@linkplain TableColumnLayout}.<br>
 * DpiTableColumnLayout supports DPI dependent values.
 * 
 * @since 6.0
 */
public class DpiTableColumnLayout extends AbstractDpiColumnLayout {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.layout.AbstractColumnLayout#getColumnCount(org.eclipse.swt.widgets.Scrollable)
	 */
	@Override
	protected int getColumnCount(final Scrollable tableTree) {
		return ((Table) tableTree).getColumnCount();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.layout.AbstractColumnLayout#setColumnWidths(org.eclipse.swt.widgets.Scrollable, int[])
	 */
	@Override
	protected void setColumnWidths(final Scrollable tableTree, final int[] widths) {
		final TableColumn[] columns = ((Table) tableTree).getColumns();
		for (int i = 0; i < widths.length; i++) {
			columns[i].setWidth(widths[i]);
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.layout.AbstractColumnLayout#getLayoutData(org.eclipse.swt.widgets.Scrollable, int)
	 */
	@Override
	protected ColumnLayoutData getLayoutData(final Scrollable tableTree, final int columnIndex) {
		final TableColumn column = ((Table) tableTree).getColumn(columnIndex);
		return (ColumnLayoutData) column.getData(LAYOUT_DATA);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.layout.AbstractColumnLayout#updateColumnData(org.eclipse.swt.widgets.Widget)
	 */
	@Override
	protected void updateColumnData(final Widget column) {
		final TableColumn tColumn = (TableColumn) column;
		final Table t = tColumn.getParent();

		if (!Util.isGtk() || t.getColumn(t.getColumnCount() - 1) != tColumn) {
			tColumn.setData(LAYOUT_DATA, new ColumnPixelData(tColumn.getWidth()));
			layout(t.getParent(), true);
		}
	}
}
