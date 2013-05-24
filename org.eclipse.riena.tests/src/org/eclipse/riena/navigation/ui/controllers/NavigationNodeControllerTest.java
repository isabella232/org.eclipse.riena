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
package org.eclipse.riena.navigation.ui.controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Map;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.core.RienaStatus;
import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.internal.ui.ridgets.swt.LabelRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.TextRidget;
import org.eclipse.riena.navigation.INavigationNodeController;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.NavigationProcessor;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.OutputMarker;
import org.eclipse.riena.ui.ridgets.AbstractCompositeRidget;
import org.eclipse.riena.ui.ridgets.IBasicMarkableRidget;
import org.eclipse.riena.ui.ridgets.ICompositeRidget;
import org.eclipse.riena.ui.ridgets.IMenuItemRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.RidgetResolver;
import org.eclipse.riena.ui.ridgets.listener.IFocusListener;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link NavigationNodeController}.
 */
@UITestCase
public class NavigationNodeControllerTest extends RienaTestCase {

	private MyNavigationNodeController controller;
	private SubModuleNode node;
	private Shell shell;
	private NavigationProcessor navigationProcessor;
	private final static String SUBMODULE1_TYPE_ID = "subModule1TypeId"; //$NON-NLS-1$

	@Override
	protected void setUp() throws Exception {

		final Display display = Display.getDefault();
		shell = new Shell(display);
		shell.pack();
		shell.setVisible(true);

		final Realm realm = SWTObservables.getRealm(display);
		assertNotNull(realm);
		ReflectionUtils.invokeHidden(realm, "setDefault", realm); //$NON-NLS-1$

		node = new SubModuleNode(new NavigationNodeId(SUBMODULE1_TYPE_ID));
		navigationProcessor = new NavigationProcessor();
		node.setNavigationProcessor(navigationProcessor);
		controller = new MyNavigationNodeController(node);
	}

	@Override
	protected void tearDown() throws Exception {
		controller = null;
		node = null;
		SwtUtilities.dispose(shell);
	}

	public void testUpdateNavigationMarkersUniqueAndDisabled() throws Exception {
		assertTrue(node.getMarkers().isEmpty());

		final TextRidget r1 = new TextRidget();
		r1.setUIControl(new Text(shell, SWT.NONE));
		final MandatoryMarker m1 = new MandatoryMarker(true);
		r1.addMarker(m1);
		controller.addRidget("id1", r1); //$NON-NLS-1$
		controller.updateNavigationNodeMarkers();
		assertEquals(1, node.getMarkers().size());
		assertSame(m1, node.getMarkersOfType(MandatoryMarker.class).iterator().next());

		final TextRidget r2 = new TextRidget();
		r2.setUIControl(new Text(shell, SWT.NONE));
		final MandatoryMarker m2 = new MandatoryMarker(true);
		r2.addMarker(m2);
		controller.addRidget("id2", r2); //$NON-NLS-1$
		controller.updateNavigationNodeMarkers();
		// m2 was not added to the node since m1 is there
		assertEquals(1, node.getMarkers().size());
		assertSame(m1, node.getMarkersOfType(MandatoryMarker.class).iterator().next());

		m1.setDisabled(true); // triggers also controller.updateNavigationNodeMarkers();
		// m1 was removed since it is disabled, m2 was added to the node instead
		assertEquals(1, node.getMarkers().size());
		assertSame(m2, node.getMarkersOfType(MandatoryMarker.class).iterator().next());
	}

	/**
	 * Tests the method {@code addRidget}.
	 */
	public void testAddRidget() {

		final LabelRidget ridget = new LabelRidget();
		controller.addRidget("4711", ridget); //$NON-NLS-1$
		assertNotNull(controller.getRidgets());
		assertEquals(1, controller.getRidgets().size());

		final PropertyChangeSupport support = ReflectionUtils.getHidden(ridget, "propertyChangeSupport"); //$NON-NLS-1$
		assertNotNull(support.getPropertyChangeListeners());
		assertEquals(4, support.getPropertyChangeListeners().length);
	}

