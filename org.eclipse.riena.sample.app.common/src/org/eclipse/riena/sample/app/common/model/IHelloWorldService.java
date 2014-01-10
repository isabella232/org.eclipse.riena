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
package org.eclipse.riena.sample.app.common.model;

/**
 * Interface for HelloWorld service sample.
 */
public interface IHelloWorldService {

	/**
	 * Receive a message from server.
	 * 
	 * @return the message.
	 */
	String getMessage();
}
