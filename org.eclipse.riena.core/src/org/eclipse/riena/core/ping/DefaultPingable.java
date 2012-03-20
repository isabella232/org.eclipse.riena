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

/**
 * A default implementation of the {@link IPingable IPingable} interface.
 * <p>
 * Services that want to be pingable can simply sub-class this or they have to
 * implement {@code IPingable} on their own.
 */
public abstract class DefaultPingable implements IPingable {

	/**
	 * {@inheritDoc}
	 */
	public PingVisitor ping(final PingVisitor visitor) {
		return visitor.visit(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public PingFingerprint getPingFingerprint() {
		return new PingFingerprint(this);
	}

}
