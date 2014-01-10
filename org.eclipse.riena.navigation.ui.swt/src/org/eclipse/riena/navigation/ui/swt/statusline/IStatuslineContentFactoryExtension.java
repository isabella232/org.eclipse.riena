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
package org.eclipse.riena.navigation.ui.swt.statusline;

import org.eclipse.riena.core.injector.extension.CreateLazy;
import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.ui.swt.IStatusLineContentFactory;

/**
 * Extension interface for the configuration of the status line.
 */
@ExtensionInterface(id = "statusLine")
public interface IStatuslineContentFactoryExtension {

	/**
	 * Create an instance of {@code IStatusLineContentFactory}.
	 * 
	 * @return a new {@code IStatusLineContentFactory}
	 */
	@CreateLazy
	IStatusLineContentFactory createFactory();

}
