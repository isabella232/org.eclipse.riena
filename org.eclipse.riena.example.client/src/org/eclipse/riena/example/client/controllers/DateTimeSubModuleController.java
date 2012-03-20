/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.example.client.controllers;

import java.util.Date;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;

import org.eclipse.riena.beans.common.TypedBean;
import org.eclipse.riena.example.client.views.DateTimeSubModuleView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IDateTimeRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 * Controller for the {@link DateTimeSubModuleView} example.
 */
public class DateTimeSubModuleController extends SubModuleController {

	public DateTimeSubModuleController() {
		this(null);
	}

	public DateTimeSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	@Override
	public void configureRidgets() {
		final IDateTimeRidget dtDate = getRidget("dtDate"); //$NON-NLS-1$
		final IDateTimeRidget dtTime = getRidget("dtTime"); //$NON-NLS-1$
		final IDateTimeRidget dtDateOnly = getRidget("dtDateOnly"); //$NON-NLS-1$
		final IDateTimeRidget dtTimeOnly = getRidget("dtTimeOnly"); //$NON-NLS-1$
		final IDateTimeRidget dtCal = getRidget("dtCal"); //$NON-NLS-1$
		final ITextRidget txt1 = getRidget("txt1"); //$NON-NLS-1$
		final ITextRidget txt2 = getRidget("txt2"); //$NON-NLS-1$
		final ITextRidget txt3 = getRidget("txt3"); //$NON-NLS-1$
		final ITextRidget txt4 = getRidget("txt4"); //$NON-NLS-1$

		final long now = System.currentTimeMillis();
		final TypedBean<Date> date1 = new TypedBean<Date>(new Date(now));
		final TypedBean<Date> date2 = new TypedBean<Date>(new Date(now));
		final TypedBean<Date> date3 = new TypedBean<Date>(new Date(now));
		final TypedBean<Date> date4 = new TypedBean<Date>(new Date(now));

		dtDate.bindToModel(date1, TypedBean.PROP_VALUE);
		dtDate.updateFromModel();
		dtTime.bindToModel(date1, TypedBean.PROP_VALUE);
		dtTime.updateFromModel();

		dtDateOnly.bindToModel(date2, TypedBean.PROP_VALUE);
		dtDateOnly.updateFromModel();

		dtTimeOnly.bindToModel(date3, TypedBean.PROP_VALUE);
		dtTimeOnly.updateFromModel();

		dtCal.bindToModel(date4, TypedBean.PROP_VALUE);
		dtCal.updateFromModel();

		final DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(BeansObservables.observeValue(txt1, ITextRidget.PROPERTY_TEXT),
				BeansObservables.observeValue(date1, TypedBean.PROP_VALUE));
		dbc.bindValue(BeansObservables.observeValue(txt2, ITextRidget.PROPERTY_TEXT),
				BeansObservables.observeValue(date2, TypedBean.PROP_VALUE));
		dbc.bindValue(BeansObservables.observeValue(txt3, ITextRidget.PROPERTY_TEXT),
				BeansObservables.observeValue(date3, TypedBean.PROP_VALUE));
		dbc.bindValue(BeansObservables.observeValue(txt4, ITextRidget.PROPERTY_TEXT),
				BeansObservables.observeValue(date4, TypedBean.PROP_VALUE));

		makeOutputOnly(txt1, txt2, txt3, txt4);
	}

	// helping methods
	//////////////////

	private void makeOutputOnly(final ITextRidget... ridgets) {
		for (final ITextRidget ridget : ridgets) {
			ridget.setOutputOnly(true);
		}
	}
}
