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
package org.eclipse.riena.ui.swt.lnf.renderer;

import junit.framework.TestCase;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * Tests of the class {@link EmbeddedTitlebarRenderer}.
 */
@UITestCase
public class EmbeddedTitlebarRendererTest extends TestCase {
	private EmbeddedTitlebarRenderer renderer;
	private Shell shell;
	private GC gc;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		renderer = new EmbeddedTitlebarRenderer();
		renderer.setBounds(0, 0, 100, 24);
		shell = new Shell();
		gc = new GC(shell);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		gc.dispose();
		shell.dispose();
		renderer.dispose();
		super.tearDown();
	}

	/**
	 * Tests of the method <code>computeSize(GC, int, int) </code>
	 */
	public void testComputeSize() {
		final int wHint = 12;
		final int hHint = 24;
		final Point size = renderer.computeSize(gc, wHint, hHint);

		assertEquals(wHint, size.x);

		gc.setFont(LnfManager.getLnf().getFont(LnfKeyConstants.EMBEDDED_TITLEBAR_FONT));
		final int expectedHeight = gc.getFontMetrics().getHeight() + 8;

		assertEquals(expectedHeight, size.y);
	}

	public void testComputeTextBoundsOnlyText() throws Exception {
		renderer.setImage(null);
		renderer.setCloseable(false);
		renderer.setBlocked(false);

		System.out.println(renderer.computeTextBounds(gc));

		renderer.setCloseable(true);
		System.out.println(renderer.computeTextBounds(gc));
	}

}
