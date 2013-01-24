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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The class containing the result of a ping.
 */
public class PingResult {

	private final String pingableName;
	private String failureMessage;
	private List<PingResult> nestedResults;

	/**
	 * Create the ping result.
	 * 
	 * @param pingableName
	 */
	public PingResult(final String pingableName) {
		this.pingableName = pingableName;
	}

	/**
	 * Return the name of the visited {@link IPingable IPingable}.
	 * 
	 * @return the name of the visited {@link IPingable IPingable}.
	 */
	public String getPingableName() {
		return pingableName;
	}

	public void addNestedResult(final PingResult nested) {
		if (nestedResults == null) {
			nestedResults = new ArrayList<PingResult>();
		}
		nestedResults.add(nested);
	}

	public Iterable<PingResult> getNestedResults() {
		if (nestedResults == null) {
			return Collections.emptyList();
		}
		return nestedResults;
	}

	/**
	 * Extracts the StackTrace from the given exception and sets it as the
	 * {@link #setPingFailure(String) failure message}.
	 * 
	 * @param exception
	 */
	public void setPingFailure(final Exception exception) {
		setPingFailure(toString(exception));
	}

	/**
	 * Sets the message describing the failure that occurred on ping.
	 * 
	 * @param message
	 */
	public void setPingFailure(final String message) {
		this.failureMessage = message;
	}

	/**
	 * @return the message describing the failure that occurred on ping, or
	 *         <code>null</code> on success.
	 */
	public String getPingFailure() {
		return failureMessage;
	}

	/**
	 * @return <code>true</code>, if the ping failed (means: if there is a
	 *         {@link #getPingFailure() failure message).
	 */
	public boolean hasPingFailed() {
		return getPingFailure() != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder bob = new StringBuilder("PingResult["); //$NON-NLS-1$
		bob.append("name="); //$NON-NLS-1$
		bob.append(getPingableName());
		bob.append(", failure="); //$NON-NLS-1$
		bob.append(getPingFailure());
		bob.append(", nested="); //$NON-NLS-1$
		bob.append(getNestedResults());
		bob.append("]"); //$NON-NLS-1$
		return bob.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return getPingableName().hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PingResult other = (PingResult) obj;
		if (pingableName == null) {
			if (other.pingableName != null) {
				return false;
			}
		} else if (!pingableName.equals(other.pingableName)) {
			return false;
		}
		if (failureMessage == null) {
			if (other.failureMessage != null) {
				return false;
			}
		} else if (!failureMessage.equals(other.failureMessage)) {
			return false;
		}
		if (nestedResults == null) {
			if (other.nestedResults != null) {
				return false;
			}
		} else if (!nestedResults.equals(other.nestedResults)) {
			return false;
		}
		return true;
	}

	/**
	 * Extracts the StackTrace from the Exception.
	 * 
	 * @param throwable
	 * @return the StackTrace of the Exception.
	 */
	private String toString(final Throwable throwable) {
		if (throwable == null) {
			return null;
		}
		final StringWriter stringWriter = new StringWriter();
		throwable.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}
}
