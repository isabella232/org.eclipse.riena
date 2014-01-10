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
package org.eclipse.riena.internal.sample.app.server;

import java.util.Date;
import java.util.List;

import org.eclipse.riena.monitor.common.Collectible;
import org.eclipse.riena.monitor.common.IReceiver;

/**
 *
 */
public class SimpleMonitoringReceiver implements IReceiver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.common.ICollectibleReceiver#take(long,
	 * java.util.List)
	 */
	public boolean take(final long senderTime, final List<Collectible<?>> collectibles) {
		System.out.println("Received " + collectibles.size() + " collectibles at " + new Date(senderTime) //$NON-NLS-1$ //$NON-NLS-2$
				+ " sender time."); //$NON-NLS-1$
		for (final Collectible<?> received : collectibles) {
			System.out.println(" - " + received); //$NON-NLS-1$
		}
		return true;
	}
}
