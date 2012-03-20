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
package org.eclipse.riena.core.wire;

import org.osgi.framework.BundleContext;

/**
 *
 */
public class BeanWiringMock extends AbstractWiring {

	@Override
	public void wire(final Object bean, final BundleContext context) {
		((Bean) bean).bind(new SchtonkSchtonk());
	}

}
