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
package org.eclipse.riena.ui.swt.synchronizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import org.eclipse.riena.core.exception.MurphysLawFailure;
import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.swt.uiprocess.SwtUISynchronizer;

/**
 * {@link TestCase} for {@link SwtUISynchronizer}
 */
@UITestCase
public class SwtUISynchronizerTest extends RienaTestCase {

	private AtomicBoolean provideDisplay;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		provideDisplay = new AtomicBoolean(false);

	}

	private static class MockDisplay extends Display {

		private int syncExecCalls = 0;
		private int asyncExecCalls = 0;

		@Override
		public void syncExec(final Runnable runnable) {
			try {
				runnable.run();
			} finally {
				syncExecCalls++;
			}
		}

		@Override
		public void asyncExec(final Runnable runnable) {
			new Thread(runnable).start();
			asyncExecCalls++;
		}

		@Override
		protected void checkSubclass() {
		}

		@Override
		protected void create(final DeviceData data) {
		}

		@Override
		protected void checkDevice() {
		}

		@Override
		public boolean isDisposed() {
			return false;
		}

	}

	private static class DummyJob implements Runnable {
		private final AtomicBoolean done = new AtomicBoolean(false);
		private long sleepTime = 0;
		private final CountDownLatch latch = new CountDownLatch(1);

		public void run() {
			try {
				Thread.sleep(sleepTime);
			} catch (final InterruptedException e) {
				throw new MurphysLawFailure("Sleeping failed", e);
			}
			done.set(true);
			latch.countDown();
			System.out.println("Job done");
		}
	}

	public void testDelayedDisplayAsync() {
		if (!PlatformUI.isWorkbenchRunning()) {
			System.out.println("Skipping SwtUISynchronizerTest.testDelayedDisplayAsync() - requires PDEJUnit");
			return;
		}
		final MockDisplay mockDisplay = new MockDisplay();
		final DummyJob job = new DummyJob();
		job.sleepTime = 4000;
		provideDisplay.set(false);

		final SwtUISynchronizer synchronizer = new SwtUISynchronizer() {

			@Override
			public Display getDisplay() {
				return provideDisplay.get() ? mockDisplay : null;
			}

			@Override
			protected boolean hasDisplay() {
				return true;
			}

		};
		long start = System.currentTimeMillis();
		synchronizer.asyncExec(job);
		final long end = System.currentTimeMillis();
		assertTrue(end - start < job.sleepTime);
		// as the display does not yet exist the job must not done
		assertFalse(job.done.get());

		final Timer timer = new Timer();
		final long taskDelay = 3000;
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				provideDisplay.set(true);
			}
		}, taskDelay);

		start = System.currentTimeMillis();

		boolean assertCalls = false;

		try {
			// wait for the job to concurrently execute
			assertCalls = job.latch.await(10000, TimeUnit.MILLISECONDS);
		} catch (final InterruptedException e) {
			throw new MurphysLawFailure("Waiting failed", e);
		}

		// the job should have waked this thread
		assertTrue(assertCalls);
		// and therefore the job should be done now
		assertTrue(System.currentTimeMillis() - start >= job.sleepTime + taskDelay);
		assertTrue(job.done.get());
		assertEquals(1, mockDisplay.asyncExecCalls);
	}

	public void testDelayedDisplaySync() {
		if (!PlatformUI.isWorkbenchRunning()) {
			System.out.println("Skipping SwtUISynchronizerTest.testDelayedDisplaySync() - requires PDEJunit");
			return;
		}
		final MockDisplay mockDisplay = new MockDisplay();
		DummyJob job = new DummyJob();
		job.sleepTime = 4000;
		provideDisplay.set(false);

		final SwtUISynchronizer synchronizer = new SwtUISynchronizer() {

			@Override
			public Display getDisplay() {
				return provideDisplay.get() ? mockDisplay : null;
			}

			@Override
			protected boolean hasDisplay() {
				return true;
			}

		};

		job = new DummyJob();
		job.sleepTime = 4000;
		provideDisplay.set(false);

		final Timer timer = new Timer();
		final long taskDelay = 5000;
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				provideDisplay.set(true);
			}
		}, taskDelay);

		final long start = System.currentTimeMillis();
		synchronizer.syncExec(job);
		// this thread has been waiting for execution of the job (syncExec),
		// ie the job must be done and syncExec must have been called
		assertTrue(System.currentTimeMillis() - start >= job.sleepTime + taskDelay);
		assertTrue(job.done.get());
		assertEquals(1, mockDisplay.syncExecCalls);
	}

	public void testDelayedDisplayMultiple() {
		if (!PlatformUI.isWorkbenchRunning()) {
			System.out.println("Skipping SwtUISynchronizerTest.testDelayedDisplayMultiple() - requires PDEJunit");
			return;
		}

		final MockDisplay mockDisplay = new MockDisplay();
		provideDisplay.set(false);

		final SwtUISynchronizer synchronizer = new SwtUISynchronizer() {
			@Override
			public Display getDisplay() {
				return provideDisplay.get() ? mockDisplay : null;
			}

			@Override
			protected boolean hasDisplay() {
				return true;
			}
		};

		provideDisplay.set(false);

		final Timer timer = new Timer();
		final long taskDelay = 5000;

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				provideDisplay.set(true);
			}
		}, taskDelay);
		final List<DummyJob> jobs = new ArrayList<DummyJob>();
		final DummyJob exceptionJob = new DummyJob() {
			@Override
			public void run() {
				super.run();
				throw new RuntimeException();
			}
		};
		jobs.add(exceptionJob);
		synchronizer.asyncExec(exceptionJob);
		mockDisplay.asyncExecCalls = 0;
		for (int i = 0; i < 25; i++) {
			final DummyJob jobI = new DummyJob();
			jobs.add(jobI);
			synchronizer.asyncExec(jobI);
			try {
				Thread.sleep(80);
			} catch (final InterruptedException e) {
				throw new MurphysLawFailure("Sleeping failed", e);
			}
		}
		assertNotNull(ReflectionUtils.getHidden(synchronizer, "displayObserver"));
		for (final DummyJob dummyJob : jobs) {
			try {
				dummyJob.latch.await(25 * 80 + taskDelay + 5000, TimeUnit.MILLISECONDS);
			} catch (final InterruptedException e) {
				throw new MurphysLawFailure("Waiting failed", e);
			}
		}
		assertTrue(mockDisplay.asyncExecCalls > 1);
		assertNull(ReflectionUtils.getHidden(synchronizer, "displayObserver"));

		// all jobs must be done by now
		for (final DummyJob job : jobs) {
			assertTrue(job.done.get());
		}
	}

	public void testSyncExec() {
		final MockDisplay mockDisplay = new MockDisplay();
		final SwtUISynchronizer synchronizer = new SwtUISynchronizer() {
			@Override
			public Display getDisplay() {
				return mockDisplay;
			}

			@Override
			protected boolean hasDisplay() {
				return true;
			}
		};
		synchronizer.syncExec(EasyMock.createNiceMock(Runnable.class));
		assertEquals(1, mockDisplay.syncExecCalls);
		assertEquals(0, mockDisplay.asyncExecCalls);

	}

	public void testAsyncExec() {
		final MockDisplay mockDisplay = new MockDisplay();
		final SwtUISynchronizer synchronizer = new SwtUISynchronizer() {
			@Override
			public Display getDisplay() {
				return mockDisplay;
			}

			@Override
			protected boolean hasDisplay() {
				return true;
			}
		};
		synchronizer.asyncExec(EasyMock.createNiceMock(Runnable.class));
		assertEquals(0, mockDisplay.syncExecCalls);
		assertEquals(1, mockDisplay.asyncExecCalls);
	}

}
