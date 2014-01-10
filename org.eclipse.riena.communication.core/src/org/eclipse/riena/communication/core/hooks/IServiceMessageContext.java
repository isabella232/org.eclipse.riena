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
package org.eclipse.riena.communication.core.hooks;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Protocol independent interface that gives access to the low level properties
 * of the protocol implementation.
 */
public interface IServiceMessageContext {

	/**
	 * Returns a Map of headers, typical HTTP Headers.
	 * 
	 * @return Map of HTTP Headers
	 */
	Map<String, List<String>> listRequestHeaders();

	/**
	 * Returns the value for a specific HTTP header.
	 * 
	 * @param name
	 *            name of HTTP Header i.e. "Cookie"
	 * @return value of the HTTP Header if found, otherwise it returns null
	 */
	List<String> getRequestHeaderValue(String name);

	/**
	 * Adds a HTTP header to the response.
	 * 
	 * @param name
	 *            name of HTTP Header to add
	 * @param value
	 *            value of HTTP Header to add
	 */
	void addResponseHeader(String name, String value);

	/**
	 * Access for the servlet request for this remote service call.
	 * 
	 * @return ServletRequest object i.e. HttpServletRequest object
	 */
	HttpServletRequest getServletRequest();

}
