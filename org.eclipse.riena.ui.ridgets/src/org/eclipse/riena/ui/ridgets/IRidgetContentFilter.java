/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
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
 * A ridget content filter is used by some ridgets to reduce the set of displayed content elements.
 * <p>
 * Implementations of this class can be registered/unregistered in {@link ISelectableRidget} instances.
 * 
 * @see ISelectableRidget#addFilter(IRidgetContentFilter)
 * 
 * @since 4.0
 */
public interface IRidgetContentFilter {

	/**
	 * Returns whether the given element will be filtered out.
	 * 
	 * @param parentElement
	 *            the parent element
	 * @param element
	 *            the element
	 * @return <code>true</code> if the given element should be visible and <code>false</code> if it should be filtered out.
	 */
	boolean isElementVisible(Object parentElement, Object element);

}
