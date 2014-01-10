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
package org.eclipse.riena.core.singleton;

import org.eclipse.riena.core.exception.Failure;

/**
 * 
 * Signals any failure while singleton creation.
 * 
 * @since 3.0
 */
public class SingletonFailure extends Failure {

	private static final long serialVersionUID = 1L;

	/**
	 * @since 4.0
	 */
	public SingletonFailure(final String msg) {
		super(msg);
	}

	public SingletonFailure(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}