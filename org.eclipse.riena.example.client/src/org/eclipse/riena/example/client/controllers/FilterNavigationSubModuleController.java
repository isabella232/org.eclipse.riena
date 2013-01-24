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
package org.eclipse.riena.example.client.controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.list.WritableList;

import org.eclipse.riena.internal.navigation.ui.filter.UIFilterRuleNavigationDisabledMarker;
import org.eclipse.riena.internal.navigation.ui.filter.UIFilterRuleNavigationHiddenMarker;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.model.SimpleNavigationNodeAdapter;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.ui.controllers.SubApplicationController;
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
public class FilterNavigationSubModuleController extends SubModuleController {

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

		getNavigationNode().addSimpleListener(new SimpleNavigationNodeAdapter() {
			@Override
			public void afterActivated(final INavigationNode<?> source) {
				super.afterActivated(source);
				final SubApplicationNode subAppNode = getNavigationNode().getParentOfType(SubApplicationNode.class);
				final SubApplicationController subApp = (SubApplicationController) subAppNode.getNavigationNodeController();
				final IActionRidget ridget = subApp.getMenuActionRidget("org.eclipse.riena.example.client.histBackMenuItem"); //$NON-NLS-1$
				ridget.setEnabled(false);
			}
		});

	}

	/**
	 * Initializes the ridgets for adding UI filters.
	 */
	private void initNavigationFilterGroup() {

		final ITextRidget ridgetID = getRidget("nodeId"); //$NON-NLS-1$
		filterModel = new FilterModel();
		ridgetID.bindToModel(filterModel, "nodeId"); //$NON-NLS-1$
		ridgetID.updateFromModel();

		final ISingleChoiceRidget filterType = getRidget("filterType"); //$NON-NLS-1$		
		filterType.addPropertyChangeListener(new FilterTypeChangeListener());
		filterType.bindToModel(filterModel, "types", filterModel, "selectedType"); //$NON-NLS-1$ //$NON-NLS-2$
		filterType.updateFromModel();

		filterTypeValues = getRidget("filterTypeValues"); //$NON-NLS-1$
		filterTypeValues.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				if (addFilter != null) {
					addFilter.setEnabled(evt.getNewValue() != null);
				}
			}
		});

		addFilter = getRidget("addFilter"); //$NON-NLS-1$
		addFilter.addListener(new IActionListener() {
			public void callback() {
				doAddFilter();
			}
		});

		final IActionRidget removeFilters = getRidget("removeFilters"); //$NON-NLS-1$
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

		final IApplicationNode applNode = getNavigationNode().getParentOfType(IApplicationNode.class);
		final Collection<IUIFilterRule> rules = new ArrayList<IUIFilterRule>(1);
		rules.add(createFilterRule(filterModel, filterModel.getNodeId()));
		final IUIFilter filter = new UIFilter(rules);
		applNode.addFilter(filter);

	}

	/**
	 * Removes all filters form a node.
	 */
	private void doRemoveFilters() {

		final IApplicationNode applNode = getNavigationNode().getParentOfType(IApplicationNode.class);
		applNode.removeAllFilters();

	}

	/**
	 * Creates a filter rule for a ridget, dependent on the selected type of filter.
	 * 
	 * @param model
	 *            model with selections.
	 * @return filter rule
	 */
	private IUIFilterRule createFilterRule(final FilterModel model, final String nodeId) {

		IUIFilterRule attribute = null;

		final Object filterValue = model.getSelectedFilterTypeValue();
		final FilterType type = model.getSelectedType();

		if (type == FilterType.MARKER) {
			if (filterValue instanceof DisabledMarker) {
				attribute = new UIFilterRuleNavigationDisabledMarker(nodeId);
			} else if (filterValue instanceof HiddenMarker) {
				attribute = new UIFilterRuleNavigationHiddenMarker(nodeId);
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

		private String nodeId;
		private List<FilterType> types;
		private FilterType selectedType;
		private Object selectedFilterTypeValue;

		public FilterModel() {
			nodeId = ""; //$NON-NLS-1$
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

		@SuppressWarnings("unused")
		public void setNodeId(final String nodeId) {
			this.nodeId = nodeId;
		}

		public String getNodeId() {
			return nodeId;
		}

	}

}
