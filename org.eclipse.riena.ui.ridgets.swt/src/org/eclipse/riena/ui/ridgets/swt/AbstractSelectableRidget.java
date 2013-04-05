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
package org.eclipse.riena.ui.ridgets.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.ListBinding;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListDiff;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.ValueDiff;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.Assert;

import org.eclipse.riena.core.util.ListenerList;
import org.eclipse.riena.ui.ridgets.IFilterableContentRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContentFilter;
import org.eclipse.riena.ui.ridgets.IRidgetContentFilterHolder;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.listener.ISelectionListener;
import org.eclipse.riena.ui.ridgets.listener.SelectionEvent;

/**
 * Default implementation of an {@link ISelectableRidget}. This ridget than can observe single and multiple selection of a widget and bind the selection state
 * to model elements.
 */
public abstract class AbstractSelectableRidget extends AbstractSWTRidget implements ISelectableRidget, IFilterableContentRidget {

	/** The selected option (single-selection). */
	private final WritableValue singleSelectionObservable;
	/** A list of selected options (multiple-selection). */
	private final WritableList multiSelectionObservable;
	/** Binds the singleSelectionObservable to some model. */
	private Binding singleSelectionBinding;
	/** Binds the multiSelectionObservable to some model. */
	private ListBinding multiSelectionBinding;
	/** The selection type. */
	private SelectionType selectionType = SelectionType.SINGLE;
	/** A list of selection listeners. */
	private ListenerList<ISelectionListener> selectionListeners;

