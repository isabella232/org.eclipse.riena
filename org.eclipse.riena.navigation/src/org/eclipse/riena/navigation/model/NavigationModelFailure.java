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
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.core.exception.Failure;

/**
 * A failure in the model of the navigation.
 */
public class NavigationModelFailure extends Failure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2725327200301100618L;

	public NavigationModelFailure(final String msg) {
		super(msg);
	}

	/**
	 * constructor.
	 * 
	 * @param cause
	 *            exception which has caused this Failure.
	 * @param msg
	 *            message text or message code.
	 */
	public NavigationModelFailure(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
