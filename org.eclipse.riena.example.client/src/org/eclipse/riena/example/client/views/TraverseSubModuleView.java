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
package org.eclipse.riena.example.client.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.StatusMeterWidget;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * View of a sub-module with some <i>traverseable</i> SWT Widgets (e.g.
 * {@code Scale}).
 */
public class TraverseSubModuleView extends SubModuleView {

	public TraverseSubModuleView() {
	}

	public static final String ID = TraverseSubModuleView.class.getName();

	/**
	 * {@inheritDoc}
	 * <p>
	 * <i>Traverseable</i> SWT Widgets are added to the view.
	 */
	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		UIControlsFactory.createLabel(parent, "Degrees Fahrenheit:"); //$NON-NLS-1$

		UIControlsFactory.createSpinner(parent, SWT.BORDER, "fahrenheitSpinner"); //$NON-NLS-1$

		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		UIControlsFactory.createLabel(parent, "Degree Celsius:"); //$NON-NLS-1$

		UIControlsFactory.createScale(parent, SWT.NONE, "celsiusScale"); //$NON-NLS-1$
		new Label(parent, SWT.NONE);

		final Composite compositeCelsius = new Composite(parent, SWT.NONE);
		compositeCelsius.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		compositeCelsius.setLayout(new GridLayout(3, false));

		final Label lblZero = new Label(compositeCelsius, SWT.NONE);
		lblZero.setText("0\u00B0"); //$NON-NLS-1$

		final Label labelDummy = new Label(compositeCelsius, SWT.NONE);
		labelDummy.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

		final Label lblPlus50 = new Label(compositeCelsius, SWT.NONE);
		lblPlus50.setText("50\u00B0"); //$NON-NLS-1$

		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		UIControlsFactory.createLabel(parent, "Kelvin:"); //$NON-NLS-1$

		final ProgressBar progressBar = UIControlsFactory
				.createProgressBar(parent, SWT.HORIZONTAL, "kelvinProgressBar"); //$NON-NLS-1$
		progressBar.setState(SWT.PAUSED);
		new Label(parent, SWT.NONE);

		final Composite compositeKelvin = new Composite(parent, SWT.NONE);
		compositeKelvin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		compositeKelvin.setLayout(new GridLayout(3, false));

		final Label lbl255 = new Label(compositeKelvin, SWT.NONE);
		lbl255.setText("255"); //$NON-NLS-1$

		final Label labelDummy2 = new Label(compositeKelvin, SWT.NONE);
		labelDummy2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

		final Label lbl300 = new Label(compositeKelvin, SWT.NONE);
		lbl300.setText("323"); //$NON-NLS-1$

		// Rankine (StatusMeter demo)
		UIControlsFactory.createLabel(parent, "Rankine:"); //$NON-NLS-1$

		final StatusMeterWidget statusMeter = UIControlsFactory.createStatusMeter(parent, "rankineStatusMeter"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().hint(SWT.DEFAULT, 24).applyTo(statusMeter);

		new Label(parent, SWT.NONE);

		final Composite compositeRankine = new Composite(parent, SWT.NONE);
		compositeRankine.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		compositeRankine.setLayout(new GridLayout(3, false));

		final Label lblRankineStart = new Label(compositeRankine, SWT.NONE);
		lblRankineStart.setText("491"); //$NON-NLS-1$

		final Label labelDummy3 = new Label(compositeRankine, SWT.NONE);
		labelDummy3.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

		final Label lblRankineEnd = new Label(compositeRankine, SWT.NONE);
		lblRankineEnd.setText("582"); //$NON-NLS-1$

	}

}
