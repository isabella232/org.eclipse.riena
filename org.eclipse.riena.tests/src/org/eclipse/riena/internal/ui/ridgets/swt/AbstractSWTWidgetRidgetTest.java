/*******************************************************************************
 * Copyright (c) 2007, 2014 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import junit.framework.TestCase;

import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.ridgets.swt.AbstractSWTWidgetRidget;

/**
 * Tests of the class {@link AbstractSWTWidgetRidget}.
 */
@NonUITestCase
public class AbstractSWTWidgetRidgetTest extends TestCase {

	/**
	 * Tests the method {@code hasChanged}.
	 */
	public void testHasChanged() {

		final MockRidget ridget = new MockRidget();

		assertTrue(ridget.hasChanged("a", "b"));
		assertFalse(ridget.hasChanged("a", "a"));
		assertTrue(ridget.hasChanged(null, "b"));
		assertTrue(ridget.hasChanged("a", null));
		assertFalse(ridget.hasChanged(null, null));

	}

	private static class MockRidget extends AbstractSWTWidgetRidget {

		@Override
		protected void bindUIControl() {
		}

		@Override
		protected void checkUIControl(final Object uiControl) {
		}

		@Override
		public boolean isDisableMandatoryMarker() {
			return false;
		}

		@Override
		protected void unbindUIControl() {
		}

		@Override
		protected void updateEnabled() {
		}

		@Override
		protected void updateToolTip() {
		}

		@Override
		public boolean hasChanged(final Object oldValue, final Object newValue) {
			return super.hasChanged(oldValue, newValue);
		}

		public boolean isVisible() {
			return false;
		}

	}

}
