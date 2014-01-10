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

/**
 * Factory that creates {@code IController} instances.
 * 
 * @since 3.0
 */
public interface IControllerFactory {

	/**
	 * Create an {@code IController} instance.
	 * 
	 * @return a controller
	 */
	IController createController();

}
