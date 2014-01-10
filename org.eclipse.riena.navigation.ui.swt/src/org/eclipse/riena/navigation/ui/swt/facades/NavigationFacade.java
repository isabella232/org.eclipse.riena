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
package org.eclipse.riena.navigation.ui.swt.facades;

import org.eclipse.swt.widgets.Tree;

import org.eclipse.riena.navigation.ui.swt.views.ModuleNavigationListener;
import org.eclipse.riena.ui.swt.facades.FacadeFactory;

/**
 * 
 * @since 5.0
 */
public abstract class NavigationFacade {

	private static final NavigationFacade INSTANCE = FacadeFactory.newFacade(NavigationFacade.class);

	/**
	 * The applicable implementation of this class.
	 */
	public static final NavigationFacade getDefault() {
		return INSTANCE;
	}

	/**
	 * Attaches a platform specific {@link ModuleNavigationListener} instance to the given tree
	 * 
	 * @param tree
	 *            a {@link Tree} instance; never null
	 * @since 5.0
	 */
	public abstract void attachModuleNavigationListener(Tree tree);

}
