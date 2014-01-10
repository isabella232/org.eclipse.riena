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
package org.eclipse.riena.internal.communication.core.ssl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.crypto.Cipher;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.osgi.framework.Bundle;
import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.Base16Util;
import org.eclipse.riena.core.util.CipherUtils;
import org.eclipse.riena.core.util.Iter;
import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.internal.communication.core.Activator;

/**
 * This class performs the configuration of the trust store for jre SSL Protocol
 * implementation.
 */
public class SSLConfiguration {

	private String protocol;
	private String keystore;
	private String password;
	private String encrypt;
	private HostnameVerifier hostnameVerifier;
	private Bundle contributingBundle;

	private boolean configured;

	private String previousHttpsProtocol;
	private HostnameVerifier previousHostnameVerifier;
	private SSLSocketFactory previousSSLSocketFactor;

	private static final String JRE_CACERTS_MARKER = "#jre-cacerts#"; //$NON-NLS-1$
	private static final String HTTPS_PROTOCOLS_PROPERTY_KEY = "https.protocols"; //$NON-NLS-1$

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), SSLConfiguration.class);

	@InjectExtension(min = 0, max = 1)
	public void configure(final ISSLPropertiesExtension sslProperties) {
		if (configured && sslProperties == null) {
			restore();
			return;
		}
		configured = false;

		if (sslProperties == null) {
			LOGGER.log(LogService.LOG_INFO, "No configuration given!."); //$NON-NLS-1$
			return;
		}
		protocol = sslProperties.getProtocol();
		keystore = sslProperties.getKeystore();
		password = sslProperties.getPassword();
		encrypt = sslProperties.getEncrypt();
		contributingBundle = sslProperties.getContributingBundle();

		LOGGER.log(LogService.LOG_INFO, "Configuring SSL protocol '" + protocol + "' with keystore '" + keystore //$NON-NLS-1$ //$NON-NLS-2$
				+ "'."); //$NON-NLS-1$

		// Check protocol & keystore
		if (keystore == null || keystore.length() == 0 || protocol == null || protocol.length() == 0) {
			// no keystore configured. Apparently no SSL used in this context.
			LOGGER.log(LogService.LOG_WARNING, "Neither keystore nor protocol given!"); //$NON-NLS-1$
			return;
		}

		// save previous value
		previousHttpsProtocol = System.getProperty(HTTPS_PROTOCOLS_PROPERTY_KEY);
		// set new value
		System.setProperty(HTTPS_PROTOCOLS_PROPERTY_KEY, protocol);

		try {
			// obtain some debug information related to security providers
			final Provider[] providers = Security.getProviders();
			if (providers == null) {
				LOGGER.log(LogService.LOG_WARNING,
						"Security did not find any providers. This might be a problem. Check imported jar files for sunjce_provider.jar!"); //$NON-NLS-1$
			} else {
				LOGGER.log(LogService.LOG_INFO, "Security found " + providers.length + " security providers."); //$NON-NLS-1$ //$NON-NLS-2$
				for (int i = 0; i < providers.length; i++) {
					LOGGER.log(LogService.LOG_DEBUG, "Security provider[" + i + "]: " + providers[i].getName()); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}

			final KeyStore keyStore = KeyStore.getInstance("JKS"); //$NON-NLS-1$
			final URL keystoreUrl = getKeystoreUrl();

			if (keystoreUrl == null) {
				LOGGER.log(LogService.LOG_ERROR, "Specified keystore '" + keystore //$NON-NLS-1$
						+ "' can not be found. SSL not initialized."); //$NON-NLS-1$
				return;
			}

			LOGGER.log(LogService.LOG_DEBUG, "Keystore is '" + keystoreUrl + "'."); //$NON-NLS-1$ //$NON-NLS-2$

			final char[] passwordChars = getPasswordChars(password, encrypt);

			keyStore.load(keystoreUrl.openStream(), passwordChars);

			// Some debug information
			final Enumeration<String> enumeration = keyStore.aliases();
			if (enumeration == null) {
				LOGGER.log(LogService.LOG_ERROR, "Found no certificate."); //$NON-NLS-1$
				throw new Exception("Found no certificate."); //$NON-NLS-1$
			} else {
				for (final String alias : Iter.able(enumeration)) {

					LOGGER.log(LogService.LOG_DEBUG, "Found certificate: " + alias); //$NON-NLS-1$
					final Certificate certificate = keyStore.getCertificate(alias);
					if (certificate instanceof X509Certificate) {
						final X509Certificate x509Certificate = (X509Certificate) certificate;
						LOGGER.log(LogService.LOG_DEBUG, "  Subject: " + x509Certificate.getSubjectDN()); //$NON-NLS-1$
						LOGGER.log(LogService.LOG_DEBUG, "  Issuer : " + x509Certificate.getIssuerDN()); //$NON-NLS-1$
						LOGGER.log(LogService.LOG_DEBUG, "  Valid from " + x509Certificate.getNotBefore() + " to " //$NON-NLS-1$ //$NON-NLS-2$
								+ x509Certificate.getNotAfter());
					} else {
						LOGGER.log(LogService.LOG_DEBUG, "  " + certificate); //$NON-NLS-1$
					}
				}
			}

			final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509"); //$NON-NLS-1$
			trustManagerFactory.init(keyStore);

			final TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

			final SSLContext sslContext = SSLContext.getInstance("SSL"); //$NON-NLS-1$
			sslContext.init(null, trustManagers, null);

			LOGGER.log(LogService.LOG_DEBUG, "SSLContext protocol: " + sslContext.getProtocol()); //$NON-NLS-1$
			LOGGER.log(LogService.LOG_DEBUG, "SSLContext SocketFactory: " + sslContext.getSocketFactory()); //$NON-NLS-1$

			// save old value
			previousSSLSocketFactor = HttpsURLConnection.getDefaultSSLSocketFactory();
			// set new value
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

			hostnameVerifier = sslProperties.createHostnameVerifier();
			if (hostnameVerifier == null) {
				hostnameVerifier = new StrictHostnameVerifier();
			} else {
				LOGGER.log(LogService.LOG_DEBUG, "Using custom host name verifier " //$NON-NLS-1$
						+ hostnameVerifier.getClass().getName());
			}

			// save old value
			previousHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
			// set new value
			HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

			LOGGER.log(LogService.LOG_INFO, "Configuring the SSL protocol finished!"); //$NON-NLS-1$
			configured = true;
		} catch (final Exception ex) {
			LOGGER.log(LogService.LOG_ERROR, "Configuration of SSL protocol failed. SSL will not work properly!", ex); //$NON-NLS-1$
		}
	}

	private char[] getPasswordChars(final String password, final String encrypt) throws Exception {
		if (password == null) {
			return null;
		}
		if (!Boolean.parseBoolean(encrypt)) {
			return password.toCharArray();
		}
		final Cipher decrypt = CipherUtils.getCipher(null, Cipher.DECRYPT_MODE);
		return new String(decrypt.doFinal(Base16Util.toBytes(password))).toCharArray();
	}

	/**
	 * @return
	 * @throws MalformedURLException
	 */
	private URL getKeystoreUrl() throws MalformedURLException {
		if (keystore.equals(JRE_CACERTS_MARKER)) {
			final String jreDir = System.getProperty("java.home"); //$NON-NLS-1$
			LOGGER.log(LogService.LOG_DEBUG, "Attempting to load keystore from cacerts of the jre: " + jreDir); //$NON-NLS-1$
			// walk down
			final File cacertFile = new File(new File(new File(new File(jreDir), "lib"), "security"), "cacerts"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			return cacertFile.canRead() ? cacertFile.toURL() : null;
		}

		// maybe it is a entry?
		URL keystoreUrl = contributingBundle.getEntry(keystore);
		if (keystoreUrl != null) {
			return keystoreUrl;
		}

		// maybe it is a resource?
		keystoreUrl = contributingBundle.getResource(keystore);
		if (keystoreUrl != null) {
			return keystoreUrl;
		}

		// keystore location a file?
		final File keystoreFile = new File(keystore);
		if (keystoreFile.canRead()) {
			return keystoreFile.toURL();
		}

		// and finally try as a url?
		return new URL(keystore);
	}

	public boolean isConfigured() {
		return configured;
	}

	/**
	 * Restore to previous settings.
	 */
	private void restore() {
		if (previousHttpsProtocol != null) {
			System.setProperty(HTTPS_PROTOCOLS_PROPERTY_KEY, previousHttpsProtocol);
		}
		if (previousSSLSocketFactor != null) {
			HttpsURLConnection.setDefaultSSLSocketFactory(previousSSLSocketFactor);
		}
		if (previousHostnameVerifier != null) {
			HttpsURLConnection.setDefaultHostnameVerifier(previousHostnameVerifier);
		}
	}

	@Override
	public String toString() {
		return "SSLConfiguration [keystore=" + keystore + ", protocol=" + protocol + ", hostnameVerifier=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ hostnameVerifier + "]"; //$NON-NLS-1$
	}

	public final static class StrictHostnameVerifier implements HostnameVerifier {

		public boolean verify(final String hostname, final SSLSession session) {
			LOGGER.log(LogService.LOG_ERROR, "Hostname '" + hostname //$NON-NLS-1$
					+ "' does not match the certificate´s host name (" + session.getPeerHost() + ")!"); //$NON-NLS-1$ //$NON-NLS-2$
			return false;
		}

	}
}
