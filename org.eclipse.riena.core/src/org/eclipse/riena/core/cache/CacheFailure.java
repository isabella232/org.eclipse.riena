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
package org.eclipse.riena.core.cache;

import org.eclipse.riena.core.exception.Failure;

/**
 * Failure for all GenericObjectCache related Failures
 */
public class CacheFailure extends Failure {

	private static final long serialVersionUID = 8131505473671580214L;

	/**
	 * @param msg
	 */
	public CacheFailure(final String msg) {
		super(msg);
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public CacheFailure(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
