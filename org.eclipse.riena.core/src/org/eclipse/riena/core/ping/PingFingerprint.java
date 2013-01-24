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

/**
 * The fingerprint used to identify a {@link IPingable IPingable}.
 */
public class PingFingerprint {

	private String name;

	/**
	 * Dummy constructor for hessian.
	 */
	@SuppressWarnings("unused")
	private PingFingerprint() {
	}

	/**
	 * Creates a {@code PingFingerprint}.
	 * 
	 * @param pingable
	 *            the IPingable to create the fingerprint for.
	 */
	public PingFingerprint(final IPingable pingable) {
		this(pingable, true);
	}

	/**
	 * Creates a {@code PingFingerprint}.
	 * 
	 * @param pingable
	 *            the IPingable to create the fingerprint for.
	 * @param isSingleton
	 */
	public PingFingerprint(final IPingable pingable, final boolean isSingleton) {
		this(pingable, createPostfix(pingable, isSingleton));
	}

	protected static String createPostfix(final IPingable pingable, final boolean isSingleton) {
		if (!isSingleton) {
			return Integer.toString(System.identityHashCode(pingable));
		}
		return null;
	}

	/**
	 * Create a {@code PingFingerprint} with a given post-fix.
	 * 
	 * @param pingable
	 * @param postfix
	 */
	public PingFingerprint(final IPingable pingable, final String postfix) {
		final StringBuilder bob = new StringBuilder(pingable.getClass().getName());
		if (postfix != null) {
			bob.append("#"); //$NON-NLS-1$
			bob.append(postfix);
		}
		name = bob.toString();
	}

	/**
	 * Return the name of the fingerprint.
	 * 
	 * @return the name of the fingerprint.
	 */
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof PingFingerprint)) {
			return false;
		}
		return name.equals(((PingFingerprint) other).name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "PingFingerprint[" + name + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
