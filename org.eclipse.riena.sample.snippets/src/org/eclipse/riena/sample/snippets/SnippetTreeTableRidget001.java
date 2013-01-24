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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import org.eclipse.riena.beans.common.WordNode;
import org.eclipse.riena.ui.ridgets.ITreeTableRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;

/**
 * Demonstrates binding a tree table ridget to a bean-based model.
 */
public class SnippetTreeTableRidget001 {

	public SnippetTreeTableRidget001(final Shell shell) {

		final Tree tree = new Tree(shell, SWT.FULL_SELECTION | SWT.MULTI);
		final TreeColumn tc1 = new TreeColumn(tree, SWT.DEFAULT);
		tc1.setWidth(200);
		final TreeColumn tc2 = new TreeColumn(tree, SWT.DEFAULT);
		tc2.setWidth(200);

		final ITreeTableRidget treeTableRidget = (ITreeTableRidget) SwtRidgetFactory.createRidget(tree);
		final WordNode[] roots = createTreeInput();
		final String[] columnValues = new String[] { "word", "ACount" }; //$NON-NLS-1$//$NON-NLS-2$
		treeTableRidget.bindToModel(roots, WordNode.class, "children", "parent", //$NON-NLS-1$//$NON-NLS-2$
				columnValues, null);
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			shell.setLayout(new FillLayout());
			new SnippetTreeTableRidget001(shell);
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

		final WordNode aTowns = new WordNode(root, "A"); //$NON-NLS-1$
		new WordNode(aTowns, "Ashland"); //$NON-NLS-1$
		new WordNode(aTowns, "Athena"); //$NON-NLS-1$
		new WordNode(aTowns, "Aurora"); //$NON-NLS-1$
		new WordNode(aTowns, "Alpine"); //$NON-NLS-1$
		new WordNode(aTowns, "Astoria"); //$NON-NLS-1$

		final WordNode bTowns = new WordNode(root, "B"); //$NON-NLS-1$
		new WordNode(bTowns, "Boring"); //$NON-NLS-1$
		new WordNode(bTowns, "Buchanan"); //$NON-NLS-1$
		new WordNode(bTowns, "Beaverton"); //$NON-NLS-1$
		new WordNode(bTowns, "Bend"); //$NON-NLS-1$
		new WordNode(bTowns, "Black Butte Ranch"); //$NON-NLS-1$
		new WordNode(bTowns, "Baker City"); //$NON-NLS-1$
		new WordNode(bTowns, "Bay City"); //$NON-NLS-1$
		new WordNode(bTowns, "Bridgeport"); //$NON-NLS-1$

		final WordNode cTowns = new WordNode(root, "C"); //$NON-NLS-1$
		new WordNode(cTowns, "Cedar Mill"); //$NON-NLS-1$
		new WordNode(cTowns, "Crater Lake"); //$NON-NLS-1$
		new WordNode(cTowns, "Coos Bay"); //$NON-NLS-1$
		new WordNode(cTowns, "Corvallis"); //$NON-NLS-1$
		new WordNode(cTowns, "Cannon Beach"); //$NON-NLS-1$

		final WordNode dTowns = new WordNode(root, "D"); //$NON-NLS-1$
		new WordNode(dTowns, "Dunes City"); //$NON-NLS-1$
		new WordNode(dTowns, "Damascus"); //$NON-NLS-1$
		new WordNode(dTowns, "Diamond Lake"); //$NON-NLS-1$
		new WordNode(dTowns, "Dallas"); //$NON-NLS-1$
		new WordNode(dTowns, "Depoe Bay"); //$NON-NLS-1$

		return new WordNode[] { root };
	}

}
