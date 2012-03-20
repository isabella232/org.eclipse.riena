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
package org.eclipse.riena.core.wire;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import org.eclipse.riena.core.util.Literal;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.tests.Activator;

/**
 * Test the {@code Wire} stuff.
 */
@NonUITestCase
public class WireTest extends RienaTestCase {

	private final BundleContext context = Activator.getDefault().getContext();
	private ServiceRegistration schtonkReg;
	private ServiceRegistration stunkReg;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		schtonkReg = context.registerService(Schtonk.class.getName(), new Schtonk(), null);
		stunkReg = context.registerService(Stunk.class.getName(), new Stunk(), null);
	}

	@Override
	protected void tearDown() throws Exception {
		schtonkReg.unregister();
		stunkReg.unregister();
		super.tearDown();
	}

	public void testWiringBean() {
		final Bean bean = new Bean();
		final WirePuller puller = Wire.instance(bean).andStart(context);
		assertTrue(bean.hasSchtonk());
		puller.stop();
	}

	public void testWiringDeeplyAndCheckSequenceConstraint() {
		final BeanOnBean beanOnBean = new BeanOnBean();
		SequenceUtil.init();
		final WirePuller puller = Wire.instance(beanOnBean).andStart(context);
		SequenceUtil.assertExpected(BeanWiring.class, BeanOnBeanWiring.class);
		assertTrue(beanOnBean.hasSchtonk());
		assertTrue(beanOnBean.hasStunk());
		SequenceUtil.init();
		puller.stop();
		SequenceUtil.assertExpected(BeanOnBeanWiring.class, BeanWiring.class);
	}

	public void testWiringDeeplyAndWithOrderAndCheckSequenceConstraint() {
		final BeanOnBeanWithOrder beanOnBean = new BeanOnBeanWithOrder();
		SequenceUtil.init();
		final WirePuller puller = Wire.instance(beanOnBean).andStart(context);
		final List<String> expected = Literal.list("BeanWithOrder+schtonk").list("BeanWithOrder+stunk")
				.list("BeanWithOrder+done").list("BeanOnBeanWithOrder+stunk").list("BeanOnBeanWithOrder+schtonk")
				.list("BeanOnBeanWithOrder+done");
		SequenceUtil.assertExpected(expected);
		assertTrue(beanOnBean.hasSchtonk0());
		assertTrue(beanOnBean.hasStunk0());
		assertTrue(beanOnBean.hasSchtonk1());
		assertTrue(beanOnBean.hasStunk1());
		SequenceUtil.init();
		puller.stop();
		expected.remove("BeanWithOrder+done");
		expected.remove("BeanOnBeanWithOrder+done");
		Collections.reverse(expected);
		SequenceUtil.assertExpected(expected);
	}

	public void testWiringDeeply() {
		final NoWirableBean noWirableBean = new NoWirableBean();
		SequenceUtil.init();
		final WirePuller puller = Wire.instance(noWirableBean).andStart(context);
		SequenceUtil.assertExpected(BeanWiring.class);
		assertTrue(noWirableBean.hasSchtonk());
		puller.stop();
	}

	public void testWireMocking() {
		final Map<Class<?>, Class<? extends IWiring>> wiringMocks = new HashMap<Class<?>, Class<? extends IWiring>>();
		wiringMocks.put(Bean.class, BeanWiringMock.class);
		WirePuller.injectWiringMocks(wiringMocks);
		final Bean bean = new Bean();
		final WirePuller puller = Wire.instance(bean).andStart(context);
		assertTrue(bean.isSchtonkSchtonk());
		puller.stop();

		WirePuller.injectWiringMocks(null);
	}

	public void testAnnotatedServiceWiring() {
		final AnnoServiceBeanB beanB = new AnnoServiceBeanB();
		SequenceUtil.init();
		final WirePuller puller = Wire.instance(beanB).andStart(context);
		SequenceUtil.assertExpected(AnnoServiceBeanA.class, AnnoServiceBeanB.class);
		SequenceUtil.init();
		puller.stop();
		SequenceUtil.assertExpected(AnnoServiceBeanB.class, AnnoServiceBeanA.class);
	}

	public void testAnnotatedExtensionWiring() {
		addPluginXml(WireTest.class, "plugin.xml");
		addPluginXml(WireTest.class, "plugin_ext.xml");

		try {
			final AnnoExtBeanB beanB = new AnnoExtBeanB();
			SequenceUtil.init();
			final WirePuller puller = Wire.instance(beanB).andStart(context);
			SequenceUtil.assertExpected("textA", "infoB");
			SequenceUtil.init();
			puller.stop();
			SequenceUtil.assertExpected("-IDataB", "-IDataA");
		} finally {
			removeExtension("core.test.extpoint.idA");
			removeExtension("core.test.extpoint.idB");
		}
	}
}
