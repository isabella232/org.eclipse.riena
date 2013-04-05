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
 *
 */
public class ExtensionPointFailure extends Failure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6236123264651424352L;

	/**
	 * @param msg
	 */
	public ExtensionPointFailure(final String msg) {
		super(msg);
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public ExtensionPointFailure(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
