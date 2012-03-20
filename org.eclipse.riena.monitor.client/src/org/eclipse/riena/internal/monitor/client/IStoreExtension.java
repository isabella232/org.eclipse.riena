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
package org.eclipse.riena.internal.monitor.client;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;
import org.eclipse.riena.monitor.client.IStore;

/**
 * Extension interface for the {@code IStore} definition.
 */
@ExtensionInterface(id = "store")
public interface IStoreExtension {

	/**
	 * Return the descriptive name of the store.
	 * 
	 * @return the descriptive name of the store
	 */
	String getName();

	/**
	 * Create the configured {@code IStore}.
	 * 
	 * @return the store
	 */
	@MapName("class")
	IStore createStore();

}
