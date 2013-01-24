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
package org.eclipse.riena.communication.core.attachment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.Assert;

/**
 *
 */
public class FileDataSource implements IDataSource {

	private final File file;

	public FileDataSource(final File file) {
		Assert.isNotNull(file, "file must not be null"); //$NON-NLS-1$
		this.file = file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.attachment.DataSource#getInputStream
	 * ()
	 */
	public InputStream getInputStream() throws IOException {
		final FileInputStream inputStream = new FileInputStream(file);
		return inputStream;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.communication.core.attachment.DataSource#getName()
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
		if (file.exists()) {
			return;
		} else {
			throw new FileNotFoundException(file.getAbsolutePath());
		}
	}
}
