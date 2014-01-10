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
package org.eclipse.riena.example.client.controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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

import org.eclipse.riena.beans.common.TypedComparator;
import org.eclipse.riena.beans.common.WordNode;
import org.eclipse.riena.example.client.views.TreeSubModuleView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IGroupedTreeTableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;

/**
 * Controller for the {@link TreeSubModuleView} example.
 */
public class TreeTableSubModuleController extends SubModuleController {

	private IActionRidget buttonRename;
	private IGroupedTreeTableRidget tree;

	public TreeTableSubModuleController() {
		this(null);
	}

	public TreeTableSubModuleController(final ISubModuleNode navigationNode) {
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
		final Object[] roots = createTreeInput();
		final String[] columnPropertyNames = { "word", "upperCase", "ACount" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		final String[] columnHeaders = { "Word", "Uppercase", "A Count" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		tree.bindToModel(roots, WordNode.class, "children", "parent", columnPropertyNames, columnHeaders); //$NON-NLS-1$ //$NON-NLS-2$
		tree.expand(roots[0]);
		tree.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
		tree.setComparator(0, new TypedComparator<String>());
		tree.setComparator(1, new TypedComparator<String>());
		tree.setColumnSortable(2, false);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	@Override
	public void configureRidgets() {

		tree = getRidget(IGroupedTreeTableRidget.class, "tree"); //$NON-NLS-1$
		final IToggleButtonRidget buttonEnableGrouping = getRidget(IToggleButtonRidget.class, "buttonEnableGrouping"); //$NON-NLS-1$
		final IToggleButtonRidget buttonShowRoots = getRidget(IToggleButtonRidget.class, "buttonShowRoots"); //$NON-NLS-1$
		final IToggleButtonRidget buttonEmptyInput = getRidget(IToggleButtonRidget.class, "buttonEmptyInput"); //$NON-NLS-1$
		final IActionRidget buttonAddSibling = getRidget(IActionRidget.class, "buttonAddSibling"); //$NON-NLS-1$
		final IActionRidget buttonAddChild = getRidget(IActionRidget.class, "buttonAddChild"); //$NON-NLS-1$
		buttonRename = getRidget(IActionRidget.class, "buttonRename"); //$NON-NLS-1$
		final IActionRidget buttonDelete = getRidget(IActionRidget.class, "buttonDelete"); //$NON-NLS-1$
		final IActionRidget buttonExpand = getRidget(IActionRidget.class, "buttonExpand"); //$NON-NLS-1$
		final IActionRidget buttonCollapse = getRidget(IActionRidget.class, "buttonCollapse"); //$NON-NLS-1$

		tree.addDoubleClickListener(new IActionListener() {
			public void callback() {
				final WordNode node = (WordNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					final boolean isUpperCase = !node.isUpperCase();
					node.setUpperCase(isUpperCase);
				}
			}
		});

		buttonEnableGrouping.setText("Grouping &Enabled"); //$NON-NLS-1$
		buttonEnableGrouping.setSelected(true);

		buttonShowRoots.setText("Show Roots"); //$NON-NLS-1$
		buttonShowRoots.setSelected(true);
		buttonShowRoots.addPropertyChangeListener(IToggleButtonRidget.PROPERTY_SELECTED, new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				tree.setRootsVisible((Boolean) evt.getNewValue());
				tree.updateFromModel();
			}
		});

		buttonEmptyInput.setText("Empty Input"); //$NON-NLS-1$
		buttonEmptyInput.addPropertyChangeListener(IToggleButtonRidget.PROPERTY_SELECTED, new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				final Object[] roots;
				if ((Boolean) evt.getNewValue()) {
					roots = new Object[0];
				} else {
					roots = createTreeInput();
				}
				final String[] columnPropertyNames = { "word", "upperCase", "ACount" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				final String[] columnHeaders = { "Word", "Uppercase", "A Count" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				tree.bindToModel(roots, WordNode.class, "children", "parent", columnPropertyNames, columnHeaders); //$NON-NLS-1$ //$NON-NLS-2$
				tree.expandAll();
			}
		});

		buttonAddSibling.setText("Add &Sibling"); //$NON-NLS-1$
		buttonAddSibling.addListener(new IActionListener() {
			public void callback() {
				final WordNode node = (WordNode) tree.getSingleSelectionObservable().getValue();
				final WordNode parent = (node != null) ? node.getParent() : null;
				if (parent != null) {
					new WordNode(parent, "A_NEW_SIBLING"); //$NON-NLS-1$
				}
			}
		});

		buttonAddChild.setText("Add &Child"); //$NON-NLS-1$
		buttonAddChild.addListener(new IActionListener() {
			public void callback() {
				final WordNode node = (WordNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					new WordNode(node, "ANOTHER_CHILD"); //$NON-NLS-1$
				}
			}
		});

		buttonRename.setText("&Modify"); //$NON-NLS-1$
		buttonRename.addListener(new IActionListener() {
			public void callback() {
				final WordNode node = (WordNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					final String newValue = getNewValue(node.getWordIgnoreUppercase());
					if (newValue != null) {
						node.setWord(newValue);
					}
				}
			}
		});

		buttonDelete.setText("&Delete"); //$NON-NLS-1$
		buttonDelete.addListener(new IActionListener() {
			public void callback() {
				final WordNode node = (WordNode) tree.getSingleSelectionObservable().getValue();
				final WordNode parent = (node != null) ? node.getParent() : null;
				if (parent != null) {
					final List<WordNode> children = parent.getChildren();
					children.remove(node);
					parent.setChildren(children);
				}
			}
		});

		buttonExpand.setText("E&xpand"); //$NON-NLS-1$
		buttonExpand.addListener(new IActionListener() {
			public void callback() {
				final WordNode node = (WordNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					tree.expand(node);
				}
			}
		});

		buttonCollapse.setText("&Collapse"); //$NON-NLS-1$
		buttonCollapse.addListener(new IActionListener() {
			public void callback() {
				final WordNode node = (WordNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					tree.collapse(node);
				}
			}
		});

		final IObservableValue viewerSelection = tree.getSingleSelectionObservable();
		final IObservableValue hasSelection = new ComputedValue(Boolean.TYPE) {
			@Override
			protected Object calculate() {
				return Boolean.valueOf(viewerSelection.getValue() != null);
			}
		};
		final IObservableValue hasNonRootSelection = new ComputedValue(Boolean.TYPE) {
			@Override
			protected Object calculate() {
				boolean result = false;
				final Object node = viewerSelection.getValue();
				if (node instanceof WordNode) {
					result = ((WordNode) node).getParent() != null;
				}
				return Boolean.valueOf(result);
			}
		};
		final DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(BeansObservables.observeValue(tree, IGroupedTreeTableRidget.PROPERTY_GROUPING_ENABLED),
				BeansObservables.observeValue(buttonEnableGrouping, IToggleButtonRidget.PROPERTY_SELECTED), null, null);
		bindEnablementToValue(dbc, buttonAddChild, hasSelection);
		bindEnablementToValue(dbc, buttonAddSibling, hasNonRootSelection);
		bindEnablementToValue(dbc, buttonDelete, hasNonRootSelection);
		bindEnablementToValue(dbc, buttonRename, hasSelection);
		bindEnablementToValue(dbc, buttonExpand, hasSelection);
		bindEnablementToValue(dbc, buttonCollapse, hasSelection);
	}

	private void bindEnablementToValue(final DataBindingContext dbc, final IRidget ridget, final IObservableValue value) {
		dbc.bindValue(BeansObservables.observeValue(ridget, IRidget.PROPERTY_ENABLED), value, null, null);
	}

	private String getNewValue(final Object oldValue) {
		String newValue = null;
		if (oldValue != null) {
			final Shell shell = ((Button) buttonRename.getUIControl()).getShell();
			final IInputValidator validator = new IInputValidator() {
				public String isValid(final String newText) {
					final boolean isValid = newText.trim().length() > 0;
					return isValid ? null : "Word cannot be empty!"; //$NON-NLS-1$
				}
			};
			final InputDialog dialog = new InputDialog(shell, "Modify", "Enter a new word:", String.valueOf(oldValue), //$NON-NLS-1$ //$NON-NLS-2$
					validator);
			final int result = dialog.open();
			if (result == Window.OK) {
				newValue = dialog.getValue();
			}
		}
		return newValue;
	}

	private WordNode[] createTreeInput() {
		final WordNode groupA = new WordNode("A"); //$NON-NLS-1$
		final WordNode groupB = new WordNode("B"); //$NON-NLS-1$
		final WordNode groupC = new WordNode("C"); //$NON-NLS-1$
		final WordNode groupD = new WordNode("D"); //$NON-NLS-1$
		final WordNode groupE = new WordNode("E"); //$NON-NLS-1$

		final WordNode node1 = new WordNode(groupA, "Abandonment"); //$NON-NLS-1$
		node1.setUpperCase(true);
		new WordNode(groupA, "Adventure"); //$NON-NLS-1$
		new WordNode(groupA, "Acclimatisation"); //$NON-NLS-1$
		new WordNode(groupA, "Aardwark"); //$NON-NLS-1$
		new WordNode(groupB, "Binoculars"); //$NON-NLS-1$
		new WordNode(groupB, "Beverage"); //$NON-NLS-1$
		new WordNode(groupB, "Boredom"); //$NON-NLS-1$
		new WordNode(groupB, "Ballistics"); //$NON-NLS-1$
		new WordNode(groupC, "Calculation"); //$NON-NLS-1$
		new WordNode(groupC, "Coexistence"); //$NON-NLS-1$
		new WordNode(groupC, "Cinnamon"); //$NON-NLS-1$
		new WordNode(groupC, "Celebration"); //$NON-NLS-1$
		new WordNode(groupD, "Disney"); //$NON-NLS-1$
		new WordNode(groupD, "Dictionary"); //$NON-NLS-1$
		new WordNode(groupD, "Delta"); //$NON-NLS-1$
		new WordNode(groupD, "Desperate"); //$NON-NLS-1$
		new WordNode(groupE, "Elf"); //$NON-NLS-1$
		new WordNode(groupE, "Electronics"); //$NON-NLS-1$
		new WordNode(groupE, "Elwood"); //$NON-NLS-1$
		new WordNode(groupE, "Enemy"); //$NON-NLS-1$

		return new WordNode[] { groupA, groupB, groupC, groupD, groupE };
	}
}
