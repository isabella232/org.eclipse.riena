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
package org.eclipse.riena.ui.ridgets;

/**
 * Provides formatting options for a single column. Can be used with
 * column-based ridgets (ITableRidget, ITreeTableRidget).
 * <p>
 * Toolkits may provide (and ridgets may require) a specific implementation of
 * this interface. See ColumnFormatter for an SWT-specific implementation.
 * <p>
 * <i> Note: not every property will be used for every kind of table Ridget!
 * </i>
 * <p>
 * <i> Note: the tool tip properties (except toolTip) are only used if the table
 * Ridget does not use native tool tips. </i>
 * 
 * @see ITableRidget#setColumnFormatter(int, ColumnFormatter)
 * @see ITreeTableRidget#setColumnFormatter(int, ColumnFormatter)
 */
public interface IColumnFormatter {

	/**
	 * Returns the text for a column's row.
	 * 
	 * @param element
	 *            the row element
	 * @return a text (String) for this element, or null, if no text should be
	 *         shown
	 */
	String getText(Object element);

	/**
	 * Returns the image or image data for a column's row.
	 * 
	 * @param element
	 *            the row element
	 * @return an image for this element, or null, if no image should be shown.
	 *         Implementors may return a type specific to their ui-toolkit.
	 */
	Object getImage(Object element);

	/**
	 * Returns the foreground color for a column's row.
	 * 
	 * @param element
	 *            the row element
	 * @return the foreground color for this element or null to use the default
	 *         foreground color. Implementors may return a type specific to
	 *         their ui-toolkit.
	 */
	Object getForeground(Object element);

	/**
	 * Returns the background color for a column's row.
	 * 
	 * @param element
	 *            the row element
	 * @return the background color for this element or null to use the default
	 *         background color. Implementors may return a type specific to
	 *         their ui-toolkit.
	 */
	Object getBackground(Object element);

	/**
	 * Returns the font for a column's row.
	 * 
	 * @param element
	 *            the row element
	 * @return the font color for this element or null to use the default font.
	 *         Implementors may return a type specific to their ui-toolkit.
	 */
	Object getFont(Object element);

	/**
	 * Returns the horizontal alignment for a column's row.
	 * <p>
	 * <i>Currently not used for {@link ITableRidget} and
	 * {@link ITreeTableRidget}!</i>
	 * 
	 * @param element
	 *            the row element
	 * @return horizontal alignment ({@code SWT.LEFT}, {@code SWT.CENTER},
	 *         {@code SWT.RIGHT} or {@code SWT.DEFAULT})
	 * @since 1.2
	 */
	int getHorizontalAlignment(Object element);

	/**
	 * Returns the number of indents that should be added before the content of
	 * a cell.
	 * <p>
	 * The number of indents is multiplied with a setting of the Riena (SCP)
	 * Look&Feel ({@code SCPLnfKeyConstants.MATRIX_ADDTIONAL_PADDING_LEFT}). So
	 * the pixels for left padding is computed.
	 * <p>
	 * <i>Currently not used for {@link ITableRidget} and
	 * {@link ITreeTableRidget}!</i>
	 * 
	 * @param element
	 *            the row element
	 * @return number of indents
	 * @since 2.0
	 */
	int getLeftIndent(Object element);

	/**
	 * Get the text displayed in the tool tip for object.
	 * 
	 * @param element
	 *            the element for which the tool tip is shown
	 * @return the {@link String} or <code>null</code> if there is not text to
	 *         display
	 * @since 4.0
	 */
	String getToolTip(Object element);

	/**
	 * Get the image displayed in the tool tip for object.
	 * 
	 * @param element
	 *            the element for which the tool tip is shown
	 * @return image or <code>null</code> if there is not image.
	 * @since 4.0
	 */
	Object getToolTipImage(Object element);

	/**
	 * Return the background color used for the tool tip
	 * 
	 * @param element
	 *            the element for which the tool tip is shown
	 * 
	 * @return the color used or <code>null</code> if you want to use the
	 *         default color
	 * @since 4.0
	 */
	Object getToolTipBackgroundColor(Object element);

	/**
	 * The foreground color used to display the the text in the tool tip
	 * 
	 * @param element
	 *            the element for which the tool tip is shown
	 * @return the color used or <code>null</code> if you want to use the
	 *         default color
	 * @since 4.0
	 */
	Object getToolTipForegroundColor(Object element);

	/**
	 * Get the font used to display the tool tip
	 * 
	 * @param element
	 *            the element for which the tool tip is shown
	 * @return font or <code>null</code> if the default font is to be used.
	 * @since 4.0
	 */
	Object getToolTipFont(Object element);

	/**
	 * Return the amount of pixels in x and y direction you want the tool tip to
	 * pop up from the mouse pointer. The default shift is 10px right and 0px
	 * below your mouse cursor. Be aware of the fact that you should at least
	 * position the tool tip 1px right to your mouse cursor else click events
	 * may not get propagated properly.
	 * 
	 * @param element
	 *            the element for which the tool tip is shown
	 * 
	 * @return x- and y-pixels (point) to shift of the tool tip or
	 *         <code>null</code> if the default shift should be used.
	 * @since 4.0
	 */
	Object getToolTipShift(Object element);

	/**
	 * The time in milliseconds the tool tip is shown for.
	 * 
	 * @param element
	 *            the element for which the tool tip is shown
	 * 
	 * @return time in milliseconds the tool tip is shown for
	 * @since 4.0
	 */
	int getToolTipTimeDisplayed(Object element);

	/**
	 * The time in milliseconds until the tool tip is displayed.
	 * 
	 * @param element
	 *            the element for which the tool tip is shown
	 * @return time in milliseconds until the tool tip is displayed
	 * @since 4.0
	 */
	int getToolTipDisplayDelayTime(Object element);

	/**
	 * The {@link SWT} style used to create the {@link CLabel} (see there for
	 * supported styles). By default {@link SWT#SHADOW_NONE} is used.
	 * 
	 * @param element
	 *            the element for which the tool tip is shown
	 * @return the style used to create the label
	 * @since 4.0
	 */
	int getToolTipStyle(Object element);

}
