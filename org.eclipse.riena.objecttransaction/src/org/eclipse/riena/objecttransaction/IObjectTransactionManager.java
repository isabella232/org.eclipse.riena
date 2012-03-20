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

/**
 * Interface for the ObjectTransactionManager. The ObjectTransactionManager
 * manages the currently active ObjectTransaction. The ObjectTransaction that is
 * needed can be set calling setCurrent and then all involved components can
 * retrieve them with getCurrent().
 * 
 * ObjectTransactionManager is used by the @see ObjectTransactionContext to set
 * the objectTransaction needed in this context.
 * 
 * The implementation of ObjectTransactionManager is often implemented as
 * threaded @see ObjectTransactionManager.getInstance()
 * 
 */
public interface IObjectTransactionManager {

	/**
	 * Returns the current ObjectTransaction object
	 * 
	 * @post !return.isInvalid()
	 * @return current ObjectTransaction
	 */
	IObjectTransaction getCurrent();

	/**
	 * Sets the current ObjectTransaction
	 * 
	 * @param objectTransaction
	 *            current object transaction
	 * @pre objecTransaction =! null && !objectTransaction.isInvalid()
	 */
	void setCurrent(IObjectTransaction objectTransaction);

}