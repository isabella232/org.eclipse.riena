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
package org.eclipse.riena.ui.filter.extension;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;

/**
 * The ID of the navigation node to that the filter will be added.
 */
@ExtensionInterface
public interface IFilterNodeIdExtension {

	/**
	 * Returns the ID of the navigation node to that the filter will be added.
	 * 
	 * @return ID of the node
	 */
	String getId();

}
