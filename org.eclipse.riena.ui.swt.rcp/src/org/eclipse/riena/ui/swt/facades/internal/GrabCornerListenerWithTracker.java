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
package org.eclipse.riena.ui.swt.facades.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tracker;

import org.eclipse.riena.ui.swt.GrabCorner;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Mouse listener to be used together with a {@link GrabCorner}.
 * <p>
 * This implementation relies on a SWT {@link Tracker} for resize operations.
 */
public final class GrabCornerListenerWithTracker extends MouseAdapter implements MouseTrackListener {

	private final Control control;

	private Cursor resizeCursor;
	private Cursor defaultCursor;

	public GrabCornerListenerWithTracker(final Control control) {
		control.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(final DisposeEvent e) {
				SwtUtilities.dispose(resizeCursor);
				SwtUtilities.dispose(defaultCursor);
			}
		});
		control.addMouseListener(this);
		control.addMouseTrackListener(this);
		this.control = control;
	}

	@Override
	public void mouseDown(final MouseEvent e) {
		final Tracker tracker = createTracker();
		try {
			final boolean doResize = tracker.open();
			if (doResize) {
				handleResize(tracker);
			}
		} finally {
			SwtUtilities.dispose(tracker);
		}
	}

	public void mouseEnter(final MouseEvent e) {
		showResizeCursor();
	}

	public void mouseExit(final MouseEvent e) {
		showDefaultCursor();
	}

	public void mouseHover(final MouseEvent e) {
		// unused
	}

	// helping methods
	// ////////////////

	/**
	 * Sets the resize cursor.
	 */
	private void showResizeCursor() {
		if (resizeCursor == null) {
			resizeCursor = new Cursor(control.getDisplay(), SWT.CURSOR_SIZENWSE);
		}
		control.setCursor(resizeCursor);
	}

	/**
	 * Sets the default cursor.
	 */
	private void showDefaultCursor() {
		if (defaultCursor == null) {
			defaultCursor = new Cursor(control.getDisplay(), SWT.CURSOR_ARROW);
		}
		control.setCursor(defaultCursor);
	}

	/**
	 * Create a tracker rectangle
	 */
	private Tracker createTracker() {
		final Shell shell = control.getShell();
		final Display display = shell.getDisplay();
		final Tracker tracker = new Tracker(display, SWT.DOWN | SWT.RIGHT | SWT.RESIZE);
		tracker.addControlListener(new TrackerListener());
		final Rectangle boundsDsp = display.map(shell, null, shell.getClientArea());
		tracker.setRectangles(new Rectangle[] { boundsDsp });
		tracker.setStippled(true);
		return tracker;
	}

	/**
	 * Resize the shell with the coordinates from the tracker
	 */
	private void handleResize(final Tracker tracker) {
		final Rectangle bounds = getTrackerBounds(tracker);
		if (bounds != null) {
			control.getShell().setBounds(bounds);
			control.getShell().redraw(bounds.x, bounds.y, bounds.width, bounds.height, true);
		}
	}

	/**
	 * Returns the bounds of the given tracker.<br>
	 * <i>The tracker can have different bounds. This method returns the bounds
	 * of the first rectangle.</i>
	 * 
	 * @param tracker
	 * @return bounds; {@code null} if the tracker has no bounds.
	 */
	private Rectangle getTrackerBounds(final Tracker tracker) {
		final Rectangle[] rectangles = tracker.getRectangles();
		if (rectangles.length > 0) {
			return rectangles[0];
		} else {
			return null;
		}

	}

	/**
	 * If the size of the tracker is less than the minimum size of the shell,
	 * the tracker will be set the minimum shell bounds.
	 * 
	 * @param tracker
	 */
	private void setMinimumBounds(final Tracker tracker) {

		final Rectangle bounds = getTrackerBounds(tracker);
		final Point miniSize = control.getShell().getMinimumSize();
		boolean setTrackerBounds = false;
		if (bounds.width < miniSize.x) {
			setTrackerBounds = true;
			bounds.width = miniSize.x;
		}
		if (bounds.height < miniSize.y) {
			setTrackerBounds = true;
			bounds.height = miniSize.y;
		}
		if (setTrackerBounds) {
			tracker.setRectangles(new Rectangle[] { bounds });
		}

	}

	/**
	 *
	 */
	private class TrackerListener implements ControlListener {

		/**
		 * @see org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt
		 *      .events.ControlEvent)
		 */
		public void controlMoved(final ControlEvent e) {
			// nothing to do
		}

		/**
		 * @see org.eclipse.swt.events.ControlListener#controlResized(org.eclipse
		 *      .swt.events.ControlEvent)
		 */
		public void controlResized(final ControlEvent e) {

			if (e.widget instanceof Tracker) {
				final Tracker tracker = (Tracker) e.widget;
				setMinimumBounds(tracker);
			}

		}

	}

}
