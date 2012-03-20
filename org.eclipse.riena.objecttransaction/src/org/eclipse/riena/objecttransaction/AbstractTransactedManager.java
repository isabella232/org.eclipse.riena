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
package org.eclipse.riena.objecttransaction;

import java.lang.reflect.Proxy;

import org.eclipse.riena.objecttransaction.context.BasicContextHolder;
import org.eclipse.riena.objecttransaction.context.ContextProxy;
import org.eclipse.riena.objecttransaction.context.IContextHolder;
import org.eclipse.riena.objecttransaction.context.IObjectTransactionContext;
import org.eclipse.riena.objecttransaction.context.ObjectTransactionContext;

/**
 * An abstract superclass simplifying the implementation of application managers
 * based on an object transaction.
 * 
 * To ensure that all methods of a concrete manager are running in the context
 * of the currently set Transaction IT IS STRONGLY recomended not to provide any
 * public contructors. Istead provide a factory method creating the needed Proxy
 * by calling cover() on the newly created instance of the manager.
 * 
 * This superclass supports the follwing tasks: * surrounding all functions of
 * the manager by a context * surrounding all functions of any object exposed by
 * the manager * provides convenience methods for the creation of a new
 * transaction
 * 
 * The Transaction to actually work on, has to be provided by a concrete
 * subclass. Since the abstract class do not know the business context the
 * concrete subclass works on.
 * 
 */
public abstract class AbstractTransactedManager {

	private final IContextHolder contextHolder;
	private final IObjectTransactionContext objectTransactionContext;

	/**
	 * Creates a new ObjectTransactionContextManager Initializes the context
	 * contained.
	 * 
	 * @param context
	 */
	protected AbstractTransactedManager() {
		super();
		this.objectTransactionContext = new ObjectTransactionContext();
		this.contextHolder = new BasicContextHolder(objectTransactionContext);
	}

	/**
	 * Delgates the Transaction to work on to the contained context. The setting
	 * of the Transaction is not public per default, thus this method is
	 * protected. The Object Transaction passed to this method is aktivated
	 * immediately!
	 * 
	 * @param pObjectTransaction
	 *            the ObjectTransaction to set.
	 */
	protected final void setCurrentObjectTransaction(final IObjectTransaction pObjectTransaction) {
		objectTransactionContext.setObjectTransaction(pObjectTransaction);
	}

	/**
	 * @return the IObjectTransaction beeing current from the point of view of
	 *         the concrete subclass of the manager
	 */
	protected final IObjectTransaction getCurrentObjectTransaction() {
		return objectTransactionContext.getObjectTransaction();
	}

	/**
	 * Creates a new ObjectTransaction without setting it current. The
	 * transaction set from the context will remain.
	 * 
	 * @return an IObjectTransaction newly created
	 */
	protected final IObjectTransaction createObjectTransaction() {
		// TODO: the object Transaction shold not aktivate itself, talk about
		// with cc, this moment the current context is reactivated!
		final IObjectTransaction oldTransaction = ObjectTransactionManager.getInstance().getCurrent();
		final IObjectTransaction result = ObjectTransactionFactory.getInstance().createObjectTransaction();
		ObjectTransactionManager.getInstance().setCurrent(oldTransaction);
		return result;
	}

	/**
	 * Creates a new SubObjectTransaction relative to the current object
	 * Transaction.
	 * 
	 * @return the newly create sub object transaction
	 */
	protected final IObjectTransaction createSubObjectTransaction() {
		final IObjectTransaction cobj = getCurrentObjectTransaction();
		IObjectTransaction result = null;
		if (cobj != null) {
			result = this.createSubObjectTransaction(cobj);
		}
		return result;
	}

	/**
	 * Creates a new SubObjectTransaction without setting it current. The
	 * transaction set from the context will remain.
	 * 
	 * @return an IObjectTransaction newly created
	 */
	protected final IObjectTransaction createSubObjectTransaction(final IObjectTransaction pParentTransaction) {
		// TODO: the object Transaction shold not aktivate itself, talk about
		// with cc, this moment the current context is reactivated!
		final IObjectTransaction oldTransaction = ObjectTransactionManager.getInstance().getCurrent();
		final IObjectTransaction result = pParentTransaction.createSubObjectTransaction();
		ObjectTransactionManager.getInstance().setCurrent(oldTransaction);
		return result;
	}

	/**
	 * @param oObject
	 *            the Object to cover with the contained context
	 * @param <T>
	 *            the Type extected to return
	 * @return a Context Proxy on any concrete Manager
	 */
	public final <T> T cover(final T pObject) {
		return ContextProxy.cover(pObject, contextHolder);
	}

	/**
	 * Uncovers an object from a Context proxy, if the passed object is one
	 * 
	 * @param <T>
	 *            the extected type
	 * @param pCovered
	 *            the covered object
	 * @return
	 */
	// During uncovering the object will be automatically castet
	public final <T> T uncover(final T pCovered) {
		T result = null;
		if (pCovered != null && Proxy.isProxyClass(pCovered.getClass())
				&& (Proxy.getInvocationHandler(pCovered) instanceof ContextProxy)) {
			result = (T) ((ContextProxy) Proxy.getInvocationHandler(pCovered)).getService();
		} else {
			result = pCovered;
		}
		return result;
	}

	/**
	 * Commits the curren transaction if any
	 */
	public void commit() {
		final IObjectTransaction cot = this.getCurrentObjectTransaction();
		if (cot != null) {
			cot.commit();
		}
	}

	/**
	 * Roll back the current transaction if any
	 */
	public void rollback() {
		final IObjectTransaction cot = this.getCurrentObjectTransaction();
		if (cot != null) {
			cot.rollback();
		}
	}

}
