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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.log.LogService;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;
import org.eclipse.nebula.widgets.compositetable.AbstractNativeHeader;
import org.eclipse.nebula.widgets.compositetable.CompositeTable;
import org.eclipse.nebula.widgets.compositetable.IRowContentProvider;
import org.eclipse.nebula.widgets.compositetable.IRowFocusListener;
import org.eclipse.nebula.widgets.compositetable.RowConstructionListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.ui.ridgets.swt.StructuredViewerFilterHolder;
import org.eclipse.riena.ui.common.IComplexComponent;
import org.eclipse.riena.ui.common.ISortableByColumn;
import org.eclipse.riena.ui.ridgets.IExtendedRidgetProperties;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRowRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.listener.IClickListener;
import org.eclipse.riena.ui.ridgets.listener.ISelectionListener;
import org.eclipse.riena.ui.ridgets.swt.AbstractSWTWidgetRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractSelectableIndexedRidget;
import org.eclipse.riena.ui.ridgets.swt.MarkerSupport;
import org.eclipse.riena.ui.ridgets.swt.SortableComparator;
import org.eclipse.riena.ui.ridgets.swt.optional.ICompositeTableRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingPropertyLocator;
import org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper;
import org.eclipse.riena.ui.swt.lnf.LnFUpdater;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 * A ridget for nebula's {@link CompositeTable} - this is a table with an instance of an arbitrary composite in each row.
 */
