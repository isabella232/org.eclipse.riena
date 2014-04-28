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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;

import org.eclipse.riena.internal.ui.swt.layout.DpiGridData;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * DpiGridLayout is a copy of the final class {@linkplain GridLayout}.<br>
 * DpiGridLayout supports DPI dependent values.
 */
public class DpiGridLayout extends Layout {

	/**
	 * numColumns specifies the number of cell columns in the layout. If numColumns has a value less than 1, the layout will not set the size and position of
	 * any controls.
	 * 
	 * The default value is 1.
	 */
	public int numColumns = 1;

	/**
	 * makeColumnsEqualWidth specifies whether all columns in the layout will be forced to have the same width.
	 * 
	 * The default value is false.
	 */
	public boolean makeColumnsEqualWidth = false;

	/**
	 * marginWidth specifies the number of pixels of horizontal margin that will be placed along the left and right edges of the layout.
	 * 
	 * The default value is 5.
	 */
	public int marginWidth = 5;

	/**
	 * marginHeight specifies the number of pixels of vertical margin that will be placed along the top and bottom edges of the layout.
	 * 
	 * The default value is 5.
	 */
	public int marginHeight = 5;

	/**
	 * marginLeft specifies the number of pixels of horizontal margin that will be placed along the left edge of the layout.
	 * 
	 * The default value is 0.
	 * 
	 * @since 3.1
	 */
	public int marginLeft = 0;

	/**
	 * marginTop specifies the number of pixels of vertical margin that will be placed along the top edge of the layout.
	 * 
	 * The default value is 0.
	 * 
	 * @since 3.1
	 */
	public int marginTop = 0;

	/**
	 * marginRight specifies the number of pixels of horizontal margin that will be placed along the right edge of the layout.
	 * 
	 * The default value is 0.
	 * 
	 * @since 3.1
	 */
	public int marginRight = 0;

	/**
	 * marginBottom specifies the number of pixels of vertical margin that will be placed along the bottom edge of the layout.
	 * 
	 * The default value is 0.
	 * 
	 * @since 3.1
	 */
	public int marginBottom = 0;

	/**
	 * horizontalSpacing specifies the number of pixels between the right edge of one cell and the left edge of its neighbouring cell to the right.
	 * 
	 * The default value is 5.
	 */
	public int horizontalSpacing = 5;

	/**
	 * verticalSpacing specifies the number of pixels between the bottom edge of one cell and the top edge of its neighbouring cell underneath.
	 * 
	 * The default value is 5.
	 */
	public int verticalSpacing = 5;

	/**
	 * Constructs a new instance of this class with a single column.
	 */
	public DpiGridLayout() {
	}

