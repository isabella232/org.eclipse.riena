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
package org.eclipse.riena.internal.monitor.client;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;
import org.eclipse.riena.monitor.client.ICollector;

/**
 * Extension interface for {@code ICollector} definitions.
 */
@ExtensionInterface(id = "collectors")
public interface ICollectorExtension {

	/**
	 * Return the name of the category this collector belongs to.
	 * 
	 * @return the category name
	 */
	String getCategory();

/**
	 * Create the {@code ICollector.
	 * 
	 * @return a collector
	 */
	@MapName("class")
	ICollector createCollector();

	/**
	 * Return the maximum number of {@code Collectible} items this collector may
	 * keep for transfer to the <i>server</i>.
	 * 
	 * @return maximum number if items to keep.
	 */
	int getMaxItems();
}
