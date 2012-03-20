/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.objecttransaction.impl;

import org.eclipse.riena.objecttransaction.IObjectTransaction;
import org.eclipse.riena.objecttransaction.IObjectTransactionManager;
import org.eclipse.riena.objecttransaction.InvalidTransactionFailure;

/**
 * ObjectTransactionManagerImpl manages the current ObjectTransaction
 * 
 */
public class ObjectTransactionManagerImpl implements IObjectTransactionManager {

	private IObjectTransaction objectTransaction;

	/**
	 * @see org.eclipse.riena.objecttransaction.IObjectTransactionManager#getCurrent()
	 */
	public IObjectTransaction getCurrent() {
		if (objectTransaction != null && objectTransaction.isInvalid()) {
			throw new InvalidTransactionFailure("no valid objecttransaction, but required"); //$NON-NLS-1$
		}
		return objectTransaction;
	}

	/**
	 * @see org.eclipse.riena.objecttransaction.IObjectTransactionManager#setCurrent(org.eclipse.riena.objecttransaction.IObjectTransaction)
	 */
	public void setCurrent(final IObjectTransaction currentObjectTransaction) {
		if (currentObjectTransaction != null && currentObjectTransaction.isInvalid()) {
			throw new InvalidTransactionFailure(
					"an invalid object transaction cannot be set to be the current object transaction"); //$NON-NLS-1$
		}
		this.objectTransaction = currentObjectTransaction;
	}

}