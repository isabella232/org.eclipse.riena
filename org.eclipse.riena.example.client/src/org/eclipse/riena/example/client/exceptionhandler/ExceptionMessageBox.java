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
package org.eclipse.riena.example.client.exceptionhandler;

import org.eclipse.equinox.log.Logger;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.core.exception.IExceptionHandler;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget.MessageBoxOption;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget.Type;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.MessageBox;

public class ExceptionMessageBox implements IExceptionHandler {

	public IExceptionHandler.Action handleException(final Throwable t, final String msg, final Logger logger) {
		final MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell());
		final IMessageBoxRidget messageBoxRidget = (IMessageBoxRidget) SwtRidgetFactory.createRidget(messageBox);

		messageBoxRidget.setTitle("Exception at Runtime"); //$NON-NLS-1$
		messageBoxRidget.setType(Type.ERROR);
		if (t != null) {
			messageBoxRidget.setText(t.getMessage());
		}
		final IMessageBoxRidget.MessageBoxOption ok = new IMessageBoxRidget.MessageBoxOption("OK"); //$NON-NLS-1$
		final IMessageBoxRidget.MessageBoxOption ignore = new IMessageBoxRidget.MessageBoxOption("Ignore"); //$NON-NLS-1$
		final IMessageBoxRidget.MessageBoxOption printstack = new IMessageBoxRidget.MessageBoxOption(
				"Print stacktrace and OK"); //$NON-NLS-1$
		messageBoxRidget.setOptions(new IMessageBoxRidget.MessageBoxOption[] { ok, ignore, printstack });
		final MessageBoxOption show = messageBoxRidget.show();
		if (t != null && show.equals(printstack)) {
			t.printStackTrace();
		}
		if (show.equals(ignore)) {
			return IExceptionHandler.Action.NOT_HANDLED;
		}
		return IExceptionHandler.Action.OK;
	}

	public IExceptionHandler.Action handleUncaught(final Throwable t, final String msg, final Logger logger) {
		return IExceptionHandler.Action.NOT_HANDLED;
	}
}
