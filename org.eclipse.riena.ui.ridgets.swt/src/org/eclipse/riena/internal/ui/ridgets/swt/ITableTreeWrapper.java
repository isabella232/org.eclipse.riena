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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;

/**
 * The wrapper for SWT {@link Table} and SWT {@link Tree} provides a couple of
 * common methods.
 */
public interface ITableTreeWrapper {

	/**
	 * Returns the control (table or tree) that is wrapped.
	 * 
	 * @return wrapped control
	 */
	Composite getControl();

	/**
	 * Returns the column at the given, zero-relative index in the table/tree.
	 * Throws an exception if the index is out of range. Columns are returned in
	 * the order that they were created.
	 * 
	 * @param index
	 *            the index of the column to return
	 * @return the column at the given index
	 */
	Widget getColumn(int index);

	/**
	 * Returns the number of columns contained in the table/tree.
	 * 
	 * @return the number of columns
	 */
	int getColumnCount();

	/**
	 * Returns the number of items contained in the table/tree.
	 * 
	 * @return the number of items
	 */
	int getItemCount();

	/**
	 * Returns the item at the given point in the table or null if no such item
	 * exists. The point is in the coordinate system of the table.
	 * 
	 * @param point
	 *            the point used to locate the item
	 * @return the item at the given point
	 */
	Item getItem(final Point point);

	/**
	 * Sets the width of a column.
	 * 
	 * @param columnIndex
	 *            the index of the column
	 * @param width
	 *            the new width
	 */
	void setWidth(int columnIndex, int width);

	/**
	 * Sets whether a column is resizable or not.
	 * 
	 * @param columnIndex
	 *            the index of the column
	 * @param resizable
	 *            {@code true} column is resizable; otherwise {@code false}
	 */
	void setResizable(int columnIndex, boolean resizable);

}
