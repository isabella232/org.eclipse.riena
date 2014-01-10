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
package org.eclipse.riena.ui.filter;

/**
 * A filter rule can modify an UI element (e.g. the marker of a ridget or the
 * visibility of a navigation node).
 */
public interface IUIFilterRule {

	/**
	 * Returns whether the given object(s) matches the conditions of this rule.
	 * 
	 * @param args
	 *            object(s) to check
	 * @return {@code true} if the given object matches; otherwise {@code false}
	 */
	boolean matches(Object... args);

	/**
	 * Modifies the given object.
	 * 
	 * @param object
	 *            object to modify
	 */
	void apply(Object object);

	/**
	 * Removes this modification from the given object.
	 * 
	 * @param object
	 *            object whose modification will be removed
	 */
	void remove(Object object);

}
