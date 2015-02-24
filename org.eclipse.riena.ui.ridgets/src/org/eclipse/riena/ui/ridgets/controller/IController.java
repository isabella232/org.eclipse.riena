/*******************************************************************************
 * Copyright (c) 2007, 2014 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets.controller;

import org.eclipse.riena.core.annotationprocessor.DisposerList;
import org.eclipse.riena.core.annotationprocessor.IDisposer;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;

/**
 * Controller for a view. Must have a property for every UI control added to the
 * view. The type of the property must be a Ridget matching the type of the
 * UI-control.
 */
public interface IController extends IRidgetContainer {

	/**
	 * Add {@link IDisposer}s, which will be called if the controller is going to be disposed.
	 * 
	 * @param list
	 *            A list with new {@link IDisposer} elements.
	 * @since 6.1
	 */
	void addAnnotationDisposerList(DisposerList list);

	/**
	 * Invoked after the controller was bound to a view.
	 */
	void afterBind();

	/**
	 * Blocks of unblocks the user input for the view to which this controller
	 * is bound.
	 * 
	 * @param blocked
	 *            {@code true} if input is blocked for the view; otherwise
	 *            {@code false}
	 */
	void setBlocked(boolean blocked);

	/**
	 * Returns true if user input for the view to which this controller is bound
	 * is blocked.
	 * 
	 * @return {@code true} if input is blocked for the view; otherwise
	 *         {@code false}
	 */
	boolean isBlocked();

}
