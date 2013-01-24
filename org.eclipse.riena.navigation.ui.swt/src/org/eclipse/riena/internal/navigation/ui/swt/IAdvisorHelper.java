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
package org.eclipse.riena.internal.navigation.ui.swt;

import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import org.eclipse.riena.navigation.ui.swt.application.SwtApplication;

/**
 * Tweak the various advisors during application startup.
 */
public interface IAdvisorHelper {

	/**
	 * Creates an action bar advisor for a window.
	 * <p>
	 * The minimum implementation should return a new instance of
	 * {@link ActionBarAdvisor}.
	 * 
	 * @param configurer
	 *            the action bar configurer for a window; never null
	 * 
	 * @return an {@link ActionBarAdvisor} or subclass; never null
	 */
	ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer);

	/**
	 * Returns the default key binding scheme for this application.
	 * 
	 * @return the default key binding scheme
	 * 
	 * @see {@code org.eclipse.ui.bindings} extension point documentation
	 * @see SwtApplication#getKeyScheme()
	 */
	String getKeyScheme();
}
