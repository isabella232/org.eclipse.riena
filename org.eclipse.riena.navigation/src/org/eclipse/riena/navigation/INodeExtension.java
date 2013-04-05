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

import org.eclipse.riena.core.injector.extension.DoNotReplaceSymbols;
import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;

/**
 * Common interface for all navigation node extensions that define how to create
 * a node or a subtree in the application model tree.
 */
@ExtensionInterface
public interface INodeExtension {

	/**
	 * Returns the type part of the ID of a navigation node.
	 * 
	 * @return the type part of the ID
	 * @see NavigationNodeId#getTypeId()
	 */
	@DoNotReplaceSymbols
	String getTypeId();

	/**
	 * Returns the instance part of the ID of a navigation node.
	 * 
	 * @return the instance part of the ID
	 * @see NavigationNodeId#getInstanceId()
	 */
	@DoNotReplaceSymbols
	String getInstanceId();

	/**
	 * Returns the child navigation node definitions of the receiver. These may
	 * be sub application-, module group-, module- or sub module node
	 * definitions.
	 * 
	 * @return child node definitions
	 * 
	 * @see ISubApplicationNodeExtension
	 * @see IModuleGroupNodeExtension
	 * @see IModuleNodeExtension
	 * @see ISubModuleNodeExtension
	 */
	INodeExtension[] getChildNodes();

	/**
	 * Children may also be added by using references to assemblies defined
	 * elsewhere.
	 * 
	 * @return An array of assembly references.
	 */
	@MapName("assembly")
	INavigationAssemblyExtension[] getAssemblies();
}
