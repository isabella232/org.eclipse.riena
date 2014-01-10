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
package org.eclipse.riena.ui.swt;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.core.marker.Markable;
import org.eclipse.riena.core.util.ListenerList;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.swt.facades.SWTFacade;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.renderer.EmbeddedTitlebarRenderer;

/**
 * Title bar of an embedded window (e.g. view of the current sub-module).
 */
public class EmbeddedTitleBar extends Canvas {

	private boolean windowActive;
	private boolean pressed;
	private boolean hover;
	private boolean closeButtonPressed;
	private boolean closeButtonHover;
	private boolean closeable;
	private Image image;
	private String title;
	private String toolTipText;
	private Collection<? extends IMarker> markers;

	protected ListenerList<IEmbeddedTitleBarListener> titleBarListeners;
	private PaintListener paintListener;
	private TitlebarMouseListener mouseListener;

	/**
	 * Constructs a new instance of {@code EmbeddedTitleBar} given its parent
	 * and a style value describing its behavior and appearance.
	 * 
	 * @param parent
	 *            a composite control which will be the parent of the new
	 *            instance (cannot be null)
	 * @param style
	 *            the style of control to construct
	 */
	public EmbeddedTitleBar(final Composite parent, final int style) {
		super(parent, style | SWT.DOUBLE_BUFFERED);
		addListeners();
		titleBarListeners = new ListenerList<IEmbeddedTitleBarListener>(IEmbeddedTitleBarListener.class);
	}

	/**
	 * Adds a {@code PaintListener} to this {@code EmbeddedTitleBar}.
	 */
	protected void addListeners() {

		paintListener = new PaintListener() {
			public void paintControl(final PaintEvent e) {
				onPaint(e);
			}
		};
		addPaintListener(paintListener);

		mouseListener = new TitlebarMouseListener();
		addMouseListener(mouseListener);
		final SWTFacade swtFacade = SWTFacade.getDefault();
		swtFacade.addMouseMoveListener(this, mouseListener);
		swtFacade.addMouseTrackListener(this, mouseListener);
	}

	/**
	 * Removes the paint listener from this {@code EmbeddedTitleBar}.
	 */
	protected void removeListeners() {
		if (mouseListener != null) {
			removeMouseListener(mouseListener);
			final SWTFacade swtFacade = SWTFacade.getDefault();
			swtFacade.removeMouseMoveListener(this, mouseListener);
			swtFacade.removeMouseTrackListener(this, mouseListener);
			mouseListener = null;
		}
		if (paintListener != null) {
			removePaintListener(paintListener);
			paintListener = null;
		}
	}

	/**
	 * Paints the title bar.<br>
	 * Configures and calls the renderer that paints the title bar.
	 * 
	 * @param e
	 *            an event containing information about the paint
	 */
	private void onPaint(final PaintEvent e) {

		final GC gc = e.gc;

		// title bar
		getLnfTitlebarRenderer().setActive(isWindowActive());
		getLnfTitlebarRenderer().setCloseable(isCloseable());
		getLnfTitlebarRenderer().setPressed(isPressed());
		getLnfTitlebarRenderer().setHover(isHover());
		getLnfTitlebarRenderer().setCloseButtonPressed(isCloseButtonPressed());
		getLnfTitlebarRenderer().setCloseButtonHover(isCloseButtonHover());
		getLnfTitlebarRenderer().setImage(getImage());
		getLnfTitlebarRenderer().setTitle(getTitle());
		getLnfTitlebarRenderer().setMarkers(getMarkers());
		final Point titlebarSize = getLnfTitlebarRenderer().computeSize(gc, getBounds().width, 0);
		final Rectangle titlebarBounds = new Rectangle(0, 0, titlebarSize.x, titlebarSize.y);
		getLnfTitlebarRenderer().setBounds(titlebarBounds);
		getLnfTitlebarRenderer().paint(gc, this);

	}

