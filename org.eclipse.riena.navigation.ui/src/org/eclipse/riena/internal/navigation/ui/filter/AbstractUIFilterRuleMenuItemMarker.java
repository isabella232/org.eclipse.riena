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
package org.eclipse.riena.internal.navigation.ui.filter;

import org.eclipse.riena.core.marker.IMarker;

/**
 *
 */
public abstract class AbstractUIFilterRuleMenuItemMarker extends AbstractUIFilterRuleRidgetMarker {

	/**
	 * Creates a new instance of {@code UIFilterRuleMenuActionDisabledMarker}.
	 * 
	 * @param marker
	 */
	public AbstractUIFilterRuleMenuItemMarker(final IMarker marker) {
		super(null, marker);
	}

	/**
	 * Creates a new instance of {@code UIFilterRuleMenuItemDisabledMarker}.
	 * 
	 * @param id
	 *            ID
	 * @param marker
	 */
	public AbstractUIFilterRuleMenuItemMarker(final String id, final IMarker marker) {
		super(id, marker);
	}

	@Override
	protected RidgetMatcher createMatcher(final String id) {
		return new MenuItemRidgetMatcher(id);
	}

}
