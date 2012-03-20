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
package org.eclipse.riena.sample.snippets;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.MessageBox;

/**
 * Snippet using a {@link IMessageBoxRidget}.
 */
public final class SnippetMessageBoxRidget001 {

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			GridLayoutFactory.fillDefaults().applyTo(shell);

			final MessageBox messageBox = new MessageBox(shell);
			final IMessageBoxRidget messageBoxRidget = (IMessageBoxRidget) SwtRidgetFactory.createRidget(messageBox);
			// Configure MessageBoxRidget:
			messageBoxRidget.setType(IMessageBoxRidget.Type.QUESTION);
			messageBoxRidget.setTitle("Bridgekeeper"); //$NON-NLS-1$
			messageBoxRidget.setText("What is your favourite colour?"); //$NON-NLS-1$
			// Set options
			final IMessageBoxRidget.MessageBoxOption[] customOptions = new IMessageBoxRidget.MessageBoxOption[] {
					new IMessageBoxRidget.MessageBoxOption("Blue"), new IMessageBoxRidget.MessageBoxOption("Yellow") }; //$NON-NLS-1$//$NON-NLS-2$
			messageBoxRidget.setOptions(customOptions);

			final Button button = new Button(shell, SWT.BORDER);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(button);

			final IActionRidget actionRidget = (IActionRidget) SwtRidgetFactory.createRidget(button);
			actionRidget.setText("Show message box"); //$NON-NLS-1$
			actionRidget.addListener(new IActionListener() {
				public void callback() {
					messageBoxRidget.show();
				}
			});

			shell.pack();
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} finally {
			display.dispose();
		}
	}
}
