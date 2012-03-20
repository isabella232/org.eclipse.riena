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
package org.eclipse.riena.navigation.common;

import org.eclipse.core.runtime.PlatformObject;

/**
 * Implements the type casting interface
 */
public class TypecastingObject extends PlatformObject implements ITypecastingAdaptable {

	/**
	 * Cast the object to the requested class
	 */
	public <T> T getTypecastedAdapter(final Class<T> pClass) {
		return (T) getAdapter(pClass);
	}

}
