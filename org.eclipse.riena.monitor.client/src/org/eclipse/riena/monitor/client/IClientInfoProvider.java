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
package org.eclipse.riena.monitor.client;

/**
 * A {@code IClientInfoProvider} is responsible for delivering information about
 * the client, e.g. user name, host name, ip address,..<br>
 * This information will be stored with each {@code Collectible} and transfered
 * to the server.
 */
public interface IClientInfoProvider {

	/**
	 * Return the client information encoded as a {@code String}.
	 * 
	 * @return the client info
	 */
	String getClientInfo();

}
