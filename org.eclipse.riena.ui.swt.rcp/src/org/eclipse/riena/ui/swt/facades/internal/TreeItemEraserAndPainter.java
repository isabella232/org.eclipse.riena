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
package org.eclipse.riena.ui.swt.facades.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Erase listener to paint all cells empty when this ridget is disabled.
 * <p>
 * Implementation note: this works by registering this class an an SWT.EraseItem
 * and SWT.PaintItem listener and indicating we will be repsonsible from erasing
 * and drawing the cells content.
 * 
 * @see '<a href=
 *      "http://www.eclipse.org/articles/article.php?file=Article-CustomDrawingTableAndTreeItems/index.html"
 *      >Custom Drawing Table and Tree Items</a>'
 */
public final class TreeItemEraserAndPainter implements Listener {

	private final Rectangle bounds = new Rectangle(0, 0, 0, 0);

	/*
	 * Called EXTREMELY frequently. Must be as efficient as possible.
	 */
	public void handleEvent(final Event event) {
		if (SWT.EraseItem == event.type) {
			// indicate we are responsible for drawing the cell's content
			event.detail &= ~SWT.FOREGROUND;
		} else if (SWT.PaintItem == event.type) {
			final TreeItem item = (TreeItem) event.item;
			final Tree tree = item.getParent();
			if (!tree.isEnabled()) {
				final GC gc = event.gc;
				bounds.width = tree.getBounds().width;
				bounds.height = tree.getBounds().height;
				gc.fillRectangle(bounds);
			}
		}
	}
}