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
package org.eclipse.riena.ui.ridgets.tree2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.tests.base.PropertyChangeEventEquals;

/**
 * Tests for the class {@link TreeNode}.
 */
@NonUITestCase
public class TreeNodeTest extends TestCase {

	private PropertyChangeListener propertyChangeListenerMock;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		propertyChangeListenerMock = EasyMock.createMock(PropertyChangeListener.class);
	}

	public void testGetSetValue() {
		final TreeNode root = new TreeNode("value");
		root.addPropertyChangeListener(propertyChangeListenerMock);

		assertEquals("value", root.getValue());

		expectPropertyChangeEvent(root, ITreeNode.PROPERTY_VALUE, "value", "newValue");
		root.setValue("newValue");
		verifyPropertyChangeEvents();
		assertEquals("newValue", root.getValue());

		expectNoPropertyChangeEvent();
		root.setValue("newValue");
		verifyPropertyChangeEvents();
		assertEquals("newValue", root.getValue());

		expectPropertyChangeEvent(root, ITreeNode.PROPERTY_VALUE, "newValue", null);
		root.setValue(null);
		verifyPropertyChangeEvents();
		assertNull(root.getValue());
	}

	public void testGetParent() {
		final TreeNode root = new TreeNode("value");

		assertNull(root.getParent());

		final TreeNode child = new TreeNode(root, "child1");

		assertNull(root.getParent());
		assertSame(root, child.getParent());
	}

	public void testGetChildren() {
		final TreeNode root = new TreeNode("value");

		assertEquals(0, root.getChildren().size());

		final TreeNode child1 = new TreeNode(root, "child1");
		final TreeNode child2 = new TreeNode(root, "child2");

		assertEquals(2, root.getChildren().size());
		assertTrue(root.getChildren().contains(child1));
		assertTrue(root.getChildren().contains(child2));

		// check that we don't expose internal list
		root.getChildren().remove(0);
		assertEquals(2, root.getChildren().size());
	}

	public void testSetChildren() {
		final TreeNode root = new TreeNode("value");
		root.addPropertyChangeListener(propertyChangeListenerMock);
		final TreeNode child1 = new TreeNode(root, "child1");
		final TreeNode child2 = new TreeNode(root, "child2");

		assertEquals(2, root.getChildren().size());

		final List<ITreeNode> twoChildren = root.getChildren();
		final List<ITreeNode> oneChild = root.getChildren();
		oneChild.remove(child1);
		expectPropertyChangeEvent(root, ITreeNode.PROPERTY_CHILDREN, twoChildren, oneChild);
		root.setChildren(oneChild);
		verifyPropertyChangeEvents();

		assertEquals(1, root.getChildren().size());
		assertTrue(root.getChildren().contains(child2));
	}

	public void testSetEnabled() {
		final TreeNode root = new TreeNode("value");
		root.addPropertyChangeListener(propertyChangeListenerMock);

		assertTrue(root.isEnabled());

		expectPropertyChangeEvent(root, ITreeNode2.PROPERTY_ENABLED, true, false);
		root.setEnabled(false);
		verifyPropertyChangeEvents();
		assertFalse(root.isEnabled());

		expectNoPropertyChangeEvent();
		root.setEnabled(false);
		verifyPropertyChangeEvents();

		expectPropertyChangeEvent(root, ITreeNode2.PROPERTY_ENABLED, false, true);
		root.setEnabled(true);
		verifyPropertyChangeEvents();
		assertTrue(root.isEnabled());
	}

	public void testSetVisible() {
		final TreeNode root = new TreeNode("value");
		root.addPropertyChangeListener(propertyChangeListenerMock);

		assertTrue(root.isVisible());

		expectPropertyChangeEvent(root, ITreeNode2.PROPERTY_VISIBLE, true, false);
		root.setVisible(false);
		verifyPropertyChangeEvents();
		assertFalse(root.isVisible());

		expectNoPropertyChangeEvent();
		root.setVisible(false);
		verifyPropertyChangeEvents();

		expectPropertyChangeEvent(root, ITreeNode2.PROPERTY_VISIBLE, false, true);
		root.setVisible(true);
		verifyPropertyChangeEvents();
		assertTrue(root.isVisible());
	}

	// helping methods
	//////////////////

	private PropertyChangeEvent createArgumentMatcher(final PropertyChangeEvent propertyChangeEvent) {
		return PropertyChangeEventEquals.eqPropertyChangeEvent(propertyChangeEvent);
	}

	private void expectNoPropertyChangeEvent() {
		EasyMock.reset(propertyChangeListenerMock);
		EasyMock.replay(propertyChangeListenerMock);
	}

	private void expectPropertyChangeEvents(final PropertyChangeEvent... propertyChangeEvents) {
		EasyMock.reset(propertyChangeListenerMock);
		for (final PropertyChangeEvent propertyChangeEvent : propertyChangeEvents) {
			propertyChangeListenerMock.propertyChange(createArgumentMatcher(propertyChangeEvent));
		}
		EasyMock.replay(propertyChangeListenerMock);
	}

	private void expectPropertyChangeEvent(final Object bean, final String propertyName, final Object oldValue,
			final Object newValue) {
		expectPropertyChangeEvents(new PropertyChangeEvent(bean, propertyName, oldValue, newValue));
	}

	private void verifyPropertyChangeEvents() {
		EasyMock.verify(propertyChangeListenerMock);
	}

}
