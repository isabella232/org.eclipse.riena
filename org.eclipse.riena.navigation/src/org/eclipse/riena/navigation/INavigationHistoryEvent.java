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

import java.util.List;

/**
 * This event is fired, when the navigation history is changed.
 */
public interface INavigationHistoryEvent {

	/**
	 * Returns the number of all nodes inside he history stack at the time the
	 * event was fired.
	 * 
	 * @return number of nodes in the history
	 */
	int getHistorySize();

	/**
	 * Returns the list of all nodes inside the history stack at the time the
	 * event was fired.
	 * 
	 * @return list of nodes in the history
	 */
	List<INavigationNode<?>> getHistoryNodes();

}
