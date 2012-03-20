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

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.ui.swt.test.UITestHelper;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.listener.SelectionEvent;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.tree2.ITreeNode;
import org.eclipse.riena.ui.ridgets.tree2.TreeNode;

/**
 * Tests for the {@link TreeRidget}.
 * <p>
 * These tests use a TreeNode that wraps a String and focus on methods from the
 * {@link ITreeRidget}.
 * 
 * @see TreeRidgetTest2
 */
public class TreeRidgetTest extends AbstractSWTRidgetTest {

	private static final String ROOT_NODE_USER_OBJECT = "TestRootNode";
	private static final String ROOT_CHILD1_NODE_USER_OBJECT = "TestRootChild1Node";
	private static final String ROOT_CHILD2_NODE_USER_OBJECT = "TestRootChild2Node";
	private static final String ROOT_CHILD1_CHILD1_NODE_USER_OBJECT = "TestRootChild1Child1Node";
	private static final String ROOT_CHILD1_CHILD2_NODE_USER_OBJECT = "TestRootChild1Child2Node";
	private static final String ROOT_CHILD1_CHILD2_CHILD_NODE_USER_OBJECT = "TestRootChild1Child2ChildNode";

	private ITreeNode rootNode;
	private ITreeNode rootChild1Node;
	private ITreeNode rootChild2Node;
	private ITreeNode rootChild1Child2Node;
	private ITreeNode rootChild1Child1Node;
	private ITreeNode rootChild1Child2ChildNode;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		getRidget().bindToModel(initializeTreeModel(), ITreeNode.class, ITreeNode.PROPERTY_CHILDREN,
				ITreeNode.PROPERTY_PARENT, ITreeNode.PROPERTY_VALUE);
	}

	@Override
	protected Control createWidget(final Composite parent) {
		return new Tree(parent, SWT.MULTI);
	}

	@Override
	protected IRidget createRidget() {
		return new TreeRidget();
	}

	@Override
	protected Tree getWidget() {
		return (Tree) super.getWidget();
	}

	@Override
	protected ITreeRidget getRidget() {
		return (ITreeRidget) super.getRidget();
	}

	// testing methods
	// ////////////////

	public void testRidgetMapping() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(TreeRidget.class, mapper.getRidgetClass(getWidget()));
	}

	public void testGetUIControl() throws Exception {
		final Tree control = getWidget();
		assertEquals(control, getRidget().getUIControl());
	}

	public void testBindToModel() {
		assertEquals(ROOT_NODE_USER_OBJECT, getUIControlItem(0).getText(0));
		assertEquals(ROOT_CHILD1_NODE_USER_OBJECT, getUIControlItem(1).getText(0));
		assertEquals(ROOT_CHILD1_CHILD1_NODE_USER_OBJECT, getUIControlItem(2).getText(0));
		assertEquals(ROOT_CHILD2_NODE_USER_OBJECT, getUIControlItem(3).getText(0));
	}

	public void testBindToModelNull() {
		final ITreeRidget ridget = getRidget();
		final ITreeNode[] roots = { rootNode };

		try {
			ridget.bindToModel(null, ITreeNode.class, ITreeNode.PROPERTY_CHILDREN, ITreeNode.PROPERTY_PARENT,
					ITreeNode.PROPERTY_VALUE);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		try {
			ridget.bindToModel(roots, null, ITreeNode.PROPERTY_CHILDREN, ITreeNode.PROPERTY_PARENT,
					ITreeNode.PROPERTY_VALUE);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		try {
			ridget.bindToModel(roots, ITreeNode.class, null, ITreeNode.PROPERTY_PARENT, ITreeNode.PROPERTY_VALUE);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		try {
			ridget.bindToModel(roots, ITreeNode.class, ITreeNode.PROPERTY_CHILDREN, null, ITreeNode.PROPERTY_VALUE);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		try {
			ridget.bindToModel(roots, ITreeNode.class, ITreeNode.PROPERTY_CHILDREN, ITreeNode.PROPERTY_PARENT, null);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
	}

	public void testUpdateTreeFromModel() {
		final ITreeRidget ridget = getRidget();
		final Tree control = getWidget();

		ridget.expandAll();

		assertEquals(1, control.getItemCount());
		final TreeItem treeRoot = control.getItems()[0];
		assertEquals(ROOT_NODE_USER_OBJECT, treeRoot.getText());
		assertEquals(2, treeRoot.getItemCount());

		final TreeItem treeRootChild1 = treeRoot.getItem(0);
		assertEquals(ROOT_CHILD1_NODE_USER_OBJECT, treeRootChild1.getText());
		final TreeItem treeRootChild2 = treeRoot.getItem(1);
		assertEquals(ROOT_CHILD2_NODE_USER_OBJECT, treeRootChild2.getText());
		assertEquals(2, treeRootChild1.getItemCount());
		assertEquals(0, treeRootChild2.getItemCount());

		final TreeItem treeRootChild1Child1 = treeRootChild1.getItem(0);
		assertEquals(ROOT_CHILD1_CHILD1_NODE_USER_OBJECT, treeRootChild1Child1.getText());
		final TreeItem treeRootChild1Child2 = treeRootChild1.getItem(1);
		assertEquals(ROOT_CHILD1_CHILD2_NODE_USER_OBJECT, treeRootChild1Child2.getText());
		assertEquals(0, treeRootChild1Child1.getItemCount());
		assertEquals(1, treeRootChild1Child2.getItemCount());

		final TreeItem treeRootChild1Child2Child = treeRootChild1Child2.getItem(0);
		assertEquals(ROOT_CHILD1_CHILD2_CHILD_NODE_USER_OBJECT, treeRootChild1Child2Child.getText());
		assertEquals(0, treeRootChild1Child2Child.getItemCount());
	}

	public void testUpdateOnModelChanges() {
		final Tree control = getWidget();

		assertEquals(3, TreeUtils.getItemCount(control));

		removeFromParent(rootChild2Node);

		assertEquals(2, TreeUtils.getItemCount(control));

		new TreeNode(rootNode, "TestNewNode1");
		new TreeNode(rootNode, "TestNewNode2");

		assertEquals(4, TreeUtils.getItemCount(control));
	}

	public void testExpandAndCollapseTree() {
		final ITreeRidget ridget = getRidget();
		final Tree control = getWidget();

		assertEquals(3, TreeUtils.getItemCount(control));

		ridget.expandAll();

		assertEquals(6, TreeUtils.getItemCount(control));

		ridget.collapseAll();

		assertEquals(1, TreeUtils.getItemCount(control));

		// check that the tree was collapsed recursively...

		ridget.expand(rootNode);

		assertEquals(3, TreeUtils.getItemCount(control));

		ridget.expandAll();

		assertEquals(6, TreeUtils.getItemCount(control));

		// ...to check that it collapses to its default state when a new model
		// is updated...
		getRidget().bindToModel(initializeTreeModel(), ITreeNode.class, ITreeNode.PROPERTY_CHILDREN,
				ITreeNode.PROPERTY_PARENT, ITreeNode.PROPERTY_VALUE);

		assertEquals(3, TreeUtils.getItemCount(control));

		ridget.expandAll();

		assertEquals(6, TreeUtils.getItemCount(control));

		// ...update from model should preserver expansion state...
		ridget.updateFromModel();

		assertEquals(6, TreeUtils.getItemCount(control));
	}

	/**
	 * The expandTree and collapseTree methods do not affect the control when
	 * unbound, but are applied to the control after rebinding.
	 */
	public void testExpandAndCollapseTreeWhenUnbound() {
		final ITreeRidget ridget = getRidget();
		final Tree control = getWidget();

		assertEquals(3, TreeUtils.getItemCount(control));

		ridget.setUIControl(null);
		ridget.expandAll();
		ridget.setUIControl(control);

		assertEquals(6, TreeUtils.getItemCount(control));

		ridget.setUIControl(null);
		ridget.collapseAll();
		ridget.setUIControl(control);

		assertEquals(1, TreeUtils.getItemCount(control));
	}

	public void testExpandAndCollapseSingleNodes() {
		final ITreeRidget ridget = getRidget();
		final Tree control = getWidget();

		ridget.expandAll();

		assertEquals(6, TreeUtils.getItemCount(control));

		ridget.collapse(rootChild1Child2Node);

		assertEquals(5, TreeUtils.getItemCount(control));

		ridget.collapse(rootChild1Node);

		assertEquals(3, TreeUtils.getItemCount(control));

		ridget.expand(rootChild1Node);

		assertEquals(5, TreeUtils.getItemCount(control));

		ridget.expand(rootChild1Child2Node);

		assertEquals(6, TreeUtils.getItemCount(control));
	}

	/**
	 * The expand and collapse methods do not affect the control when unbound,
	 * but are applied to the control after rebinding.
	 */
	public void testExpandAndCollapseSingleNodesWhenUnbound() {
		final ITreeRidget ridget = getRidget();
		final Tree control = getWidget();

		assertEquals(3, TreeUtils.getItemCount(control));

		ridget.setUIControl(null);
		ridget.expand(rootChild1Node);
		ridget.setUIControl(control);

		assertEquals(5, TreeUtils.getItemCount(control));

		ridget.setUIControl(null);
		ridget.collapse(rootNode);
		ridget.setUIControl(control);

		assertEquals(1, TreeUtils.getItemCount(control));
	}

	public void testFullExpansionStatusIsPreserved() {
		final ITreeRidget ridget = getRidget();
		final Tree control = getWidget();

		ridget.expandAll();

		assertEquals(6, TreeUtils.getItemCount(control));

		ridget.setUIControl(null);
		ridget.setUIControl(control);

		assertEquals(6, TreeUtils.getItemCount(control));

		ridget.collapseAll();

		assertEquals(1, TreeUtils.getItemCount(control));

		ridget.setUIControl(null);
		ridget.setUIControl(control);

		assertEquals(1, TreeUtils.getItemCount(control));
	}

	public void testPartialExpansionStatusIsPreserved() {
		final ITreeRidget ridget = getRidget();
		final Tree control = getWidget();

		assertEquals(3, TreeUtils.getItemCount(control));

		ridget.expand(rootChild1Node);

		assertEquals(5, TreeUtils.getItemCount(control));

		ridget.setUIControl(null);
		ridget.setUIControl(control);

		assertEquals(5, TreeUtils.getItemCount(control));

		ridget.collapse(rootNode);

		assertEquals(1, TreeUtils.getItemCount(control));

		ridget.setUIControl(null);
		ridget.setUIControl(control);

		assertEquals(1, TreeUtils.getItemCount(control));
	}

	public void testAddDoubleClickListener() {
		final ITreeRidget ridget = getRidget();
		final Tree control = getWidget();

		try {
			ridget.addDoubleClickListener(null);
			fail();
		} catch (final RuntimeException npe) {
			ok();
		}

		final FTActionListener listener1 = new FTActionListener();
		ridget.addDoubleClickListener(listener1);

		final FTActionListener listener2 = new FTActionListener();
		ridget.addDoubleClickListener(listener2);
		ridget.addDoubleClickListener(listener2);

		final Event doubleClick = new Event();
		doubleClick.widget = control;
		doubleClick.type = SWT.MouseDoubleClick;
		control.notifyListeners(SWT.MouseDoubleClick, doubleClick);

		assertEquals(1, listener1.getCount());
		assertEquals(1, listener2.getCount());

		ridget.removeDoubleClickListener(listener1);

		control.notifyListeners(SWT.MouseDoubleClick, doubleClick);

		assertEquals(1, listener1.getCount());
	}

	public void testAddSelectionListener() {
		final ITreeRidget ridget = getRidget();
		final Tree control = getWidget();

		try {
			ridget.addSelectionListener(null);
			fail();
		} catch (final RuntimeException npe) {
			ok();
		}

		final TestSelectionListener selectionListener = new TestSelectionListener();

		ridget.addSelectionListener(selectionListener);
		assertEquals(0, ridget.getSelection().size());
		assertEquals(0, control.getSelectionCount());

		control.setFocus();
		UITestHelper.sendKeyAction(control.getDisplay(), UITestHelper.KC_ARROW_DOWN);

		assertEquals(1, ridget.getSelection().size());
		assertEquals(1, control.getSelectionCount());
		assertEquals(1, selectionListener.getCount());
		final SelectionEvent selectionEvent = selectionListener.getSelectionEvent();
		assertEquals(ridget, selectionEvent.getSource());
		assertTrue(selectionEvent.getOldSelection().isEmpty());
		assertEquals(ridget.getSelection(), selectionEvent.getNewSelection());
		// System.out.println("SelectionEvent: " + selectionListener.getSelectionEvent());

		UITestHelper.sendKeyAction(control.getDisplay(), UITestHelper.KC_ARROW_DOWN);

		assertEquals(1, ridget.getSelection().size());
		assertEquals(1, control.getSelectionCount());
		assertEquals(2, selectionListener.getCount());
		final SelectionEvent selectionEvent2 = selectionListener.getSelectionEvent();
		assertEquals(ridget, selectionEvent.getSource());
		assertEquals(selectionEvent.getNewSelection(), selectionEvent2.getOldSelection());
		assertEquals(ridget.getSelection(), selectionEvent2.getNewSelection());
		// System.out.println("SelectionEvent: " + selectionListener.getSelectionEvent());

		ridget.removeSelectionListener(selectionListener);
	}

	public void testBug266042() {
		final ITreeRidget ridget = getRidget();
		final Tree control = getWidget();
		final TreeNode root1 = new TreeNode("r1");
		new TreeNode(root1, "r1.a");
		new TreeNode(root1, "r1.b");
		final TreeNode root2 = new TreeNode("r2");
		new TreeNode(root2, "r2.a");
		new TreeNode(root2, "r2.b");

		ridget.bindToModel(new Object[] { root1, root2 }, TreeNode.class, ITreeNode.PROPERTY_CHILDREN,
				ITreeNode.PROPERTY_PARENT, ITreeNode.PROPERTY_VALUE);

		assertEquals(2, TreeUtils.getItemCount(control));
		assertEquals("r1", control.getItem(0).getText());
		assertEquals("r2", control.getItem(1).getText());
	}

	public void testBug290365() {
		final ITreeRidget ridget = getRidget();

		final TreeNodeWithIcon root1 = new TreeNodeWithIcon(null, "r1");
		new TreeNodeWithIcon(root1, "r1.a");
		new TreeNodeWithIcon(root1, "r1.b");
		ridget.bindToModel(new Object[] { root1 }, TreeNodeWithIcon.class, ITreeNode.PROPERTY_CHILDREN,
				ITreeNode.PROPERTY_PARENT, ITreeNode.PROPERTY_VALUE, null, null, "icon");

		ridget.updateFromModel(); // bug: throws exception

		ok();
	}

	/**
	 * As per bug 277283.
	 */
	public void testSetRootsVisibleFiresEvents() {
		final ITreeRidget ridget = getRidget();

		assertTrue(ridget.getRootsVisible());

		expectPropertyChangeEvent(ITreeRidget.PROPERTY_ROOTS_VISIBLE, true, false);
		ridget.setRootsVisible(false);
		verifyPropertyChangeEvents();

		assertFalse(ridget.getRootsVisible());

		expectPropertyChangeEvent(ITreeRidget.PROPERTY_ROOTS_VISIBLE, false, true);
		ridget.setRootsVisible(true);
		verifyPropertyChangeEvents();

		assertTrue(ridget.getRootsVisible());

		expectNoPropertyChangeEvent();
		ridget.setRootsVisible(true);
		verifyPropertyChangeEvents();
	}

	/**
	 * As per bug 300014
	 */
	public void testRefreshNull() {
		final ITreeRidget ridget = getRidget();
		final Tree control = getWidget();

		final TreeNode root = new TreeNode(null) {
			private Object myvalue;

			// hack to create an implementation that does not fire an event
			@Override
			public void setValue(final Object value) {
				myvalue = value;
			}

			@Override
			public Object getValue() {
				return myvalue;
			}
		};
		root.setValue("hello world");
		ridget.bindToModel(new Object[] { root }, TreeNode.class, TreeNode.PROPERTY_CHILDREN, TreeNode.PROPERTY_PARENT,
				TreeNode.PROPERTY_VALUE);
		ridget.updateFromModel();

		final TreeItem treeRoot = control.getItems()[0];
		assertEquals("hello world", treeRoot.getText());

		root.setValue("changed");

		assertEquals("hello world", treeRoot.getText());

		ridget.refresh(null);

		assertEquals("changed", treeRoot.getText());
	}

	/**
	 * As per bug 300014
	 */
	public void testRefresh() {
		final ITreeRidget ridget = getRidget();
		final Tree control = getWidget();

		final TreeNode root = new TreeNode(null) {
			private Object myvalue;

			// hack to create an implementation that does not fire an event
			@Override
			public void setValue(final Object value) {
				myvalue = value;
			}

			@Override
			public Object getValue() {
				return myvalue;
			}
		};
		root.setValue("hello world");
		ridget.bindToModel(new Object[] { root }, TreeNode.class, TreeNode.PROPERTY_CHILDREN, TreeNode.PROPERTY_PARENT,
				TreeNode.PROPERTY_VALUE);
		ridget.updateFromModel();

		final TreeItem treeRoot = control.getItems()[0];
		assertEquals("hello world", treeRoot.getText());

		root.setValue("changed");

		assertEquals("hello world", treeRoot.getText());

		ridget.refresh(root);

		assertEquals("changed", treeRoot.getText());
	}

	/**
	 * Tests the private method {@code collectChildren}.
	 */
	public void testCollectChildren() {

		initializeTreeModel();
		final List<ITreeNode> result = new ArrayList<ITreeNode>();
		ReflectionUtils.invokeHidden(getRidget(), "collectChildren", rootNode, result);
		assertEquals(5, result.size());
		assertSame(rootChild1Child1Node, result.get(0));
		assertSame(rootChild1Child2ChildNode, result.get(1));
		assertSame(rootChild1Child2Node, result.get(2));
		assertSame(rootChild1Node, result.get(3));
		assertSame(rootChild2Node, result.get(4));

	}

	/**
	 * Tests the private method {@code addExpansionCommandsForRootDescendants}.
	 */
	public void testAddExpansionCommandsForRootDescendants() {
		final ITreeNode[] treeModel = initializeTreeModel();
		getRidget().bindToModel(treeModel, ITreeNode.class, ITreeNode.PROPERTY_CHILDREN, ITreeNode.PROPERTY_PARENT,
				ITreeNode.PROPERTY_VALUE, null, null, null, null, ExpandedTreeNode.PROPERTY_EXPANDED);
		final Queue<?> expansionStack = ReflectionUtils.getHidden(getRidget(), "expansionStack");
		expansionStack.clear();
		ReflectionUtils.invokeHidden(getRidget(), "addExpansionCommandsForRootDescendants", treeModel[0]);
		assertEquals(2, expansionStack.size());
	}

	/**
	 * Tests the private method {@code isLeaf(Object)}.
	 */
	public void testIsLeaf() {
		initializeTreeModel();
		boolean ret = ReflectionUtils.invokeHidden(getRidget(), "isLeaf", rootChild1Node);
		assertFalse(ret);
		ret = ReflectionUtils.invokeHidden(getRidget(), "isLeaf", rootChild1Child2ChildNode);
		assertTrue(ret);
	}

	// helping methods
	// ////////////////

	private ITreeNode[] initializeTreeModel() {
		rootNode = new ExpandedTreeNode(ROOT_NODE_USER_OBJECT);
		rootChild1Node = new ExpandedTreeNode(rootNode, ROOT_CHILD1_NODE_USER_OBJECT, false);
		rootChild2Node = new ExpandedTreeNode(rootNode, ROOT_CHILD2_NODE_USER_OBJECT);
		rootChild1Child1Node = new ExpandedTreeNode(rootChild1Node, ROOT_CHILD1_CHILD1_NODE_USER_OBJECT);
		rootChild1Child2Node = new ExpandedTreeNode(rootChild1Node, ROOT_CHILD1_CHILD2_NODE_USER_OBJECT, true);
		rootChild1Child2ChildNode = new ExpandedTreeNode(rootChild1Child2Node,
				ROOT_CHILD1_CHILD2_CHILD_NODE_USER_OBJECT);
		return new ITreeNode[] { rootNode };
	}

	/** Removes the give node from its parent (if it has one). */
	private void removeFromParent(final ITreeNode node) {
		final ITreeNode parent = node.getParent();
		if (parent != null) {
			final List<ITreeNode> children = parent.getChildren();
			children.remove(node);
			parent.setChildren(children);
		}
	}

	/**
	 * Return the TreeItem corresponding to the following mock "index" scheme:
	 * 0: item for node1, 1: item for node2, 2: item for node3, 3: item for
	 * node4.
	 * <p>
	 * This method will fully expand the tree to ensure all tree items are
	 * created.
	 */
	private TreeItem getUIControlItem(final int index) {
		getRidget().expandAll();
		final Tree control = getWidget();
		switch (index) {
		case 0:
			return control.getItem(0);
		case 1:
			return control.getItem(0).getItem(0);
		case 2:
			return control.getItem(0).getItem(0).getItem(0);
		case 3:
			return control.getItem(0).getItem(1);
		default:
			throw new IndexOutOfBoundsException("index= " + index);
		}
	}

	// helping classes
	//////////////////

	/**
	 * Tree node with an icon
	 */
	private static final class TreeNodeWithIcon extends TreeNode {

		public TreeNodeWithIcon(final ITreeNode pParent, final Object pValue) {
			super(pParent, pValue);
		}

		@SuppressWarnings("unused")
		public String getIcon() {
			return "eclipse.gif";
		}
	}

	/**
	 * Tree node with an expanded flag.
	 */
	public static final class ExpandedTreeNode extends TreeNode {

		public final static String PROPERTY_EXPANDED = "expanded"; //$NON-NLS-1$

		private boolean expanded;

		public ExpandedTreeNode(final Object pValue) {
			super(pValue);
			setExpanded(true);
		}

		public ExpandedTreeNode(final ITreeNode pParent, final Object pValue) {
			this(pParent, pValue, true);
		}

		public ExpandedTreeNode(final ITreeNode pParent, final Object pValue, final boolean expanded) {
			super(pParent, pValue);
			setExpanded(expanded);
		}

		public boolean isExpanded() {
			return expanded;
		}

		public void setExpanded(final boolean expanded) {
			this.expanded = expanded;
		}
	}
}
