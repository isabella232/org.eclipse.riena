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
package org.eclipse.riena.navigation.extension;

import org.eclipse.riena.core.injector.extension.DefaultValue;
import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;
import org.eclipse.riena.ui.ridgets.controller.IController;

/**
 * Interface for a SubModuleNode extension that defines how to create a node or
 * a subtree in the application model tree.
 * 
 * @since 2.0
 */
@ExtensionInterface
public interface ISubModuleNode2Extension extends INode2Extension {

	/**
	 * Returns all sub module node definitions that are children of this sub
	 * module node.
	 * 
	 * @return child module node definitions
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
	 * Returns the ID of the view associated with the sub module. Must match a
	 * view elements id attribute of an "org.eclipse.ui.view" extension.
	 * 
	 * @return ID of the view
	 */
	String getViewId();

	/**
	 * Returns a new instance for the controller instance each time this method
	 * is called.
	 * 
	 * @return instance of sub module controller
	 * 
	 * @since 3.0
	 */
	IController createController();

	/**
	 * Indicates whether the view is shared i.e. whether one instance of the
	 * view should be used for all sub module instances.
	 * 
	 * @return {@code true} if the specified view should be a shared view,
	 *         {@code false} otherwise
	 */
	boolean isSharedView();

	/**
	 * Indicates whether or not this node can be selected. The default value is
	 * {@code true}.
	 * 
	 * @return {@code true} if selectable, otherwise {@code false}
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

	/**
	 * Indicates whether the SubModule is expanded by default. <br>
	 * The default value is {@code false}
	 * 
	 * @return {@code true} is expanded; otherwise {@code false}
	 */
	boolean isExpanded();

	/**
	 * Indicates whether the view can be closed.
	 * 
	 * @return {@code true} if the specified view can be closed, {@code false}
	 *         otherwise
	 * @since 3.0
	 */
	@DefaultValue("false")
	boolean isClosable();

	/**
	 * Indicates whether the SubModule is visible in the navigation tree on
	 * system start. <br>
	 * The default value is {@code true}
	 * 
	 * @return {@code true} is shown; otherwise {@code false}
	 */
	@DefaultValue("true")
	boolean isVisible();

}
