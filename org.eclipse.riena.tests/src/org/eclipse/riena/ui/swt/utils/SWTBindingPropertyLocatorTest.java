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
package org.eclipse.riena.ui.swt.utils;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.common.IComplexComponent;

/**
 * Tests the class {@link SWTBindingPropertyLocator}.
 */
@UITestCase
public class SWTBindingPropertyLocatorTest extends TestCase {

	private Shell shell;

	@Override
	protected void setUp() {
		shell = new Shell();
	}

	@Override
	protected void tearDown() throws Exception {
		shell.dispose();
	}

	/**
	 * Tests the <i>private</i> method {@code locateBindingProperty(Widget)}.
	 */
	public void testLocateBindingProperty() {
		final SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();

		String prop = locator.locateBindingProperty(shell);

		assertEquals(null, prop);

		final Label label = new Label(shell, SWT.NONE);
		locator.setBindingProperty(label, "label1"); //$NON-NLS-1$
		prop = locator.locateBindingProperty(label);

		assertEquals("label1", prop); //$NON-NLS-1$

		label.dispose();
		prop = locator.locateBindingProperty(label);

		assertEquals(null, prop);

		prop = locator.locateBindingProperty(null);

		assertEquals(null, prop);

		prop = locator.locateBindingProperty(new Object());

		assertEquals(null, prop);
	}

	public void testLocateBindingPropertyInComplexComponent() {
		final SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		final TestComplexComponent complexComponent = new TestComplexComponent(shell, SWT.NONE);
		final Text text = new Text(complexComponent, SWT.NONE);

		locator.setBindingProperty(complexComponent, "complex1"); //$NON-NLS-1$

		assertEquals(null, locator.locateBindingProperty(text));

		locator.setBindingProperty(text, "text1"); //$NON-NLS-1$

		assertEquals("text1", locator.locateBindingProperty(text)); //$NON-NLS-1$

		try {
			locator.setBindingProperty(complexComponent, ""); //$NON-NLS-1$

		} catch (final IllegalArgumentException ex) {

		}

		assertEquals("text1", locator.locateBindingProperty(text)); //$NON-NLS-1$
	}

	public void testLocateBindingPropertyInComplexComponentWithNesting() {
		final SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		final TestComplexComponent complexComponent = new TestComplexComponent(shell, SWT.NONE);
		final Composite composite = new Composite(complexComponent, SWT.NONE);
		final Text text = new Text(composite, SWT.NONE);

		locator.setBindingProperty(complexComponent, "complex1"); //$NON-NLS-1$
		locator.setBindingProperty(text, "text"); //$NON-NLS-1$

		assertEquals("text", locator.locateBindingProperty(text)); //$NON-NLS-1$
	}

	public void testGetControlsWithBindingProperty() {
		final SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		final Composite composite1 = new Composite(shell, SWT.NONE);
		final Label label1 = new Label(composite1, SWT.NONE);
		final Label label2 = new Label(composite1, SWT.NONE);

		locator.setBindingProperty(label1, "label1"); //$NON-NLS-1$
		locator.setBindingProperty(label2, "label2"); //$NON-NLS-1$

		final List<Object> result1 = SWTBindingPropertyLocator.getControlsWithBindingProperty(composite1);

		assertEquals(2, result1.size());
		assertTrue(result1.contains(label1));
		assertTrue(result1.contains(label2));
	}

	public void testGetControlsWithBindingPropertyExcludeFirstComposite() {
		final SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		final Composite composite1 = new Composite(shell, SWT.NONE);
		final Label label1 = new Label(composite1, SWT.NONE);

		locator.setBindingProperty(composite1, "complex1"); //$NON-NLS-1$
		locator.setBindingProperty(label1, "label1"); //$NON-NLS-1$

		final List<Object> result1 = SWTBindingPropertyLocator.getControlsWithBindingProperty(composite1);

		assertEquals(1, result1.size());
		assertTrue(result1.contains(label1));
	}

	public void testGetControlsWithBindingPropertyRecursive() {
		final SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();

		final Composite composite1 = new Composite(shell, SWT.NONE);
		locator.setBindingProperty(composite1, "composite1"); //$NON-NLS-1$
		final Composite composite2 = new Composite(composite1, SWT.NONE);
		locator.setBindingProperty(composite2, "composite2"); //$NON-NLS-1$
		final Label c2Label1 = new Label(composite2, SWT.NONE);
		locator.setBindingProperty(c2Label1, "label1"); //$NON-NLS-1$
		final Label c2Label2 = new Label(composite2, SWT.NONE);
		locator.setBindingProperty(c2Label2, "label2"); //$NON-NLS-1$

		final List<Object> result = SWTBindingPropertyLocator.getControlsWithBindingProperty(composite1);

		assertEquals(3, result.size());
		assertTrue(result.contains(composite2));
		assertTrue(result.contains(c2Label1));
		assertTrue(result.contains(c2Label2));
	}

