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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.ui.swt.facades.GCFacade;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;

/**
 * The StatusMeter is intended to visualize processes that run for much longer
 * periods than those that are usually visualized by a <code>ProgressBar</code>.
 * In difference to the <code>ProgressBar</code>, it offers the ability to
 * change colors and it can be used in tables.
 * <p>
 * <b>NOTE:</b> This is an image factory for use in {@link ColumnFormatter} and
 * alike. See {@link StatusMeterWidget} for an actual widget implementation.
 * </p>
 * <p>
 * StatusMeter uses a builder pattern. Use one of the static methods to create a
 * builder.
 * 
 * @since 3.0
 */
public class StatusMeter {

	protected StatusMeter() {
	}

	/**
	 * Creates an empty builder instance.
	 * 
	 * @return A builder instance
	 */
	public static StatusMeterBuilder empty() {
		return new StatusMeterBuilder();
	}

	/**
	 * Creates a builder instance with the following values
	 * <ul>
	 * <li>maximum = 100
	 * <li>width = 100
	 * <li>height = 16
	 * <li>leftIndent = 1
	 * <li>margin = 1
	 * </ul>
	 * 
	 * @return A builder instance
	 */
	public static StatusMeterBuilder imageDefault() {
		return new StatusMeterBuilder().maximum(100).width(100).height(16).leftIndent(1).margin(1);
	}

	/**
	 * Creates a builder instance with the following values
	 * <ul>
	 * <li>value = 100
	 * <li>maximum = 100
	 * <li>width = 100
	 * <li>height = 16
	 * <li>leftIndent = 1
	 * <li>margin = 1
	 * </ul>
	 * 
	 * @return A builder instance
	 */
	public static StatusMeterBuilder imageFinished() {
		return new StatusMeterBuilder().value(100).maximum(100).width(100).height(16).leftIndent(1).margin(1);
	}

	/**
	 * Creates a builder instance with the following values
	 * <ul>
	 * <li>maximum = 100
	 * <li>width = 100
	 * <li>height = 16
	 * <li>leftIndent = 0
	 * <li>margin = 0
	 * </ul>
	 * 
	 * @return A builder instance
	 */
	public static StatusMeterBuilder widgetDefault() {
		return StatusMeter.imageDefault().leftIndent(0).margin(0);
	}

	/**
	 * Creates a builder instance with the following values
	 * <ul>
	 * <li>value = 100
	 * <li>maximum = 100
	 * <li>width = 100
	 * <li>height = 16
	 * <li>leftIndent = 0
	 * <li>margin = 0
	 * </ul>
	 * 
	 * @return A builder instance
	 */
	public static StatusMeterBuilder widgetFinished() {
		return StatusMeter.imageFinished().leftIndent(0).margin(0);
	}

	/**
	 * Builder for StatusMeter
	 * 
	 * @see StatusMeter
	 */
	public static final class StatusMeterBuilder {
		private int value = 0;
		private int maximum = 100;
		private int minimum = 0;
		private int width = 0;
		private int height = 0;
		private int indent = 0;
		private int margin = 0;

		private Color borderColor;
		private Color gradientStartColor;
		private Color gradientEndColor;
		private Color backgroundColor;
		private final Color transparentColor;

		private static final int BORDER_WIDTH = 1;

		private StatusMeterBuilder() {
			final RienaDefaultLnf lnf = LnfManager.getLnf();
			borderColor = lnf.getColor(LnfKeyConstants.STATUS_METER_BORDER_COLOR);
			gradientStartColor = lnf.getColor(LnfKeyConstants.STATUS_METER_GRADIENT_START_COLOR);
			gradientEndColor = lnf.getColor(LnfKeyConstants.STATUS_METER_GRADIENT_END_COLOR);
			backgroundColor = lnf.getColor(LnfKeyConstants.STATUS_METER_BACKGROUND_COLOR);
			transparentColor = lnf.getColor(LnfKeyConstants.STATUS_METER_TRANSPARENT_COLOR);
		}

