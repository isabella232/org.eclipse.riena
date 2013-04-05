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
package org.eclipse.riena.core.injector.service;

import org.osgi.framework.ServiceRegistration;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Tests the {@code ServiceInjector}.
 */
@NonUITestCase
public class ServiceInjectorOnceOnlyTest extends RienaTestCase {

	private ServiceRegistration reg;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		reg = getContext().registerService(DepOne.class.getName(), new DepOne(), null);
	}

	@Override
	protected void tearDown() throws Exception {
		if (reg != null) {
			reg.unregister();
		}
		super.tearDown();
	}

	public void testInjectOnceOnlyViaStaticOneTarget() {
		printTestName();
		final TargetOnceOnlyViaStatic target = new TargetOnceOnlyViaStatic();

		ServiceInjector injector = null;
		try {
			TargetOnceOnlyViaStatic.resetCounters();
			injector = Inject.service(DepOne.class.getName()).into(target).bind("bind").andStart(getContext());
			assertEquals(1, TargetOnceOnlyViaStatic.getBindCounter());
		} finally {
			injector.stop();
			assertEquals(1, TargetOnceOnlyViaStatic.getUnbindCounter());
		}
	}

	public void testInjectOnceOnlyViaStaticTwoTargetsFirstGoesFirst() {
		printTestName();
		final TargetOnceOnlyViaStatic target1 = new TargetOnceOnlyViaStatic();
		final TargetOnceOnlyViaStatic target2 = new TargetOnceOnlyViaStatic();

		ServiceInjector injector1 = null;
		ServiceInjector injector2 = null;
		try {
			TargetOnceOnlyViaStatic.resetCounters();

			injector1 = Inject.service(DepOne.class.getName()).into(target1).bind("bind").andStart(getContext());
			assertEquals(1, TargetOnceOnlyViaStatic.getBindCounter());

			injector2 = Inject.service(DepOne.class.getName()).into(target2).bind("bind").andStart(getContext());
			assertEquals(1, TargetOnceOnlyViaStatic.getBindCounter());

		} finally {
			injector1.stop();
			assertEquals(0, TargetOnceOnlyViaStatic.getUnbindCounter());

			injector2.stop();
			assertEquals(1, TargetOnceOnlyViaStatic.getUnbindCounter());
		}
	}

	public void testInjectOnceOnlyViaStaticTwoTargetsFirstGoesLast() {
		printTestName();
		final TargetOnceOnlyViaStatic target1 = new TargetOnceOnlyViaStatic();
		final TargetOnceOnlyViaStatic target2 = new TargetOnceOnlyViaStatic();

		ServiceInjector injector1 = null;
		ServiceInjector injector2 = null;
		try {
			TargetOnceOnlyViaStatic.resetCounters();

			injector1 = Inject.service(DepOne.class.getName()).into(target1).bind("bind").andStart(getContext());
			assertEquals(1, TargetOnceOnlyViaStatic.getBindCounter());

			injector2 = Inject.service(DepOne.class.getName()).into(target2).bind("bind").andStart(getContext());
			assertEquals(1, TargetOnceOnlyViaStatic.getBindCounter());

		} finally {
			injector2.stop();
			assertEquals(0, TargetOnceOnlyViaStatic.getUnbindCounter());

			injector1.stop();
			assertEquals(1, TargetOnceOnlyViaStatic.getUnbindCounter());
		}
	}

	public void testInjectOnceOnlyViaStaticTwoTargetsFirstGoesFirstServiceGoesAndComes() {
		printTestName();
		final TargetOnceOnlyViaStatic target1 = new TargetOnceOnlyViaStatic();
		final TargetOnceOnlyViaStatic target2 = new TargetOnceOnlyViaStatic();

		ServiceInjector injector1 = null;
		ServiceInjector injector2 = null;
		try {
			TargetOnceOnlyViaStatic.resetCounters();

			injector1 = Inject.service(DepOne.class.getName()).into(target1).bind("bind").andStart(getContext());
			assertEquals(1, TargetOnceOnlyViaStatic.getBindCounter());

			injector2 = Inject.service(DepOne.class.getName()).into(target2).bind("bind").andStart(getContext());
			assertEquals(1, TargetOnceOnlyViaStatic.getBindCounter());

			reg.unregister();

			assertEquals(1, TargetOnceOnlyViaStatic.getUnbindCounter());

			reg = getContext().registerService(DepOne.class.getName(), new DepOne(), null);

			assertEquals(2, TargetOnceOnlyViaStatic.getBindCounter());

		} finally {
			injector1.stop();
			assertEquals(1, TargetOnceOnlyViaStatic.getUnbindCounter());

			injector2.stop();
			assertEquals(2, TargetOnceOnlyViaStatic.getUnbindCounter());
		}
	}

	public void testInjectOnceOnlyViaStaticTwoTargetsFirstGoesLastGoesAndComes() {
		printTestName();
		final TargetOnceOnlyViaStatic target1 = new TargetOnceOnlyViaStatic();
		final TargetOnceOnlyViaStatic target2 = new TargetOnceOnlyViaStatic();

		ServiceInjector injector1 = null;
		ServiceInjector injector2 = null;
		try {
			TargetOnceOnlyViaStatic.resetCounters();

			injector1 = Inject.service(DepOne.class.getName()).into(target1).bind("bind").andStart(getContext());
			assertEquals(1, TargetOnceOnlyViaStatic.getBindCounter());

			injector2 = Inject.service(DepOne.class.getName()).into(target2).bind("bind").andStart(getContext());
			assertEquals(1, TargetOnceOnlyViaStatic.getBindCounter());

			reg.unregister();

			assertEquals(1, TargetOnceOnlyViaStatic.getUnbindCounter());

			reg = getContext().registerService(DepOne.class.getName(), new DepOne(), null);

			assertEquals(2, TargetOnceOnlyViaStatic.getBindCounter());

		} finally {
			injector2.stop();
			assertEquals(1, TargetOnceOnlyViaStatic.getUnbindCounter());

			injector1.stop();
			assertEquals(2, TargetOnceOnlyViaStatic.getUnbindCounter());
		}
	}

}
