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

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.internal.ui.swt.Activator;
import org.eclipse.riena.ui.ridgets.IControlDecoration;
import org.eclipse.riena.ui.swt.facades.SWTFacade;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * This class renders a decoration around the control. For every decorated
 * control a border is painted.
 * 
 * @since 2.0
 */
public class BorderControlDecoration implements IControlDecoration {

	private static final Rectangle ZERO_RECTANGLE = new Rectangle(0, 0, 0, 0);
	private static final int DEFAULT_BORDER_WIDTH = 1;

	private Control control;

	private DisposeListener disposeListener;
	private PaintListener paintListener;
	private ControlListener updateListener;
	/**
	 * The area that needs updating before drawing the decoration somewhere
	 * else.
	 */
	private Rectangle updateArea;

	private Color borderColor;
	private int borderWidth = DEFAULT_BORDER_WIDTH;

	private boolean visible;
	private final IDecorationActivationStrategy activationStrategy;

	private static void logWarning(final String message) {
		final Logger logger = Log4r.getLogger(Activator.getDefault(), BorderControlDecoration.class);
		logger.log(LogService.LOG_WARNING, message);
	}

	private static class DefaultActivationStrategy implements IDecorationActivationStrategy {

		public boolean isActive() {
			return true;
		}

	}

	/**
	 * Creates a new instance of {@code ControlDecoration} for decorating the
	 * specified control.
	 * 
	 * @param control
	 *            the control to be decorated
	 */
	public BorderControlDecoration(final Control control) {
		this(control, DEFAULT_BORDER_WIDTH);
	}

	/**
	 * Creates a new instance of {@code ControlDecoration} for decorating the
	 * specified control.
	 * 
	 * @param control
	 *            the control to be decorated
	 */
	public BorderControlDecoration(final Control control, final int borderWidth) {
		this(control, borderWidth, null);
	}

	/**
	 * Creates a new instance of {@code ControlDecoration} for decorating the
	 * specified control.
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
		this(control, borderWidth, borderColor, new DefaultActivationStrategy());
	}

	/**
	 * Creates a new instance of {@code ControlDecoration} for decorating the
	 * specified control.
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
	public BorderControlDecoration(final Control control, final int borderWidth, final Color borderColor,
			final IDecorationActivationStrategy activationStrategy) {
		this.activationStrategy = activationStrategy;
		this.control = getBorderControl(control);
		this.borderWidth = checkBorderWidth(borderWidth);
		this.borderColor = borderColor;
		addListeners();
	}

	/**
	 * Disposes this {@code BorderControlDecoration}. Unhooks any listeners that
	 * have been installed on the target control. This method has no effect if
	 * the receiver is already disposed.
	 */
	public void dispose() {
		if (control == null) {
			return;
		}
		removeListeners();
		control = null;
	}

	/**
	 * Returns the color of the border.
	 * 
	 * @return border color
	 */
	public Color getBorderColor() {
		return borderColor;
	}

	/**
	 * Returns the width of the border.
	 * 
	 * @return border width
	 */
	public int getBorderWidth() {
		return borderWidth;
	}

