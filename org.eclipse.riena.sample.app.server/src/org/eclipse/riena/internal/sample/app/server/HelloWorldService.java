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
package org.eclipse.riena.internal.sample.app.server;

import java.util.Date;

import org.eclipse.riena.sample.app.common.model.IHelloWorldService;

/**
 * HellowWorldService is a very simple Web Service for our simple HelloService
 * example.
 */
public class HelloWorldService implements IHelloWorldService {

	/**
	 * @see org.eclipse.riena.sample.app.common.model.IHelloWorldService#getMessage()
	 */
	public String getMessage() {

		return new Date() + ": Hello World!"; //$NON-NLS-1$
	}
}
