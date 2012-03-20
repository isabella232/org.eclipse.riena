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

package org.eclipse.riena.internal.communication.sample.pingpong.server;

import org.eclipse.riena.communication.sample.pingpong.common.IPingPong;
import org.eclipse.riena.communication.sample.pingpong.common.Ping;
import org.eclipse.riena.communication.sample.pingpong.common.Pong;

/**
 * Simple PingPong sample implementation
 */
public class PingPong implements IPingPong {

	/**
	 * Writes the given ping into the console and answers a pong.
	 * 
	 * @see org.eclipse.riena.communication.sample.pingpong.common.IPingPong#ping(org.eclipse.riena.communication.sample.pingpong.common.Ping)
	 */
	public Pong ping(final Ping ping) {

		System.out.println("PingPong::Server:: " + ping); //$NON-NLS-1$

		final Pong pong = new Pong();
		pong.setText("Thx. I got the ping!"); //$NON-NLS-1$

		return pong;
	}

}
