/*******************************************************************************
 * Copyright (c) 2007, 2013 compeople AG and others.
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
public class AnnoExtBeanB extends AnnoExtBeanA {

	@InjectExtension(id = "core.test.extpointB")
	public void config(final IDataB data) {
		if (data != null) {
			SequenceUtil.add(data.getInfo());
		} else {
			SequenceUtil.add("-IDataB");
		}
	}

}