public class CompositeTableRidget extends AbstractSelectableIndexedRidget implements ICompositeTableRidget {

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), CompositeTableRidget.class);

	private final CTRowToRidgetMapper rowToRidgetMapper;
	private final SelectionSynchronizer selectionSynchronizer;
	private final Map<Integer, Boolean> sortableColumnsMap;
	private final Map<Integer, Comparator<Object>> comparatorMap;
	private final SelectionListener sortListener;

	/**
	 * An observable list supplied via bindToModel(...). May be null. May change at any time.
	 */
	private IObservableList modelObservables;
	/**
	 * A copy of rowObservables as an array. This is used to feed the rows / rowRidgets in the table. Will be updated when #updateFromModel() is called. The
	 * array will be re-ordered if sorting is applied. Never null.
	 */
	private Object[] rowValues;
	/**
	 * A copy of the <b>unsorted</b> rowValues. It provides the ability to restore the unsorted state of rowValues. This array is null if rowValues is unsorted.
	 */
	private Object[] rowValuesUnsorted;
	private Class<? extends Object> rowBeanClass;
	private Class<? extends Object> rowRidgetClass;
	private String[] columnHeaders;

	private boolean isSortedAscending;
	private int sortedColumn;

	private final LnFUpdater lnfUpdater = LnFUpdater.getInstance();

	public CompositeTableRidget() {
		Assert.isLegal(!SelectionType.MULTI.equals(getSelectionType()));
		rowToRidgetMapper = new CTRowToRidgetMapper();
		selectionSynchronizer = new SelectionSynchronizer();
		sortableColumnsMap = new HashMap<Integer, Boolean>();
		comparatorMap = new HashMap<Integer, Comparator<Object>>();
		sortListener = new ColumnSortListener();
		isSortedAscending = true;
		rowValues = new Object[0];
		sortedColumn = -1;
		getSingleSelectionObservable().addValueChangeListener(new IValueChangeListener() {
			public void handleValueChange(final ValueChangeEvent event) {
				final Object value = event.getObservableValue().getValue();
				getMultiSelectionObservable().clear();
				if (value != null) {
					getMultiSelectionObservable().add(value);
				}
			}
		});
		addPropertyChangeListener(IRidget.PROPERTY_ENABLED, new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				refreshRowStyles();
			}
		});
		addPropertyChangeListener(IMarkableRidget.PROPERTY_OUTPUT_ONLY, new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				refreshRowStyles();
			}
		});
	}

	@Override
	protected void checkUIControl(final Object uiControl) {
		checkType(uiControl, CompositeTable.class);
	}

	@Override
	protected void bindUIControl() {
		final CompositeTable control = getUIControl();
		if (control != null) {
			control.addRowConstructionListener(rowToRidgetMapper);
			control.addRowContentProvider(rowToRidgetMapper);
			updateControl(control);
			control.addRowFocusListener(selectionSynchronizer);
			getSingleSelectionObservable().addValueChangeListener(selectionSynchronizer);
			if (getHeader() != null) {
				for (final TableColumn column : getHeader().getColumns()) {
					column.addSelectionListener(sortListener);
				}
			}
			refreshRowStyles();
		}
	}

	@Override
	protected void unbindUIControl() {
		super.unbindUIControl();
		final CompositeTable control = getUIControl();
		if (control != null) {
			if (getHeader() != null) {
				for (final TableColumn column : getHeader().getColumns()) {
					column.removeSelectionListener(sortListener);
				}
			}
			getSingleSelectionObservable().removeValueChangeListener(selectionSynchronizer);
			control.removeRowFocusListener(selectionSynchronizer);
			control.removeRowContentProvider(rowToRidgetMapper);
			control.removeRowConstructionListener(rowToRidgetMapper);
		}
	}

	@Override
	protected List<?> getRowObservables() {
		return modelObservables != null ? Arrays.asList(rowValues) : null;
	}

	@Override
	public Object getOption(final int index) {
		if (getRowObservables() == null || index < 0 || index >= getOptionCount()) {
			throw new IllegalArgumentException("index: " + index); //$NON-NLS-1$
		}
		return rowValues[index];
	}

	public void bindToModel(final IObservableList rowObservables, final Class<? extends Object> rowClass, final Class<? extends Object> rowRidgetClass) {
		Assert.isLegal(IRowRidget.class.isAssignableFrom(rowRidgetClass));
		bindToModel(rowObservables, rowClass, rowRidgetClass, null);
	}

	public void bindToModel(final IObservableList rowObservables, final Class<? extends Object> rowClass, final Class<? extends Object> rowRidgetClass,
			final String[] columnHeaders) {
		Assert.isLegal(IRowRidget.class.isAssignableFrom(rowRidgetClass));

		unbindUIControl();

		modelObservables = rowObservables;
		rowValues = new Object[0];
		rowValuesUnsorted = null;
		this.rowBeanClass = rowClass;
		this.rowRidgetClass = rowRidgetClass;
		if (columnHeaders != null) {
			this.columnHeaders = new String[columnHeaders.length];
			System.arraycopy(columnHeaders, 0, this.columnHeaders, 0, this.columnHeaders.length);
		} else {
			this.columnHeaders = null;
		}

		bindUIControl();
	}

	public void bindToModel(final Object listHolder, final String listPropertyName, final Class<? extends Object> rowClass,
			final Class<? extends Object> rowRidgetClass, final String[] columnHeaders) {
		IObservableList rowBeansObservables;
		if (AbstractSWTWidgetRidget.isBean(rowClass)) {
			rowBeansObservables = BeansObservables.observeList(listHolder, listPropertyName);
		} else {
			rowBeansObservables = PojoObservables.observeList(listHolder, listPropertyName);
		}
		bindToModel(rowBeansObservables, rowClass, rowRidgetClass, columnHeaders);
	}

	public void bindToModel(final Object listHolder, final String listPropertyName, final Class<? extends Object> rowClass,
			final Class<? extends Object> rowRidgetClass) {
		bindToModel(listHolder, listPropertyName, rowClass, rowRidgetClass, null);
	}

	@Override
	public void updateFromModel() {
		super.updateFromModel();
		final CompositeTable control = getUIControl();
		if (modelObservables != null) {
			rowValues = modelObservables.toArray();
		} else {
			rowValues = new Object[0];
		}
		rowValuesUnsorted = null;
		if (control != null) {
			updateControl(control);
		} else {
			refreshSelection();
		}
	}

	@Override
	public int getSelectionIndex() {
		final Object selection = getSingleSelectionObservable().getValue();
		return indexOfOption(selection);
	}

	@Override
	public int[] getSelectionIndices() {
		final int index = getSelectionIndex();
		return index == -1 ? new int[0] : new int[] { index };
	}

	@Override
	public CompositeTable getUIControl() {
		return (CompositeTable) super.getUIControl();
	}

	@Override
	public int indexOfOption(final Object option) {
		int result = -1;
		if (option != null) {
			result = rowIndexOfOption(option);
		}
		return result;
	}

	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
	}

	@Override
	public void setSelectionType(final SelectionType selectionType) {
		if (SelectionType.MULTI.equals(selectionType)) {
			throw new IllegalArgumentException("SelectionType.MULTI is not supported by the UI-control"); //$NON-NLS-1$
		}
		super.setSelectionType(selectionType);
	}

	@Override
	public void setSelection(final List<?> newSelection) {
		readAndDispatch();
		super.setSelection(newSelection);
		readAndDispatch();
	}

	public void setComparator(final int columnIndex, final Comparator<Object> comparator) {
		checkColumnRange(columnIndex);
		final Integer key = Integer.valueOf(columnIndex);
		if (comparator != null) {
			comparatorMap.put(key, comparator);
		} else {
			comparatorMap.remove(key);
		}
		if (columnIndex == sortedColumn) {
			applyComparator();
		}
	}

	@Override
	protected StructuredViewerFilterHolder getFilterHolder() {
		return null; // filtering is not supported
	}

	// methods of ISortableByColumn
	///////////////////////////////

	/**
	 * {@inheritDoc}
	 * <p>
	 * Implementation note: there is no API to query the sorted column on CompositeTable. The returned value is computed with information held in the ridget.
	 */
	public int getSortedColumn() {
		int result = -1;
		final Integer key = Integer.valueOf(sortedColumn);
		if (getUIControl() != null && comparatorMap.containsKey(key)) {
			result = sortedColumn;
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Implementation notes: to have columns that are sortable by the user, the CompositeTable bound to this ridget must have an {@link AbstractNativeHeader}.
	 * In any other case this method will return false.
	 */
	public boolean isColumnSortable(final int columnIndex) {
		checkColumnRange(columnIndex);
		boolean result = false;
		if (getHeader() != null) {
			final Integer key = Integer.valueOf(columnIndex);
			final Boolean sortable = sortableColumnsMap.get(columnIndex);
			if (sortable == null || Boolean.TRUE.equals(sortable)) {
				result = comparatorMap.get(key) != null;
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Implementation note: there is no API to query the sorted column on CompositeTable. The returned value is computed with information held in the ridget.
	 */
	public boolean isSortedAscending() {
		boolean result = false;
		if (getSortedColumn() != -1) {
			result = isSortedAscending;
		}
		return result;
	}

	public void setColumnSortable(final int columnIndex, final boolean sortable) {
		checkColumnRange(columnIndex);
		final Integer key = Integer.valueOf(columnIndex);
		final Boolean newValue = Boolean.valueOf(sortable);
		Boolean oldValue = sortableColumnsMap.put(key, newValue);
		if (oldValue == null) {
			oldValue = Boolean.TRUE;
		}
		if (!newValue.equals(oldValue)) {
			firePropertyChange(ISortableByColumn.PROPERTY_COLUMN_SORTABILITY, null, columnIndex);
		}
	}

	public void setSortedAscending(final boolean ascending) {
		if (isSortedAscending != ascending) {
			final boolean oldSortedAscending = isSortedAscending;
			isSortedAscending = ascending;
			applyComparator();
			firePropertyChange(ISortableByColumn.PROPERTY_SORT_ASCENDING, oldSortedAscending, isSortedAscending);
		}
	}

	public void setSortedColumn(final int columnIndex) {
		if (columnIndex != -1) {
			checkColumnRange(columnIndex);
		}
		if (sortedColumn != columnIndex) {
			final int oldSortedColumn = sortedColumn;
			sortedColumn = columnIndex;
			applyComparator();
			firePropertyChange(ISortableByColumn.PROPERTY_SORTED_COLUMN, oldSortedColumn, sortedColumn);
		}
	}

	/**
	 * {@link CompositeTableRidget} does currently not support this operation.
	 * 
	 * @throws UnsupportedOperationException
	 *             when invoked
	 */
	@Override
	public void addSelectionListener(final ISelectionListener selectionListener) {
		throw new UnsupportedOperationException("not supported"); //$NON-NLS-1$
	}

	/**
	 * {@link CompositeTableRidget} does currently not support this operation.
	 * 
	 * @throws UnsupportedOperationException
	 *             when invoked
	 */
	@Override
	public void removeSelectionListener(final ISelectionListener selectionListener) {
		throw new UnsupportedOperationException("not supported"); //$NON-NLS-1$
	}

	/**
	 * {@link CompositeTableRidget} does currently not support this operation, because the CompositeTable dosn't fire mouse events.
	 * 
	 * @throws UnsupportedOperationException
	 *             when invoked
	 */
	@Override
	public void addClickListener(final IClickListener listener) {
		throw new UnsupportedOperationException("not supported"); //$NON-NLS-1$
	}

	/**
	 * {@link CompositeTableRidget} does currently not support this operation, because the CompositeTable dosn't fire mouse events.
	 * 
	 * @throws UnsupportedOperationException
	 *             when invoked
	 */
	@Override
	public void removeClickListener(final IClickListener listener) {
		throw new UnsupportedOperationException("not supported"); //$NON-NLS-1$
	}

	// helping methods
	//////////////////

	private void applyComparator() {
		final CompositeTable control = getUIControl();
		if (control != null) {
			Comparator<Object> comparator = null;
			if (sortedColumn != -1) {
				final Integer key = Integer.valueOf(sortedColumn);
				comparator = comparatorMap.get(key);
			}
			if (comparator != null) {
				setSortColumnOnHeader(sortedColumn);
				final int direction = isSortedAscending ? SWT.UP : SWT.DOWN;
				setSortDirectionOnHeader(direction);
				if (rowValuesUnsorted == null) {
					rowValuesUnsorted = new Object[rowValues.length];
					System.arraycopy(rowValues, 0, rowValuesUnsorted, 0, rowValuesUnsorted.length);
				}
				final SortableComparator sortableComparator = new SortableComparator(this, comparator);
				Arrays.sort(rowValues, sortableComparator);
				control.refreshAllRows();
			} else {
				setSortColumnOnHeader(sortedColumn);
				setSortDirectionOnHeader(SWT.NONE);
				if (rowValuesUnsorted != null) {
					Assert.isLegal(rowValuesUnsorted.length == rowValues.length);
					System.arraycopy(rowValuesUnsorted, 0, rowValues, 0, rowValues.length);
					rowValuesUnsorted = null;
					control.refreshAllRows();
				}
			}
		}
	}

	private void checkColumnRange(final int columnIndex) {
		Assert.isLegal(-1 < columnIndex, "columnIndex out of range: " + columnIndex); //$NON-NLS-1$
	}

	private AbstractNativeHeader getHeader() {
		final CompositeTable control = getUIControl();
		final Control header = control.getHeader();
		return (AbstractNativeHeader) (header instanceof AbstractNativeHeader ? header : null);
	}

	/**
	 * CompositeTable.setSelection(x,y) is asynchronous. This means that: - we have to fully process the event-queue before, to avoid pending events adding
	 * selection changes after our call, via asyncexec(op)). - we have to fully process the event-queue afterwards, to make sure the selection is applied to the
	 * widget.
	 * <p>
	 * Typically this method will be called before AND after ct.setSelection(x,y);
	 */
	private void readAndDispatch() {
		final CompositeTable control = getUIControl();
		if (control != null) {
			final Display display = control.getDisplay();
			while (display.readAndDispatch()) {
				Nop.reason("keep working"); //$NON-NLS-1$
			}
		}
	}

	private void refreshRowStyle(final Control rowControl, final boolean isEnabled, final boolean isOutput, final Color bgColor) {
		rowControl.setBackground(bgColor);
		if (MarkerSupport.isHideDisabledRidgetContent()) {
			rowControl.setVisible(isEnabled);
		}
		rowControl.setEnabled(isEnabled && !isOutput);
	}

	private void refreshRowStyles() {
		final CompositeTable table = getUIControl();
		final boolean enabled = isEnabled();
		final boolean output = isOutputOnly();
		if (table != null) {
			// update each row
			for (final Control rowControl : table.getRowControls()) {
				final Color bgColor = table.getBackground();
				refreshRowStyle(rowControl, enabled, output, bgColor);
			}
			// Updates the color of contentPane (=composite that holds the rows). 
			//
			// This is a clever trick: MarkerSupport manipulates
			// the bgColor of the CompositeTable, we manipulate the bgColor
			// of the internal contentPane - this way we don't get in each
			// other's way. By using the color for table when enabled we make
			// sure that we apply the 'right' color from MarkerSupport. 
			final Control[] children = table.getChildren();
			final Control contentPane = children[children.length - 1];
			final Display display = table.getDisplay();
			final Color cpBgColor = enabled ? table.getBackground() : display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
			contentPane.setBackground(cpBgColor);
		}
	}

	private int rowIndexOfOption(final Object element) {
		int result = -1;
		for (int i = 0; result == -1 && i < rowValues.length; i++) {
			if (rowValues[i] == element) {
				result = i;
			}
		}
		return result;
	}

	private void setSortDirectionOnHeader(final int direction) {
		final AbstractNativeHeader header = getHeader();
		if (header != null) {
			header.setSortDirection(direction);
		}
	}

	private void setSortColumnOnHeader(final int columnIndex) {
		final AbstractNativeHeader header = getHeader();
		if (header != null) {
			header.setSortColumn(columnIndex);
		}
	}

	/**
	 * Sets the text of every column header.
	 */
	private void updateHeader() {
		if (columnHeaders == null) {
			return;
		}
		final AbstractNativeHeader header = getHeader();
		if (header != null) {
			final TableColumn[] columns = header.getColumns();
			for (int i = 0; i < columns.length; i++) {
				if (i < columnHeaders.length) {
					columns[i].setText(columnHeaders[i]);
				}
			}
		}
	}

	private void updateControl(final CompositeTable control) {
		control.setRedraw(false);
		try {
			control.setNumRowsInCollection(rowValues.length);
			updateHeader();
			applyComparator();
			updateSelection();
		} finally {
			control.setRedraw(true);
		}
	}

	// helping classes
	//////////////////

	/**
	 * Re-applies ridget selection to control (if selection exists), otherwise clears ridget selection
	 */
	private void updateSelection() {
		final CompositeTable control = getUIControl();
		if (control != null) {
			final Object selection = getSingleSelectionObservable().getValue();
			final int index = rowIndexOfOption(selection);
			if (index > -1) {
				final int row = index - control.getTopRow();
				readAndDispatch();
				control.setSelection(0, row);
				readAndDispatch();
			} else {
				clearSelection();
				control.clearSelection();
			}
		}
	}

	/**
	 * Binds and configures Ridgets to a Row control.
	 */
	private final class CTRowToRidgetMapper extends RowConstructionListener implements IRowContentProvider {

		@Override
		public void headerConstructed(final Control newHeader) {
			// unused
		}

		@Override
		public void rowConstructed(final Control newRow) {
			final IComplexComponent rowControl = (IComplexComponent) newRow;
			if (rowRidgetClass == null) {
				return;
			}
			final IRowRidget rowRidget = (IRowRidget) ReflectionUtils.newInstance(rowRidgetClass, (Object[]) null);
			final IBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
			for (final Object control : rowControl.getUIControls()) {
				final String bindingProperty = locator.locateBindingProperty(control);
				if (bindingProperty != null) {
					final IRidget ridget = createRidget(control);
					ridget.setUIControl(control);
					ridget.setController(rowRidget);
					rowRidget.addRidget(bindingProperty, ridget);
					new RowChangeNotifier().install(ridget);
				} else {
					final String message = String.format("widget without binding property: %s : %s", rowControl.getClass(), //$NON-NLS-1$
							control);
					LOGGER.log(LogService.LOG_WARNING, message);
				}
			}
			if (Activator.getDefault() != null) {
				Wire.instance(rowRidget).andStart(Activator.getDefault().getContext());
			}
			newRow.setData("rowRidget", rowRidget); //$NON-NLS-1$
		}

		public void refresh(final CompositeTable table, final int index, final Control row) {
			if (index < rowValues.length) {
				final Object rowBean = rowValues[index];
				Assert.isLegal(rowBeanClass.isAssignableFrom(rowBean.getClass()));
				final IRowRidget rowRidget = (IRowRidget) row.getData("rowRidget"); //$NON-NLS-1$
				rowRidget.setData(rowBean);
				rowRidget.configureRidgets();
				rowRidget.setConfigured(true);
				refreshRowStyle(row, isEnabled(), isOutputOnly(), table.getBackground());
				if (row instanceof Composite) {
					lnfUpdater.updateUIControls((Composite) row, false);
				}
			}
		}

		private IRidget createRidget(final Object control) {
			final IControlRidgetMapper<Object> mapper = SwtControlRidgetMapper.getInstance();
			final Class<? extends IRidget> ridgetClass = mapper.getRidgetClass(control);
			return ReflectionUtils.newInstance(ridgetClass);
		}
	}

	/**
	 * Observes the ridgets of the table´s rows
	 */
	private class RowChangeNotifier implements PropertyChangeListener {

		public void install(final IRidget ridget) {
			ridget.addPropertyChangeListener(ITextRidget.PROPERTY_TEXT, this);
			ridget.addPropertyChangeListener(ISelectableRidget.PROPERTY_SELECTION, this);
			ridget.addPropertyChangeListener(IToggleButtonRidget.PROPERTY_SELECTED, this);
		}

		public void propertyChange(final PropertyChangeEvent evt) {
			// delegation
			propertyChangeSupport.firePropertyChange(IExtendedRidgetProperties.PROPERTY_ROW_VALUE, evt.getOldValue(), evt.getNewValue());
		}

	}

	/**
	 * Updates the selection in a CompositeTable control, when the value of the (single selection) observable changes and vice versa.
	 */
	private final class SelectionSynchronizer implements IRowFocusListener, IValueChangeListener {

		private boolean isArriving = false;
		private boolean isSelecting = false;

		public void arrive(final CompositeTable sender, final int currentObjectOffset, final Control newRow) {
			if (getRowObservables() == null) {
				return;
			}
			if (isSelecting) {
				return;
			}
			isArriving = true;
			try {
				final int selectionIndex = rowIndexOfOption(getSingleSelectionObservable().getValue());
				if (currentObjectOffset != selectionIndex) {
					setSelection(currentObjectOffset);
				}
			} finally {
				isArriving = false;
			}
		}

		public void depart(final CompositeTable sender, final int currentObjectOffset, final Control row) {
			// unused
		}

		public boolean requestRowChange(final CompositeTable sender, final int currentObjectOffset, final Control row) {
			return true;
		}

		public void handleValueChange(final ValueChangeEvent event) {
			if (isArriving) {
				return;
			}
			isSelecting = true;
			try {
				updateSelection();
			} finally {
				isSelecting = false;
			}
		}
	}

	/**
	 * Selection listener for table headers that changes the sort order of a column according to the information stored in the ridget.
	 */
	private final class ColumnSortListener extends SelectionAdapter {
		@Override
		public void widgetSelected(final SelectionEvent e) {
			final TableColumn column = (TableColumn) e.widget;
			final int columnIndex = column.getParent().indexOf(column);
			final int direction = column.getParent().getSortDirection();
			if (columnIndex == sortedColumn) {
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
