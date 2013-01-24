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
package org.eclipse.riena.ui.filter;

import java.util.Collection;

/**
 * This container stores an UI filter and a collection of node ID on which the
 * filter maybe applied.
 */
public interface IUIFilterContainer {

	/**
	 * Returns the UI filter of the container.
	 * 
	 * @return UI filter
	 */
	IUIFilter getFilter();

	/**
	 * Returns the navigation-node IDs on which the filter maybe applied.
	 * 
	 * @return collection of node IDs
	 */
	Collection<String> getFilterTargetNodeIds();

}
