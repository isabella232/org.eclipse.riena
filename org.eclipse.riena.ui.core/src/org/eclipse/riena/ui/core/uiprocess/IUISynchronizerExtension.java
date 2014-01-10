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
package org.eclipse.riena.ui.core.uiprocess;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;

/**
 * {@code ExtensionInterface} for the ui synchronizer.
 * 
 * @since 1.2
 */
@ExtensionInterface(id = "uiSynchronizer")
public interface IUISynchronizerExtension {

	/**
	 * Return a new {@code IUISynchronizer}.
	 * 
	 * @return
	 */
	@MapName("class")
	IUISynchronizer createUISynchronizer();

}
