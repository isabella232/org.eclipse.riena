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
package org.eclipse.riena.navigation.ui.application;

import org.eclipse.riena.navigation.IApplicationNode;

/**
 * Creates the application model. Implementations are contributed via <tt>plugin.xml</tt>.
 * <p>
 * Note: This interface is used for the E4 support only.
 * 
 * @since 5.0
 * 
 */
public interface IApplicationModelCreator {

	/**
	 * This method is invoked instead of the {@link AbstractApplication#createModel()} method.
	 * 
	 * @return the application model root
	 */
	IApplicationNode createModel();

	/**
	 * Hook for configuration code
	 */
	void configure();

	void initApplicationNode(IApplicationNode applicationNode);

}
