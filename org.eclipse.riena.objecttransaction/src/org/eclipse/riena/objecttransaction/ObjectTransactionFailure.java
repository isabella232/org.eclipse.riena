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
package org.eclipse.riena.objecttransaction;

import org.eclipse.riena.core.exception.Failure;

/**
 * This failure is thrown for error while processing a request in the
 * objecttransaction
 * 
 */
public class ObjectTransactionFailure extends Failure {

	private static final long serialVersionUID = -8680824324437292930L;

	/**
	 * @param message
	 */
	public ObjectTransactionFailure(final String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param t
	 */
	public ObjectTransactionFailure(final String message, final Throwable t) {
		super(message, t);
	}

}