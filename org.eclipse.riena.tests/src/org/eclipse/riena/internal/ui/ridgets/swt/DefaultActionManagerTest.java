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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.internal.ui.swt.test.UITestHelper;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.IWindowRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;

/**
 * Tests for {@link DefaultActionManager}.
 */
@UITestCase
public class DefaultActionManagerTest extends TestCase {

	private Display display;

	private Shell shell;
	private Text text1;
	private Text text2;
	private Button button1;
	private Button button2;

	private IWindowRidget rShell;
	private ITextRidget rText1;
	private ITextRidget rText2;
	private IActionRidget rButton1;
	private IActionRidget rButton2;

	private DefaultActionManager actionMan;

	@Override
	protected void setUp() throws Exception {
		display = Display.getDefault();
		shell = createUI(display);
		createRidgets();
		actionMan = new DefaultActionManager(rShell);
	}

	@Override
	protected void tearDown() throws Exception {
		actionMan.dispose();
		shell.dispose();
	}

	public void testOneDefaultButton() {
		assertNull(shell.getDefaultButton());

		actionMan.addAction(rText2, rButton2);
		actionMan.activate();
		shell.open();
		UITestHelper.readAndDispatch(shell);

		assertNull(shell.getDefaultButton());

		rText2.requestFocus();
		UITestHelper.readAndDispatch(shell);

		assertEquals(button2, shell.getDefaultButton());

		rText1.requestFocus();
		UITestHelper.readAndDispatch(shell);

		assertEquals(null, shell.getDefaultButton());
	}

	public void testUnboundRidgets() {
		actionMan.addAction(rShell, rButton1);
		rText2.setUIControl(null);
		actionMan.addAction(rText2, rButton2);
		actionMan.activate();

	}

	public void testNestedDefaultButtons() {
		assertNull(shell.getDefaultButton());

		actionMan.addAction(rShell, rButton1);
		actionMan.addAction(rText2, rButton2);
		actionMan.activate();
		shell.open();
		UITestHelper.readAndDispatch(shell);

		assertEquals(button1, shell.getDefaultButton());

		rText2.requestFocus();
		UITestHelper.readAndDispatch(shell);

		assertEquals(button2, shell.getDefaultButton());

		rText1.requestFocus();
		UITestHelper.readAndDispatch(shell);

		assertEquals(button1, shell.getDefaultButton());
	}

	public void testDeactivateActivate() {
		actionMan.addAction(rText1, rButton1);
		actionMan.activate();
		shell.open();
		UITestHelper.readAndDispatch(shell);

		assertEquals(button1, shell.getDefaultButton());

		actionMan.deactivate();

		assertNull(shell.getDefaultButton());

		actionMan.activate();

		assertEquals(button1, shell.getDefaultButton());
	}

	public void testDispose() {
		actionMan.addAction(rText1, rButton1);
		actionMan.activate();
		shell.open();
		UITestHelper.readAndDispatch(shell);

		assertEquals(button1, shell.getDefaultButton());

		actionMan.dispose();

		assertNull(shell.getDefaultButton());

		rText2.requestFocus();
		UITestHelper.readAndDispatch(shell);

		assertNull(shell.getDefaultButton());

		rText1.requestFocus();
		UITestHelper.readAndDispatch(shell);

		assertNull(shell.getDefaultButton());
	}

	// helping methods
	//////////////////

	private Shell createUI(final Display display) {
		final Shell result = new Shell(display, SWT.ON_TOP);
		GridLayoutFactory.fillDefaults().numColumns(2).applyTo(result);

		text1 = new Text(result, SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(text1);
		button1 = new Button(result, SWT.PUSH);
		button1.setText("button1");
		GridDataFactory.fillDefaults().applyTo(button1);

		text2 = new Text(result, SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(text2);
		button2 = new Button(result, SWT.PUSH);
		button2.setText("button2");
		GridDataFactory.fillDefaults().applyTo(button2);

		result.setBounds(0, 0, 200, 200);
		result.layout();
		return result;
	}

	private void createRidgets() {
		rShell = (IWindowRidget) SwtRidgetFactory.createRidget(shell);
		rText1 = (ITextRidget) SwtRidgetFactory.createRidget(text1);
		rText1.setText("text1");
		rText2 = (ITextRidget) SwtRidgetFactory.createRidget(text2);
		rText2.setText("text2");
		rButton1 = (IActionRidget) SwtRidgetFactory.createRidget(button1);
		rButton2 = (IActionRidget) SwtRidgetFactory.createRidget(button2);
	}

}
