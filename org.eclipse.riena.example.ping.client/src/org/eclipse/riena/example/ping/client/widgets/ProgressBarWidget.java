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
package org.eclipse.riena.example.ping.client.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * Since the color used to paint the progress bar cannot be changed in the SWT
 * widget, we have to provide a simple widget for that.
 */
public class ProgressBarWidget extends Canvas {

	private static final int HEIGHT = 18;

	private int selection = 0;
	private int minimum = 0;
	private int maximum = 100;

	/**
	 * Creates a ProgressBarWidget.
	 * 
	 * @param parent
	 *            the parent widget.
	 */
	public ProgressBarWidget(final Composite parent) {
		super(parent, SWT.BORDER | SWT.SHADOW_IN);
		addPaintListener(new PaintListener() {
			public void paintControl(final PaintEvent e) {
				ProgressBarWidget.this.paintControl(e);
			}
		});
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * Paints the progress bar.
	 * 
	 * @param e
	 *            the {@link PaintEvent}.
	 */
	public void paintControl(final PaintEvent e) {
		final GC gc = e.gc;
		final Rectangle clientArea = getClientArea();
		gc.setBackground(getBackground());
		gc.fillRectangle(clientArea);
		gc.setBackground(getForeground());
		final int width = (int) (clientArea.width * getPercentage());
		gc.fillRectangle(clientArea.x, clientArea.y, width, clientArea.height);
	}

	@Override
	public Point computeSize(final int wHint, final int hHint, final boolean changed) {
		final Point size = super.computeSize(wHint, hHint, changed);
		if (size.y > HEIGHT) {
			size.y = HEIGHT;
		}
		return size;
	}

	/**
	 * Returns the progress value.
	 * 
	 * @return the progress value.
	 */
	public int getSelection() {
		return selection;
	}

	/**
	 * Sets the progress value.
	 * 
	 * @param selection
	 */
	public void setSelection(final int selection) {
		this.selection = selection;
		redraw();
	}

	/**
	 * Returns the maximum value.
	 * 
	 * @return the maximum value.
	 */
	public int getMaximum() {
		return maximum;
	}

	/**
	 * Sets the maximum value.
	 * 
	 * @param maximum
	 */
	public void setMaximum(final int maximum) {
		this.maximum = maximum;
		redraw();
	}

	/**
	 * Returns the minimum value.
	 * 
	 * @return the minimum value.
	 */
	public int getMinimum() {
		return minimum;
	}

	/**
	 * Sets the minimum value.
	 * 
	 * @param minimum
	 */
	public void setMinimum(final int minimum) {
		this.minimum = minimum;
		redraw();
	}

	/**
	 * Returns the percentage of the progress. This is the value that actually
	 * gets painted.
	 * 
	 * @return the percentage of the progress.
	 */
	protected double getPercentage() {
		return getSelection() / (double) (getMaximum() - getMinimum());
	}
}
