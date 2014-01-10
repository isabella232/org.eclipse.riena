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
package org.eclipse.riena.core.util;

import java.util.Arrays;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * Nomen est Omen!
 */
@NonUITestCase
public class Base16UtilTest extends RienaTestCase {

	/**
	 * Nomen est Omen!
	 */
	public void testOddStringLength() {
		try {
			Base16Util.toBytes("1"); //$NON-NLS-1$
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
	}

	/**
	 * Nomen est Omen!
	 */
	public void testAllBytes() {
		final byte[] bytes = new byte[255];
		for (int i = 0; i < 255; i++) {
			bytes[i] = (byte) i;
		}
		final String bytesString = Base16Util.toString(bytes);
		assertTrue(Arrays.equals(bytes, Base16Util.toBytes(bytesString.toUpperCase())));
		assertTrue(Arrays.equals(bytes, Base16Util.toBytes(bytesString.toLowerCase())));
	}

}
