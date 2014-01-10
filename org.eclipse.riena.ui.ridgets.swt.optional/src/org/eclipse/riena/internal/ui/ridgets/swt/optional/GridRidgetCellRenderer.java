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
package org.eclipse.riena.internal.ui.ridgets.swt.optional;

import org.eclipse.core.runtime.Assert;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.nebula.widgets.grid.internal.DefaultCellRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * This extended renderer hides the cell content, if the grid is disabled, and
 * paint a border around a marked cell.
 */
public class GridRidgetCellRenderer extends DefaultCellRenderer {

	private final GridRidget ridget;
	private final static Color BORDER_COLOR = LnfManager.getLnf().getColor(LnfKeyConstants.ERROR_MARKER_BORDER_COLOR);
	private final static int BORDER_THICKNESS = LnfManager.getLnf().getIntegerSetting(
			LnfKeyConstants.ROW_ERROR_MARKER_BORDER_THICKNESS, 1);

	public GridRidgetCellRenderer(final GridRidget ridget) {
		super();
		this.ridget = ridget;
	}

	@Override
	public void paint(final GC gc, final Object value) {

		Assert.isTrue(value instanceof GridItem);

		final GridItem item = (GridItem) value;
		final Grid grid = item.getParent();

		super.paint(gc, value);

		if (!grid.isEnabled()) {
			// the grid disabled
			// paint over the whole cell with the background color of the widget 
			gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
			gc.fillRectangle(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
		} else if (isMarked(item)) {
			markRow(gc, item);
		}

	}

	private boolean isMarked(final GridItem item) {
		if (ridget == null) {
			return false;
		}
		return ridget.isErrorMarked(item);
	}

	private void markRow(final GC gc, final GridItem item) {
		final Color oldForeground = gc.getForeground();
		gc.setForeground(BORDER_COLOR);
		try {
			int x = 0, y = 0, width = 0, height = 0;
			final int colCount = ridget.getColumnCount();
			if (colCount > 0) {
				for (int i = 0; i < colCount; i++) {
					final Rectangle bounds = item.getBounds(i);
					if (i == 0) {
						x = bounds.x;
						y = bounds.y;
					}
					width += bounds.width;
					height = Math.max(height, bounds.height);
				}
				width = Math.max(0, width - 1);
				height = Math.max(0, height - 1);
			}
			if (width > 0) {
				for (int i = 0; i < BORDER_THICKNESS; i++) {
					int arc = 3;
					if (i > 0) {
						arc = 0;
					}
					gc.drawRoundRectangle(x + i, y + i, width - 2 * i, height - 2 * i, arc, arc);
				}
			}
		} finally {
			gc.setForeground(oldForeground);
		}
	}

}
