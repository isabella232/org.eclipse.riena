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

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Wrapper of a SWT {@link Tree}.
 */
public class TreeWrapper extends AbstractTableTreeWrapper {

	/**
	 * Creates a new wrapper for the given SWT {@link Tree}.
	 * 
	 * @param control
	 *            tree that will be wrapped
	 */
	public TreeWrapper(final Tree control) {
		super(control);
	}

	@Override
	public Tree getControl() {
		return (Tree) super.getControl();
	}

	public int getColumnCount() {
		return getControl().getColumnCount();
	}

	public TreeColumn getColumn(final int index) {
		return getControl().getColumn(index);
	}

	public void setWidth(final int columnIndex, final int width) {
		getColumn(columnIndex).setWidth(width);
	}

	public void setResizable(final int columnIndex, final boolean resizable) {
		getColumn(columnIndex).setResizable(resizable);
	}

	public int getItemCount() {
		return getControl().getItemCount();
	}

	public TreeItem getItem(final Point point) {
		return getControl().getItem(point);
	}

}
