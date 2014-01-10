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
package org.eclipse.riena.navigation.ui.login;

/**
 * The interface which the login dialog view should implement.
 */
public interface ILoginDialogView {

	/**
	 * Build and open the dialog.
	 */
	void build();

	/**
	 * Returns the result of the login operation. The following conventions have
	 * to be considered:
	 * <ol>
	 * <li>IApplication.EXIT_OK indicates that the login was successful,</li>
	 * <li>IApplication.EXIT_RESTART indicates that the login was not successful
	 * (i.e. wrong authentication data was specified),</li>
	 * <li>In case of some other result the login operation was aborted.</li>
	 * </ol>
	 * 
	 * @return the result of the login.
	 */
	int getResult();
}
