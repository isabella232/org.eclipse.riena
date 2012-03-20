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
import org.eclipse.riena.core.ping.PingVisitor;
import org.eclipse.riena.example.ping.client.controllers.SonarController;

/**
 * A special {@link IPingable} used by the {@link SonarController} to indicate
 * that no pingable was found to ping.
 */
public class NoPingableFound implements IPingable {

	public PingFingerprint getPingFingerprint() {
		return new PingFingerprint(this);
	}

	public PingVisitor ping(final PingVisitor visitor) {
		throw new RuntimeException("No IPingable found to ping!"); //$NON-NLS-1$
	}

}
