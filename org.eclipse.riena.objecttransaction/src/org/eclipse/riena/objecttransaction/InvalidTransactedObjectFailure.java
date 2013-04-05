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

/**
 * This failure is thrown when the transacted object is not valid in this
 * context
 * 
 */
public class InvalidTransactedObjectFailure extends ObjectTransactionFailure {

	private static final long serialVersionUID = 6756763367095528488L;

	/**
	 * @param message
	 */
	public InvalidTransactedObjectFailure(final String message) {
		super(message);
	}

}