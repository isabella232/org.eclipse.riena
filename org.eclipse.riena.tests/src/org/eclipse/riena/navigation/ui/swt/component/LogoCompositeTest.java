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
package org.eclipse.riena.navigation.ui.swt.component;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.swt.lnf.ImageLnfResource;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.ImageStore;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link LogoComposite}.
 */
@UITestCase
public class LogoCompositeTest extends TestCase {

	private final static String ICON_ECLIPSE = "eclipse.gif";
	private LogoComposite logoComposite;
	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		shell = new Shell();
		shell.setLayout(new FormLayout());
		logoComposite = new LogoComposite(shell, SWT.NONE);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		SwtUtilities.dispose(logoComposite);
		SwtUtilities.dispose(shell);
	}

	/**
	 * Tests the <i>private</i> method {@code getLogoImage()}.
	 */
	public void testGetLogoImage() {
		final RienaDefaultLnf originaLnf = LnfManager.getLnf();

		try {
			final MyLnf lnf = new MyLnf();
			LnfManager.setLnf(lnf);
			lnf.initialize();

			final Image eclipseImage = ImageStore.getInstance().getImage(ICON_ECLIPSE);
			lnf.setLogo(ICON_ECLIPSE);
			Image logoImage = ReflectionUtils.invokeHidden(logoComposite, "getLogoImage");
			assertNotNull(logoImage);
			assertEquals(eclipseImage.getBounds().width, logoImage.getBounds().width);
			assertEquals(eclipseImage.getBounds().height, logoImage.getBounds().height);

			final Image missingImage = ImageStore.getInstance().getMissingImage();
			lnf.setLogo(ICON_ECLIPSE + "4711");
			logoImage = ReflectionUtils.invokeHidden(logoComposite, "getLogoImage");
			assertNotNull(logoImage);
			assertEquals(missingImage.getBounds().width, logoImage.getBounds().width);
			assertEquals(missingImage.getBounds().height, logoImage.getBounds().height);
		} finally {
			LnfManager.setLnf(originaLnf);
		}
	}

	/**
	 * Look and Feel where it is possible to change the image of the logo.
	 */
	private static class MyLnf extends RienaDefaultLnf {

		public void setLogo(final String logo) {
			putLnfResource(LnfKeyConstants.TITLELESS_SHELL_LOGO, new ImageLnfResource(logo));
		}

	}

}
