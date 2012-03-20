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

/**
 * A helper supporting the activation and passivation of a context
 * 
 */

public final class ContextHelper {

	private ContextHelper() {
		// there is no instance of the Helper
	}

	/**
	 * Activates aContext
	 * 
	 * @param pContext
	 *            context
	 */
	public static void activateContext(final IContext pContext) {
		if (pContext != null) {
			pContext.activate();
		}
	}

	/**
	 * Passivates aContext
	 * 
	 * @param pContext
	 *            context
	 */
	public static void passivateContext(final IContext pContext) {
		if (pContext != null) {
			pContext.passivate();
		}
	}

	/**
	 * Makes sure the right context to set to a Context Holder. Possibly the set
	 * context has to be activated, dependent on the state of the old context.
	 * This should be then done on this central place
	 * 
	 * @param pContext
	 *            context
	 */
	public static IContext setContext(final IContext pOldContext, final IContext pNewContext) {
		return pNewContext;
	}

}
