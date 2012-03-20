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
package org.eclipse.riena.internal.ui.swt.utils;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * A collection of utility methods for RCP.
 */
public final class RcpUtilities {

	private static Shell myShell;

	/**
	 * This class contains only static methods. So it is not necessary to create
	 * an instance.
	 */
	private RcpUtilities() {
		throw new Error("RcpUtilities is just a container for static methods"); //$NON-NLS-1$
	}

	/**
	 * Returns the shell of the active workbench.
	 * 
	 * @return shell of {@code null} if no active workbench exists.
	 */
	public static Shell getWorkbenchShell() {
		if (myShell != null) {
			return myShell;
		}

		if (PlatformUI.isWorkbenchRunning() && PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null) {
			return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		} else {
			return null;
		}

	}

	/**
	 * Returns the activate Display
	 * 
	 * @return instance of Display or null if there is no display or no active
	 *         Workbench
	 */
	public static Display getDisplay() {
		if (myShell != null) {
			return myShell.getDisplay();
		}
		if (PlatformUI.isWorkbenchRunning() && PlatformUI.getWorkbench().getDisplay() != null) {
			return PlatformUI.getWorkbench().getDisplay();
		} else {
			return null;
		}
	}

	/**
	 * Returns true if there is an active Display
	 * 
	 * @return true if Display is available
	 */
	public static boolean hasDisplay() {
		if (myShell != null) {
			return myShell.getDisplay() != null;
		}
		return PlatformUI.isWorkbenchRunning() && PlatformUI.getWorkbench().getDisplay() != null;
	}

	public static void setShell(final Shell shell) {
		myShell = shell;
	}

}
