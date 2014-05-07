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

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.layout.AbstractColumnLayout;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * AbstractDpiColumnLayout is a copy/extension of the class {@linkplain AbstractColumnLayout}.<br>
 * AbstractDpiColumnLayout supports DPI dependent values.
 */
public abstract class AbstractDpiColumnLayout extends AbstractColumnLayout {

	private boolean inupdateMode = false;

	private boolean relayout = true;

	private final Listener resizeListener = new Listener() {

		public void handleEvent(final Event event) {
			if (!inupdateMode) {
				updateColumnData(event.widget);
			}
		}

	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setColumnData(final Widget column, final ColumnLayoutData data) {

		if (column.getData(LAYOUT_DATA) == null) {
			column.addListener(SWT.Resize, resizeListener);
		}

		column.setData(LAYOUT_DATA, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Point computeSize(final Composite composite, final int wHint, final int hHint, final boolean flushCache) {
		return computeTableTreeSize(getControl(composite), wHint, hHint);
	}

	/**
	 * Compute the size of the table or tree based on the ColumnLayoutData and the width and height hint.
	 * 
	 * @param scrollable
	 *            the widget to compute
	 * @param wHint
	 *            the width hint
	 * @param hHint
	 *            the height hint
	 * @return Point where x is the width and y is the height
	 */
	private Point computeTableTreeSize(final Scrollable scrollable, final int wHint, final int hHint) {
		final Point result = scrollable.computeSize(wHint, hHint);

		int width = 0;
		final int size = getColumnCount(scrollable);
		for (int i = 0; i < size; ++i) {
			final ColumnLayoutData layoutData = getLayoutData(scrollable, i);
			if (layoutData instanceof ColumnPixelData) {
				final ColumnPixelData col = (ColumnPixelData) layoutData;
				width += col.width;
				if (col.addTrim) {
					width += getColumnTrim();
				}
			} else if (layoutData instanceof ColumnWeightData) {
				final ColumnWeightData col = (ColumnWeightData) layoutData;
				width += col.minimumWidth;
			} else {
				Assert.isTrue(false, "Unknown column layout data"); //$NON-NLS-1$
			}
		}
		width = SwtUtilities.convertXToDpi(width);
		if (width > result.x) {
			result.x = width;
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void layout(final Composite composite, final boolean flushCache) {
		final Rectangle area = composite.getClientArea();
		final Scrollable table = getControl(composite);
		final int tableWidth = table.getSize().x;
		final int trim = computeTrim(area, table, tableWidth);
		final int width = Math.max(0, area.width - trim);

		if (width > 1) {
			layoutTableTree(table, width, area, tableWidth < area.width);
		}

		// For the first time we need to relayout because Scrollbars are not
		// calculate appropriately
		if (relayout) {
			relayout = false;
			composite.layout();
		}
	}

	/**
	 * Layout the scrollable based on the supplied width and area. Only increase the size of the scrollable if increase is <code>true</code>.
	 * 
	 * @param scrollable
	 * @param width
	 * @param area
	 * @param increase
	 */
	private void layoutTableTree(final Scrollable scrollable, final int width, final Rectangle area, final boolean increase) {
		final int numberOfColumns = getColumnCount(scrollable);
		final int[] widths = new int[numberOfColumns];

		final int[] weightColumnIndices = new int[numberOfColumns];
		int numberOfWeightColumns = 0;

		int fixedWidth = 0;
		int totalWeight = 0;

		// First calc space occupied by fixed columns
		for (int i = 0; i < numberOfColumns; i++) {
			final ColumnLayoutData col = getLayoutData(scrollable, i);
			if (col instanceof ColumnPixelData) {
				final ColumnPixelData cpd = (ColumnPixelData) col;
				int pixels = cpd.width;
				if (cpd.addTrim) {
					pixels += getColumnTrim();
				}
				pixels = SwtUtilities.convertXToDpi(pixels);
				widths[i] = pixels;
				fixedWidth += pixels;
			} else if (col instanceof ColumnWeightData) {
				final ColumnWeightData cw = (ColumnWeightData) col;
				weightColumnIndices[numberOfWeightColumns] = i;
				numberOfWeightColumns++;
				totalWeight += cw.weight;
			} else {
				Assert.isTrue(false, "Unknown column layout data"); //$NON-NLS-1$
			}
		}

		boolean recalculate;
		do {
			recalculate = false;
			for (int i = 0; i < numberOfWeightColumns; i++) {
				final int colIndex = weightColumnIndices[i];
				final ColumnWeightData cw = (ColumnWeightData) getLayoutData(scrollable, colIndex);
				int minWidth = cw.minimumWidth;
				minWidth = SwtUtilities.convertXToDpi(minWidth);
				final int allowedWidth = totalWeight == 0 ? 0 : (width - fixedWidth) * cw.weight / totalWeight;
				if (allowedWidth < minWidth) {
					/*
					 * if the width assigned by weight is less than the minimum, then treat this column as fixed, remove it from weight calculations, and
					 * recalculate other weights.
					 */
					numberOfWeightColumns--;
					totalWeight -= cw.weight;
					fixedWidth += minWidth;
					widths[colIndex] = minWidth;
					System.arraycopy(weightColumnIndices, i + 1, weightColumnIndices, i, numberOfWeightColumns - i);
					recalculate = true;
					break;
				}
				widths[colIndex] = allowedWidth;
			}
		} while (recalculate);

		if (increase) {
			scrollable.setSize(area.width, area.height);
		}

		inupdateMode = true;
		setColumnWidths(scrollable, widths);
		scrollable.update();
		inupdateMode = false;

		if (!increase) {
			scrollable.setSize(area.width, area.height);
		}
	}

	/**
	 * Compute the area required for trim.
	 * 
	 * @param area
	 * @param scrollable
	 * @param currentWidth
	 * @return int
	 */
	private int computeTrim(final Rectangle area, final Scrollable scrollable, final int currentWidth) {
		int trim;

		if (currentWidth > 1) {
			trim = currentWidth - scrollable.getClientArea().width;
		} else {
			// initially, the table has no extend and no client area - use the
			// border with
			// plus some padding as educated guess
			trim = 2 * scrollable.getBorderWidth() + 1;
		}

		return trim;
	}

	/**
	 * Get the control being laid out.
	 * 
	 * @param composite
	 *            the composite with the layout
	 * @return {@link Scrollable}
	 */
	private Scrollable getControl(final Composite composite) {
		return (Scrollable) composite.getChildren()[0];
	}

}
