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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.ui.swt.test.UITestHelper;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IBasicMarkableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 * Tests of the class {@link MenuItemRidget}.
 */
public class MenuItemRidgetTest extends AbstractSWTRidgetTest {

	private final static String ICON_ECLIPSE = "eclipse.gif"; //$NON-NLS-1$

	private final static String LABEL = "testlabel"; //$NON-NLS-1$
	private final static String LABEL2 = "testlabel2"; //$NON-NLS-1$

	@Override
	protected MenuItemRidget createRidget() {
		return new MenuItemRidget();
	}

	@Override
	protected MenuItem createWidget(final Composite parent) {
		final Menu menu = new Menu(parent);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(menu, "menu"); //$NON-NLS-1$
		final MenuItem item = new MenuItem(menu, SWT.NONE);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(item, "item"); //$NON-NLS-1$
		return item;
	}

	@Override
	protected MenuItem getWidget() {
		return (MenuItem) super.getWidget();
	}

	@Override
	protected MenuItemRidget getRidget() {
		return (MenuItemRidget) super.getRidget();
	}

	@Override
	public void testIsVisible() {
		getShell().open();

		assertTrue("Fails for " + getRidget(), getRidget().isVisible()); //$NON-NLS-1$
		assertFalse("Fails for " + getRidget(), getWidget().isDisposed()); //$NON-NLS-1$

		getRidget().setVisible(false);

		assertFalse("Fails for " + getRidget(), getRidget().isVisible()); //$NON-NLS-1$
		// widget expected to be hidden by disposing it:
		assertTrue("Fails for " + getRidget(), getWidget().isDisposed()); //$NON-NLS-1$

		getRidget().setVisible(true);

		assertTrue("Fails for " + getRidget(), getRidget().isVisible()); //$NON-NLS-1$
		// dispose of old widget cannot be undone:
		assertTrue("Fails for " + getRidget(), getWidget().isDisposed()); //$NON-NLS-1$
		// but a new widget should have been created:
		assertFalse("Fails for " + getRidget(), getRidget().getUIControl().isDisposed()); //$NON-NLS-1$
	}

	/**
	 * Tests the constructor {@code MenuItemRidget()}.
	 */
	public void testMenuItemRidget() {

		final MenuItemRidget item = new MenuItemRidget();
		final boolean textAlreadyInitialized = ReflectionUtils.getHidden(item, "textAlreadyInitialized"); //$NON-NLS-1$
		assertFalse(textAlreadyInitialized);
		final boolean useRidgetIcon = ReflectionUtils.getHidden(item, "useRidgetIcon"); //$NON-NLS-1$
		assertFalse(useRidgetIcon);

	}

