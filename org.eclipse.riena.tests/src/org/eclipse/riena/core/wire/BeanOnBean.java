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

/**
 *
 */
@WireWith(BeanOnBeanWiring.class)
public class BeanOnBean extends Bean {

	private Stunk stunk;

	public void bind(final Stunk stunk) {
		this.stunk = stunk;
	}

	public void unbind(final Stunk stunk) {
		this.stunk = stunk;
	}

	/**
	 * @return
	 */
	public boolean hasStunk() {
		return stunk != null;
	}

}
