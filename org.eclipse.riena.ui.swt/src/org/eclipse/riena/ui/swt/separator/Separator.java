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
package org.eclipse.riena.ui.swt.separator;

import java.beans.Beans;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * A component separating different areas inside a ui-parent
 * 
 * @since 3.0
 */
public class Separator extends Canvas {

	public static enum ORIENTATION {
		VERTICAL, HORIZONTAL
	}

	private final SeparatorDescriptor descriptor;

	protected Separator(final Composite parent, final ORIENTATION orientation, final int style,
			final Color firstLineColor, final Color secondLineColor) {
		super(parent, style | SWT.NO_SCROLL);

		// [ev] hack to run this in SWT only mode - need to check if color is initialized by LnF...
		final Color firstLine = firstLineColor == null ? getDisplay().getSystemColor(SWT.COLOR_WHITE) : firstLineColor;

		this.descriptor = new SeparatorDescriptor(orientation, secondLineColor == null ? 1 : 2, firstLine,
				secondLineColor);
		addPaintListener(new Painter());
	}

	public Separator(final Composite parent, final int style, final Color firstLineColor, final Color secondLineColor) {
		this(parent, (style & SWT.HORIZONTAL) > 0 ? ORIENTATION.HORIZONTAL : ORIENTATION.VERTICAL,
				(style & ~(SWT.HORIZONTAL | SWT.VERTICAL)), firstLineColor, secondLineColor);
	}

	public Separator(final Composite parent, final int style, final Color lineColor) {
		this(parent, style, lineColor, null);
	}

	public Separator(final Composite parent, final int style) {
		this(parent, style, Beans.isDesignTime() ? Display.getDefault().getSystemColor(SWT.COLOR_BLACK) : LnfManager
				.getLnf().getColor(LnfKeyConstants.TITLEBAR_SEPARATOR_FIRST_LINE_FOREGROUND),
				Beans.isDesignTime() ? Display.getDefault().getSystemColor(SWT.COLOR_BLACK) : LnfManager.getLnf()
						.getColor(LnfKeyConstants.TITLEBAR_SEPARATOR_SECOND_LINE_FOREGROUND));
	}

	private class Painter implements PaintListener {

		public void paintControl(final PaintEvent e) {
			setBackground(getParent().getBackground());
			final SeparatorLineRenderer separatorLineRenderer = getSeparatorLineRenderer();
			separatorLineRenderer.setDescriptor(descriptor);
			separatorLineRenderer.setBounds(getBounds());
			separatorLineRenderer.paint(e.gc, Separator.this);
		}

		private SeparatorLineRenderer getSeparatorLineRenderer() {
			return new SeparatorLineRenderer();
		}

	}

	@Override
	public Point computeSize(final int wHint, final int hHint, final boolean changed) {
		// for fix height(HORIZONTAL) or fix width(VERTICAL)
		if (ORIENTATION.HORIZONTAL.equals(descriptor.getOrientation())) {
			return super.computeSize(wHint, descriptor.getLines(), changed);
		} else {
			return super.computeSize(descriptor.getLines(), hHint, changed);
		}
	}

}
