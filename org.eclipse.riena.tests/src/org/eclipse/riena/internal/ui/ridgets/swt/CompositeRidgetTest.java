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

import org.eclipse.core.databinding.BindingException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.ui.ridgets.ICompositeRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 * Tests for {@link CompositeRidget}.
 */
public class CompositeRidgetTest extends AbstractSWTRidgetTest {

	@Override
	protected IRidget createRidget() {
		return new CompositeRidget();
	}

	@Override
	protected Widget createWidget(final Composite parent) {
		final Widget result = new Composite(parent, SWT.NONE);
		result.setData(SWTBindingPropertyLocator.BINDING_PROPERTY, "pbId"); //$NON-NLS-1$
		return result;
	}

	@Override
	protected ICompositeRidget getRidget() {
		return (ICompositeRidget) super.getRidget();
	}

	@Override
	protected Composite getWidget() {
		return (Composite) super.getWidget();
	}

	// testing methods
	//////////////////

	public void testCompositeToRidgetMapping() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();

		final Composite composite = new Composite(getShell(), SWT.NONE);
		composite.setData(SWTBindingPropertyLocator.BINDING_PROPERTY, "id1"); //$NON-NLS-1$

		assertNotNull(composite.getData(SWTBindingPropertyLocator.BINDING_PROPERTY));
		assertSame(CompositeRidget.class, mapper.getRidgetClass(composite));
	}

	public void testGroupToRidgetMapping() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();

		final Group group = new Group(getShell(), SWT.NONE);
		group.setData(SWTBindingPropertyLocator.BINDING_PROPERTY, "id2"); //$NON-NLS-1$

		assertNotNull(group.getData(SWTBindingPropertyLocator.BINDING_PROPERTY));
		assertSame(CompositeRidget.class, mapper.getRidgetClass(group));
	}

	public void testCompositeToRidgetMappingWithNoId() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();

		final Composite composite = new Composite(getShell(), SWT.NONE);

		assertNullId(mapper, composite);

		composite.setData(SWTBindingPropertyLocator.BINDING_PROPERTY, ""); //$NON-NLS-1$

		assertEmptyId(mapper, composite);
	}

	public void testGroupToRidgetMappingWithNoId() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();

		final Composite composite = new Composite(getShell(), SWT.NONE);

		assertNullId(mapper, composite);

		composite.setData(SWTBindingPropertyLocator.BINDING_PROPERTY, ""); //$NON-NLS-1$

		assertEmptyId(mapper, composite);
	}

	@Override
	public void testGetFocusable() {
		createTextWidget();
		super.testGetFocusable();
	}

	@Override
	public void testSetFocusable() {
		createTextWidget();
		super.testSetFocusable();
	}

	@Override
	public void testRequestFocus() throws Exception {
		createTextWidget();
		super.testRequestFocus();
	}

	// helping methods
	//////////////////

	private void assertEmptyId(final SwtControlRidgetMapper mapper, final Composite control) {
		assertEquals("", control.getData(SWTBindingPropertyLocator.BINDING_PROPERTY)); //$NON-NLS-1$
		try {
			mapper.getRidgetClass(control);
			fail();
		} catch (final BindingException bex) {
			ok("expected"); //$NON-NLS-1$
		}
	}

	private void assertNullId(final SwtControlRidgetMapper mapper, final Composite control) {
		assertEquals(null, control.getData(SWTBindingPropertyLocator.BINDING_PROPERTY));
		try {
			mapper.getRidgetClass(control);
			fail();
		} catch (final BindingException bex) {
			ok("expected"); //$NON-NLS-1$
		}
	}

	/*
	 * Giving focus assumes there is something in the composite than can receive it. This method will create a Text widget / ridget to set that up.
	 */
	private void createTextWidget() {
		final Composite control = getWidget();
		final Text txt = new Text(control, SWT.BORDER);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(txt, "txt"); //$NON-NLS-1$
		final TextRidget txtRidget = new TextRidget();
		txtRidget.setUIControl(txt);

		final ICompositeRidget ridget = getRidget();
		ridget.addRidget("txt", txtRidget); //$NON-NLS-1$
	}

	@Override
	public void testGetMenuItemCount() {
		final IRidget ridget = getRidget();

		try {
			ridget.getMenuItemCount();
			fail("UnsupportedOperationException expected"); //$NON-NLS-1$
		} catch (final UnsupportedOperationException expected) {
			Nop.reason("UnsupportedOperationException expected"); //$NON-NLS-1$
		}
	}

	@Override
	public void testGetMenuItem() {
		final IRidget ridget = getRidget();

		try {
			ridget.getMenuItem(0);
			fail("UnsupportedOperationException expected"); //$NON-NLS-1$
		} catch (final UnsupportedOperationException expected) {
			Nop.reason("UnsupportedOperationException expected"); //$NON-NLS-1$
		}
	}

	@Override
	public void testAddMenuItem() {
		final IRidget ridget = getRidget();
		final String menuItemWithoutIconText = "MenuItemWithoutIcon"; //$NON-NLS-1$

		try {
			ridget.addMenuItem(menuItemWithoutIconText);
			fail("UnsupportedOperationException expected"); //$NON-NLS-1$
		} catch (final UnsupportedOperationException expected) {
			Nop.reason("UnsupportedOperationException expected"); //$NON-NLS-1$
		}
	}

	@Override
	public void testRemoveMenuItem() {
		final IRidget ridget = getRidget();
		final String menuItemWithIconText = "MenuItemWithIcon"; //$NON-NLS-1$

		try {
			ridget.removeMenuItem(menuItemWithIconText);
			fail("UnsupportedOperationException expected"); //$NON-NLS-1$
		} catch (final UnsupportedOperationException expected) {
			Nop.reason("UnsupportedOperationException expected"); //$NON-NLS-1$
		}
	}
}
