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
package org.eclipse.riena.sample.snippets;

import java.util.Date;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.IDateTextRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Demonstrates validation "after set" which allows values that do not pass validation to be written to the model
 */
public class SnippetValidationRule001 {
	public static class ValueHolder {
		private Date value;

		public Date getValue() {
			return value;
		}

		public void setValue(final Date value) {
			System.out.println("SnippetValidationAfterSet001.ValueHolder.setValue(): " + value); //$NON-NLS-1$
			this.value = value;
		}
	}

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			shell.setLayout(new GridLayout());

			UIControlsFactory.createLabel(shell, "The date will only pass validation if it is in the year 2000.", SWT.WRAP).setLayoutData(
					new GridData(GridData.FILL_HORIZONTAL));

			final Text text = UIControlsFactory.createTextDate(shell);
			final GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
			layoutData.horizontalIndent = 20;
			text.setLayoutData(layoutData);
			final IDateTextRidget r = (IDateTextRidget) SwtRidgetFactory.createRidget(text);

			r.addValidationRule(new IValidator() {
				public IStatus validate(final Object value) {
					if (value.toString().contains("2000")) { //$NON-NLS-1$
						return Status.OK_STATUS;
					}
					return new Status(IStatus.ERROR, "org.eclipse.riena.sample.snippets", "error message"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}, ValidationTime.AFTER_UPDATE_TO_MODEL);
			r.setDirectWriting(true);
			r.bindToModel(new ValueHolder(), "value"); //$NON-NLS-1$

			UIControlsFactory.createLabel(shell, "Note that all technically correct dates will be written to the model, even if they do not pass validation.",
					SWT.WRAP).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			shell.setSize(400, 200);
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
