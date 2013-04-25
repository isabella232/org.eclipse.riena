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

import java.util.Collection;

import org.eclipse.riena.core.service.Service;
import org.eclipse.riena.example.client.views.SharedViewDemoSubModuleView;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.NavigationNodeUtility;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.filter.IUIFilter;
import org.eclipse.riena.ui.filter.IUIFilterContainer;
import org.eclipse.riena.ui.filter.IUIFilterProvider;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.workarea.WorkareaManager;

/**
 *
 */
public class FilterExampleSubModuleController extends SubModuleController {

	public enum FilterId {
		MENUITEM("rienaExample.menuToolBar"), //$NON-NLS-1$
		NAVIGATION("rienaExample.navigation"), //$NON-NLS-1$
		RIDGET("rienaExample.ridget"), //$NON-NLS-1$
		RIDGET_01("rienaExample.ridget01"), //$NON-NLS-1$
		RIDGET_HIDE("rienaExample.ridgetHide"), //$NON-NLS-1$
		RIDGET_DISABLE("rienaExample.ridgetDisable"); //$NON-NLS-1$

		private final String id;

		FilterId(final String id) {
			this.id = id;
		}

		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return id;
		}

	}

	private final static String SELECTED_TEXT = "deactivate"; //$NON-NLS-1$
	private final static String NOT_SELECTED_TEXT = "activate"; //$NON-NLS-1$

	@Override
	public void configureRidgets() {

		super.configureRidgets();

		final IToggleButtonRidget navigationAction = getRidget(IToggleButtonRidget.class, "navigationBtn"); //$NON-NLS-1$
		updateToggleText(navigationAction);
		navigationAction.addListener(new IActionListener() {
			public void callback() {
				doFilter(FilterId.NAVIGATION, "navigationBtn"); //$NON-NLS-1$
			}
		});

		final IToggleButtonRidget menuToolAction = getRidget(IToggleButtonRidget.class, "menuToolItemBtn"); //$NON-NLS-1$
		updateToggleText(menuToolAction);
		menuToolAction.addListener(new IActionListener() {
			public void callback() {
				doFilter(FilterId.MENUITEM, "menuToolItemBtn"); //$NON-NLS-1$
			}
		});

		final IToggleButtonRidget ridgetAction = getRidget(IToggleButtonRidget.class, "ridgetBtn"); //$NON-NLS-1$
		updateToggleText(ridgetAction);
		ridgetAction.addListener(new IActionListener() {
			public void callback() {
				doFilter(FilterId.RIDGET, "ridgetBtn"); //$NON-NLS-1$
			}
		});

		final IToggleButtonRidget ridgetDisableAction = getRidget(IToggleButtonRidget.class, "ridgetDisableBtn"); //$NON-NLS-1$
		updateToggleText(ridgetAction);
		ridgetDisableAction.addListener(new IActionListener() {
			public void callback() {
				doFilter(FilterId.RIDGET_DISABLE, "ridgetDisableBtn"); //$NON-NLS-1$
			}
		});

		final IToggleButtonRidget ridgetHideAction = getRidget(IToggleButtonRidget.class, "ridgetHideBtn"); //$NON-NLS-1$
		updateToggleText(ridgetAction);
		ridgetHideAction.addListener(new IActionListener() {
			public void callback() {
				doFilter(FilterId.RIDGET_HIDE, "ridgetHideBtn"); //$NON-NLS-1$
			}
		});

		final IToggleButtonRidget ridget01Action = getRidget(IToggleButtonRidget.class, "ridget01Btn"); //$NON-NLS-1$
		updateToggleText(ridgetAction);
		ridget01Action.addListener(new IActionListener() {
			public void callback() {
				doFilter(FilterId.RIDGET_01, "ridget01Btn"); //$NON-NLS-1$
			}
		});

		final MySampleBean sampleBean = new MySampleBean();

		final ITextRidget sampleText = getRidget(ITextRidget.class, "sampleText"); //$NON-NLS-1$
		sampleText.setMandatory(true);
		sampleText.bindToModel(sampleBean, "text"); //$NON-NLS-1$
		sampleText.updateFromModel();

		final IActionRidget addFilteredNode = getRidget(IActionRidget.class, "addNode"); //$NON-NLS-1$
		addFilteredNode.addListener(new IActionListener() {
			public void callback() {
				final ISubModuleNode sharedViewSm10 = new SubModuleNode(new NavigationNodeId("newx", "10" + System.currentTimeMillis()), "new x"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				WorkareaManager.getInstance().registerDefinition(sharedViewSm10, SharedViewDemoSubModuleController.class, SharedViewDemoSubModuleView.ID, true);
				getNavigationNode().getParentOfType(ModuleNode.class).addChild(sharedViewSm10);

			}
		});

	}

	private void doFilter(final FilterId filterId, final String buttonRidgetId) {

		final IToggleButtonRidget action = getRidget(IToggleButtonRidget.class, buttonRidgetId);

		final IApplicationNode applNode = getNavigationNode().getParentOfType(IApplicationNode.class);
		final IUIFilterContainer container = Service.get(IUIFilterProvider.class).provideFilter(filterId.toString());
		final IUIFilter filter = container.getFilter();
		final Collection<String> targetNodeIds = container.getFilterTargetNodeIds();
		for (final String targetNodeId : targetNodeIds) {
			final INavigationNode<?> node = NavigationNodeUtility.findNodesByLongId(targetNodeId, applNode).get(0);
			if (action.isSelected()) {
				node.addFilter(filter);
			} else {
				node.removeFilter(filter.getFilterID());
			}
		}

		updateToggleText(action);

	}

	/**
	 * Updates the text of the given toggle button.
	 * 
	 * @param toggle
	 *            toggle button
	 */
	private void updateToggleText(final IToggleButtonRidget toggle) {

		if (toggle.isSelected()) {
			toggle.setText(SELECTED_TEXT);
		} else {
			toggle.setText(NOT_SELECTED_TEXT);
		}

	}

	private static class MySampleBean {

		private String text = ""; //$NON-NLS-1$

		@SuppressWarnings("unused")
		public void setText(final String text) {
			this.text = text;
		}

		@SuppressWarnings("unused")
		public String getText() {
			return text;
		}

	}

}
