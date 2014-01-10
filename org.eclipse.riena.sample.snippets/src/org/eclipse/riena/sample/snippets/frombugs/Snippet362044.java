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
package org.eclipse.riena.sample.snippets.frombugs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.ui.ridgets.IBrowserRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

public class Snippet362044 {

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			shell.setLayout(new GridLayout());

			final Control c = UIControlsFactory.createBrowser(shell, SWT.WEBKIT);
			c.setLayoutData(new GridData(GridData.FILL_BOTH));
			final IBrowserRidget r = (IBrowserRidget) SwtRidgetFactory.createRidget(c);
			r.setUrl("http://eclipse.org");
			r.updateFromModel();

			shell.setSize(400, 400);
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
