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
package org.eclipse.riena.navigation.extension;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.navigation.NavigationNodeId;

/**
 * Common interface for all navigation node extensions that define how to create
 * a node or a subtree in the application model tree.
 */
@ExtensionInterface
public interface INode2Extension {

	/**
	 * Returns the name (label) of the navigation node.
	 * 
	 * @return name (label)
	 */
	String getName();

	/**
	 * Returns the ID (type ID) of the navigation node.
	 * 
	 * @return type ID
	 * @see NavigationNodeId#getTypeId()
	 */
	String getNodeId();

	/**
	 * Returns the icon ID of the navigation node.
	 * 
	 * @return icon ID
	 */
	String getIcon();

	/**
	 * Returns all definitions of all child navigation nodes. These may be sub
	 * application-, module group-, module- or sub module node definitions.
	 * 
	 * @return child node definitions
	 * 
	 * @see ISubApplicationNode2Extension
	 * @see IModuleGroupNode2Extension
	 * @see IModuleNode2Extension
	 * @see ISubModuleNode2Extension
	 */
	INode2Extension[] getChildNodes();

}
