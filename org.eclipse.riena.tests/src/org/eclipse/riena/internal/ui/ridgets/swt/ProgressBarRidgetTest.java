/*******************************************************************************
 * Copyright (c) 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *    Florian Pirchner - added tests
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.ridgets.ITraverseRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;

/**
 * Tests for the class {@link ProgressBarRidget}.
 */
@UITestCase
public class ProgressBarRidgetTest extends AbstractSWTRidgetTest {

	@Override
	protected Widget createWidget(final Composite parent) {
		return new ProgressBar(parent, SWT.NONE);
	}

	@Override
	protected ITraverseRidget createRidget() {
		return new MyProgressBarRidget();
	}

	@Override
	protected ProgressBar getWidget() {
		return (ProgressBar) super.getWidget();
	}

	// test methods
	// ////////////

	public void testRidgetMapping() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(ProgressBarRidget.class, mapper.getRidgetClass(getWidget()));
	}

	/**
	 * Tests the method {@code initFromUIControl()}.
	 */
	public void testInitFromUIControl() {

		final MyProgressBarRidget ridget = (MyProgressBarRidget) getRidget();
		final ProgressBar widget = getWidget();

		assertEquals(widget.getMaximum(), ridget.getMaximum());
		assertEquals(widget.getMinimum(), ridget.getMinimum());
		assertEquals(widget.getSelection(), ridget.getValue());

		widget.setMaximum(333);
		widget.setMinimum(22);
		widget.setSelection(55);
		ReflectionUtils.setHidden(ridget, "initialized", false);
		ReflectionUtils.setHidden(ridget, "maxInitialized", false);
		ReflectionUtils.setHidden(ridget, "minInitialized", false);
		ridget.initFromUIControl();
		assertEquals(333, ridget.getMaximum());
		assertEquals(22, ridget.getMinimum());
		assertEquals(55, ridget.getValue());

	}

	/**
	 * This class changes the visibility of some protected methods for testing.
	 */
	private class MyProgressBarRidget extends ProgressBarRidget {
		@Override
		public void initFromUIControl() {
			super.initFromUIControl();
		}
	}

}