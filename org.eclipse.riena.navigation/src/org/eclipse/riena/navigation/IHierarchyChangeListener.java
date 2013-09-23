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

import org.eclipse.riena.navigation.model.NavigationNode;

/**
 * Events that occur in a {@link NavigationNode}'s parents hierarchy.
 * 
 * @since 5.0
 * 
 */
public interface IHierarchyChangeListener {

	/**
	 * @param node
	 *            the node whose label has changed
	 */
	void labelChanged(INavigationNode<?> node);

}
