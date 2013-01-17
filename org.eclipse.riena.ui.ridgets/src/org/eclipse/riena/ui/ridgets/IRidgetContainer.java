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
package org.eclipse.riena.ui.ridgets;

import java.util.Collection;

import org.eclipse.riena.ui.core.marker.IMessageMarker;

/**
 * A container for ridgets.
 */
public interface IRidgetContainer {
	/**
	 * Adds the ridget with id to the ridgets of this ridget container.
	 * <p>
	 * If the given (id, ridget) pair is already in the container, the previous value will be replaced.
	 * 
	 * @param id
	 *            the ridget id.
	 * @param ridget
	 *            the ridget.
	 */
	void addRidget(String id, IRidget ridget);

	/**
	 * @since 5.0
	 */
	boolean removeRidget(String id);

	/**
	 * Returns the ridget with the given id from this ridget container.
	 * 
	 * @param id
	 *            the ridget id.
	 * @return the ridget instance or null, if no ridget with this id was found.
	 * 
	 * @since 3.0
	 */
	<R extends IRidget> R getRidget(String id);

	/**
	 * Returns the ridget with the given id from this container. <br>
	 * If no ridget with that id can be found (e.g. when running controller tests), this method creates and returns a ridget of the given ridgetClazz and adds
	 * it to the container.
	 * 
	 * @param <R>
	 *            the type of class
	 * @param ridgetClazz
	 *            the class of the ridget
	 * @param id
	 *            the id of the ridget
	 * @return the ridget instance
	 * 
	 * @since 2.0
	 */
	<R extends IRidget> R getRidget(Class<R> ridgetClazz, String id);

	/**
	 * Returns the ridgets of this ridget container.
	 * 
	 * @return the ridgets.
	 */
	Collection<? extends IRidget> getRidgets();

	/**
	 * This method is called after all ridgets are injected.
	 */
	void configureRidgets();

	/**
	 * Returns whether the Rdigets of this container are configured or not.
	 * 
	 * @return {@code true} all Ridgets are configured; otherwise {@code false}
	 * @since 4.0
	 */
	boolean isConfigured();

	/**
	 * Sets whether the Rdigets of this container are configured or not.
	 * 
	 * @param configured
	 *            {@code true} all Ridgets are configured; otherwise {@code false}
	 * @since 4.0
	 */
	void setConfigured(boolean configured);

	/**
	 * If a non-<code>null</code> {@link IStatuslineRidget} is set, than all ridgets with {@link IMessageMarker} within this container will automatically
	 * display their messages in the given status line. Setting <code>null</code> will disable this functionality.
	 * 
	 * @param statuslineToShowMarkerMessages
	 *            a {@link IStatuslineRidget} to bind the ridgets to or <code>null</code> to unbind and disable automatic status line binding
	 * @since 5.0
	 */
	void setStatuslineToShowMarkerMessages(IStatuslineRidget statuslineToShowMarkerMessages);

}
