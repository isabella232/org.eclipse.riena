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
package org.eclipse.riena.ui.swt.facades.internal;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.ui.swt.component.SubApplicationItem;
import org.eclipse.riena.navigation.ui.swt.component.SubApplicationSwitcherWidget;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Test of the class {@link SubApplicationToolTip}.
 */
@UITestCase
public class SubApplicationToolTipTest extends TestCase {

	private SubApplicationSwitcherWidget switcher;
	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		shell = new Shell();
		final IApplicationNode node = new ApplicationNode();
		switcher = new SubApplicationSwitcherWidget(shell, SWT.NONE, node);
	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.dispose(switcher);
		SwtUtilities.dispose(shell);
	}

	/**
	 * Tests the method {@code getToolTipText(Event)}.
	 */
	public void testGetToolTipText() {

		final SubApplicationNode node = new SubApplicationNode();
		node.setToolTipText("hello, this is a tip"); //$NON-NLS-1$
		final SubApplicationItem item = new SubApplicationItem(switcher, node);
		item.setBounds(new Rectangle(0, 0, 10, 10));
		final List<SubApplicationItem> items = ReflectionUtils.invokeHidden(switcher, "getItems", (Object[]) null); //$NON-NLS-1$
		items.add(item);

		final MyToolTip toolTip = new MyToolTip(switcher);

		final Event event = new Event();
		event.x = 5;
		event.y = 5;
		assertTrue(toolTip.shouldCreateToolTip(event));
		String text = toolTip.getToolTipText(event);
		assertEquals(node.getToolTipText(), text);

		event.x = 500;
		event.y = 500;
		assertFalse(toolTip.shouldCreateToolTip(event));
		text = toolTip.getToolTipText(event);
		assertNull(text);

	}

	private class MyToolTip extends SubApplicationToolTip {

		public MyToolTip(final SubApplicationSwitcherWidget control) {
			super(control);
		}

		@Override
		public String getToolTipText(final Event event) {
			return super.getToolTipText(event);
		}

		@Override
		public boolean shouldCreateToolTip(final Event event) {
			return super.shouldCreateToolTip(event);
		}
	}

}
