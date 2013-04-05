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

/**
 * Implementation of this service interface provides service methods to get
 * information provided by UI Filter definitions identified by a given
 * filter-ID.
 */
public interface IUIFilterProvider {

	/**
	 * Returns a UIFilter identified by the given filterId. The filter is
	 * created if it not yet exists.
	 * 
	 * @param filterID
	 *            ID of the filter
	 * 
	 * @return a container with an UI filter
	 */
	IUIFilterContainer provideFilter(String filterID);

}
