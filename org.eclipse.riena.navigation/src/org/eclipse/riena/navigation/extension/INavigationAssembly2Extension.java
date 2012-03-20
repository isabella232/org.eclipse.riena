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
package org.eclipse.riena.navigation.extension;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;

/**
 * Interface for a (assembly) extension that defines how to create a node or a
 * subtree in the application model tree.
 */
@ExtensionInterface
public interface INavigationAssembly2Extension extends ICommonNavigationAssemblyExtension {

	String EXTENSIONPOINT = "org.eclipse.riena.navigation.assemblies2"; //$NON-NLS-1$

	/**
	 * Returns all sub-application definitions of this assembly.
	 * 
	 * @return sub-application group definitions
	 */
	@MapName("subApplication")
	ISubApplicationNode2Extension[] getSubApplications();

	/**
	 * Returns all module group definitions of this assembly.
	 * 
	 * @return module group definitions
	 */
	@MapName("moduleGroup")
	IModuleGroupNode2Extension[] getModuleGroups();

	/**
	 * Returns all module definitions of this assembly.
	 * 
	 * @return module definitions
	 */
	@MapName("module")
	IModuleNode2Extension[] getModules();

	/**
	 * Returns all sub-module definitions of this assembly.
	 * 
	 * @return sub-module definitions
	 */
	@MapName("subModule")
	ISubModuleNode2Extension[] getSubModules();

	/**
	 * Returns the ID of the parent indicating where to insert a node or subtree
	 * created with this definition in the application model tree.
	 * 
	 * @return ID of the parent node
	 */
	String getParentNodeId();

	/**
	 * Returns the index that this assembly takes in the system startup
	 * sequence. 0 or less indicates that automatic startup of this assembly is
	 * not desired.
	 * 
	 * @return >0 start order; otherwise no auto start
	 */
	int getStartOrder();

}
