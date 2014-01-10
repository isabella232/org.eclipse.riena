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
package org.eclipse.riena.navigation.ui.swt.lnf.renderer;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.ui.swt.facades.GCFacade;
import org.eclipse.riena.ui.swt.facades.SWTFacade;
import org.eclipse.riena.ui.swt.lnf.AbstractLnfRenderer;
import org.eclipse.riena.ui.swt.lnf.FlasherSupportForRenderer;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.ImageStore;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Renderer of a tab of the switcher between sub-applications.
 */
public class SubApplicationTabRenderer extends AbstractLnfRenderer {

	private Color defaultColor = null;

	public final static int ACTIVE_Y_OFFSET = 2;
	private final static int BORDER_TOP_WIDTH = 3;
	private final static int BORDER_BOTTOM_WIDTH = 1;
	private final static int BORDER_LEFT_WIDTH = 2;
	private final static int BORDER_RIGHT_WIDTH = 2;
	private final static int TEXT_TOP_INSET = 3;
	private final static int TEXT_BOTTOM_INSET = 4;
	private final static int TEXT_LEFT_INSET = 6;
	private final static int TEXT_RIGHT_INSET = 6;
	private final static int ACTIVE_BOTTOM_INSET = 6;
	private final static int ACTIVE_LEFT_INSET = 3;
	private final static int ACTIVE_RIGHT_INSET = 3;
	private final static int ICON_TEXT_GAP = 4;

	private Color selStartColor;
	private Color selEndColor;
	private Image image;
	private String icon;
	private String label;
	private boolean active;
	private Control control;
	private final FlasherSupportForRenderer flasherSupport;

