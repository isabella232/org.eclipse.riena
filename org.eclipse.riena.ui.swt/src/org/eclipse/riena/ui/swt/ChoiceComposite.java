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
package org.eclipse.riena.ui.swt;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * This composite presents a list of single or multiple choices. It is mapped to
 * a {@link org.eclipse.riena.ui.ridgets.ISingleChoiceRidget} or
 * {@link org.eclipse.riena.ui.ridgets.IMultipleChoiceRidget}.
 */
public class ChoiceComposite extends Composite implements SelectionListener {

	private final boolean isMulti;

	private int marginHeight;
	private int marginWidth;
	private int vSpacing;
	private int hSpacing = 3;
	private int orientation;
	private boolean isEditable;

	private Color bgColor;

	/**
	 * Create a new ChoiceComposite instance given its parent and style value.
	 * The default orientation is <tt>SWT.VERTICAL</tt> (see also
	 * {@link #setOrientation(int)}). Multiple selection is allowed (=check
	 * boxes).
	 * 
	 * @param parent
	 *            the parent Composite (non-null)
	 * @param style
	 *            the SWT style of the Composite
	 */
	public ChoiceComposite(final Composite parent, final int style) {
		this(parent, style, true);
	}

	/**
	 * Create a new ChoiceComposite instance given its parent and style value.
	 * The default orientation is <tt>SWT.VERTICAL</tt> (see also
	 * {@link #setOrientation(int)}).
	 * 
	 * @param parent
	 *            the parent Composite (non-null)
	 * @param style
	 *            the SWT style of the Composite
	 * @param multipleSelection
	 *            true to allow multiple selection (=check boxes), false for
	 *            single selection (=radio buttons)
	 */
	public ChoiceComposite(final Composite parent, final int style, final boolean multipleSelection) {
		super(parent, style);
		this.isMulti = multipleSelection;
		this.orientation = SWT.VERTICAL;
		isEditable = true;
		applyLayout();
	}

	/**
	 * Creates an appropriate button (check or radio) within this composite.
	 * 
	 * @param caption
	 *            the String on the Button; never null
	 * @param value
	 *            the selection value for the button
	 * 
	 * @since 3.0
	 */
	public Button createChild(final String caption) {
		final int style = isMulti ? SWT.CHECK : SWT.RADIO;
		final Button result = new Button(this, style);
		result.setText(caption);
		result.setForeground(getForeground());
		result.setBackground(getBackground());
		result.addSelectionListener(this);
		new EventForwarder(result, this);
		updateEnabled(result, isEnabled());
		return result;
	}

	/**
	 * Gets the editable state.
	 * 
	 * @return whether or not the receiver is editable
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.0
	 */
	public final boolean getEditable() {
		checkWidget();
		return isEditable;
	}

	/**
	 * Return the margins, in pixels, for the top/bottom, left/right edges of
	 * the widget.
	 * 
	 * @return a Point; never null. The x value corresponds to the top/bottom
	 *         margin. The y value corresponds to the left/right margin.
	 * 
	 * @since 3.0
	 */
	public final Point getMargins() {
		return new Point(marginHeight, marginWidth);
	}

	/**
	 * Returns the orientation of this ChoiceComposite.
	 * 
	 * @return one of <tt>SWT.VERTICAL</tt> or <tt>SWT.HORIZONTAL</tt>.
	 */
	public int getOrientation() {
		return orientation;
	}

	/**
	 * Return the spacing, in pixels, for the right/left and top/bottom edgets
	 * of the cells within the widget.
	 * 
	 * @return a Point; never null. The x value corresponds to the right/left
	 *         spacing. The y value corresponds to the top/bottom spacing.
	 * 
	 * @since 3.0
	 */
	public final Point getSpacing() {
		return new Point(hSpacing, vSpacing);
	}

