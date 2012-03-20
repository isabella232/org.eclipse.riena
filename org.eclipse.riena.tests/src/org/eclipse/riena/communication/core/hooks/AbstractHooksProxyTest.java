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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;

import javax.security.auth.Subject;

import org.eclipse.riena.core.util.ReflectionFailure;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Test the {@code AbstractHooksProxyTest}.
 */
@NonUITestCase
public class AbstractHooksProxyTest extends RienaTestCase {

	public void testProxySameInterfaceAsDelegate() {
		final IAdder adder = (IAdder) newHooksProxy(new Adder());
		assertEquals("Hello World", adder.add("Hello ", "World")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals("12", adder.add((Number) 1, (Number) 2)); //$NON-NLS-1$
		assertEquals("3", adder.add(1, 2)); //$NON-NLS-1$
		assertMethodTableUsage(false, adder);
	}

	public void testProxySameInterfaceAsDelegateWithSubject() {
		final IAdder adder = (IAdder) newHooksProxy(new Adder(), new Subject());
		assertEquals("Hello World", adder.add("Hello ", "World")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals("12", adder.add((Number) 1, (Number) 2)); //$NON-NLS-1$
		assertEquals("3", adder.add(1, 2)); //$NON-NLS-1$
		assertMethodTableUsage(false, adder);
	}

	public void testProxyCallsProxyWithSameInterface() {
		final IAdder adder = (IAdder) newHooksProxy(newHooksProxy(new Adder()));
		assertEquals("Hello World", adder.add("Hello ", "World")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals("12", adder.add((Number) 1, (Number) 2)); //$NON-NLS-1$
		assertEquals("3", adder.add(1, 2)); //$NON-NLS-1$
		assertMethodTableUsage(false, adder);
	}

	public void testProxyCallsProxyWithSameInterfaceWithSubject() {
		final IAdder adder = (IAdder) newHooksProxy(newHooksProxy(new Adder(), new Subject()));
		assertEquals("Hello World", adder.add("Hello ", "World")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals("12", adder.add((Number) 1, (Number) 2)); //$NON-NLS-1$
		assertEquals("3", adder.add(1, 2)); //$NON-NLS-1$
		assertMethodTableUsage(false, adder);
	}

	public void testProxyCallsObjectWithSameMethodButNotInInterface() {
		final IAdder adder = (IAdder) newHooksProxy(new FreeAdder());
		assertEquals("Hello World", adder.add("Hello ", "World")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals("12", adder.add((Number) 1, (Number) 2)); //$NON-NLS-1$
		assertEquals("3", adder.add(1, 2)); //$NON-NLS-1$
		assertMethodTableUsage(true, adder);
	}

	public void testProxyCallsObjectWithSameMethodButNotInInterfaceWithSubject() {
		final IAdder adder = (IAdder) newHooksProxy(new FreeAdder(), new Subject());
		assertEquals("Hello World", adder.add("Hello ", "World")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals("12", adder.add((Number) 1, (Number) 2)); //$NON-NLS-1$
		assertEquals("3", adder.add(1, 2)); //$NON-NLS-1$
		assertMethodTableUsage(true, adder);
	}

	private Object newHooksProxy(final Object delegate) {
		return Proxy.newProxyInstance(AbstractHooksProxyTest.class.getClassLoader(), new Class<?>[] { IAdder.class },
				new HooksProxy(delegate));
	}

	private Object newHooksProxy(final Object delegate, final Subject subject) {
		return Proxy.newProxyInstance(AbstractHooksProxyTest.class.getClassLoader(), new Class<?>[] { IAdder.class },
				new HooksProxy(delegate, subject));
	}

	/**
	 * This just checks whether the {@code AbstractHooksProxy} uses its method
	 * table to retrieve the appropriate method.
	 * 
	 * @param proxy
	 * @return
	 */
	private void assertMethodTableUsage(final boolean expected, final Object proxy) {
		final InvocationHandler handler = Proxy.getInvocationHandler(proxy);
		try {
			final AtomicReference<?> ref = ReflectionUtils.getHidden(handler, "methodTableRef"); //$NON-NLS-1$
			assertEquals(expected, ref.get() != null);
		} catch (final ReflectionFailure f) {
			System.err.println("Culd not access 'methodTableRef' field."); //$NON-NLS-1$
		}
	}
}
