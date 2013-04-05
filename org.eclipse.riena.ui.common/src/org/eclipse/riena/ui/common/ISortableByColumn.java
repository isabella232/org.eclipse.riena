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
package org.eclipse.riena.ui.common;

/**
 * This interface adds support sorting by column and ascending / descending
 * sorting.
 */
public interface ISortableByColumn {

	/**
	 * Property holding the sortability state of the individual columns:
	 * sortable or not sortable.
	 * <p>
	 * Implementors firing a property change must supply the index value of the
	 * column whose sortability was changed. The index is 0-based. The old value
	 * is null.
	 */
	String PROPERTY_COLUMN_SORTABILITY = "columnSortability"; //$NON-NLS-1$

	/**
	 * Property holding the column by which the table is currently sorted.
	 * <p>
	 * Implementors firing a property change must supply the old and new index
	 * value of the sorted column. The index is 0-based.
	 */
	String PROPERTY_SORTED_COLUMN = "sortedColumn"; //$NON-NLS-1$

	/**
	 * Property indicating whether the order in the sorted column is ascending
	 * or descending.
	 * <p>
	 * Implementors firing a property change must supply the old and new value.
	 */
	String PROPERTY_SORT_ASCENDING = "sortAscending"; //$NON-NLS-1$

	/**
	 * Returns whether the currently sorted column is in ascending order.
	 * 
	 * @return <code>true</code> if and only if the currently sorted column is
	 *         in ascending order. Otherwise <code>false</code>
	 */
	boolean isSortedAscending();

	/**
	 * Set the sort order of the currently sorted column.
	 * <p>
	 * Fires a {@link #PROPERTY_SORT_ASCENDING} event.
	 * 
	 * @param ascending
	 *            <code>true</code> for ascending order, <code>false</code> for
	 *            descending order.
	 */
	void setSortedAscending(boolean ascending);

	/**
	 * Returns index of the currently sorted column or -1 if no column is
	 * sorted.
	 * 
	 * @return the 0-based index of the currently sorted column or
	 *         <code>-1</code> if no column is currently sorted.
	 */
	int getSortedColumn();

	/**
	 * Set the index of the column the table is sorted by. The sort order
	 * (ascending or descending) is determined by using the result of this
	 * objects {@link #isSortedAscending()} method.
	 * <p>
	 * Fires a {@link #PROPERTY_SORTED_COLUMN} event.
	 * 
	 * @param columnIndex
	 *            a columnIndex in the allowed range (-1 &le; columnIndex &lt;
	 *            numColumns). A value of -1 indicates that no column should be
	 *            sorted.
	 * @throws RuntimeException
	 *             if <code>columnIndex</code> is out of range
	 */
	void setSortedColumn(int columnIndex);

	/**
	 * Returns <code>true</code> if and only if the column at columnIndex is
	 * sortable. Otherwise <code>false</code>.
	 * <p>
	 * Implementations may differ in what exactly is assumed sortable(e.g
	 * implementors of the {@link Comparable} interface only)
	 * 
	 * @param columnIndex
	 *            a columnIndex in the allowed range (0 &le; columnIndex &lt;
	 *            numColumns)
	 * @return {@code true} if the column is sortable; otherwise {@code false}
	 * @throws RuntimeException
	 *             if <code>columnIndex</code> is out of range
	 */
	boolean isColumnSortable(int columnIndex);

	/**
	 * Set the sortability of column at columnIndex.
	 * <p>
	 * Implementations may differ in what exactly is assumed sortable (e.g
	 * implementors of the {@link Comparable} interface only). For example a
	 * sortable column may allows the user to change the sort order from the UI.
	 * Turning <em>off</em> the sortability of the currently sorted column does
	 * not change it's sort order.
	 * <p>
	 * Note that {@link #isColumnSortable(int)} is not required to return the
	 * value set using this method as implementations are free to choose the way
	 * how to determine column sortability.
	 * <p>
	 * Fires a {@link #PROPERTY_COLUMN_SORTABILITY} event.
	 * 
	 * @param columnIndex
	 *            a columnIndex in the allowed range (0 &le; columnIndex &lt;
	 *            numColumns)
	 * @param sortable
	 *            <code>true</code> if the column is sortable,
	 *            <code>false</code> otherwise
	 * @throws RuntimeException
	 *             if <code>columnIndex</code> is out of range
	 */
	void setColumnSortable(int columnIndex, boolean sortable);

}
