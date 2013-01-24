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
package org.eclipse.riena.navigation.ui.controllers;

import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.ui.filter.IUIFilterRule;

/**
 * This closures executes a method of {@link IUIFilterRule}.
 */
public interface IUIFilterRuleClosure {

	/**
	 * Executes a method of {@link IUIFilterRule}.
	 * 
	 * @param node
	 *            navigation node to which the given object ({@code obj})
	 *            belongs
	 * @param attr
	 *            filter rule
	 * @param obj
	 *            object that is given as a parameter to the method
	 */
	void execute(INavigationNode<?> node, IUIFilterRule attr, Object obj);

}
