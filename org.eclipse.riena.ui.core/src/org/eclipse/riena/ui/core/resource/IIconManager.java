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
package org.eclipse.riena.ui.core.resource;

/**
 * Describes how the icons of the application are maintained. Icons are
 * identified by a <code>String</code>, an icon ID that should be created using
 * the icon manager. The information required to get the actual icon file is
 * passed to the icon manager when creating the icon ID and will be stored for
 * later retrieval.
 * 
 * The <code>String</code> ID is used throughout the application rather than an
 * icon object for downward compatibility reasons.
 */
public interface IIconManager {

	/**
	 * Stores an icon with the specified name and size and returns an ID to
	 * access the icons data. The icons state is assumed to be 'normal'.
	 * 
	 * @see IconState#NORMAL
	 * 
	 * @param name
	 *            The name of the icon.
	 * @param size
	 *            The icon size.
	 * @return The ID of the icon.
	 */
	String getIconID(String name, IconSize size);

	/**
	 * Stores an icon with the specified name, size and state and returns an ID
	 * to access the icons data.
	 * 
	 * @param name
	 *            The name of the icon.
	 * @param size
	 *            The icon size.
	 * @param state
	 *            The icons state.
	 * @return The ID of the icon.
	 */
	String getIconID(String name, IconSize size, IconState state);

	/**
	 * Returns the name of the icon if the specified ID. The ID must be obtained
	 * from the icon manager.
	 * 
	 * @see #getIconID(String, IconSize)
	 * 
	 * @param iconID
	 *            The ID of the icon.
	 * @return The name of the icon if an icon with the specified ID is stored,
	 *         <code>null</code> otherwise.
	 */
	String getName(String iconID);

	/**
	 * Returns the size of the icon with the specified ID. The ID must be
	 * obtained from the icon manager.
	 * 
	 * @see #getIconID(String, IconSize)
	 * @see IconSize
	 * 
	 * @param iconID
	 *            The ID of the icon.
	 * @return The size of the icon if an icon with the specified ID is stored,
	 *         <code>null</code> otherwise.
	 */
	IconSize getSize(String iconID);

	/**
	 * Returns the state of the icon with the specified ID. The ID must be
	 * obtained from the icon manager.
	 * 
	 * @see #getIconID(String, IconSize)
	 * @see IconState
	 * 
	 * @param iconID
	 *            The ID of the icon.
	 * @return The state of the icon if an icon with the specified ID is stored,
	 *         <code>null</code> otherwise.
	 */
	IconState getState(String iconID);

	/**
	 * Return, if <code>iconID</code> has a file extension or not.
	 * 
	 * @param iconID
	 *            The ID of the icon.
	 * @return true, if <code>iconID</code> has a file extension, else false.
	 */
	boolean hasExtension(String iconID);

}
