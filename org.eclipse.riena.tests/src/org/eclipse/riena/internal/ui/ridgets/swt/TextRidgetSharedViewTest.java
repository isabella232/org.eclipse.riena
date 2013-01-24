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

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 * Test for the MarkerSupport in a shared view. When a view is shared several
 * Ridgets share the same widget.
 */
@UITestCase
public class TextRidgetSharedViewTest extends RienaTestCase {

	private Shell shell;
	private Text widget;
	private ITextRidget textRidget1;
	private ITextRidget textRidget2;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		final Display display = Display.getDefault();

		final Realm realm = SWTObservables.getRealm(display);
		assertNotNull(realm);
		ReflectionUtils.invokeHidden(realm, "setDefault", realm);

		shell = new Shell(SWT.SYSTEM_MODAL | SWT.ON_TOP);
		shell.setLayout(new RowLayout(SWT.VERTICAL));

		widget = new Text(shell, SWT.NONE);

		textRidget1 = new TextRidget();
		textRidget1.setUIControl(widget);

		textRidget2 = new TextRidget();

		shell.setSize(130, 100);
		shell.setLocation(0, 0);
		shell.open();
	}

	@Override
	protected void tearDown() throws Exception {
		textRidget1 = null;
		textRidget2 = null;
		widget = null;
		shell.dispose();
		shell = null;

		super.tearDown();
	}

	/**
	 * Test for bug 269349.
	 */
	public void testMandatoryMarkersInSharedViews() throws Exception {

		final Color normalBackground = widget.getBackground();

		textRidget1.setMandatory(true);
		textRidget2.setMandatory(true);

		final Color mandatoryBackground = widget.getBackground();
		assertFalse(normalBackground.equals(mandatoryBackground));

		assertEquals(mandatoryBackground, widget.getBackground());

		textRidget1.setUIControl(null);
		textRidget2.setUIControl(widget);

		assertEquals(mandatoryBackground, widget.getBackground());

		textRidget2.setText("Text");

		assertEquals(normalBackground, widget.getBackground());

		textRidget2.setUIControl(null);
		textRidget1.setUIControl(widget);

		assertEquals(mandatoryBackground, widget.getBackground());

		textRidget1.setText("Text");

		assertEquals(normalBackground, widget.getBackground());
	}

}
