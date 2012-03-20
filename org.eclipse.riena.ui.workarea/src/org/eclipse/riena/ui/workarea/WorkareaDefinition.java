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
package org.eclipse.riena.ui.workarea;

import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.ui.ridgets.controller.IController;
import org.eclipse.riena.ui.ridgets.controller.IControllerFactory;

/**
 * A WorkareaDefinition consists of viewId and a {@link IController}. Also other
 * information for creating a view or a controller of a navigation node are
 * stored.
 */
public class WorkareaDefinition implements IWorkareaDefinition {

	private final Class<? extends IController> controllerClass;
	private final IControllerFactory controllerFactory;
	private final Object viewId;
	private boolean viewShared;
	private boolean requiredPreparation;

	/**
	 * Creates a new instance of {@code WorkareaDefinition} with only a view ID
	 * and the information whether the view is shared.
	 * 
	 * @param viewId
	 *            ID of the view (see <code>org.eclipse.ui.views</code>
	 *            extension point)
	 */
	public WorkareaDefinition(final Object viewId) {
		this((Class<? extends IController>) null, viewId);
	}

	/**
	 * Creates a new instance of {@code WorkareaDefinition} with controller
	 * class, view ID and the information whether the view is shared.
	 * 
	 * @param controllerClass
	 *            the controller class to be used with the view
	 * @param viewId
	 *            ID of the view (see <code>org.eclipse.ui.views</code>
	 *            extension point)
	 */
	public WorkareaDefinition(final Class<? extends IController> controllerClass, final Object viewId) {
		this.controllerClass = controllerClass;
		this.controllerFactory = null;
		this.viewId = viewId;
	}

	/**
	 * @since 3.0
	 */
	public WorkareaDefinition(final IControllerFactory controllerFactory, final Object viewId) {
		this.controllerFactory = controllerFactory;
		this.controllerClass = null;
		this.viewId = viewId;
	}

	public Class<? extends IController> getControllerClass() {
		return controllerClass;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return the created controller or {@code null} if no controller class
	 *         exists
	 */
	public IController createController() throws IllegalAccessException, InstantiationException {

		if (getControllerClass() != null) {
			final IController controller = getControllerClass().newInstance();
			// It is necessary to explicitly wire the controller here, because the controller is not created from an extension interface, which would do the wiring by default.
			Wire.instance(controller).andStart();
			return controller;
		} else if (controllerFactory != null) {
			return controllerFactory.createController();
		}
		return null;
	}

	public Object getViewId() {
		return viewId;
	}

	public boolean isViewShared() {
		return viewShared;
	}

	/**
	 * @since 2.0
	 */
	public boolean isRequiredPreparation() {
		return requiredPreparation;
	}

	/**
	 * @since 2.0
	 */
	public void setViewShared(final boolean shared) {
		this.viewShared = shared;
	}

	/**
	 * @since 2.0
	 */
	public void setRequiredPreparation(final boolean required) {
		this.requiredPreparation = required;
	}

}
