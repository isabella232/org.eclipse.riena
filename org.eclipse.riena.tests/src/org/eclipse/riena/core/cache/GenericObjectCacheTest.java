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
package org.eclipse.riena.core.cache;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.ManualTestCase;
import org.eclipse.riena.core.util.Nop;

/**
 * Tests the GenericObjectCache class.
 * 
 */
@ManualTestCase
public class GenericObjectCacheTest extends RienaTestCase {

	private IGenericObjectCache<String, Integer> genericCache;
	private IGenericObjectCache<Integer, TestRunner> genericCache2;
	private IGenericObjectCache<String, StringBuffer> genericCache3;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		genericCache = new GenericObjectCache<String, Integer>();
	}

	@Override
	public void tearDown() throws Exception {
		genericCache = null;
		super.tearDown();
		genericCache = null;
	}

	/**
	 * test basic instantiation
	 */
	public void testInstantiate() {
		genericCache.put("test", Integer.valueOf(2));
		assertNotNull("did not find put object in cache", genericCache.get("test"));
		assertTrue("object in cache has incorrect value", genericCache.get("test").equals(Integer.valueOf(2)));
		genericCache.clear();
	}

	/**
	 * @throws Exception
	 */
	public void testTimeout() throws Exception {
		genericCache.put("test", Integer.valueOf(3));
		genericCache.setTimeout(300);
		Thread.sleep(100);
		assertNotNull("did not find put object in cache", genericCache.get("test"));
		Thread.sleep(600);
		assertNull("must not find object in cache after timeout", genericCache.get("test"));
		genericCache.clear();
	}

	/**
	 * @throws Exception
	 */
	public void testPutMultiple() throws Exception {
		final IGenericObjectCache<Integer, String> genericCacheLocal = new GenericObjectCache<Integer, String>();
		for (int i = 0; i < 30; i++) {
			genericCacheLocal.put(Integer.valueOf(i), "testvalue");
		}

		for (int i = 0; i < 30; i++) {
			assertNotNull("object not found in cache", genericCacheLocal.get(Integer.valueOf(i)));
		}
		genericCacheLocal.clear();
	}

	/**
	 * @throws Exception
	 */
	public void testMultiThread() throws Exception {
		genericCache2 = new GenericObjectCache<Integer, TestRunner>();
		final Thread[] runner = new Thread[10];
		for (int i = 0; i < 10; i++) {
			runner[i] = new TestRunner(i * 20, i * 20 + 19, 10);
		}
		runAndCheckThreads(runner);
		genericCache.clear();
	}

	/**
	 * @throws Exception
	 */
	public void testMultiThreadSameRange() throws Exception {
		genericCache2 = new GenericObjectCache<Integer, TestRunner>();
		final Thread[] runner = new Thread[10];
		for (int i = 0; i < 10; i++) {
			runner[i] = new TestRunner(0, 20, 10);
		}
		runAndCheckThreads(runner);
		genericCache.clear();
	}

	/**
	 * @throws Exception
	 */
	public void testMultiThreadLargeNoOfThreadsSameRange() throws Exception {
		genericCache2 = new GenericObjectCache<Integer, TestRunner>();
		final Thread[] runner = new Thread[100];
		for (int i = 0; i < 100; i++) {
			runner[i] = new TestRunner(0, 20, 100);
		}
		runAndCheckThreads(runner);
		genericCache.clear();
	}

	/**
	 * @throws Exception
	 */
	public void testMultiThreadLargeNoOfThreads() throws Exception {
		genericCache2 = new GenericObjectCache<Integer, TestRunner>();
		final Thread[] runner = new Thread[100];
		for (int i = 0; i < 100; i++) {
			runner[i] = new TestRunner(i * 20, i * 20 + 19, 100);
		}
		runAndCheckThreads(runner);
		genericCache.clear();
	}

	/**
	 * Tests if softreferences are really free-ed in low-memory situations
	 * 
	 * @throws Exception
	 */
	public void testTryOverloadCache() throws Exception {
		genericCache3 = new GenericObjectCache<String, StringBuffer>();

		int x = 1;
		for (int i = 0; i < 5000; i++) {
			genericCache3.put(Integer.valueOf(i).toString(), new StringBuffer(20000));
			//			System.out.print(".");
			x++;
			if (x > 100) {
				//				System.out.println(i);
				x = 0;
			}
		}
		genericCache3.clear();
		genericCache3 = null;
	}

	private void runAndCheckThreads(final Thread[] runner) {
		final Status status = new Status();
		for (final Thread element : runner) {
			//			trace("starting thread " + i);
			((TestRunner) element).setStatus(status);
			element.start();
		}
		int count = 0; // number of running threads
		boolean first = true;
		Thread.yield();
		while (count != 0 || first) {
			count = 0;
			for (final Thread element : runner) {
				if (element.isAlive()) {
					count++;
				}
			}
			assertNull("one of the threads created an error", status.getLastException());
			first = false;
			try {
				Thread.sleep(100);
			} catch (final InterruptedException e) {
				Nop.reason("no action");
			}
		}
	}

	class TestRunner extends Thread {
		private final int lowRange;
		private final int highRange;
		private final int runs;
		private Status status;

		TestRunner(final int lowRange, final int highRange, final int runs) {
			this.lowRange = lowRange;
			this.highRange = highRange;
			this.runs = runs;
			//			trace("low=" + lowRange + " high=" + highRange + " runs=" + runs);
		}

		/**
		 * @param status
		 */
		public void setStatus(final Status status) {
			this.status = status;
		}

		@Override
		public void run() {
			try {
				for (int i = 0; i < runs; i++) {
					for (int k = lowRange; k <= highRange; k++) {
						genericCache2.put(Integer.valueOf(k), this);
					}
					for (int k = lowRange; k <= highRange; k++) {
						assertNotNull("object not found in cache", genericCache2.get(Integer.valueOf(k)));
					}
				}
			} catch (final Exception e) {
				this.status.setLastException(e);
			}
		}
	}

	static class Status {
		private Exception lastException;

		/**
		 * @param lastException
		 */
		public void setLastException(final Exception lastException) {
			this.lastException = lastException;
		}

		/**
		 * @return
		 */
		public Exception getLastException() {
			return lastException;
		}
	}
}