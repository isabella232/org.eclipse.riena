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
package org.eclipse.riena.navigation.ui.swt.binding;

import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.internal.ui.ridgets.swt.ActionRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.LabelRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ToggleButtonRidget;
import org.eclipse.riena.navigation.IAction;
import org.eclipse.riena.ui.ridgets.ClassRidgetMapper;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.listener.IFocusListener;
import org.eclipse.riena.ui.ridgets.swt.uibinding.StyleCondition;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.uibinding.AbstractControlRidgetMapper.Mapping;
import org.eclipse.riena.ui.ridgets.uibinding.IMappingCondition;

/**
 * Tests of the class <code>SwtControlRidgetMapper</code>
 */
@UITestCase
public class SwtControlRidgetMapperTest extends RienaTestCase {

	private SwtControlRidgetMapper mapper;
	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mapper = SwtControlRidgetMapper.getInstance();
		shell = new Shell();
	}

	@Override
	protected void tearDown() throws Exception {

		// Create new instance of SwtControlRidgetMapper to start with initial mappings only (not additional mappings added in previous test cases)
		final Object scrm = ReflectionUtils.getHidden(SwtControlRidgetMapper.class, "SCRM"); //$NON-NLS-1$
		ReflectionUtils.setHidden(scrm, "singleton", null); //$NON-NLS-1$
		mapper = null;
		shell.dispose();
		shell = null;

		super.tearDown();
	}

	/**
	 * Tests the method <code>addMapping(Class<? extends Widget> , Class<? extends IRidget> )</code> .
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testAddMapping() throws Exception {

		mapper.addMapping(MockComposite.class, MockRidget.class);

		final Class<? extends IRidget> ridget = mapper.getRidgetClass(MockComposite.class);
		assertNotNull(ridget);
		assertEquals(MockRidget.class.getName(), ridget.getName());

	}

	/**
	 * Tests the method {@link SwtControlRidgetMapper#addMapping(Class, Class, IMappingCondition)} .
	 */
	public void testAddMappingWithCondition() {

		final FTMappingCondition condition1 = new FTMappingCondition(false);
		final FTMappingCondition condition2 = new FTMappingCondition(false);

		mapper.addMapping(MockComposite.class, MockRidget.class, condition1);
		mapper.addMapping(MockComposite.class, MockRidget2.class, condition2);

		final MockComposite widget = new MockComposite(shell, SWT.NONE);
		try {
			condition1.setMatch(true);

			Class<? extends IRidget> ridgetClass = mapper.getRidgetClass(widget);
			assertNotNull(ridgetClass);
			assertEquals(MockRidget.class.getName(), ridgetClass.getName());

			condition1.setMatch(false);
			condition2.setMatch(true);

			ridgetClass = mapper.getRidgetClass(widget);
			assertNotNull(ridgetClass);
			assertEquals(MockRidget2.class.getName(), ridgetClass.getName());

			condition2.setMatch(false);

			try {
				mapper.getRidgetClass(widget);
				fail();
			} catch (final BindingException bex) {
				ok();
			}
		} finally {
			widget.dispose();
		}
	}

	/**
	 * Tests the method <code>getRidgetClass(Class<? extends Widget>)</code>.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testGetRidgetClass() throws Exception {

		final Class<? extends IRidget> ridget = mapper.getRidgetClass(Label.class);
		assertNotNull(ridget);
		assertEquals(LabelRidget.class.getName(), ridget.getName());

		try {
			mapper.getRidgetClass(MockComposite.class);
			fail("BindingException expected"); //$NON-NLS-1$
		} catch (final BindingException e) {
			ok("BindingException expected"); //$NON-NLS-1$
		}

	}

	/**
	 * Tests the method <code>getRidgetClass(Widget)</code>.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testGetRidgetClassWidget() throws Exception {

		Button button = new Button(shell, SWT.DEFAULT);
		Class<? extends IRidget> ridget = mapper.getRidgetClass(button);
		assertNotNull(ridget);
		assertEquals(ActionRidget.class.getName(), ridget.getName());

		button = new Button(shell, SWT.FLAT);
		ridget = mapper.getRidgetClass(button);
		assertNotNull(ridget);
		assertEquals(ActionRidget.class.getName(), ridget.getName());

		button = new Button(shell, SWT.CHECK);
		ridget = mapper.getRidgetClass(button);
		assertNotNull(ridget);
		assertEquals(ToggleButtonRidget.class.getName(), ridget.getName());

	}

	/**
	 * Tests the method <code>isMatching(Class<? extends Widget>)</code>.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testIsMatching() throws Exception {

		Mapping mapping = new Mapping(MockComposite.class, MockRidget.class);
		assertTrue(mapping.isMatching(MockComposite.class));
		assertFalse(mapping.isMatching(MockComposite2.class));

		mapping = new Mapping(MockComposite.class, MockRidget.class, new StyleCondition(SWT.CHECK));
		assertFalse(mapping.isMatching(MockComposite.class));
		assertFalse(mapping.isMatching(MockComposite2.class));

		mapping = new Mapping(MockComposite.class, MockRidget.class, new FTMappingCondition(true));
		assertFalse(mapping.isMatching(MockComposite.class));
		assertFalse(mapping.isMatching(MockComposite2.class));
	}

	/**
	 * Tests the method <code>isMatching(Widget>)</code>.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testIsMatchingWidget() throws Exception {

		Mapping mapping = new Mapping(MockComposite.class, MockRidget.class);
		MockComposite comp = new MockComposite(shell, SWT.DEFAULT);
		assertTrue(mapping.isMatching(comp));
		comp.dispose();
		final MockComposite2 comp2 = new MockComposite2(shell, SWT.DEFAULT);
		assertFalse(mapping.isMatching(comp2));
		comp2.dispose();

		mapping = new Mapping(MockComposite.class, MockRidget.class, new StyleCondition(SWT.ABORT));
		comp = new MockComposite(shell, SWT.ALPHA);
		assertFalse(mapping.isMatching(comp));
		comp.dispose();
		comp = new MockComposite(shell, SWT.ABORT);
		assertTrue(mapping.isMatching(comp));
		comp.dispose();
		comp = new MockComposite(shell, SWT.ABORT | SWT.ALT);
		assertTrue(mapping.isMatching(comp));
		comp.dispose();

		final FTMappingCondition condition = new FTMappingCondition(true);
		mapping = new Mapping(MockComposite.class, MockRidget.class, condition);
		comp = new MockComposite(shell, SWT.DEFAULT);
		try {
			assertTrue(mapping.isMatching(comp));
			condition.setMatch(false);
			assertFalse(mapping.isMatching(comp));
		} finally {
			comp.dispose();
		}

	}

	public void testGetPrimaryRidgetInterface() {
		assertEquals(IRidget.class, mapper.getPrimaryRidgetInterface(MockRidget.class));
		assertEquals(IMockRidget2.class, mapper.getPrimaryRidgetInterface(MockRidget3.class));
		assertEquals(IMockRidget2.class, mapper.getPrimaryRidgetInterface(MockRidgetImplementigSeveralInterfaces.class));
	}

	public void testAddMappingToClassRidgetMapper() {
		final ClassRidgetMapper classRidgetMapper = ClassRidgetMapper.getInstance();

		mapper.addMapping(MockComposite.class, MockRidget.class);
		assertEquals(MockRidget.class, classRidgetMapper.getRidgetClass(IRidget.class));

		mapper.addMapping(MockComposite2.class, MockRidget3.class);
		assertEquals(MockRidget3.class, classRidgetMapper.getRidgetClass(IMockRidget2.class));

		try {
			classRidgetMapper.getRidgetClass(IMockRidget.class);
			fail("BindingException expected"); //$NON-NLS-1$
		} catch (final BindingException e) {
			ok("BindingException expected"); //$NON-NLS-1$
		}
	}

	// helping classes
	// ////////////////

	/**
	 * Simple implementation of an IMappingCondition used for testing purposes. USe the {@link #setMatch(boolean)} to change the behavior of a condition.
	 */
	private static final class FTMappingCondition implements IMappingCondition {

		private boolean isMatch = true;

		public FTMappingCondition(final boolean isMatch) {
			this.isMatch = isMatch;
		}

		void setMatch(final boolean isMatch) {
			this.isMatch = isMatch;
		}

		public boolean isMatch(final Object widget) {
			return isMatch;
		}

	}

	/**
	 * Mock extention of <code>Composite</code>.
	 */
	private static final class MockComposite extends Composite {

		public MockComposite(final Composite parent, final int style) {
			super(parent, style);
		}

	}

	/**
	 * Another mock extention of <code>Composite</code>.
	 */
	private static final class MockComposite2 extends Composite {

		public MockComposite2(final Composite parent, final int style) {
			super(parent, style);
		}

	}

	/**
	 * Mock implementation of ridget.
	 */
	private static final class MockRidget implements IRidget {

		public Object getUIControl() {
			return null;
		}

		public void setUIControl(final Object uiControl) {
		}

		public void addPropertyChangeListener(final PropertyChangeListener propertyChangeListener) {
		}

		public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener propertyChangeListener) {
		}

		public void removePropertyChangeListener(final PropertyChangeListener propertyChangeListener) {
		}

		public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener propertyChangeListener) {
		}

		public boolean isVisible() {
			return false;
		}

		public void setVisible(final boolean visible) {
		}

		public boolean isEnabled() {
			return false;
		}

		public void setEnabled(final boolean enabled) {
		}

		public void addFocusListener(final IFocusListener listener) {
		}

		public void removeFocusListener(final IFocusListener listener) {
		}

		public void updateFromModel() {
		}

		public void requestFocus() {
		}

		public boolean hasFocus() {
			return false;
		}

		public boolean isFocusable() {
			return false;
		}

		public void setFocusable(final boolean focusable) {
		}

		public String getToolTipText() {
			return null;
		}

		public void setToolTipText(final String toolTipText) {
		}

		public boolean isBlocked() {
			return false;
		}

		public void setBlocked(final boolean blocked) {
		}

		public String getID() {
			return null;
		}

		public IRidgetContainer getController() {
			return null;
		}

		public void setController(final IRidgetContainer controller) {
		}

		public void setIgnoreBindingError(final boolean ignore) {
		}

		public boolean isIgnoreBindingError() {
			return false;
		}
	}

	/**
	 * Another mock implementation of ridget.
	 */
	private static final class MockRidget2 implements IRidget {

		public Object getUIControl() {
			return null;
		}

		public void setUIControl(final Object uiControl) {
		}

		public void addPropertyChangeListener(final PropertyChangeListener propertyChangeListener) {
		}

		public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener propertyChangeListener) {
		}

		public void removePropertyChangeListener(final PropertyChangeListener propertyChangeListener) {
		}

		public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener propertyChangeListener) {
		}

		public boolean isVisible() {
			return false;
		}

		public void setVisible(final boolean visible) {
		}

		public boolean isEnabled() {
			return false;
		}

		public void setEnabled(final boolean enabled) {
		}

		public void addFocusListener(final IFocusListener listener) {
		}

		public void removeFocusListener(final IFocusListener listener) {
		}

		public void updateFromModel() {
		}

		public void requestFocus() {
		}

		public boolean hasFocus() {
			return false;
		}

		public boolean isFocusable() {
			return false;
		}

		public void setFocusable(final boolean focusable) {
		}

		public String getToolTipText() {
			return null;
		}

		public void setToolTipText(final String toolTipText) {
		}

		public boolean isBlocked() {
			return false;
		}

		public void setBlocked(final boolean blocked) {
		}

		public String getID() {
			return null;
		}

		public IRidgetContainer getController() {
			return null;
		}

		public void setController(final IRidgetContainer controller) {
		}

		public void setIgnoreBindingError(final boolean ignore) {
		}

		public boolean isIgnoreBindingError() {
			return false;
		}
	}

	/**
	 * Mock interface extending IRidget.
	 */
	static interface IMockRidget2 extends IMockRidget {

	}

	/**
	 * Mock interface extending IRidget.
	 */
	static interface IMockRidget extends IRidget {

	}

	/**
	 * abstract mock implementation of ridget.
	 */
	private static abstract class AbstractMockRidget implements IMockRidget2 {
		public Object getUIControl() {
			return null;
		}

		public void setUIControl(final Object uiControl) {
		}

		public void addPropertyChangeListener(final PropertyChangeListener propertyChangeListener) {
		}

		public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener propertyChangeListener) {
		}

		public void removePropertyChangeListener(final PropertyChangeListener propertyChangeListener) {
		}

		public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener propertyChangeListener) {
		}

		public boolean isVisible() {
			return false;
		}

		public void setVisible(final boolean visible) {
		}

		public boolean isEnabled() {
			return false;
		}

		public void setEnabled(final boolean enabled) {
		}

		public void addFocusListener(final IFocusListener listener) {
		}

		public void removeFocusListener(final IFocusListener listener) {
		}

		public void updateFromModel() {
		}

		public void requestFocus() {
		}

		public boolean hasFocus() {
			return false;
		}

		public boolean isFocusable() {
			return false;
		}

		public void setFocusable(final boolean focusable) {
		}

		public String getToolTipText() {
			return null;
		}

		public void setToolTipText(final String toolTipText) {
		}

		public boolean isBlocked() {
			return false;
		}

		public void setBlocked(final boolean blocked) {
		}

		public String getID() {
			return null;
		}

		public IRidgetContainer getController() {
			return null;
		}

		public void setController(final IRidgetContainer controller) {
		}

		public void setIgnoreBindingError(final boolean ignore) {
		}

		public boolean isIgnoreBindingError() {
			return false;
		}
	}

	/**
	 * Another mock implementation of ridget.
	 */
	private static class MockRidget3 extends AbstractMockRidget {

	}

	private static class MockNonRidgetSuperClass implements Comparable<MockNonRidgetSuperClass> {

		public int compareTo(final MockNonRidgetSuperClass o) {
			return 0;
		}

	}

	private static class MockRidgetImplementigSeveralInterfaces extends MockNonRidgetSuperClass implements IMockRidget2, IAction {

		public Object getUIControl() {
			return null;
		}

		public void setUIControl(final Object uiControl) {
		}

		public void addPropertyChangeListener(final PropertyChangeListener propertyChangeListener) {
		}

		public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener propertyChangeListener) {
		}

		public void removePropertyChangeListener(final PropertyChangeListener propertyChangeListener) {
		}

		public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener propertyChangeListener) {
		}

		public boolean isVisible() {
			return false;
		}

		public void setVisible(final boolean visible) {
		}

		public boolean isEnabled() {
			return false;
		}

		public void setEnabled(final boolean enabled) {
		}

		public void addFocusListener(final IFocusListener listener) {
		}

		public void removeFocusListener(final IFocusListener listener) {
		}

		public void updateFromModel() {
		}

		public void requestFocus() {
		}

		public boolean hasFocus() {
			return false;
		}

		public boolean isFocusable() {
			return false;
		}

		public void setFocusable(final boolean focusable) {
		}

		public String getToolTipText() {
			return null;
		}

		public void setToolTipText(final String toolTipText) {
		}

		public boolean isBlocked() {
			return false;
		}

		public void setBlocked(final boolean blocked) {
		}

		public String getID() {
			return null;
		}

		public void run() {
		}

		public IRidgetContainer getController() {
			return null;
		}

		public void setController(final IRidgetContainer controller) {
		}

		public void setIgnoreBindingError(final boolean ignore) {
			// TODO Auto-generated method stub

		}

		public boolean isIgnoreBindingError() {
			// TODO Auto-generated method stub
			return false;
		}
	}

}
