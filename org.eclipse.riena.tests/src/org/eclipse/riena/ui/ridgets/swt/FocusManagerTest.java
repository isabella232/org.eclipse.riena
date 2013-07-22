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

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.ui.ridgets.swt.CComboRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ComboRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.CompletionComboRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.TextRidget;
import org.eclipse.riena.ui.swt.facades.internal.CompletionComboRCP;

/**
 * Tests of the class {@link FocusManager}.
 */
@UITestCase
public class FocusManagerTest extends TestCase {

	private Shell shell;
	private DefaultRealm realm;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		realm = new DefaultRealm();
		shell = new Shell(SWT.SYSTEM_MODAL | SWT.ON_TOP);
		shell.setLayout(new RowLayout(SWT.VERTICAL));
		shell.setSize(130, 100);
		shell.setLocation(0, 0);
		shell.open();

	}

	@Override
	protected void tearDown() throws Exception {
		if (shell != null) {
			shell.dispose();
			shell = null;
		}
		if (realm != null) {
			realm.dispose();
			realm = null;
		}
		super.tearDown();
	}

	/**
	 * Tests the method {@code isFocusable()}.
	 */
	public void testIsFocusable() {

		final TextRidget textRidget = new TextRidget();
		textRidget.setUIControl(new Text(shell, SWT.BORDER));

		textRidget.setFocusable(true);
		boolean ret = ReflectionUtils.invokeHidden(textRidget.getFocusManager(), "isFocusable"); //$NON-NLS-1$
		assertTrue(ret);

		textRidget.setFocusable(false);
		ret = ReflectionUtils.invokeHidden(textRidget.getFocusManager(), "isFocusable"); //$NON-NLS-1$
		assertFalse(ret);

		textRidget.setFocusable(true);
		textRidget.setOutputOnly(true);
		ret = ReflectionUtils.invokeHidden(textRidget.getFocusManager(), "isFocusable"); //$NON-NLS-1$
		assertFalse(ret);

		final Event event = new Event();
		event.widget = textRidget.getUIControl();
		textRidget.getFocusManager().mouseDown(new MouseEvent(event));
		ret = ReflectionUtils.invokeHidden(textRidget.getFocusManager(), "isFocusable"); //$NON-NLS-1$
		assertTrue(ret);

	}

	/**
	 * Tests the method {@code showComboList(Widget)}.
	 */
	public void testUpdateShowComboListFlag() {

		// Combo
		final ComboRidget comboRidget = new ComboRidget();
		comboRidget.setUIControl(new Combo(shell, SWT.READ_ONLY));

		comboRidget.setFocusable(true);
		ReflectionUtils.invokeHidden(comboRidget.getFocusManager(),
				"updateShowComboListFlag", comboRidget.getUIControl()); //$NON-NLS-1$
		boolean showComboList = ReflectionUtils.getHidden(comboRidget.getFocusManager(), "showComboList"); //$NON-NLS-1$
		assertFalse(showComboList);

		comboRidget.setFocusable(false);
		ReflectionUtils.invokeHidden(comboRidget.getFocusManager(),
				"updateShowComboListFlag", comboRidget.getUIControl()); //$NON-NLS-1$
		showComboList = ReflectionUtils.getHidden(comboRidget.getFocusManager(), "showComboList"); //$NON-NLS-1$
		assertTrue(showComboList);

		// CCombo
		final CComboRidget ccomboRidget = new CComboRidget();
		ccomboRidget.setUIControl(new CCombo(shell, SWT.READ_ONLY));

		ccomboRidget.setFocusable(true);
		ReflectionUtils.invokeHidden(ccomboRidget.getFocusManager(),
				"updateShowComboListFlag", ccomboRidget.getUIControl()); //$NON-NLS-1$
		showComboList = ReflectionUtils.getHidden(ccomboRidget.getFocusManager(), "showComboList"); //$NON-NLS-1$
		assertFalse(showComboList);

		ccomboRidget.setFocusable(false);
		ReflectionUtils.invokeHidden(ccomboRidget.getFocusManager(),
				"updateShowComboListFlag", ccomboRidget.getUIControl()); //$NON-NLS-1$
		showComboList = ReflectionUtils.getHidden(ccomboRidget.getFocusManager(), "showComboList"); //$NON-NLS-1$
		assertTrue(showComboList);

		// CompletionCombo
		final CompletionComboRidget completionComboRidget = new CompletionComboRidget();
		completionComboRidget.setUIControl(new CompletionComboRCP(shell, SWT.READ_ONLY));

		completionComboRidget.setFocusable(true);
		ReflectionUtils.invokeHidden(completionComboRidget.getFocusManager(),
				"updateShowComboListFlag", completionComboRidget.getUIControl()); //$NON-NLS-1$
		showComboList = ReflectionUtils.getHidden(completionComboRidget.getFocusManager(), "showComboList"); //$NON-NLS-1$
		assertFalse(showComboList);

		completionComboRidget.setFocusable(false);
		ReflectionUtils.invokeHidden(completionComboRidget.getFocusManager(),
				"updateShowComboListFlag", completionComboRidget.getUIControl()); //$NON-NLS-1$
		showComboList = ReflectionUtils.getHidden(completionComboRidget.getFocusManager(), "showComboList"); //$NON-NLS-1$
		assertFalse(showComboList);

		// Text (some other Widget)
		final TextRidget textRidget = new TextRidget();
		textRidget.setUIControl(new Text(shell, SWT.BORDER));

		textRidget.setFocusable(true);
		ReflectionUtils
				.invokeHidden(textRidget.getFocusManager(), "updateShowComboListFlag", textRidget.getUIControl()); //$NON-NLS-1$
		showComboList = ReflectionUtils.getHidden(textRidget.getFocusManager(), "showComboList"); //$NON-NLS-1$
		assertFalse(showComboList);

		textRidget.setFocusable(false);
		ReflectionUtils
				.invokeHidden(textRidget.getFocusManager(), "updateShowComboListFlag", textRidget.getUIControl()); //$NON-NLS-1$
		showComboList = ReflectionUtils.getHidden(textRidget.getFocusManager(), "showComboList"); //$NON-NLS-1$
		assertFalse(showComboList);

	}

	public void testShowComboList() {

		final CComboRidget ccomboRidget = new CComboRidget();
		ccomboRidget.setUIControl(new CCombo(shell, SWT.READ_ONLY));

		ReflectionUtils.setHidden(ccomboRidget.getFocusManager(), "showComboList", true); //$NON-NLS-1$
		ReflectionUtils.invokeHidden(ccomboRidget.getFocusManager(), "showComboList", ccomboRidget.getUIControl()); //$NON-NLS-1$
		assertTrue(ccomboRidget.getUIControl().getListVisible());
		final boolean showComboList = ReflectionUtils.getHidden(ccomboRidget.getFocusManager(), "showComboList"); //$NON-NLS-1$
		assertFalse(showComboList);

		ccomboRidget.getUIControl().setListVisible(false);
		ReflectionUtils.invokeHidden(ccomboRidget.getFocusManager(), "showComboList", ccomboRidget.getUIControl()); //$NON-NLS-1$
		assertFalse(ccomboRidget.getUIControl().getListVisible());

	}

}
