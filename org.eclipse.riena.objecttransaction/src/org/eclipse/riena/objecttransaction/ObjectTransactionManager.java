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

import org.eclipse.riena.internal.objecttransaction.impl.ObjectTransactionManagerImpl;

/**
 * ObjectTransactionManager
 * 
 * @since 1.2
 * 
 */
public final class ObjectTransactionManager {

	private static final ThreadLocal<IObjectTransactionManager> THREAD_LOCAL_OTM = new ThreadLocal<IObjectTransactionManager>() {
		@Override
		protected IObjectTransactionManager initialValue() {
			return new ObjectTransactionManagerImpl();
		}
	};

	private ObjectTransactionManager() {
		// utility
	}

	/**
	 * Returns the ObjectTransactionManager (currently defined as threaded)
	 * 
	 * @return ObjectTransactionManager
	 */
	public static IObjectTransactionManager getInstance() {
		return THREAD_LOCAL_OTM.get();
	}
}