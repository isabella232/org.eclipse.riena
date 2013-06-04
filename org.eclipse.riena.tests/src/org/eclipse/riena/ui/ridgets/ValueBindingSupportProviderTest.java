/*******************************************************************************
 * Copyright (c) 2007, 2013 Florian Pirchner.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets;

import org.junit.Test;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonGatherableTestCase;
import org.eclipse.riena.internal.ui.ridgets.swt.TextRidget;
import org.eclipse.riena.ui.ridgets.swt.DefaultRealm;

/**
 * Tests for the ValueBindingSupport.
 */
// @NonUITestCase
@NonGatherableTestCase(" This test case works local but not on the build server!!!")
public class ValueBindingSupportProviderTest extends RienaTestCase {

	private DefaultRealm realm;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		realm = new DefaultRealm();
	}

	@Override
	protected void tearDown() throws Exception {
		realm.dispose();
		realm = null;
		super.tearDown();
	}

	@Test
	public void testGetValueBindingSupportNoExtension() throws Exception {
		final ValueBindingSupport bindingSupport = IValueBindingSupportProvider.ExtensionAccess.createInstance(ITextRidget.class, new WritableValue());
		assertNull(bindingSupport);
	}

	@Test
	public void testGetCustomValueBindingSupport() throws Exception {
		addPluginXml(ValueBindingSupportProviderTest.class, "bindingSupportExtension.xml"); //$NON-NLS-1$
		final ValueBindingSupport bindingSupport = IValueBindingSupportProvider.ExtensionAccess.createInstance(ITextRidget.class, new WritableValue());
		assertSame(CustomValueBindingSupport.class, bindingSupport.getClass());
		removeExtension("org.eclipse.riena.ui.ridgets.ValueBindingSupportProviderTest"); //$NON-NLS-1$
	}

	@Test
	public void testGetCustomValueBindingSupportThrowsException() throws Exception {
		addPluginXml(ValueBindingSupportProviderTest.class, "bindingSupportExtensionThrowsException.xml"); //$NON-NLS-1$
		final ValueBindingSupport bindingSupport = IValueBindingSupportProvider.ExtensionAccess.createInstance(ITextRidget.class, new WritableValue());
		assertNull(bindingSupport);
		removeExtension("org.eclipse.riena.ui.ridgets.ValueBindingSupportProviderTest"); //$NON-NLS-1$
	}

	@Test
	public void testAbstractValueRidget() throws Exception {

		// test case 1: Default support it used
		final TextRidget ridget = new TextRidget();
		assertSame(ValueBindingSupport.class, ridget.getValueBindingSupport().getClass());

		// test case 2: Custom support it used
		addPluginXml(ValueBindingSupportProviderTest.class, "bindingSupportExtension.xml"); //$NON-NLS-1$
		final TextRidget ridget2 = new TextRidget();
		assertSame(CustomValueBindingSupport.class, ridget2.getValueBindingSupport().getClass());
		removeExtension("org.eclipse.riena.ui.ridgets.ValueBindingSupportProviderTest"); //$NON-NLS-1$
	}

	/**
	 * Custom binding support provider for tests.
	 */
	public static class CustomValueBindingSupportProvider implements IValueBindingSupportProvider {

		public ValueBindingSupport createInstance(final Class<? extends IRidget> ridgetClass, final IObservableValue ridgetObservable) {
			return new CustomValueBindingSupport(ridgetObservable);
		}

	}

	/**
	 * Custom binding support for tests.
	 */
	public static class CustomValueBindingSupport extends ValueBindingSupport {

		/**
		 * @param target
		 */
		public CustomValueBindingSupport(final IObservableValue target) {
			super(target);
		}

		/**
		 * @param target
		 * @param model
		 */
		public CustomValueBindingSupport(final IObservableValue target, final IObservableValue model) {
			super(target, model);
		}
	}
}
