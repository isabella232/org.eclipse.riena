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
package org.eclipse.riena.internal.ui.ridgets.swt.optional;

import java.util.Comparator;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.AbstractTableViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridCellRenderer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.internal.ui.ridgets.swt.AbstractTableRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ITableTreeWrapper;
import org.eclipse.riena.internal.ui.ridgets.swt.TableComparator;
import org.eclipse.riena.internal.ui.ridgets.swt.TableRidgetLabelProvider;
import org.eclipse.riena.ui.ridgets.IColumnFormatter;
import org.eclipse.riena.ui.ridgets.listener.ClickEvent;
import org.eclipse.riena.ui.ridgets.swt.SortableComparator;
import org.eclipse.riena.ui.ridgets.swt.optional.IGridRidget;

/**
 * Ridget for SWT/Nebula {@link Grid} widgets.
 */
public class GridRidget extends AbstractTableRidget implements IGridRidget {

	private final GridCellRenderer cellRenderer;
	private final GridCellRenderer emptyCellRenderer;

	public GridRidget() {
		super();
		sortListener = new ColumnSortListener();
		cellRenderer = new GridRidgetCellRenderer(this);
		emptyCellRenderer = new GridRidgetEmptyCellRenderer();
	}

	@Override
	public Grid getUIControl() {
		return (Grid) super.getUIControl();
	}

	@Override
	protected final int getUiSelectionCount() {
		final Grid control = getUIControl();
		return control == null ? -1 : control.getSelectionCount();
	}

	@Override
	public boolean isErrorMarked(final Widget item) {
		return super.isErrorMarked(item);
	}

	//
	//	@Override
	//	protected final int getUiSelectionIndex() {
	//		final Grid control = getUIControl();
	//		return control == null ? -1 : control.getSelectionIndex();
	//	}
	//
	//	@Override
	//	protected final void setUiSelection(final int index) {
	//		final Grid control = getUIControl();
	//		if (control == null) {
	//			return;
	//		}
	//		control.setSelection(index);
	//	}

	@Override
	protected final void setUiSelection(final Widget item) {
		Assert.isTrue(item instanceof GridItem);
		final Grid control = getUIControl();
		if (control != null) {
			final int index = control.getIndexOfItem((GridItem) item);
			control.setSelection(index);
		}
	}

	@Override
	public int getSelectionIndex() {
		final Grid control = getUIControl();
		return control == null ? -1 : control.getSelectionIndex();
	}

	@Override
	public int[] getSelectionIndices() {
		final Grid control = getUIControl();
		return control == null ? new int[0] : control.getSelectionIndices();
	}

