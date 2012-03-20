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
package org.eclipse.riena.internal.navigation.ui.filter;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.core.util.StringMatcher;
import org.eclipse.riena.ui.filter.IUIFilterRuleMarkerRidget;
import org.eclipse.riena.ui.filter.impl.AbstractUIFilterRuleMarker;
import org.eclipse.riena.ui.ridgets.IBasicMarkableRidget;

/**
 * Filter rule to provide a marker for a ridget.
 */
public abstract class AbstractUIFilterRuleRidgetMarker extends AbstractUIFilterRuleMarker implements
		IUIFilterRuleMarkerRidget {

	private final Map<IBasicMarkableRidget, IMarker> markerMap;
	protected RidgetMatcher matcher;

	/**
	 * Creates a new instance of {@code AbstractUIFilterRuleRidgetMarker}.
	 * 
	 * @param idPattern
	 *            pattern ({@link StringMatcher}) for ridget IDs
	 * @param marker
	 */
	public AbstractUIFilterRuleRidgetMarker(final String idPattern, final IMarker marker) {
		super(marker);
		matcher = createMatcher(idPattern);
		markerMap = new HashMap<IBasicMarkableRidget, IMarker>();
	}

	/**
	 * This method compares the ID of this rule and the given ID of a ridget.
	 * 
	 * @see org.eclipse.riena.ui.internal.IUIFilterRule.IUIFilterAttribute#matches(java.lang.Object)
	 */

	public boolean matches(final Object... args) {
		if ((args == null) || (args.length <= 0)) {
			return false;
		}
		return matcher.matches(args);
	}

	/**
	 * Adds the marker of this rule to the given object (if the object is an
	 * markable ridget).
	 * 
	 * @see org.eclipse.riena.ui.internal.IUIFilterRule.IUIFilterAttribute#apply(java.lang.Object)
	 */
	public void apply(final Object object) {
		if (object instanceof IBasicMarkableRidget) {
			final IBasicMarkableRidget markableRidget = (IBasicMarkableRidget) object;
			final IMarker marker = getMarker();
			markableRidget.addMarker(marker);
			markerMap.put(markableRidget, marker);
		}
	}

	/**
	 * Removes the marker of this rule from the given object (if the object is
	 * an markable ridget).
	 * 
	 * @see org.eclipse.riena.ui.internal.IUIFilterRule.IUIFilterAttribute#remove(java.lang.Object)
	 */
	public void remove(final Object object) {
		if (object instanceof IBasicMarkableRidget) {
			final IBasicMarkableRidget markableRidget = (IBasicMarkableRidget) object;
			final IMarker marker = markerMap.get(markableRidget);
			markableRidget.removeMarker(marker);
			markerMap.remove(markableRidget);
		}
	}

	public void setId(final String idPattern) {
		matcher.setId(idPattern);
	}

	protected RidgetMatcher createMatcher(final String idPattern) {
		return new RidgetMatcher(idPattern);
	}

}
