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
package org.eclipse.riena.navigation.common;

import org.eclipse.core.runtime.IAdaptable;

/**
 * An adaptable, but casted to the destination class.
 */
public interface ITypecastingAdaptable extends IAdaptable {

	/**
	 * Returns an object of class <code>pClass</code>. Returns <code>null</code>
	 * if no such object can be found.
	 * 
	 * @param pClass
	 *            the adapter class to look up.
	 * @return an object of class <code>pClass</code>, or <code>null</code> if
	 *         this object does not have an adapter for the given class.
	 */
	<T> T getTypecastedAdapter(Class<T> pClass);
}
