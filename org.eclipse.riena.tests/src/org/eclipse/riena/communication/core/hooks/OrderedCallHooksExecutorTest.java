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
package org.eclipse.riena.communication.core.hooks;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.internal.communication.core.factory.ICallHookExtension;
import org.eclipse.riena.internal.communication.core.factory.OrderedCallHooksExecuter;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

@SuppressWarnings("restriction")
@NonUITestCase
public class OrderedCallHooksExecutorTest extends RienaTestCase {

	public void testUpdate() {
		List<DummyCallHook> before = new ArrayList<OrderedCallHooksExecutorTest.DummyCallHook>();
		List<DummyCallHook> after = new ArrayList<OrderedCallHooksExecutorTest.DummyCallHook>();
		DummyCallHook ch1 = new DummyCallHook(before, after);
		ExtensionBean b1 = new ExtensionBean("a", "", "b", ch1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		DummyCallHook ch2 = new DummyCallHook(before, after);
		ExtensionBean b2 = new ExtensionBean("b", "", "", ch2); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		final OrderedCallHooksExecuter exec = new OrderedCallHooksExecuter();
		exec.update(new ICallHookExtension[] { b1, b2 });
		exec.beforeCall(null);
		assertEquals(ch1, before.get(0));
		assertEquals(ch2, before.get(1));
		exec.afterCall(null);
		assertEquals(ch1, after.get(1));
		assertEquals(ch2, after.get(0));

		before = new ArrayList<OrderedCallHooksExecutorTest.DummyCallHook>();
		after = new ArrayList<OrderedCallHooksExecutorTest.DummyCallHook>();
		ch1 = new DummyCallHook(before, after);
		ch2 = new DummyCallHook(before, after);
		b1 = new ExtensionBean("a", "b", "", ch1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		b2 = new ExtensionBean("b", "", "", ch2); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		exec.update(new ICallHookExtension[] { b1, b2 });
		exec.beforeCall(null);
		assertEquals(ch1, before.get(1));
		assertEquals(ch2, before.get(0));
		exec.afterCall(null);
		assertEquals(ch1, after.get(0));
		assertEquals(ch2, after.get(1));

		before = new ArrayList<OrderedCallHooksExecutorTest.DummyCallHook>();
		after = new ArrayList<OrderedCallHooksExecutorTest.DummyCallHook>();
		ch1 = new DummyCallHook(before, after);
		ch2 = new DummyCallHook(before, after);
		b1 = new ExtensionBean("a", "", "", ch1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		b2 = new ExtensionBean("b", "", "a", ch2); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		exec.update(new ICallHookExtension[] { b1, b2 });
		exec.beforeCall(null);
		assertEquals(ch1, before.get(1));
		assertEquals(ch2, before.get(0));
		exec.afterCall(null);
		assertEquals(ch1, after.get(0));
		assertEquals(ch2, after.get(1));
	}

	private final static class DummyCallHook implements ICallHook {

		private final List<DummyCallHook> before;

		private final List<DummyCallHook> after;

		public DummyCallHook(final List<DummyCallHook> before, final List<DummyCallHook> after) {
			super();
			this.before = before;
			this.after = after;
		}

		public void beforeCall(final CallContext context) {
			before.add(this);
		}

		public void afterCall(final CallContext context) {
			after.add(this);
		}

	}

	private final static class ExtensionBean implements ICallHookExtension {

		private final String name;

		private final String preHooks;

		private final String postHooks;

		private final ICallHook callHook;

		public ExtensionBean(final String name, final String preHooks, final String postHooks, final ICallHook callHook) {
			this.name = name;
			this.preHooks = preHooks;
			this.postHooks = postHooks;
			this.callHook = callHook;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the preHooks
		 */
		public String getPreHooks() {
			return preHooks;
		}

		/**
		 * @return the postHooks
		 */
		public String getPostHooks() {
			return postHooks;
		}

		/**
		 * @return the callHook
		 */
		public ICallHook getCallHook() {
			return callHook;
		}

	}
}
