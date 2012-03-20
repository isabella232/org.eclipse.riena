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
package org.eclipse.riena.ui.filter;

import org.eclipse.riena.core.marker.IMarker;

/**
 * This filter rule adds a marker to an UI element (e.g. ridget or navigation
 * node).
 */
public interface IUIFilterRuleMarker extends IUIFilterRule {

	/**
	 * Returns the marker of this IUIFilterRuleMarker.
	 * 
	 * @return marker
	 */
	IMarker getMarker();

}
