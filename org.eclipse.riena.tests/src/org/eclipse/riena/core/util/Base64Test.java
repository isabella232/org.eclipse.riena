/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     compeople AG - copied from org.eclipse.equinox.internal.security.tests.storage
 *                    and adapted to our code style and needs
 *******************************************************************************/
package org.eclipse.riena.core.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Test {@link Base64}
 */
@NonUITestCase
public class Base64Test extends RienaTestCase {

	/**
	 * Number of random-generated round trip tests to run
	 */
	final private static int RANDOM_ITERATIONS = 1000;

	final private static String DECODED1 = "sample^^*"; //$NON-NLS-1$
	final private static String ENCODED1 = "c2FtcGxlXl4q"; //$NON-NLS-1$

	final private static String DECODED2 = "lazy frog jumped over sleeping dog"; //$NON-NLS-1$
	final private static String ENCODED2 = "bGF6eSBmcm9nIGp1bXBlZCBvdmVyIHNsZWVwaW5nIGRvZw=="; //$NON-NLS-1$

	final private static byte[] DECODED3 = { 5, 0, 0, 12, 32, 1, 127, (byte) 0xFF };
	final private static String ENCODED3 = "BQAADCABf/8="; //$NON-NLS-1$

	final private static byte[] EDGE_CASE_DECODED = new byte[0];
	final private static String EDGE_CASE_ENCODED = ""; //$NON-NLS-1$

	final private static String BAD_ENCODIN_INVALID_CHARS = "M\05S4y\tM\n\rzQ=\r\n"; //$NON-NLS-1$

	/**
	 * Tests encoding using hand-calculated examples.
	 */
	public void testHandCoded() {
		final String encoded = new String(Base64.encode(DECODED1.getBytes()));
		assertEquals(ENCODED1, encoded);

		final String decoded = new String(Base64.decode(ENCODED2.getBytes()));
		assertEquals(DECODED2, decoded);

		final String testZeroes = new String(Base64.encode(DECODED3));
		assertEquals(ENCODED3, testZeroes);
		final byte[] roundtripBytes = Base64.decode(testZeroes.getBytes());
		assertTrue(Arrays.equals(DECODED3, roundtripBytes));

		try {
			Base64.decode(BAD_ENCODIN_INVALID_CHARS.getBytes());
		} catch (final IllegalArgumentException e) {
			ok();
		}
	}

	/**
	 * Tests edge conditions: null or empty arguments
	 */
	public void testEdge() {
		try {
			Base64.encode(null);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
		try {
			Base64.decode(null);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}

		final String encoded = new String(Base64.encode(EDGE_CASE_DECODED));
		assertNotNull(encoded);
		assertEquals(EDGE_CASE_ENCODED, encoded);

		final byte[] decoded = Base64.decode(encoded.getBytes());
		assertTrue(Arrays.equals(EDGE_CASE_DECODED, decoded));
	}

	/**
	 * Tests round trip using large random sequences
	 */
	public void testRandom() {
		final Random generator = new Random(System.currentTimeMillis());

		for (int i = 0; i < RANDOM_ITERATIONS; i++) {
			// length of array is random in [100, 1000)
			final int length = 100 + generator.nextInt(900);
			final byte[] bytes = new byte[length];
			generator.nextBytes(bytes);

			// round trip
			final byte[] encoded = Base64.encode(bytes);
			final byte[] decoded = Base64.decode(encoded);
			assertTrue(Arrays.equals(bytes, decoded));
		}
	}

	public void testRegression() throws IOException {
		final String encodeBASE64 = new BASE64Encoder().encode(DECODED2.getBytes());
		final String encodeBase64 = new String(Base64.encode(DECODED2.getBytes()));
		assertEquals(encodeBASE64, encodeBase64);
		final String decodeBASE64 = new String(new BASE64Decoder().decodeBuffer(encodeBASE64));
		final String decodeBase64 = new String(Base64.decode(encodeBase64.getBytes()));
		assertEquals(decodeBASE64, decodeBase64);
	}

}