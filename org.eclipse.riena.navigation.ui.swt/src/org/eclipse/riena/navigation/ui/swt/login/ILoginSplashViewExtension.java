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
package org.eclipse.riena.navigation.ui.swt.login;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;

/**
 * The definition for the login splash dialog view (see also
 * <code>ILoginSplashView</code>).
 */
@ExtensionInterface(id = "loginSplashViewDefinition")
public interface ILoginSplashViewExtension {

	ILoginSplashView createViewClass();

	/**
	 * Return the duration of non activity in the application, after which the
	 * login dialog is presented to the user again for a duration greater than
	 * 0. For a duration equal or less than 0 the login timer is not used at
	 * all.
	 * 
	 * @return the duration.
	 */
	int getNonActivityDuration();
}
