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
package org.eclipse.riena.navigation.model;

import java.util.List;

import org.eclipse.riena.navigation.INavigationHistoryEvent;
import org.eclipse.riena.navigation.INavigationNode;

/**
 * This event is fired in case of a change in the navigation history. The event
 * is fired independently for the backward and forward history.
 */
public class NavigationHistoryEvent implements INavigationHistoryEvent {

	private List<INavigationNode<?>> historyNodes = null;

	public NavigationHistoryEvent(final List<INavigationNode<?>> list) {
		historyNodes = list;
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationHistoryEvent#getHistoryNodes()
	 */
	public List<INavigationNode<?>> getHistoryNodes() {
		return historyNodes;
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationHistoryEvent#getHistorySize()
	 */
	public int getHistorySize() {
		return historyNodes.size();
	}

}
