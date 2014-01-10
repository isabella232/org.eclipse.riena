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
package org.eclipse.riena.internal.core.exceptionmanager;

import org.eclipse.riena.core.exception.IExceptionHandler;
import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;

/**
 * Definition of the ExceptionHandlers.
 */
@ExtensionInterface(id = "exceptionHandlers")
public interface IExceptionHandlerExtension {

	/**
	 * Get the descriptive name of the exception handler. This name will be used
	 * for sorting the exception handlers.
	 * 
	 * @see getPreExceptionHandlers, getPostExceptionHandlers
	 * @return the name
	 */
	String getName();

	/**
	 * Create an instance of the exception handler.
	 * 
	 * @return the exception handler
	 */
	@MapName("class")
	IExceptionHandler createExceptionHandler();

	@MapName("class")
	String getExceptionHandler();

	/**
	 * The comma-separated list of exception handler names that shall be
	 * executed before this exception handler.
	 * 
	 * @return the pre exception handler list
	 */
	String getPreHandlers();

	/**
	 * The comma-separated list of exception handler names that shall be
	 * executed after this exception handler.
	 * 
	 * @return the post exception handler list
	 */
	String getPostHandlers();

	/**
	 * Get the exception handler name that is called afterwards this exception
	 * handler.
	 * 
	 * @return the before exception handler
	 */
	@Deprecated
	String getBefore();

}
