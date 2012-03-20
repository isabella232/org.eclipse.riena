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

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Utility class for tesing {@link Tree} widgets.
 */
public final class TreeUtils {

	private TreeUtils() {
		// prevent instantation
	}

	/**
	 * Returns the number of items in the tree starting from the root
	 * (included).
	 */
	public static int getItemCount(final Tree control) {
		int count = 0;
		for (final TreeItem root : control.getItems()) {
			count += getItemCount(root);
		}
		return count;
	}

	/**
	 * Returns the number of items in subtree starting from item (included).
	 */
	public static int getItemCount(final TreeItem item) {
		int count = 0;
		if (item != null) {
			count++;
			if (item.getExpanded()) {
				for (final TreeItem child : item.getItems()) {
					count += getItemCount(child);
				}
			}
		}
		return count;
	}

	private static int level = 0;

	/**
	 * Dump the substree to system.out starting with the given item.
	 * 
	 * @param item
	 *            a non-null TreeItem
	 */
	public synchronized static void print(final TreeItem item) {
		printSpaces(level);
		System.out.println(item);
		level++;
		try {
			for (final TreeItem child : item.getItems()) {
				print(child);
			}
		} finally {
			level--;
		}
	}

	/**
	 * Dump the tree to system.out starting with the given item.
	 * 
	 * @param tree
	 *            a non-null Tree instance
	 */
	public synchronized static void print(final Tree tree) {
		for (final TreeItem child : tree.getItems()) {
			print(child);
		}
		System.out.println("###");
	}

	// helping methods
	// ////////////////

	private static void printSpaces(final int numSpaces) {
		Assert.isLegal(numSpaces >= 0);
		for (int i = 0; i < numSpaces; i++) {
			System.out.print(" ");
		}
	}
}
