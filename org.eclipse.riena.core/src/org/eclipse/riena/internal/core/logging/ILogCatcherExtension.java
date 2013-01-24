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
package org.eclipse.riena.internal.core.logging;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;
import org.eclipse.riena.core.logging.ILogCatcher;

/**
 * Defines an {@code ILogCatcher}.
 */
@ExtensionInterface(id = "logCatchers")
public interface ILogCatcherExtension {

	/**
	 * The descriptive name of the {@code ILogCatcher}
	 * 
	 * @return the descriptive name if the {@code ILogCatcher}
	 */
	String getName();

	/**
	 * Create an instance of the {@code ILogCatcher}
	 * 
	 * @return the {@code ILogCatcher}
	 */
	@MapName("class")
	ILogCatcher createLogCatcher();

}
