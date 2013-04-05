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
 * An interface to combine all filters of the UI. This interfaces describes the
 * minimal capabilities of filterable objects
 */
public interface IUIFilterable {

	/**
	 * Adds a filter to the filterable object.
	 * 
	 * @param filter
	 *            the filter to add.
	 */
	void addFilter(IUIFilter filter);

	/**
	 * Removes a filter from the filterable object.
	 * 
	 * @param filter
	 *            the filter to remove.
	 */
	void removeFilter(IUIFilter filter);

	/**
	 * Removes a filter with given ID from the filterable object.
	 * 
	 * @param filterID
	 *            the ID of the filter to remove.
	 */
	void removeFilter(String filterID);

	/**
	 * Removes all filters from the filterable object.
	 */
	void removeAllFilters();

	/**
	 * Returns the filters of the filterable object
	 * 
	 * @return The filters of the filterable object.
	 */
	Collection<? extends IUIFilter> getFilters();

}
