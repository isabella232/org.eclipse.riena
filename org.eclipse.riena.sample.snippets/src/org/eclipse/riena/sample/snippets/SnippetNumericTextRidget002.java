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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.ui.ridgets.IDecimalTextRidget;
import org.eclipse.riena.ui.ridgets.INumericTextRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Demonstrates how to use a {@link INumericTextRidget} or
 * {@link IDecimalTextRidget} that converts empty values to "0" (or "0,0..0").
 */
public final class SnippetNumericTextRidget002 {

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = UIControlsFactory.createShell(display);
			shell.setText(SnippetNumericTextRidget002.class.getSimpleName());
			GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(true).margins(10, 10).spacing(20, 10)
					.applyTo(shell);

			final GridDataFactory gdfFill = GridDataFactory.fillDefaults().grab(true, false);

			final Label header1 = UIControlsFactory.createLabel(shell, "ConvertEmptyToZero = false"); //$NON-NLS-1$
			GridDataFactory.fillDefaults().span(2, 1).applyTo(header1);

			UIControlsFactory.createLabel(shell, "NumericTextRidget:"); //$NON-NLS-1$
			final Text txtNumeric1 = UIControlsFactory.createTextNumeric(shell);
			gdfFill.applyTo(txtNumeric1);

			UIControlsFactory.createLabel(shell, "DecimalTextRidget:"); //$NON-NLS-1$
			final Text txtDecimal1 = UIControlsFactory.createTextDecimal(shell);
			gdfFill.applyTo(txtDecimal1);

			final Label header2 = UIControlsFactory.createLabel(shell, "ConvertEmptyToZero = TRUE"); //$NON-NLS-1$
			GridDataFactory.fillDefaults().span(2, 1).applyTo(header2);

			UIControlsFactory.createLabel(shell, "NumericTextRidget:"); //$NON-NLS-1$
			final Text txtNumeric2 = UIControlsFactory.createTextNumeric(shell);
			gdfFill.applyTo(txtNumeric2);

			UIControlsFactory.createLabel(shell, "DecimalTextRidget:"); //$NON-NLS-1$
			final Text txtDecimal2 = UIControlsFactory.createTextDecimal(shell);
			gdfFill.applyTo(txtDecimal2);

			final INumericTextRidget ridgetNumeric1 = (INumericTextRidget) SwtRidgetFactory.createRidget(txtNumeric1);
			final INumericTextRidget ridgetNumeric2 = (INumericTextRidget) SwtRidgetFactory.createRidget(txtNumeric2);
			final INumericTextRidget ridgetDecimal1 = (INumericTextRidget) SwtRidgetFactory.createRidget(txtDecimal1);
			final INumericTextRidget ridgetDecimal2 = (INumericTextRidget) SwtRidgetFactory.createRidget(txtDecimal2);

			ridgetNumeric1.setText(null);
			ridgetNumeric2.setConvertEmptyToZero(true);
			ridgetNumeric2.setText(null);
			ridgetDecimal1.setText(null);
			ridgetDecimal2.setConvertEmptyToZero(true);
			ridgetDecimal2.setText(null);

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
