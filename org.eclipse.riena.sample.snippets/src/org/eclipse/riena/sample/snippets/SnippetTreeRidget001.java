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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.ridgets.tree2.ITreeNode;
import org.eclipse.riena.ui.ridgets.tree2.TreeNode;

/**
 * Demonstrates binding a tree ridget to a model.
 */
public class SnippetTreeRidget001 {

	public SnippetTreeRidget001(final Shell shell) {

		final Tree tree = new Tree(shell, SWT.FULL_SELECTION | SWT.MULTI);

		final ITreeRidget treeRidget = (ITreeRidget) SwtRidgetFactory.createRidget(tree);
		final ITreeNode[] roots = createTreeInput();
		treeRidget.bindToModel(roots, ITreeNode.class, ITreeNode.PROPERTY_CHILDREN, ITreeNode.PROPERTY_PARENT,
				ITreeNode.PROPERTY_VALUE);

	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			shell.setLayout(new FillLayout());
			new SnippetTreeRidget001(shell);
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

	private ITreeNode[] createTreeInput() {

		final ITreeNode root = new TreeNode("Gods"); //$NON-NLS-1$

		final ITreeNode greek = new TreeNode(root, "Greek Gods"); //$NON-NLS-1$
		new TreeNode(greek, "Aphrodite"); //$NON-NLS-1$
		new TreeNode(greek, "Apollo"); //$NON-NLS-1$
		new TreeNode(greek, "Ares"); //$NON-NLS-1$
		new TreeNode(greek, "Artemis"); //$NON-NLS-1$
		new TreeNode(greek, "Athena"); //$NON-NLS-1$
		new TreeNode(greek, "Demeter"); //$NON-NLS-1$
		new TreeNode(greek, "Dionysus"); //$NON-NLS-1$
		new TreeNode(greek, "Hephaestus"); //$NON-NLS-1$
		new TreeNode(greek, "Hera"); //$NON-NLS-1$
		new TreeNode(greek, "Hermes"); //$NON-NLS-1$
		new TreeNode(greek, "Hestia"); //$NON-NLS-1$
		new TreeNode(greek, "Zeus"); //$NON-NLS-1$
		final ITreeNode greekDemigods = new TreeNode(greek, "Demigods"); //$NON-NLS-1$
		new TreeNode(greekDemigods, "Achilles"); //$NON-NLS-1$
		new TreeNode(greekDemigods, "Hercules"); //$NON-NLS-1$
		new TreeNode(greekDemigods, "Perseus "); //$NON-NLS-1$

		final ITreeNode roman = new TreeNode(root, "Roman Gods"); //$NON-NLS-1$
		new TreeNode(roman, "Diana"); //$NON-NLS-1$
		new TreeNode(roman, "Janus"); //$NON-NLS-1$
		new TreeNode(roman, "Juno"); //$NON-NLS-1$
		new TreeNode(roman, "Jupiter"); //$NON-NLS-1$
		new TreeNode(roman, "Mars"); //$NON-NLS-1$
		new TreeNode(roman, "Saturn"); //$NON-NLS-1$
		new TreeNode(roman, "Vesta"); //$NON-NLS-1$

		final ITreeNode germanic = new TreeNode(root, "Germanic Gods"); //$NON-NLS-1$
		new TreeNode(germanic, "Thor"); //$NON-NLS-1$
		new TreeNode(germanic, "Odin"); //$NON-NLS-1$
		new TreeNode(germanic, "Tyr"); //$NON-NLS-1$
		new TreeNode(germanic, "Frigg"); //$NON-NLS-1$

		return new ITreeNode[] { root };
	}

}
