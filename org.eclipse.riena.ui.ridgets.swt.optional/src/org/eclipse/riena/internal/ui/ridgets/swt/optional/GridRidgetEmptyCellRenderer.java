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

import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.nebula.widgets.grid.internal.DefaultEmptyCellRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;

/**
 * This extended renderer hides the cell lines, if the grid is disabled.
 */
public class GridRidgetEmptyCellRenderer extends DefaultEmptyCellRenderer {

	@Override
	public void paint(final GC gc, final Object value) {

		super.paint(gc, value);

		Grid grid = null;
		if (value instanceof Grid) {
			grid = (Grid) value;
		} else if (value instanceof GridItem) {
			final GridItem item = (GridItem) value;
			grid = item.getParent();
		} else {
			return;
		}
		if (!grid.isEnabled()) {
			// the grid disabled
			// paint over the whole cell with the background color of the widget 
			// also over the lines
			gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
			gc.fillRectangle(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
		}

	}

}
