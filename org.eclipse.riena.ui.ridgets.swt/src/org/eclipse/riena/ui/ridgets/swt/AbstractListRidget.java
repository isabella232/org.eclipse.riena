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
package org.eclipse.riena.ui.ridgets.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.databinding.viewers.IViewerObservableList;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.AbstractListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionListener;

import org.eclipse.riena.internal.ui.ridgets.swt.OutputAwareValidator;
import org.eclipse.riena.ui.common.ISortableByColumn;
import org.eclipse.riena.ui.ridgets.IColumnFormatter;
import org.eclipse.riena.ui.ridgets.IListRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.listener.ClickEvent;

/**
 * An abstract Ridget for lists that does not depend on the class
 * org.eclipse.swt.widgets.List. May be used for Ridgets for custom lists.
 */
public abstract class AbstractListRidget extends AbstractSelectableIndexedRidget implements IListRidget {
	protected SelectionListener selectionTypeEnforcer;

	private DataBindingContext dbc;
	private Binding viewerSSB;
	/*
	 * Binds the viewer's multiple selection to the multiple selection
	 * observable. This binding hsa to be disposed when the ridget is set to
	 * output-only, to avoid updating the model. It has to be recreated when the
	 * ridget is set to not-output-only.
	 */
	private Binding viewerMSB;
	private Class<?> rowBeanClass;
	/*
	 * Data we received in bindToModel(...). May change without our doing.
	 */
	private IObservableList modelObservables;
	/*
	 * Data the viewer is bound to. It is updated from modelObservables on
	 * updateFromModel().
	 */
	private IObservableList viewerObservables;
	private String renderingMethod;

	private boolean isSortedAscending;
	private int sortedColumn;
	private ViewerComparator comparator;

