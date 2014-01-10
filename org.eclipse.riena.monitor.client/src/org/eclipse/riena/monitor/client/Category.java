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
package org.eclipse.riena.monitor.client;

import org.eclipse.core.runtime.Assert;

/**
 * The category properties.
 */
public class Category {

	private final String name;
	private final int maxItems;

	/**
	 * @param name
	 * @param maxItems
	 */
	public Category(final String name, final int maxItems) {
		super();
		Assert.isLegal(name != null, "name must not be null"); //$NON-NLS-1$
		Assert.isLegal(maxItems > 0, "maxItems must be greater than 0."); //$NON-NLS-1$
		this.name = name;
		this.maxItems = maxItems;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the maxItems
	 */
	public int getMaxItems() {
		return maxItems;
	}

}
