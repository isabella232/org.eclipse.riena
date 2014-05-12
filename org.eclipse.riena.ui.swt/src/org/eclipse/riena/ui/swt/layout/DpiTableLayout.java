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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * DpiTableLayout is a copy/extension of the class {@linkplain TableLayout}.<br>
 * DpiTableLayout supports DPI dependent values.
 * <p>
 * <b>!!! This class has not been tested so far !!!</b>
 * 
 * @since 6.0
 */
public class DpiTableLayout extends TableLayout {

	/**
	 * The number of extra pixels taken as horizontal trim by the table column. To ensure there are N pixels available for the content of the column, assign
	 * N+COLUMN_TRIM for the column width.
	 * 
	 * @since 3.1
	 */
	private static int COLUMN_TRIM;

	static {
		if (Util.isWindows()) {
			COLUMN_TRIM = 4;
		} else if (Util.isMac()) {
			COLUMN_TRIM = 24;
		} else {
			COLUMN_TRIM = 3;
		}
	}

	/**
	 * The list of column layout data (element type: <code>ColumnLayoutData</code>).
	 */
	private final List<ColumnLayoutData> columns = new ArrayList<ColumnLayoutData>();

	/**
	 * Indicates whether <code>layout</code> has yet to be called.
	 */
	private boolean firstTime = true;

	/**
	 * Adds a new column of data to this table layout.
	 * 
	 * @param data
	 *            the column layout data
	 */
	@Override
	public void addColumnData(final ColumnLayoutData data) {
		columns.add(data);
	}

	@Override
	public Point computeSize(final Composite c, final int wHint, final int hHint, final boolean flush) {
		if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
			return new Point(wHint, hHint);
		}

		final Table table = (Table) c;
		// To avoid recursions.
		table.setLayout(null);
		// Use native layout algorithm
		final Point result = table.computeSize(wHint, hHint, flush);
		table.setLayout(this);

		int width = 0;
		final int size = columns.size();
		for (int i = 0; i < size; ++i) {
			final ColumnLayoutData layoutData = columns.get(i);
			if (layoutData instanceof ColumnPixelData) {
				final ColumnPixelData col = (ColumnPixelData) layoutData;
				width += col.width;
				if (col.addTrim) {
					width += COLUMN_TRIM;
				}
			} else if (layoutData instanceof ColumnWeightData) {
				final ColumnWeightData col = (ColumnWeightData) layoutData;
				width += col.minimumWidth;
			} else {
				Assert.isTrue(false, "Unknown column layout data");//$NON-NLS-1$
			}
		}
		width = SwtUtilities.convertXToDpi(width);
		if (width > result.x) {
			result.x = width;
		}
		return result;
	}

	@Override
	public void layout(final Composite c, final boolean flush) {
		// Only do initial layout. Trying to maintain proportions when resizing
		// is too hard,
		// causes lots of widget flicker, causes scroll bars to appear and
		// occasionally stick around (on Windows),
		// requires hooking column resizing as well, and may not be what the
		// user wants anyway.
		if (!firstTime) {
			return;
		}

		final int width = c.getClientArea().width;

		// XXX: Layout is being called with an invalid value the first time
		// it is being called on Linux. This method resets the
		// Layout to null so we make sure we run it only when
		// the value is OK.
		if (width <= 1) {
			return;
		}

		final Item[] tableColumns = getColumns(c);
		final int size = Math.min(columns.size(), tableColumns.length);
		final int[] widths = new int[size];
		int fixedWidth = 0;
		int numberOfWeightColumns = 0;
		int totalWeight = 0;

		// First calc space occupied by fixed columns
		for (int i = 0; i < size; i++) {
			final ColumnLayoutData col = columns.get(i);
			if (col instanceof ColumnPixelData) {
				final ColumnPixelData cpd = (ColumnPixelData) col;
				int pixels = cpd.width;
				if (cpd.addTrim) {
					pixels += COLUMN_TRIM;
				}
				widths[i] = pixels;
				fixedWidth += pixels;
			} else if (col instanceof ColumnWeightData) {
				final ColumnWeightData cw = (ColumnWeightData) col;
				numberOfWeightColumns++;
				// first time, use the weight specified by the column data,
				// otherwise use the actual width as the weight
				// int weight = firstTime ? cw.weight :
				// tableColumns[i].getWidth();
				final int weight = cw.weight;
				totalWeight += weight;
			} else {
				Assert.isTrue(false, "Unknown column layout data");//$NON-NLS-1$
			}
		}

		// Do we have columns that have a weight
		if (numberOfWeightColumns > 0) {
			// Now distribute the rest to the columns with weight.
			final int rest = width - fixedWidth;
			int totalDistributed = 0;
			for (int i = 0; i < size; ++i) {
				final ColumnLayoutData col = columns.get(i);
				if (col instanceof ColumnWeightData) {
					final ColumnWeightData cw = (ColumnWeightData) col;
					// calculate weight as above
					// int weight = firstTime ? cw.weight :
					// tableColumns[i].getWidth();
					final int weight = cw.weight;
					int pixels = totalWeight == 0 ? 0 : weight * rest / totalWeight;
					if (pixels < cw.minimumWidth) {
						pixels = cw.minimumWidth;
					}
					totalDistributed += pixels;
					widths[i] = pixels;
				}
			}

			// Distribute any remaining pixels to columns with weight.
			int diff = rest - totalDistributed;
			for (int i = 0; diff > 0; ++i) {
				if (i == size) {
					i = 0;
				}
				final ColumnLayoutData col = columns.get(i);
				if (col instanceof ColumnWeightData) {
					++widths[i];
					--diff;
				}
			}
		}

		firstTime = false;

		for (int i = 0; i < size; i++) {
			setWidth(tableColumns[i], widths[i]);
		}
	}

	/**
	 * Set the width of the item.
	 * 
	 * @param item
	 * @param width
	 */
	private void setWidth(final Item item, final int width) {
		final int dpiWidth = SwtUtilities.convertXToDpi(width);
		if (item instanceof TreeColumn) {
			((TreeColumn) item).setWidth(dpiWidth);
		} else {
			((TableColumn) item).setWidth(dpiWidth);
		}

	}

	/**
	 * Return the columns for the receiver.
	 * 
	 * @param composite
	 * @return Item[]
	 */
	private Item[] getColumns(final Composite composite) {
		if (composite instanceof Tree) {
			return ((Tree) composite).getColumns();
		}
		return ((Table) composite).getColumns();
	}

}