	public AbstractListRidget() {
		isSortedAscending = true;
		sortedColumn = -1;
		getSingleSelectionObservable().addValueChangeListener(new IValueChangeListener() {
			public void handleValueChange(final ValueChangeEvent event) {
				disableMandatoryMarkers(hasInput());
			}
		});
		getMultiSelectionObservable().addListChangeListener(new IListChangeListener() {
			public void handleListChange(final ListChangeEvent event) {
				disableMandatoryMarkers(hasInput());
			}
		});
		addPropertyChangeListener(IRidget.PROPERTY_ENABLED, new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				final boolean isEnabled = ((Boolean) evt.getNewValue()).booleanValue();
				updateEnabled(isEnabled);
			}
		});
		addPropertyChangeListener(IMarkableRidget.PROPERTY_OUTPUT_ONLY, new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				if (isOutputOnly()) {
					disposeMultipleSelectionBinding();
				} else {
					createMultipleSelectionBinding();
				}
			}
		});
	}

	public void bindToModel(final IObservableList rowValues, final Class<? extends Object> rowClass,
			final String columnPropertyName) {
		Assert.isNotNull(columnPropertyName, "columnPropertyName"); //$NON-NLS-1$
		final String[] columns = { columnPropertyName };
		bindToModel(rowValues, rowClass, columns, null);
	}

	public void bindToModel(final Object listHolder, final String listPropertyName,
			final Class<? extends Object> rowClass, final String columnPropertyName) {
		Assert.isNotNull(columnPropertyName, "columnPropertyName"); //$NON-NLS-1$
		final String[] columns = { columnPropertyName };
		bindToModel(listHolder, listPropertyName, rowClass, columns, null);
	}

	/**
	 * @since 2.0
	 */
	public void bindToModel(final Object listHolder, final String listPropertyName) {
		bindToModel(listHolder, listPropertyName, Object.class, new String[] {}, null);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Implementation note: the ListRidget ignores columnHeaders.
	 * 
	 * @see #bindToModel(IObservableList, Class, String)
	 */
	public void bindToModel(final IObservableList rowValues, final Class<? extends Object> rowClass,
			final String[] columnPropertyNames, final String[] columnHeaders) {
		unbindUIControl();

		rowBeanClass = rowClass;
		modelObservables = rowValues;
		viewerObservables = null;
		renderingMethod = columnPropertyNames.length > 0 ? columnPropertyNames[0] : null;

		bindUIControl();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Implementation note: the ListRidget ignores columnHeaders.
	 * 
	 * @see #bindToModel(Object, String, Class, String)
	 */
	public void bindToModel(final Object listHolder, final String listPropertyName,
			final Class<? extends Object> rowClass, final String[] columnPropertyNames, final String[] columnHeaders) {
		IObservableList rowValues;
		if (AbstractSWTWidgetRidget.isBean(rowClass)) {
			rowValues = BeansObservables.observeList(listHolder, listPropertyName);
		} else {
			rowValues = PojoObservables.observeList(listHolder, listPropertyName);
		}
		bindToModel(rowValues, rowClass, columnPropertyNames, columnHeaders);
	}

	public IObservableList getObservableList() {
		return viewerObservables;
	}

	@Override
	public Object getOption(final int index) {
		if (getRowObservables() == null || index < 0 || index >= getOptionCount()) {
			throw new IllegalArgumentException("index: " + index); //$NON-NLS-1$
		}
		final AbstractListViewer viewer = getViewer();
		if (viewer != null) {
			return viewer.getElementAt(index); // sorted
		}
		return getRowObservables().get(index); // unsorted
	}

	@Override
	public int getSelectionIndex() {
		return getUIControl() == null ? -1 : getUIControlSelectionIndex();
	}

	@Override
	public int[] getSelectionIndices() {
		return getUIControl() == null ? new int[0] : getUIControlSelectionIndices();
	}

	public int getSortedColumn() {
		return comparator != null && sortedColumn == 0 ? 0 : -1;
	}

	public final boolean hasMoveableColumns() {
		return false;
	}

	@Override
	public int indexOfOption(final Object option) {
		if (getUIControl() != null) {
			final int optionCount = getUIControlItemCount();
			for (int i = 0; i < optionCount; i++) {
				if (getViewer().getElementAt(i).equals(option)) {
					return i;
				}
			}
		}
		return -1;
	}

	public boolean isColumnSortable(final int columnIndex) {
		Assert.isLegal(columnIndex == 0, "columnIndex out of bounds (must be 0)"); //$NON-NLS-1$
		return comparator != null;
	}

	@Override
	public boolean isDisableMandatoryMarker() {
		return hasInput();
	}

	public boolean isSortedAscending() {
		return isSortedAscending;
	}

	public void refresh(final Object node) {
		final AbstractListViewer viewer = getViewer();
		if (viewer != null) {
			viewer.refresh(node, true);
		}
	}

	/**
	 * This method is not supported by this ridget.
	 * 
	 * @throws UnsupportedOperationException
	 *             this is not supported by this ridget
	 */
	public void setColumnFormatter(final int columnIndex, final IColumnFormatter formatter) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This method is not supported by this ridget.
	 * 
	 * @throws UnsupportedOperationException
	 *             this is not supported by this ridget
	 */
	public final void setColumnSortable(final int columnIndex, final boolean sortable) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This method is not supported by this ridget.
	 * 
	 * @throws UnsupportedOperationException
	 *             this is not supported by this ridget
	 * @since 4.0
	 */
	public void clearColumnFormatters() {
		throw new UnsupportedOperationException();
	}

	/**
	 * This method is not supported by this ridget.
	 * 
	 * @throws UnsupportedOperationException
	 *             this is not supported by this ridget
	 */
	public final void setColumnWidths(final Object[] widths) {
		throw new UnsupportedOperationException();
	}

	public void setComparator(final int columnIndex, final Comparator<?> comparator) {
		Assert.isLegal(columnIndex == 0, "columnIndex out of bounds (must be 0)"); //$NON-NLS-1$
		if (comparator != null) {
			final SortableComparator sortableComparator = new SortableComparator(this, comparator);
			this.comparator = new ViewerComparator(sortableComparator);
		} else {
			this.comparator = null;
		}
		updateComparator();
	}

	/**
	 * This method is not supported by this ridget.
	 * 
	 * @throws UnsupportedOperationException
	 *             this is not supported by this ridget
	 */
	public final void setMoveableColumns(final boolean moveableColumns) {
		throw new UnsupportedOperationException("not implemented"); //$NON-NLS-1$
	}

	public void setSortedAscending(final boolean ascending) {
		if (ascending != isSortedAscending) {
			final boolean oldSortedAscending = isSortedAscending;
			isSortedAscending = ascending;
			if (hasViewer()) {
				refreshViewer();
			}
			firePropertyChange(ISortableByColumn.PROPERTY_SORT_ASCENDING, oldSortedAscending, isSortedAscending);
		}
	}

	public final void setSortedColumn(final int columnIndex) {
		final String msg = "columnIndex out of range (-1 - 0): " + columnIndex; //$NON-NLS-1$
		Assert.isLegal(columnIndex >= -1 && columnIndex <= 0, msg);
		if (sortedColumn != columnIndex) {
			final int oldSortedColumn = sortedColumn;
			sortedColumn = columnIndex;
			updateComparator();
			firePropertyChange(ISortableByColumn.PROPERTY_SORTED_COLUMN, oldSortedColumn, sortedColumn);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateFromModel() {
		super.updateFromModel();
		if (modelObservables != null) {
			final List<Object> copy = new ArrayList<Object>(modelObservables);
			viewerObservables = new WritableList(copy, rowBeanClass);
		}
		if (viewerObservables != null) {
			if (hasViewer()) {
				final AbstractListViewer viewer = getViewer();
				viewer.getControl().setRedraw(false); // prevent flicker during
				// update
				final StructuredSelection currentSelection = new StructuredSelection(getSelection());
				try {
					configureViewer(viewer);
				} finally {
					viewer.setSelection(currentSelection);
					viewer.getControl().setRedraw(true);
				}
			} else {
				refreshSelection();
			}
		}
	}

	protected void configureViewer(final AbstractListViewer viewer) {
		final ObservableListContentProvider viewerCP = new ObservableListContentProvider();
		final String[] propertyNames = new String[] { renderingMethod };
		IObservableMap[] attributeMap = null;

		// if renderingMethod is null, toString-Method will be used in ListLabelProvider
		if (null != renderingMethod) {
			if (AbstractSWTWidgetRidget.isBean(rowBeanClass)) {
				attributeMap = BeansObservables.observeMaps(viewerCP.getKnownElements(), rowBeanClass, propertyNames);
			} else {
				attributeMap = PojoObservables.observeMaps(viewerCP.getKnownElements(), rowBeanClass, propertyNames);
			}
		}

		viewer.setLabelProvider(new ListLabelProvider(attributeMap));
		viewer.setContentProvider(viewerCP);
		viewer.setInput(viewerObservables);
	}

	protected void createSelectionBindings() {
		dbc = new DataBindingContext();
		// viewer to single selection binding
		final IObservableValue viewerSelection = ViewersObservables.observeSingleSelection(getViewer());
		viewerSSB = dbc.bindValue(viewerSelection, getSingleSelectionObservable(), new UpdateValueStrategy(
				UpdateValueStrategy.POLICY_UPDATE).setAfterGetValidator(new OutputAwareValidator(this)),
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		// viewer to multiple selection binding
		viewerMSB = null;
		if (!isOutputOnly()) {
			createMultipleSelectionBinding();
		}
	}

	@Override
	protected java.util.List<?> getRowObservables() {
		return viewerObservables;
	}

	protected abstract int getUIControlItemCount();

	protected abstract int getUIControlSelectionIndex();

	protected abstract int[] getUIControlSelectionIndices();

	// helping methods
	// ////////////////

	protected abstract AbstractListViewer getViewer();

	protected void disposeSelectionBindings() {
		if (viewerSSB != null && !viewerSSB.isDisposed()) {
			viewerSSB.dispose();
		}
		disposeMultipleSelectionBinding();
	}

	protected boolean hasViewer() {
		return getViewer() != null;
	}

	protected boolean hasViewerModel() {
		return viewerObservables != null;
	}

	protected void refreshViewer() {
		getViewer().refresh();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.swt.AbstractSWTWidgetRidget#unbindUIControl()
	 */
	@Override
	protected void unbindUIControl() {
		super.unbindUIControl();
		if (dbc != null) {
			disposeSelectionBindings();
			dbc.dispose();
			dbc = null;
		}
	}

	protected void updateComparator() {
		if (hasViewer()) {
			if (sortedColumn == 0) {
				getViewer().setComparator(this.comparator);
			} else {
				getViewer().setComparator(null);
			}
		}
	}

	/**
	 * @since 4.0
	 */
	@Override
	protected ClickEvent createClickEvent(final MouseEvent e) {
		Object rowData;
		final int index = getUIControlSelectionIndex();
		if (index > -1) {
			rowData = getViewer().getElementAt(index);
		} else {
			rowData = null;
		}
		return new ClickEvent(AbstractListRidget.this, e.button, 0, rowData);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws UnsupportedOperationException
	 *             is always throw because the ListRidget doesn't support
	 *             editing.
	 * 
	 * @since 4.0
	 */
	public void setColumnEditable(final int columnIndex, final boolean editable) {
		throw new UnsupportedOperationException("not supported"); //$NON-NLS-1$
	}

	protected abstract void updateEnabled(boolean isEnabled);

	// helping methods
	// ////////////////

	private void createMultipleSelectionBinding() {
		if (viewerMSB == null && dbc != null && hasViewer()) {
			final StructuredSelection currentSelection = new StructuredSelection(getSelection());
			final IViewerObservableList viewerSelections = ViewersObservables.observeMultiSelection(getViewer());
			viewerMSB = dbc.bindList(viewerSelections, getMultiSelectionObservable(), new UpdateListStrategy(
					UpdateListStrategy.POLICY_UPDATE), new UpdateListStrategy(UpdateListStrategy.POLICY_UPDATE));
			getViewer().setSelection(currentSelection);
		}
	}

	private void disposeMultipleSelectionBinding() {
		if (viewerMSB != null) { // implies dbc != null
			viewerMSB.dispose();
			dbc.removeBinding(viewerMSB);
			viewerMSB = null;
		}
	}

	private boolean hasInput() {
		return !getSelection().isEmpty();
	}

	// helping classes
	// ////////////////

	/**
	 * Generates the labels (i.e. strings) shown in the list.
	 */
	private final class ListLabelProvider extends ObservableMapLabelProvider {

		private final boolean useToString;

		public ListLabelProvider(final IObservableMap[] attributeMap) {
			super(null == attributeMap ? new IObservableMap[] {} : attributeMap);
			useToString = attributeMap == null;
		}

		@Override
		public String getColumnText(final Object element, final int columnIndex) {
			String result;
			if (MarkerSupport.isHideDisabledRidgetContent() && !isEnabled()) {
				result = ""; //$NON-NLS-1$
			} else {
				result = useToString ? toString(element) : super.getColumnText(element, columnIndex);
			}
			return result;
		}

		private String toString(final Object element) {
			if (element == null) {
				throw new NullPointerException("Row-element in ListRidget is null"); //$NON-NLS-1$
			}
			final String result = element.toString();
			if (result == null) {
				throw new NullPointerException("Row-element.toString() returned null"); //$NON-NLS-1$
			}
			return result;
		}
	}

}
