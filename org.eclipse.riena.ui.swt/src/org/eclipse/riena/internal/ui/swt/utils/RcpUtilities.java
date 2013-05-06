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
package org.eclipse.riena.internal.ui.swt.utils;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.internal.ui.swt.facades.WorkbenchFacade;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * A collection of utility methods for RCP.
 * <p>
 * TODO investigate if this class can be removed. Can {@link WorkbenchFacade} be used instead?
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

		return WorkbenchFacade.getInstance().getActiveWindowShell();
	}

	/**
	 * Returns the activate Display
	 * 
	 * @return instance of Display or null if there is no display or no active
	 *         Workbench
	 */
	public static Display getDisplay() {
		if (!SwtUtilities.isDisposed(myShell)) {
			return myShell.getDisplay();
		}

		return WorkbenchFacade.getInstance().getWorkbenchDisplay();
	}

	/**
	 * Returns true if there is an active Display
	 * 
	 * @return true if Display is available
	 */
	public static boolean hasDisplay() {
		if (!SwtUtilities.isDisposed(myShell)) {
			return myShell.getDisplay() != null;
		}
		return WorkbenchFacade.getInstance().getWorkbenchDisplay() != null;
	}

	public static void setShell(final Shell shell) {
		myShell = shell;
	}

}
