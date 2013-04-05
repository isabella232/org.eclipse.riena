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

import java.util.Arrays;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.databinding.ConverterFactory;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Demonstrates converting an enum to String and back while using a ComboRidget.
 */
public class SnippetComboRidget002 {

	private enum Answer {
		YES, NO, MAYBE
	}

	public static void main(final String[] args) {
		final Display display = Display.getDefault();

		try {
			final Shell shell = UIControlsFactory.createShell(display);
			shell.setLayout(new GridLayout(2, false));
			shell.setText(SnippetComboRidget002.class.getSimpleName());

			UIControlsFactory.createLabel(shell, "ComboRidget:"); //$NON-NLS-1$
			final Combo combo = UIControlsFactory.createCombo(shell);

			UIControlsFactory.createLabel(shell, "Selection:"); //$NON-NLS-1$
			final Label label = UIControlsFactory.createLabel(shell, ""); //$NON-NLS-1$
			GridDataFactory.fillDefaults().grab(true, false).applyTo(label);

			final IComboRidget comboRidget = (IComboRidget) SwtRidgetFactory.createRidget(combo);
			final WritableList values = new WritableList(Arrays.asList(Answer.YES, Answer.NO, Answer.MAYBE),
					Answer.class);
			final WritableValue selection = new WritableValue(Answer.YES, Answer.class);

			final ConverterFactory<?, ?> factory = new ConverterFactory<Answer, String>(Answer.class, String.class)
					.add(Answer.YES, "Yes").add(Answer.NO, "No").add(Answer.MAYBE, "Maybe").add(null, ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			comboRidget.setModelToUIControlConverter(factory.createFromToConverter());
			comboRidget.setUIControlToModelConverter(factory.createToFromConverter());
			comboRidget.bindToModel(values, Answer.class, null, selection);
			comboRidget.updateFromModel();

			final DataBindingContext dbc = new DataBindingContext();
			dbc.bindValue(SWTObservables.observeText(label), selection);

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
