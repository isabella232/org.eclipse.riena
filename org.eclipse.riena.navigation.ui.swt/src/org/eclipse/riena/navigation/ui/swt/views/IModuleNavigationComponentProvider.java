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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.model.ModuleGroupNode;

/**
 * Describes the interface of a navigation component provider being interested
 * to be extended with scrolling logic.
 */
public interface IModuleNavigationComponentProvider {

	/**
	 * The control representing the main navigation component.
	 * 
	 * @return the main navigation component.
	 */
	Composite getNavigationComponent();

	/**
	 * @return the composite holding the real content being scrolled inside the
	 *         main navigation component.
	 */
	Composite getScrolledComponent();

	/**
	 * Calculate the height of the the main navigation component.
	 * 
	 * @return the calculated height.
	 * @since 4.0
	 */
	int calculateHeight();

	/**
	 * @param moduleGroupNode
	 *            the node for which the corresponding view has to be looked up.
	 * @return the {@link ModuleGroupView} for the parameter node.
	 */
	ModuleGroupView getModuleGroupViewForNode(IModuleGroupNode moduleGroupNode);

	/**
	 * @param moduleGroupNode
	 *            the node for which the corresponding view has to be looked up.
	 * @return the {@link ModuleGroupView} for the parameter node.
	 * 
	 * @since 1.2
	 */
	ModuleView getModuleViewForNode(IModuleNode moduleGroupNode);

	/**
	 * @return the currently active {@link ModuleGroupNode}
	 */
	IModuleGroupNode getActiveModuleGroupNode();

	/**
	 * @return the current sub application node.
	 */
	ISubApplicationNode getSubApplicationNode();

}
