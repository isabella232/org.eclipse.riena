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
package org.eclipse.riena.ui.ridgets.listener;

/**
 * Event sent to {@link ILocationListener}s when the user activates a hyperlink.
 * 
 * @since 3.0
 */
public final class LocationEvent {

	private final String location;
	private final boolean allow;
	private final boolean topFrame;

	/**
	 * Creates a new LocationEvent instance.
	 * 
	 * @param location
	 *            the new location (URL)
	 * @param allow
	 *            true if the location should be opened, false if the location
	 *            should not be opened
	 * @param topFrame
	 *            true if the location is to be opened in the top frame. Most
	 *            commonly the value is {@code false} (i.e. when no frames are
	 *            used).
	 */
	public LocationEvent(final String location, final boolean allow, final boolean topFrame) {
		this.location = location;
		this.allow = allow;
		this.topFrame = topFrame;
	}

	/**
	 * Returns location value (an URL).
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * A flag indicating that it is permitted to open (visit) the hyperlink (
	 * {@code true}) or that it is not permitted to opend the hyperlink (
	 * {@code false}).
	 * <p>
	 * This can be used to check if the change has already been vetoed.
	 * 
	 * @return a boolean value
	 */
	public boolean isAllowed() {
		return allow;
	}

	/**
	 * Returns true if the location is to be opened in the top frame. Most
	 * commonly the value is {@code false} (i.e. when no frames are used).
	 */
	public boolean isTopFrame() {
		return topFrame;
	}

	@Override
	public String toString() {
		return String.format("LocationEvent [topFrame=%b, location=%s]", topFrame, location); //$NON-NLS-1$
	}

}