	/**
	 * Returns the renderer of the title bar.
	 * 
	 * @return renderer
	 */
	protected EmbeddedTitlebarRenderer getLnfTitlebarRenderer() {

		EmbeddedTitlebarRenderer renderer = (EmbeddedTitlebarRenderer) LnfManager.getLnf().getRenderer(
				LnfKeyConstants.SUB_MODULE_VIEW_TITLEBAR_RENDERER);
		if (renderer == null) {
			renderer = new EmbeddedTitlebarRenderer();
		}
		return renderer;

	}

	public void addEmbeddedTitleBarListener(final IEmbeddedTitleBarListener listener) {
		titleBarListeners.add(listener);
	}

	public void removeEmbeddedTitleBarListener(final IEmbeddedTitleBarListener listener) {
		titleBarListeners.remove(listener);
	}

	/**
	 * @see org.eclipse.swt.widgets.Control#getSize()
	 */
	@Override
	public Point getSize() {
		Point size = super.getSize();
		final GC gc = new GC(this);
		size = getLnfTitlebarRenderer().computeSize(gc, size.x, size.y);
		gc.dispose();
		return size;
	}

	/**
	 * @see org.eclipse.swt.widgets.Control#isFocusControl()
	 */
	@Override
	public boolean isFocusControl() {
		return false;
	}

	/**
	 * @return the windowActive
	 */
	public boolean isWindowActive() {
		return windowActive;
	}

	/**
	 * @param windowActive
	 *            the windowActive to set
	 */
	public void setWindowActive(final boolean active) {
		if (hasChanged(this.windowActive, active)) {
			this.windowActive = active;
			redraw();
		}
	}

	/**
	 * @return the pressed
	 */
	public boolean isPressed() {
		return pressed;
	}

	/**
	 * @param pressed
	 *            the pressed to set
	 */
	public void setPressed(final boolean pressed) {
		if (isDisposed()) {
			return;
		}
		if (hasChanged(this.pressed, pressed)) {
			this.pressed = pressed;
			redraw();
		}
	}

	/**
	 * @return the hover
	 */
	public boolean isHover() {
		return hover;
	}

	/**
	 * @param hover
	 *            the hover to set
	 */
	public void setHover(final boolean hover) {
		if (isDisposed()) {
			return;
		}
		if (hasChanged(this.hover, hover)) {
			this.hover = hover;
			redraw();
		}
	}

	/**
	 * @return the closeable
	 */
	public boolean isCloseable() {
		return closeable;
	}

