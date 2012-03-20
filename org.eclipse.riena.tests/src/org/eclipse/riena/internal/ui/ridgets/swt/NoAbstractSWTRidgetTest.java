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

import java.math.BigInteger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.ridgets.IRidget;

/**
 * Tests for (none ridget dependent) methods of the class
 * {@link AbstractSwtRidget}.
 */
public class NoAbstractSWTRidgetTest extends AbstractSWTRidgetTest {

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetTest#createRidget()
	 */
	@Override
	protected IRidget createRidget() {
		return new LabelRidget();
	}

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetTest#createWidget(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createWidget(final Composite parent) {
		return new Label(parent, SWT.NONE);
	}

	/**
	 * Tests the method {@code hasChanged(Object, Object)}.
	 */
	public void testHasChanged() {

		boolean changed = ReflectionUtils.invokeHidden(getRidget(), "hasChanged", new Object[] { "1", "2" });
		assertTrue(changed);

		changed = ReflectionUtils.invokeHidden(getRidget(), "hasChanged", new Object[] { "1", "1" });
		assertFalse(changed);

		changed = ReflectionUtils.invokeHidden(getRidget(), "hasChanged", new Object[] { null, "1" });
		assertTrue(changed);

		changed = ReflectionUtils.invokeHidden(getRidget(), "hasChanged", new Object[] { "2", null });
		assertTrue(changed);

		changed = ReflectionUtils.invokeHidden(getRidget(), "hasChanged", new Object[] { "2", new BigInteger("2") });
		assertTrue(changed);

		changed = ReflectionUtils.invokeHidden(getRidget(), "hasChanged", new Object[] { new BigInteger("2"),
				new BigInteger("2") });
		assertFalse(changed);

	}

}