	@Override
	public void setNativeToolTip(final boolean nativeToolTip) {
		if (!nativeToolTip) {
			throw new IllegalArgumentException("Grid only supports 'natvie' tool tips!"); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The UI control must be a {@link Grid}.
	 */
	@Override
	protected final void checkUIControl(final Object uiControl) {
		checkType(uiControl, Grid.class);
	}

	@Override
	protected GridTableViewer getTableViewer() {
		return (GridTableViewer) super.getTableViewer();
	}

	@Override
	protected final void bindUIControl() {
		super.bindUIControl();
		final Grid control = getUIControl();
		if (control != null) {
			for (final GridColumn column : control.getColumns()) {
				column.addSelectionListener(sortListener);
				column.setCellRenderer(cellRenderer);
			}
			control.addSelectionListener(selectionTypeEnforcer);
			control.setEmptyCellRenderer(emptyCellRenderer);
		}
	}

	@Override
	protected final void unbindUIControl() {
		super.unbindUIControl();
		final Grid control = getUIControl();
		if (control != null) {
			for (final GridColumn column : control.getColumns()) {
				column.removeSelectionListener(sortListener);
			}
			control.removeSelectionListener(selectionTypeEnforcer);
		}
	}

	@Override
	protected final void applyColumns() {
		final Grid control = getUIControl();
		if (control == null) {
			return;
		}
		final int expectedCols = getExpectedColumnCount();
		if (getColumnCount() != expectedCols) {
			for (final GridColumn column : control.getColumns()) {
				column.dispose();
			}
			for (int i = 0; i < expectedCols; i++) {
				new GridColumn(control, SWT.NONE);
			}
			applyColumnWidths();
		}
		final GridColumn[] columns = control.getColumns();
		for (int columnIndex = 0; columnIndex < columns.length; columnIndex++) {
			final ViewerColumn viewerColumn = new GridViewerColumn(getTableViewer(), columns[columnIndex]);
			applyEditingSupport(viewerColumn, columnIndex);
		}
	}

	@Override
	protected final void applyColumnsMovable() {
		final Grid control = getUIControl();
		if (control == null) {
			return;
		}
		for (final GridColumn column : control.getColumns()) {
			column.setMoveable(hasMoveableColumns());
		}
	}

	@Override
	protected final void applyTableColumnHeaders() {
		final Grid control = getUIControl();
		final boolean headersVisible = columnHeaders != null;
		control.setHeaderVisible(headersVisible);
		if (headersVisible) {
			final GridColumn[] columns = control.getColumns();
			for (int i = 0; i < columns.length; i++) {
				String columnHeader = ""; //$NON-NLS-1$
				if (i < columnHeaders.length && columnHeaders[i] != null) {
					columnHeader = columnHeaders[i];
				}
				columns[i].setText(columnHeader);
				// not necessary for GridColumns because they implement this function already!
				// final String tooltip = isShowColumnTooltip(columns[i], columnHeader) ? columnHeader : ""; //$NON-NLS-1$
				// columns[i].setHeaderTooltip(tooltip);
			}
		}
	}

	@Override
	protected final void applyComparator(final Map<Integer, Comparator<?>> comparatorMap) {
		if (getTableViewer() != null) {
			Comparator<?> compi = null;
			if (getSortedColumn() != -1) {
				final Integer key = Integer.valueOf(getSortedColumn());
				compi = comparatorMap.get(key);
			}
			final Grid control = getUIControl();
			clearSortIndicator();
			if (compi != null) {
				final GridColumn column = control.getColumn(getSortedColumn());
				final int direction = getSortDirection();
				column.setSort(direction);
				final SortableComparator sortableComparator = new SortableComparator(this, compi);
				getTableViewer().setComparator(new TableComparator(sortableComparator));
			} else {
				getTableViewer().setComparator(null);
			}
		}
	}

	@Override
	protected int getColumnStyle(final int columnIndex) {
		checkColumnRange(columnIndex);
		final Grid control = getUIControl();
		if (control == null) {
			return SWT.DEFAULT;
		}
		final GridColumn[] columns = control.getColumns();
		return columns[columnIndex].getStyle();
	}

	private void clearSortIndicator() {
		final Grid control = getUIControl();
		if (control == null) {
			return;
		}
		final GridColumn[] columns = control.getColumns();
		for (final GridColumn column : columns) {
			column.setSort(SWT.NONE);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Sets for every item the tool tip text. The text can be configured with the {@link IColumnFormatter}.
	 * <p>
	 * <i>note: The tool tip of a grid is only shown, if in a cell not the complete text can be displayed.</i>
	 */
	@Override
	protected final void updateToolTipSupport() {

		final Grid control = getUIControl();
		if (control == null) {
			return;
		}
		int colCount = getColumnCount();
		final GridItem[] items = control.getItems();
		for (final GridItem item : items) {
			for (final int colIndex = 0; colCount < colCount; colCount++) {
				String toolTip = null;
				if (toolTip == null) {
					toolTip = getToolTipText(item, colIndex);
				}
				item.setToolTipText(colIndex, toolTip);
			}
		}

	}

	private String getToolTipText(final GridItem item, final int column) {
		String result = null;
		final IBaseLabelProvider labelProvider = getTableViewer().getLabelProvider();
		if (labelProvider != null) {
			final Object element = item.getData();
			result = ((TableRidgetLabelProvider) labelProvider).getToolTipText(element, column);
		}
		if (result == null) {
			result = item.getText(column);
		}
		return result;

	}

	@Override
	protected AbstractTableViewer createTableViewer() {
		return new GridRidgetGridTableViewer(this);
	}

	@Override
	protected ITableTreeWrapper createTableWrapper() {
		Assert.isNotNull(getUIControl());
		return new GridWrapper(getUIControl());
	}

	@Override
	protected ClickEvent createClickEvent(final MouseEvent e) {
		final Grid grid = (Grid) e.widget;
		final int colIndex = findColumn(grid, new Point(e.x, e.y));
		// x = 0 gets us an item even not using SWT.FULL_SELECTION
		final Item item = getItem(new Point(0, e.y));
		final Object rowData = item != null ? item.getData() : null;
		final ClickEvent event = new ClickEvent(this, e.button, colIndex, rowData);
		return event;
	}

	/**
	 * Returns the 0 based index of the column at {@code pt}. The code can handle re-ordered columns. The index refers to the original ordering (as used by SWT
	 * API).
	 * <p>
	 * Will return -1 if no column could be computed -- this is the case when all columns are resized to have width 0.
	 */
	private int findColumn(final Grid grid, final Point pt) {
		int width = 0;
		final int[] colOrder = grid.getColumnOrder();
		// compute the current column ordering
		final GridColumn[] columns = new GridColumn[colOrder.length];
		for (int i = 0; i < colOrder.length; i++) {
			final int idx = colOrder[i];
			columns[i] = grid.getColumn(idx);
		}
		// find the column under Point pt\
		for (final GridColumn col : columns) {
			final int colWidth = col.getWidth();
			if (width < pt.x && pt.x < width + colWidth) {
				return grid.indexOf(col);
			}
			width += colWidth;
		}
		return -1;
	}

	/**
	 * Selection listener for table headers that changes the sort order of a column according to the information stored in the ridget.
	 */
	private final class ColumnSortListener extends SelectionAdapter {
		@Override
		public void widgetSelected(final SelectionEvent e) {
			final GridColumn column = (GridColumn) e.widget;
			final int columnIndex = column.getParent().indexOf(column);
			final int direction = column.getSort();
			if (columnIndex == getSortedColumn()) {
				if (direction == SWT.UP) {
					setSortedAscending(false);
				} else if (direction == SWT.DOWN) {
					setSortedColumn(-1);
				}
			} else if (isColumnSortable(columnIndex)) {
				setSortedColumn(columnIndex);
				if (direction == SWT.NONE) {
					setSortedAscending(true);
				}
			}
			column.getParent().showSelection();
		}
	}

}
