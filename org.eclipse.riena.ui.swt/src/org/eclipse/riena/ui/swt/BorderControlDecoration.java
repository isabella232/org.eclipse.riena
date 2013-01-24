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
package org.eclipse.riena.ui.swt;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.internal.ui.swt.Activator;
import org.eclipse.riena.ui.ridgets.IControlDecoration;

/**
 * This class renders a decoration around the control. For every decorated control a border is painted.
 * 
 * @since 2.0
 */
public class BorderControlDecoration implements IControlDecoration {
	private final BorderDrawer borderDrawer;
	private boolean visible;

	/**
	 * Creates a new instance of {@code ControlDecoration} for decorating the specified control.
	 * 
	 * @param control
	 *            the control to be decorated
	 */
	public BorderControlDecoration(final Control control) {
		this(control, BorderDrawer.DEFAULT_BORDER_WIDTH);
	}

	/**
	 * Creates a new instance of {@code ControlDecoration} for decorating the specified control.
	 * 
	 * @param control
	 *            the control to be decorated
	 */
	public BorderControlDecoration(final Control control, final int borderWidth) {
		this(control, borderWidth, null);
	}

	/**
	 * Creates a new instance of {@code ControlDecoration} for decorating the specified control.
	 * 
	 * @param control
	 *            the control to be decorated
	 * @param borderWidth
	 *            the width of the border
	 * @param borderColor
	 *            the color of the border
	 * 
	 */
	public BorderControlDecoration(final Control control, final int borderWidth, final Color borderColor) {
		this(control, borderWidth, borderColor, null);
	}

	/**
	 * Creates a new instance of {@code ControlDecoration} for decorating the specified control.
	 * 
	 * @param control
	 *            the control to be decorated
	 * @param borderWidth
	 *            the width of the border
	 * @param borderColor
	 *            the color of the border
	 * @param activationStrategy
	 *            the activationStrategy of the decoration
	 * @since 3.0
	 */
	public BorderControlDecoration(final Control control, final int borderWidth, final Color borderColor, final IDecorationActivationStrategy activationStrategy) {
		this(control, borderWidth, borderColor, false, activationStrategy);
	}

	/**
	 * Creates a new instance of {@code ControlDecoration} for decorating the specified control.
	 * 
	 * @param control
	 *            the control to be decorated
	 * @param borderWidth
	 *            the width of the border
	 * @param borderColor
	 *            the color of the border
	 * @param useVisibleControlArea
	 *            <code>true</code> if the border should be drawn according to the visible control area. This does not work for all control types.
	 * @param activationStrategy
	 *            the activationStrategy of the decoration
	 * @since 5.0
	 */
	public BorderControlDecoration(final Control control, final int borderWidth, final Color borderColor, final boolean useVisibleControlArea,
			final IDecorationActivationStrategy activationStrategy) {
		borderDrawer = new BorderDrawer(getBorderControl(control), checkBorderWidth(borderWidth), borderColor, useVisibleControlArea,
				new IDecorationActivationStrategy() {
					public boolean isActive() {
						return visible && (activationStrategy == null || activationStrategy.isActive());
					}
				});
		borderDrawer.register();
	}

	/**
	 * Disposes this {@code BorderControlDecoration}. Unhooks any listeners that have been installed on the target control. This method has no effect if the
	 * receiver is already disposed.
	 */
	public void dispose() {
		borderDrawer.dispose();
	}

	/**
	 * Returns the color of the border.
	 * 
	 * @return border color
	 */
	public Color getBorderColor() {
		return borderDrawer.getBorderColor();
	}

	/**
	 * Returns the width of the border.
	 * 
	 * @return border width
	 */
	public int getBorderWidth() {
		return borderDrawer.getBorderWidth();
	}

	/**
	 * Hides the control decoration and any associated hovers. This message has no effect if the decoration is already hidden.
	 */
	public void hide() {
		if (visible) {
			visible = false;
			borderDrawer.update(true);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.0
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets the color of the border.
	 * 
	 * @param borderColor
	 *            border color
	 */
	public void setBorderColor(final Color borderColor) {
		borderDrawer.setBorderColor(borderColor);
		borderDrawer.update(false);
	}

	/**
	 * Shows the control decoration. This message has no effect if the decoration is already showing.
	 */
	public void show() {
		if (!visible) {
			visible = true;
			borderDrawer.scheduleUpdate(true);
		}
	}

	// helping methods
	//////////////////

	private static void logWarning(final String message) {
		final Logger logger = Log4r.getLogger(Activator.getDefault(), BorderControlDecoration.class);
		logger.log(LogService.LOG_WARNING, message);
	}

	private int checkBorderWidth(final int candidate) {
		int result = candidate;
		if (candidate < 0) {
			logWarning("BorderWidth is lower than 0: " + candidate); //$NON-NLS-1$
			result = 0;
		}
		return result;
	}

	private Control getBorderControl(final Control control) {
		Control result = control;
		// workaround for DatePicker
		if (control.getParent() instanceof DatePickerComposite) {
			result = control.getParent();
		}
		return result;
	}
}
