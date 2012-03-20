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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.swt.ChoiceComposite;

/**
 * Tests for the class {@link ChoiceComposite}.
 */
@UITestCase
public class ChoiceCompositeTest extends RienaTestCase {

	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		final Display display = Display.getDefault();

		shell = new Shell(display);
		shell.setLayout(new FillLayout());

		shell.setSize(130, 100);
		shell.setLocation(0, 0);
		shell.open();
	}

	@Override
	protected void tearDown() throws Exception {
		shell.dispose();
		shell = null;

		super.tearDown();
	}

	// testing methods
	// ////////////////

	public void testConstructor() {
		try {
			new ChoiceComposite(null, SWT.NONE, false);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		final ChoiceComposite control1 = new ChoiceComposite(shell, SWT.NONE, false);
		assertFalse(control1.isMultipleSelection());

		final ChoiceComposite control2 = new ChoiceComposite(shell, SWT.NONE, true);
		assertTrue(control2.isMultipleSelection());
	}

	public void testSetOrientation() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);

		assertEquals(SWT.VERTICAL, control.getOrientation());

		control.setOrientation(SWT.HORIZONTAL);

		assertEquals(SWT.HORIZONTAL, control.getOrientation());
		assertTrue(control.getLayout() instanceof RowLayout);
		assertEquals(SWT.HORIZONTAL, ((RowLayout) control.getLayout()).type);

		control.setOrientation(SWT.VERTICAL);

		assertEquals(SWT.VERTICAL, control.getOrientation());
		assertTrue(control.getLayout() instanceof FillLayout);
		assertEquals(SWT.VERTICAL, ((FillLayout) control.getLayout()).type);

		try {
			control.setOrientation(SWT.NONE);
			fail();
		} catch (final RuntimeException rex) {
			// expected and unchanged
			assertEquals(SWT.VERTICAL, control.getOrientation());
		}
	}

	public void testSetForeground() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);
		final Button child1 = new Button(control, SWT.RADIO);

		final Color colorGreen = control.getDisplay().getSystemColor(SWT.COLOR_GREEN);

		assertTrue(!colorGreen.equals(control.getForeground()));

		control.setForeground(colorGreen);

		assertEquals(colorGreen, control.getForeground());
		assertEquals(colorGreen, child1.getForeground());
	}

	public void testSetBackground() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);
		final Button child1 = new Button(control, SWT.RADIO);

		final Color colorRed = control.getDisplay().getSystemColor(SWT.COLOR_RED);

		assertTrue(!colorRed.equals(control.getBackground()));

		control.setBackground(colorRed);

		assertEquals(colorRed, control.getBackground());
		assertEquals(colorRed, child1.getBackground());
	}

	public void testSetEnabled() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);
		final Button child1 = new Button(control, SWT.RADIO);

		assertTrue(control.getEnabled());
		assertTrue(child1.getEnabled());

		control.setEnabled(false);

		assertFalse(control.getEnabled());
		assertFalse(child1.getEnabled());

		control.setEnabled(true);

		assertTrue(control.getEnabled());
		assertTrue(child1.getEnabled());
	}

	/**
	 * As per Bug 321927
	 */
	public void testSetEditable() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, true);
		final Button child1 = new Button(control, SWT.CHECK);
		child1.setSelection(true);
		final Button child2 = new Button(control, SWT.CHECK);
		child2.setSelection(true);
		final Button child3 = new Button(control, SWT.CHECK);
		child3.setSelection(false);

		assertTrue(control.isEnabled());

		assertTrue(control.getEditable());
		assertTrue(child1.isEnabled());
		assertTrue(child2.isEnabled());
		assertTrue(child3.isEnabled());

		control.setEditable(false);

		assertFalse(control.getEditable());
		assertTrue(child1.isEnabled()); // selected
		assertTrue(child2.isEnabled()); // selected
		assertFalse(child3.isEnabled());

		control.setEditable(true);

		assertTrue(control.getEditable());
		assertTrue(child1.isEnabled());
		assertTrue(child2.isEnabled());
		assertTrue(child3.isEnabled());
	}

	/**
	 * As per Bug 321927
	 */
	public void testSetEditableFalseBlocksChangesFromUI() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, true);
		final Button child1 = control.createChild("child1");

		child1.setSelection(true);
		control.setEditable(false);

		assertTrue(child1.getSelection());
		assertFalse(control.getEditable());

		child1.setSelection(false);
		final Event event = new Event();
		event.type = SWT.Selection;
		event.widget = child1;
		event.display = child1.getDisplay();
		child1.notifyListeners(event.type, event);

		// editable = false -> selection reverted
		assertTrue(child1.getSelection());
	}

	/**
	 * As per Bug 321927
	 */
	public void testToggleEditableWhenDisabled() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, true);
		final Button child1 = new Button(control, SWT.CHECK);
		child1.setSelection(true);
		final Button child2 = new Button(control, SWT.CHECK);
		child2.setSelection(true);
		final Button child3 = new Button(control, SWT.CHECK);
		child3.setSelection(false);

		control.setEditable(false);
		control.setEnabled(false);

		assertFalse(child1.isEnabled());
		assertFalse(child2.isEnabled());
		assertFalse(child3.isEnabled());

		control.setEnabled(true);

		assertTrue(child1.isEnabled()); // selected
		assertTrue(child2.isEnabled()); // selected
		assertFalse(child3.isEnabled());

		control.setEditable(true);

		assertTrue(child1.isEnabled());
		assertTrue(child2.isEnabled());
		assertTrue(child3.isEnabled());
	}

	/**
	 * As per Bug 317568.
	 */
	public void testSetMarginsVerticalOrientation() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);
		assertEquals(SWT.VERTICAL, control.getOrientation());

		assertEquals(0, control.getMargins().x);
		assertEquals(0, control.getMargins().y);
		assertEquals(0, ((FillLayout) control.getLayout()).marginHeight);
		assertEquals(0, ((FillLayout) control.getLayout()).marginWidth);

		control.setMargins(10, 20);

		assertEquals(10, control.getMargins().x);
		assertEquals(20, control.getMargins().y);
		assertEquals(10, ((FillLayout) control.getLayout()).marginHeight);
		assertEquals(20, ((FillLayout) control.getLayout()).marginWidth);
	}

	/**
	 * As per Bug 317568.
	 */
	public void testSetMarginsHorizontalOrientation() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);
		control.setOrientation(SWT.HORIZONTAL);
		assertEquals(SWT.HORIZONTAL, control.getOrientation());

		assertEquals(0, control.getMargins().x);
		assertEquals(0, control.getMargins().y);
		assertEquals(0, ((RowLayout) control.getLayout()).marginHeight);
		assertEquals(0, ((RowLayout) control.getLayout()).marginWidth);

		control.setMargins(10, 20);

		assertEquals(10, control.getMargins().x);
		assertEquals(20, control.getMargins().y);
		assertEquals(10, ((RowLayout) control.getLayout()).marginHeight);
		assertEquals(20, ((RowLayout) control.getLayout()).marginWidth);
	}

	/**
	 * As per Bug 317568.
	 */
	public void testSetMarginsNegativeValue() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);

		try {
			control.setMargins(-5, 10);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		try {
			control.setMargins(10, -5);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
	}

	/**
	 * As per Bug 317568.
	 */
	public void testSetSpacingVerticalOrientation() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);
		assertEquals(SWT.VERTICAL, control.getOrientation());

		assertEquals(3, control.getSpacing().x);
		assertEquals(0, control.getSpacing().y);
		assertEquals(0, ((FillLayout) control.getLayout()).spacing);

		control.setSpacing(0, 10);

		assertEquals(0, control.getSpacing().x);
		assertEquals(10, control.getSpacing().y);
		assertEquals(10, ((FillLayout) control.getLayout()).spacing);
	}

	/**
	 * As per Bug 317568.
	 */
	public void testSetSpacingHorizontalOrientation() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);
		control.setOrientation(SWT.HORIZONTAL);
		assertEquals(SWT.HORIZONTAL, control.getOrientation());

		assertEquals(3, control.getSpacing().x);
		assertEquals(0, control.getSpacing().y);
		assertEquals(3, ((RowLayout) control.getLayout()).spacing);

		control.setSpacing(10, 0);

		assertEquals(10, control.getSpacing().x);
		assertEquals(0, control.getSpacing().y);
		assertEquals(10, ((RowLayout) control.getLayout()).spacing);
	}

	/**
	 * As per Bug 317568.
	 */
	public void testSetSpacingNegativeValue() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);

		try {
			control.setSpacing(-5, 10);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		try {
			control.setSpacing(10, -5);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
	}

	/**
	 * As per Bug 323449
	 */
	public void testDisabledWidgetHasGrayBackground() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);
		final Button child1 = control.createChild("child1");

		assertTrue(control.isEnabled());

		final Color defaultBg = control.getBackground();
		final Color defaultChildBg = child1.getBackground();
		final Color disabledBg = control.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);

		control.setEnabled(false);

		assertEquals(disabledBg, control.getBackground());
		assertEquals(disabledBg, child1.getBackground());

		control.setEnabled(true);

		assertEquals(defaultBg, control.getBackground());
		assertEquals(defaultChildBg, child1.getBackground());
	}

	/**
	 * As per Bug 323449
	 */
	public void testSetBackgroundColorWhileDisabled() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);
		final Button child1 = control.createChild("child1");
		final Display display = control.getDisplay();
		final Color disabledBg = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		final Color red = display.getSystemColor(SWT.COLOR_RED);

		control.setEnabled(false);
		control.setBackground(red);

		assertEquals(disabledBg, control.getBackground());
		assertEquals(disabledBg, child1.getBackground());

		control.setEnabled(true);

		assertEquals(red, control.getBackground());
		assertEquals(red, child1.getBackground());
	}

	/**
	 * As per Bug ruv301
	 * 
	 * control should have the same background color as its parent, if it is
	 * disabled.
	 */
	public void testSetBackgroundColorWhileDisabledRespectingParentsBackground() {
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, false);
		final Button child1 = control.createChild("child1");
		final Display display = control.getDisplay();
		final Color red = display.getSystemColor(SWT.COLOR_RED);

		shell.setBackground(red);
		control.setEnabled(false);

		assertEquals(red, control.getBackground());
		assertEquals(red, child1.getBackground());
		assertEquals(red, shell.getBackground());

		control.setEnabled(true);

		assertEquals(red, control.getBackground());
		assertEquals(red, child1.getBackground());
		assertEquals(red, shell.getBackground());

		control.setEnabled(false);

		assertEquals(red, control.getBackground());
		assertEquals(red, child1.getBackground());
		assertEquals(red, shell.getBackground());
	}
}