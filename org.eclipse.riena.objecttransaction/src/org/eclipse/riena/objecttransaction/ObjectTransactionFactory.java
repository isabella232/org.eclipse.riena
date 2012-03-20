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

import org.eclipse.riena.internal.objecttransaction.impl.ObjectTransactionFactoryImpl;

/**
 * Accessor that gives access to the ObjectTransactionFactory
 * 
 * @since 1.2
 * 
 */
public final class ObjectTransactionFactory {

	private static IObjectTransactionFactory singleton = null;

	private ObjectTransactionFactory() {
		// utility
	}

	/**
	 * Returns the correct ObjectTransactionFactory (currently defined as
	 * singleton)
	 * 
	 * @return ObjectTransactionFactory
	 */
	public static IObjectTransactionFactory getInstance() {
		synchronized (ObjectTransactionFactory.class) {
			if (singleton == null) {
				singleton = new ObjectTransactionFactoryImpl(ObjectTransactionManager.getInstance());
			}
			return singleton;
		}
	}
}