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
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.nebula.widgets.compositetable.AbsoluteLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Test the class {@link BorderControlDecoration}.
 */
@UITestCase
public class BorderControlDecorationTest extends TestCase {

	private Display display;
	private Shell shell;
	private Text text;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		display = Display.getDefault();
		shell = new Shell(display);
		shell.setBounds(10, 10, 200, 100);
		shell.setLayout(new AbsoluteLayout());
		text = new Text(shell, SWT.BORDER);
		text.setLayoutData(new Rectangle(4, 6, 40, 20));
	}

	@Override
	protected void tearDown() {
		SwtUtilities.dispose(text);
		SwtUtilities.dispose(shell);
	}

	/**
	 * Tests the <i>private</i> method {@code shouldShowDecoration()}.
	 */
	public void testShouldShowDecoration() {

		BorderControlDecoration deco = new BorderControlDecoration(text, 2);
		boolean ret = ReflectionUtils.invokeHidden(deco, "shouldShowDecoration");
		assertFalse(ret);

		shell.setVisible(true);
		deco.show();
		ret = ReflectionUtils.invokeHidden(deco, "shouldShowDecoration");
		assertTrue(ret);

		deco.hide();
		ret = ReflectionUtils.invokeHidden(deco, "shouldShowDecoration");
		assertFalse(ret);

		deco.dispose();
		deco = new BorderControlDecoration(text, 0);
		deco.show();
		ret = ReflectionUtils.invokeHidden(deco, "shouldShowDecoration");
		assertFalse(ret);

		deco.dispose();
		shell.setVisible(false);

	}

	/**
	 * Tests the <i>private</i> method {@code getDecorationRectangle(Control)}.
	 */
	public void testGetDecorationRectangle() {

		shell.setVisible(true);

		final BorderControlDecoration deco = new BorderControlDecoration(text, 2);
		final Rectangle decoRect = ReflectionUtils.invokeHidden(deco, "getDecorationRectangle", shell);
		assertEquals(new Rectangle(2, 4, 43, 23), decoRect);

		shell.setVisible(false);

	}

}
