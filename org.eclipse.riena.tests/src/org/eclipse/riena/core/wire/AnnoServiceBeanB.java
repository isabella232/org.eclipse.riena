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

/**
 *
 */
public class AnnoServiceBeanB extends AnnoServiceBeanA {

	@InjectService(service = Stunk.class)
	public void bind(final Stunk stunk) {
		SequenceUtil.add(AnnoServiceBeanB.class);
	}

	public void unbind(final Stunk stunk) {
		SequenceUtil.add(AnnoServiceBeanB.class);
	}

}
