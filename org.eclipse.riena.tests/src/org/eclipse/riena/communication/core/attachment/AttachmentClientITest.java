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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.SecureRandom;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.RemoteFailure;
import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.ManualTestCase;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.sample.app.common.attachment.IAttachmentService;

/**
 * Integration test for testing attachment together with the AttachmentService.
 * 
 */
@ManualTestCase
public final class AttachmentClientITest extends RienaTestCase {

	private static final String STRING1 = "das sind testdaten, die wir mal einfach so verschicken um et+das sind " //$NON-NLS-1$
			+ "testdaten, die wir mal einfach so verschicken um et"; //$NON-NLS-1$
	private static final String STRING2 = "first+das sind testdaten, die wir mal einfach so verschicken um et+second+" //$NON-NLS-1$
			+ "das sind testdaten, die wir mal einfach so verschicken um et+third+2"; //$NON-NLS-1$
	private static final String TESTDATA1 = "das sind testdaten, die wir mal einfach so verschicken um etwas zu testen."; //$NON-NLS-1$
	private static final String TESTDATA2 = "das sind testdaten, die wir mal einfach so verschicken um etwas zu testen. (2.test)"; //$NON-NLS-1$
	private IAttachmentService attachService;
	private IRemoteServiceRegistration regAttachmentService;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
		regAttachmentService = Register.remoteProxy(IAttachmentService.class)
				.usingUrl("http://localhost:8080/hessian/AttachmentService").withProtocol("hessian") //$NON-NLS-1$ //$NON-NLS-2$
				.andStart(Activator.getDefault().getContext());
		attachService = (IAttachmentService) Activator
				.getDefault()
				.getContext()
				.getService(Activator.getDefault().getContext().getServiceReference(IAttachmentService.class.getName()));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	public void tearDown() throws Exception {
		regAttachmentService.unregister();
		attachService = null;
		super.tearDown();
	}

	/**
	 * test for sending a simple single file using attachments and webservices
	 * 
	 * @throws FileNotFoundException
	 * 
	 */
	public void testSendSimpleFile() throws IOException {
		final Attachment attachment = new Attachment(setupTestFile(TESTDATA1));
		final String str = attachService.sendSingleAttachment(attachment);
		assertTrue("expecting a certain string", //$NON-NLS-1$
				str != null && str.equals("das sind testdaten, die wir mal einfach so verschicken um et")); //$NON-NLS-1$
		trace("testSendSimpleFile " + str); //$NON-NLS-1$
	}

	/**
	 * test for sending two files
	 * 
	 * @throws FileNotFoundException
	 */
	public void testSendTwoFiles() throws IOException {
		final Attachment attachment = new Attachment(setupTestFile(TESTDATA1));
		final Attachment attachment2 = new Attachment(setupTestFile(TESTDATA2));
		final String str = attachService.sendTwoAttachments(attachment, attachment2);
		assertTrue("expecting a certain string", str != null && str.equals(STRING1)); //$NON-NLS-1$
		trace("testSendTwoFiles " + str); //$NON-NLS-1$
	}

	/**
	 * test for sending multiple files plus regular java objects in one call
	 * 
	 * @throws FileNotFoundException
	 */
	public void testSendFileAndData() throws IOException {
		final Attachment attachment = new Attachment(setupTestFile(TESTDATA1));
		final Attachment attachment2 = new Attachment(setupTestFile(TESTDATA2));
		final String str = attachService.sendAttachmentsAndData("first", attachment, "second", attachment2, "third", 2); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertTrue("expecting a certain string", str != null && str.equals(STRING2)); //$NON-NLS-1$
		trace("testSendFileAndData " + str); //$NON-NLS-1$
	}

	/**
	 * @throws IOException
	 */
	public void testReturn() throws IOException {
		final Attachment attachment = attachService.returnAttachment();
		trace("testReturn as String " + readAttachmentStart(attachment)); //$NON-NLS-1$
	}

	/**
	 * @throws IOException
	 */
	public void testReturnOnRequest() throws IOException {
		final Attachment attachment = attachService.returnAttachmentForRequest("validfilename"); //$NON-NLS-1$
		trace("testReturn as String " + readAttachmentStart(attachment)); //$NON-NLS-1$
	}

	/**
	 * @throws FileNotFoundException
	 */
	public void testReturnOnRequestInvalidFile() throws IOException {
		try {
			attachService.returnAttachmentForRequest("invalidfilename"); //$NON-NLS-1$
			fail("the requested file does not exist and the webservice should throw an exception"); //$NON-NLS-1$
		} catch (final FileNotFoundException e) {
			ok(e.getMessage());
		}
	}

	/**
	 * @throws FileNotFoundException
	 */
	public void testSendAndReturn() throws IOException {
		final Attachment attachment = new Attachment(setupTestFile(TESTDATA1));
		final Attachment attachment2 = attachService.sendAndReturnAttachment(attachment);
		trace("testReturn as String " + readAttachmentStart(attachment2)); //$NON-NLS-1$
		assertEquals("The webservice echos the request, how the result is not the same as it was sent in the request.", //$NON-NLS-1$
				readAttachmentStart(attachment), readAttachmentStart(attachment2));
	}

