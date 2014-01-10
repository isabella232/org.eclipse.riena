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
package org.eclipse.riena.communication.core.attachment;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.riena.core.util.Nop;

/**
 *
 */
public class InputStreamDataSource implements IDataSource {

	private final InputStream inputStream;

	public InputStreamDataSource(final InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.attachment.IDataSource#getInputStream
	 * ()
	 */
	public InputStream getInputStream() throws IOException {
		try {
			inputStream.reset();
		} catch (final IOException e) {
			Nop.reason("catch and ignore"); //$NON-NLS-1$
		}
		return inputStream;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.attachment.IDataSource#getName()
	 */
	public String getName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.attachment.IDataSource#checkValid()
	 */
	public void checkValid() throws IOException {
		if (inputStream.markSupported()) {
			inputStream.reset();
		}
	}

}
