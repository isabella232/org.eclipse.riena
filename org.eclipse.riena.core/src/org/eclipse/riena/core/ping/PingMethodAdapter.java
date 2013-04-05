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
package org.eclipse.riena.core.ping;

import java.lang.reflect.Method;

import org.eclipse.core.runtime.Assert;

/**
 * Encapsulates a method call (a <code>ping..()</code> method) as a
 * {@link IPingable IPingable}.
 */
public class PingMethodAdapter extends DefaultPingable {

	private final Method method;
	private final IPingable pingable;

	/**
	 * Creates a PingMethodAdapter.
	 * 
	 * @param pingable
	 * @param method
	 * @pre pingable != null
	 * @pre method != null
	 */
	public PingMethodAdapter(final IPingable pingable, final Method method) {
		Assert.isNotNull(pingable, "pingable"); //$NON-NLS-1$
		Assert.isNotNull(method, "method"); //$NON-NLS-1$
		this.pingable = pingable;
		this.method = method;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PingVisitor ping(final PingVisitor visitor) {
		invokePingMethod();
		return visitor.visit(this);
	}

	/**
	 * Invokes the ping...() method on the pingable.
	 */
	protected void invokePingMethod() {
		try {
			method.invoke(pingable, new Object[0]);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PingFingerprint getPingFingerprint() {
		return new PingFingerprint(pingable, method.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof PingMethodAdapter)) {
			return false;
		}
		final PingMethodAdapter otherPingableMethodAdapter = (PingMethodAdapter) other;
		return (pingable == otherPingableMethodAdapter.pingable) && method.equals(otherPingableMethodAdapter.method);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return pingable.hashCode() + 7 * method.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "PingMethodAdapter[pingable=" + pingable + ", method=" + method.getName() + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
