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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;

import org.eclipse.riena.beans.common.ListBean;
import org.eclipse.riena.core.util.ListenerList;
import org.eclipse.riena.ui.ridgets.IChoiceRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IMultipleChoiceRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.listener.ISelectionListener;
import org.eclipse.riena.ui.ridgets.swt.AbstractSWTRidget;
import org.eclipse.riena.ui.ridgets.swt.MarkerSupport;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.lnf.LnFUpdater;

/**
 * Ridget for a {@link ChoiceComposite} widget with multiple selection.
 */
public class MultipleChoiceRidget extends AbstractChoiceRidget implements IMultipleChoiceRidget {

	private final static LnFUpdater LNF_UPDATER = LnFUpdater.getInstance();

	/** The list of available options. */
	private final WritableList optionsObservable;
	/** The selected option. */
	private final WritableList selectionObservable;

	private Binding optionsBinding;
	private Binding selectionBinding;
	private String[] optionLabels;

	/** A list of selection listeners. */
	private ListenerList<ISelectionListener> selectionListeners;

	public MultipleChoiceRidget() {
		optionsObservable = new WritableList();
		selectionObservable = new WritableList();
		selectionObservable.addChangeListener(new IChangeListener() {
			public void handleChange(final ChangeEvent event) {
				disableMandatoryMarkers(hasInput());
			}
		});
		addPropertyChangeListener(IRidget.PROPERTY_ENABLED, new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				updateSelection(getUIControl());
			}
		});
		addPropertyChangeListener(IMarkableRidget.PROPERTY_OUTPUT_ONLY, new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				final boolean isOutput = ((Boolean) evt.getNewValue()).booleanValue();
				updateEditable(getUIControl(), !isOutput);
			}
		});
	}

	@Override
	protected void bindUIControl() {
		if (optionsBinding != null) {
			createChildren(getUIControl());
		}
	}

	@Override
	protected void checkUIControl(final Object uiControl) {
		checkType(uiControl, ChoiceComposite.class);
		if (uiControl != null) {
			final ChoiceComposite composite = (ChoiceComposite) uiControl;
			Assert.isTrue(composite.isMultipleSelection(), "expected multiple selection ChoiceComposite"); //$NON-NLS-1$
		}
	}

	@Override
	protected void unbindUIControl() {
		super.unbindUIControl();
		disposeChildren(getUIControl());
	}

	// public methods
	// ///////////////

	@Override
	public ChoiceComposite getUIControl() {
		return (ChoiceComposite) super.getUIControl();
	}

	public void bindToModel(final IObservableList optionValues, final IObservableList selectionValues) {
		Assert.isNotNull(optionValues, "optionValues"); //$NON-NLS-1$
		Assert.isNotNull(selectionValues, "selectionValues"); //$NON-NLS-1$
		bindToModel(optionValues, null, selectionValues);
	}

	public void bindToModel(final Object listHolder, final String listPropertyName, final Object selectionHolder, final String selectionPropertyName) {
		Assert.isNotNull(listHolder, "listHolder"); //$NON-NLS-1$
		Assert.isNotNull(listPropertyName, "listPropertyName"); //$NON-NLS-1$
		Assert.isNotNull(selectionHolder, "selectionHolder"); //$NON-NLS-1$
		Assert.isNotNull(selectionPropertyName, "selectionPropertyName"); //$NON-NLS-1$
		IObservableList optionValues;
		if (AbstractSWTRidget.isBean(listHolder.getClass())) {
			optionValues = BeansObservables.observeList(listHolder, listPropertyName);
		} else {
			optionValues = PojoObservables.observeList(listHolder, listPropertyName);
		}
		IObservableList selectionValues;
		if (AbstractSWTRidget.isBean(selectionHolder.getClass())) {
			selectionValues = BeansObservables.observeList(selectionHolder, selectionPropertyName);
		} else {
			selectionValues = PojoObservables.observeList(selectionHolder, selectionPropertyName);
		}
		bindToModel(optionValues, null, selectionValues);
	}

	public void bindToModel(final List<? extends Object> optionValues, final List<String> optionLabels, final Object selectionHolder,
			final String selectionPropertyName) {
		Assert.isNotNull(optionValues, "optionValues"); //$NON-NLS-1$
		Assert.isNotNull(selectionHolder, "selectionHolder"); //$NON-NLS-1$
		Assert.isNotNull(selectionPropertyName, "selectionPropertyName"); //$NON-NLS-1$
		final IObservableList optionList = PojoObservables.observeList(new ListBean(optionValues), ListBean.PROPERTY_VALUES);
		IObservableList selectionList;
		if (AbstractSWTRidget.isBean(selectionHolder.getClass())) {
			selectionList = BeansObservables.observeList(selectionHolder, selectionPropertyName);
		} else {
			selectionList = PojoObservables.observeList(selectionHolder, selectionPropertyName);
		}
		bindToModel(optionList, optionLabels, selectionList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateFromModel() {
		assertIsBoundToModel();
		super.updateFromModel();
		optionsBinding.updateModelToTarget();
		final List<?> oldSelection = new ArrayList<Object>(selectionObservable);
		selectionBinding.updateModelToTarget();
		final ChoiceComposite control = getUIControl();
		final int oldCount = getChildrenCount(control);
		disposeChildren(control);
		createChildren(control);
		final int newCount = getChildrenCount(control);
		if (oldCount != newCount) {
			// if the number of children has changed
			// update the layout of the parent composite
			control.getParent().layout(true, false);
		}
		// remove unavailable elements and re-apply selection
		for (final Object candidate : oldSelection) {
			if (!optionsObservable.contains(candidate)) {
				selectionObservable.remove(candidate);
			}
		}
		firePropertyChange(PROPERTY_SELECTION, oldSelection, selectionObservable);
	}

	public IObservableList getObservableSelectionList() {
		return selectionObservable;
	}

	@SuppressWarnings("unchecked")
	public List<?> getSelection() {
		return new ArrayList<Object>(selectionObservable);
	}

	@SuppressWarnings("unchecked")
	public void setSelection(final List<?> selection) {
		assertIsBoundToModel();
		final List<?> oldSelection = new ArrayList<Object>(selectionObservable);
		final List<?> newSelection = selection == null ? Collections.EMPTY_LIST : selection;
		for (final Object candidate : newSelection) {
			if (!optionsObservable.contains(candidate)) {
				throw new BindingException("candidate not in option list: " + candidate); //$NON-NLS-1$
			}
		}
		selectionObservable.clear();
		selectionObservable.addAll(newSelection);
		updateSelection(getUIControl());
		firePropertyChange(PROPERTY_SELECTION, oldSelection, newSelection);
	}

	public IObservableList getObservableList() {
		return optionsObservable;
	}

	@Override
	public boolean isDisableMandatoryMarker() {
		return hasInput();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.2
	 */
	public void addSelectionListener(final ISelectionListener selectionListener) {
		Assert.isNotNull(selectionListener, "selectionListener is null"); //$NON-NLS-1$
		if (selectionListeners == null) {
			selectionListeners = new ListenerList<ISelectionListener>(ISelectionListener.class);
			addPropertyChangeListener(IChoiceRidget.PROPERTY_SELECTION, new PropertyChangeListener() {
				public void propertyChange(final PropertyChangeEvent evt) {
					notifySelectionListeners((List<?>) evt.getOldValue(), (List<?>) evt.getNewValue());
				}
			});
		}
		selectionListeners.add(selectionListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.2
	 */
	public void removeSelectionListener(final ISelectionListener selectionListener) {
		if (selectionListeners != null) {
			selectionListeners.remove(selectionListener);
		}
	}

	// helping methods
	// ////////////////

	private void assertIsBoundToModel() {
		if (optionsBinding == null || selectionBinding == null) {
			throw new BindingException("ridget not bound to model"); //$NON-NLS-1$
		}
	}

	private void bindToModel(final IObservableList optionValues, final List<String> optionLabels, final IObservableList selectionValues) {
		if (optionLabels != null) {
			final String msg = "Mismatch between number of optionValues and optionLabels"; //$NON-NLS-1$
			Assert.isLegal(optionValues.size() == optionLabels.size(), msg);
		}

		unbindUIControl();

		// clear observables as they may be bound to another model
		// must dispose old bindings first to avoid updating the old model
		if (optionsBinding != null) {
			optionsBinding.dispose();
			optionsBinding = null;
			optionsObservable.clear();
		}
		if (selectionBinding != null) {
			selectionBinding.dispose();
			selectionBinding = null;
			selectionObservable.clear();
		}

		// set up new binding
		final DataBindingContext dbc = new DataBindingContext();
		optionsBinding = dbc.bindList(optionsObservable, optionValues, new UpdateListStrategy(UpdateListStrategy.POLICY_UPDATE), new UpdateListStrategy(
				UpdateListStrategy.POLICY_ON_REQUEST));
		selectionBinding = dbc.bindList(selectionObservable, selectionValues, new UpdateListStrategy(UpdateListStrategy.POLICY_UPDATE), new UpdateListStrategy(
				UpdateListStrategy.POLICY_ON_REQUEST));
		if (optionLabels != null) {
			this.optionLabels = optionLabels.toArray(new String[optionLabels.size()]);
		} else {
			this.optionLabels = null;
		}

		bindUIControl();
	}

	private void createChildren(final ChoiceComposite control) {
		if (control != null && !control.isDisposed()) {
			final Object[] values = optionsObservable.toArray();
			for (int i = 0; i < values.length; i++) {
				final Object value = values[i];
				final String caption = optionLabels != null ? optionLabels[i] : String.valueOf(value);

				final Button button = control.createChild(caption);
				button.setData(value);
				button.addSelectionListener(new SelectionAdapter() {
					@SuppressWarnings("unchecked")
					@Override
					public void widgetSelected(final SelectionEvent e) {
						final Button button = (Button) e.widget;
						final Object data = button.getData();
						if (!isOutputOnly()) {
							if (button.getSelection()) {
								if (!selectionObservable.contains(data)) {
									final List<?> oldSelection = new ArrayList<Object>(selectionObservable);
									selectionObservable.add(data);
									firePropertyChange(PROPERTY_SELECTION, oldSelection, selectionObservable);
								}
							} else {
								final List<?> oldSelection = new ArrayList<Object>(selectionObservable);
								selectionObservable.remove(data);
								firePropertyChange(PROPERTY_SELECTION, oldSelection, selectionObservable);
							}
							if (!button.isDisposed()) {
								// this is a workaround to make composite table aware of focus changes, Bug #264627
								fireFocusIn(button.getParent());
							}
						}
					}
				});
			}
			updateSelection(control);
			LNF_UPDATER.updateUIControls(control, true);
		}
	}

	private void fireFocusIn(final Control control) {
		final Event event = new Event();
		event.type = SWT.FocusIn;
		event.widget = control;
		control.notifyListeners(SWT.FocusIn, event);
	}

	private boolean hasInput() {
		return selectionObservable != null && selectionObservable.size() > 0;
	}

	private void updateEditable(final ChoiceComposite control, final boolean isEditable) {
		if (control != null && !control.isDisposed()) {
			control.setEditable(isEditable);
		}
	}

	/**
	 * Iterates over the composite's children, disabling all buttons, except the one that has value as it's data element. If the ridget is not enabled, it may
	 * deselect all buttons, as mandated by {@link MarkerSupport#isHideDisabledRidgetContent()}.
	 */
	private void updateSelection(final ChoiceComposite control) {
		final boolean canSelect = isEnabled() || !MarkerSupport.isHideDisabledRidgetContent();
		if (control != null && !control.isDisposed()) {
			for (final Control child : control.getChildrenButtons()) {
				final Button button = (Button) child;
				final boolean isSelected = canSelect && selectionObservable.contains(button.getData());
				button.setSelection(isSelected);
			}
		}
		updateEditable(control, !isOutputOnly());
	}

	private void notifySelectionListeners(final List<?> oldSelectionList, final List<?> newSelectionList) {
		if (selectionListeners != null) {
			final org.eclipse.riena.ui.ridgets.listener.SelectionEvent event = new org.eclipse.riena.ui.ridgets.listener.SelectionEvent(this, oldSelectionList,
					newSelectionList);
			for (final ISelectionListener listener : selectionListeners.getListeners()) {
				listener.ridgetSelected(event);
			}
		}
	}

}
