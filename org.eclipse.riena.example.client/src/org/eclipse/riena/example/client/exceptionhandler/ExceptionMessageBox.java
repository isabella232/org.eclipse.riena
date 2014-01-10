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
package org.eclipse.riena.example.client.exceptionhandler;

import org.eclipse.equinox.log.Logger;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.core.exception.IExceptionHandler;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget.MessageBoxOption;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget.Type;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.MessageBox;
import org.eclipse.riena.ui.swt.uiprocess.SwtUISynchronizer;

/**
 * {@link IExceptionHandler} implementation showing a swt message box. Note that instances of {@link IExceptionHandler} are called on the {@link Thread} causing
 * the exception to be handled. For that reason {@link SwtUISynchronizer} is used to serialize execution of the message box code to the swt user interface
 * {@link Thread}.
 */
public class ExceptionMessageBox implements IExceptionHandler {

	private final static SwtUISynchronizer uiSynchronizer = new SwtUISynchronizer();

	public IExceptionHandler.Action handleException(final Throwable t, final String msg, final Logger logger) {
		final Wrapper resultWrapper = new Wrapper();
		// As we have no control over the calling thread we have to ensure thread safety here. Note: This method will block until the message box is applied/closed.
		uiSynchronizer.syncExec(new Runnable() {

			public void run() {
				resultWrapper.action = showMessageBox(t);

			}
		});
		return resultWrapper.action;
	}

	// Wrapper for the result of the MessageBox
	static private class Wrapper {
		IExceptionHandler.Action action;
	}

	private IExceptionHandler.Action showMessageBox(final Throwable t) {
		final Display display = uiSynchronizer.getDisplay();
		final MessageBox messageBox = new MessageBox(display.getActiveShell());
		final IMessageBoxRidget messageBoxRidget = (IMessageBoxRidget) SwtRidgetFactory.createRidget(messageBox);

		messageBoxRidget.setTitle("Exception at Runtime"); //$NON-NLS-1$
		messageBoxRidget.setType(Type.ERROR);
		if (t != null) {
			// Message is not always filled. If message is null take the value of #toString()
			messageBoxRidget.setText(t.getMessage() != null ? t.getMessage() : t.toString());
		}
		final IMessageBoxRidget.MessageBoxOption ok = new IMessageBoxRidget.MessageBoxOption("OK"); //$NON-NLS-1$
		final IMessageBoxRidget.MessageBoxOption ignore = new IMessageBoxRidget.MessageBoxOption("Ignore"); //$NON-NLS-1$
		final IMessageBoxRidget.MessageBoxOption printstack = new IMessageBoxRidget.MessageBoxOption("Print stacktrace and OK"); //$NON-NLS-1$
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
