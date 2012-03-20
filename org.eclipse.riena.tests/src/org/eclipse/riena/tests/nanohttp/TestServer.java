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
package org.eclipse.riena.tests.nanohttp;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.eclipse.riena.core.util.Nop;

/**
 * Embedded simple web server for unit tests based on NanoHTTPD.
 */
public class TestServer extends NanoHTTPD {

	private final File root;
	private final int port;

	/**
	 * @param port
	 * @param root
	 * @throws IOException
	 */
	public TestServer(final int port, final File root) throws IOException {
		super(port);
		this.root = root;
		this.port = port;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.tests.nanohttp.NanoHTTPD#serveFile(java.lang.String,
	 * java.util.Properties, java.io.File, boolean)
	 */
	@Override
	public Response serveFile(final String uri, final Properties header, final File homeDir,
			final boolean allowDirectoryListing) {
		return super.serveFile(uri, header, root, allowDirectoryListing);
	}

	@Override
	public void stop() {
		// after the NanoHTTPD has received its stop signal, we have to open any connection to actually see the thread go away
		super.stop();
		try {
			new URL("http://localhost:" + port).openStream();
		} catch (final IOException e) {
			Nop.reason("no action necessary");
		}
	}

}