	/**
	 * Returns true if this instance allows multiple selection, false otherwise.
	 */
	public final boolean isMultipleSelection() {
		return isMulti;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The value will be propagated to children of this composite.
	 */
	@Override
	public final void setBackground(final Color color) {
		setRedraw(false);
		try {
			this.bgColor = color;
			updateBgColor();
		} finally {
			setRedraw(true);
		}
	}

	/**
	 * Sets the editable state.
	 * <p>
	 * Implementation notes:
	 * <p>
	 * (a) {@code setEnabled(false)} takes precedence over
	 * {@code setEditable(boolean)}
	 * <p>
	 * (b) on {@code setEditable(false)} the widget will automatically disable
	 * all buttons which are not selected. It will also block / revert selection
	 * events from the user. However, if the selection of the buttons is changed
	 * programatically, you have to invoke {@code choice.setEditable(false)}
	 * afterwards to update the state of the contained buttons.
	 * 
	 * @param editable
	 *            the new editable state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.0
	 */
	public final void setEditable(final boolean editable) {
		this.isEditable = editable;
		updateEnabled(getEnabled());
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This will also update the enabled state all buttons contained in this
	 * widget.
	 * <p>
	 * Implementation note: {@code setEnabled(false)} takes precedence over
	 * {@code setEditable(boolean)}.
	 */
	@Override
	public final void setEnabled(final boolean enabled) {
		setRedraw(false);
		try {
			super.setEnabled(enabled);
			updateEnabled(enabled);
			updateBgColor();
		} finally {
			setRedraw(true);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The value will be propagated to children of this composite.
	 */
	@Override
	public final void setForeground(final Color color) {
		setRedraw(false);
		try {
			super.setForeground(color);
			for (final Control child : getChildren()) {
				child.setForeground(color);
			}
		} finally {
			setRedraw(true);
		}
	}

	/**
	 * Sets the margin for the top/bottom, left/right edges of the widget.
	 * 
	 * @param marginHeight
	 *            the margin, in pixels, that will be placed along the top and
	 *            bottom edges of the widget. The default value is 0.
	 * @param marginWidth
	 *            the margin, in pixels, that will be placed along the left and
	 *            right edges of the widget. The default value is 0.
	 * 
	 * @since 3.0
	 */
	public final void setMargins(final int marginHeight, final int marginWidth) {
		Assert.isLegal(marginHeight >= 0, "marginHeight must be greater or equal to zero: " + marginHeight); //$NON-NLS-1$
		Assert.isLegal(marginWidth >= 0, "marginWidth must be greater or equal to zero: " + marginWidth); //$NON-NLS-1$
		this.marginHeight = marginHeight;
		this.marginWidth = marginWidth;
		applyLayout();
	}

	/**
	 * Sets the orientation (vertical or horizontal) of the choices in this
	 * composite.
	 * 
	 * @param orientation
	 *            <tt>SWT.VERTICAL</tt> for vertical orientation or
	 *            <tt>SWT.HORIZONTAL</tt> for horizontal orientation
	 * @throws RuntimeException
	 *             if orientation has an unsupported value
	 */
	public final void setOrientation(final int orientation) {
		Assert.isLegal(orientation == SWT.VERTICAL || orientation == SWT.HORIZONTAL);
		if (this.orientation != orientation) {
			this.orientation = orientation;
			applyLayout();
		}
	}

	/**
	 * Sets the spacing for the right/left and top/bottom edges of the cells
	 * within the widget.
	 * <p>
	 * Implementation note: only one of the two values will be used depending on
	 * the orientation (horizontal or vertical &ndash; see
	 * {@link #setOrientation(int)}).
	 * 
	 * @param hSpacing
	 *            the space, in pixels, between the right edge of a cell and the
	 *            left edge of the cell to the left. The default value is 3.
	 * @param vSpacing
	 *            the space, in pixels, between the bottom edge of a cell and
	 *            the top edge of the cell underneath. The default value is 0.
	 * 
	 * @since 3.0
	 */
	public final void setSpacing(final int hSpacing, final int vSpacing) {
		Assert.isLegal(hSpacing >= 0, "hSpacing must be greater or equal to zero: " + hSpacing); //$NON-NLS-1$
		Assert.isLegal(vSpacing >= 0, "vSpacing must be greater or equal to zero: " + vSpacing); //$NON-NLS-1$
		this.hSpacing = hSpacing;
		this.vSpacing = vSpacing;
		applyLayout();
	}

	/**
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public void widgetSelected(final SelectionEvent e) {
		if (!isEditable) {
			final Button button = (Button) e.widget;
			button.setSelection(button.isEnabled());
		}
	}

	/**
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public void widgetDefaultSelected(final SelectionEvent e) {
		// unused
	}

	// helping methods
	// ////////////////

	private void applyLayout() {
		if (orientation == SWT.VERTICAL) {
			final FillLayout layout = new FillLayout(SWT.VERTICAL);
			layout.marginHeight = marginHeight;
			layout.marginWidth = marginWidth;
			layout.spacing = vSpacing;
			setLayout(layout);
		} else {
			final RowLayout layout = new RowLayout(SWT.HORIZONTAL);
			layout.marginHeight = marginHeight;
			layout.marginWidth = marginWidth;
			layout.marginLeft = 0;
			layout.marginRight = 0;
			layout.marginTop = 0;
			layout.marginBottom = 0;
			layout.spacing = hSpacing;
			layout.wrap = false;
			setLayout(layout);
		}
	}

	private void updateBgColor() {
		updateBgColor(isEnabled() && bgColor != null ? bgColor : getParent().getBackground());
	}

	private void updateBgColor(final Color color) {
		super.setBackground(color);
		for (final Control child : getChildren()) {
			child.setBackground(color);
		}
	}

	private void updateEnabled(final boolean isEnabled) {
		for (final Control child : getChildren()) {
			updateEnabled(child, isEnabled);
		}
	}

	private void updateEnabled(final Control child, final boolean isEnabled) {
		if (!isEditable && isEnabled) {
			final Button button = (Button) child;
			child.setEnabled(button.getSelection());
		} else { // isEditable && (isEnabled == true || isEnabled == false)
			child.setEnabled(isEnabled);
		}
	}
}