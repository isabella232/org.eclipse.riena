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
package org.eclipse.riena.navigation;

/**
 * Describes the ability of registering a NavigationHistoryListener.
 */
public interface INavigationHistoryListenerable {

	/**
	 * Adds the given listener.
	 * 
	 * @param pListener
	 *            history listener to add
	 */
	void addNavigationHistoryListener(INavigationHistoryListener pListener);

	/**
	 * Removes the given listener.
	 * 
	 * @param pListener
	 *            history listener to remove
	 */
	void removeNavigationHistoryListener(INavigationHistoryListener pListener);

	/**
	 * Answer the current size of the next navigation history
	 * 
	 * @return the amount of navigation nodes on the navigation stack
	 */
	int getHistoryBackSize();

	/**
	 * Answer the current size of the forward navigation history
	 * 
	 * @return the amount of navigation nodes on the navigation stack
	 */
	int getHistoryForwardSize();
}
