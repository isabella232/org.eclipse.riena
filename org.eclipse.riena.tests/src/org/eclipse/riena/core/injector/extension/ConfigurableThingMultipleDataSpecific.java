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
public class ConfigurableThingMultipleDataSpecific {

	private IExtData[] data;

	public void update(final IExtData[] data) {
		// TODO warning suppression: Ignore FindBugs warning about internal
		// representation being exposed: seems ok for testing
		this.data = data;
	}

	public IExtData[] getExtData() {
		// TODO warning suppression: Ignore FindBugs warning about internal
		// representation being exposed: seems ok for testing
		return data;
	}

}
