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
package org.eclipse.riena.sample.snippets.frombugs;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.ui.core.marker.NegativeMarker;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Text field ridget with minimum length validation rule and direct writing.
 */
public final class Snippet294024 {

	public static void main(String[] args) {
		Display display = Display.getDefault();
		try {
			Shell shell = new Shell();
			GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 10).spacing(20, 10).applyTo(shell);

			final Text text1 = UIControlsFactory.createText(shell);
			final Text text2 = UIControlsFactory.createText(shell);

			Button button = UIControlsFactory.createButton(shell);
			button.setText("Toggle bound text field"); //$NON-NLS-1$

			final ITextRidget textRidget1 = (ITextRidget) SwtRidgetFactory.createRidget(text1);
			textRidget1.setErrorMarked(true);
			textRidget1.setOutputOnly(true);
			textRidget1.addMarker(new NegativeMarker());
			textRidget1.setText("ridget 1"); //$NON-NLS-1$

			final ITextRidget textRidget2 = (ITextRidget) SwtRidgetFactory.createRidget(text2);
			textRidget2.setText("ridget 2"); //$NON-NLS-1$

			IActionRidget actionRidget = (IActionRidget) SwtRidgetFactory.createRidget(button);
			actionRidget.addListener(new IActionListener() {
				int i;

				public void callback() {
					i++;
					textRidget1.setUIControl(null);
					textRidget2.setUIControl(null);
					if (i % 2 == 0) {
						textRidget1.setUIControl(text1);
						textRidget2.setUIControl(text2);
					} else {
						textRidget1.setUIControl(text2);
						textRidget2.setUIControl(text1);
					}
				}
			});

			button.setFocus();

			shell.setSize(200, 200);
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

	private static Label createLabel(Shell shell, String caption) {
		Label result = new Label(shell, SWT.NONE);
		result.setText(caption);
		return result;
	}
}