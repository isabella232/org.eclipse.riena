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
 * Defines the API for a NavigationHistoryListener. Every time when the
 * navigation history changes, the matching listener method is called.
 */
public interface INavigationHistoryListener {

	/**
	 * This method is called, when the backward history is changed.
	 * 
	 * @param event
	 *            history event
	 */
	void backHistoryChanged(INavigationHistoryEvent event);

	/**
	 * This method is called, when the forward history is changed.
	 * 
	 * @param event
	 *            history event
	 */
	void forwardHistoryChanged(INavigationHistoryEvent event);

}
