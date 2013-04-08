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
package org.eclipse.riena.navigation.ui.controllers;

import java.util.Collection;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.internal.navigation.ui.Activator;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.listener.NavigationTreeObserver;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComplexRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IUIProcessRidget;
import org.eclipse.riena.ui.ridgets.SubModuleUtils;
import org.eclipse.riena.ui.workarea.IWorkareaDefinition;
import org.eclipse.riena.ui.workarea.WorkareaManager;

/**
 * Implements the Controller for a Module Sub Application
 */
public class SubApplicationController extends NavigationNodeController<ISubApplicationNode> {

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), SubApplicationController.class);

	private IUIProcessRidget uiProcessRidget;
	private final NodeEventDelegation contextUpdater = new NodeEventDelegation();

	private NavigationTreeObserver navigationTreeObserver;

	/**
	 * Create a new Controller, find the corresponding subApplication for the passed ID
	 */
	public SubApplicationController(final ISubApplicationNode pSubApplication) {
		super(pSubApplication);
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidgetContainer#configureRidgets()
	 */
	public void configureRidgets() {
		installNavigationListeners();
	}

	/**
	 * @since 5.0
	 */
	public void installNavigationListeners() {
		if (null != navigationTreeObserver) {
			return;
		}
		navigationTreeObserver = new NavigationTreeObserver();
		navigationTreeObserver.addListener(new MySubModuleNodeListener());
		navigationTreeObserver.addListenerTo(getNavigationNode());
	}

	@Override
	public void afterBind() {
		super.afterBind();
		initUiProcessRidget();
	}

	private void initUiProcessRidget() {
		if (uiProcessRidget == null) {
			// fallback
			uiProcessRidget = getRidget("uiProcessRidget"); //$NON-NLS-1$
			if (uiProcessRidget == null) {
				return;
			}
		}
		uiProcessRidget.setContextLocator(contextUpdater);
	}

	/**
	 * Returns the ridget of a menu action.
	 * 
	 * @param id
	 *            id of the menu item
	 * @return action ridget; {@code null} if no action ridget was found
	 */
	public IActionRidget getMenuActionRidget(final String id) {

		final String menuItemId = IActionRidget.BASE_ID_MENUACTION + id;
		return getActionRidget(menuItemId);

	}

	/**
	 * Returns the ridget of a tool bar action.
	 * 
	 * @param id
	 *            id of the tool bar button
	 * @return action ridget; {@code null} if no action ridget was found
	 */
	public IActionRidget getToolbarActionRidget(final String id) {

		final String menuItemId = IActionRidget.BASE_ID_TOOLBARACTION + id;
		return getActionRidget(menuItemId);

	}

	/**
	 * Returns the action ridget with given id.
	 * 
	 * @param id
	 *            id of the ridget
	 * @return action ridget; {@code null} if no action ridget was found
	 */
	private IActionRidget getActionRidget(final String id) {

		final IRidget ridget = getRidget(id);
		if (ridget instanceof IActionRidget) {
			return (IActionRidget) ridget;
		} else {
			return null;
		}

	}

	/**
	 * @return the progressBoxRidget
	 */
	public IUIProcessRidget getUiProcessRidget() {
		return uiProcessRidget;
	}

	/**
	 * @param uiProcessRidget
	 *            the progressBoxRidget to set
	 */
	public void setUiProcessRidget(final IUIProcessRidget uiProcessRidget) {
		this.uiProcessRidget = uiProcessRidget;
	}

	/**
	 * Prepares the controller for the given sub-module node.
	 * <p>
	 * Controller is created and the ridgets are configured. So the logic of the controller works without a binded view.
	 * 
	 * @param source
	 *            node of the sub-module.
	 */
	private void prepareController(final ISubModuleNode source) {
		final IWorkareaDefinition definition = WorkareaManager.getInstance().getDefinition(source);
		if (definition == null) {
			final String message = String.format("cannnot find definition for %s", source); //$NON-NLS-1$ 
			LOGGER.log(LogService.LOG_ERROR, message);
			return;
		}
		try {
			if (!definition.isRequiredPreparation()) {
				final String message = String.format(
						"controller for class %s will be prepared, although required preparation flag is not set", definition.getControllerClass()); //$NON-NLS-1$ 
				LOGGER.log(LogService.LOG_WARNING, message);
			}
			final SubModuleController controller = (SubModuleController) definition.createController();
			if (controller == null && definition.getControllerClass() == null) {
				final String message = String.format("no controller class is define for %s", source); //$NON-NLS-1$ 
				LOGGER.log(LogService.LOG_DEBUG, message);
				return;
			}
			controller.setNavigationNode(source);
			source.setNavigationNodeController(controller);
			controller.configureRidgets();
			controller.setConfigured(true);
			final Collection<? extends IRidget> ridgets = controller.getRidgets();
			for (final IRidget ridget : ridgets) {
				if (ridget instanceof IComplexRidget) {
					final IComplexRidget complexRidget = (IComplexRidget) ridget;
					complexRidget.configureRidgets();
					complexRidget.setConfigured(true);
				}
			}
		} catch (final Exception ex) {
			final String message = String.format("cannnot create controller for class %s", definition.getControllerClass()); //$NON-NLS-1$ 
			LOGGER.log(LogService.LOG_ERROR, message, ex);
		}
	}

	private class MySubModuleNodeListener extends SubModuleNodeListener {
		@Override
		public void prepared(final ISubModuleNode source) {

			if (!SubModuleUtils.isPrepareView() && (source.getNavigationNodeController() == null)) {
				// create only the controller and no view
				prepareController(source);
			}

		}
	}
}
