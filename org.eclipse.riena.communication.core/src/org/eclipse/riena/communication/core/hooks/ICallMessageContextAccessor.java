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
package org.eclipse.riena.communication.core.hooks;

/**
 * A helper class that allows to get or create the CallMessageContext for a
 * specific remote service protocol
 */
public interface ICallMessageContextAccessor {

	/**
	 * Creates a new CallMessageContext
	 * 
	 * @param proxy
	 *            remote service proxy
	 * @param methodName
	 *            method name
	 * @param requestId
	 *            request id (unique id for each request, appended to the URL
	 *            for ease tracking of calls on the server)
	 * @return
	 */
	ICallMessageContext createMessageContext(Object proxy, String methodName, String requestId);

	/**
	 * Returns the previously create CallMessageContext
	 * 
	 * @return current instance
	 */
	ICallMessageContext getMessageContext();
}
