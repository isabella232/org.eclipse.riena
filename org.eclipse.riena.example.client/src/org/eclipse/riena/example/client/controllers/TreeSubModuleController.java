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
package org.eclipse.riena.example.client.controllers;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.example.client.views.TreeSubModuleView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.tree2.ITreeNode;
import org.eclipse.riena.ui.ridgets.tree2.TreeNode;

/**
 * Controller for the {@link TreeSubModuleView} example.
 */
public class TreeSubModuleController extends SubModuleController {

	private int nodeCount = 0;
	private IActionRidget buttonRename;
	private ITreeRidget tree;

	public TreeSubModuleController() {
		this(null);
	}

	public TreeSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.controllers.SubModuleController#afterBind()
	 */
	@Override
	public void afterBind() {
		super.afterBind();
		bindModel();
	}

	private void bindModel() {
		tree.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
		final TreeNode[] roots = createTreeInput();
		tree.setRootsVisible(false);
		tree.bindToModel(roots, TreeNode.class, TreeNode.PROPERTY_CHILDREN, TreeNode.PROPERTY_PARENT,
				TreeNode.PROPERTY_VALUE, TreeNode.PROPERTY_ENABLED, TreeNode.PROPERTY_VISIBLE);
		tree.setSelection(roots[0].getChildren().get(0));
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	@Override
	public void configureRidgets() {

		tree = getRidget("tree"); //$NON-NLS-1$
		final IActionRidget buttonAddSibling = getRidget("buttonAddSibling"); //$NON-NLS-1$
		final IActionRidget buttonAddChild = getRidget("buttonAddChild"); //$NON-NLS-1$
		buttonRename = getRidget("buttonRename"); //$NON-NLS-1$
		final IActionRidget buttonDelete = getRidget("buttonDelete"); //$NON-NLS-1$
		final IActionRidget buttonExpand = getRidget("buttonExpand"); //$NON-NLS-1$
		final IActionRidget buttonCollapse = getRidget("buttonCollapse"); //$NON-NLS-1$
		final IActionRidget buttonDisable = getRidget("buttonDisable"); //$NON-NLS-1$
		final IActionRidget buttonEnable = getRidget("buttonEnable"); //$NON-NLS-1$
		final IActionRidget buttonHide = getRidget("buttonHide"); //$NON-NLS-1$
		final IActionRidget buttonShow = getRidget("buttonShow"); //$NON-NLS-1$

		buttonAddSibling.setText("Add &Sibling"); //$NON-NLS-1$
		buttonAddSibling.addListener(new IActionListener() {
			public void callback() {
				final ITreeNode node = (ITreeNode) tree.getSingleSelectionObservable().getValue();
				final ITreeNode parent = (node != null) ? node.getParent() : null;
				if (parent != null) {
					new TreeNode(parent, "SIBLING " + nodeCount++); //$NON-NLS-1$
				}
			}
		});

		buttonAddChild.setText("Add &Child"); //$NON-NLS-1$
		buttonAddChild.addListener(new IActionListener() {
			public void callback() {
				final ITreeNode node = (ITreeNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					new TreeNode(node, "CHILD " + nodeCount++); //$NON-NLS-1$
				}
			}
		});

		buttonRename.setText("&Rename"); //$NON-NLS-1$
		buttonRename.addListener(new IActionListener() {
			public void callback() {
				final ITreeNode node = (ITreeNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					final String newValue = getNewValue(node.getValue());
					if (newValue != null) {
						node.setValue(newValue);
					}
				}
			}
		});

		buttonDelete.setText("&Delete"); //$NON-NLS-1$
		buttonDelete.addListener(new IActionListener() {
			public void callback() {
				final ITreeNode node = (ITreeNode) tree.getSingleSelectionObservable().getValue();
				final ITreeNode parent = (node != null) ? node.getParent() : null;
				if (parent != null) {
					final List<ITreeNode> children = parent.getChildren();
					children.remove(node);
					parent.setChildren(children);
				}
			}
		});

		buttonExpand.setText("E&xpand"); //$NON-NLS-1$
		buttonExpand.addListener(new IActionListener() {
			public void callback() {
				final ITreeNode node = (ITreeNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					tree.expand(node);
				}
			}
		});

		buttonCollapse.setText("&Collapse"); //$NON-NLS-1$
		buttonCollapse.addListener(new IActionListener() {
			public void callback() {
				final ITreeNode node = (ITreeNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					tree.collapse(node);
				}
			}
		});

		buttonDisable.setText("D&isable"); //$NON-NLS-1$
		buttonDisable.addListener(new IActionListener() {
			public void callback() {
				final TreeNode node = (TreeNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					setEnabledRec(node, false);
					tree.clearSelection();
				}
			}
		});

		buttonEnable.setText("&Enable"); //$NON-NLS-1$
		buttonEnable.addListener(new IActionListener() {
			public void callback() {
				final TreeNode node = (TreeNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					setEnabledRec(node, true);
				}
			}
		});

		buttonHide.setText("&Hide"); //$NON-NLS-1$
		buttonHide.addListener(new IActionListener() {
			public void callback() {
				final TreeNode node = (TreeNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					node.setVisible(false);
				}
			}
		});

		buttonShow.setText("Sho&w children"); //$NON-NLS-1$
		buttonShow.addListener(new IActionListener() {
			public void callback() {
				final TreeNode node = (TreeNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					for (final ITreeNode child : node.getChildren()) {
						((TreeNode) child).setVisible(true);
					}
				}
			}
		});

		final IObservableValue viewerSelection = tree.getSingleSelectionObservable();
		final IObservableValue hasSelection = new NotNullValue(viewerSelection);
		final IObservableValue hasNonRootSelection = new ComputedValue(Boolean.TYPE) {
			@Override
			protected Object calculate() {
				boolean result = false;
				final Object node = viewerSelection.getValue();
				if (node instanceof ITreeNode) {
					result = ((ITreeNode) node).getParent() != null;
				}
				return Boolean.valueOf(result);
			}
		};
		final DataBindingContext dbc = new DataBindingContext();
		bindEnablementToValue(dbc, buttonAddChild, hasSelection);
		bindEnablementToValue(dbc, buttonAddSibling, hasNonRootSelection);
		bindEnablementToValue(dbc, buttonDelete, hasNonRootSelection);
		bindEnablementToValue(dbc, buttonRename, hasSelection);
		bindEnablementToValue(dbc, buttonExpand, hasSelection);
		bindEnablementToValue(dbc, buttonCollapse, hasSelection);
		bindEnablementToValue(dbc, buttonEnable, hasSelection);
		bindEnablementToValue(dbc, buttonDisable, hasSelection);
		bindEnablementToValue(dbc, buttonHide, hasSelection);
		bindEnablementToValue(dbc, buttonShow, hasSelection);
	}

	// helping methods
	//////////////////

	private void bindEnablementToValue(final DataBindingContext dbc, final IRidget ridget, final IObservableValue value) {
		dbc.bindValue(BeansObservables.observeValue(ridget, IRidget.PROPERTY_ENABLED), value, null, null);
	}

	private TreeNode[] createTreeInput() {
		final TreeNode root = new TreeNode("root"); //$NON-NLS-1$

		final TreeNode groupA = new TreeNode(root, "group a"); //$NON-NLS-1$
		new TreeNode(groupA, "a_child_1"); //$NON-NLS-1$
		new TreeNode(groupA, "a_child_2"); //$NON-NLS-1$
		new TreeNode(groupA, "a_child_3"); //$NON-NLS-1$

		final TreeNode groupB = new TreeNode(root, "group b"); //$NON-NLS-1$
		new TreeNode(groupB, "b_child_1"); //$NON-NLS-1$
		new TreeNode(groupB, "b_child_2"); //$NON-NLS-1$
		new TreeNode(groupB, "b_child_3"); //$NON-NLS-1$

		final TreeNode groupC = new TreeNode(root, "group c"); //$NON-NLS-1$
		new TreeNode(groupC, "c_child_1"); //$NON-NLS-1$
		new TreeNode(groupC, "c_child_2"); //$NON-NLS-1$
		new TreeNode(groupC, "c_child_3"); //$NON-NLS-1$

		return new TreeNode[] { root };
	}

	private String getNewValue(final Object oldValue) {
		String newValue = null;
		if (oldValue != null) {
			final Shell shell = ((Button) buttonRename.getUIControl()).getShell();
			final IInputValidator validator = new IInputValidator() {
				public String isValid(final String newText) {
					final boolean isValid = newText.trim().length() > 0;
					return isValid ? null : "Name cannot be empty!"; //$NON-NLS-1$
				}
			};
			final InputDialog dialog = new InputDialog(shell, "Rename", "Enter a new name:", String.valueOf(oldValue), //$NON-NLS-1$ //$NON-NLS-2$
					validator);
			final int result = dialog.open();
			if (result == Window.OK) {
				newValue = dialog.getValue();
			}
		}
		return newValue;
	}

	private void setEnabledRec(final TreeNode node, final boolean isEnabled) {
		node.setEnabled(isEnabled);
		for (final ITreeNode child : node.getChildren()) {
			setEnabledRec((TreeNode) child, isEnabled);
		}
	}

	private static final class NotNullValue extends ComputedValue {

		private final IObservableValue value;

		private NotNullValue(final IObservableValue value) {
			super(Boolean.TYPE);
			this.value = value;
		}

		@Override
		protected Object calculate() {
			return Boolean.valueOf(value.getValue() != null);
		}
	}

}
