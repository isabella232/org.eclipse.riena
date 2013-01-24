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
package org.eclipse.riena.objecttransaction.context;

import org.eclipse.riena.objecttransaction.IObjectTransaction;
import org.eclipse.riena.objecttransaction.ObjectTransactionManager;

/**
 * Objects of this interface IObjectTransactionContext not only implement the
 * activate and passivate methods from @see IContext but they also store a
 * reference the associated objectTransaction.
 * 
 * So when activate is called on an implementation of this interface, the
 * objectTransaction of this object is set as the current active
 * objectTransaction in the @see {@link ObjectTransactionManager}
 * 
 */
public interface IObjectTransactionContext extends IContext {

	/**
	 * @return the objectTransaction reference stored in this instance (not
	 *         necessarily the current active objectTransaction).
	 */
	IObjectTransaction getObjectTransaction();

	/**
	 * @param objectTransaction
	 *            The objectTransaction reference in this instance is set. If
	 *            this context is currently active, the passed object
	 *            Transaction is also set as the new currently active
	 *            objectTransaction in the ObjectTransactionManager
	 */
	void setObjectTransaction(IObjectTransaction pObjectTransaction);

}