	public void testRidgetMapping() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(MenuItemRidget.class, mapper.getRidgetClass(getWidget()));
	}

	public final void testSetText() throws Exception {
		final MenuItemRidget ridget = getRidget();
		final MenuItem widget = getWidget();

		ridget.setText(""); //$NON-NLS-1$

		assertEquals("", ridget.getText()); //$NON-NLS-1$
		assertEquals("", widget.getText()); //$NON-NLS-1$

		try {
			ridget.setText(null);
			fail();
		} catch (final IllegalArgumentException iae) {
			ok();
		}

		ridget.setText(LABEL);

		assertEquals(LABEL, ridget.getText());
		assertEquals(LABEL, widget.getText());

		ridget.setUIControl(null);
		ridget.setText(LABEL2);

		assertEquals(LABEL2, ridget.getText());
		assertEquals(LABEL, widget.getText());

		ridget.setUIControl(widget);

		assertEquals(LABEL2, ridget.getText());
		assertEquals(LABEL2, widget.getText());
	}

	/**
	 * Test method get/setIcon().
	 */
	public final void testSetIcon() {

		final MenuItemRidget ridget = getRidget();
		final MenuItem widget = ridget.getUIControl();

		ridget.setIcon(ICON_ECLIPSE);

		assertEquals(ICON_ECLIPSE, ridget.getIcon());
		assertNotNull(widget.getImage());

		ridget.setIcon(null);

		assertNull(ridget.getIcon());
		assertNull(widget.getImage());

		MenuItem button = createWidget(getShell());
		final Image buttonImage = button.getDisplay().getSystemImage(SWT.ICON_INFORMATION);
		button.setImage(buttonImage);
		IActionRidget buttonRidget = createRidget();
		// binding doesn't remove image of button, because the icon of the ridget is null and the method #setIcon wasn't called yet.
		buttonRidget.setUIControl(button);
		assertSame(buttonImage, button.getImage());

		buttonRidget.setIcon(null);
		assertNull(buttonRidget.getIcon());
		assertNull(button.getImage());

		buttonRidget.setIcon(ICON_ECLIPSE);
		assertEquals(ICON_ECLIPSE, buttonRidget.getIcon());
		assertNotNull(button.getImage());
		assertNotSame(buttonImage, button.getImage());

		button = createWidget(getShell());
		button.setImage(buttonImage);
		buttonRidget = createRidget();
		buttonRidget.setIcon(ICON_ECLIPSE);
		// binding replaces image of button, because the icon of the ridget is not null.
		buttonRidget.setUIControl(button);
		assertNotNull(button.getImage());
		assertNotSame(buttonImage, button.getImage());

	}

	/**
	 * Tests the method {@code initText}
	 */
	public void testInitText() {
		final MenuItemRidget ridget = getRidget();
		final MenuItem widget = ridget.getUIControl();

		ReflectionUtils.setHidden(ridget, "textAlreadyInitialized", false); //$NON-NLS-1$
		ReflectionUtils.setHidden(ridget, "text", null); //$NON-NLS-1$
		widget.setText("Hello!"); //$NON-NLS-1$

		ReflectionUtils.invokeHidden(ridget, "initText", new Object[] {}); //$NON-NLS-1$
		assertEquals("Hello!", ridget.getText()); //$NON-NLS-1$
		assertEquals("Hello!", widget.getText()); //$NON-NLS-1$
		assertTrue((Boolean) ReflectionUtils.getHidden(ridget, "textAlreadyInitialized")); //$NON-NLS-1$

		widget.setText("World"); //$NON-NLS-1$
		ReflectionUtils.invokeHidden(ridget, "initText", new Object[] {}); //$NON-NLS-1$
		assertEquals("Hello!", ridget.getText()); //$NON-NLS-1$
		assertEquals("World", widget.getText()); //$NON-NLS-1$
	}

	public void testAddListener() {
		final MenuItem widget = getWidget();
		final MenuItemRidget ridget = getRidget();

		final FTActionListener listener1 = new FTActionListener();
		final FTActionListener listener2 = new FTActionListener();

		ridget.addListener(listener1);
		ridget.addListener(listener2);
		// listener2 will not be added again
		// if the same instance is already added
		ridget.addListener(listener2);

		UITestHelper.fireSelectionEvent(widget);

		assertEquals(1, listener1.getCount());
		assertEquals(1, listener2.getCount());

		ridget.removeListener(listener1);
		UITestHelper.fireSelectionEvent(widget);

		assertEquals(1, listener1.getCount());
		assertEquals(2, listener2.getCount());

		ridget.removeListener(listener2);
		UITestHelper.fireSelectionEvent(widget);

		assertEquals(1, listener1.getCount());
		assertEquals(2, listener2.getCount());

		ridget.removeListener(listener2);
		UITestHelper.fireSelectionEvent(widget);

		assertEquals(1, listener1.getCount());
		assertEquals(2, listener2.getCount());
	}

	/**
	 * Make sure that enabled setting from ridget is applied to UI control. See <a href="http://bugs.eclipse.org/270444">Bug #270444 - Case 1</a>.
	 */
	@Override
	public void testApplyEnabledToUIControl() {
		final IRidget ridget = createRidget();
		final MenuItem item = createWidget(getShell());

		item.setEnabled(false);
		ridget.setEnabled(true);
		ridget.setUIControl(item);

		assertTrue(ridget.isEnabled());
		assertTrue(item.isEnabled());
		assertEquals(0, ((IBasicMarkableRidget) ridget).getMarkersOfType(DisabledMarker.class).size());
	}

	@Override
	public void testAddClickListener() {

		final MenuItemRidget ridget = getRidget();
		try {
			final FTClickListener listener1 = new FTClickListener();
			ridget.addClickListener(listener1);
			fail();
		} catch (final UnsupportedOperationException uoe) {
			ok();
		}

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

	@Override
	public void testGetMenuItemEmptyContextMenu() {
		try {
			final IRidget ridget = getRidget();
			ridget.getMenuItem(0);
			fail("UnsupportedOperationException expected"); //$NON-NLS-1$
		} catch (final UnsupportedOperationException expected) {
			Nop.reason("UnsupportedOperationException expected"); //$NON-NLS-1$
		}
	}

	@Override
	public void testGetMenuItemNotExistingItem() {
		final IRidget ridget = getRidget();
		try {
			ridget.getMenuItem(0);
			fail("UnsupportedOperationException expected"); //$NON-NLS-1$
		} catch (final UnsupportedOperationException expected) {
			Nop.reason("UnsupportedOperationException expected"); //$NON-NLS-1$
		}
	}
}
