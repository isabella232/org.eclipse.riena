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

import java.util.List;

/**
 * Implementation of this service interface provides service methods to get
 * information provided by WorkAreaPresentationDefinitions and
 * NavigationNodePresentationDefitinios identified by a given presentationID.
 */
public interface INavigationNodeProvider {

	/**
	 * Returns a navigationNode identified by the given navigationNodeId. The
	 * node is created if it not yet exists.
	 * 
	 * @param sourceNode
	 *            an existing node in the navigation model
	 * @param targetId
	 *            the ID of the target node
	 * @param argument
	 *            contains information passed used for providing the target node
	 * @return target node
	 */
	INavigationNode<?> provideNode(INavigationNode<?> sourceNode, NavigationNodeId targetId, NavigationArgument argument);

	/**
	 * Return a sorted list of startup nodes.
	 * 
	 * @return sorted list of startup nodes
	 * @since 1.2
	 */
	List<StartupNodeInfo> getSortedStartupNodeInfos();

	/**
	 * Perform some housekeeping.
	 */
	void cleanUp();

}
