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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.riena.beans.common.ListBean;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IFilterableContentRidget;
import org.eclipse.riena.ui.ridgets.IListRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContentFilter;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.ITreeRidget;

public class ContentFilterSubModuleController extends SubModuleController {
	public static class Something {
		private final String name;
		private final List<Something> children;

		Something(final String name) {
			this(name, new ArrayList<Something>());
		}

		Something(final String name, final List<Something> children) {
			this.name = name;
			this.children = children;
		}

		public String getName() {
			return name;
		}

		public List<Something> getChildren() {
			return children;
		}
	}

	private class HideWeekendFilter implements IRidgetContentFilter {
		public boolean isElementVisible(final Object parentElement, final Object element) {
			final Something s = (Something) element;
			return !s.getChildren().isEmpty() || !isWeekend(s);
		}
	}

	private class HideWeekdaysFilter implements IRidgetContentFilter {
		public boolean isElementVisible(final Object parentElement, final Object element) {
			final Something s = (Something) element;
			return !s.getChildren().isEmpty() || isWeekend(s);
		}
	}

	private static final List<Something> DAYS = Arrays.asList(new Something("Monday"), new Something("Tuesday"), new Something("Wednesday"), new Something(
			"Thursday"), new Something("Friday"), new Something("Saturday"), new Something("Sunday"));
	private static final List<String> RIDGET_IDS = Arrays.asList("list", "table", "tree");

	private final IRidgetContentFilter hideWeekendFilter = new HideWeekendFilter();
	private final IRidgetContentFilter hideWeekdaysFilter = new HideWeekdaysFilter();

	public ContentFilterSubModuleController() {
		this(null);
	}

	public ContentFilterSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.navigation.ui.controllers.SubModuleController#configureRidgets()
	 */
	@Override
	public void configureRidgets() {
		final IListRidget list = getRidget(IListRidget.class, "list");
		list.bindToModel(new ListBean(DAYS), ListBean.PROPERTY_VALUES, Something.class, "name");

		final ITableRidget table = getRidget(ITableRidget.class, "table");
		table.bindToModel(new ListBean(DAYS), ListBean.PROPERTY_VALUES, Something.class, new String[] { "name" }, new String[] { "day" });

		final ITreeRidget tree = getRidget(ITreeRidget.class, "tree");
		tree.bindToModel(new Object[] { new Something("Week", DAYS) }, Something.class, "children", "name", "name");

		updateAllRidgetsFromModel();

		final IToggleButtonRidget hideWeekdays = getRidget(IToggleButtonRidget.class, "hideWeekdays");
		hideWeekdays.addListener(new IActionListener() {
			public void callback() {
				if (hideWeekdays.isSelected()) {
					setFilter(hideWeekdaysFilter);
				} else {
					unsetFilter(hideWeekdaysFilter);
				}
			}
		});
		final IToggleButtonRidget hideWeekend = getRidget(IToggleButtonRidget.class, "hideWeekend");
		hideWeekend.addListener(new IActionListener() {
			public void callback() {
				if (hideWeekend.isSelected()) {
					setFilter(hideWeekendFilter);
				} else {
					unsetFilter(hideWeekendFilter);
				}
			}
		});
	}

	private void setFilter(final IRidgetContentFilter filter) {
		for (final String id : RIDGET_IDS) {
			getRidget(IFilterableContentRidget.class, id).addFilter(filter);
		}
	}

	private void unsetFilter(final IRidgetContentFilter filter) {
		for (final String id : RIDGET_IDS) {
			getRidget(IFilterableContentRidget.class, id).removeFilter(filter);
		}
	}

	private boolean isWeekend(final Something element) {
		return "Saturday".equals(element.getName()) || "Sunday".equals(element.getName());
	}
}
