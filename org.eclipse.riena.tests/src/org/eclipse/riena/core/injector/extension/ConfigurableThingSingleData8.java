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
package org.eclipse.riena.core.injector.extension;

/**
 * 
 */
public class ConfigurableThingSingleData8 {

	private IData8 data;

	public void configure(final IData8 data) {
		this.data = data;
	}

	public IData8 getData() {
		return data;
	}

}
