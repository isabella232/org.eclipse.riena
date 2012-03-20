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
package org.eclipse.riena.communication.core.ssl;

import java.io.File;
import java.io.IOException;

import javax.net.ssl.HostnameVerifier;

import org.osgi.framework.Bundle;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.communication.core.ssl.ISSLPropertiesExtension;
import org.eclipse.riena.internal.communication.core.ssl.SSLConfiguration;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.tests.nanohttp.TestServer;

/**
 * 
 */
@NonUITestCase
public class SSLConfigurationTest extends RienaTestCase {

	public void testZeroConfiguration() {
		printTestName();

		final SSLConfiguration config = new SSLConfiguration();
		assertNull(ReflectionUtils.getHidden(config, "protocol")); //$NON-NLS-1$
		assertNull(ReflectionUtils.getHidden(config, "keystore")); //$NON-NLS-1$
		assertNull(ReflectionUtils.getHidden(config, "password")); //$NON-NLS-1$
		assertNull(ReflectionUtils.getHidden(config, "hostnameVerifier")); //$NON-NLS-1$
	}

	public void testOneConfiguration() {
		printTestName();
		addPluginXml(SSLConfigurationTest.class, "plugin.xml"); //$NON-NLS-1$

		try {
			final SSLConfiguration config = new SSLConfiguration();
			Wire.instance(config).andStart(getContext());

			assertEquals(ReflectionUtils.getHidden(config, "protocol"), "TLSv1"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(ReflectionUtils.getHidden(config, "keystore"), "#jre-cacerts#"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(ReflectionUtils.getHidden(config, "password"), "changeit"); //$NON-NLS-1$ //$NON-NLS-2$
			assertNotNull(ReflectionUtils.getHidden(config, "hostnameVerifier")); //$NON-NLS-1$
			assertEquals(ReflectionUtils.getHidden(config, "hostnameVerifier").getClass(), //$NON-NLS-1$
					SSLConfiguration.StrictHostnameVerifier.class);
		} finally {
			removeExtension("org.eclipse.riena.communication.core.ssl.test"); //$NON-NLS-1$
		}
	}

	public void testOneConfigurationWithEncryptedPassword() {
		printTestName();
		addPluginXml(SSLConfigurationTest.class, "pluginWithEncryptedPassword.xml"); //$NON-NLS-1$

		try {
			final SSLConfiguration config = new SSLConfiguration();
			Wire.instance(config).andStart(getContext());

			assertEquals(ReflectionUtils.getHidden(config, "protocol"), "TLSv1"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(ReflectionUtils.getHidden(config, "keystore"), "#jre-cacerts#"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(ReflectionUtils.getHidden(config, "password"), "cae3c55508eaa369d5c8870398c90a64"); // not encrypted yet!! //$NON-NLS-1$ //$NON-NLS-2$
			assertNotNull(ReflectionUtils.getHidden(config, "hostnameVerifier")); //$NON-NLS-1$
			assertEquals(ReflectionUtils.getHidden(config, "hostnameVerifier").getClass(), //$NON-NLS-1$
					SSLConfiguration.StrictHostnameVerifier.class);
		} finally {
			removeExtension("org.eclipse.riena.communication.core.ssl.test"); //$NON-NLS-1$
		}
	}

	public void testOneConfigurationWithHostnameVerifier() {
		printTestName();
		addPluginXml(SSLConfigurationTest.class, "pluginWithHostnameVerifier.xml"); //$NON-NLS-1$

		try {
			final SSLConfiguration config = new SSLConfiguration();
			Wire.instance(config).andStart(getContext());

			assertEquals(ReflectionUtils.getHidden(config, "protocol"), "TLSv1"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(ReflectionUtils.getHidden(config, "keystore"), "#jre-cacerts#"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(ReflectionUtils.getHidden(config, "password"), "changeit"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(ReflectionUtils.getHidden(config, "hostnameVerifier").getClass(), TestHostnameVerifier.class); //$NON-NLS-1$
		} finally {
			removeExtension("org.eclipse.riena.communication.core.ssl.test"); //$NON-NLS-1$
		}
	}

	public void testLocateKeystoreJreCacerts() {
		printTestName();
		final ISSLPropertiesExtension properties = new SSLProperties("TLSv1", "#jre-cacerts#", "changeit"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		final SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
		assertEquals(ReflectionUtils.getHidden(config, "protocol"), "TLSv1"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(ReflectionUtils.getHidden(config, "keystore"), "#jre-cacerts#"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(ReflectionUtils.getHidden(config, "password"), "changeit"); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull(ReflectionUtils.getHidden(config, "hostnameVerifier")); //$NON-NLS-1$
		assertEquals(ReflectionUtils.getHidden(config, "hostnameVerifier").getClass(), //$NON-NLS-1$
				SSLConfiguration.StrictHostnameVerifier.class);
	}

	public void testLocateKeystoreJreCacertsEncryptedPassword() {
		printTestName();
		final ISSLPropertiesExtension properties = new SSLProperties("TLSv1", "#jre-cacerts#", //$NON-NLS-1$ //$NON-NLS-2$
				"cae3c55508eaa369d5c8870398c90a64", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		final SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
		assertEquals(ReflectionUtils.getHidden(config, "protocol"), "TLSv1"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(ReflectionUtils.getHidden(config, "keystore"), "#jre-cacerts#"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(ReflectionUtils.getHidden(config, "password"), "cae3c55508eaa369d5c8870398c90a64"); // not encrypted yet!! //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull(ReflectionUtils.getHidden(config, "hostnameVerifier")); //$NON-NLS-1$
		assertEquals(ReflectionUtils.getHidden(config, "hostnameVerifier").getClass(), //$NON-NLS-1$
				SSLConfiguration.StrictHostnameVerifier.class);
	}

	public void testGetPasswordCharsNotEncrypted() {
		final SSLConfiguration config = new SSLConfiguration();
		final char[] password = ReflectionUtils.invokeHidden(config, "getPasswordChars", "changeit", "false"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals("changeit", new String(password)); //$NON-NLS-1$
	}

	public void testGetPasswordCharsEncrypted() {
		final SSLConfiguration config = new SSLConfiguration();
		final char[] password = ReflectionUtils.invokeHidden(config, "getPasswordChars", //$NON-NLS-1$
				"cae3c55508eaa369d5c8870398c90a64", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("changeit", new String(password)); //$NON-NLS-1$
	}

	public void testLocateKeystoreJreCacertsAndCustomHostnameVerifier() {
		printTestName();
		final HostnameVerifier hostnameVerifier = new TestHostnameVerifier();
		final ISSLPropertiesExtension properties = new SSLProperties("TLSv1", "#jre-cacerts#", "changeit", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				hostnameVerifier);
		final SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
		assertEquals(ReflectionUtils.getHidden(config, "protocol"), "TLSv1"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(ReflectionUtils.getHidden(config, "keystore"), "#jre-cacerts#"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(ReflectionUtils.getHidden(config, "password"), "changeit"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(ReflectionUtils.getHidden(config, "hostnameVerifier"), hostnameVerifier); //$NON-NLS-1$
	}

	public void testLocateKeystoreFile() {
		printTestName();
		final String jreDir = System.getProperty("java.home"); //$NON-NLS-1$
		final File cacertFile = new File(new File(new File(new File(jreDir), "lib"), "security"), "cacerts"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		final ISSLPropertiesExtension properties = new SSLProperties("TLSv1", cacertFile.toString(), "changeit"); //$NON-NLS-1$ //$NON-NLS-2$
		final SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
	}

	public void testLocateKeystoreResource() {
		printTestName();
		final ISSLPropertiesExtension properties = new SSLProperties("TLSv1", //$NON-NLS-1$
				"/org/eclipse/riena/communication/core/ssl/cacerts", "changeit"); //$NON-NLS-1$ //$NON-NLS-2$
		final SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
	}

	public void testLocateKeystoreEntry() {
		printTestName();
		final ISSLPropertiesExtension properties = new SSLProperties("TLSv1", "/keystore/cacerts", "changeit"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		final SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
	}

	public void testLocateKeystoreUrl() throws IOException {
		printTestName();
		final String jreDir = System.getProperty("java.home"); //$NON-NLS-1$
		final File cacertDir = new File(new File(new File(jreDir), "lib"), "security"); //$NON-NLS-1$ //$NON-NLS-2$
		final TestServer nano = new TestServer(8888, cacertDir);

		final ISSLPropertiesExtension properties = new SSLProperties("TLSv1", "http://localhost:8888/cacerts", //$NON-NLS-1$ //$NON-NLS-2$
				"changeit"); //$NON-NLS-1$
		final SSLConfiguration config = new SSLConfiguration();
		config.configure(properties);
		assertTrue(config.isConfigured());
		nano.stop();
	}

	private static class SSLProperties implements ISSLPropertiesExtension {

		private final String protocol;
		private final String keystore;
		private final String password;
		private final String encrypt;
		private final HostnameVerifier hostnameVerifier;

		public SSLProperties(final String protocol, final String keystore, final String password) {
			this(protocol, keystore, password, null, null);
		}

		public SSLProperties(final String protocol, final String keystore, final String password,
				final HostnameVerifier hostnameVerifier) {
			this(protocol, keystore, password, null, hostnameVerifier);
		}

		public SSLProperties(final String protocol, final String keystore, final String password, final String encrypt) {
			this(protocol, keystore, password, encrypt, null);
		}

		public SSLProperties(final String protocol, final String keystore, final String password, final String encrypt,
				final HostnameVerifier hostnameVerifier) {
			this.protocol = protocol;
			this.keystore = keystore;
			this.password = password;
			this.encrypt = encrypt;
			this.hostnameVerifier = hostnameVerifier;
		}

		public String getKeystore() {
			return keystore;
		}

		public String getPassword() {
			return password;
		}

		public String getProtocol() {
			return protocol;
		}

		public HostnameVerifier createHostnameVerifier() {
			return hostnameVerifier;
		}

		public Bundle getContributingBundle() {
			return Activator.getDefault().getBundle();
		}

		public String getEncrypt() {
			return encrypt;
		}

	}
}
