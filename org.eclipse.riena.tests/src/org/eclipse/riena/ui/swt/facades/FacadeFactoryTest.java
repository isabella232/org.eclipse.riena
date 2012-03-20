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
package org.eclipse.riena.ui.swt.facades;

import org.eclipse.swt.SWT;

import org.eclipse.riena.core.util.InvocationTargetFailure;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Test the {@code FacadeFactory}.
 */
@NonUITestCase
public class FacadeFactoryTest extends RienaTestCase {

	public void testBadCast() throws Throwable {
		SWT.getPlatform();
		try {
			newFacade(TestBadCastFacade.class);
			fail();
		} catch (final RuntimeException e) {
			assertTrue(e.getCause() instanceof ClassCastException);
		}
	}

	public void testInstantiationBug() throws Throwable {
		try {
			newFacade(TestInstantiationBugFacade.class);
			fail();
		} catch (final RuntimeException e) {
			assertTrue(e.getCause() instanceof NullPointerException);
		}
	}

	public void testSpecificFacadeNotFound() throws Throwable {
		try {
			newFacade(FacadeFactoryTest.class);
			fail();
		} catch (final RuntimeException e) {
			assertTrue(e.getCause() instanceof ClassNotFoundException);
		}
	}

	public void testSomethingGood() throws Throwable {
		final GCFacade gcFacade = (GCFacade) newFacade(GCFacade.class);
		assertTrue(gcFacade.getClass() != GCFacade.class);
		assertTrue(gcFacade instanceof GCFacade);
	}

	// helping methods
	//////////////////

	private Object newFacade(final Class<?> clazz) throws Throwable {
		try {
			return ReflectionUtils.invokeHidden(FacadeFactory.class, "newFacade", new Object[] { clazz });
		} catch (final InvocationTargetFailure itaf) {
			throw itaf.getCause();
		}
	}
}
