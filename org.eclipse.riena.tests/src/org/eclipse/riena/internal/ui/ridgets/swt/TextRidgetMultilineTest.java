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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.beans.common.TestBean;
import org.eclipse.riena.internal.ui.swt.test.UITestHelper;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;

public class TextRidgetMultilineTest extends AbstractSWTRidgetTest {

	private TestBean bean;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		bean = new TestBean();
		final Shell shell = getShell();
		shell.layout();
	}

	@Override
	protected IRidget createRidget() {
		return new TextRidget();
	}

	@Override
	protected Control createWidget(final Composite parent) {
		return new Text(getShell(), SWT.BORDER | SWT.MULTI);
	}

	@Override
	protected ITextRidget getRidget() {
		return (ITextRidget) super.getRidget();
	}

	@Override
	protected Text getWidget() {
		return (Text) super.getWidget();
	}

	/**
	 * In this test, we have a multiline text field as created in {@link #createWidget(Composite)}. So the flag <tt>multilineIgnoreEnterKey</tt> should be
	 * always ignored.
	 */
	public void testMultilineIgnoreEnterKey() throws Exception {
		// default is multilineIgnoreEnterKey=false
		final Text control = getWidget();
		assertTrue("This test case expects a multiline text field.", (control.getStyle() & SWT.MULTI) != 0);

		final ITextRidget ridget = getRidget();
		ridget.bindToModel(bean, TestBean.PROPERTY);
		control.setFocus();

		assertEquals(null, bean.getProperty());
		UITestHelper.sendString(control.getDisplay(), "1\r");
		assertEquals("1\r\n", bean.getProperty());

		// now setting the flag, which must be ignored
		ridget.setMultilineIgnoreEnterKey(true);
		assertEquals("1\r\n", bean.getProperty());
		UITestHelper.sendString(control.getDisplay(), "2\r");
		assertEquals("1\r\n", bean.getProperty());

		// now set the flag to false again
		ridget.setMultilineIgnoreEnterKey(false);
		assertEquals("1\r\n", bean.getProperty());
		UITestHelper.sendString(control.getDisplay(), "2\r");
		assertEquals("1\r\n2\r\n2\r\n", bean.getProperty());
	}
}
