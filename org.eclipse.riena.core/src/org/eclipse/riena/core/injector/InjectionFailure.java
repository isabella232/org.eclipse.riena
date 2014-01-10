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
package org.eclipse.riena.core.injector;

import org.eclipse.riena.core.exception.Failure;

/**
 * Injection specific failure, i.e. runtime exception
 */
public class InjectionFailure extends Failure {

	private static final long serialVersionUID = 1112658917665471792L;

	/**
	 * {@inheritDoc}
	 */
	public InjectionFailure(final String msg) {
		super(msg);
	}

	/**
	 * {@inheritDoc}
	 */
	public InjectionFailure(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
