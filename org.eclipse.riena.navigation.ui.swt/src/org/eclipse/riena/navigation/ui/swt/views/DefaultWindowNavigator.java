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

import org.eclipse.riena.ui.swt.utils.ShellHelper;

/**
 * This will center the shell on the current screen.
 * 
 * @since 4.0
 */
public class DefaultWindowNavigator implements IWindowNavigator {

	public void beforeOpen(final Shell shell) {
		ShellHelper.center(shell);
	}

	public void beforeClose(final Shell shell) {
		// nothing by default		
	}

}
