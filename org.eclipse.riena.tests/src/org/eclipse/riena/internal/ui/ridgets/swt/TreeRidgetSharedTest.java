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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;

import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.tree2.ITreeNode;
import org.eclipse.riena.ui.ridgets.tree2.TreeNode;

/**
 * Test for shared {@link Tree}
 */
public class TreeRidgetSharedTest extends AbstractRidgetSharedTestCase {

	private static final String ROOT_NODE_USER_OBJECT = "TestRootNode";
	private static final String ROOT_CHILD1_NODE_USER_OBJECT = "TestRootChild1Node";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	private ITreeNode[] initializeTreeModel() {
		final TreeNode rootNode = new TreeNode(ROOT_NODE_USER_OBJECT);
		new TreeNode(rootNode, ROOT_CHILD1_NODE_USER_OBJECT);
		return new ITreeNode[] { rootNode };
	}

	@Override
	protected Control createWidget(final Composite parent) {
		return new Tree(parent, SWT.NONE);
	}

	@Override
	protected Tree getWidget() {
		return (Tree) super.getWidget();
	}

	@Override
	protected TreeRidget getRidget1() {
		return (TreeRidget) super.getRidget1();
	}

	@Override
	protected TreeRidget getRidget2() {
		return (TreeRidget) super.getRidget2();
	}

	@Override
	protected IRidget createRidget() {
		return new TreeRidget();
	}

	public void testExpand() throws Exception {
		getRidget1().bindToModel(initializeTreeModel(), ITreeNode.class, ITreeNode.PROPERTY_CHILDREN,
				ITreeNode.PROPERTY_PARENT, ITreeNode.PROPERTY_VALUE);
		activateRidget1();
		getRidget1().updateFromModel();

		getRidget2().bindToModel(initializeTreeModel(), ITreeNode.class, ITreeNode.PROPERTY_CHILDREN,
				ITreeNode.PROPERTY_PARENT, ITreeNode.PROPERTY_VALUE);
		activateRidget2();
		getRidget2().updateFromModel();

		getWidget().getItem(0).setExpanded(true);
		assertEquals(ROOT_CHILD1_NODE_USER_OBJECT, getWidget().getItem(0).getItem(0).getText());

	}
}
