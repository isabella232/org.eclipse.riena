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
package org.eclipse.riena.sample.app.common.exception;

/**
 * The Interface for the ExceptionService.
 * 
 * 
 */
public interface IExceptionService {

	/**
	 * Returns the date of the Server in European Format.
	 * 
	 * @return The Server date as String
	 */
	String getDate();

	/**
	 * This method causes the server to throw a certain Exception. The exception
	 * is specifed by the name.
	 * 
	 * @param name
	 *            the full-qualified name of the exception which is requested.
	 * @return <code>null</code> since the requested failure is thrown.
	 * @throws Throwable
	 *             (Exception)
	 */
	String throwException(String name) throws Throwable;

	/**
	 * Throws a nested exception.
	 * 
	 * @return
	 * @throws Throwable
	 */
	String throwNestedException() throws Throwable;

	/**
	 * This method causes the server to throw a certain RuntimeException. So the
	 * exception is not in the method signature. So we test communication
	 * whether that is still working.
	 * 
	 * @param name
	 * @return
	 */
	String throwRuntimeException(String name);

}
