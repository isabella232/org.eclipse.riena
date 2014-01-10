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
package org.eclipse.riena.ui.swt.separator;

import org.eclipse.swt.graphics.Color;

import org.eclipse.riena.ui.swt.separator.Separator.ORIENTATION;

/**
 * Bean class holding information about a {@link Separator}.
 * 
 * @since 3.0
 */
public class SeparatorDescriptor {

	private final ORIENTATION orientation;

	private final int lines;

	private final Color firstLineColor;
	private final Color secondLineColor;

	/**
	 * @param orientation
	 * @param lines
	 * @param firstLineColor
	 * @param secondLineColor
	 */
	public SeparatorDescriptor(final ORIENTATION orientation, final int lines, final Color firstLineColor,
			final Color secondLineColor) {
		super();
		this.orientation = orientation;
		this.lines = lines;
		this.firstLineColor = firstLineColor;
		this.secondLineColor = secondLineColor;
	}

	/**
	 * @return the orientation
	 */
	public ORIENTATION getOrientation() {
		return orientation;
	}

	/**
	 * @return the number of lines
	 */
	public int getLines() {
		return lines;
	}

	/**
	 * @return the firstLineColor
	 */
	public Color getFirstLineColor() {
		return firstLineColor;
	}

	/**
	 * @return the secondLineColor
	 */
	public Color getSecondLineColor() {
		return secondLineColor;
	}

}