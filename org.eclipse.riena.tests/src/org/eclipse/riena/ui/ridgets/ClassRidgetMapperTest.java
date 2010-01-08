/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets;

import java.util.Map;

import org.eclipse.core.databinding.BindingException;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.ui.ridgets.swt.TextRidget;
import org.eclipse.riena.navigation.ui.controllers.NavigationNodeControllerTest.MockRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;

/**
 *
 */
public class ClassRidgetMapperTest extends RienaTestCase {
	private ClassRidgetMapper mapper;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// only used to get the initial mappings
		// Create new instance of SwtControlRidgetMapper to start with initial mappings only (not additional mappings added in previous test cases)
		ReflectionUtils.setHidden(SwtControlRidgetMapper.class, "instance", ReflectionUtils.newInstanceHidden(
				SwtControlRidgetMapper.class, new Object[0]));

		mapper = ClassRidgetMapper.getInstance();
	}

	@Override
	protected void tearDown() throws Exception {

		// Create new instance of ClassRidgetMapper to start with initial mappings only (not additional mappings added in previous test cases)
		ReflectionUtils.setHidden(ClassRidgetMapper.class, "instance", ReflectionUtils.newInstanceHidden(
				ClassRidgetMapper.class, new Object[0]));
		mapper = null;

		super.tearDown();
	}

	public void testAddMapping() throws Exception {
		mapper.addMapping(IMockRidget.class, MockRidget.class);

		Class<? extends IRidget> ridget = mapper.getRidgetClass(IMockRidget.class);
		assertNotNull(ridget);
		assertEquals(MockRidget.class.getName(), ridget.getName());

		Map<Class<? extends IRidget>, Class<? extends IRidget>> mappings = ReflectionUtils
				.getHidden(mapper, "mappings");
		int size = mappings.size();
		mapper.addMapping(null, null);
		assertEquals(size, mappings.size());
	}

	public void testGetRidgetClass() throws Exception {

		Class<? extends IRidget> ridget = mapper.getRidgetClass(ITextRidget.class);
		assertNotNull(ridget);
		assertEquals(TextRidget.class.getName(), ridget.getName());

		try {
			mapper.getRidgetClass(IMockRidget.class);
			fail("BindingException expected");
		} catch (BindingException e) {
			ok("BindingException expected");
		}

	}

	/**
	 * Mock interface extending IRidget.
	 */
	static interface IMockRidget extends IRidget {

	}

	/**
	 * Mock interface extending IRidget.
	 */
	static interface IMockRidget2 extends IMockRidget {

	}

	static interface IMockRidget3 extends IMockRidget {

	}

}
