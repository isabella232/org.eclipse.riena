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
package org.eclipse.riena.internal.sample.app.server;

import java.io.IOException;
import java.io.InputStream;

/**
 * nomen est omen
 */
public class SampleInputStream extends InputStream {

	private int offset = 0;
	private final int start = 0;

	/**
	 *
	 */
	public SampleInputStream() {
		super();
	}

	@Override
	public int read() throws IOException {
		if (offset++ < 100) {
			return 'x';
		} else {
			throw new IOException("could not read past 100 chars (by design)"); //$NON-NLS-1$
		}
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public synchronized void reset() throws IOException {
		offset = start;
	}

}
