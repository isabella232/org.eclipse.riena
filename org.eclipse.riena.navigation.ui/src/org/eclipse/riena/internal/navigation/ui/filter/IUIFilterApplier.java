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
package org.eclipse.riena.internal.navigation.ui.filter;

import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.ui.filter.IUIFilter;

/**
 * Applies {@link IUIFilter}s to the given {@link INavigationNode}.
 */
public interface IUIFilterApplier {

	/**
	 * Filters the user interface referenced by the given application model root
	 * 
	 * @param node
	 *            the {@link INavigationNode} to apply {@link IUIFilter}s to
	 */
	void applyFilter(final INavigationNode<?> node);
}