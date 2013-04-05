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
package org.eclipse.riena.internal.ui.ridgets.swt.optional;

import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.graphics.Point;

import org.eclipse.riena.internal.ui.ridgets.swt.AbstractTableTreeWrapper;

/**
 * Wrapper of a SWT/Nebula {@link Grid}.
 */
public class GridWrapper extends AbstractTableTreeWrapper {

	/**
	 * Creates a new wrapper for the given {@link Grid}.
	 * 
	 * @param control
	 *            grid that will be wrapped
	 */
	public GridWrapper(final Grid control) {
		super(control);
	}

	@Override
	public Grid getControl() {
		return (Grid) super.getControl();
	}

	public int getColumnCount() {
		return getControl().getColumnCount();
	}

	public GridColumn getColumn(final int index) {
		return getControl().getColumn(index);
	}

	public void setWidth(final int columnIndex, final int width) {
		getColumn(columnIndex).setWidth(width);
	}

	public void setResizable(final int columnIndex, final boolean resizable) {
		getColumn(columnIndex).setResizeable(resizable);
	}

	public int getItemCount() {
		return getControl().getItemCount();
	}

	public GridItem getItem(final Point point) {
		return getControl().getItem(point);
	}

}
