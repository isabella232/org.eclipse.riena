/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.core.resource;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;

/**
 * Extension interface to set an icon manager.
 */
@ExtensionInterface(id = "iconManager")
public interface IIconManagerExtension {

	/**
	 * Creates and returns the icon manager.
	 * 
	 * @return instance of the icon manager
	 */
	@MapName("iconManager")
	IIconManager createIconManager();

	/**
	 * Returns the order. There can be more than one icon manager. The manager
	 * with the highest order is used.
	 * 
	 * @return order
	 */
	int getOrder();

}
