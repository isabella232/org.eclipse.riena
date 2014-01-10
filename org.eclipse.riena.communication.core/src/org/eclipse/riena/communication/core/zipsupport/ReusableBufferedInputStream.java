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
package org.eclipse.riena.communication.core.zipsupport;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @since 3.0
 */
public class ReusableBufferedInputStream extends BufferedInputStream {

	private BufferEntry entry = null;

	public ReusableBufferedInputStream(final InputStream in) {
		super(in, 1);
		entry = BufferEntryManager.getBuffer();
		this.buf = entry.buffer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.BufferedInputStream#close()
	 */
	@Override
	public void close() throws IOException {
		this.buf = null; // take buffer away before stream is closed
		super.close();
		BufferEntryManager.putBuffer(entry); // return buffer to manager
		entry = null;
	}

}
