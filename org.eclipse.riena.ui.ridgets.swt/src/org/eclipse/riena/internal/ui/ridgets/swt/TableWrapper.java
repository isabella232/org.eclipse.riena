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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * Wrapper of a SWT {@link Table}.
 */
public class TableWrapper extends AbstractTableTreeWrapper {

	/**
	 * Creates a new wrapper for the given SWT {@link Table}.
	 * 
	 * @param control
	 *            table that will be wrapped
	 */
	public TableWrapper(final Table control) {
		super(control);
	}

	@Override
	public Table getControl() {
		return (Table) super.getControl();
	}

	public int getColumnCount() {
		return getControl().getColumnCount();
	}

	public TableColumn getColumn(final int index) {
		return getControl().getColumn(index);
	}

	public void setWidth(final int columnIndex, final int width) {
		getColumn(columnIndex).setWidth(width);
	}

	public void setResizable(final int columnIndex, final boolean resizable) {
		getColumn(columnIndex).setResizable(resizable);
	}

	public int getItemCount() {
		return getControl().getItemCount();
	}

	public TableItem getItem(final Point point) {
		return getControl().getItem(point);
	}

}
