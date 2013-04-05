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
package org.eclipse.riena.internal.communication.factory.hessian;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.Permission;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.eclipse.riena.communication.core.zipsupport.ReusableBufferedInputStream;

/**
 * Simply wraps a {@code HttpURLConnection} for the only purpose to gzip the
 * input and the output streams.
 */
public class GZippingHttpURLConnectionWrapper extends HttpURLConnection {

	private final HttpURLConnection connection;
	private GZIPOutputStream myGZIPOutputStream;

	public GZippingHttpURLConnectionWrapper(final HttpURLConnection connection) {
		super(null);
		this.connection = connection;
	}

	protected GZippingHttpURLConnectionWrapper(final URL u) {
		super(null);
		this.connection = null;
		throw new UnsupportedOperationException(
				"This is not the intended usage of the GZippingHttpURLConnectionWrapper."); //$NON-NLS-1$
	}

	@Override
	public void disconnect() {
		connection.disconnect();
	}

	@Override
	public boolean usingProxy() {
		return connection.usingProxy();
	}

	@Override
	public void connect() throws IOException {
		connection.connect();
	}

	@Override
	public String getHeaderFieldKey(final int n) {
		return connection.getHeaderFieldKey(n);
	}

	@Override
	public void setFixedLengthStreamingMode(final int contentLength) {
		connection.setFixedLengthStreamingMode(contentLength);
	}

	@Override
	public void setChunkedStreamingMode(final int chunklen) {
		connection.setChunkedStreamingMode(chunklen);
	}

	@Override
	public String getHeaderField(final int n) {
		return connection.getHeaderField(n);
	}

	@Override
	public void setInstanceFollowRedirects(final boolean followRedirects) {
		connection.setInstanceFollowRedirects(followRedirects);
	}

	@Override
	public boolean getInstanceFollowRedirects() {
		return connection.getInstanceFollowRedirects();
	}

	@Override
	public void setRequestMethod(final String method) throws ProtocolException {
		connection.setRequestMethod(method);
	}

	@Override
	public String getRequestMethod() {
		return connection.getRequestMethod();
	}

	@Override
	public int getResponseCode() throws IOException {
		return connection.getResponseCode();
	}

	@Override
	public String getResponseMessage() throws IOException {
		return connection.getResponseMessage();
	}

	@Override
	public long getHeaderFieldDate(final String name, final long defaultValue) {
		return connection.getHeaderFieldDate(name, defaultValue);
	}

	@Override
	public Permission getPermission() throws IOException {
		return connection.getPermission();
	}

	@Override
	public InputStream getErrorStream() {
		return connection.getErrorStream();
	}

	@Override
	public void setConnectTimeout(final int timeout) {
		connection.setConnectTimeout(timeout);
	}

	@Override
	public int getConnectTimeout() {
		return connection.getConnectTimeout();
	}

	@Override
	public void setReadTimeout(final int timeout) {
		connection.setReadTimeout(timeout);
	}

	@Override
	public int getReadTimeout() {
		return connection.getReadTimeout();
	}

	@Override
	public URL getURL() {
		return connection.getURL();
	}

	@Override
	public int getContentLength() {
		return connection.getContentLength();
	}

	@Override
	public String getContentType() {
		return connection.getContentType();
	}

	@Override
	public String getContentEncoding() {
		return connection.getContentEncoding();
	}

	@Override
	public long getExpiration() {
		return connection.getExpiration();
	}

	@Override
	public long getDate() {
		return connection.getDate();
	}

	@Override
	public long getLastModified() {
		return connection.getLastModified();
	}

	@Override
	public String getHeaderField(final String name) {
		return connection.getHeaderField(name);
	}

	@Override
	public Map<String, List<String>> getHeaderFields() {
		return connection.getHeaderFields();
	}

	@Override
	public int getHeaderFieldInt(final String name, final int defaultValue) {
		return connection.getHeaderFieldInt(name, defaultValue);
	}

	@Override
	public Object getContent() throws IOException {
		return connection.getContent();
	}

	@Override
	public Object getContent(final Class[] classes) throws IOException {
		return connection.getContent(classes);
	}

	@Override
	public InputStream getInputStream() throws IOException {
		final InputStream zipTestInputStream = new ReusableBufferedInputStream(connection.getInputStream());
		if (zipTestInputStream.markSupported()) {
			zipTestInputStream.mark(20);
			final int readMAGIC = zipTestInputStream.read() + zipTestInputStream.read() * 256;
			zipTestInputStream.reset();
			if (readMAGIC == GZIPInputStream.GZIP_MAGIC) {
				return new GZIPInputStream(zipTestInputStream);
			}
		}
		return zipTestInputStream;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		myGZIPOutputStream = new GZIPOutputStream(connection.getOutputStream());
		return myGZIPOutputStream;
	}

	public GZIPOutputStream getUsedGZIPOutputStream() {
		return myGZIPOutputStream;
	}

	@Override
	public String toString() {
		return connection.toString();
	}

	@Override
	public void setDoInput(final boolean doinput) {
		connection.setDoInput(doinput);
	}

	@Override
	public boolean getDoInput() {
		return connection.getDoInput();
	}

	@Override
	public void setDoOutput(final boolean dooutput) {
		connection.setDoOutput(dooutput);
	}

	@Override
	public boolean getDoOutput() {
		return connection.getDoOutput();
	}

	@Override
	public void setAllowUserInteraction(final boolean allowuserinteraction) {
		connection.setAllowUserInteraction(allowuserinteraction);
	}

	@Override
	public boolean getAllowUserInteraction() {
		return connection.getAllowUserInteraction();
	}

	@Override
	public void setUseCaches(final boolean usecaches) {
		connection.setUseCaches(usecaches);
	}

	@Override
	public boolean getUseCaches() {
		return connection.getUseCaches();
	}

	@Override
	public void setIfModifiedSince(final long ifmodifiedsince) {
		connection.setIfModifiedSince(ifmodifiedsince);
	}

	@Override
	public long getIfModifiedSince() {
		return connection.getIfModifiedSince();
	}

	@Override
	public boolean getDefaultUseCaches() {
		return connection.getDefaultUseCaches();
	}

	@Override
	public void setDefaultUseCaches(final boolean defaultusecaches) {
		connection.setDefaultUseCaches(defaultusecaches);
	}

	@Override
	public void setRequestProperty(final String key, final String value) {
		connection.setRequestProperty(key, value);
	}

	@Override
	public void addRequestProperty(final String key, final String value) {
		connection.addRequestProperty(key, value);
	}

	@Override
	public String getRequestProperty(final String key) {
		return connection.getRequestProperty(key);
	}

	@Override
	public Map<String, List<String>> getRequestProperties() {
		return connection.getRequestProperties();
	}

	@Override
	public int hashCode() {
		return connection.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		return connection.equals(obj);
	}

}
