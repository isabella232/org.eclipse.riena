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

/**
 * If a pingable could not be retrieved (e.g. reflection not possible due to
 * security restriction), this class is used for proxy in order to provide an
 * error message to the ping originate.
 */
public class UnavailablePingable implements IPingable {

	private final String name;
	private final String message;

	/**
	 * Creates an UnavailablePingable with the given name and failure message.
	 * 
	 * @param name
	 *            the name of the pingable that will be used for the
	 *            {@link #getPingFingerprint() fingerprint}.
	 * @param failureMessage
	 *            this message will be used in the RuntimeException thrown in
	 *            {@link #ping(PingVisitor)}.
	 */
	public UnavailablePingable(final String name, final String failureMessage) {
		this.name = name;
		this.message = failureMessage;
	}

	/**
	 * {@inheritDoc}
	 */
	public PingFingerprint getPingFingerprint() {
		return new PingFingerprint(this, name);
	}

	/**
	 * {@inheritDoc}
	 */
	public PingVisitor ping(final PingVisitor visitor) {
		throw new RuntimeException(message);
	}

}