	/**
	 * @param closeable
	 *            the closeable to set
	 */
	public void setCloseable(final boolean closeable) {
		if (hasChanged(this.closeable, closeable)) {
			this.closeable = closeable;
			redraw();
		}
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(final String title) {
		if (hasChanged(this.title, title)) {
			this.title = title;
			redraw();
		}
	}

	@Override
	public void setToolTipText(final String text) {
		toolTipText = text;
	}

	@Override
	public String getToolTipText() {
		return toolTipText;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(final Image image) {
		if (hasChanged(this.image, image)) {
			this.image = image;
			redraw();
		}
	}

	/**
	 * @return the image
	 */
	public Image getImage() {
		return image;
	}

	public void setMarkers(final Collection<? extends IMarker> markers) {
		this.markers = markers;
	}

	public Collection<? extends IMarker> getMarkers() {
		return markers;
	}

	public <T extends IMarker> Collection<T> getMarkersOfType(final Class<T> type) {
		return Markable.getMarkersOfType(getMarkers(), type);
	}

	protected boolean isOverClose(final Point point) {

		if (!isCloseable()) {
			return false;
		}

		final boolean inside = getLnfTitlebarRenderer().isInsideCloseButton(point);
		return inside;

	}

	public boolean isTextClipped() {

		final EmbeddedTitlebarRenderer titlebarRenderer = getLnfTitlebarRenderer();
		titlebarRenderer.setBounds(getBounds());
		final GC gc = new GC(this);
		final String clippedText = titlebarRenderer.getClippedText(gc, title);
		gc.dispose();
		if (StringUtils.isEmpty(title)) {
			return false;
		}

		return !title.equals(clippedText);

	}

	/**
	 * Compares the two given values.
	 * 
	 * @param oldValue
	 *            old value
	 * @param newValue
	 *            new value
	 * @return true, if value has changed; otherwise false
	 */
	private boolean hasChanged(final Object oldValue, final Object newValue) {
		return (oldValue == null && newValue != null) || (oldValue != null && !oldValue.equals(newValue));
	}

	public void setCloseButtonPressed(final boolean closeButtonPressed) {
		if (isDisposed()) {
			return;
		}
		if (hasChanged(this.closeButtonPressed, closeButtonPressed)) {
			this.closeButtonPressed = closeButtonPressed;
			redraw();
		}
	}

	public boolean isCloseButtonPressed() {
		return closeButtonPressed;
	}

	public void setCloseButtonHover(final boolean closeButtonHover) {
		if (isDisposed()) {
			return;
		}
		if (hasChanged(this.closeButtonHover, closeButtonHover) && !isDisposed()) {
			this.closeButtonHover = closeButtonHover;
			redraw();
		}
	}

	public boolean isCloseButtonHover() {
		return closeButtonHover;
	}

	@Override
	public void dispose() {
		removeListeners();
		super.dispose();
	}

	/**
	 * After any mouse operation a method of this listener is called. The item
	 * under the current mouse position is selected, pressed or "hovered".
	 */
	private class TitlebarMouseListener implements MouseListener, MouseTrackListener, MouseMoveListener {

		public void mouseUp(final MouseEvent e) {
			if (!isEnabled()) {
				return;
			}
			if (!shouldIgnore(e)) {
				final Point point = new Point(e.x, e.y);
				if (isOverClose(point)) {
					fireClosed(e);
				} else {
					fireActivated(e);
				}
				setPressed(false);
			}
			updateCloseButtonState(e);
		}

		public void mouseDown(final MouseEvent e) {
			if (!isEnabled()) {
				return;
			}
			if (!shouldIgnore(e)) {
				setPressed(true);
			}
			updateCloseButtonState(e);
		}

		public void mouseDoubleClick(final MouseEvent e) {
			// nothing to do
		}

		public void mouseEnter(final MouseEvent e) {
			if (!isEnabled()) {
				return;
			}
			setHover(true);
			updateCloseButtonState(e);
		}

		public void mouseExit(final MouseEvent e) {
			if (!isEnabled()) {
				return;
			}
			setHover(false);
			updateCloseButtonState(e);
		}

		public void mouseHover(final MouseEvent e) {
		}

		public void mouseMove(final MouseEvent e) {
			updateCloseButtonState(e);
		}

		/**
		 * Updates the (hover and pressed) state of the close button.
		 * 
		 * @param e
		 */
		private void updateCloseButtonState(final MouseEvent e) {

			final Point point = new Point(e.x, e.y);
			if (isOverClose(point)) {
				setCloseButtonHover(isHover());
				setCloseButtonPressed(isPressed());
			} else {
				setCloseButtonHover(false);
				setCloseButtonPressed(false);
			}

		}

		/**
		 * Ignore mouse events if the component is null, not enabled, or the
		 * event is not associated with the left mouse button.
		 */
		protected boolean shouldIgnore(final MouseEvent e) {
			return e.button != 1;
		}

		protected boolean isEnabled() {
			return getMarkersOfType(DisabledMarker.class).isEmpty();
		}

		/**
		 * Informs all listeners that the module is activated.
		 * 
		 * @param event
		 *            origin mouse event
		 */
		private void fireActivated(final MouseEvent event) {
			for (final IEmbeddedTitleBarListener listener : titleBarListeners.getListeners()) {
				listener.windowActivated(event);
			}
		}

		/**
		 * Informs all listeners that the close button was clicked.
		 * 
		 * @param event
		 *            origin mouse event
		 */
		private void fireClosed(final MouseEvent event) {
			for (final IEmbeddedTitleBarListener listener : titleBarListeners.getListeners()) {
				listener.windowClosed(event);
			}
		}

	}

}
