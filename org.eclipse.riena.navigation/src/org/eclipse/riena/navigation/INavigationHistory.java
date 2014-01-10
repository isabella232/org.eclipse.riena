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
package org.eclipse.riena.navigation;

/**
 * Gives access to the navigation history.
 */
public interface INavigationHistory {

	/**
	 * Navigates one step back in the navigation history.
	 * 
	 */
	void historyBack();

	/**
	 * Navigates one step forward in the navigation history.
	 * 
	 */
	void historyForward();

}
