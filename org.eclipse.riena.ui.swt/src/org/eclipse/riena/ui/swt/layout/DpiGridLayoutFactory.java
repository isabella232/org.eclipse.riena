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
package org.eclipse.riena.ui.swt.layout;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * DpiGridLayoutFactory is a copy of the final class {@linkplain GridLayoutFactory}.<br>
 * DpiGridLayout supports DPI dependent layout {@linkplain DpiGridLayout}.
 * 
 * @since 6.0
 */
public class DpiGridLayoutFactory {

	/**
	 * Template layout. The factory will create copies of this layout.
	 */
	private final DpiGridLayout l;
	private final static int HORIZONTAL_DIALOG_UNIT_PER_CHAR = 4;

	private final static int VERTICAL_DIALOG_UNITS_PER_CHAR = 8;

	/**
	 * Creates a new DpiGridLayoutFactory that will create copies of the given layout.
	 * 
	 * @param l
	 *            layout to copy
	 */
	private DpiGridLayoutFactory(final DpiGridLayout l) {
		this.l = l;
	}

	/**
	 * Creates a factory that creates copies of the given layout.
	 * 
	 * @param l
	 *            layout to copy
	 * @return a new DpiGridLayoutFactory instance that creates copies of the given layout
	 */
	public static DpiGridLayoutFactory createFrom(final DpiGridLayout l) {
		return new DpiGridLayoutFactory(copyLayout(l));
	}

	/**
	 * Creates a factory that creates copies of the given layout.
	 * 
	 * @param l
	 *            layout to copy
	 * @return a new DpiGridLayoutFactory instance that creates copies of the given layout
	 */
	public static DpiGridLayoutFactory createFrom(final GridLayout l) {
		return new DpiGridLayoutFactory(copyLayout(l));
	}

	/**
	 * Creates a copy of the reciever.
	 * 
	 * @return a copy of the reciever
	 */
	public DpiGridLayoutFactory copy() {
		return new DpiGridLayoutFactory(create());
	}

	/**
	 * Creates a DpiGridLayoutFactory that creates DpiGridLayouts with the default SWT values.
	 * 
	 * <p>
	 * Initial values are:
	 * </p>
	 * 
	 * <ul>
	 * <li>numColumns(1)</li>
	 * <li>margins(5,5)</li>
	 * <li>extendedMargins(0,0,0,0)</li>
	 * <li>spacing(5,5)</li>
	 * <li>equalWidth(false)</li>
	 * </ul>
	 * 
	 * @return a DpiGridLayoutFactory that creates DpiGridLayouts as though created with their default constructor
	 * @see #fillDefaults
	 */
	public static DpiGridLayoutFactory swtDefaults() {
		return new DpiGridLayoutFactory(new DpiGridLayout());
	}

	/**
	 * Creates a DpiGridLayoutFactory that creates DpiGridLayouts with no margins and default dialog spacing.
	 * 
	 * <p>
	 * Initial values are:
	 * </p>
	 * 
	 * <ul>
	 * <li>numColumns(1)</li>
	 * <li>margins(0,0)</li>
	 * <li>extendedMargins(0,0,0,0)</li>
	 * <li>spacing(LayoutConstants.getSpacing())</li>
	 * <li>equalWidth(false)</li>
	 * </ul>
	 * 
	 * @return a DpiGridLayoutFactory that creates DpiGridLayouts as though created with their default constructor
	 * @see #swtDefaults
	 */
	public static DpiGridLayoutFactory fillDefaults() {
		final DpiGridLayout layout = new DpiGridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		final Point defaultSpacing = computeSpacingWithoutScaling();
		layout.horizontalSpacing = defaultSpacing.x;
		layout.verticalSpacing = defaultSpacing.y;
		return new DpiGridLayoutFactory(layout);
	}

