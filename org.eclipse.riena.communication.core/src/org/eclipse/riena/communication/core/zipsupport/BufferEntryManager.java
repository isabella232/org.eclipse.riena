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
package org.eclipse.riena.communication.core.zipsupport;

import java.util.LinkedList;

class BufferEntryManager {

	private static LinkedList<BufferEntry> cachedBuffers = new LinkedList<BufferEntry>();

	static synchronized BufferEntry getBuffer() {
		if (cachedBuffers.size() == 0) {
			return new BufferEntry();
		} else {
			return cachedBuffers.removeLast();
		}
	}

	synchronized static void putBuffer(final BufferEntry entry) {
		cachedBuffers.addLast(entry);
	}

}