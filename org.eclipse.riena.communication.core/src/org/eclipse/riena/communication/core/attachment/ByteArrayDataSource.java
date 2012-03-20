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
package org.eclipse.riena.communication.core.attachment;

import java.io.IOException;
import java.io.InputStream;

/**
 * internal class used for serializing attachments. this class is only used for
 * hessian webservice types.
 */
public class ByteArrayDataSource implements IDataSource {

	private String name;
	private InputStream input;

	/**
	 * Default constructor. <br>
	 * called by deserialization
	 */
	public ByteArrayDataSource() {
		super();
	}

	/**
	 * copies the source into an byte array
	 * 
	 * @param source
	 */
	public ByteArrayDataSource(final IDataSource source) {
		super();
		if (source != null) {
			name = source.getName();
			try {
				input = source.getInputStream();
			} catch (final IOException e) {
				throw new RuntimeException("IOException when transporting attachment ", e); //$NON-NLS-1$
			}
		}
	}

	/**
	 * @see javax.IDataSource.DataSource#getInputStream()
	 */
	public InputStream getInputStream() throws IOException {
		if (input == null) {
			return null;
		}
		if (input.markSupported()) {
			input.reset();
		}
		return input;
	}

	/**
	 * @see javax.IDataSource.DataSource#getName()
	 */
	public String getName() {
		return name;
	}

	public void checkValid() {
		return;
	}
}
