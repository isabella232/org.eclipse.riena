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
package org.eclipse.riena.ui.ridgets.swt;

import org.eclipse.riena.ui.ridgets.IColumnFormatter;
import org.eclipse.riena.ui.ridgets.ITableFormatter;

/**
 * SWT-specific default implementation of {@linkplain ITableFormatter}.
 * 
 * @since 5.0
 */
public class TableFormatter implements ITableFormatter {

	private static final IColumnFormatter DEFAULT_FOMATTER = new ColumnFormatter();

	public String getText(final Object element, final int columnIndex) {
		return DEFAULT_FOMATTER.getText(element);
	}

	public Object getImage(final Object element, final int columnIndex) {
		return DEFAULT_FOMATTER.getImage(element);
	}

	public Object getForeground(final Object element, final int columnIndex) {
		return DEFAULT_FOMATTER.getForeground(element);
	}

	public Object getBackground(final Object element, final int columnIndex) {
		return DEFAULT_FOMATTER.getBackground(element);
	}

	public Object getFont(final Object element, final int columnIndex) {
		return DEFAULT_FOMATTER.getFont(element);
	}

	public int getHorizontalAlignment(final Object element, final int columnIndex) {
		return DEFAULT_FOMATTER.getHorizontalAlignment(element);
	}

	public int getLeftIndent(final Object element, final int columnIndex) {
		return DEFAULT_FOMATTER.getLeftIndent(element);
	}

	public String getToolTip(final Object element, final int columnIndex) {
		return DEFAULT_FOMATTER.getToolTip(element);
	}

	public Object getToolTipImage(final Object element, final int columnIndex) {
		return DEFAULT_FOMATTER.getToolTipImage(element);
	}

	public Object getToolTipBackgroundColor(final Object element, final int columnIndex) {
		return DEFAULT_FOMATTER.getToolTipBackgroundColor(element);
	}

	public Object getToolTipForegroundColor(final Object element, final int columnIndex) {
		return DEFAULT_FOMATTER.getToolTipForegroundColor(element);
	}

	public Object getToolTipFont(final Object element, final int columnIndex) {
		return DEFAULT_FOMATTER.getToolTipFont(element);
	}

	public Object getToolTipShift(final Object element, final int columnIndex) {
		return DEFAULT_FOMATTER.getToolTipTimeDisplayed(element);
	}

	public int getToolTipTimeDisplayed(final Object element, final int columnIndex) {
		return DEFAULT_FOMATTER.getToolTipTimeDisplayed(element);
	}

	public int getToolTipDisplayDelayTime(final Object element, final int columnIndex) {
		return DEFAULT_FOMATTER.getToolTipDisplayDelayTime(element);
	}

	public int getToolTipStyle(final Object element, final int columnIndex) {
		return DEFAULT_FOMATTER.getToolTipStyle(element);
	}

}
