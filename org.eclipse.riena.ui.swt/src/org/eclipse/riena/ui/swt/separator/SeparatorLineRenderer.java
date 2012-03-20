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
package org.eclipse.riena.ui.swt.separator;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import org.eclipse.riena.ui.swt.lnf.AbstractLnfRenderer;
import org.eclipse.riena.ui.swt.separator.Separator.ORIENTATION;

/**
 * Renderer for the {@link Separator} component.
 * 
 * @since 3.0
 */
public class SeparatorLineRenderer extends AbstractLnfRenderer {

	private SeparatorDescriptor descriptor;

	@Override
	public void paint(final GC gc, final Object value) {
		// the rendering of the separator lines is done here
		final boolean vAlign = getDescriptor().getOrientation() == ORIENTATION.VERTICAL;

		final Rectangle bounds = getBounds();
		final int width = bounds.width;
		final int height = bounds.height;

		//first line
		gc.setForeground(getDescriptor().getFirstLineColor());
		gc.drawLine(0, 0, vAlign ? 0 : width, vAlign ? height : 0);

		if (getSeparatorLines() == 1) {
			return;
		}

		//second line
		gc.setForeground(getDescriptor().getSecondLineColor());
		gc.drawLine(vAlign ? 1 : 0, vAlign ? 0 : 1, vAlign ? 1 : width, vAlign ? height : 1);
	}

	public void dispose() {

	}

	private int getSeparatorLines() {
		return getDescriptor().getLines();
	}

	public SeparatorDescriptor getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(final SeparatorDescriptor descriptor) {
		this.descriptor = descriptor;
	}

}
