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
package org.eclipse.riena.core.exception;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import junit.framework.Assert;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.injector.InjectionFailure;
import org.eclipse.riena.core.service.Service;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.exceptionmanager.IExceptionHandlerExtension;
import org.eclipse.riena.internal.core.exceptionmanager.SimpleExceptionHandlerManager;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * 
 */
@NonUITestCase
public class SimpleExceptionHandlerManagerTest extends RienaTestCase {

	private SimpleExceptionHandlerManager manager;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		manager = new SimpleExceptionHandlerManager();
	}

	@Override
	public void tearDown() throws Exception {
		manager = null;
		super.tearDown();
	}

	public void testAddOneHandler() {
		manager.update(new IExceptionHandlerExtension[] { createHandlerExtension("handler1", "IOE", null, null, null) });

		final Exception exception = new Exception("IOE");
		manager.handleException(exception);

		Assert.assertEquals("expected exception", "handler1", TestExceptionHandler.getHandledName());
	}

	public void testAddTwoHandlersWithNoParticularOrderAndExceptionNotHandled1() {
		manager.update(new IExceptionHandlerExtension[] {
				createHandlerExtension("handler1", "IAE", "handlerNotThere", null, null),
				createHandlerExtension("handler2", "IAE", null, null, null) });

		final List<IExceptionHandler> orderedHandlers = getOrderedHandlers();
		Assert.assertEquals("handler1", orderedHandlers.get(0).toString());
		Assert.assertEquals("handler2", orderedHandlers.get(1).toString());

		final Exception exception = new Exception("no responsibility");
		manager.handleException(exception);

		Assert.assertNull("no one should handle", TestExceptionHandler.getHandledName());
	}

	public void testAddTwoHandlersWithNoParticularOrderAndExceptionNotHandled2() {
		manager.update(new IExceptionHandlerExtension[] { createHandlerExtension("handler2", "IAE", null, null, null),
				createHandlerExtension("handler1", "IAE", "handlerNotThere", null, null) });

		final List<IExceptionHandler> orderedHandlers = getOrderedHandlers();
		Assert.assertEquals("handler2", orderedHandlers.get(0).toString());
		Assert.assertEquals("handler1", orderedHandlers.get(1).toString());

		final Exception exception = new Exception("no responsibility");
		manager.handleException(exception);

		Assert.assertNull("no one should handle", TestExceptionHandler.getHandledName());
	}

	public void testAddThreeHandlersWithParticularOrderAndExceptionHandled1() {
		manager.update(new IExceptionHandlerExtension[] {
				createHandlerExtension("handler1", "IOE", "handlerNotThere", null, null),
				createHandlerExtension("handler2", "IOE", "*", null, null),
				createHandlerExtension("handler3", "IOE", null, null, "*") });

		final List<IExceptionHandler> orderedHandlers = getOrderedHandlers();
		Assert.assertEquals("handler2", orderedHandlers.get(0).toString());
		Assert.assertEquals("handler1", orderedHandlers.get(1).toString());
		Assert.assertEquals("handler3", orderedHandlers.get(2).toString());

		final Exception exception = new Exception("IOE");
		manager.handleException(exception);

		Assert.assertEquals("handler2", TestExceptionHandler.getHandledName());
	}

	public void testAddThreeHandlersWithParticularOrderAndExceptionHandled2() {
		manager.update(new IExceptionHandlerExtension[] {
				createHandlerExtension("handler1", "IOE", "handlerNotThere", null, null),
				createHandlerExtension("handler3", "IOE", null, null, "*"),
				createHandlerExtension("handler2", "IAE", "*", null, null) });

		final List<IExceptionHandler> orderedHandlers = getOrderedHandlers();
		Assert.assertEquals("handler2", orderedHandlers.get(0).toString());
		Assert.assertEquals("handler1", orderedHandlers.get(1).toString());
		Assert.assertEquals("handler3", orderedHandlers.get(2).toString());

		final Exception exception = new Exception("IOE");
		manager.handleException(exception);

		Assert.assertEquals("handler1", TestExceptionHandler.getHandledName());
	}

	public void testAddHandlersWithConflictingBeforeAndPostHandlerAttributes() {
		try {
			manager.update(new IExceptionHandlerExtension[] {
					createHandlerExtension("handler1", "IOE", "A", "B", null), // conflict, either before or postHandlers
					createHandlerExtension("handler3", "IOE", null, null, "*"),
					createHandlerExtension("handler2", "IAE", "*", null, null) });
			fail("Exception expected");
		} catch (final InjectionFailure e) {
			Assert.assertTrue(e.getMessage().contains(
					"uses both the deprecated 'before' and new 'getPostHandler' attributes"));
		}
	}

	public void testFailingJob() throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);
		manager.update(new IExceptionHandlerExtension[] { createLatchedHandlerExtension(latch) });
		manager.bind(Service.get(IJobManager.class));

		new Job("NPE") {
			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				throw new NullPointerException("Ahh!");
			}
		}.schedule();
		latch.await();
		Assert.assertEquals(NullPointerException.class, TestLatchedExceptionHandler.caught.getClass());
	}

	public void testNoneFailingJob() throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(0);
		manager.update(new IExceptionHandlerExtension[] { createLatchedHandlerExtension(latch) });
		manager.bind(Service.get(IJobManager.class));

		new Job("NPE") {
			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				return Status.OK_STATUS;
			}
		}.schedule();
		latch.await();
		Assert.assertNull(TestLatchedExceptionHandler.caught);
	}

	private IExceptionHandlerExtension createHandlerExtension(final String name, final String exceptionMessageTrigger,
			final String before, final String postHandlers, final String preHandlers) {
		return new IExceptionHandlerExtension() {

			public IExceptionHandler createExceptionHandler() {
				return new TestExceptionHandler(name, exceptionMessageTrigger);
			}

			public String getBefore() {
				return before;
			}

			public String getExceptionHandler() {
				return name;
			}

			public String getName() {
				return name;
			}

			public String getPreHandlers() {
				return preHandlers;
			}

			public String getPostHandlers() {
				return postHandlers;
			}
		};
	}

	private List<IExceptionHandler> getOrderedHandlers() {
		return (List<IExceptionHandler>) ReflectionUtils.getHidden(manager, "handlers");
	}

	private IExceptionHandlerExtension createLatchedHandlerExtension(final CountDownLatch latch) {
		return new IExceptionHandlerExtension() {

			public IExceptionHandler createExceptionHandler() {
				return new TestLatchedExceptionHandler(latch);
			}

			public String getBefore() {
				return null;
			}

			public String getExceptionHandler() {
				return "Latcher";
			}

			public String getName() {
				return null;
			}

			public String getPreHandlers() {
				return null;
			}

			public String getPostHandlers() {
				return null;
			}
		};
	}

	private static class TestLatchedExceptionHandler implements IExceptionHandler {

		private final CountDownLatch latch;
		static Throwable caught;

		TestLatchedExceptionHandler(final CountDownLatch latch) {
			this.latch = latch;
			caught = null;
		}

		public Action handleException(final Throwable t, final String msg, final Logger logger) {
			caught = t;
			latch.countDown();
			return Action.OK;
		}

	}

}
