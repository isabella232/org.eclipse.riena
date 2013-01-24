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
package org.eclipse.riena.ui.ridgets;

/**
 * These ridgets are capable of content filtering.
 * 
 * @see IRidgetContentFilter
 * @since 4.0
 */
public interface IFilterableContentRidget extends IRidget {

	/**
	 * Adds the given {@link IRidgetContentFilter} to the filters collection of this ridget. All filters will be consulted (in the order they were added) to
	 * determine which content to show. If the filter is already added, this call will be ignored.
	 * 
	 * @param filter
	 *            the filter to add
	 */
	void addFilter(IRidgetContentFilter filter);

	/**
	 * Removes the given {@link IRidgetContentFilter} from the filters collection of this ridget. If the filter is not in the filters collection, this call will
	 * be ignored.
	 * 
	 * @param filter
	 *            the filter to remove
	 */
	void removeFilter(IRidgetContentFilter filter);
}
