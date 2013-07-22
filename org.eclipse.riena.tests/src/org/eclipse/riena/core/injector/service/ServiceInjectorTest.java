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

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.injector.InjectionFailure;
import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * Tests the {@code ServiceInjector}.
 */
@NonUITestCase
public class ServiceInjectorTest extends RienaTestCase {

	public void testInjectDepOneObviousBindUnbindError() {
		printTestName();
		final Target target = new Target();

		final DepOne depOne = new DepOne();
		final ServiceRegistration reg = getContext().registerService(DepOne.class.getName(), depOne, null);

		try {
			Inject.service(DepOne.class.getName()).into(target).bind("baind").andStart(getContext());
			fail("Well, that should not have happended");
		} catch (final InjectionFailure e) {
			assertTrue(true);
		} finally {
			reg.unregister();
		}

	}

	public void testInjectDepOneNotSoObviousBindUnbindError() {
		printTestName();
		final Target target = new Target();

		final DepTwo depTwo = new DepTwo();
		final ServiceRegistration reg = getContext().registerService(DepTwo.class.getName(), depTwo, null);

		try {
			Inject.service(DepOne.class.getName()).into(target).bind("binde").unbind("entbinde").andStart(getContext());
			assertEquals(0, target.count("binde", DepOne.class));
		} catch (final InjectionFailure e) {
			assertTrue(false);
		} finally {
			reg.unregister();
		}
	}

	public void testInjectDepOneDefaultBindUnbind() {
		printTestName();
		final Target target = new Target();

		final DepOne depOne = new DepOne();
		final ServiceRegistration reg = getContext().registerService(DepOne.class.getName(), depOne, null);

		final ServiceInjector shot = Inject.service(DepOne.class.getName()).into(target).andStart(getContext());
		assertEquals(1, target.count("bind", DepOne.class));

		shot.stop();
		assertEquals(0, target.count("bind", DepOne.class));

		reg.unregister();
	}

	public void testInjectDepOneDefaultBindUnbindUseClassInsteadOfClassname() {
		printTestName();
		final Target target = new Target();

		final DepOne depOne = new DepOne();
		final ServiceRegistration reg = getContext().registerService(DepOne.class.getName(), depOne, null);

		final ServiceInjector shot = Inject.service(DepOne.class).into(target).andStart(getContext());
		assertEquals(1, target.count("bind", DepOne.class));

		shot.stop();
		assertEquals(0, target.count("bind", DepOne.class));

		reg.unregister();
	}

	public void testInjectDepOneAndDepTwoDefaultBindUnbind() {
		printTestName();
		final Target target = new Target();

		final DepOne depOne = new DepOne();
		final ServiceRegistration reg1 = getContext().registerService(DepOne.class.getName(), depOne, null);

		final ServiceInjector shot1 = Inject.service(DepOne.class.getName()).into(target).andStart(getContext());
		assertEquals(1, target.count("bind", DepOne.class));

		final ServiceInjector shot2 = Inject.service(DepTwo.class.getName()).into(target).andStart(getContext());

		final DepTwo depTwo = new DepTwo();
		final ServiceRegistration reg2 = getContext().registerService(DepTwo.class.getName(), depTwo, null);

		assertEquals(1, target.count("bind", DepTwo.class));

		shot1.stop();
		assertEquals(0, target.count("bind", DepOne.class));

		shot2.stop();
		assertEquals(0, target.count("bind", DepTwo.class));

		reg1.unregister();
		reg2.unregister();
	}

	public void testInjectDepOneBindeEntbinde() {
		printTestName();
		final Target target = new Target();

		final DepOne depOne = new DepOne();
		final ServiceRegistration reg = getContext().registerService(DepOne.class.getName(), depOne, null);

		final ServiceInjector shot = Inject.service(DepOne.class.getName()).into(target).bind("binde")
				.unbind("entbinde").andStart(getContext());
		assertEquals(1, target.count("binde", DepOne.class));

		shot.stop();
		assertEquals(0, target.count("binde", DepOne.class));

		reg.unregister();
	}

	public void testInjectRankedServicesServicesRegisteredBefore() {
		printTestName();
		final Target target = new Target();

		final IRanking rank1 = new RankingOne(0);
		final ServiceRegistration reg1 = getContext().registerService(IRanking.class.getName(), rank1, null);

		final IRanking rank2 = new RankingTwo(100);
		final Dictionary<String, Object> dict = new Hashtable<String, Object>();
		dict.put(Constants.SERVICE_RANKING, Integer.valueOf(100));
		final ServiceRegistration reg2 = getContext().registerService(IRanking.class.getName(), rank2, dict);

		final ServiceInjector shot = Inject.service(IRanking.class.getName()).useRanking().into(target)
				.andStart(getContext());
		assertEquals(1, target.count("bind", IRanking.class));

		assertEquals(100, target.getDepRanking());

		shot.stop();
		assertEquals(0, target.count("bind", IRanking.class));

		reg1.unregister();
		reg2.unregister();
	}

	public void testInjectRankedServicesServicesRegisteredOnTheRun() {
		printTestName();
		final Target target = new Target();

		final IRanking rank1 = new RankingOne(0);
		final ServiceRegistration reg1 = getContext().registerService(IRanking.class.getName(), rank1, null);

		final ServiceInjector shot = Inject.service(IRanking.class.getName()).useRanking().into(target)
				.andStart(getContext());
		assertEquals(1, target.count("bind", IRanking.class));
		assertEquals(0, target.getDepRanking());

		final IRanking rank2 = new RankingTwo(100);
		final Dictionary<String, Object> dict = new Hashtable<String, Object>();
		dict.put(Constants.SERVICE_RANKING, Integer.valueOf(100));
		final ServiceRegistration reg2 = getContext().registerService(IRanking.class.getName(), rank2, dict);

		assertEquals(1, target.count("bind", IRanking.class));

		assertEquals(100, target.getDepRanking());

		reg2.unregister();

		assertEquals(1, target.count("bind", IRanking.class));

		assertEquals(0, target.getDepRanking());

		final ServiceRegistration reg3 = getContext().registerService(IRanking.class.getName(), rank2, dict);

		assertEquals(1, target.count("bind", IRanking.class));

		assertEquals(100, target.getDepRanking());

		shot.stop();
		assertEquals(0, target.count("bind", IRanking.class));

		reg1.unregister();
		reg3.unregister();
	}

	public void testFilterTest() {
		printTestName();
		final Target target = new Target();

		final DepOne depOne = new DepOne();
		final Hashtable<String, String> props = new Hashtable<String, String>();
		props.put("x", "y");
		final ServiceRegistration reg = getContext().registerService(DepOne.class.getName(), depOne, props);

		final ServiceInjector shot = Inject.service(DepOne.class.getName()).useFilter("(x=y)").into(target)
				.andStart(getContext());
		assertEquals(1, target.count("bind", DepOne.class));

		shot.stop();
		assertEquals(0, target.count("bind", DepOne.class));

		reg.unregister();
	}
}