	/**
	 * Tests the method {@code updateNavigationNodeMarkers()}.
	 */
	public void testUpdateNavigationNodeMarkers() {

		final TextRidget ridget = new TextRidget();
		ridget.setUIControl(new Text(shell, 0));
		ridget.addMarker(new ErrorMarker());
		controller.addRidget("4711", ridget); //$NON-NLS-1$
		controller.updateNavigationNodeMarkers();
		assertFalse(node.getMarkers().isEmpty());
		assertFalse(node.getMarkersOfType(ErrorMarker.class).isEmpty());

		final MandatoryMarker mandatory1 = new MandatoryMarker();
		ridget.addMarker(mandatory1);
		controller.updateNavigationNodeMarkers();
		assertFalse(node.getMarkers().isEmpty());
		assertFalse(node.getMarkersOfType(ErrorMarker.class).isEmpty());
		assertFalse(node.getMarkersOfType(MandatoryMarker.class).isEmpty());

		ridget.setText("testtext"); //$NON-NLS-1$
		controller.updateNavigationNodeMarkers();
		assertFalse(node.getMarkers().isEmpty());
		assertFalse(node.getMarkersOfType(ErrorMarker.class).isEmpty());
		// the mandatory marker of the ridget is disabled because the text is not empty
		assertTrue(node.getMarkersOfType(MandatoryMarker.class).isEmpty());

		ridget.setText(null);

		ridget.addMarker(mandatory1);
		controller.updateNavigationNodeMarkers();
		assertFalse(node.getMarkersOfType(MandatoryMarker.class).isEmpty());

		mandatory1.setDisabled(true);

		final MandatoryMarker mandatory2 = new MandatoryMarker();
		final TextRidget ridget2 = new TextRidget();
		ridget2.setUIControl(new Text(shell, 0));
		ridget2.addMarker(mandatory2);
		controller.addRidget("554", ridget2); //$NON-NLS-1$
		controller.updateNavigationNodeMarkers();
		assertFalse(node.getMarkersOfType(MandatoryMarker.class).isEmpty());
	}

	/**
	 * Tests the private method {@code getRidgetMarkers()}.
	 */
	public void testGetRidgetMarkers() {

		final TextRidget ridget = new TextRidget();
		ridget.setUIControl(new Text(shell, 0));
		controller.addRidget("4711", ridget); //$NON-NLS-1$
		final TextRidget ridget2 = new TextRidget();
		ridget2.setUIControl(new Text(shell, 0));
		controller.addRidget("0815", ridget2); //$NON-NLS-1$

		Collection<IMarker> markers = ReflectionUtils.invokeHidden(controller, "getRidgetMarkers", (Object[]) null); //$NON-NLS-1$
		assertNotNull(markers);

		final ErrorMarker errorMarker = new ErrorMarker();
		ridget.addMarker(errorMarker);
		final OutputMarker outputMarker = new OutputMarker();
		ridget2.addMarker(outputMarker);
		markers = ReflectionUtils.invokeHidden(controller, "getRidgetMarkers", (Object[]) null); //$NON-NLS-1$
		assertNotNull(markers);
		assertEquals(2, markers.size());
		assertTrue(markers.contains(errorMarker));
		assertTrue(markers.contains(outputMarker));

		final CompositeRidget compositeRidget = new CompositeRidget();
		final TextRidget ridget3 = new TextRidget();
		ridget3.setUIControl(new Text(shell, 0));
		compositeRidget.addRidget("label3", ridget3); //$NON-NLS-1$
		controller.addRidget("comp", compositeRidget); //$NON-NLS-1$
		final MandatoryMarker mandatoryMarker = new MandatoryMarker();
		ridget3.addMarker(mandatoryMarker);
		markers = ReflectionUtils.invokeHidden(controller, "getRidgetMarkers", (Object[]) null); //$NON-NLS-1$
		assertNotNull(markers);
		assertEquals(3, markers.size());
		assertTrue(markers.contains(mandatoryMarker));

	}