	/**
	 * Constructs a new instance of this class given the number of columns, and whether or not the columns should be forced to have the same width. If
	 * numColumns has a value less than 1, the layout will not set the size and position of any controls.
	 * 
	 * @param numColumns
	 *            the number of columns in the grid
	 * @param makeColumnsEqualWidth
	 *            whether or not the columns will have equal width
	 * 
	 * @since 2.0
	 */
	public DpiGridLayout(final int numColumns, final boolean makeColumnsEqualWidth) {
		this.numColumns = numColumns;
		this.makeColumnsEqualWidth = makeColumnsEqualWidth;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.swt.widgets.Layout#flushCache(org.eclipse.swt.widgets.Control)
	 */
	@Override
	protected boolean flushCache(final Control control) {
		final DpiGridData data = getDpiGridData(control);
		if (data != null) {
			data.flushCache();
		}
		return true;
	}

	DpiGridData getData(final Control[][] grid, final int row, final int column, final int rowCount, final int columnCount, final boolean first) {
		final Control control = grid[row][column];
		if (control != null) {
			if (control.getLayoutData() instanceof GridData) {
				final GridData gridData = (GridData) control.getLayoutData();
				control.setLayoutData(new DpiGridData(gridData));
			}
			final DpiGridData data = (DpiGridData) control.getLayoutData();
			final int hSpan = Math.max(1, Math.min(data.horizontalSpan, columnCount));
			final int vSpan = Math.max(1, data.verticalSpan);
			final int i = first ? row + vSpan - 1 : row - vSpan + 1;
			final int j = first ? column + hSpan - 1 : column - hSpan + 1;
			if (0 <= i && i < rowCount) {
				if (0 <= j && j < columnCount) {
					if (control == grid[i][j]) {
						return data;
					}
				}
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <i>One to one copy of the method {@code GridLayout.layout(Composite, boolean)}.</i>
	 * </p>
	 * 
	 * @see org.eclipse.swt.widgets.Layout#layout(org.eclipse.swt.widgets.Composite, boolean)
	 */
	@Override
	protected void layout(final Composite composite, final boolean flushCache) {
		final Rectangle rect = composite.getClientArea();
		layout(composite, true, rect.x, rect.y, rect.width, rect.height, flushCache);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <i>One to one copy of the method {@code GridLayout.computeSize}.</i>
	 * </p>
	 * 
	 * @see org.eclipse.swt.widgets.Layout#computeSize(org.eclipse.swt.widgets.Composite, int, int, boolean)
	 */
	@Override
	protected Point computeSize(final Composite composite, final int wHint, final int hHint, final boolean flushCache) {
		final Point size = layout(composite, false, 0, 0, wHint, hHint, flushCache);
		if (wHint != SWT.DEFAULT) {
			size.x = wHint;
		}
		if (hHint != SWT.DEFAULT) {
			size.y = hHint;
		}
		return size;
	}

	private Point layout(final Composite composite, final boolean move, final int x, final int y, final int width, final int height, final boolean flushCache) {

		if (numColumns < 1) {
			// no columns
			return getMarginPoint();
		}

		final Control[] children = composite.getChildren();
		int count = 0;
		for (int i = 0; i < children.length; i++) {
			final Control control = children[i];
			final DpiGridData data = getDpiGridData(control);
			if (data == null || !data.exclude) {
				children[count++] = children[i];
			}
		}
		if (count == 0) {
			// no children
			return getMarginPoint();
		}

		for (int i = 0; i < count; i++) {
			final Control child = children[i];
			DpiGridData data = getDpiGridData(child);
			if (data == null) {
				final GridData gridData = new GridData();
				child.setLayoutData(gridData);
				data = getDpiGridData(child);
			}
			if (flushCache) {
				data.flushCache();
			}
			data.computeSize(child, data.widthHint, data.heightHint, flushCache);
			if (data.grabExcessHorizontalSpace && data.minimumWidth > 0) {
				if (data.cacheWidth < data.minimumWidth) {
					final int trim = getTrim(child);
					data.cacheWidth = data.cacheHeight = SWT.DEFAULT;
					data.computeSize(child, Math.max(0, data.minimumWidth - trim), data.heightHint, false);
				}
			}
			if (data.grabExcessVerticalSpace && data.minimumHeight > 0) {
				data.cacheHeight = Math.max(data.cacheHeight, data.minimumHeight);
			}
		}

		/* Build the grid */
		int row = 0, column = 0, rowCount = 0;
		final int columnCount = numColumns;
		Control[][] grid = new Control[4][columnCount];
		for (int i = 0; i < count; i++) {
			final Control child = children[i];
			final DpiGridData data = getDpiGridData(child);
			final int hSpan = Math.max(1, Math.min(data.horizontalSpan, columnCount));
			final int vSpan = Math.max(1, data.verticalSpan);
			while (true) {
				final int lastRow = row + vSpan;
				if (lastRow >= grid.length) {
					final Control[][] newGrid = new Control[lastRow + 4][columnCount];
					System.arraycopy(grid, 0, newGrid, 0, grid.length);
					grid = newGrid;
				}
				if (grid[row] == null) {
					grid[row] = new Control[columnCount];
				}
				while (column < columnCount && grid[row][column] != null) {
					column++;
				}
				final int endCount = column + hSpan;
				if (endCount <= columnCount) {
					int index = column;
					while (index < endCount && grid[row][index] == null) {
						index++;
					}
					if (index == endCount) {
						break;
					}
					column = index;
				}
				if (column + hSpan >= columnCount) {
					column = 0;
					row++;
				}
			}
			for (int j = 0; j < vSpan; j++) {
				if (grid[row + j] == null) {
					grid[row + j] = new Control[columnCount];
				}
				for (int k = 0; k < hSpan; k++) {
					grid[row + j][column + k] = child;
				}
			}
			rowCount = Math.max(rowCount, row + vSpan);
			column += hSpan;
		}

		/* Column widths */
		final int dpiHorizontalSpacing = SwtUtilities.convertXToDpi(horizontalSpacing);
		final int dpiMarginLeft = SwtUtilities.convertXToDpi(marginLeft);
		final int dpiMarginWidth = SwtUtilities.convertXToDpi(marginWidth);
		final int dpiMarginRight = SwtUtilities.convertXToDpi(marginRight);
		final int availableWidth = width - dpiHorizontalSpacing * (columnCount - 1) - (dpiMarginLeft + dpiMarginWidth * 2 + dpiMarginRight);
		int expandCount = 0;
		final int[] widths = new int[columnCount];
		final int[] minWidths = new int[columnCount];
		final boolean[] expandColumn = new boolean[columnCount];
		for (int j = 0; j < columnCount; j++) {
			for (int i = 0; i < rowCount; i++) {
				final DpiGridData data = getData(grid, i, j, rowCount, columnCount, true);
				if (data != null) {
					final int hSpan = Math.max(1, Math.min(data.horizontalSpan, columnCount));
					if (hSpan == 1) {
						int w = data.cacheWidth + data.horizontalIndent;
						widths[j] = Math.max(widths[j], w);
						if (data.grabExcessHorizontalSpace) {
							if (!expandColumn[j]) {
								expandCount++;
							}
							expandColumn[j] = true;
						}
						if (!data.grabExcessHorizontalSpace || data.minimumWidth != 0) {
							w = !data.grabExcessHorizontalSpace || data.minimumWidth == SWT.DEFAULT ? data.cacheWidth : data.minimumWidth;
							w += data.horizontalIndent;
							minWidths[j] = Math.max(minWidths[j], w);
						}
					}
				}
			}
			for (int i = 0; i < rowCount; i++) {
				final DpiGridData data = getData(grid, i, j, rowCount, columnCount, false);
				if (data != null) {
					final int hSpan = Math.max(1, Math.min(data.horizontalSpan, columnCount));
					if (hSpan > 1) {
						int spanWidth = 0, spanMinWidth = 0, spanExpandCount = 0;
						for (int k = 0; k < hSpan; k++) {
							spanWidth += widths[j - k];
							spanMinWidth += minWidths[j - k];
							if (expandColumn[j - k]) {
								spanExpandCount++;
							}
						}
						if (data.grabExcessHorizontalSpace && spanExpandCount == 0) {
							expandCount++;
							expandColumn[j] = true;
						}
						int w = data.cacheWidth + data.horizontalIndent - spanWidth - (hSpan - 1) * dpiHorizontalSpacing;
						if (w > 0) {
							if (makeColumnsEqualWidth) {
								final int equalWidth = (w + spanWidth) / hSpan;
								final int remainder = (w + spanWidth) % hSpan;
								int last = -1;
								for (int k = 0; k < hSpan; k++) {
									widths[last = j - k] = Math.max(equalWidth, widths[j - k]);
								}
								if (last > -1) {
									widths[last] += remainder;
								}
							} else {
								if (spanExpandCount == 0) {
									widths[j] += w;
								} else {
									final int delta = w / spanExpandCount;
									final int remainder = w % spanExpandCount;
									int last = -1;
									for (int k = 0; k < hSpan; k++) {
										if (expandColumn[j - k]) {
											widths[last = j - k] += delta;
										}
									}
									if (last > -1) {
										widths[last] += remainder;
									}
								}
							}
						}
						if (!data.grabExcessHorizontalSpace || data.minimumWidth != 0) {
							w = !data.grabExcessHorizontalSpace || data.minimumWidth == SWT.DEFAULT ? data.cacheWidth : data.minimumWidth;
							w += data.horizontalIndent - spanMinWidth - (hSpan - 1) * dpiHorizontalSpacing;
							if (w > 0) {
								if (spanExpandCount == 0) {
									minWidths[j] += w;
								} else {
									final int delta = w / spanExpandCount;
									final int remainder = w % spanExpandCount;
									int last = -1;
									for (int k = 0; k < hSpan; k++) {
										if (expandColumn[j - k]) {
											minWidths[last = j - k] += delta;
										}
									}
									if (last > -1) {
										minWidths[last] += remainder;
									}
								}
							}
						}
					}
				}
			}
		}
		if (makeColumnsEqualWidth) {
			int minColumnWidth = 0;
			int columnWidth = 0;
			for (int i = 0; i < columnCount; i++) {
				minColumnWidth = Math.max(minColumnWidth, minWidths[i]);
				columnWidth = Math.max(columnWidth, widths[i]);
			}
			columnWidth = width == SWT.DEFAULT || expandCount == 0 ? columnWidth : Math.max(minColumnWidth, availableWidth / columnCount);
			for (int i = 0; i < columnCount; i++) {
				expandColumn[i] = expandCount > 0;
				widths[i] = columnWidth;
			}
		} else {
			if (width != SWT.DEFAULT && expandCount > 0) {
				int totalWidth = 0;
				for (int i = 0; i < columnCount; i++) {
					totalWidth += widths[i];
				}
				int c = expandCount;
				int delta = (availableWidth - totalWidth) / c;
				int remainder = (availableWidth - totalWidth) % c;
				int last = -1;
				while (totalWidth != availableWidth) {
					for (int j = 0; j < columnCount; j++) {
						if (expandColumn[j]) {
							if (widths[j] + delta > minWidths[j]) {
								widths[last = j] = widths[j] + delta;
							} else {
								widths[j] = minWidths[j];
								expandColumn[j] = false;
								c--;
							}
						}
					}
					if (last > -1) {
						widths[last] += remainder;
					}

					for (int j = 0; j < columnCount; j++) {
						for (int i = 0; i < rowCount; i++) {
							final DpiGridData data = getData(grid, i, j, rowCount, columnCount, false);
							if (data != null) {
								final int hSpan = Math.max(1, Math.min(data.horizontalSpan, columnCount));
								if (hSpan > 1) {
									if (!data.grabExcessHorizontalSpace || data.minimumWidth != 0) {
										int spanWidth = 0, spanExpandCount = 0;
										for (int k = 0; k < hSpan; k++) {
											spanWidth += widths[j - k];
											if (expandColumn[j - k]) {
												spanExpandCount++;
											}
										}
										int w = !data.grabExcessHorizontalSpace || data.minimumWidth == SWT.DEFAULT ? data.cacheWidth : data.minimumWidth;
										w += data.horizontalIndent - spanWidth - (hSpan - 1) * dpiHorizontalSpacing;
										if (w > 0) {
											if (spanExpandCount == 0) {
												widths[j] += w;
											} else {
												final int delta2 = w / spanExpandCount;
												final int remainder2 = w % spanExpandCount;
												int last2 = -1;
												for (int k = 0; k < hSpan; k++) {
													if (expandColumn[j - k]) {
														widths[last2 = j - k] += delta2;
													}
												}
												if (last2 > -1) {
													widths[last2] += remainder2;
												}
											}
										}
									}
								}
							}
						}
					}
					if (c == 0) {
						break;
					}
					totalWidth = 0;
					for (int i = 0; i < columnCount; i++) {
						totalWidth += widths[i];
					}
					delta = (availableWidth - totalWidth) / c;
					remainder = (availableWidth - totalWidth) % c;
					last = -1;
				}
			}
		}

		/* Wrapping */
		DpiGridData[] flush = null;
		int flushLength = 0;
		if (width != SWT.DEFAULT) {
			for (int j = 0; j < columnCount; j++) {
				for (int i = 0; i < rowCount; i++) {
					final DpiGridData data = getData(grid, i, j, rowCount, columnCount, false);
					if (data != null) {
						if (data.heightHint == SWT.DEFAULT) {
							final Control child = grid[i][j];
							//TEMPORARY CODE
							final int hSpan = Math.max(1, Math.min(data.horizontalSpan, columnCount));
							int currentWidth = 0;
							for (int k = 0; k < hSpan; k++) {
								currentWidth += widths[j - k];
							}
							currentWidth += (hSpan - 1) * dpiHorizontalSpacing - data.horizontalIndent;
							if ((currentWidth != data.cacheWidth && data.horizontalAlignment == SWT.FILL) || (data.cacheWidth > currentWidth)) {
								final int trim = getTrim(child);
								data.cacheWidth = data.cacheHeight = SWT.DEFAULT;
								data.computeSize(child, Math.max(0, currentWidth - trim), data.heightHint, false);
								if (data.grabExcessVerticalSpace && data.minimumHeight > 0) {
									data.cacheHeight = Math.max(data.cacheHeight, data.minimumHeight);
								}
								if (flush == null) {
									flush = new DpiGridData[count];
								}
								flush[flushLength++] = data;
							}
						}
					}
				}
			}
		}

		/* Row heights */
		final int dpiVerticalSpacing = SwtUtilities.convertYToDpi(verticalSpacing);
		final int dpiMarginTop = SwtUtilities.convertYToDpi(marginTop);
		final int dpiMarginHeight = SwtUtilities.convertYToDpi(marginHeight);
		final int dpiMarginBottom = SwtUtilities.convertYToDpi(marginBottom);
		final int availableHeight = height - dpiVerticalSpacing * (rowCount - 1) - (dpiMarginTop + dpiMarginHeight * 2 + dpiMarginBottom);
		expandCount = 0;
		final int[] heights = new int[rowCount];
		final int[] minHeights = new int[rowCount];
		final boolean[] expandRow = new boolean[rowCount];
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				final DpiGridData data = getData(grid, i, j, rowCount, columnCount, true);
				if (data != null) {
					final int vSpan = Math.max(1, Math.min(data.verticalSpan, rowCount));
					if (vSpan == 1) {
						int h = data.cacheHeight + data.verticalIndent;
						heights[i] = Math.max(heights[i], h);
						if (data.grabExcessVerticalSpace) {
							if (!expandRow[i]) {
								expandCount++;
							}
							expandRow[i] = true;
						}
						if (!data.grabExcessVerticalSpace || data.minimumHeight != 0) {
							h = !data.grabExcessVerticalSpace || data.minimumHeight == SWT.DEFAULT ? data.cacheHeight : data.minimumHeight;
							h += data.verticalIndent;
							minHeights[i] = Math.max(minHeights[i], h);
						}
					}
				}
			}
			for (int j = 0; j < columnCount; j++) {
				final DpiGridData data = getData(grid, i, j, rowCount, columnCount, false);
				if (data != null) {
					final int vSpan = Math.max(1, Math.min(data.verticalSpan, rowCount));
					if (vSpan > 1) {
						int spanHeight = 0, spanMinHeight = 0, spanExpandCount = 0;
						for (int k = 0; k < vSpan; k++) {
							spanHeight += heights[i - k];
							spanMinHeight += minHeights[i - k];
							if (expandRow[i - k]) {
								spanExpandCount++;
							}
						}
						if (data.grabExcessVerticalSpace && spanExpandCount == 0) {
							expandCount++;
							expandRow[i] = true;
						}
						int h = data.cacheHeight + data.verticalIndent - spanHeight - (vSpan - 1) * dpiVerticalSpacing;
						if (h > 0) {
							if (spanExpandCount == 0) {
								heights[i] += h;
							} else {
								final int delta = h / spanExpandCount;
								final int remainder = h % spanExpandCount;
								int last = -1;
								for (int k = 0; k < vSpan; k++) {
									if (expandRow[i - k]) {
										heights[last = i - k] += delta;
									}
								}
								if (last > -1) {
									heights[last] += remainder;
								}
							}
						}
						if (!data.grabExcessVerticalSpace || data.minimumHeight != 0) {
							h = !data.grabExcessVerticalSpace || data.minimumHeight == SWT.DEFAULT ? data.cacheHeight : data.minimumHeight;
							h += data.verticalIndent - spanMinHeight - (vSpan - 1) * dpiVerticalSpacing;
							if (h > 0) {
								if (spanExpandCount == 0) {
									minHeights[i] += h;
								} else {
									final int delta = h / spanExpandCount;
									final int remainder = h % spanExpandCount;
									int last = -1;
									for (int k = 0; k < vSpan; k++) {
										if (expandRow[i - k]) {
											minHeights[last = i - k] += delta;
										}
									}
									if (last > -1) {
										minHeights[last] += remainder;
									}
								}
							}
						}
					}
				}
			}
		}
		if (height != SWT.DEFAULT && expandCount > 0) {
			int totalHeight = 0;
			for (int i = 0; i < rowCount; i++) {
				totalHeight += heights[i];
			}
			int c = expandCount;
			int delta = (availableHeight - totalHeight) / c;
			int remainder = (availableHeight - totalHeight) % c;
			int last = -1;
			while (totalHeight != availableHeight) {
				for (int i = 0; i < rowCount; i++) {
					if (expandRow[i]) {
						if (heights[i] + delta > minHeights[i]) {
							heights[last = i] = heights[i] + delta;
						} else {
							heights[i] = minHeights[i];
							expandRow[i] = false;
							c--;
						}
					}
				}
				if (last > -1) {
					heights[last] += remainder;
				}

				for (int i = 0; i < rowCount; i++) {
					for (int j = 0; j < columnCount; j++) {
						final DpiGridData data = getData(grid, i, j, rowCount, columnCount, false);
						if (data != null) {
							final int vSpan = Math.max(1, Math.min(data.verticalSpan, rowCount));
							if (vSpan > 1) {
								if (!data.grabExcessVerticalSpace || data.minimumHeight != 0) {
									int spanHeight = 0, spanExpandCount = 0;
									for (int k = 0; k < vSpan; k++) {
										spanHeight += heights[i - k];
										if (expandRow[i - k]) {
											spanExpandCount++;
										}
									}
									int h = !data.grabExcessVerticalSpace || data.minimumHeight == SWT.DEFAULT ? data.cacheHeight : data.minimumHeight;
									h += data.verticalIndent - spanHeight - (vSpan - 1) * dpiVerticalSpacing;
									if (h > 0) {
										if (spanExpandCount == 0) {
											heights[i] += h;
										} else {
											final int delta2 = h / spanExpandCount;
											final int remainder2 = h % spanExpandCount;
											int last2 = -1;
											for (int k = 0; k < vSpan; k++) {
												if (expandRow[i - k]) {
													heights[last2 = i - k] += delta2;
												}
											}
											if (last2 > -1) {
												heights[last2] += remainder2;
											}
										}
									}
								}
							}
						}
					}
				}
				if (c == 0) {
					break;
				}
				totalHeight = 0;
				for (int i = 0; i < rowCount; i++) {
					totalHeight += heights[i];
				}
				delta = (availableHeight - totalHeight) / c;
				remainder = (availableHeight - totalHeight) % c;
				last = -1;
			}
		}

		/* Position the controls */
		if (move) {
			int gridY = y + dpiMarginTop + dpiMarginHeight;
			for (int i = 0; i < rowCount; i++) {
				int gridX = x + dpiMarginLeft + dpiMarginWidth;
				for (int j = 0; j < columnCount; j++) {
					final DpiGridData data = getData(grid, i, j, rowCount, columnCount, true);
					if (data != null) {
						final int hSpan = Math.max(1, Math.min(data.horizontalSpan, columnCount));
						final int vSpan = Math.max(1, data.verticalSpan);
						int cellWidth = 0, cellHeight = 0;
						for (int k = 0; k < hSpan; k++) {
							cellWidth += widths[j + k];
						}
						for (int k = 0; k < vSpan; k++) {
							cellHeight += heights[i + k];
						}
						cellWidth += dpiHorizontalSpacing * (hSpan - 1);
						int childX = gridX + data.horizontalIndent;
						int childWidth = Math.min(data.cacheWidth, cellWidth);
						switch (data.horizontalAlignment) {
						case SWT.CENTER:
						case GridData.CENTER:
							childX += Math.max(0, (cellWidth - data.horizontalIndent - childWidth) / 2);
							break;
						case SWT.RIGHT:
						case SWT.END:
						case GridData.END:
							childX += Math.max(0, cellWidth - data.horizontalIndent - childWidth);
							break;
						case SWT.FILL:
							childWidth = cellWidth - data.horizontalIndent;
							break;
						}
						cellHeight += dpiVerticalSpacing * (vSpan - 1);
						int childY = gridY + data.verticalIndent;
						int childHeight = Math.min(data.cacheHeight, cellHeight);
						switch (data.verticalAlignment) {
						case SWT.CENTER:
						case GridData.CENTER:
							childY += Math.max(0, (cellHeight - data.verticalIndent - childHeight) / 2);
							break;
						case SWT.BOTTOM:
						case SWT.END:
						case GridData.END:
							childY += Math.max(0, cellHeight - data.verticalIndent - childHeight);
							break;
						case SWT.FILL:
							childHeight = cellHeight - data.verticalIndent;
							break;
						}
						final Control child = grid[i][j];
						if (child != null) {
							child.setBounds(childX, childY, childWidth, childHeight);
						}
					}
					gridX += widths[j] + dpiHorizontalSpacing;
				}
				gridY += heights[i] + dpiVerticalSpacing;
			}
		}

		// clean up cache
		for (int i = 0; i < flushLength; i++) {
			flush[i].cacheWidth = flush[i].cacheHeight = -1;
		}

		int totalDefaultWidth = 0;
		int totalDefaultHeight = 0;
		for (int i = 0; i < columnCount; i++) {
			totalDefaultWidth += widths[i];
		}
		for (int i = 0; i < rowCount; i++) {
			totalDefaultHeight += heights[i];
		}
		totalDefaultWidth += dpiHorizontalSpacing * (columnCount - 1) + dpiMarginLeft + dpiMarginWidth * 2 + dpiMarginRight;
		totalDefaultHeight += dpiVerticalSpacing * (rowCount - 1) + dpiMarginTop + dpiMarginHeight * 2 + dpiMarginBottom;
		return new Point(totalDefaultWidth, totalDefaultHeight);

	}

	private Point getMarginPoint() {
		int x = marginLeft + marginWidth * 2 + marginRight;
		x = SwtUtilities.convertXToDpi(x);
		int y = marginTop + marginHeight * 2 + marginBottom;
		y = SwtUtilities.convertYToDpi(y);
		return new Point(x, y);
	}

	private int getTrim(final Control control) {
		if (control instanceof Scrollable) {
			final Rectangle rect = ((Scrollable) control).computeTrim(0, 0, 0, 0);
			return rect.width;
		} else {
			return control.getBorderWidth() * 2;
		}
	}

	private DpiGridData getDpiGridData(final Control control) {
		if (control.getLayoutData() instanceof GridData) {
			final GridData gridData = (GridData) control.getLayoutData();
			control.setLayoutData(new DpiGridData(gridData));
		}
		return (DpiGridData) control.getLayoutData();
	}

	private String getName() {
		final String string = getClass().getName();
		final int index = string.lastIndexOf('.');
		if (index == -1) {
			return string;
		}
		return string.substring(index + 1, string.length());
	}

	/**
	 * Returns a string containing a concise, human-readable description of the receiver.
	 * 
	 * @return a string representation of the layout
	 */
	@Override
	public String toString() {
		String string = getName() + " {"; //$NON-NLS-1$
		if (numColumns != 1) {
			string += "numColumns=" + numColumns + " "; //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (makeColumnsEqualWidth) {
			string += "makeColumnsEqualWidth=" + makeColumnsEqualWidth + " "; //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (marginWidth != 0) {
			string += "marginWidth=" + marginWidth + " "; //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (marginHeight != 0) {
			string += "marginHeight=" + marginHeight + " "; //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (marginLeft != 0) {
			string += "marginLeft=" + marginLeft + " "; //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (marginRight != 0) {
			string += "marginRight=" + marginRight + " "; //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (marginTop != 0) {
			string += "marginTop=" + marginTop + " "; //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (marginBottom != 0) {
			string += "marginBottom=" + marginBottom + " "; //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (horizontalSpacing != 0) {
			string += "horizontalSpacing=" + horizontalSpacing + " "; //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (verticalSpacing != 0) {
			string += "verticalSpacing=" + verticalSpacing + " "; //$NON-NLS-1$ //$NON-NLS-2$
		}
		string = string.trim();
		string += "}"; //$NON-NLS-1$
		return string;
	}

}