	public void testGetControlsWithBindingPropertyConflict() {
		final SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		final Composite composite1 = new Composite(shell, SWT.NONE);
		final Label label1 = new Label(composite1, SWT.NONE);
		final Label label2 = new Label(composite1, SWT.NONE);

		locator.setBindingProperty(composite1, "composite1"); //$NON-NLS-1$
		locator.setBindingProperty(label1, "label"); //$NON-NLS-1$
		locator.setBindingProperty(label2, "label"); //$NON-NLS-1$

		try {
			SWTBindingPropertyLocator.getControlsWithBindingProperty(composite1);
			fail();
		} catch (final RuntimeException rex) {
			// ok
		}
	}

	public void testGetControlsWithBindingPropertyRecursiveConflict() {
		final SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		final Composite composite1 = new Composite(shell, SWT.NONE);
		final Label label1 = new Label(composite1, SWT.NONE);
		final Composite composite2 = new Composite(composite1, SWT.NONE);
		final Label label2 = new Label(composite2, SWT.NONE);

		locator.setBindingProperty(composite1, "composite1"); //$NON-NLS-1$
		locator.setBindingProperty(label1, "label"); //$NON-NLS-1$
		locator.setBindingProperty(label2, "label"); //$NON-NLS-1$

		try {
			SWTBindingPropertyLocator.getControlsWithBindingProperty(composite1);
			fail();
		} catch (final RuntimeException rex) {
			// ok
		}
	}

	public void testGetControlsWithBindingPropertyRecursiveConflict2() {
		final SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		final Composite composite1 = new Composite(shell, SWT.NONE);
		final Composite composite2 = new Composite(shell, SWT.NONE);
		final Label c1Label1 = new Label(composite1, SWT.NONE);
		final Label c2Label1 = new Label(composite2, SWT.NONE);

		locator.setBindingProperty(composite1, "composite1"); //$NON-NLS-1$
		locator.setBindingProperty(composite2, "composite2"); //$NON-NLS-1$
		locator.setBindingProperty(c1Label1, "label"); //$NON-NLS-1$
		locator.setBindingProperty(c2Label1, "label"); //$NON-NLS-1$

		try {
			SWTBindingPropertyLocator.getControlsWithBindingProperty(shell);
			fail();
		} catch (final RuntimeException rex) {
			// ok
		}
	}

	/**
	 * Tests the <i>private/i> method {@code getComplexParent(Control)}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testGetComplexParent() throws Exception {

		final SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();

		final Composite composite1 = new Composite(shell, SWT.NONE);
		IComplexComponent parent = ReflectionUtils.invokeHidden(locator, "getComplexParent", composite1); //$NON-NLS-1$
		assertNull(parent);

		final Label c1Label1 = new Label(composite1, SWT.NONE);
		parent = ReflectionUtils.invokeHidden(locator, "getComplexParent", c1Label1); //$NON-NLS-1$
		assertNull(parent);

		final TestComplexComponent complexComponent = new TestComplexComponent(shell, SWT.NONE);
		parent = ReflectionUtils.invokeHidden(locator, "getComplexParent", complexComponent); //$NON-NLS-1$
		assertNull(parent);

		final Label c2Label1 = new Label(complexComponent, SWT.NONE);
		parent = ReflectionUtils.invokeHidden(locator, "getComplexParent", c2Label1); //$NON-NLS-1$
		assertSame(complexComponent, parent);

	}

	public void testGetComplexBindingId() throws Exception {

		final SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();

		String id = locator.getComplexBindingId(null);
		assertNull(id);

		final Composite composite1 = new Composite(shell, SWT.NONE);
		final Label c1Label1 = new Label(composite1, SWT.NONE);
		locator.setBindingProperty(composite1, "composite1"); //$NON-NLS-1$
		locator.setBindingProperty(c1Label1, "label"); //$NON-NLS-1$

		id = locator.getComplexBindingId(c1Label1);
		assertEquals("label", id); //$NON-NLS-1$

		final TestComplexComponent complexComponent = new TestComplexComponent(shell, SWT.NONE);
		final Label c2Label1 = new Label(complexComponent, SWT.NONE);
		locator.setBindingProperty(complexComponent, "complexComponent"); //$NON-NLS-1$
		locator.setBindingProperty(c2Label1, "label"); //$NON-NLS-1$

		id = locator.getComplexBindingId(c2Label1);
		assertEquals("complexComponent.label", id); //$NON-NLS-1$

	}

	// helping classes
	//////////////////

	private static class TestComplexComponent extends Composite implements IComplexComponent {
		public TestComplexComponent(final Composite parent, final int style) {
			super(parent, style);
		}

		public List<Object> getUIControls() {
			return null;
		}
	}

}