		/**
		 * Returns a StatusMeter as image (data).
		 * 
		 * @return An image (data) according to the values set to the builder
		 * @throws IllegalArgumentException
		 *             if conditions that are described in the setter methods
		 *             are violated
		 * @since 4.0
		 */
		public ImageData getImageData() {
			if (width <= 0 || height <= 0) {
				throw new IllegalArgumentException("Width and height must be values greater than 0 to draw."); //$NON-NLS-1$
			}
			if (value < 0 || value > maximum) {
				throw new IllegalArgumentException("Value must be positive and smaller or equal to the maximum value."); //$NON-NLS-1$
			}
			if (minimum < 0) {
				throw new IllegalArgumentException("Minimum must be positive."); //$NON-NLS-1$
			}
			if (margin < 0 || margin > (height / 2) || margin > (width / 2)) {
				throw new IllegalArgumentException(
						"Margin must be positive and must not be greater than half of the height and half of the width."); //$NON-NLS-1$
			}
			if (indent < 0 || indent > width) {
				throw new IllegalArgumentException(
						"Indentation must be positive and must not be greater than the total width."); //$NON-NLS-1$
			}

			// TODO sma@2010-08-17 Is this the Riena way?
			final Display display = Display.getCurrent();
			final Image tempImage = GCFacade.getDefault().createImage(display, width, height);

			//final Image image = new Image(display, width, height);
			//final GC gc = new GC(image);

			final GC gc = GCFacade.getDefault().createGCFromImage(tempImage);

			final int start = margin + indent + BORDER_WIDTH;
			final int end = width - (margin * 2) - BORDER_WIDTH - 1;
			final int barHeight = height - (margin * 2) - (BORDER_WIDTH * 2);

			final int actualWidth = end - start + 1;
			final float scale = (float) actualWidth / (maximum - minimum);
			final int actualValue = (int) ((value - minimum) * scale);

			// Background
			gc.setBackground(transparentColor);
			gc.fillRectangle(0, 0, width, height);

			// Progress
			gc.setForeground(gradientStartColor);
			gc.setBackground(gradientEndColor);
			gc.fillGradientRectangle(start, margin + BORDER_WIDTH, actualValue, barHeight, true);

			// Background
			gc.setBackground(backgroundColor);
			gc.fillRectangle(start + actualValue, margin + BORDER_WIDTH, actualWidth - actualValue, barHeight);

			// Border
			gc.setForeground(borderColor);
			final int bottom = height - margin - 1;
			// horizontal lines
			gc.drawLine(start, margin, end, margin);
			gc.drawLine(start, bottom, end, bottom);
			// vertical lines
			gc.drawLine(start - BORDER_WIDTH, margin + BORDER_WIDTH, start - BORDER_WIDTH, bottom - BORDER_WIDTH);
			gc.drawLine(end + BORDER_WIDTH, margin + BORDER_WIDTH, end + BORDER_WIDTH, bottom - BORDER_WIDTH);

			final ImageData imageData = tempImage.getImageData();
			gc.dispose();
			tempImage.dispose();

			imageData.transparentPixel = imageData.palette.getPixel(transparentColor.getRGB());
			return imageData;
		}

		/**
		 * Value must be positive and must not exceed the set maximum.
		 * 
		 * @param value
		 * @return The builder
		 */
		public StatusMeterBuilder value(final int value) {
			this.value = value;
			return this;
		}

		/**
		 * Maximum must be positive and greater or equal to the set value.
		 * 
		 * @param max
		 * @return The builder
		 */
		public StatusMeterBuilder maximum(final int max) {
			this.maximum = max;
			return this;
		}

		/**
		 * Minimum must be positive.
		 * 
		 * @param min
		 * @return The builder
		 */
		public StatusMeterBuilder minimum(final int min) {
			this.minimum = min;
			return this;
		}

		/**
		 * Width must be greater than 0.
		 * 
		 * @param width
		 * @return The builder
		 */
		public StatusMeterBuilder width(final int width) {
			this.width = width;
			return this;
		}

		/**
		 * Height must be greater than 0.
		 * 
		 * @param height
		 * @return The builder
		 */
		public StatusMeterBuilder height(final int height) {
			this.height = height;
			return this;
		}

		/**
		 * Indent must be positive and smaller than the set width.
		 * 
		 * @param indent
		 * @return The builder
		 */
		public StatusMeterBuilder leftIndent(final int indent) {
			this.indent = indent;
			return this;
		}

		/**
		 * Margin must be positive and smaller than half of the set height and
		 * half of the set width.
		 * 
		 * @param margin
		 * @return The builder
		 */
		public StatusMeterBuilder margin(final int margin) {
			this.margin = margin;
			return this;
		}

		/**
		 * The color that is used for the border. Default is set in
		 * {@link RienaDefaultLnf}.
		 * 
		 * @param color
		 * @return The builder
		 */
		public StatusMeterBuilder borderColor(final Color color) {
			this.borderColor = color;
			return this;
		}

		/**
		 * The color that is used for the background area that is not filed with
		 * the bar. Default is set in {@link RienaDefaultLnf}.
		 * 
		 * @param color
		 * @return The builder
		 */
		public StatusMeterBuilder backgroundColor(final Color color) {
			this.backgroundColor = color;
			return this;
		}

		/**
		 * The start color that makes the gradient of the bar. Default is set in
		 * {@link RienaDefaultLnf}.
		 * 
		 * @param color
		 * @return The builder
		 */
		public StatusMeterBuilder gradientStartColor(final Color color) {
			this.gradientStartColor = color;
			return this;
		}

		/**
		 * The end color that makes the gradient of the bar. Default is set in
		 * {@link RienaDefaultLnf}.
		 * 
		 * @param color
		 * @return The builder
		 */
		public StatusMeterBuilder gradientEndColor(final Color color) {
			this.gradientEndColor = color;
			return this;
		}
	}
}
