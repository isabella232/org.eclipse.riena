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
package org.eclipse.riena.ui.ridgets.swt;

import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.internal.ui.ridgets.swt.TextRidget;
import org.eclipse.riena.ui.swt.BorderControlDecoration;
import org.eclipse.riena.ui.swt.lnf.ColorLnfResource;
import org.eclipse.riena.ui.swt.lnf.ILnfResource;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link BorderMarkerSupport}.
 */
@UITestCase
public class BorderMarkerSupportTest extends TestCase {

	private Display display;
	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		display = Display.getDefault();
		shell = new Shell(display);
	}

	@Override
	protected void tearDown() {
		SwtUtilities.dispose(shell);
	}

	/**
	 * Tests the <i>private</i> method {@code createErrorDecoration}.
	 */
	public void testCreateErrorDecoration() {

		final RienaDefaultLnf originalLnf = LnfManager.getLnf();
		final DefaultRealm realm = new DefaultRealm();
		try {
			final Text text = new Text(shell, SWT.NONE);
			final TextRidget ridget = new TextRidget();
			ridget.setUIControl(text);
			BorderMarkerSupport support = new BorderMarkerSupport(ridget, null);

			LnfManager.setLnf(new MyLnf());
			BorderControlDecoration deco = ReflectionUtils.invokeHidden(support, "createErrorDecoration", text);
			assertEquals(3, deco.getBorderWidth());
			assertNotNull(deco.getBorderColor());

			LnfManager.setLnf(new MyNonsenseLnf());

			deco = ReflectionUtils.invokeHidden(support, "createErrorDecoration", text);
			assertEquals(1, deco.getBorderWidth());
			assertNull(deco.getBorderColor());

			support = null;
			SwtUtilities.dispose(text);
		} finally {
			LnfManager.setLnf(originalLnf);
			realm.dispose();
		}

	}

	/**
	 * Look and Feel with correct setting.
	 */
	private static class MyLnf extends RienaDefaultLnf {
		@Override
		protected void initializeTheme() {
			super.initializeTheme();
			putLnfResource(LnfKeyConstants.ERROR_MARKER_BORDER_COLOR, new ColorLnfResource(0, 250, 0));
			putLnfSetting(LnfKeyConstants.ERROR_MARKER_BORDER_WIDTH, 3);
		}
	}

	/**
	 * Look and Feel with invalid setting: no settings and no colors
	 */
	private static class MyNonsenseLnf extends RienaDefaultLnf {
		@Override
		protected void initializeTheme() {
			super.initializeTheme();
			final Map<String, ILnfResource> resourceTable = ReflectionUtils.getHidden(MyNonsenseLnf.this,
					"resourceTable");
			resourceTable.clear();
			final Map<String, Object> settingTable = ReflectionUtils.getHidden(MyNonsenseLnf.this, "settingTable");
			settingTable.clear();
		}
	}

}
