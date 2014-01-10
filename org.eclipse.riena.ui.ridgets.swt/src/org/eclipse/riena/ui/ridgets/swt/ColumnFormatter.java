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
package org.eclipse.riena.ui.ridgets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import org.eclipse.riena.ui.ridgets.IColumnFormatter;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITreeTableRidget;

/**
 * SWT-specific implementation of IColumnFormatter. This class provides
 * formatting options for a single column. Can be used with column-based ridgets
 * (ITableRidget, ITreeTableRidget).
 * <p>
 * The class follows the adapter pattern and provides a default implementation
 * for all methods. Implementor should only override the formatting methods that
 * apply to their use case.
 * 
 * @see ITableRidget#setColumnFormatter(int, ColumnFormatter)
 * @see ITreeTableRidget#setColumnFormatter(int, ColumnFormatter)
 */
public class ColumnFormatter implements IColumnFormatter {

	public String getText(final Object element) {
		return null;
	}

	public Object getImage(final Object element) {
		return null;
	}

	public Color getForeground(final Object element) {
		return null;
	}

	public Color getBackground(final Object element) {
		return null;
	}

	public Font getFont(final Object element) {
		return null;
	}

	public int getHorizontalAlignment(final Object element) {
		return SWT.DEFAULT;
	}

	public int getLeftIndent(final Object element) {
		return 0;
	}

	/**
	 * @since 4.0
	 */
	public String getToolTip(final Object element) {
		return null;
	}

	/**
	 * @since 4.0
	 */
	public Object getToolTipImage(final Object object) {
		return null;
	}

	/**
	 * @since 4.0
	 */
	public Object getToolTipBackgroundColor(final Object element) {
		return null;
	}

	/**
	 * @since 4.0
	 */
	public Object getToolTipForegroundColor(final Object element) {
		return null;
	}

	/**
	 * @since 4.0
	 */
	public Object getToolTipFont(final Object element) {
		return null;
	}

	/**
	 * @since 4.0
	 */
	public Object getToolTipShift(final Object element) {
		return null;
	}

	/**
	 * @since 4.0
	 */
	public int getToolTipTimeDisplayed(final Object element) {
		return 0;
	}

	/**
	 * @since 4.0
	 */
	public int getToolTipDisplayDelayTime(final Object element) {
		return 0;
	}

	/**
	 * @since 4.0
	 */
	public int getToolTipStyle(final Object element) {
		return SWT.SHADOW_NONE;
	}

}
