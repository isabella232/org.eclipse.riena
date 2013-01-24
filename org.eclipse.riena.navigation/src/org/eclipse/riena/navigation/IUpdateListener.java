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
package org.eclipse.riena.navigation;

/**
 * Listener for changes performed. The {@code NavigationArgument} itself is
 * immutable and cannot be changed, but the change listener could notify a
 * listener (i.e. another controller) that a passed object has changed.
 * 
 * @see INavigationNode#navigate(NavigationNodeId, NavigationArgument)
 */
public interface IUpdateListener {

	/**
	 * Called when the Object passed as an argument during a navigation was
	 * changed.
	 * 
	 * @param argument
	 *            The changed argument.
	 */
	void handleUpdate(Object argument);

}
