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

import org.eclipse.riena.core.injector.extension.DefaultValue;
import org.eclipse.riena.core.injector.extension.DoNotReplaceSymbols;
import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;
import org.eclipse.riena.ui.ridgets.controller.IController;

/**
 * Interface for a SubModuleNode extension that defines how to create a node or
 * a subtree in the application model tree.
 */
@ExtensionInterface
public interface ISubModuleNodeExtension extends INodeExtension {

	/**
	 * Returns the label of the sub module.
	 * 
	 * @return label of the sub module
	 */
	@DoNotReplaceSymbols
	String getLabel();

	/**
	 * Returns the icon ID of the sub module.
	 * 
	 * @return icon ID
	 */
	@DoNotReplaceSymbols
	String getIcon();

	/**
	 * Returns the controller factory that creates the control for the UI
	 * widgets in the view through Ridgets.
	 * 
	 * @since 4.0
	 * 
	 * @return controller factory of the sub module
	 */
	IController createController();

	/**
	 * Returns (for the SWT-based Riena UI) the ID of the view associated with
	 * the submodule. Must match a view elements id attribute of an
	 * "org.eclipse.ui.view" extension.
	 * 
	 * @return view ID
	 */
	@MapName("view")
	String getViewId();

	/**
	 * Indicates whether the view is shared i.e. whether one instance of the
	 * view should be used for all submodule instances.
	 * 
	 * @return true if the specified view should be a shared view, false
	 *         otherwise
	 */
	boolean isShared();

	/**
	 * Returns all sub module node definitions that are children of this sub
	 * module node.
	 * 
	 * @return child module node definitions
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

	/**
	 * Indicates whether the view is shared i.e. whether one instance of the
	 * view should be used for all sub module instances.
	 * 
	 * @return {@code true} if the specified view should be a shared view,
	 *         {@code false} otherwise
	 * @since 1.2
	 */
	@DefaultValue("true")
	boolean isSelectable();

	/**
	 * Indicates whether the navigation node should be prepared after the sub
	 * module node is created. Preparation means that the controller is created
	 * (among other things).
	 * 
	 * @return {@code true} should be prepared; otherwise {@code false}
	 */
	boolean isRequiresPreparation();

}
