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

import org.eclipse.riena.core.injector.extension.DoNotReplaceSymbols;
import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;

/**
 * Interface for a ModuleNode extension that defines how to create a node or a
 * subtree in the application model tree.
 */
@ExtensionInterface
public interface IModuleNodeExtension extends INodeExtension {

	/**
	 * Returns the label of the module.
	 * 
	 * @return label of the module
	 */
	@DoNotReplaceSymbols
	String getLabel();

	/**
	 * Returns the icon ID of the module.
	 * 
	 * @return icon ID
	 */
	@DoNotReplaceSymbols
	String getIcon();

	/**
	 * Returns whether the user can close the module.
	 * 
	 * @return <code>true</code> if this item is closable, <code>false</code>
	 *         otherwise. Default is <code>false</code>.
	 */
	boolean isUnclosable();

	/**
	 * Returns all sub module node definitions that are children of this module
	 * node.
	 * 
	 * @return child sub module node definitions
	 */
	@MapName("submodule")
	ISubModuleNodeExtension[] getSubModuleNodes();

	/**
	 * {@inheritDoc}
	 * 
	 * @return child sub module node definitions
	 */
	@MapName("submodule")
	ISubModuleNodeExtension[] getChildNodes();

}
