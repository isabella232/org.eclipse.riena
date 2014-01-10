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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.riena.core.exception.Failure;

/**
 * Failure class for view specific failures.
 */
public class UIViewFailure extends Failure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7955739836963025929L;

	/**
	 * Failure with a simple message.
	 * 
	 * @param msg
	 *            the message
	 */
	public UIViewFailure(final String msg) {
		super(msg);
	}

	/**
	 * @param msg
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public UIViewFailure(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
