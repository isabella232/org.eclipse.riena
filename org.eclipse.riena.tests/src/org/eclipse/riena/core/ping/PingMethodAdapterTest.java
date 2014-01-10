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
package org.eclipse.riena.core.ping;

import java.lang.reflect.Method;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * Tests class {@link PingMethodAdapter PingMethodAdapter}.
 */
@NonUITestCase
public class PingMethodAdapterTest extends RienaTestCase {

	protected static class PingableMock extends DefaultPingable {

		private boolean pingMethodCalled;

		public void pingDiesUndDas() {
			pingMethodCalled = true;
		}

		public void reset() {
			pingMethodCalled = false;
		}

		public void verify() {
			assertEquals(true, pingMethodCalled);
		}

		public Method getPingDiesUndDasMethod() throws Exception {
			return this.getClass().getMethod("pingDiesUndDas", new Class[0]); //$NON-NLS-1$
		}

		public Method getNonePingMethod() throws Exception {
			return this.getClass().getMethod("dummyMethod", new Class[0]); //$NON-NLS-1$
		}

		public void dummyMethod() {

		}
	}

	protected PingableMock pingableMock = new PingableMock();

	/**
	 * Tests method
	 * {@link PingMethodAdapter#PingMethodAdapter(IPingable, Method)
	 * PingMethodAdapter()}.
	 */
	public void testPingMethodAdapter() throws Exception {
		try {
			new PingMethodAdapter(pingableMock, null);
			fail("Expected precondition violation"); //$NON-NLS-1$
		} catch (final Exception e) {
			ok();
		}
		try {
			new PingMethodAdapter(null, pingableMock.getPingDiesUndDasMethod());
			fail("Expected precondition violation"); //$NON-NLS-1$
		} catch (final Exception e) {
			ok();
		}
		new PingMethodAdapter(pingableMock, pingableMock.getPingDiesUndDasMethod());
	}

	/**
	 * Tests method {@link PingMethodAdapter#ping(PingVisitor) ping()}.
	 */
	public void testPing() throws Exception {
		final PingMethodAdapter adapter = new PingMethodAdapter(pingableMock, pingableMock.getPingDiesUndDasMethod());
		adapter.ping(new PingVisitor());
		pingableMock.verify();
	}

	/**
	 * Tests method {@link PingMethodAdapter#getPingFingerprint()
	 * getPingFingerprint()}.
	 */
	public void testGetPingFingerprint() throws Exception {
		final PingMethodAdapter adapter = new PingMethodAdapter(pingableMock, pingableMock.getPingDiesUndDasMethod());
		assertEquals(new PingFingerprint(pingableMock, pingableMock.getPingDiesUndDasMethod().getName()),
				adapter.getPingFingerprint());
	}

	/**
	 * Tests method {@link PingMethodAdapter#equals(Object) equals()} and
	 * {@link PingMethodAdapter#hashCode() hashCode()}.
	 */
	public void testEqualsAndHash() throws Exception {
		final PingMethodAdapter adapter = new PingMethodAdapter(pingableMock, pingableMock.getPingDiesUndDasMethod());
		checkEqualsAndHashCode(true, adapter, adapter);
		checkEqualsAndHashCode(true, adapter,
				new PingMethodAdapter(pingableMock, pingableMock.getPingDiesUndDasMethod()));
		checkEqualsAndHashCode(true, adapter,
				new PingMethodAdapter(pingableMock, pingableMock.getPingDiesUndDasMethod()));

		checkEqualsAndHashCode(false, adapter, new PingMethodAdapter(pingableMock, pingableMock.getNonePingMethod()));
		final PingableMock otherPingableMock = new PingableMock();
		checkEqualsAndHashCode(false, adapter,
				new PingMethodAdapter(otherPingableMock, otherPingableMock.getPingDiesUndDasMethod()));
		checkEqualsAndHashCode(false, adapter,
				new PingMethodAdapter(otherPingableMock, pingableMock.getPingDiesUndDasMethod()));
		checkEqualsAndHashCode(false, adapter, 3);
		checkEqualsAndHashCode(false, adapter, null);
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

	/**
	 * Tests method {@link PingMethodAdapter#toString() toString()}.
	 */
	public void testToString() throws Exception {
		final PingMethodAdapter adapter = new PingMethodAdapter(pingableMock, pingableMock.getPingDiesUndDasMethod());
		assertEquals("PingMethodAdapter[pingable=" + pingableMock + ", method=" //$NON-NLS-1$ //$NON-NLS-2$
				+ pingableMock.getPingDiesUndDasMethod().getName() + "]", adapter.toString()); //$NON-NLS-1$
	}

}
