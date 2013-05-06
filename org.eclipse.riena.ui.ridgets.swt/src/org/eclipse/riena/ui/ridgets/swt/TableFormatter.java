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

	public String getText(final Object rowElement, final Object cellElement, final int columnIndex) {
		return DEFAULT_FOMATTER.getText(rowElement);
	}

	public Object getImage(final Object rowElement, final Object cellElement, final int columnIndex) {
		return DEFAULT_FOMATTER.getImage(rowElement);
	}

	public Object getForeground(final Object rowElement, final Object cellElement, final int columnIndex) {
		return DEFAULT_FOMATTER.getForeground(rowElement);
	}

	public Object getBackground(final Object rowElement, final Object cellElement, final int columnIndex) {
		return DEFAULT_FOMATTER.getBackground(rowElement);
	}

	public Object getFont(final Object rowElement, final Object cellElement, final int columnIndex) {
		return DEFAULT_FOMATTER.getFont(rowElement);
	}

	public int getHorizontalAlignment(final Object rowElement, final Object cellElement, final int columnIndex) {
		return DEFAULT_FOMATTER.getHorizontalAlignment(rowElement);
	}

	public int getLeftIndent(final Object rowElement, final Object cellElement, final int columnIndex) {
		return DEFAULT_FOMATTER.getLeftIndent(rowElement);
	}

	public String getToolTip(final Object rowElement, final Object cellElement, final int columnIndex) {
		return DEFAULT_FOMATTER.getToolTip(rowElement);
	}

	public Object getToolTipImage(final Object rowElement, final Object cellElement, final int columnIndex) {
		return DEFAULT_FOMATTER.getToolTipImage(rowElement);
	}

	public Object getToolTipBackgroundColor(final Object rowElement, final Object cellElement, final int columnIndex) {
		return DEFAULT_FOMATTER.getToolTipBackgroundColor(rowElement);
	}

	public Object getToolTipForegroundColor(final Object rowElement, final Object cellElement, final int columnIndex) {
		return DEFAULT_FOMATTER.getToolTipForegroundColor(rowElement);
	}

	public Object getToolTipFont(final Object rowElement, final Object cellElement, final int columnIndex) {
		return DEFAULT_FOMATTER.getToolTipFont(rowElement);
	}

	public Object getToolTipShift(final Object rowElement, final Object cellElement, final int columnIndex) {
		return DEFAULT_FOMATTER.getToolTipTimeDisplayed(rowElement);
	}

	public int getToolTipTimeDisplayed(final Object rowElement, final Object cellElement, final int columnIndex) {
		return DEFAULT_FOMATTER.getToolTipTimeDisplayed(rowElement);
	}

	public int getToolTipDisplayDelayTime(final Object rowElement, final Object cellElement, final int columnIndex) {
		return DEFAULT_FOMATTER.getToolTipDisplayDelayTime(rowElement);
	}

	public int getToolTipStyle(final Object rowElement, final Object cellElement, final int columnIndex) {
		return DEFAULT_FOMATTER.getToolTipStyle(rowElement);
	}

}
