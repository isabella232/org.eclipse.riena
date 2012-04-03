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
package org.eclipse.riena.example.client.controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.list.WritableList;

import org.eclipse.riena.internal.navigation.ui.filter.UIFilterRuleMenuItemDisabledMarker;
import org.eclipse.riena.internal.navigation.ui.filter.UIFilterRuleMenuItemHiddenMarker;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.filter.IUIFilter;
import org.eclipse.riena.ui.filter.IUIFilterRule;
import org.eclipse.riena.ui.filter.impl.UIFilter;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 * Controller of the sub module that demonstrates UI filters for navigation nodes.
 */
public class FilterActionSubModuleController extends SubModuleController {

	private IActionRidget addFilter;
	private IComboRidget filterTypeValues;
	private FilterModel filterModel;

	/**
	 * Enumeration of different kind of UI filters.
	 */
	private enum FilterType {

		MARKER("Marker", new DisabledMarker(), new HiddenMarker()); //$NON-NLS-1$

		private final String text;
		private final Object[] args;

		private FilterType(final String text, final Object... args) {
			this.text = text;
			this.args = args;
		}

		@Override
		public String toString() {
			return text;
		}

		public Object[] getArgs() {
			return args;
		}

	}

	@Override
	public void afterBind() {

		super.afterBind();

		initNavigationFilterGroup();

		rebindFilterTypeValues(filterModel, filterTypeValues, addFilter);

	}

	@Override
	public void configureRidgets() {
		super.configureRidgets();
		filterModel = new FilterModel();
		final ISingleChoiceRidget filterType = getRidget(ISingleChoiceRidget.class, "filterType"); //$NON-NLS-1$		
		filterType.addPropertyChangeListener(new FilterTypeChangeListener());
		filterType.bindToModel(filterModel, "types", filterModel, "selectedType"); //$NON-NLS-1$ //$NON-NLS-2$
		filterType.updateFromModel();
	}

	/**
	 * Initializes the ridgets for adding UI filters.
	 */
	private void initNavigationFilterGroup() {

		final ITextRidget itemId = getRidget(ITextRidget.class, "itemId"); //$NON-NLS-1$
		itemId.bindToModel(filterModel, "itemId"); //$NON-NLS-1$
		itemId.updateFromModel();

		filterTypeValues = getRidget(IComboRidget.class, "filterTypeValues"); //$NON-NLS-1$
		filterTypeValues.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				if (addFilter != null) {
					addFilter.setEnabled(evt.getNewValue() != null);
				}
			}
		});

		addFilter = getRidget(IActionRidget.class, "addFilter"); //$NON-NLS-1$
		addFilter.addListener(new IActionListener() {
			public void callback() {
				doAddFilter();
			}
		});

		final IActionRidget removeFilters = getRidget(IActionRidget.class, "removeFilters"); //$NON-NLS-1$
		removeFilters.addListener(new IActionListener() {
			public void callback() {
				doRemoveFilters();
			}
		});

	}

	/**
	 * The combo box for filter values is update with the given model. Also the add button is enabled or disabled.
	 * 
	 * @param model
	 * @param typeValues
	 *            combo box
	 * @param add
	 *            add button
	 */
	private void rebindFilterTypeValues(final FilterModel model, final IComboRidget typeValues, final IActionRidget add) {

		if (model == null) {
			return;
		}
		model.setSelectedFilterTypeValue(null);
		if (typeValues != null) {
			typeValues.bindToModel(new WritableList(Arrays.asList(model.getSelectedType().getArgs()), Object.class), FilterModel.class, null,
					PojoObservables.observeValue(model, "selectedFilterTypeValue")); //$NON-NLS-1$
			typeValues.updateFromModel();
		}
		if (add != null) {
			add.setEnabled(model.getSelectedFilterTypeValue() != null);
		}

	}

	/**
	 * Adds a filter to a node.
	 */
	private void doAddFilter() {

		final ISubApplicationNode subApp = getNavigationNode().getParentOfType(ISubApplicationNode.class);
		final Collection<IUIFilterRule> attributes = new ArrayList<IUIFilterRule>(1);
		attributes.add(createFilterAttribute(filterModel));
		final IUIFilter filter = new UIFilter(attributes);
		subApp.addFilter(filter);

	}

	/**
	 * Removes all filters form a node.
	 */
	private void doRemoveFilters() {

		final ISubApplicationNode subApp = getNavigationNode().getParentOfType(ISubApplicationNode.class);
		subApp.removeAllFilters();

	}

	/**
	 * Creates a filter attribute for a ridget, dependent on the selected type of filter.
	 * 
	 * @param model
	 *            model with selections.
	 * @return filter attribute
	 */
	private IUIFilterRule createFilterAttribute(final FilterModel model) {

		IUIFilterRule attribute = null;

		final Object filterValue = model.getSelectedFilterTypeValue();
		final FilterType type = model.getSelectedType();

		if (type == FilterType.MARKER) {
			if (filterValue instanceof DisabledMarker) {
				attribute = new UIFilterRuleMenuItemDisabledMarker(model.getItemId());
			} else if (filterValue instanceof HiddenMarker) {
				attribute = new UIFilterRuleMenuItemHiddenMarker(model.getItemId());
			}
		}

		return attribute;

	}

	/**
	 * After changing the type the combo box with the values must be updated.
	 */
	private class FilterTypeChangeListener implements PropertyChangeListener {

		public void propertyChange(final PropertyChangeEvent evt) {
			rebindFilterTypeValues(filterModel, filterTypeValues, addFilter);
		}

	}

	/**
	 * Model with the filter types and value of the filter group.
	 */
	private static class FilterModel {

		private String itemId;
		private List<FilterType> types;
		private FilterType selectedType;
		private Object selectedFilterTypeValue;

		public FilterModel() {
			setItemId(""); //$NON-NLS-1$
		}

		public List<FilterType> getTypes() {
			if (types == null) {
				types = new ArrayList<FilterType>();
				types.add(FilterType.MARKER);
			}
			return types;
		}

		@SuppressWarnings("unused")
		public void setSelectedType(final FilterType selectedType) {
			this.selectedType = selectedType;
		}

		public FilterType getSelectedType() {
			if (selectedType == null) {
				selectedType = getTypes().get(0);
			}
			return selectedType;
		}

		public void setSelectedFilterTypeValue(final Object selectedFilterTypeValue) {
			this.selectedFilterTypeValue = selectedFilterTypeValue;
		}

		public Object getSelectedFilterTypeValue() {
			return selectedFilterTypeValue;
		}

		public void setItemId(final String itemId) {
			this.itemId = itemId;
		}

		public String getItemId() {
			return itemId;
		}

	}

}
