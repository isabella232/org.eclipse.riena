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

	/**
	 * Tests of the method <code>computeSize(GC, int, int) </code>
	 */
	public void testComputeSize() {
		final EmbeddedTitlebarRenderer renderer = new EmbeddedTitlebarRenderer();
		final Shell shell = new Shell();
		final GC gc = new GC(shell);

		try {
			final int wHint = 12;
			final int hHint = 24;
			final Point size = renderer.computeSize(gc, wHint, hHint);

			assertEquals(wHint, size.x);

			gc.setFont(LnfManager.getLnf().getFont(LnfKeyConstants.EMBEDDED_TITLEBAR_FONT));
			final int expectedHeight = gc.getFontMetrics().getHeight() + 8;

			assertEquals(expectedHeight, size.y);
		} finally {
			gc.dispose();
			shell.dispose();
		}
	}

}
