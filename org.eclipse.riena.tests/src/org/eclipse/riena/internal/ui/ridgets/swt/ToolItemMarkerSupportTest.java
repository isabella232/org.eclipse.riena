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

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link ToolItemMarkerSupport}
 */
@UITestCase
public class ToolItemMarkerSupportTest extends TestCase {

	private Shell shell;
	private ToolBar toolbar;

	@Override
	protected void setUp() throws Exception {
		shell = new Shell();
		toolbar = new ToolBar(shell, SWT.NONE);
	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.dispose(shell);
		SwtUtilities.dispose(toolbar);
	}

	/**
	 * Tests the method {@code updateMarkers()}.
	 */
	public void testUpdateMarkers() {

		final ToolItem item = new ToolItem(toolbar, SWT.NONE);
		final ToolItemRidget ridget = new ToolItemRidget();

		final ToolItemMarkerSupport markerSupport = new ToolItemMarkerSupport(ridget, null);
		ridget.setEnabled(false);
		assertTrue(item.isEnabled());

		ridget.setUIControl(item);
		markerSupport.updateMarkers();
		assertFalse(item.isEnabled());

	}

	/**
	 * Tests the <i>private</i> method {@code updateDisabled}.
	 */
	public void testUpdateDisabled() {

		final ToolItem item = new ToolItem(toolbar, SWT.NONE);
		final ToolItemRidget ridget = new ToolItemRidget();
		final ToolItemMarkerSupport markerSupport = new ToolItemMarkerSupport(ridget, null);

		ridget.setEnabled(false);
		ReflectionUtils.invokeHidden(markerSupport, "updateDisabled", item);
		assertFalse(item.isEnabled());

		ridget.setEnabled(true);
		ReflectionUtils.invokeHidden(markerSupport, "updateDisabled", item);
		assertTrue(item.isEnabled());

		item.dispose();
		ReflectionUtils.invokeHidden(markerSupport, "updateDisabled", item);

	}

}
