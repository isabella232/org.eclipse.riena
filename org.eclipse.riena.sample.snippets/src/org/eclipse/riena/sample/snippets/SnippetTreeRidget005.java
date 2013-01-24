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

import org.eclipse.riena.beans.common.WordNode;
import org.eclipse.riena.internal.ui.ridgets.swt.SharedImages;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;

/**
 * Demonstrates a tree ridget with custom images for nodes and leaves.
 */
public class SnippetTreeRidget005 {

	public SnippetTreeRidget005(final Shell shell) {
		final Tree tree = new Tree(shell, SWT.FULL_SELECTION);

		final ITreeRidget treeRidget = (ITreeRidget) SwtRidgetFactory.createRidget(tree);
		final WordNodeWithIcon[] roots = createTreeInput();
		treeRidget.bindToModel(roots, WordNodeWithIcon.class, "children", "parent", "word", null, null, "icon", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				"openIcon"); //$NON-NLS-1$
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			shell.setLayout(new FillLayout());
			shell.setText(SnippetTreeRidget005.class.getSimpleName());
			new SnippetTreeRidget005(shell);
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

	private WordNodeWithIcon[] createTreeInput() {
		final WordNodeWithIcon root = new WordNodeWithIcon("Alphabet"); //$NON-NLS-1$

		final WordNodeWithIcon node1 = new WordNodeWithIcon(root, "A-Z (English)"); //$NON-NLS-1$
		node1.setIcons(SharedImages.IMG_CHECKED, SharedImages.IMG_UNCHECKED);
		final WordNodeWithIcon nodeEye = new WordNodeWithIcon(node1, "Eye"); //$NON-NLS-1$
		nodeEye.setIcons(SharedImages.IMG_UNCHECKED, null);
		final WordNodeWithIcon nodeBe = new WordNodeWithIcon(node1, "Be"); //$NON-NLS-1$
		nodeBe.setIcons(SharedImages.IMG_CHECKED, null);
		new WordNodeWithIcon(node1, "See"); //$NON-NLS-1$
		new WordNodeWithIcon(node1, "Dii"); //$NON-NLS-1$

		final WordNodeWithIcon node2 = new WordNodeWithIcon(root, "A-Z (German)"); //$NON-NLS-1$
		node2.setIcons(null, SharedImages.IMG_UNCHECKED);
		new WordNodeWithIcon(node2, "Ah"); //$NON-NLS-1$
		new WordNodeWithIcon(node2, "Beh"); //$NON-NLS-1$
		new WordNodeWithIcon(node2, "Zeh"); //$NON-NLS-1$
		new WordNodeWithIcon(node2, "Deh"); //$NON-NLS-1$

		final WordNodeWithIcon node3 = new WordNodeWithIcon(root, "A-Z (Greek)"); //$NON-NLS-1$
		node3.setIcons(SharedImages.IMG_CHECKED, null);
		new WordNodeWithIcon(node3, "Alpha"); //$NON-NLS-1$
		new WordNodeWithIcon(node3, "Beta"); //$NON-NLS-1$
		new WordNodeWithIcon(node3, "Gamma"); //$NON-NLS-1$
		new WordNodeWithIcon(node3, "Delta"); //$NON-NLS-1$

		return new WordNodeWithIcon[] { root };
	}

	// helping classes
	//////////////////

	private class WordNodeWithIcon extends WordNode {
		private String icon;
		private String openIcon;

		public WordNodeWithIcon(final String word) {
			super(word);
		}

		public WordNodeWithIcon(final WordNodeWithIcon parent, final String word) {
			super(parent, word);
		}

		@SuppressWarnings("unused")
		public String getIcon() {
			return icon;
		}

		@SuppressWarnings("unused")
		public String getOpenIcon() {
			return openIcon;
		}

		public void setIcons(final String icon, final String openIcon) {
			this.icon = icon;
			this.openIcon = openIcon;
		}
	}

}
