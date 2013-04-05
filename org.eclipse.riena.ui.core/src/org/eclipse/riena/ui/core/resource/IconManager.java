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
package org.eclipse.riena.ui.core.resource;

import java.util.HashMap;
import java.util.Map;

/**
 * Maintains the icons used by the application.
 * 
 * @since 2.0
 */
public class IconManager implements IIconManager {

	private static final char EXTENSION_SEPARATOR = '.';
	private final Map<String, Icon> icons;

	/**
	 * Constructor.
	 */
	public IconManager() {
		icons = new HashMap<String, Icon>();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getIconID(final String name, final IconSize size) {
		return getIconID(name, size, IconState.NORMAL);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getIconID(final String name, final IconSize size, final IconState state) {

		if (name == null) {
			return null;
		}

		final Icon icon = createIcon(name, size, state);
		final String iconID = icon.getID();

		icons.put(iconID, icon);

		return iconID;
	}

	protected Icon createIcon(final String name, final IconSize size, final IconState state) {
		return new Icon(name, size, state);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName(final String iconID) {

		final Icon icon = icons.get(iconID);

		if (icon != null) {
			return icon.getName();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public IconSize getSize(final String iconID) {

		final Icon icon = icons.get(iconID);

		if (icon != null) {
			return icon.getSize();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public IconState getState(final String iconID) {

		final Icon icon = icons.get(iconID);

		if (icon != null) {
			return icon.getState();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasExtension(final String iconID) {
		return iconID.indexOf(EXTENSION_SEPARATOR) > 0;
	}

	/**
	 * This class stores all the information of an icon that are necessary to
	 * create the file name.
	 */
	public class Icon {

		private final String name;
		private final IconSize size;
		private final IconState state;

		/**
		 * Creates an icon.
		 * 
		 * @param name
		 *            The name of the icon.
		 * @param size
		 *            The icon size.
		 * @param state
		 *            The icon state.
		 */
		public Icon(final String name, final IconSize size, final IconState state) {
			this.name = name;
			this.size = size;
			this.state = state;
		}

		/**
		 * Returns the ID of the icon.
		 * 
		 * @return ID
		 */
		public String getID() {
			String id = name;
			if (state != null) {
				id += state.getDefaultMapping();
			}
			if (size != null) {
				id += size.getDefaultMapping();
			}
			return id;
		}

		/**
		 * Return the name of the icon
		 * 
		 * @return icon name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Returns the size of the icon.
		 * 
		 * @return icon size
		 */
		public IconSize getSize() {
			return size;
		}

		/**
		 * Returns the state of the icon.
		 * 
		 * @return icon state
		 */
		public IconState getState() {
			return state;
		}
	}

}
