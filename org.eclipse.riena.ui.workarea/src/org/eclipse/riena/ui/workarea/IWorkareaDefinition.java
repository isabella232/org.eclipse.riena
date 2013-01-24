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
package org.eclipse.riena.ui.workarea;

import org.eclipse.riena.ui.ridgets.controller.IController;

/**
 * A WorkareaDefinition consists of viewId and a {@link IController}.
 * 
 * @see IWorkareaDefinitionRegistry
 */
/**
 *
 */
public interface IWorkareaDefinition {

	/**
	 * Returns the ID of the view.
	 * 
	 * @return The id of this work areas view. Riena default behavior requires this to be the viewId as contributed to the <code>org.eclipse.ui.views</code>
	 *         extension point.
	 */
	Object getViewId();

	/**
	 * Returns whether should be shared or not.
	 * 
	 * @return <code>true</code> if the view associated with this node should be shared, <code>false</code> otherwise
	 */
	boolean isViewShared();

	/**
	 * Returns whether the navigation node should be prepared after the node is created. Preparation means that the controller is created (among other things).
	 * 
	 * @return {@code true} should be prepared; otherwise {@code false}
	 * @since 2.0
	 */
	boolean isRequiredPreparation();

	/**
	 * Returns the class to be used to create the controller.
	 * 
	 * @return The controller class to be used with the view representing the working area, NOT the tree.
	 */
	Class<? extends IController> getControllerClass();

	/**
	 * Creates the controller.
	 * 
	 * @return The controller to be used with the view representing the working area, NOT the tree.
	 */
	IController createController() throws IllegalAccessException, InstantiationException;

	/**
	 * Sets whether the view should be shared or not.
	 * 
	 * @param shared
	 *            <code>true</code> if the view associated with this node should be shared, <code>false</code> otherwise
	 * @since 2.0
	 */
	void setViewShared(boolean shared);

	/**
	 * Sets whether the navigation node should be prepared after the node is created. Preparation means that the controller is created (among other things).
	 * 
	 * @param required
	 *            {@code true} should be prepared; otherwise {@code false}
	 * @since 2.0
	 */
	void setRequiredPreparation(boolean required);

	/**
	 * Sets whether also the view is created when the navigation node should be prepared.
	 * 
	 * @see #setRequiredPreparation
	 * 
	 * @param prepare
	 *            {@code true} also prepare view; {@code false} create view while node is activated
	 * @since 5.0
	 */
	void setPrepareView(boolean prepare);

	/**
	 * Returns whether also the view is created when the navigation node should be prepared.
	 * 
	 * @see #isRequiredPreparation
	 * 
	 * @return {@code true} also prepare view; {@code false} create view while node is activated
	 * @since 5.0
	 */
	boolean isPrepareView();

}
