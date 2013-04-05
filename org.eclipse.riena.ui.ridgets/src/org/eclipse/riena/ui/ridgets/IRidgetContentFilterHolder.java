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
 * A filter holder is responsible for managing the list of content filters and applying them to the underlying view.
 * 
 * @since 4.0
 */
public interface IRidgetContentFilterHolder<CONTENT_VIEW> {

	/**
	 * Adds the given filter from the filters list.
	 * 
	 * @param filter
	 *            the filter to add
	 */
	void add(IRidgetContentFilter filter);

	/**
	 * Removes the given filter from the filters list.
	 * 
	 * @param filter
	 *            the filter to remove
	 */
	void remove(IRidgetContentFilter filter);

	/**
	 * Activates the filters on the given target.
	 * 
	 * @param target
	 *            the target view, on which to apply the filter
	 */
	void activate(CONTENT_VIEW target);

	/**
	 * Deactivates the filters on the given target.
	 * 
	 * @param target
	 *            the target view, on which to unregister the filter
	 */
	void deactivate(CONTENT_VIEW target);
}