	/**
	 * Create a new instance of the renderer of a tab of the sub-application
	 * switcher.
	 */
	public SubApplicationTabRenderer() {
		super();
		flasherSupport = new FlasherSupportForRenderer(this, new MarkerUpdater());
	}

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.AbstractLnfRenderer#paint(org.eclipse.swt.graphics.GC,
	 *      java.lang.Object)
	 */
	@Override
	public void paint(final GC gc, final Object value) {

		super.paint(gc, value);
		Assert.isNotNull(gc);
		Assert.isNotNull(value);
		Assert.isTrue(value instanceof Control);
		control = (Control) value;

		if (getBounds().y - ACTIVE_Y_OFFSET < 0) {
			final Rectangle bounds = new Rectangle(getBounds().x, ACTIVE_Y_OFFSET, getBounds().width,
					getBounds().height);
			setBounds(bounds);
		}

		final RienaDefaultLnf lnf = LnfManager.getLnf();
		final Font font = getTabFont();
		gc.setFont(font);

		paintBackground(gc);

		final int leftInset = getLeftInset();
		final int rightInset = getRightInset();
		Color borderTopRightColor = lnf.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_BORDER_TOP_RIGHT_COLOR);
		Color borderBottomLeftColor = lnf.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_BORDER_BOTTOM_LEFT_COLOR);
		if (!isEnabled()) {
			borderTopRightColor = lnf
					.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_DISABLED_BORDER_TOP_RIGHT_COLOR);
			borderBottomLeftColor = lnf
					.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_DISABLED_BORDER_BOTTOM_LEFT_COLOR);
		}
		Color borderBottomLeftSecondaryColor = lnf
				.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_BORDER_BOTTOM_LEFT_SECONDARY_COLOR);
		// Border
		// - left
		gc.setForeground(borderBottomLeftColor);
		int x = getBounds().x - leftInset;
		int y = getBounds().y + BORDER_TOP_WIDTH - 1;
		int x2 = x;
		int y2 = getBounds().y + getHeight() - 1;
		if (isActive()) {
			GCFacade.getDefault().setAntialias(gc, SWT.OFF);
		}
		gc.drawLine(x, y, x2, y2);
		if (isActive()) {
			gc.setForeground(borderBottomLeftSecondaryColor);
			gc.drawPoint(x, y2);
			gc.drawLine(x + 1, y, x2 + 1, y2 - 1);
		}

		final Color innerBorderColor = getInnerBorderColor(lnf);
		if (!isActive()) {
			gc.setForeground(innerBorderColor);
			x += 1;
			x2 += 1;
			gc.drawLine(x, y, x2, y2);
		}
		// -top
		gc.setForeground(borderTopRightColor);
		x = getBounds().x + BORDER_LEFT_WIDTH - leftInset;
		y = getBounds().y;
		x2 = x + getWidth() - BORDER_LEFT_WIDTH - BORDER_RIGHT_WIDTH + rightInset;
		y2 = y;
		gc.drawLine(x, y, x2, y2);
		// --top-left
		//		gc.setAntialias(SWT.ON);
		if (!isActive()) {
			x = getBounds().x - leftInset;
			y = getBounds().y + BORDER_TOP_WIDTH - 1;
			x2 = x + 1;
			y2 = y;
			gc.drawLine(x, y, x2, y2);
		}
		x = getBounds().x + 1 - leftInset;
		y = getBounds().y + BORDER_TOP_WIDTH - 2;
		x2 = x + 1;
		y2 = y;
		gc.drawLine(x, y, x2, y2);
		// --top-right
		if (!isActive()) {
			x = getBounds().x + getWidth() + rightInset;
			y = getBounds().y + BORDER_TOP_WIDTH - 1;
			x2 = x - 1;
			y2 = y;
			gc.drawLine(x, y, x2, y2);
		}
		x = getBounds().x + getWidth() - 1 + rightInset;
		y = getBounds().y + BORDER_TOP_WIDTH - 2;
		x2 = x - 1;
		y2 = y;
		gc.drawLine(x, y, x2, y2);
		// -right
		gc.setForeground(borderTopRightColor);
		final int topBorder = BORDER_TOP_WIDTH - (isActive() ? 1 : 0);
		x = getBounds().x + getWidth() + rightInset;
		y = getBounds().y + topBorder;
		x2 = x;
		y2 = getBounds().y + getHeight() - topBorder;
		gc.drawLine(x, y, x2, y2);
		if (!isActive()) {
			gc.setForeground(innerBorderColor);
		} else {
			gc.setForeground(lnf.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_BORDER_TOP_RIGHT_SECONDARY_COLOR));
			gc.drawPoint(x, y2 + 1);
		}
		x -= 1;
		x2 -= 1;
		gc.drawLine(x, y, x2, y2);
		// - bottom
		if (isActive()) {
			final Color backgroundEndColor = getColor(
					LnfKeyConstants.SUB_APPLICATION_SWITCHER_ACTIVE_BACKGROUND_END_COLOR,
					LnfKeyConstants.SUB_APPLICATION_SWITCHER_PASSIVE_BACKGROUND_END_COLOR, null,
					LnfKeyConstants.SUB_APPLICATION_SWITCHER_PROCESS_FINISHED_BACKGROUND_END_COLOR);
			gc.setForeground(backgroundEndColor);
			borderBottomLeftSecondaryColor = lnf.getColor(LnfKeyConstants.TITLELESS_SHELL_BACKGROUND);
		} else {
			gc.setForeground(borderBottomLeftColor);
		}
		x = getBounds().x - leftInset + (isActive() ? 1 : 0);
		y = getBounds().y + getHeight() - 1;
		x2 = getBounds().x + getWidth() + rightInset - (isActive() ? 2 : 0);
		y2 = y;
		gc.drawLine(x, y, x2, y2);
		// - bottom secondary
		gc.setForeground(borderBottomLeftSecondaryColor);
		gc.drawLine(x - 1, y + 1, x2 + 2, y2 + 1);

		// Icon
		x = getBounds().x + getBounds().width / 2 - getImageTextWidth(gc) / 2;
		if (getImage() != null) {
			y = getBounds().y + BORDER_TOP_WIDTH + getTextTopInset();
			final FontMetrics fontMetrics = gc.getFontMetrics();
			y += fontMetrics.getHeight() / 2;
			y -= getImage().getBounds().height / 2;
			gc.drawImage(getImage(), x, y);
			x += getImage().getBounds().width + ICON_TEXT_GAP;
		}

		// Text
		final Color textColor = getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_ACTIVE_FOREGROUND,
				LnfKeyConstants.SUB_APPLICATION_SWITCHER_PASSIVE_FOREGROUND,
				LnfKeyConstants.SUB_APPLICATION_SWITCHER_DISABLED_FOREGROUND,
				LnfKeyConstants.SUB_APPLICATION_SWITCHER_PASSIVE_FOREGROUND);
		gc.setForeground(textColor);
		y = getBounds().y + BORDER_TOP_WIDTH + getTextTopInset();

		final int fontHeight = gc.getFontMetrics().getHeight();
		if (control.getBounds().height - (y + fontHeight) < 0) {
			y = control.getBounds().height - fontHeight;
		}
		gc.drawText(getLabel(), x, y, SWT.DRAW_TRANSPARENT | SWTFacade.DRAW_MNEMONIC);

		// Selection
		if (isActive() || flasherSupport.isProcessMarkerVisible()) {
			paintSelection(gc);
		}

		flasherSupport.startFlasher();
		GCFacade.getDefault().setAntialias(gc, SWT.DEFAULT);
	}

	private void paintSelection(final GC gc) {

		final RienaDefaultLnf lnf = LnfManager.getLnf();
		final Color selColor = lnf.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_TOP_SELECTION_COLOR);
		gc.setForeground(selColor);
		gc.setBackground(selColor);

		int x = getBounds().x - getLeftInset();
		int y = getBounds().y;
		int w = 2;
		int h = 2;
		gc.fillRectangle(x, y, w, h);
		gc.drawPoint(x + 1, y - 1);

		x = getBounds().x + getWidth() - 1 + getRightInset();
		y = getBounds().y;
		gc.fillRectangle(x, y, w, h);
		gc.drawPoint(x, y - 1);

		gc.setForeground(getSelectionStartColor());
		gc.setBackground(getSelectionEndColor());
		x = getBounds().x + BORDER_LEFT_WIDTH - getLeftInset();
		y = getBounds().y - ACTIVE_Y_OFFSET;
		w = getWidth() - BORDER_LEFT_WIDTH - BORDER_RIGHT_WIDTH + 1 + getRightInset() + getLeftInset();
		h = 4;
		gc.fillGradientRectangle(x, y, w, h, true);
	}

	private Color getInnerBorderColor(final RienaDefaultLnf lnf) {

		Color innerBorderColor = lnf.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_INNER_BORDER_COLOR);

		if (!isEnabled()) {
			innerBorderColor = lnf.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_INNER_DISABLED_BORDER_COLOR);
		}

		if (isProcessFinishedInBackground()) {
			innerBorderColor = lnf
					.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_INNER_PROCESS_FINISHED_BORDER_COLOR);
		}

		return innerBorderColor;

	}

	/**
	 * Paints the background of one tab.
	 * 
	 * @param gc
	 *            Graphic Context
	 */
	private void paintBackground(final GC gc) {

		final Color backgroundStartColor = getColor(
				LnfKeyConstants.SUB_APPLICATION_SWITCHER_ACTIVE_BACKGROUND_START_COLOR,
				LnfKeyConstants.SUB_APPLICATION_SWITCHER_PASSIVE_BACKGROUND_START_COLOR, null,
				LnfKeyConstants.SUB_APPLICATION_SWITCHER_PROCESS_FINISHED_BACKGROUND_START_COLOR);
		gc.setForeground(backgroundStartColor);
		final Color backgroundEndColor = getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_ACTIVE_BACKGROUND_END_COLOR,
				LnfKeyConstants.SUB_APPLICATION_SWITCHER_PASSIVE_BACKGROUND_END_COLOR, null,
				LnfKeyConstants.SUB_APPLICATION_SWITCHER_PROCESS_FINISHED_BACKGROUND_END_COLOR);
		gc.setBackground(backgroundEndColor);

		final int x = getBounds().x + BORDER_LEFT_WIDTH - 1 - getLeftInset();
		final int y = getBounds().y + 1;
		final int w = getWidth() - BORDER_LEFT_WIDTH - BORDER_RIGHT_WIDTH + 3 + getLeftInset() + getRightInset();
		final int h = getHeight() - 1;
		gc.fillGradientRectangle(x, y, w, h, true);

	}

	private int getLeftInset() {
		if (isActive()) {
			return ACTIVE_LEFT_INSET;
		} else {
			return 0;
		}
	}

	private int getRightInset() {
		if (isActive()) {
			return ACTIVE_RIGHT_INSET;
		} else {
			return 0;
		}
	}

	/**
	 * Returns whether a process finished in the background but the flashing
	 * state is not active anymore.
	 * 
	 * @return true if a process has finished in the background but the tab is
	 *         not flashing anymore, false otherwise
	 */
	private boolean isProcessFinishedInBackground() {
		return flasherSupport.isProcessMarkerVisible() && !flasherSupport.isFlashing();
	}

	/**
	 * Returns whether the processFinishedMarker is visible AND the flashing
	 * state is true.
	 * 
	 * @return true if the tab is flashing and the marker is visible as well,
	 *         false otherwise
	 */
	private boolean isProcessFinishedMarkerVisibleWhileFlashing() {
		return (flasherSupport.isProcessMarkerVisible() && flasherSupport.isFlashing());
	}

	int getTextTopInset() {
		return getHorizontalInset(TEXT_TOP_INSET);
	}

	int getTextBottomInset() {
		return getHorizontalInset(TEXT_BOTTOM_INSET);
	}

	int getHorizontalInset(final int defaultValue) {
		if (control != null && control.getBounds() != null) {
			double yDelta = control.getBounds().height - (getBounds().y + getBounds().height);
			if (yDelta < 0) {
				yDelta *= -1;
				return Math.max(0, (int) (defaultValue - (yDelta / 2)));
			}
		}
		return defaultValue;
	}

	private Color getSelectionStartColor() {
		final RienaDefaultLnf lnf = LnfManager.getLnf();
		if ((selStartColor == null) || selStartColor.isDisposed()) {
			final Color selColor = lnf.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_TOP_SELECTION_COLOR);
			selStartColor = SwtUtilities.makeBrighter(selColor, 0.9f);
		}
		return selStartColor;

	}

	private Color getSelectionEndColor() {
		final RienaDefaultLnf lnf = LnfManager.getLnf();
		if ((selEndColor == null) || selStartColor.isDisposed()) {
			final Color selColor = lnf.getColor(LnfKeyConstants.SUB_APPLICATION_SWITCHER_TOP_SELECTION_COLOR);
			selEndColor = SwtUtilities.makeBrighter(selColor, 1.1f);
		}
		return selEndColor;
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfRenderer#dispose()
	 */
	public void dispose() {
		SwtUtilities.dispose(selStartColor);
		SwtUtilities.dispose(selEndColor);
		SwtUtilities.dispose(defaultColor);
	}

	/**
	 * Computes the size of a tab.
	 * 
	 * @param gc
	 * @param value
	 * @return size of tab
	 */
	public Point computeSize(final GC gc, final Object value) {

		final Font font = getTabFont();
		gc.setFont(font);
		final FontMetrics fontMetrics = gc.getFontMetrics();

		int width = getImageTextWidth(gc);
		width = width + BORDER_LEFT_WIDTH + BORDER_RIGHT_WIDTH + TEXT_LEFT_INSET + TEXT_RIGHT_INSET;
		final int minWidth = getSubApplicationSwitcherTabMinWidth();
		width = Math.max(width, minWidth);

		int height = fontMetrics.getHeight();
		height = height + BORDER_TOP_WIDTH + BORDER_BOTTOM_WIDTH + getTextTopInset() + getTextBottomInset();
		if (isActive()) {
			height += ACTIVE_BOTTOM_INSET;
		}

		return new Point(width, height);

	}

	/**
	 * Adds the width of the image, the text and the gap between both.
	 * 
	 * @param gc
	 * @return width
	 */
	private int getImageTextWidth(final GC gc) {

		final Font font = getTabFont();
		gc.setFont(font);

		String tabLabel = getLabel();
		tabLabel = tabLabel.replaceFirst("&", ""); //$NON-NLS-1$ //$NON-NLS-2$
		int width = SwtUtilities.calcTextWidth(gc, tabLabel);
		// Icon
		if (getImage() != null) {
			width += getImage().getBounds().width + ICON_TEXT_GAP;
		}

		return width;

	}

	/**
	 * Returns the font of the tab.
	 * 
	 * @return font
	 */
	private Font getTabFont() {
		final RienaDefaultLnf lnf = LnfManager.getLnf();
		final Font font = lnf.getFont(LnfKeyConstants.SUB_APPLICATION_SWITCHER_FONT);
		return font;
	}

	private int getHeight() {
		return getBounds().height - 1;
	}

	private int getWidth() {
		return getBounds().width - 1;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(final String icon) {
		this.icon = icon;
		setImage(ImageStore.getInstance().getImage(icon));
	}

	private Image getImage() {
		final RienaDefaultLnf lnf = LnfManager.getLnf();
		if (lnf.getBooleanSetting(LnfKeyConstants.SUB_APPLICATION_SWITCHER_TAB_SHOW_ICON)) {
			return image;
		} else {
			return null;
		}
	}

	private void setImage(final Image image) {
		this.image = image;
	}

	public String getLabel() {
		if (label == null) {
			label = ""; //$NON-NLS-1$
		}
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	/**
	 * @since 1.2
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @since 1.2
	 */
	public void setActive(final boolean active) {
		this.active = active;
	}

	/**
	 * This class updates (redraws) the tab, so that the marker are also updated
	 * (redrawn).
	 */
	private class MarkerUpdater implements Runnable {

		/**
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			if (control != null) {
				control.redraw();
			}
		}
	}

	/**
	 * Returns according to the states of the title bar the color of one of the
	 * given key.<br>
	 * If one key is not needed, the parameter can be {@code null}.
	 * 
	 * @param activeColorKey
	 * @param passiveColorKey
	 * @param disabeldColorKey
	 * @return color
	 * @TODO same code in EmbeddedTitlebarRenderer
	 */
	private Color getColor(final String activeColorKey, final String passiveColorKey, final String disabeldColorKey,
			final String processFinishedKey) {

		Color color = null;

		String colorKey = getKey(activeColorKey, passiveColorKey, disabeldColorKey, processFinishedKey);
		if (colorKey == null) {
			colorKey = activeColorKey;
		}

		final RienaDefaultLnf lnf = LnfManager.getLnf();
		color = lnf.getColor(colorKey);
		if (color == null) {
			return getDefaultColor();
		}

		return color;

	}

	private Color getDefaultColor() {
		// this was added so that the class loading no longer accesses the UIThread
		if (defaultColor == null) {
			defaultColor = new Color(null, 0, 0, 0); // black
		}
		return defaultColor;
	}

	/**
	 * Returns according to the state of the title bar one of the given keys.<br>
	 * If one key is not needed, the parameter can be {@code null}.
	 * 
	 * @param activeKey
	 * @param passiveKey
	 * @param disabeldKey
	 * @return key
	 * @TODO same code in EmbeddedTitlebarRenderer Returns according to the
	 */
	private String getKey(final String activeKey, final String passiveKey, final String disabeldKey,
			final String processFinishedKey) {

		String key = null;
		if (isEnabled()) {
			if (isActive() || isProcessFinishedMarkerVisibleWhileFlashing()) {
				key = activeKey;
			} else if (isProcessFinishedInBackground()) {
				key = processFinishedKey;
			} else {
				key = passiveKey;
			}
		} else {
			key = disabeldKey;
		}

		if (key == null) {
			key = activeKey;
		}
		if (key == null) {
			key = passiveKey;
		}
		if (key == null) {
			key = disabeldKey;
		}

		return key;

	}

	/**
	 * Returns the minimum width of a tab.
	 * 
	 * @return minimum width or 0, if no minimum width is set
	 */
	private int getSubApplicationSwitcherTabMinWidth() {
		final RienaDefaultLnf lnf = LnfManager.getLnf();
		return lnf.getIntegerSetting(LnfKeyConstants.SUB_APPLICATION_SWITCHER_TAB_MIN_WIDTH, 0);
	}

}