	private static Point computeSpacingWithoutScaling() {
		int horizontal = 0;
		int vertical = 0;

		horizontal = (6 * IDialogConstants.HORIZONTAL_SPACING + HORIZONTAL_DIALOG_UNIT_PER_CHAR / 2) / HORIZONTAL_DIALOG_UNIT_PER_CHAR;
		vertical = (15 * IDialogConstants.VERTICAL_SPACING + VERTICAL_DIALOG_UNITS_PER_CHAR / 2) / VERTICAL_DIALOG_UNITS_PER_CHAR;

		return new Point(horizontal, vertical);
	}

	/**
	 * Sets whether the columns should be forced to be equal width
	 * 
	 * @param equal
	 *            true if the columns should be forced to be equal width
	 * @return this
	 */
	public DpiGridLayoutFactory equalWidth(final boolean equal) {
		l.makeColumnsEqualWidth = equal;
		return this;
	}

	/**
	 * Sets the spacing for layouts created with this factory. The spacing is the distance between cells within the layout.
	 * 
	 * @param hSpacing
	 *            horizontal spacing (pixels)
	 * @param vSpacing
	 *            vertical spacing (pixels)
	 * @return this
	 * @see #margins(Point)
	 * @see #margins(int, int)
	 */
	public DpiGridLayoutFactory spacing(final int hSpacing, final int vSpacing) {
		l.horizontalSpacing = hSpacing;
		l.verticalSpacing = vSpacing;
		return this;
	}

	/**
	 * Sets the spacing for layouts created with this factory. The spacing is the distance between cells within the layout.
	 * 
	 * @param spacing
	 *            space between controls in the layout (pixels)
	 * @return this
	 * @see #margins(Point)
	 * @see #margins(int, int)
	 */
	public DpiGridLayoutFactory spacing(final Point spacing) {
		l.horizontalSpacing = spacing.x;
		l.verticalSpacing = spacing.y;
		return this;
	}

	/**
	 * Sets the margins for layouts created with this factory. The margins are the distance between the outer cells and the edge of the layout.
	 * 
	 * @param margins
	 *            margin size (pixels)
	 * @return this
	 * @see #spacing(Point)
	 * @see #spacing(int, int)
	 */
	public DpiGridLayoutFactory margins(final Point margins) {
		l.marginWidth = margins.x;
		l.marginHeight = margins.y;
		return this;
	}

	/**
	 * Sets the margins for layouts created with this factory. The margins specify the number of pixels of horizontal and vertical margin that will be placed
	 * along the left/right and top/bottom edges of the layout. Note that thes margins will be added to the ones specified by
	 * {@link #extendedMargins(int, int, int, int)}.
	 * 
	 * @param width
	 *            margin width (pixels)
	 * @param height
	 *            margin height (pixels)
	 * @return this
	 * @see #spacing(Point) * @see #spacing(int, int)
	 */
	public DpiGridLayoutFactory margins(final int width, final int height) {
		l.marginWidth = width;
		l.marginHeight = height;
		return this;
	}

	/**
	 * Sets the margins for layouts created with this factory. The margins specify the number of pixels of horizontal and vertical margin that will be placed
	 * along the left, right, top, and bottom edges of the layout. Note that these margins will be added to the ones specified by {@link #margins(int, int)}.
	 * 
	 * @param left
	 *            left margin size (pixels)
	 * @param right
	 *            right margin size (pixels)
	 * @param top
	 *            top margin size (pixels)
	 * @param bottom
	 *            bottom margin size (pixels)
	 * @return this
	 * @see #spacing(Point)
	 * @see #spacing(int, int)
	 * 
	 * @since 3.3
	 */
	public DpiGridLayoutFactory extendedMargins(final int left, final int right, final int top, final int bottom) {
		l.marginLeft = left;
		l.marginRight = right;
		l.marginTop = top;
		l.marginBottom = bottom;
		return this;
	}

