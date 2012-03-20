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
package org.eclipse.riena.core.exception;

import org.eclipse.equinox.log.Logger;

/**
 * ExceptionHandler can handle exceptions
 */
public interface IExceptionHandler {

	/**
	 * Defines actions how to process after the exception was handled.
	 * 
	 * The semantic is that a return value of NOT_HANDLED means that the
	 * IExceptionHandlerManager continues to ask other IExceptionHandler. All
	 * other return values mean that the chain is interrupted and the Action is
	 * returned to the application.
	 * 
	 * The concreate meaning of OK, CANCEL or RETRY is up to the calling
	 * application.
	 */
	public enum Action {

		/**
		 * Indicates that the exception handler has not handled the exception.
		 * The exception handler manager will continue asking other handlers.
		 */
		NOT_HANDLED,

		/**
		 * Indicates that the exception handler has handled the exception. The
		 * exception handler manager does not ask others but returns this Action
		 * to the call application.
		 */
		OK,

		/**
		 * Indicates that the exception handler has handled the exception. The
		 * exception handler manager does not ask others but returns this Action
		 * to the call application.
		 */
		RETRY,

		/**
		 * Indicates that the exception handler has handled the exception. The
		 * exception handler manager does not ask others but returns this Action
		 * to the call application.
		 */
		CANCEL
	}

	/**
	 * Check if the exception passed can be handled and return an
	 * {@link IExceptionHandler.Action} how might to process. This method should
	 * be used anywhere where exceptions are caught directly.
	 * 
	 * @pre t != null
	 * @post result != null
	 * 
	 * @param t
	 *            exception to be handled
	 * @param msg
	 *            an optional message
	 * @param logger
	 *            to be used for logging
	 * 
	 * @return the Action how to process
	 * 
	 */
	Action handleException(Throwable t, String msg, Logger logger);
}
