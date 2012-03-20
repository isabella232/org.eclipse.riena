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
package org.eclipse.riena.ui.core.context;

/**
 * Interface representing the ability to hold key value pairs of context data.
 * 
 * @since 1.2
 */
public interface IContext {

	/**
	 * Returns the saved value for the given key.
	 * 
	 * @param key
	 *            key whose associated value is to be returned.
	 * @return the saved value
	 */
	Object getContext(String key);

	/**
	 * Saves the value for the given key.
	 * 
	 * @param key
	 *            key with which the specified value is to be associated.
	 * @param value
	 *            value to be associated with the specified key
	 */
	void setContext(String key, Object value);

}
