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
 * Tests class {@link DefaultPingable DefaultPingable}.
 */
@NonUITestCase
public class DefaultPingableTest extends RienaTestCase {

	/**
	 * Tests method {@link DefaultPingable#ping(PingVisitor) ping()}.
	 */
	public void testPing() {
		final DefaultPingable pingable = new DefaultPingable() {
		};
		final PingVisitor visitorMock = new PingVisitorMock();
		((PingVisitorMock) visitorMock).setExpectations(pingable, visitorMock);

		final PingVisitor result = pingable.ping(visitorMock);
		assertEquals(visitorMock, result);
	}

	/**
	 * Tests method {@link DefaultPingable#getPingFingerprint()
	 * getPingFingerprint()}.
	 */
	public void testGetPingFingerprint() {
		final DefaultPingable pingable = new DefaultPingable() {
		};
		assertEquals(new PingFingerprint(pingable), pingable.getPingFingerprint());
	}

}
