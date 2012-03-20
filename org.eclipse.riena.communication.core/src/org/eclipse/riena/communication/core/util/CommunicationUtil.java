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
package org.eclipse.riena.communication.core.util;

/**
 * CommunicationUtil class
 */
public final class CommunicationUtil {

	private CommunicationUtil() {
		super();
	}

	/**
	 * Answers the String value for the given propValue object. If the propValue
	 * not compatible to String or String[] answers the given
	 * <code>returnIfNoString</code> as default. If propValue is an instance of
	 * String[] then answers the item with index=0.
	 * 
	 * @param propValue
	 * @param returnIfNoString
	 * @return the String value for propValue or the default value
	 *         <code>returnIfNoString</code>
	 */
	public static String accessProperty(final Object propValue, final String returnIfNoString) {
		if (propValue instanceof String) { // if api programmed we receive
			return (String) propValue; // a String
		} else {
			if (propValue instanceof String[]) { // for DS we receive a
				return ((String[]) propValue)[0]; // String array
			}
		}
		return returnIfNoString;
	}

}
