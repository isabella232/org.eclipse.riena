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
 * This interface has to be implemented by all services that support testing of
 * availability (ping).
 */
public interface IPingable {

	/**
	 * Ping this pingable. The implementation by contract is to call
	 * <code>visitor.visit( this )</code>.
	 * 
	 * @param visitor
	 * @return the PingVisitor
	 */
	PingVisitor ping(PingVisitor visitor);

	/**
	 * The fingerprint is used to identify cycles.
	 * 
	 * @return this pingables fingerpring.
	 */
	PingFingerprint getPingFingerprint();
}