	public AbstractSelectableRidget() {
		singleSelectionObservable = new SingleSelectionObservable();
		multiSelectionObservable = new MultiSelectionObservable();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 2.0
	 */
	public void addSelectionListener(final ISelectionListener selectionListener) {
		Assert.isNotNull(selectionListener, "selectionListener is null"); //$NON-NLS-1$
		if (selectionListeners == null) {
			selectionListeners = new ListenerList<ISelectionListener>(ISelectionListener.class);
			addPropertyChangeListener(ISelectableRidget.PROPERTY_SELECTION, new PropertyChangeListener() {
				public void propertyChange(final PropertyChangeEvent evt) {
					notifySelectionListeners((List<?>) evt.getOldValue(), (List<?>) evt.getNewValue());
				}
			});
		}
		selectionListeners.add(selectionListener);
	}

	public final void bindMultiSelectionToModel(final IObservableList observableList) {
		if (multiSelectionBinding != null) {
			multiSelectionBinding.dispose();
		}
		final DataBindingContext dbc = new DataBindingContext();
		multiSelectionBinding = (ListBinding) dbc.bindList(multiSelectionObservable, observableList, new UpdateListStrategy(UpdateListStrategy.POLICY_UPDATE),
				new UpdateListStrategy(UpdateListStrategy.POLICY_ON_REQUEST));
	}

	public final void bindMultiSelectionToModel(final Object selectionHolder, final String selectionPropertyName) {
		IObservableList observableList;
		if (AbstractSWTRidget.isBean(selectionHolder.getClass())) {
			observableList = BeansObservables.observeList(selectionHolder, selectionPropertyName);
		} else {
			observableList = PojoObservables.observeList(selectionHolder, selectionPropertyName);
		}
		bindMultiSelectionToModel(observableList);
	}

	public final void bindSingleSelectionToModel(final IObservableValue selectionValue) {
		if (singleSelectionBinding != null) {
			singleSelectionBinding.dispose();
		}
		final DataBindingContext dbc = new DataBindingContext();
		singleSelectionBinding = dbc.bindValue(singleSelectionObservable, selectionValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE),
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST));
	}

	public final void bindSingleSelectionToModel(final Object selectionHolder, final String selectionPropertyName) {
		IObservableValue observableValue;
		if (AbstractSWTRidget.isBean(selectionHolder.getClass())) {
			observableValue = PojoObservables.observeValue(selectionHolder, selectionPropertyName);
		} else {
			observableValue = PojoObservables.observeValue(selectionHolder, selectionPropertyName);
		}
		bindSingleSelectionToModel(observableValue);
	}

	public void clearSelection() {
		singleSelectionObservable.setValue(null);
		multiSelectionObservable.clear();
	}

	public boolean containsOption(final Object option) {
		if (getRowObservables() == null) {
			return false;
		}
		return getRowObservables().contains(option);
	}

	public final IObservableList getMultiSelectionObservable() {
		return multiSelectionObservable;
	}

	public final List<Object> getSelection() {
		final List<Object> result = new ArrayList<Object>();
		if (SelectionType.SINGLE.equals(selectionType)) {
			if (singleSelectionObservable.getValue() != null) {
				result.add(singleSelectionObservable.getValue());
			}
		} else if (SelectionType.MULTI.equals(selectionType)) {
			result.addAll(Arrays.asList(multiSelectionObservable.toArray()));
		}
		return result;
	}

	/**
	 * @since 4.0
	 */
	public <T> T getSingleSelection() {
		if (!hasSelection()) {
			return null;
		}
		Assert.isLegal(getSelectionType() == SelectionType.SINGLE, "A single selection is only provided if selection type is SelectionType.SINGLE. "); //$NON-NLS-1$

		final List<Object> selection = getSelection();
		if (selection.size() != 1) {
			return null;
		}
		return (T) selection.get(0);
	}

	/**
	 * @since 4.0
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getMultiSelection() {
		if (!hasSelection()) {
			return null;
		}
		Assert.isLegal(getSelectionType() == SelectionType.MULTI, "A multi selection is only provided if selection type is SelectionType.MULTI. "); //$NON-NLS-1$

		final List<Object> selection = getSelection();
		if (selection.size() < 0) {
			return null;
		}
		return (List<T>) selection;
	}

	/**
	 * @since 4.0
	 */
	public final SelectionType getSelectionType() {
		return selectionType;
	}

	/**
	 * @since 4.0
	 */
	public boolean hasSelection() {
		final List<Object> selection = getSelection();
		if (null == selection || selection.isEmpty()) {
			return false;
		}
		return true;
	}

	public final IObservableValue getSingleSelectionObservable() {
		return singleSelectionObservable;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 2.0
	 */
	public void removeSelectionListener(final ISelectionListener selectionListener) {
		if (selectionListeners != null) {
			selectionListeners.remove(selectionListener);
		}
	}

	public void setSelection(final List<?> newSelection) {
		assertIsBoundToModel();
		final List<?> knownElements = getKnownElements(newSelection);
		if (SelectionType.SINGLE.equals(selectionType)) {
			final Object value = knownElements.size() > 0 ? knownElements.get(0) : null;
			singleSelectionObservable.setValue(value);
		} else if (SelectionType.MULTI.equals(selectionType)) {
			final ListDiff diff = Diffs.computeListDiff(multiSelectionObservable, knownElements);
			if (diff.getDifferences().length > 0) {
				multiSelectionObservable.clear();
				multiSelectionObservable.addAll(knownElements);
			}
		}
	}

	public final void setSelection(final Object newSelection) {
		assertIsBoundToModel();
		final List<Object> list = Arrays.asList(new Object[] { newSelection });
		setSelection(list);
	}

	public void setSelectionType(final SelectionType selectionType) {
		Assert.isNotNull(selectionType, "selectionType cannot be null"); //$NON-NLS-1$
		if (SelectionType.NONE.equals(selectionType)) {
			throw new IllegalArgumentException("SelectionType.NONE is not supported by the UI-control"); //$NON-NLS-1$
		}
		if (!this.selectionType.equals(selectionType)) {
			this.selectionType = selectionType;
		}
	}

	public final void updateMultiSelectionFromModel() {
		if (multiSelectionBinding != null) {
			multiSelectionBinding.updateModelToTarget();
		}
	}

	public final void updateSingleSelectionFromModel() {
		if (singleSelectionBinding != null) {
			singleSelectionBinding.updateModelToTarget();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 4.0
	 */
	public void addFilter(final IRidgetContentFilter filter) {
		final IRidgetContentFilterHolder<?> filterHolder = getFilterHolder();
		if (filterHolder == null) {
			throw new UnsupportedOperationException("This ridget type does not support filtering: " + getClass().getSimpleName()); //$NON-NLS-1$
		}
		filterHolder.add(filter);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 4.0
	 */
	public void removeFilter(final IRidgetContentFilter filter) {
		final IRidgetContentFilterHolder<?> filterHolder = getFilterHolder();
		if (filterHolder != null) {
			filterHolder.remove(filter);
		}
	}

	// protected methods
	// //////////////////

	/**
	 * Retrieves the {@link IRidgetContentFilterHolder} for this ridget.
	 * 
	 * @return the filter holder for this ridget
	 * @since 4.0
	 */
	protected abstract IRidgetContentFilterHolder<?> getFilterHolder();

	/**
	 * Return an observable list of objects which can be selected through this ridget.
	 * 
	 * @return a List instance or null, if the ridget has not been bound to a model
	 */
	abstract protected List<?> getRowObservables();

	/**
	 * Throws an exception if no model observables are available (i.e. getRowObservables() == null).
	 */
	protected final void assertIsBoundToModel() {
		if (getRowObservables() == null) {
			throw new BindingException("ridget not bound to model"); //$NON-NLS-1$
		}
	}

	/**
	 * Updates the current selection to ensure that all selected items are still available in the ridget's model.
	 * <p>
	 * Subclasses should call this from their {@link #updateFromModel()} method.
	 * <p>
	 * Implementation note: the method computes the subset of the current selection that is available. If the subset is smaller that the current selection, it
	 * will be applied and become the new current selection.
	 * 
	 * @since 2.0
	 */
	protected final void refreshSelection() {
		final List<?> rowObservables = getRowObservables();
		if (rowObservables != null) {
			boolean doUpdate = false;
			final List<Object> oldSelection = getSelection();
			final List<Object> newSelection = new ArrayList<Object>();
			for (final Object candidate : oldSelection) {
				if (!rowObservables.contains(candidate)) {
					doUpdate = true;
				} else {
					newSelection.add(candidate);
				}
			}
			if (doUpdate) {
				setSelection(newSelection);
			}
		}
	}

	// helping methods
	// ////////////////

	private List<Object> getKnownElements(final List<?> elements) {
		final List<Object> result = new ArrayList<Object>();
		final Collection<?> rowObservables = getRowObservables();
		for (final Object element : elements) {
			if (rowObservables.contains(element)) {
				result.add(element);
			}
		}
		return result;
	}

	private void notifySelectionListeners(final List<?> oldSelectionList, final List<?> newSelectionList) {
		if (selectionListeners != null) {
			final SelectionEvent event = new SelectionEvent(this, oldSelectionList, newSelectionList);
			for (final ISelectionListener listener : selectionListeners.getListeners()) {
				listener.ridgetSelected(event);
			}
		}
	}

	// helping classes
	// ////////////////

	/**
	 * Observable value for single selection. This class is used by this ridget to monitor and maintain the selection state for single selection and fire
	 * appropriate events.
	 */
	private final class SingleSelectionObservable extends WritableValue {
		SingleSelectionObservable() {
			super(null, Object.class);
		}

		@Override
		protected void fireValueChange(final ValueDiff diff) {
			super.fireValueChange(diff);
			final Object newValue = diff.getNewValue();
			final Object oldValue = diff.getOldValue();
			if (oldValue != newValue && getSelectionType() == SelectionType.SINGLE) {
				final String key = ISelectableRidget.PROPERTY_SELECTION;
				AbstractSelectableRidget.this.propertyChangeSupport.firePropertyChange(key, toList(oldValue), toList(newValue));
			}
		}

		private List<?> toList(final Object value) {
			return value == null ? Collections.EMPTY_LIST : Arrays.asList(new Object[] { value });
		}
	}

	/**
	 * Observable list for multiple selection. This class is used by this ridget to monitor and maintain the selection state for multiple selection and fire
	 * appropriate events.
	 */
	private final class MultiSelectionObservable extends WritableList {
		MultiSelectionObservable() {
			super(new ArrayList<Object>(), Object.class);
		}

		/**
		 * Only the MultiSelectionObservable is firing selection property change events to avoid duplicate events (bug 268897)
		 */
		@Override
		protected void fireListChange(final ListDiff diff) {
			super.fireListChange(diff);
			if (getSelectionType() == SelectionType.MULTI) {
				final List<Object> newSelection = Arrays.asList(toArray());
				final List<Object> oldSelection = computeOldSelection(diff, newSelection);
				final String key = ISelectableRidget.PROPERTY_SELECTION;
				AbstractSelectableRidget.this.propertyChangeSupport.firePropertyChange(key, oldSelection, newSelection);
			}
		}

		private List<Object> computeOldSelection(final ListDiff diff, final List<Object> newSelection) {
			final List<Object> oldSelection = new ArrayList<Object>();
			oldSelection.addAll(newSelection);
			for (final ListDiffEntry entry : diff.getDifferences()) {
				final Object element = entry.getElement();
				if (entry.isAddition()) {
					oldSelection.remove(element);
				} else {
					oldSelection.add(element);
				}
			}
			return oldSelection;
		}
	}

}
