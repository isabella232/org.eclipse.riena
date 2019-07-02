/*******************************************************************************
 * Copyright © 2009 Florian Pirchner and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Florian Pirchner – initial implementation
 * compeople AG     - adjustments for Riena v1.2
 *******************************************************************************/
package org.eclipse.riena.sample.snippets;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Spinner;

import org.eclipse.riena.beans.common.IntegerBean;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.ISliderRidget;
import org.eclipse.riena.ui.ridgets.ISpinnerRidget;
import org.eclipse.riena.ui.ridgets.ITraverseRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Example of the {@link ITraverseRidget} used with SWT's Slider, Scale, Spinner
 * and ProgressBar controls.
 */
public final class SnippetTraverseRidget001 {

	private static final int MINIMUM = 75;
	private static final int MAXIMUM = 200;

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			shell.setText(SnippetTraverseRidget001.class.getSimpleName());
			shell.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
			GridLayoutFactory.fillDefaults().numColumns(2).margins(10, 10).spacing(20, 10).applyTo(shell);

			/**
			 * Create the controls
			 */
			// Slider
			UIControlsFactory.createLabel(shell, "Slider"); //$NON-NLS-1$
			final Slider slider = new Slider(shell, SWT.HORIZONTAL);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(slider);

			// Scale
			UIControlsFactory.createLabel(shell, "Scale"); //$NON-NLS-1$
			final Scale scale = new Scale(shell, SWT.HORIZONTAL);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(scale);
			scale.setBackground(shell.getBackground());

			// Spinner
			UIControlsFactory.createLabel(shell, "Spinner"); //$NON-NLS-1$
			final Spinner spinner = new Spinner(shell, SWT.HORIZONTAL);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(spinner);

			// ProgressBar
			UIControlsFactory.createLabel(shell, "Progress"); //$NON-NLS-1$
			final ProgressBar progressBar = new ProgressBar(shell, SWT.HORIZONTAL | SWT.SMOOTH);
			progressBar.setState(SWT.PAUSED);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(progressBar);

			/**
			 * Create the ridgets
			 */
			// Slider
			final ISliderRidget sliderRidget = (ISliderRidget) SwtRidgetFactory.createRidget(slider);
			sliderRidget.setMaximum(MAXIMUM);
			sliderRidget.setMinimum(MINIMUM);
			sliderRidget.setPageIncrement(25);
			sliderRidget.setIncrement(2);
			sliderRidget.setToolTipText("The current value is: [VALUE]."); //$NON-NLS-1$

			sliderRidget.bindToModel(new IntegerBean(100), IntegerBean.PROP_VALUE);
			sliderRidget.addValidationRule(new IValidator() {
				public IStatus validate(final Object value) {
					final int selection = (Integer) value;
					if (selection > MAXIMUM - 20 || selection < MINIMUM + 20) {
						return ValidationRuleStatus.error(true, "Value of of range!"); //$NON-NLS-1$
					}
					return ValidationRuleStatus.ok();
				}
			}, ValidationTime.ON_UPDATE_TO_MODEL);
			sliderRidget.updateFromModel();

			// Scale
			final ITraverseRidget scaleRidget = (ITraverseRidget) SwtRidgetFactory.createRidget(scale);
			scaleRidget.setMaximum(MAXIMUM);
			scaleRidget.setMinimum(MINIMUM);
			scaleRidget.setPageIncrement(25);
			scaleRidget.setIncrement(2);
			scaleRidget.setToolTipText("[VALUE]"); //$NON-NLS-1$

			scaleRidget.bindToModel(new IntegerBean(100), IntegerBean.PROP_VALUE);
			scaleRidget.updateFromModel();

			// Spinner
			final ISpinnerRidget spinnerRidget = (ISpinnerRidget) SwtRidgetFactory.createRidget(spinner);
			spinnerRidget.setMaximum(MAXIMUM);
			spinnerRidget.setMinimum(MINIMUM);
			spinnerRidget.setPageIncrement(25);
			spinnerRidget.setIncrement(2);
			spinnerRidget.setToolTipText("[VALUE] days left"); //$NON-NLS-1$

			spinnerRidget.bindToModel(new IntegerBean(100), IntegerBean.PROP_VALUE);
			spinnerRidget.updateFromModel();

			// ProgressBar
			final ITraverseRidget progressBarRidget = (ITraverseRidget) SwtRidgetFactory.createRidget(progressBar);
			progressBarRidget.setMaximum(MAXIMUM);
			progressBarRidget.setMinimum(MINIMUM);
			progressBarRidget.setToolTipText("Value of progress: [VALUE]"); //$NON-NLS-1$

			progressBarRidget.bindToModel(new IntegerBean(100), IntegerBean.PROP_VALUE);
			progressBarRidget.updateFromModel();

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
