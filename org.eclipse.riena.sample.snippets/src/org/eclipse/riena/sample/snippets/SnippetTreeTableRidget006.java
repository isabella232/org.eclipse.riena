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
package org.eclipse.riena.sample.snippets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITreeTableRidget;
import org.eclipse.riena.ui.ridgets.swt.ColumnFormatter;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;

/**
 * Demonstrates a tree table ridget with column formatters
 * (https://bugs.eclipse.org/bugs/show_bug.cgi?id=366964)
 */
public class SnippetTreeTableRidget006 {

	public SnippetTreeTableRidget006(final Shell shell) {
		final Composite tableComposite = new Composite(shell, SWT.NONE);
		tableComposite.setLayout(new GridLayout());
		final GridData layoutData2 = new GridData(SWT.FILL, SWT.FILL, true, true);
		tableComposite.setLayoutData(layoutData2);

		final Tree tree = new Tree(tableComposite, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		final GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		layoutData.heightHint = 100;
		tree.setLayoutData(layoutData);
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);

		final TreeColumnLayout columnLayout = new TreeColumnLayout();
		final TreeColumn tableColumn = new TreeColumn(tree, SWT.FILL);
		tableColumn.setText("Test");
		columnLayout.setColumnData(tableColumn, new ColumnWeightData(10, 100));
		tableComposite.setLayout(columnLayout);

		final ITreeTableRidget treeRiget = (ITreeTableRidget) SwtRidgetFactory.createRidget(tree);
		treeRiget.setSelectionType(ISelectableRidget.SelectionType.MULTI);

		treeRiget.setColumnFormatter(0, new ColumnFormatter() {
			@Override
			public String getText(final Object element) {
				final TestTreeNode node = (TestTreeNode) element;
				if (node.getName() == null) {
					return "";
				}
				return node.getName();
			}
		});

		final Object[] roots = new Object[] { new TestTreeNode(null), new TestTreeNode("name") };
		treeRiget.bindToModel(roots, TestTreeNode.class, "children", "parent", new String[] { "name" },
				new String[] { "Name" });
	}

	private static class TestTreeNode {

		private final String name;

		public TestTreeNode(final String name) {
			this.name = name;
		}

		public TestTreeNode getParent() {
			return null;
		}

		public List<TestTreeNode> getChildren() {
			return new ArrayList<TestTreeNode>();
		}

		public String getName() {
			return name;
		}
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			shell.setLayout(new GridLayout());
			new SnippetTreeTableRidget006(shell);
			shell.pack();
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} finally {
			display.dispose();
		}
	}

}