	/**
	 * Sets the margins for layouts created with this factory. The margins specify the number of pixels of horizontal and vertical margin that will be placed
	 * along the left, right, top, and bottom edges of the layout. Note that thes margins will be added to the ones specified by {@link #margins(int, int)}.
	 * 
	 * <code><pre>
	 *     // Construct a DpiGridLayout whose left, right, top, and bottom 
	 *     // margin sizes are 10, 5, 0, and 15 respectively
	 *      
	 *     Rectangle margins = Geometry.createDiffRectangle(10,5,0,15);
	 *     DpiGridLayoutFactory.fillDefaults().extendedMargins(margins).applyTo(composite1);
	 * </pre></code>
	 * 
	 * @param differenceRect
	 *            rectangle which, when added to the client area of the layout, returns the outer area of the layout. The x and y values of the rectangle
	 *            correspond to the position of the bounds of the layout with respect to the client area. They should be negative. The width and height
	 *            correspond to the relative size of the bounds of the layout with respect to the client area, and should be positive.
	 * @return this
	 * @see #spacing(Point)
	 * @see #spacing(int, int)
	 * 
	 * @since 3.3
	 */
	public DpiGridLayoutFactory extendedMargins(final Rectangle differenceRect) {
		l.marginLeft = -differenceRect.x;
		l.marginTop = -differenceRect.y;
		l.marginBottom = differenceRect.y + differenceRect.height;
		l.marginRight = differenceRect.x + differenceRect.width;
		return this;
	}

	/**
	 * Sets the number of columns in the layout
	 * 
	 * @param numColumns
	 *            number of columns in the layout
	 * @return this
	 */
	public DpiGridLayoutFactory numColumns(final int numColumns) {
		l.numColumns = numColumns;
		return this;
	}

	/**
	 * Creates a new DpiGridLayout, and initializes it with values from the factory.
	 * 
	 * @return a new initialized DpiGridLayout.
	 * @see #applyTo
	 */
	public DpiGridLayout create() {
		return copyLayout(l);
	}

	/**
	 * Creates a new DpiGridLayout and attaches it to the given composite. Does not create the GridData of any of the controls in the composite.
	 * 
	 * @param c
	 *            composite whose layout will be set
	 * @see #generateLayout
	 * @see #create
	 * @see DpiGridLayoutFactory
	 */
	public void applyTo(final Composite c) {
		c.setLayout(copyLayout(l));
	}

	/**
	 * Copies the given DpiGridLayout instance
	 * 
	 * @param l
	 *            layout to copy
	 * @return a new DpiGridLayout
	 */
	public static DpiGridLayout copyLayout(final DpiGridLayout l) {
		final DpiGridLayout result = new DpiGridLayout(l.numColumns, l.makeColumnsEqualWidth);
		result.horizontalSpacing = l.horizontalSpacing;
		result.marginBottom = l.marginBottom;
		result.marginHeight = l.marginHeight;
		result.marginLeft = l.marginLeft;
		result.marginRight = l.marginRight;
		result.marginTop = l.marginTop;
		result.marginWidth = l.marginWidth;
		result.verticalSpacing = l.verticalSpacing;

		return result;
	}

	/**
	 * Copies the given GridLayout instance and returns DpiGridLayout.
	 * 
	 * @param l
	 *            layout to copy
	 * @return a new DpiGridLayout
	 */
	public static DpiGridLayout copyLayout(final GridLayout l) {
		final DpiGridLayout result = new DpiGridLayout(l.numColumns, l.makeColumnsEqualWidth);
		result.horizontalSpacing = l.horizontalSpacing;
		result.marginBottom = l.marginBottom;
		result.marginHeight = l.marginHeight;
		result.marginLeft = l.marginLeft;
		result.marginRight = l.marginRight;
		result.marginTop = l.marginTop;
		result.marginWidth = l.marginWidth;
		result.verticalSpacing = l.verticalSpacing;

		return result;
	}

}
