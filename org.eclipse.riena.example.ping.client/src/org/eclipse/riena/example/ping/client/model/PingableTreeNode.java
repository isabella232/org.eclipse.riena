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
package org.eclipse.riena.example.ping.client.model;

import org.eclipse.riena.core.ping.IPingable;
import org.eclipse.riena.core.ping.PingFingerprint;

/**
 * This class is used for the root nodes, which represent the {@link IPingable}
 * services.
 */
public class PingableTreeNode extends PingResultTreeNode {

	private final IPingable pingable;

	/**
	 * Creates a PingableTreeNode.
	 * 
	 * @param pingable
	 *            the {@link IPingable}.
	 */
	public PingableTreeNode(final IPingable pingable) {
		super(null, getNameFor(pingable));
		this.pingable = pingable;
	}

	/**
	 * Returns the {@link IPingable} represented by this node.
	 * 
	 * @return the {@link IPingable}.
	 */
	public IPingable getPingable() {
		return pingable;
	}

	/**
	 * Returns the {@link PingFingerprint fingerprint} of the pingable if
	 * available, otherwise it returns the {@link IPingable}s String
	 * representation.
	 * 
	 * @param pingable
	 *            the {@link IPingable} for which to retrieve the name.
	 * @return the String representation for the given {@link IPingable}.
	 */
	protected static String getNameFor(final IPingable pingable) {
		try {
			return pingable.getPingFingerprint().getName();
		} catch (final Exception e) {
			return pingable.toString();
		}
	}
}
