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

/**
 * A standard-implementation of the IContextHolder, which, can be used to proxy
 * objects not being a contextHolder!
 * 
 */
public class BasicContextHolder implements IContextHolder {

	private IContext context;

	/**
	 * Creates a new BasicContextManager
	 * 
	 * @param context
	 */
	public BasicContextHolder() {
		super();
	}

	/**
	 * Creates a new BasicContextManager
	 * 
	 * @param context
	 *            the context to work on
	 */
	public BasicContextHolder(final IContext context) {
		super();
		this.context = context;
	}

	/**
	 * @return the context working on
	 */
	public IContext getContext() {
		return context;
	}

	/**
	 * @param pContext
	 *            the context to work on
	 */
	public void setContext(final IContext pContext) {
		context = ContextHelper.setContext(context, pContext);
	}

}