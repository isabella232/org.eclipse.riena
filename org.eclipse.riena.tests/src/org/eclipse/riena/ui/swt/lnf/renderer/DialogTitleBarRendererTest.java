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

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.ui.swt.lnf.FontLnfResource;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.ImageStore;

/**
 * Tests of the class {@link DialogTitleBarRenderer}.
 */
@UITestCase
public class DialogTitleBarRendererTest extends RienaTestCase {

	private final static String ICON_ECLIPSE = "eclipse.gif";

	private OpenDialogTitleBarRenderer renderer;
	private Shell shell;
	private GC gc;
	private RienaDefaultLnf originalLnf;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		shell = new Shell();
		gc = new GC(shell);
		renderer = new OpenDialogTitleBarRenderer();
		renderer.setShell(shell);
		renderer.setBounds(0, 0, 100, 100);
		originalLnf = LnfManager.getLnf();
	}

	@Override
	protected void tearDown() throws Exception {
		renderer.dispose();
		shell.dispose();
		LnfManager.setLnf(originalLnf);

		super.tearDown();
	}

	/**
	 * Tests the method {@code paintTitle}.
	 */
	public void testPaintTitle() {

		final DialogLnf lnf = new DialogLnf();
		LnfManager.setLnf(lnf);
		lnf.setHideOsBorder(true);
		shell.setText("Hello");

		Rectangle bounds = renderer.paintTitle(gc);
		assertFalse(bounds.equals(new Rectangle(0, 0, 0, 0)));

		shell.setText("");
		bounds = renderer.paintTitle(gc);
		assertEquals(new Rectangle(0, 0, 0, 0), bounds);

		shell.setText("Hello");
		lnf.setHideOsBorder(false);
		bounds = renderer.paintTitle(gc);
		assertEquals(new Rectangle(0, 0, 0, 0), bounds);

	}

	/**
	 * Tests the method {@code paintImage}.
	 */
	public void testPaintImage() {

		final DialogLnf lnf = new DialogLnf();
		LnfManager.setLnf(lnf);
		lnf.setHideOsBorder(true);
		final Image image = ImageStore.getInstance().getImage(ICON_ECLIPSE);

		if (Activator.getDefault() != null) { // osgi running?
			assertNotNull(image);

			shell.setImage(image);

			final Rectangle bounds = renderer.paintImage(gc);
			assertFalse(bounds.equals(new Rectangle(0, 0, 0, 0)));
			assertEquals(image.getBounds().width, bounds.width);
			assertEquals(image.getBounds().height, bounds.height);
		}

		lnf.setHideOsBorder(false);
		Rectangle bounds = renderer.paintImage(gc);
		assertEquals(new Rectangle(0, 0, 0, 0), bounds);

		lnf.setHideOsBorder(true);
		shell.setImage(null);
		bounds = renderer.paintImage(gc);
		assertEquals(new Rectangle(0, 0, 0, 0), bounds);

	}

	/**
	 * This look and feel sets the font of the dialog and some setting can be
	 * changed for testing.
	 */
	private static class DialogLnf extends RienaDefaultLnf {

		@Override
		protected void initializeTheme() {
			super.initializeTheme();
			putLnfResource(LnfKeyConstants.DIALOG_FONT, new FontLnfResource("Arial", 12, SWT.NONE));
		}

		public void setHideOsBorder(final boolean hide) {
			final Map<String, Object> settingTable = ReflectionUtils.getHidden(DialogLnf.this, "settingTable");
			settingTable.put(LnfKeyConstants.DIALOG_HIDE_OS_BORDER, hide);
		}

	}

	/**
	 * This class changes the visibility of some method for testing and
	 * implements the abstract methods.
	 */
	private static class OpenDialogTitleBarRenderer extends DialogTitleBarRenderer {

		@Override
		public Rectangle paintTitle(final GC gc) {
			return super.paintTitle(gc);
		}

		@Override
		public Rectangle paintImage(final GC gc) {
			return super.paintImage(gc);
		}

	}

}
