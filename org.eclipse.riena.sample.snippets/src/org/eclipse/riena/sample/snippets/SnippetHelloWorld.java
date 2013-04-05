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

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;

/**
 * 'Hello World' using an {@link ILabelRidget}.
 */
public final class SnippetHelloWorld {

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			GridLayoutFactory.fillDefaults().applyTo(shell);

			final Label label = new Label(shell, SWT.BORDER);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(label);

			final ILabelRidget ridget = (ILabelRidget) SwtRidgetFactory.createRidget(label);
			ridget.bindToModel(PojoObservables.observeValue(new HelloModel(), "text")); //$NON-NLS-1$
			ridget.updateFromModel();

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

	private static final class HelloModel {
		private String text = "Hello Riena !"; //$NON-NLS-1$

		@SuppressWarnings("unused")
		public String getText() {
			return text;
		}

		@SuppressWarnings("unused")
		public void setText(final String text) {
			this.text = text;
		}
	}
}
