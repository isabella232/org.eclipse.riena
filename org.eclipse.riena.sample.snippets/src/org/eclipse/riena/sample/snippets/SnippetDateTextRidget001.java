/*******************************************************************************
 * Copyright (c) 2007, 2008 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.sample.snippets;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.riena.ui.ridgets.IDateTextFieldRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.ridgets.util.beans.StringBean;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Demonstrates how to use a {@link IDateTextFieldRidget}.
 */
public final class SnippetDateTextRidget001 {

	public static void main(String[] args) {
		Display display = Display.getDefault();
		try {
			Shell shell = new Shell();
			shell.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND));
			GridLayoutFactory.fillDefaults().numColumns(2).margins(10, 10).spacing(20, 10).applyTo(shell);

			UIControlsFactory.createLabel(shell, "Date (dd.MM.yyyy):"); //$NON-NLS-1$
			Text txtInput = UIControlsFactory.createTextDate(shell);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(txtInput);

			UIControlsFactory.createLabel(shell, "Output:"); //$NON-NLS-1$
			Text txtOutput = UIControlsFactory.createText(shell);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(txtOutput);

			IDateTextFieldRidget rInput = (IDateTextFieldRidget) SwtRidgetFactory.createRidget(txtInput);
			rInput.setFormat(IDateTextFieldRidget.FORMAT_DDMMYYYY);
			rInput.setDirectWriting(true);

			ITextFieldRidget rOutput = (ITextFieldRidget) SwtRidgetFactory.createRidget(txtOutput);
			rOutput.setOutputOnly(true);

			DataBindingContext dbc = new DataBindingContext();
			dbc.bindValue(BeansObservables.observeValue(rOutput, ITextFieldRidget.PROPERTY_TEXT), BeansObservables
					.observeValue(rInput, ITextFieldRidget.PROPERTY_TEXT), new UpdateValueStrategy(
					UpdateValueStrategy.POLICY_NEVER), new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
			rInput.bindToModel(new StringBean("01.10.2008"), StringBean.PROP_VALUE); //$NON-NLS-1$
			rInput.updateFromModel();

			shell.setSize(270, 270);
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
