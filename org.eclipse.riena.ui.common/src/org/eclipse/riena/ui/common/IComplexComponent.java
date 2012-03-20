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
package org.eclipse.riena.ui.common;

import java.util.List;

/**
 * A UI-control that contains other UI-controls. When bound to a IComplexRidget
 * a Ridget will be created and injected into the IComplexRidget for every
 * UI-control the complex control contains.
 * 
 * @see IComplexRidget
 */
public interface IComplexComponent {

	/**
	 * Gets the UI-controls of the complex component.
	 * 
	 * @return the List of controls
	 */
	List<Object> getUIControls();

}
