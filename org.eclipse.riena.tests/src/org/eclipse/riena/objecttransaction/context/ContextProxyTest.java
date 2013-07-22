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
package org.eclipse.riena.objecttransaction.context;

import junit.framework.TestCase;

import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * TestCase for the ContextProxy
 */
@NonUITestCase
public class ContextProxyTest extends TestCase {

	private IContext activatedContext;
	private IContext passivatedContext;

	/**
	 * Test the management of a context an a non context carrier
	 * 
	 */
	public void testContextManagementOnNotContextCarrier() {

		final IContextHolder contextCarrier = new BasicContextHolder(null);

		final IContext context1 = new ContextProxyTestContext();
		final IContext context2 = new ContextProxyTestContext();
		assertFalse(context1.equals(context2));

		final IContextProxyTestInterface nonContextCarrier = new ContextProxyTestNonContextCarrier();

		// test creating proxy on null and setting
		IContextProxyTestInterface nonContextCarrierProxy = ContextProxy.cover(nonContextCarrier, contextCarrier);

		activatedContext = null;
		passivatedContext = null;
		assertNull(contextCarrier.getContext());
		contextCarrier.setContext(context1);
		assertNull(activatedContext);
		assertNull(passivatedContext);
		nonContextCarrierProxy.doSomething();
		assertEquals(context1, activatedContext);
		assertEquals(context1, passivatedContext);

		activatedContext = null;
		passivatedContext = null;
		contextCarrier.setContext(context2);
		assertEquals(context2, contextCarrier.getContext());
		assertNull(activatedContext);
		assertNull(passivatedContext);
		nonContextCarrierProxy.doSomething();
		assertEquals(context2, activatedContext);
		assertEquals(context2, passivatedContext);

		// test creating proxy on a context
		activatedContext = null;
		passivatedContext = null;

		contextCarrier.setContext(context1);

		nonContextCarrierProxy = ContextProxy.cover(nonContextCarrier, contextCarrier);
		assertEquals(context1, contextCarrier.getContext());
		assertNull(activatedContext);
		assertNull(passivatedContext);
		nonContextCarrierProxy.doSomething();
		assertEquals(context1, activatedContext);
		assertEquals(context1, passivatedContext);

	}

	/**
	 * Test the management of the context with the contextProxy, when creating
	 * the proxy on an object implementing IContextHolder
	 */
	public void testContextManagementOnContextCarrier() {

		final IContext context1 = new ContextProxyTestContext();
		final IContext context2 = new ContextProxyTestContext();
		assertFalse(context1.equals(context2));

		final IContextProxyTestInterfaceHolder contextCarrier = new ContextProxyTestContextHolder();

		// test creating proxy on null and setting
		final IContextProxyTestInterfaceHolder contextCarrierProxy = ContextProxy.cover(contextCarrier);

		activatedContext = null;
		passivatedContext = null;
		assertNull(contextCarrierProxy.getContext());
		contextCarrierProxy.setContext(context1);
		assertEquals(context1, contextCarrierProxy.getContext());
		contextCarrierProxy.setContext(context2);
		assertEquals(context2, contextCarrierProxy.getContext());
		contextCarrierProxy.doSomething();
		assertEquals(context2, activatedContext);
		assertEquals(context2, passivatedContext);

		contextCarrierProxy.setContext(context1);
		contextCarrierProxy.doSomething();
		assertEquals(context1, activatedContext);
		assertEquals(context1, passivatedContext);

	}

	private interface IContextProxyTestInterface {
		/**
		 * A dummy method
		 */
		void doSomething();
	}

	private interface IContextProxyTestInterfaceHolder extends IContextHolder {
		/**
		 * A dummy method
		 */
		void doSomething();
	}

	private static class ContextProxyTestNonContextCarrier implements IContextProxyTestInterface {
		/**
		 * @see org.eclipse.riena.objecttransaction.context.ContextProxyTest.IContextProxyTestInterface#doSomething()
		 */
		public void doSomething() {
			return;
		}
	}

	private static class ContextProxyTestContextHolder implements IContextProxyTestInterfaceHolder {

		private IContext context;

		/**
		 * @see org.eclipse.riena.objecttransaction.context.ContextProxyTest.IContextProxyTestInterface#doSomething()
		 */
		public void doSomething() {
			return;
		}

		/**
		 * @see org.eclipse.riena.objecttransaction.context.IContextHolder#getContext()
		 */
		public IContext getContext() {
			return context;
		}

		/**
		 * @see org.eclipse.riena.objecttransaction.context.IContextHolder#setContext(org.eclipse.riena.objecttransaction.context.IContext)
		 */
		public void setContext(final IContext pContext) {
			context = pContext;
		}

	}

	private class ContextProxyTestContext implements IContext {

		/**
		 * @see org.eclipse.riena.objecttransaction.context.IContext#activate()
		 */
		public void activate() {
			activatedContext = this;
		}

		/**
		 * @see org.eclipse.riena.objecttransaction.context.IContext#isActivated()
		 */
		public boolean isActivated() {
			return this.equals(activatedContext);
		}

		/**
		 * @see org.eclipse.riena.objecttransaction.context.IContext#passivate()
		 */
		public void passivate() {
			passivatedContext = this;

		}

	}

}
