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
package org.eclipse.riena.internal.objecttransaction.impl;

import org.eclipse.core.runtime.Assert;

import org.eclipse.riena.objecttransaction.IObjectTransaction;
import org.eclipse.riena.objecttransaction.IObjectTransactionFactory;
import org.eclipse.riena.objecttransaction.IObjectTransactionManager;

/**
 * ObjectTransactionFactoryImpl contains the Factory for creating new
 * ObjectTransactions
 * 
 */
public class ObjectTransactionFactoryImpl implements IObjectTransactionFactory {

	private final IObjectTransactionManager objectTransactionManager;

	/**
	 * Create ObjectTranactionFactory setting the objecttransactionmanger in the
	 * constructor
	 * 
	 * @param otm
	 *            objecttransactionmanager
	 */
	public ObjectTransactionFactoryImpl(final IObjectTransactionManager otm) {
		this.objectTransactionManager = otm;
	}

	/**
	 * @see org.eclipse.riena.objecttransaction.IObjectTransactionFactory#createObjectTransaction()
	 */
	public IObjectTransaction createObjectTransaction() {
		final IObjectTransaction objectTransaction = new ObjectTransactionImpl();
		objectTransactionManager.setCurrent(objectTransaction);
		return objectTransaction;
	}

	/**
	 * @see org.eclipse.riena.objecttransaction.IObjectTransactionFactory#createSubObjectTransaction(org.eclipse.riena.objecttransaction.IObjectTransaction)
	 */
	public IObjectTransaction createSubObjectTransaction(final IObjectTransaction parentObjectTransaction) {
		Assert.isNotNull(parentObjectTransaction, "Parameter 'parentObjectTransaction' must not be null."); //$NON-NLS-1$

		final IObjectTransaction objectTransaction = parentObjectTransaction.createSubObjectTransaction();
		return objectTransaction;
	}
}