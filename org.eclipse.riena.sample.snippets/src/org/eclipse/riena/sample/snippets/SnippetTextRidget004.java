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

package org.eclipse.riena.sample.snippets;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.databinding.StringToLowerCaseConverter;
import org.eclipse.riena.ui.ridgets.databinding.StringToUpperCaseConverter;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * TextRidget using a converter to change the user's input before it appears in
 * the ridget.
 * 
 * @see StringToUpperCaseConverter
 * @see StringToLowerCaseConverter
 * @see ITextRidget#setInputToUIControlConverter(org.eclipse.core.databinding.conversion.IConverter)
 */
public final class SnippetTextRidget004 {

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = UIControlsFactory.createShell(display);
			shell.setText(SnippetTextRidget004.class.getSimpleName());
			GridLayoutFactory.fillDefaults().numColumns(2).margins(10, 10).spacing(20, 10).applyTo(shell);

			UIControlsFactory.createLabel(shell, "ALL UPPERCASE:"); //$NON-NLS-1$
			final Text txtUpper = UIControlsFactory.createText(shell);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(txtUpper);

			UIControlsFactory.createLabel(shell, "all lowercase:"); //$NON-NLS-1$
			final Text txtLower = UIControlsFactory.createText(shell);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLower);

			final ITextRidget upper = (ITextRidget) SwtRidgetFactory.createRidget(txtUpper);
			upper.setInputToUIControlConverter(new StringToUpperCaseConverter());

			final ITextRidget lower = (ITextRidget) SwtRidgetFactory.createRidget(txtLower);
			lower.setInputToUIControlConverter(new StringToLowerCaseConverter());

			shell.setSize(400, 100);
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