	public void testGetRidget() throws Exception {
		System.getProperties().put(RienaStatus.RIENA_TEST_SYSTEM_PROPERTY, "true"); //$NON-NLS-1$
		IRidget ridget = controller.getRidget(MockRidget.class, "myMock"); //$NON-NLS-1$
		assertEquals(MockRidget.class, ridget.getClass());

		try {
			ridget = controller.getRidget(IMockRidget.class, "myMockInterface"); //$NON-NLS-1$
			fail("BindingException expected"); //$NON-NLS-1$
		} catch (final BindingException e) {
			ok("BindingException expected"); //$NON-NLS-1$
		} finally {
			System.getProperties().put(RienaStatus.RIENA_TEST_SYSTEM_PROPERTY, "false"); //$NON-NLS-1$
		}
	}

	/**
	 * Tests the <i>private</i> method {@code getChildId(String)}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testGetChildId() throws Exception {
		final RidgetResolver ridgetResolver = ReflectionUtils.getHidden(controller, "ridgetResolver");

		String childId = ReflectionUtils.invokeHidden(ridgetResolver, "getChildId", "aa.bbb.cccc"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("cccc", childId); //$NON-NLS-1$

		childId = ReflectionUtils.invokeHidden(ridgetResolver, "getChildId", "def"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("def", childId); //$NON-NLS-1$

	}

	/**
	 * Tests the <i>private</i> method {@code getContainer(String)}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testGetContainer() throws Exception {
		final RidgetResolver ridgetResolver = ReflectionUtils.getHidden(controller, "ridgetResolver"); //$NON-NLS-1$
		final Map<String, IRidget> ridgets = ReflectionUtils.getHidden(controller, "ridgets"); //$NON-NLS-1$

		IRidgetContainer container = ReflectionUtils.invokeHidden(ridgetResolver, "getContainer", "container.child", ridgets); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(container);

		final ICompositeRidget composite = new org.eclipse.riena.internal.ui.ridgets.swt.CompositeRidget();
		controller.addRidget("container", composite); //$NON-NLS-1$

		container = ReflectionUtils.invokeHidden(ridgetResolver, "getContainer", "container.child", ridgets); //$NON-NLS-1$ //$NON-NLS-2$
		assertSame(composite, container);

		container = ReflectionUtils.invokeHidden(ridgetResolver, "getContainer", "container", ridgets); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(container);

	}

	/**
	 * Test for bug 269131.
	 */
	public void testHiddenAndDisabledMarkersBlockAllRidgetMarkers() throws Exception {

		final TextRidget ridget = new TextRidget();
		ridget.setUIControl(new Text(shell, 0));
		controller.addRidget("4711", ridget); //$NON-NLS-1$
		final TextRidget ridget2 = new TextRidget();
		ridget2.setUIControl(new Text(shell, 0));
		controller.addRidget("0815", ridget2); //$NON-NLS-1$

		final IMarker errorMarker = new ErrorMarker();
		final IMarker mandatoryMarker = new MandatoryMarker();
		final IMarker hiddenMarker = new HiddenMarker();
		final IMarker disabledMarker = new DisabledMarker();

		ridget.addMarker(errorMarker);
		ridget.addMarker(mandatoryMarker);

		Collection<IMarker> markers = ReflectionUtils.invokeHidden(controller, "getRidgetMarkers", (Object[]) null); //$NON-NLS-1$
		assertEquals(2, markers.size());
		assertTrue(markers.contains(errorMarker));
		assertTrue(markers.contains(mandatoryMarker));

		ridget.addMarker(hiddenMarker);

		markers = ReflectionUtils.invokeHidden(controller, "getRidgetMarkers", (Object[]) null); //$NON-NLS-1$
		assertTrue(markers.isEmpty());

		ridget2.addMarker(errorMarker);
		ridget2.addMarker(mandatoryMarker);

		markers = ReflectionUtils.invokeHidden(controller, "getRidgetMarkers", (Object[]) null); //$NON-NLS-1$
		assertEquals(2, markers.size());
		assertTrue(markers.contains(errorMarker));
		assertTrue(markers.contains(mandatoryMarker));

		ridget2.addMarker(disabledMarker);

		markers = ReflectionUtils.invokeHidden(controller, "getRidgetMarkers", (Object[]) null); //$NON-NLS-1$
		assertTrue(markers.isEmpty());

		ridget.removeMarker(hiddenMarker);

		markers = ReflectionUtils.invokeHidden(controller, "getRidgetMarkers", (Object[]) null); //$NON-NLS-1$
		assertEquals(2, markers.size());
		assertTrue(markers.contains(errorMarker));
		assertTrue(markers.contains(mandatoryMarker));
	}

