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
package org.eclipse.riena.internal.sample.app.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.communication.core.attachment.Attachment;
import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.sample.app.common.attachment.IAttachmentService;

/**
 * Example service to show the feature of sending attachments in Webservice
 * calls.
 */
public final class AttachmentService implements IAttachmentService {

	private final static int ATTACHMENT_START = 60;
	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), AttachmentService.class);
	private static final String TESTDATA1 = "das sind testdaten, die wir mal einfach so verschicken um etwas zu testen."; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 */
	public String sendSingleAttachment(final Attachment singleAttachment) {
		return readAttachmentStart(singleAttachment);
	}

	private String readAttachmentStart(final Attachment attachment) {
		final byte[] byteArray = new byte[ATTACHMENT_START];
		try {
			final InputStream input = attachment.readAsStream();
			final int nbrBytes = input.read(byteArray, 0, byteArray.length);
			if (nbrBytes < 1) {
				throw new IOException("Empty Attachment."); //$NON-NLS-1$
			}
		} catch (final IOException e) {
			return "[can't read " + attachment + "]"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		return new String(byteArray);

	}

	/**
	 * {@inheritDoc}
	 */
	public String sendTwoAttachments(final Attachment firstAttachment, final Attachment secondAttachment) {
		return readAttachmentStart(firstAttachment) + "+" + readAttachmentStart(secondAttachment); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	public String sendAttachmentsAndData(final String firstString, final Attachment firstAttachment,
			final String secondString, final Attachment secondAttachment, final String thirdString, final int number) {
		return firstString + "+" + readAttachmentStart(firstAttachment) + "+" + secondString + "+" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ readAttachmentStart(secondAttachment) + "+" + thirdString + "+" + number; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * {@inheritDoc}
	 */
	public Attachment returnAttachment() throws IOException {
		return new Attachment(setupTestFile(TESTDATA1));
	}

	/**
	 * {@inheritDoc}
	 */
	public Attachment returnAttachmentForRequest(final String attachmentName) throws IOException {
		if (attachmentName.equals("validfilename")) { //$NON-NLS-1$
			return new Attachment(setupTestFile(TESTDATA1));
		} else {
			if (attachmentName.equals("invalidfilename")) { //$NON-NLS-1$
				throw new FileNotFoundException(attachmentName + " not found"); //$NON-NLS-1$
			}
		}
		throw new RuntimeException("invalid request"); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	public Attachment sendAndReturnAttachment(final Attachment attachment) {
		LOGGER.log(LogService.LOG_DEBUG, readAttachmentStart(attachment));
		return attachment;
	}

	public InputStream getFile() {
		try {
			return new BufferedInputStream(new FileInputStream(setupTestFile(TESTDATA1)));
		} catch (final IOException e) {
			return null;
		}
	}

	public Attachment getBytesFromSampleWithError() throws IOException {
		return new Attachment(new SampleInputStream());
	}

	public Attachment getBytesFromUrlWithErrorAtStart() {
		try {
			final URL url = new URL("http://localhost:20080/"); //$NON-NLS-1$
			final HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			if (httpConn.getResponseCode() > 399) {
				final String errorMsg = "no error msg"; //$NON-NLS-1$
				if (httpConn.getErrorStream().available() > 0) {
					final byte[] buffer = new byte[httpConn.getErrorStream().available()];
					httpConn.getErrorStream().read(buffer, 0, buffer.length);
				}
				throw new RuntimeException("rc=" + httpConn.getResponseCode() + " msg=" + errorMsg); //$NON-NLS-1$ //$NON-NLS-2$
			}
			return new Attachment(url);
		} catch (final IOException e) {
			Nop.reason("do nothing"); //$NON-NLS-1$
		}
		return null;
	}

	public Attachment getBytesFromUrlWithError() {
		try {
			return new Attachment(new URL("http://localhost:8080/junittest/errorinthemiddle")); //$NON-NLS-1$
		} catch (final MalformedURLException e) {
			Nop.reason("do nothing"); //$NON-NLS-1$
		} catch (final IOException e) {
			Nop.reason("do nothing"); //$NON-NLS-1$
		}
		return null;
	}

	public Attachment getBytesFromUrlWithoutError() {
		try {
			final URL url = new URL("http://localhost:8080/junittest/validoutput"); //$NON-NLS-1$
			return new Attachment(url);
		} catch (final MalformedURLException e) {
			Nop.reason("do nothing"); //$NON-NLS-1$
		} catch (final IOException e) {
			Nop.reason("do nothing"); //$NON-NLS-1$
		}
		return null;
	}

	public Attachment getAttachmentForSize(final int sizeInBytes) throws IOException {
		final int countInBytes = sizeInBytes;

		return new Attachment(new InputStream() {

			private int count = countInBytes;
			private final SecureRandom random = new SecureRandom();

			@Override
			public int read() throws IOException {
				count--;
				if (count >= 0) {
					return random.nextInt();
				} else {
					return -1;
				}
			}

		});
	}

	/**
	 * {@inheritDoc}
	 */
	public Attachment getBytesFromInvalidUrl() throws IOException {
		try {
			// intentionally a invalid port
			return new Attachment(new URL("http://localhost:24444/unittest/errorinthemiddle")); //$NON-NLS-1$
		} catch (final MalformedURLException e) {
			Nop.reason("do nothing"); //$NON-NLS-1$
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendFile(final Attachment attachment) {
		return;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getAttachmentAsObject() throws IOException {
		try {
			return new Attachment(setupTestFile(TESTDATA1));
		} catch (final FileNotFoundException e) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Attachment getEmptyAttachment() throws IOException {
		return new Attachment(new InputStream() {

			@Override
			public int read() throws IOException {
				return -1;
			}
		});

	}

	public int sendAttachmentAndReturnSize(final Attachment attachment) throws IOException {
		final InputStream readAsStream = attachment.readAsStream();
		int i = 0;
		while (readAsStream.read() != -1) {
			i++;
		}
		readAsStream.close();
		return i;
	}

	public Attachment returnAttachmentForSize(final int size) throws IOException {
		return generateLargeAttachment(size);
	}

	private File setupTestFile(final String string) {
		File file;
		try {
			file = File.createTempFile("attachTest", null); //$NON-NLS-1$
			final PrintWriter printWriter = new PrintWriter(new FileOutputStream(file));
			printWriter.write(string);
			printWriter.close();
			return file;
		} catch (final IOException e) {
			throw new RuntimeException("IOException while setting up test file", e); //$NON-NLS-1$
		}
	}

	private Attachment generateLargeAttachment(final int countInBytes) throws IOException {
		return new Attachment(new InputStream() {

			private int count = countInBytes;
			private final SecureRandom random = new SecureRandom();

			@Override
			public int read() throws IOException {
				count--;
				if (count >= 0) {
					return random.nextInt();
				} else {
					return -1;
				}
			}
		});
	}

}