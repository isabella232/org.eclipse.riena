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
package org.eclipse.riena.navigation.ui.swt.views;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.ridgets.swt.uibinding.AbstractViewBindingDelegate;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingManager;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingPropertyLocator;
import org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper;

/**
 * Tests of the class {@link AbstractViewBindingDelegate}.
 */
@NonUITestCase
public class AbstractViewBindingDelegateTest extends TestCase {

	private AbstractViewBindingDelegate binding;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		binding = new MyViewBindingDelegate(null, null);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		binding = null;
	}

	/**
	 * Test of the constructor of {@code SWTViewBindingDelegate}.
	 */
	public void testSWTViewBindingDelegate() {

		final IBindingManager bindingManager = ReflectionUtils.getHidden(binding, "bindingManager");
		assertNotNull(bindingManager);

		final List<Object> uiControls = ReflectionUtils.getHidden(binding, "uiControls");
		assertNotNull(uiControls);
		assertTrue(uiControls.isEmpty());

	}

	private static class MyViewBindingDelegate extends AbstractViewBindingDelegate {

		/**
		 * @param propertyStrategy
		 * @param mapper
		 */
		public MyViewBindingDelegate(final IBindingPropertyLocator propertyStrategy,
				final IControlRidgetMapper<Object> mapper) {
			super(propertyStrategy, mapper);
		}

	}

}
