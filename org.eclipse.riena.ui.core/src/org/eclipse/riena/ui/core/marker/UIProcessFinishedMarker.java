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
package org.eclipse.riena.ui.core.marker;

import org.eclipse.riena.core.marker.AbstractMarker;

/**
 * This marker indicates a finished UI Process.
 */
public class UIProcessFinishedMarker extends AbstractMarker implements IIconizableMarker {

	public static final String MARKER_KEY = "UIProcessFinishedMarker"; //$NON-NLS-1$
	private static final String ATTRIBUTE_FINISHED = "uiprocess.finished"; //$NON-NLS-1$
	private static final int COUNTER_MINIMUM = 0;
	private static final int COUNTER_INACTIVE = -1;

	private int counter;
	private int counterMaximum;
	private boolean on;

	/**
	 * Creates a new instance of {@link UIProcessFinishedMarker} and initializes
	 * the maximum number of flashes with 10.
	 */
	public UIProcessFinishedMarker() {
		this(10);
	}

	/**
	 * Creates a new instance of {@link UIProcessFinishedMarker} and initializes
	 * the maximum number of flashes with given value.
	 * 
	 * @param counterMaximum
	 *            maximum number of flashes
	 */
	public UIProcessFinishedMarker(final int counterMaximum) {
		super();
		setCounterMaximum(counterMaximum);
		counter = COUNTER_INACTIVE;
		on = true;
	}

	/**
	 * @return the finished
	 */
	public boolean isFinished() {
		return (Boolean) getAttribute(ATTRIBUTE_FINISHED);
	}

	/**
	 * @param finished
	 *            the finished to set
	 */
	public void setFinished(final boolean finished) {
		setAttribute(ATTRIBUTE_FINISHED, finished);
	}

	/**
	 * @see org.eclipse.riena.ui.core.marker.IIconizableMarker#getIconConfigurationKey()
	 */
	public String getIconConfigurationKey() {
		return MARKER_KEY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.core.marker.IIconizableMarker#getPositionOfMarker()
	 */
	public MarkerPosition getPositionOfMarker() {
		return MarkerPosition.TOP_LEFT;
	}

	/**
	 * Turns the marker on or off (shows or hides the marker).
	 * 
	 * @param on
	 *            true to turn on the marker; false to turn off the marker
	 */
	public void setOn(final boolean on) {
		this.on = on;
	}

	/**
	 * Returns whether the marker is on or off (marker is shown or hidden).
	 * 
	 * @return true if marker is shown; otherwise false
	 */
	public boolean isOn() {
		if (counter >= getCounterMaximum()) {
			setOn(true);
		}
		return on;
	}

	/**
	 * Returns if the marker is flashing at the moment
	 * 
	 * @return true, if the marker is flashing; otherwise false.
	 */
	public boolean isFlashing() {
		return (counter >= COUNTER_MINIMUM) && (counter < getCounterMaximum());
	}

	/**
	 * Sets the maximum number of flashes.
	 * 
	 * @param counterMaximum
	 *            maximum number of flashes
	 */
	public void setCounterMaximum(final int counterMaximum) {
		this.counterMaximum = counterMaximum;
	}

	/**
	 * Returns the maximum number of flashes.
	 * 
	 * @return maximum number of flashes
	 */
	public int getCounterMaximum() {
		return counterMaximum;
	}

	/**
	 * Increases the number of flashes.
	 */
	public void increase() {
		if (isFlashing()) {
			counter++;
		}
	}

	/**
	 * Activates the flasher.
	 */
	public void activate() {
		counter = COUNTER_MINIMUM;
	}

	/**
	 * Returns is the flasher was activated.
	 * 
	 * @return true if flasher was activated; otherwise false.
	 */
	public boolean isActivated() {
		return counter != COUNTER_INACTIVE;
	}

	/**
	 * Returns the time of displaying the UI presentation (e.g. icon) of the
	 * marker.
	 * 
	 * @return time
	 */
	public int getTimeOn() {
		return 400;
	}

	/**
	 * Returns the time of displaying the UI presentation (e.g. icon) of the
	 * marker.
	 * 
	 * @return time
	 */
	public int getTimeOff() {
		return 400;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@code true}
	 * 
	 * @since 1.2
	 */
	public boolean isVisible() {
		return true;
	}

}