	/**
	 * Hides the control decoration and any associated hovers. This message has
	 * no effect if the decoration is already hidden.
	 */
	public void hide() {
		if (visible) {
			visible = false;
			update();
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
		this.borderColor = borderColor;
		update();
	}

	/**
	 * Shows the control decoration. This message has no effect if the
	 * decoration is already showing.
	 */
	public void show() {
		if (!visible) {
			visible = true;
			update();
		}
	}

	// helping methods
	//////////////////

	/**
	 * Add any listeners needed on the target control and on the composite where
	 * the decoration is to be rendered.
	 */
	private void addListeners() {

		disposeListener = new DisposeListener() {
			public void widgetDisposed(final DisposeEvent event) {
				dispose();
			}
		};
		control.addDisposeListener(disposeListener);

		updateListener = new ControlListener() {
			/**
			 * After moving the control will be redrawn.
			 */
			public void controlMoved(final ControlEvent e) {
				update();
			}

			/**
			 * After resizing the control will be redrawn.
			 */
			public void controlResized(final ControlEvent e) {
				update();
			}
		};

		paintListener = new PaintListener() {
			/**
			 * {@inheritDoc}
			 * <p>
			 * Paints a border around the decorated control, if the decoration
			 * is visible.
			 */
			public void paintControl(final PaintEvent event) {
				if (shouldShowDecoration()) {
					final Control uiControl = (Control) event.widget;
					final Rectangle rect = getDecorationRectangle(uiControl);
					onPaint(event.gc, rect);
				}
			}
		};

		// We do not know which parent in the control hierarchy
		// is providing the decoration space, so hook all the way up, until
		// the shell or the specified parent composite is reached.
		Control c = control;
		final Rectangle controlBounds = control.getBounds();
		while (c != null) {
			addPaintAndUpdateListeners(c);
			if (c instanceof Shell) {
				// We just installed on a shell, so don't go further
				c = null;
			} else if (c instanceof ScrolledComposite) {
				c = null;
			} else {
				final Rectangle globalRect = getGlobalRectangle(c);
				if (((globalRect.x >= 0) && (globalRect.y >= 0))
						&& ((controlBounds.width + getBorderWidth() <= globalRect.width) && (controlBounds.height
								+ getBorderWidth() <= globalRect.height))) {
					c = null;
				} else {
					c = c.getParent();
				}
			}
		}
	}

	private Rectangle getDecorationRectangle(final Control targetControl) {

		final Rectangle globalRect = getGlobalRectangle(targetControl);
		if ((globalRect.width <= 0) && (globalRect.height <= 0)) {
			return globalRect;
		}

		globalRect.x -= getBorderWidth();
		globalRect.y -= getBorderWidth();
		globalRect.height += 2 * getBorderWidth();
		globalRect.width += 2 * getBorderWidth();

		return globalRect;

	}

	private Rectangle getGlobalRectangle(final Control targetControl) {

		if (control == null) {
			return ZERO_RECTANGLE;
		}

		final Rectangle controlBounds = control.getBounds();
		if ((controlBounds.width <= 0) && (controlBounds.height <= 0)) {
			return ZERO_RECTANGLE;
		}
		final int x = controlBounds.x;
		final int y = controlBounds.y;
		final Point globalPoint = control.getParent().toDisplay(x, y);
		Point targetPoint;
		if (targetControl == null) {
			targetPoint = globalPoint;
		} else {
			targetPoint = targetControl.toControl(globalPoint);
		}
		final int width = controlBounds.width - 1;
		final int height = controlBounds.height - 1;
		return new Rectangle(targetPoint.x, targetPoint.y, width, height);

	}

	/**
	 * Installs the listeners used to paint on the control.
	 */
	private void addPaintAndUpdateListeners(final Control control) {
		if (!control.isDisposed()) {
			SWTFacade.getDefault().addPaintListener(control, paintListener);
			control.addControlListener(updateListener);
		}
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
			result = this.control.getParent();
		} else if (MasterDetailsComposite.BIND_ID_TABLE.equals(SWTBindingPropertyLocator.getInstance()
				.locateBindingProperty(control))) {
			result = control.getParent().getParent();
		}
		return result;
	}

	/**
	 * Paints the border.
	 * 
	 * @param gc
	 *            graphical context
	 * @param rect
	 *            the rectangle to draw
	 */
	private void onPaint(final GC gc, final Rectangle rect) {
		if ((rect.width == 0) && (rect.height == 0)) {
			return;
		}

		final Color previousForeground = gc.getForeground();
		if (getBorderColor() != null) {
			gc.setForeground(getBorderColor());
		} else {
			logWarning("BorderColor is null!"); //$NON-NLS-1$
		}
		for (int i = 0; i < getBorderWidth(); i++) {
			gc.drawRectangle(rect.x + i, rect.y + i, rect.width - i * 2, rect.height - i * 2);
		}
		gc.setForeground(previousForeground);
	}

	/**
	 * Removes every listeners installed on the controls.
	 */
	private void removeListeners() {
		if (control == null) {
			return;
		}
		control.removeDisposeListener(disposeListener);

		Control c = control;
		while (c != null) {
			removePaintAndUpdateListeners(c);
			if (c instanceof Shell) {
				// We previously installed listeners only up to the first Shell
				// encountered, so stop.
				c = null;
			} else {
				c = c.getParent();
			}
		}

		paintListener = null;
		updateListener = null;
		disposeListener = null;
	}

	/**
	 * Removes the listeners used to paint on the control.
	 */
	private void removePaintAndUpdateListeners(final Control control) {
		if (!control.isDisposed()) {
			control.removeControlListener(updateListener);
			SWTFacade.getDefault().removePaintListener(control, paintListener);
		}
	}

	/**
	 * Returns whether the decoration should be shown or it should not.
	 * 
	 * @return {@code true} if the decoration should be shown, {@code false} if
	 *         it should not.
	 */
	private boolean shouldShowDecoration() {
		if (activationStrategy != null && !activationStrategy.isActive()) {
			return false;
		}
		if (!visible) {
			return false;
		}
		if (SwtUtilities.isDisposed(control)) {
			return false;
		}
		if (!control.isVisible()) {
			return false;
		}
		if (getBorderWidth() <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * Something has changed, requiring redraw. Redraw the decoration.
	 */
	private void update() {
		if (SwtUtilities.isDisposed(control) || getBorderWidth() <= 0) {
			return;
		}
		final Shell shell = control.getShell();
		if (updateArea != null) {
			shell.redraw(updateArea.x, updateArea.y, updateArea.width, updateArea.height, true);
		}
		updateArea = getDecorationRectangle(shell);
		updateArea.x = updateArea.x - 1;
		updateArea.y = updateArea.y - 1;
		updateArea.width = updateArea.width + 2;
		updateArea.height = updateArea.height + 2;
		// Redraw this rectangle in all children
		shell.redraw(updateArea.x, updateArea.y, updateArea.width, updateArea.height, true);
		shell.update();
	}

}
