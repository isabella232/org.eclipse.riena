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
package org.eclipse.riena.core.exception;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.util.StringUtils;

/**
 * Test class for ExceptionHandler
 */
public class TestExceptionHandler implements IExceptionHandler {

	private final String name;
	private final String exceptionMessageTrigger;
	private Action action;

	private static String handledName;

	public static String getHandledName() {
		return handledName;
	}

	public TestExceptionHandler(final String name, final String exceptionMessageTrigger) {
		this.name = name;
		this.exceptionMessageTrigger = exceptionMessageTrigger;
		handledName = null;
	}

	public IExceptionHandler.Action handleException(final Throwable t, final String msg, final Logger logger) {
		action = StringUtils.equals(t.getMessage(), exceptionMessageTrigger) ? Action.OK : Action.NOT_HANDLED;
		if (action == Action.OK) {
			handledName = name;
		}
		return action;
	}

	@Override
	public String toString() {
		return name;
	}

}
