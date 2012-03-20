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
package org.eclipse.riena.core.singleton;

/**
 * A {@code ISingletonInitializer} allows to initialize a 'singleton'. This
 * might be necessary because the RAP
 * {@code org.eclipse.rwt.SessionSingletonBase} is only capable of creating
 * singletons with their default constructor.
 * 
 * @since 4.0
 */
public interface ISingletonInitializer<T> {

	/**
	 * Perform any necessary initialization actions on the newly created
	 * singleton.
	 * 
	 * @param instance
	 */
	void init(T instance);
}