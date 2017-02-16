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
package org.eclipse.riena.ui.ridgets;

import org.eclipse.riena.ui.core.resource.IconSize;

/**
 * A notification that turns up on top of the SubModuleView. No user interaction is possible and it closes after a few seconds.
 * <p>
 * It is possible to set a message and an icon.
 * 
 * @since 2.0
 */
public interface IInfoFlyoutRidget extends IRidget {

	int PROPERTY_FYLOUT_FINISHED = 0;

	void addInfo(InfoFlyoutData info);

	/**
	 * The info displayed in the {@link InfoFlyout}.
	 */
	public class InfoFlyoutData {
		private final String message;
		private final String icon;
		private IconSize iconSize = IconSize.A16;

		public InfoFlyoutData(final String icon, final String message) {
			this.icon = icon;
			this.message = message;
		}

		/**
		 * @since 6.2
		 */
		public InfoFlyoutData(final String icon, final IconSize iconSize, final String message) {
			this.icon = icon;
			this.iconSize = iconSize;
			this.message = message;
		}

		public String getIcon() {
			return icon;
		}

		/**
		 * @since 6.2
		 */
		public IconSize getIconSize() {
			return iconSize;
		}

		public String getMessage() {
			return message;
		}
	}

}
