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
package org.eclipse.riena.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Get ciphers for simple symmetric encryption/decryption.
 * 
 * @since 3.0
 */
public final class CipherUtils {

	/**
	 * 
	 */
	private static final String DES_ALGORITHM = "DES"; //$NON-NLS-1$

	private CipherUtils() {
		// utility
	}

	/**
	 * Get a cipher.
	 * 
	 * @param key
	 *            the key
	 * @param mode
	 *            the cipher mode, e.g. Cipher.ENCRYPT_MODE
	 * @return the cipher or null, if not possible
	 * @throws GeneralSecurityException
	 */
	public static Cipher getCipher(final byte[] key, final int mode) throws GeneralSecurityException {

		// Create the cipher 
		final Cipher desCipher = Cipher.getInstance(DES_ALGORITHM);

		// Initialize the cipher
		desCipher.init(mode, getKey(key));

		return desCipher;
	}

	private static Key getKey(final byte[] key) throws GeneralSecurityException {
		final DESKeySpec pass = new DESKeySpec(getAtLeastEightBytes(key));
		final SecretKeyFactory skf = SecretKeyFactory.getInstance(DES_ALGORITHM);
		final SecretKey s = skf.generateSecret(pass);
		return s;
	}

	private static byte[] getAtLeastEightBytes(final byte[] initialBytes) {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
		int bytesIndex = 0;
		for (final byte b : initialBytes == null ? new byte[0] : initialBytes) {
			bos.write(b);
			bytesIndex++;
		}
		if (bytesIndex < 8) {
			final Random random = new Random(4711081542L);
			while (bytesIndex++ < 8) {
				final byte[] randomByte = new byte[1];
				random.nextBytes(randomByte);
				try {
					bos.write(randomByte);
				} catch (final IOException e) {
					Nop.reason(" That should never happen on a ByteArrayOutputStream"); //$NON-NLS-1$
				}
			}
		}
		return bos.toByteArray();
	}
}