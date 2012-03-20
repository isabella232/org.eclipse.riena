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
package org.eclipse.riena.core.ping;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Tests class {@link PingFingerprint PingFingerprint}.
 */
@NonUITestCase
public class PingFingerprintTest extends RienaTestCase {

	/**
	 * Tests method {@link PingFingerprint#PingFingerprint(IPingable)
	 * PingFingerprint(IPingable)}.
	 */
	public void testPingFingerprintIPingable() {
		try {
			new PingFingerprint(null);
			fail("Expected precondition violation"); //$NON-NLS-1$
		} catch (final Exception e) {
			ok();
		}
		final IPingable pingable = new DefaultPingable() {
		};
		final PingFingerprint fingerprint = new PingFingerprint(pingable);
		assertEquals(new PingFingerprint(pingable, true), fingerprint);
	}

	/**
	 * Tests method {@link PingFingerprint#PingFingerprint(IPingable, boolean)
	 * PingFingerprint(IPingable, boolean)}.
	 */
	public void testPingFingerprintIPingableBoolean() {
		try {
			new PingFingerprint(null, true);
			fail("Expected precondition violation"); //$NON-NLS-1$
		} catch (final Exception e) {
			ok();
		}
		final IPingable pingable = new DefaultPingable() {
		};

		assertEquals("PingFingerprint[" + pingable.getClass().getName() + "]", //$NON-NLS-1$ //$NON-NLS-2$
				new PingFingerprint(pingable, true).toString());

		assertEquals(
				"PingFingerprint[" + pingable.getClass().getName() + "#" + System.identityHashCode(pingable) + "]", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				new PingFingerprint(pingable, false).toString());
	}

	/**
	 * Tests method {@link PingFingerprint#equals(Object) equals()} and
	 * {@link PingFingerprint#hashCode() hashCode()}.
	 */
	public void testEqualsAndHash() {
		final IPingable pingable = new DefaultPingable() {
		};
		final PingFingerprint fingerprintA = new PingFingerprint(pingable, "a"); //$NON-NLS-1$
		final PingFingerprint fingerprintB = new PingFingerprint(pingable, "b"); //$NON-NLS-1$
		final PingFingerprint fingerprintAOtherPingable = new PingFingerprint(new DefaultPingable() {
		}, "a"); //$NON-NLS-1$

		checkEqualsAndHashCode(true, fingerprintA, fingerprintA);
		checkEqualsAndHashCode(true, fingerprintA, new PingFingerprint(pingable, "a")); //$NON-NLS-1$
		checkEqualsAndHashCode(false, fingerprintA, fingerprintB);
		checkEqualsAndHashCode(false, fingerprintA, fingerprintAOtherPingable);
		checkEqualsAndHashCode(false, fingerprintA, new Integer(3));
		checkEqualsAndHashCode(false, fingerprintA, null);
	}

	private void checkEqualsAndHashCode(final boolean equalityExpected, final Object first, final Object second) {

		if (first == null) {
			assertEquals(equalityExpected, first == second);
			return;
		}
		assertEquals(equalityExpected, first.equals(second));
		if (second != null) {
			assertEquals(equalityExpected, first.hashCode() == second.hashCode());
		}
	}

}
