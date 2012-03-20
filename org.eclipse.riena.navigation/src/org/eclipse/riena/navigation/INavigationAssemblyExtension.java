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

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;
import org.eclipse.riena.navigation.extension.ICommonNavigationAssemblyExtension;

/**
 * Interface for a NavigationNodeType extension that defines how to create a
 * node or a subtree in the application model tree.
 */
@ExtensionInterface
public interface INavigationAssemblyExtension extends ICommonNavigationAssemblyExtension {

	String EXTENSIONPOINT = "org.eclipse.riena.navigation.assemblies"; //$NON-NLS-1$

	/**
	 * Returns a sub-application definitions of this assembly.
	 * 
	 * @return sub-application node definition
	 */
	@MapName("subapplication")
	ISubApplicationNodeExtension getSubApplicationNode();

	/**
	 * Returns a module group definitions of this assembly.
	 * 
	 * @return module group node definition
	 */
	@MapName("modulegroup")
	IModuleGroupNodeExtension getModuleGroupNode();

	/**
	 * Returns a module definitions of this assembly.
	 * 
	 * @return module node definition
	 */
	@MapName("module")
	IModuleNodeExtension getModuleNode();

	/**
	 * Returns a sub-module definitions of this assembly.
	 * 
	 * @return sub-module node definition
	 */
	@MapName("submodule")
	ISubModuleNodeExtension getSubModuleNode();

	/**
	 * Returns the ID of the parent indicating where to insert a node or subtree
	 * created with this definition in the application model tree.
	 * 
	 * @return ID of the parent node
	 */
	@MapName("parentTypeId")
	String getParentNodeId();

	/**
	 * Returns the index that this assembly takes in the system startup
	 * sequence. 0 or less indicates that automatic startup of this assembly is
	 * not desired.
	 * 
	 * @return >0 start order; otherwise no auto start
	 */
	@MapName("autostartsequence")
	int getStartOrder();

}
