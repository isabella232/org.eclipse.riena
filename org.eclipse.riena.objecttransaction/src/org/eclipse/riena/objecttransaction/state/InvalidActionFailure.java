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
package org.eclipse.riena.objecttransaction.state;

import org.eclipse.riena.objecttransaction.ObjectTransactionFailure;

/**
 * Failure if an invalid action in the statemachine was executed
 * 
 */
public class InvalidActionFailure extends ObjectTransactionFailure {

	private static final long serialVersionUID = 4548211115892016006L;

	/**
	 * @param message
	 */
	public InvalidActionFailure(final String message) {
		super(message);
	}

}