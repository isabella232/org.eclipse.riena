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
package org.eclipse.riena.objecttransaction.context;

import java.util.Stack;

import org.eclipse.riena.objecttransaction.IObjectTransaction;
import org.eclipse.riena.objecttransaction.ObjectTransactionManager;

/**
 * This class describes an object Transaction Context, which can be passed to
 * different places in the application to work on. At one time can only one
 * Instance of a Transaction be activated, to check this. The TransactionContext
 * must always be activated and passivated! If any other transaction is
 * activated on the same thread, when calling activated on this context, the
 * context throws an exception! The object transaction context implies, that
 * activate and passivate on it are always called in pairs!! The calls to
 * activate an passivate build a stack! The Transaction is only than set to null
 * if the call stack is 0;
 * 
 */
public class ObjectTransactionContext implements IObjectTransactionContext {

	private final Stack<IObjectTransaction> replaced = new Stack<IObjectTransaction>();
	private IObjectTransaction objectTransaction;

	/**
	 * Creates a new ObjectTransactionContext on the passed object transaction
	 */
	public ObjectTransactionContext() {
		super();
	}

	/**
	 * Creates a new ObjectTransactionContext on the passed object transaction
	 * 
	 * @param transaction
	 *            the Transaction to activate
	 */
	public ObjectTransactionContext(final IObjectTransaction transaction) {
		super();
		objectTransaction = transaction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.objecttransaction.context.IContext#activate()
	 */
	public void activate() {
		replaced.push(ObjectTransactionManager.getInstance().getCurrent());
		ObjectTransactionManager.getInstance().setCurrent(objectTransaction);
	}

	/**
	 * Passivates the contained transaction. Every activate() must follow a
	 * passivate() before the next activate on the same thread follows. The
	 * passivation is necessary to oversee the transaction management.
	 */
	public void passivate() {
		if (isActivated()) {
			ObjectTransactionManager.getInstance().setCurrent(replaced.pop());
		} else {
			throw new ObjectTransactionContextFailure("Inconsistency while passivating object transaction context!"); //$NON-NLS-1$
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.objecttransaction.context.IObjectTransactionContext
	 * #getObjectTransaction()
	 */
	public IObjectTransaction getObjectTransaction() {
		return objectTransaction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.objecttransaction.context.IObjectTransactionContext
	 * #setObjectTransaction
	 * (org.eclipse.riena.objecttransaction.IObjectTransaction)
	 */
	public void setObjectTransaction(final IObjectTransaction pObjectTransaction) {
		this.objectTransaction = pObjectTransaction;
		if (isActivated()) {
			ObjectTransactionManager.getInstance().setCurrent(objectTransaction);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.objecttransaction.context.IContext#isActivated()
	 */
	public boolean isActivated() {
		return !replaced.empty();
	}

	@Override
	public String toString() {
		return super.toString() + "ObjectTransaction=" + objectTransaction; //$NON-NLS-1$
	}

}
