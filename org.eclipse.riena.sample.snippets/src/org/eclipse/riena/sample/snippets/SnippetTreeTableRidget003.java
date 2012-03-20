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
package org.eclipse.riena.sample.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import org.eclipse.riena.beans.common.WordNode;
import org.eclipse.riena.ui.ridgets.IGroupedTreeTableRidget;
import org.eclipse.riena.ui.ridgets.swt.ColumnFormatter;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;

/**
 * Demonstrates a tree table ridget with column formatters.
 */
public class SnippetTreeTableRidget003 {

	public SnippetTreeTableRidget003(final Shell shell) {
		final Tree tree = new Tree(shell, SWT.FULL_SELECTION | SWT.MULTI);
		for (int i = 0; i < 2; i++) {
			final TreeColumn tc = new TreeColumn(tree, SWT.DEFAULT);
			tc.setWidth(200);
		}

		final IGroupedTreeTableRidget treeTableRidget = (IGroupedTreeTableRidget) SwtRidgetFactory.createRidget(tree);
		final WordNode[] roots = createTreeInput();
		final String[] columnValues = new String[] { "word", "upperCase" }; //$NON-NLS-1$//$NON-NLS-2$
		final String[] columnHeaders = new String[] { "Word", "Uppercase" }; //$NON-NLS-1$//$NON-NLS-2$
		treeTableRidget.setGroupingEnabled(true);
		treeTableRidget.setRootsVisible(false);
		treeTableRidget.setColumnFormatter(0, new ColumnFormatter() {
			@Override
			public Color getForeground(final Object element) {
				final String word = ((WordNode) element).getWord();
				if ('B' == word.charAt(0)) {
					return shell.getDisplay().getSystemColor(SWT.COLOR_RED);
				}
				return null; // use default foreground
			}
		});
		treeTableRidget.setColumnFormatter(1, new ColumnFormatter() {
			@Override
			public Image getImage(final Object element) {
				if (((WordNode) element).isUpperCase()) {
					return shell.getDisplay().getSystemImage(SWT.ICON_WARNING);
				}
				return null; // use default image
			}
		});
		treeTableRidget.bindToModel(roots, WordNode.class, "children", "parent", //$NON-NLS-1$//$NON-NLS-2$
				columnValues, columnHeaders);
		treeTableRidget.expandAll();
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			shell.setLayout(new FillLayout());
			new SnippetTreeTableRidget003(shell);
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

	private WordNode[] createTreeInput() {
		final WordNode root = new WordNode("Words"); //$NON-NLS-1$

		final WordNode bTowns = new WordNode(root, "B"); //$NON-NLS-1$
		new WordNode(bTowns, "Boring"); //$NON-NLS-1$
		new WordNode(bTowns, "Buchanan"); //$NON-NLS-1$

		final WordNode cTowns = new WordNode(root, "C"); //$NON-NLS-1$
		new WordNode(cTowns, "Cedar Mill").setUpperCase(true); //$NON-NLS-1$
		new WordNode(cTowns, "Crater Lake"); //$NON-NLS-1$

		final WordNode dTowns = new WordNode(root, "D"); //$NON-NLS-1$
		new WordNode(dTowns, "Dunes City"); //$NON-NLS-1$
		new WordNode(dTowns, "Damascus"); //$NON-NLS-1$

		return new WordNode[] { root };
	}
}
