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
package org.eclipse.riena.internal.navigation.ui.swt.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import org.eclipse.riena.internal.ui.swt.facades.WorkbenchFacade;
import org.eclipse.riena.navigation.ui.swt.nls.Messages;
import org.eclipse.riena.ui.swt.RienaMessageDialog;

/**
 * Terminates the workbench, after asking for user confirmation.
 */
public class ExitApplication extends AbstractHandler {

	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final Shell shell = HandlerUtil.getActiveShell(event);
		final String message = String.format(Messages.ExitApplication_exit, getName());
		final boolean isConfirmed = RienaMessageDialog.openConfirm(shell, Messages.ExitApplication_confirmExit, message);
		if (isConfirmed) {
			WorkbenchFacade.getInstance().closeWorkbench();
		}
		return null;
	}

	// helping methods
	//////////////////

	private String getName() {
		final IProduct product = Platform.getProduct();
		return product != null ? product.getName() : Messages.ExitApplication_application;
	}

}
