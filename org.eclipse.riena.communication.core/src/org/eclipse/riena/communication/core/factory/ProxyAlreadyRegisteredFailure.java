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
package org.eclipse.riena.communication.core.factory;

import org.eclipse.riena.core.exception.Failure;

/**
 * Exception that is thrown if the RemoteService Proxy that I want register
 * already exists. In this case if the URL is already used by another Proxy.
 */
@SuppressWarnings("serial")
public class ProxyAlreadyRegisteredFailure extends Failure {

	/**
	 * @param msg
	 */
	public ProxyAlreadyRegisteredFailure(final String msg) {
		super(msg);
	}

}