	/**
	 * @throws Exception
	 */
	public void testReadFileAsInputStream() throws Exception {
		final InputStream input = attachService.getFile();
		final byte[] b = new byte[50];
		int rc = input.read(b);
		while (rc != -1) {
			final String s = new String(b, 0, rc);
			trace(s);
			rc = input.read(b);
		}
	}

	/**
	 * @throws Exception
	 */
	public void testReadInputWithError() throws Exception {
		try {
			final Attachment attachment = attachService.getBytesFromSampleWithError();
			attachment.readAsStream();
			fail();
		} catch (final RemoteFailure e) {
			ok();
		}
	}

	/**
	 * @throws Exception
	 */
	public void testReadUrlWithErrorAtStart() throws Exception {
		try {
			final Attachment attachment = attachService.getBytesFromUrlWithErrorAtStart();
			final String output = readAttachmentStart(attachment);
			trace(output);
			fail();
		} catch (final RuntimeException e) {
			ok();
		}
	}

	/**
	 * @throws Exception
	 */
	public void testReadUrlWithError() throws Exception {
		final Attachment attachment = attachService.getBytesFromUrlWithError();
		final String output = readAttachmentStart(attachment);
		trace(output);
	}

	/**
	 * @throws Exception
	 */
	public void testReadUrlWithoutError() throws Exception {
		final Attachment attachment = attachService.getBytesFromUrlWithoutError();
		final String output = readAttachmentStart(attachment);
		trace(output);
	}

	private final static int ATTACHMENT_START = 60;

	private String readAttachmentStart(final Attachment attachment) {
		final byte[] byteArray = new byte[ATTACHMENT_START];
		try {
			final InputStream input = attachment.readAsStream();
			final int nbrBytes = input.read(byteArray, 0, byteArray.length);
			if (nbrBytes < 1) {
				throw new IOException("Empty Attachment."); //$NON-NLS-1$
			}
			return new String(byteArray, 0, nbrBytes);
		} catch (final IOException e) {
			return "[can't read " + attachment + "]"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * nomen est omen
	 * 
	 * @throws Exception
	 */
	public void testReadAttachmentCreatedWithInvalidUrl() throws Exception {
		try {
			final Attachment attachment = attachService.getBytesFromInvalidUrl();
			trace(attachment);
			fail();
		} catch (final IOException e) {
			ok();
		}
	}

	/**
	 * @throws IOException
	 */
	public void testSendAndDeleteFile() throws IOException {
		// TODO warning suppression. Ignoring FindBugs problem about
		// hard coded reference to an absolute pathname. Appears to
		// be ok for testing.
		final File file = new File("/testattachments.txt"); //$NON-NLS-1$
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			for (int i = 0; i < 200; i++) {
				out.write(i);
			}
		} finally {
			out.close();
		}

		// attachService.sendFile(new Attachment(file));
		final boolean deleted = file.delete();
		assertTrue(deleted);
		assertFalse("file must be deleted by now", file.exists()); //$NON-NLS-1$

	}

	public void testRetrieveAttachmentAsObject() throws IOException {
		final Object a = attachService.getAttachmentAsObject();
		assertTrue(a instanceof Attachment);
	}

	public void testEmptyAttachment() throws Exception {
		final Attachment attachment = attachService.getEmptyAttachment();
		final InputStream input = attachment.readAsStream();
		assertTrue(input != null);
		assertTrue(input.read() == -1);
	}

	/**
	 * Nomen est Omen
	 * 
	 * @throws Exception
	 */
	public void testSendLargeAttachments() throws Exception {
		// attachService =
		// ServiceAccessor.fetchService(PROXIEDATTACHMENTSERVICE,
		// IAttachmentService.class);
		System.out.println("generating 15 Mio bytes attachment"); //$NON-NLS-1$
		final Attachment attachment = generateLargeAttachment(15000000);
		System.out.println("sending it"); //$NON-NLS-1$
		final int i = attachService.sendAttachmentAndReturnSize(attachment);
		System.out.println("done"); //$NON-NLS-1$
		assertTrue(i == 15000000);
	}

	public void testSendFileAndTestIfItIsClosed() throws Exception {
		final File file = File.createTempFile("attachTest", null); //$NON-NLS-1$
		final PrintWriter printWriter = new PrintWriter(new FileOutputStream(file));
		printWriter.write("This text file is accessed in AttachmentTest to test the Attachment class."); //$NON-NLS-1$
		printWriter.close();
		final Attachment attach = new Attachment(file);
		assertTrue(file.exists());
		// the object gets serialized
		final AttachmentSerialized attach2 = (AttachmentSerialized) attach.writeReplace();
		final ByteArrayDataSource byteArray = (ByteArrayDataSource) ReflectionUtils.getHidden(attach2,
				"internalDataSource"); //$NON-NLS-1$
		final InputStream inputStream = byteArray.getInputStream();
		while (inputStream.read() != -1) {
			Nop.reason("nothing to do"); //$NON-NLS-1$
		}
		inputStream.close();
		assertTrue(file.exists());
		assertTrue(file.delete());
		assertFalse(file.exists());

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

	private File setupTestFile(final String string) {
		File file;
		try {
			file = File.createTempFile("attachTest", null); //$NON-NLS-1$
			final PrintWriter printWriter = new PrintWriter(new FileOutputStream(file));
			printWriter.write(string);
			printWriter.close();
			return file;
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void trace(final Object object) {
		System.out.println(object.toString());
	}

}