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
package org.eclipse.riena.monitor.common;

import java.util.List;

/**
 * The receiver of collectibles.
 */
public interface IReceiver {

	/**
	 * Receive collectibles.
	 * 
	 * @param senderTime
	 *            the current client time (ms) these collectibles have been sent
	 * @param collectibles
	 *            the list of collectibles
	 * @return true on success; otherwise false (may result in retrying with
	 *         these collectibles at a later time)
	 */
	boolean take(long senderTime, List<Collectible<?>> collectibles);

}
