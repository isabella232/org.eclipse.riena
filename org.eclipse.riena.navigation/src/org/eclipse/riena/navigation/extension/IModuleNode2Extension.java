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

import org.eclipse.riena.core.injector.extension.DefaultValue;
import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;

/**
 * Interface for a ModuleNode extension that defines how to create a node or a
 * subtree in the application model tree.
 */
@ExtensionInterface
public interface IModuleNode2Extension extends INode2Extension {

	/**
	 * Returns all sub module node definitions that are children of this module
	 * node.
	 * 
	 * @return child sub module node definitions
	 */
	@MapName("subModule")
	ISubModuleNode2Extension[] getSubModuleNodes();

	/**
	 * {@inheritDoc}
	 * 
	 * @return child sub module node definitions
	 */
	@MapName("subModule")
	ISubModuleNode2Extension[] getChildNodes();

	/**
	 * Returns whether the user can close the module.
	 * 
	 * @return {@code true} if this module is closable, <{@code false}
	 *         otherwise. Default is {@code true}.
	 */
	@DefaultValue("true")
	boolean isClosable();

}
