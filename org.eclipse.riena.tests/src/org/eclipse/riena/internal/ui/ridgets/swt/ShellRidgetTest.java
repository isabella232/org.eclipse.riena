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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.ridgets.IRidget;

/**
 * Tests of the class {@link ShellRidget}.
 */
@UITestCase
public class ShellRidgetTest extends AbstractSWTRidgetTest {

	@Override
	protected IRidget createRidget() {
		return new MockShellRidget();
	}

	@Override
	protected Widget createWidget(final Composite parent) {
		return getShell();
	}

	@Override
	protected MockShellRidget getRidget() {
		return (MockShellRidget) super.getRidget();
	}

	@Override
	protected Shell getWidget() {
		return (Shell) super.getWidget();
	}

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetTest#testGetFocusable()
	 */
	@Override
	public void testGetFocusable() {

		assertFalse(getRidget().isFocusable());

		getRidget().setFocusable(true);

		assertFalse(getRidget().isFocusable());
	}

	/**
	 * Tests the method {@code hasChanged}.
	 */
	public void testHasChanged() {

		assertTrue(getRidget().hasChanged("a", "b"));
		assertFalse(getRidget().hasChanged("a", "a"));
		assertTrue(getRidget().hasChanged(null, "b"));
		assertTrue(getRidget().hasChanged("a", null));
		assertFalse(getRidget().hasChanged(null, null));
	}

	public void testSetActive() throws Exception {

		getRidget().setActive(false);

		assertFalse(getRidget().isEnabled());
		assertFalse(getWidget().isEnabled());

		getRidget().setActive(true);

		assertTrue(getRidget().isEnabled());
		assertTrue(getWidget().isEnabled());
	}

	/**
	 * This class reduces the visibility of some protected method for testing.
	 */
	private static class MockShellRidget extends ShellRidget {

		@Override
		public boolean hasChanged(final Object oldValue, final Object newValue) {
			return super.hasChanged(oldValue, newValue);
		}

	}

}
