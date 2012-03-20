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
package org.eclipse.riena.ui.ridgets;

/**
 * Interface that reflects the extended possibilities of a
 * {@link StatusMeterRidget} over a normal {@link ITraverseRidget}.
 * 
 * @since 3.0
 */
public interface IStatusMeterRidget extends ITraverseRidget {

	/**
	 * Set the border color of the StatusMeter.
	 * 
	 * @param color
	 */
	void setBorderColor(Object color);

	/**
	 * Set the background color of the StatusMeter.
	 * 
	 * @param color
	 */
	void setBackgroundColor(Object color);

	/**
	 * Set the gradient start color of the bar in the StatusMeter.
	 * 
	 * @param color
	 */
	void setGradientStartColor(Object color);

	/**
	 * Set the gradient end color of the bar in the StatusMeter.
	 * 
	 * @param color
	 */
	void setGradientEndColor(Object color);
}
