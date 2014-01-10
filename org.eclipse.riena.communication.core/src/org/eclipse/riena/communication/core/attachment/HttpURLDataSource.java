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
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.core.runtime.Assert;

/**
 * HttpURLDataSource that is used if URLs are read into an attachment since the
 * implementation from JAF is broken in the sense that it re-issues HTTP-calls
 * for getContent() and getInputStream()
 */
public class HttpURLDataSource implements IDataSource {

	private final HttpURLConnection httpUrlConnection;

	/**
	 * Creates a HttpUrlConnectionDataSource.
	 * 
	 * @param URL
	 *            is the URL of the underlying source that we are wrapping as
	 *            DataSource
	 * 
	 * @pre httpURLConnection != null
	 */
	public HttpURLDataSource(final URL url) throws IOException {
		final HttpURLConnection myHttpUrlConnection = (HttpURLConnection) url.openConnection();
		Assert.isNotNull(myHttpUrlConnection);
		this.httpUrlConnection = myHttpUrlConnection;
	}

	/**
	 * @see javax.IDataSource.DataSource#getContentType()
	 */
	public String getContentType() {
		return httpUrlConnection.getContentType();
	}

	/**
	 * @see javax.IDataSource.DataSource#getInputStream()
	 */
	public InputStream getInputStream() throws IOException {
		final InputStream input = httpUrlConnection.getInputStream();
		if (input.markSupported()) {
			input.reset();
		}
		return input;
	}

	/**
	 * @see javax.IDataSource.DataSource#getName()
	 */
	public String getName() {
		return httpUrlConnection.getURL().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.attachment.IDataSource#checkValid()
	 */
	public void checkValid() throws IOException {
		final InputStream inputStream = httpUrlConnection.getInputStream();
		if (inputStream != null) {
			if (inputStream.markSupported()) {
				inputStream.reset();
			}
		} else {
			throw new RuntimeException("no inputstream for http url " + httpUrlConnection.getURL()); //$NON-NLS-1$
		}
	}
}
