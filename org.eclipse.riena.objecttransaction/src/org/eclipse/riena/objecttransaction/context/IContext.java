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
package org.eclipse.riena.objecttransaction.context;

/**
 * The interface IContext defines hook methods that are invoked to set a context
 * before an action is invoked. Contexts can be activated and passivated and
 * tested if they are active. The IContext is used for ObjectTransaction in
 * 
 * @see IObjectTransactionContext where an objectTransaction is activated before
 *      a business method is called on an object.
 * 
 */

public interface IContext {

	/**
	 * Activates the context instance
	 */
	void activate();

	/**
	 * Passivates the context instance
	 */
	void passivate();

	/**
	 * @return true if the context is active otherwise false
	 */
	boolean isActivated();

}
