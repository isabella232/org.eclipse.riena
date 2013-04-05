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
package org.eclipse.riena.internal.communication.core.ssl;

import javax.net.ssl.HostnameVerifier;

import org.osgi.framework.Bundle;

import org.eclipse.riena.core.injector.extension.DefaultValue;
import org.eclipse.riena.core.injector.extension.ExtensionInterface;

/**
 * SSl properties for the SSL configuration.
 */
@ExtensionInterface(id = "ssl")
public interface ISSLPropertiesExtension {

	/**
	 * The transport layer security protocol, e.g. "TLSv1"
	 * 
	 * @return the transport layer security protocol
	 */
	String getProtocol();

	/**
	 * The location of the keystore.
	 * <p>
	 * If the value is #jre-cacerts# than the keystore of the JVM will be used.
	 * Otherwise, several attempts are made to interpret the the location as:
	 * <ul>
	 * <li>
	 * an entry via Bundle.getEntry()</li>
	 * <li>
	 * a resource via Bundle.getResource()</li>
	 * <li>
	 * a file</li>
	 * <li>
	 * a URL</li>
	 * </ul>
	 * 
	 * @return keystore location
	 */
	String getKeystore();

	/**
	 * The password of the keystore.
	 * 
	 * @return the password
	 */
	String getPassword();

	/**
	 * Return an encryption information.
	 * 
	 * @return the information.
	 */
	@DefaultValue("false")
	String getEncrypt();

	/**
	 * Create an optional host name verifier that can relax the strict host name
	 * check of the default implementation which is used if this is not given.
	 * 
	 * @return the {@code HostnameVerifier}
	 */
	HostnameVerifier createHostnameVerifier();

	/**
	 * Get the contributing bundle. This is required for loading the key store.
	 * 
	 * @return the contributing bundle
	 */
	Bundle getContributingBundle();
}
