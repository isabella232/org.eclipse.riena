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
package org.eclipse.riena.core.wire;

import org.osgi.framework.BundleContext;

/**
 * Define the wiring needs for a bean.
 * <p>
 * Classes implementing this interface take responsibility for the wiring of a
 * bean.
 * 
 * @see WireWith
 */
public interface IWiring {

	/**
	 * Wire the given bean.
	 * 
	 * @param bean
	 *            bean to wire
	 * @param context
	 *            current bundle context
	 */
	void wire(Object bean, BundleContext context);

	/**
	 * Unwire the given bean.
	 * 
	 * @param bean
	 *            bean to wire
	 * @param context
	 *            current bundle context
	 */
	void unwire(Object bean, BundleContext context);
}
