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
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.beans.common.IntegerBean;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * This snippet shows how to bind an {@link ISingleChoiceRidget} (its selection)
 * to a bean model.
 * 
 * The user is shown a group of labeled radio buttons from which she can choose;
 * her choice is reflected in the model. Through another binding, the model
 * value is propagated to an output label, which displays the chosen value.
 */
public final class SnippetSingleChoiceRidget001 {

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = UIControlsFactory.createShell(display);
			shell.setText(SnippetSingleChoiceRidget001.class.getSimpleName());
			GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 10).equalWidth(false).spacing(20, 10)
					.applyTo(shell);

			// Model
			final IntegerBean model = new IntegerBean(42);

			// Radio group for input
			final Group group = UIControlsFactory.createGroup(shell, "Your favorite number"); //$NON-NLS-1$
			GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(2).spacing(40, 40).applyTo(group);

			final Composite radioGroup = new ChoiceComposite(group, SWT.NONE, false);

			final ISingleChoiceRidget radioRidget = (ISingleChoiceRidget) SwtRidgetFactory.createRidget(radioGroup);
			final List<Integer> values = Arrays.asList(0, 23, 42);
			final List<String> labels = Arrays.asList("zero", "twenty-three", "forty-two"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			radioRidget.bindToModel(values, labels, model, IntegerBean.PROP_VALUE);
			radioRidget.updateFromModel();

			// Output label for displaying model state
			UIControlsFactory.createLabel(shell, "Value of the model:"); //$NON-NLS-1$
			final Label outputValueLabel = UIControlsFactory.createLabel(shell, ""); //$NON-NLS-1$

			final ILabelRidget outputRidget = (ILabelRidget) SwtRidgetFactory.createRidget(outputValueLabel);
			final DataBindingContext dbc = new DataBindingContext();
			dbc.bindValue(BeansObservables.observeValue(outputRidget, ILabelRidget.PROPERTY_TEXT), BeansObservables
					.observeValue(model, IntegerBean.PROP_VALUE), new UpdateValueStrategy(
					UpdateValueStrategy.POLICY_NEVER), new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));

			shell.setSize(300, 300);
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
