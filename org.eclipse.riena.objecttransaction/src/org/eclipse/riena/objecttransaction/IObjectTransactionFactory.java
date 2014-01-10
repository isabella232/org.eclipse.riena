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
package org.eclipse.riena.objecttransaction;

/**
 * This Factory is used to create new objectTransactions and new
 * subObjectTransactions.
 * 
 */
public interface IObjectTransactionFactory {

	/**
	 * Creates new ObjectTransaction
	 * 
	 * @return created ObjectTransaction
	 */
	IObjectTransaction createObjectTransaction();

	/**
	 * Creates a new ObjectTransaction which is a subtransaction to the
	 * transaction passed as parameter
	 * 
	 * @param parentObjectTransaction
	 * @return newly create objectTransaction
	 * @pre parentObjectTransaction != null
	 */
	IObjectTransaction createSubObjectTransaction(IObjectTransaction parentObjectTransaction);

}