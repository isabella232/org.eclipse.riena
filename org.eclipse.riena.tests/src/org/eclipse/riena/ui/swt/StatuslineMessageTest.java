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
package org.eclipse.riena.ui.swt;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link StatuslineMessage}.
 */
@UITestCase
public class StatuslineMessageTest extends TestCase {

	private Shell shell;
	private StatuslineMessage statusMessage;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		shell = new Shell();
		statusMessage = new StatuslineMessage(shell, SWT.NONE);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.dispose(shell);
		SwtUtilities.dispose(statusMessage);
	}

	/**
	 * Tests the method {@code createContents()}.<br>
	 * <i>The method is already called by the constructor.</i>
	 */
	public void testCreateContents() {

		final Control[] controls = statusMessage.getChildren();
		assertEquals(1, controls.length);
		assertTrue(controls[0] instanceof CLabel);

	}

	/**
	 * Tests the method {@code setMessage()}.
	 */
	public void testSetMessage() {

		statusMessage.setMessage("Hello!");

		final Control[] controls = statusMessage.getChildren();
		final CLabel label = (CLabel) controls[0];
		assertEquals("Hello!", label.getText());

	}

}
