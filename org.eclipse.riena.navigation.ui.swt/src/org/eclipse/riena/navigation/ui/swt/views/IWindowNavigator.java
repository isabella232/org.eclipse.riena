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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.swt.widgets.Shell;

/**
 * Implementations can implement a custom strategy for the initial size and
 * position of the main window.
 * 
 * @since 4.0
 */
public interface IWindowNavigator {

	/**
	 * This is called after the initial setup of the main window, but before the
	 * main window becomes visible.
	 * 
	 * @param shell
	 */
	void beforeOpen(final Shell shell);

	/**
	 * This is called before the main window is closed
	 * 
	 * @param shell
	 */
	void beforeClose(Shell shell);

}
