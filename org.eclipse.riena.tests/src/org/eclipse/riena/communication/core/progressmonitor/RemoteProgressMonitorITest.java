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
package org.eclipse.riena.communication.core.progressmonitor;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;

import org.osgi.framework.BundleContext;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.attachment.Attachment;
import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.ManualTestCase;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.sample.app.common.attachment.IAttachmentService;

/**
 * This Testclass sends large byte streams to the Attachment Service and other
 * services and installs a progress monitor and watches the behavior.
 * 
 */
@ManualTestCase
public final class RemoteProgressMonitorITest extends RienaTestCase {

	private IAttachmentService attachService;
	private IRemoteServiceRegistration regAttachmentService;
	private IRemoteProgressMonitorRegistry registry;

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
		final BundleContext context = Activator.getDefault().getContext();
		registry = (IRemoteProgressMonitorRegistry) context.getService(context
				.getServiceReference(IRemoteProgressMonitorRegistry.class.getName()));

	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		regAttachmentService.unregister();
		attachService = null;
	}

	/**
	 * Nomen est Omen
	 * 
	 * @throws Exception
	 */
	public void testSendSimpleAttachmentProgress() throws Exception {
		final TestProgressMonitor monitor = new TestProgressMonitor();
		registry.addProgressMonitor(attachService, monitor, IRemoteProgressMonitorRegistry.RemovalPolicy.AFTER_ONE_CALL);

		final Attachment attachment = generateLargeAttachment(15000);
		final int i = attachService.sendAttachmentAndReturnSize(attachment);
		assertTrue(i == 15000);
		monitor.validate(15000 / 512 + 1, 1);
		registry.removeAllProgressMonitors(attachService);
	}

	public void testSendLargeAttachmentProgress() throws Exception {
		final TestProgressMonitor monitor = new TestProgressMonitor();
		registry.addProgressMonitor(attachService, monitor, IRemoteProgressMonitorRegistry.RemovalPolicy.AFTER_ONE_CALL);

		final Attachment attachment = generateLargeAttachment(15000000);
		final int i = attachService.sendAttachmentAndReturnSize(attachment);
		assertTrue(i == 15000000);
		monitor.validate(15000000 / 512 + 1, 1);
		registry.removeAllProgressMonitors(attachService);
	}

	public void testReceiveSimpleAttachmentProgress() throws Exception {
		final TestProgressMonitor monitor = new TestProgressMonitor();
		registry.addProgressMonitor(attachService, monitor, IRemoteProgressMonitorRegistry.RemovalPolicy.AFTER_ONE_CALL);

		final Attachment attachment = attachService.returnAttachmentForSize(15000);
		final int i = getSize(attachment);
		assertTrue(i == 15000);
		monitor.validate(1, i / 512 + 1);
		registry.removeAllProgressMonitors(attachService);
	}

	public void testReceiveLargeAttachmentProgress() throws Exception {
		final TestProgressMonitor monitor = new TestProgressMonitor();
		registry.addProgressMonitor(attachService, monitor, IRemoteProgressMonitorRegistry.RemovalPolicy.AFTER_ONE_CALL);

		final Attachment attachment = attachService.returnAttachmentForSize(15000000);
		final int i = getSize(attachment);
		assertTrue(i == 15000000);
		monitor.validate(1, i / 512 + 1);
		registry.removeAllProgressMonitors(attachService);
	}

	private int getSize(final Attachment attachment) throws IOException {
		final InputStream input = attachment.readAsStream();
		int count = 0;
		while (input.read() != -1) {
			count++;
		}
		return count;
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

	/**
	 *
	 */
	private static final class TestProgressMonitor implements IRemoteProgressMonitor {

		private boolean start = false;
		private boolean end = false;
		private int requestCount = 0;
		private int responseCount = 0;
		private int lastResponseLength = -1;
		private boolean requestDivideable = true;
		private boolean responseDividable = true;

		public void validate(final int requests, final int responses) {
			assertTrue("start method not called", start); //$NON-NLS-1$
			assertTrue("end method not called", end); //$NON-NLS-1$
			// request count and response count can be larger with with larger size, the protocol overhead gets more (which is included in the count)
			if (requests != -1) {
				assertTrue("expected " + requests + " but was " + requestCount + " for number of requestEvents", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						requests <= requestCount);
			}
			if (responses != -1) {
				assertTrue("expected " + responses + " but was " + responseCount + " for number of responseEvents", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						responses <= responseCount);
			}
		}

		public void request(final RemoteProgressMonitorEvent event) {
			System.out.println(event.getBytesProcessed());
			if (event.getBytesProcessed() % 512 != 0) {
				if (requestDivideable) {
					requestDivideable = false;
				} else {
					assertTrue(
							"expected bytesProcess in request to be always dividable by 512 (expect in the last call) :" //$NON-NLS-1$
									+ event.getBytesProcessed(), event.getBytesProcessed() % 512 == 0);
				}
			}

			requestCount++;
		}

		public void response(final RemoteProgressMonitorEvent event) {
			System.out.println(event.getBytesProcessed());
			if (event.getBytesProcessed() % 512 != 0) {
				if (responseDividable) {
					responseDividable = false;
				} else {
					assertTrue(
							"expected bytesProcess in response to be always dividable by 512 (expect in the last call) :" //$NON-NLS-1$
									+ event.getBytesProcessed(), event.getBytesProcessed() % 512 == 0);
				}
			}
			responseCount++;
			lastResponseLength = event.getBytesProcessed();
		}

		public void start() {
			assertFalse(start);
			System.out.println("start"); //$NON-NLS-1$
			start = true;
		}

		public void end() {
			assertFalse(end);
			System.out.println("end"); //$NON-NLS-1$
			end = true;
		}
	}
}