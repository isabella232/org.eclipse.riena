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

import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.ridgets.validation.NotEmpty;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Mandatory text field, using both mandatory and error markers: When the field
 * is empty, the background turns yellow (mandatory marker). Also, a little red
 * X appears (error marker), because a validation rule prohibits the field to be
 * empty. The two markers can also be used independently - try it out.
 */
public final class SnippetTextRidget003 {

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = UIControlsFactory.createShell(display);
			shell.setText(SnippetTextRidget003.class.getSimpleName());
			GridLayoutFactory.fillDefaults().numColumns(2).margins(10, 10).spacing(20, 10).applyTo(shell);

			UIControlsFactory.createLabel(shell, "Name (mandatory):"); //$NON-NLS-1$
			final Text text = UIControlsFactory.createText(shell);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(text);

			final ITextRidget textRidget = (ITextRidget) SwtRidgetFactory.createRidget(text);

			textRidget.setMandatory(true);
			textRidget.setDirectWriting(true);
			textRidget.addValidationRule(new NotEmpty(), ValidationTime.ON_UI_CONTROL_EDIT);

			textRidget.setText("Try removing this text"); //$NON-NLS-1$

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