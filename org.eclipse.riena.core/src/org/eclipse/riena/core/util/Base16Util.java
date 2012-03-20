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

/**
 * Base 16 conversions between strings and bytes.
 * 
 * @author Stefan Liebig
 * @since 3.0
 */
public final class Base16Util {

	private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray(); //$NON-NLS-1$

	private Base16Util() {
		//utility
	}

	/**
	 * Convert a base 16 string into bytes.
	 * 
	 * @param hexaString
	 * @return
	 */
	public static byte[] toBytes(final String hexaString) {
		if (hexaString.length() % 2 == 1) {
			throw new IllegalArgumentException("String length must be even."); //$NON-NLS-1$
		}
		final byte[] bytes = new byte[hexaString.length() / 2];
		for (int bi = 0, i = 0; bi < bytes.length; bi++) {
			int b = Character.digit(hexaString.charAt(i++), 16) << 4;
			b |= Character.digit(hexaString.charAt(i++), 16);
			bytes[bi] = (byte) b;
		}
		return bytes;
	}

	/**
	 * Convert bytes into a base 16 string.
	 * 
	 * @param bytes
	 * @return
	 */
	public static String toString(final byte[] bytes) {
		final char[] chars = new char[bytes.length * 2];
		for (int i = 0, j = 0; i < bytes.length; i++) {
			chars[j++] = HEXDIGITS[(bytes[i] >> 4) & 0x0f];
			chars[j++] = HEXDIGITS[bytes[i] & 0x0f];
		}
		return new String(chars);
	}
}
