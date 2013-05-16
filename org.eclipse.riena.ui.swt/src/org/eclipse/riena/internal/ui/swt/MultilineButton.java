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
package org.eclipse.riena.internal.ui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.ui.swt.facades.SWTFacade;

/**
 * This class is not API. It is a subclass of {@link Button} which works around Bug 400248.
 * <p>
 * This class can be removed after the SWT bug is fixed.
 * 
 * @since 5.0
 * 
 */
public class MultilineButton extends Button {

	private final GC gc;

	/**
	 * This class is not API. It is a subclass of {@link Button} which works around Bug 400248.
	 * <p>
	 * This class can be removed after the SWT bug is fixed.
	 */
	public MultilineButton(final Composite parent, final int style) {
		super(parent, style);
		gc = new GC(this);
		gc.setFont(getFont());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Control#setFont(org.eclipse.swt.graphics.Font)
	 */
	@Override
	public void setFont(final Font font) {
		super.setFont(font);
		gc.setFont(font);
	}

	@Override
	public void dispose() {
		gc.dispose();
		super.dispose();
	}

	@Override
	protected void checkSubclass() {
		// This subclass is needed until Bug 400248 is fixed
	}

	@Override
	public Point computeSize(final int wHint, final int hHint, final boolean changed) {
		final Point fromSuper = super.computeSize(wHint, hHint, changed);
		if (getText() == null || (getStyle() & SWT.WRAP) == 0) {
			return fromSuper;
		}

		final Point textSizeOneLine = gc.stringExtent(getText().replaceAll("\\r|\\n", "")); //$NON-NLS-1$ //$NON-NLS-2$
		final Point textSizeWithLineBreaks = SWTFacade.getDefault().textExtent(gc, getText(), SWT.DRAW_DELIMITER);

		return new Point(fromSuper.x - textSizeOneLine.x + textSizeWithLineBreaks.x, Math.max(fromSuper.y, textSizeWithLineBreaks.y));
	}
}
