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
package org.eclipse.riena.internal.core.exceptionhandler;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.exception.IExceptionHandler;

/**
 * a very simple exception handler that logs the exception to the riena log is
 * also defined in the extension as the one with the least priority
 */
public class SimpleExceptionHandler implements IExceptionHandler {

	public IExceptionHandler.Action handleException(final Throwable t, final String msg, final Logger logger) {
		if (logger != null) {
			logger.log(LogService.LOG_ERROR, msg, t);
			return IExceptionHandler.Action.OK;
		}
		return IExceptionHandler.Action.NOT_HANDLED;
	}

}