	/**
	 * Tests the method {@link INavigationNodeController#navigationArgumentChanged(NavigationArgument)}
	 */
	public void testNavigationArgumentChanged() throws Exception {
		final ModuleNode module = new ModuleNode(new NavigationNodeId("myModuleNode")); //$NON-NLS-1$
		module.setNavigationProcessor(navigationProcessor);
		module.addChild(node);
		final SubModuleNode node2 = new SubModuleNode();
		module.addChild(node2);
		node2.activate();
		final NavigationArgument arg = new NavigationArgument(new Object());

		assertNull(controller.lastArgument);
		node2.navigate(new NavigationNodeId(SUBMODULE1_TYPE_ID), arg);
		assertTrue(node.isActivated());
		assertSame(arg, controller.lastArgument);
	}

	@SuppressWarnings("unchecked")
	public void testHideShowMarkerFiresEvent() {
		final ITextRidget ridget = new TextRidget();
		final FTPropertyChangeListener listener = new FTPropertyChangeListener();
		ridget.addPropertyChangeListener(IBasicMarkableRidget.PROPERTY_MARKER_HIDING, listener);

		assertEquals(0, listener.count);

		ridget.hideMarkersOfType(ErrorMarker.class);

		assertEquals(1, listener.count);

		ridget.hideMarkersOfType(ErrorMarker.class);

		assertEquals(1, listener.count);

		ridget.showMarkersOfType(MandatoryMarker.class);

		assertEquals(1, listener.count);

		ridget.showMarkersOfType(ErrorMarker.class);

		assertEquals(2, listener.count);

		ridget.showMarkersOfType(ErrorMarker.class);

		assertEquals(2, listener.count);
	}

	// helping classes
	//////////////////

	public static class MyNavigationNodeController extends SubModuleController {
		private NavigationArgument lastArgument = null;

		public MyNavigationNodeController(final ISubModuleNode navigationNode) {
			super(navigationNode);
		}

		@Override
		public void updateNavigationNodeMarkers() {
			super.updateNavigationNodeMarkers();
		}

		@Override
		public void navigationArgumentChanged(final NavigationArgument argument) {
			lastArgument = argument;
		}

	}

	private static class CompositeRidget extends AbstractCompositeRidget {
	}

	private interface IMockRidget extends IRidget {
	}

	/**
	 * Mock implementation of ridget.
	 */
	public static class MockRidget implements IMockRidget {

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

		public IMenuItemRidget addMenuItem(final String menuItemText) {
			return null;
		}

		public IMenuItemRidget addMenuItem(final String menuItemText, final String iconName) {
			return null;
		}

		public void removeMenuItem(final String menuItemText) {
		}

		public void removeMenuItem(final IMenuItemRidget menuItemRidget) {
		}

		public IMenuItemRidget getMenuItem(final int index) {
			return null;
		}

		public int getMenuItemCount() {
			return 0;
		}

		public void updateContextMenu() {
		}

	}

	private static final class FTPropertyChangeListener implements PropertyChangeListener {

		private int count;

		public void propertyChange(final PropertyChangeEvent evt) {
			count++;
		}
	}

}
