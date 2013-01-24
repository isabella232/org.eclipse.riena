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
package org.eclipse.riena.ui.ridgets;

/**
 * Facade for convenient access to {@link IActionRidget}s of
 * {@link IMasterDetailsRidget}
 * 
 * @since 3.0
 */
public interface IMasterDetailsActionRidgetFacade {

	/**
	 * 
	 * @return the "add" ridget
	 */
	IActionRidget getAddActionRidget();

	/**
	 * 
	 * @return the "apply" ridget
	 */
	IActionRidget getApplyActionRidget();

	/**
	 * 
	 * @return the "remove" ridget
	 */
	IActionRidget getRemoveActionRidget();

}
