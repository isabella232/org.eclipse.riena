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
package org.eclipse.riena.example.client.views;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.navigation.ui.swt.views.IWindowNavigator;
import org.eclipse.riena.ui.swt.utils.ShellHelper;

public class ExampleWindowNavigator implements IWindowNavigator {

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.IWindowNavigator#beforeOpen(org.eclipse.swt.widgets.Shell)
	 */
	public void beforeOpen(final Shell shell) {
		final Monitor monitor = ShellHelper.getClosestMonitor(shell);
		final Rectangle screenBound = monitor.getBounds();
		final int leftMargin = screenBound.x + (screenBound.width / 4);
		final int topMargin = screenBound.y;
		shell.setLocation(leftMargin, topMargin);
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.IWindowNavigator#beforeClose(org.eclipse.swt.widgets.Shell)
	 */
	public void beforeClose(final Shell shell) {
		// nothing by default		
	}

}
