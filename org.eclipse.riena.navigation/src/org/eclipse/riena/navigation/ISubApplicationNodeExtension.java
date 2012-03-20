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
 * Interface for a SubApplicationNode extension that defines how to create a
 * node or a subtree in the application model tree.
 */
@ExtensionInterface
public interface ISubApplicationNodeExtension extends INodeExtension {

	/**
	 * Returns the label of the sub application.
	 * 
	 * @return label of the sub application
	 */
	@DoNotReplaceSymbols
	String getLabel();

	/**
	 * Returns the icon ID of the sub application.
	 * 
	 * @return icon ID
	 */
	@DoNotReplaceSymbols
	String getIcon();

	/**
	 * Returns the ID of the perspective associated with the sub application.
	 * Must match an perspective elements id attribute of an
	 * "org.eclipse.ui.perspectives" extension.
	 * 
	 * @return ID of perspective
	 */
	@MapName("view")
	String getViewId();

	/**
	 * Returns all module group node definitions that are children of this sub
	 * application node.
	 * 
	 * @return child module group node definitions
	 */
	@MapName("modulegroup")
	IModuleGroupNodeExtension[] getModuleGroupNodes();

	/**
	 * {@inheritDoc}
	 * 
	 * @return child module group node definitions
	 */
	@MapName("modulegroup")
	IModuleGroupNodeExtension[] getChildNodes();

}
