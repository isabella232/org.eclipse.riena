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
package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.beans.common.SingleSelectionListBean;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.annotation.OnActionCallback;

/**
 *
 */
public class ComboAndChoiceSubModuleController extends SubModuleController {

	private IComboRidget comboRidgetWithModel;
	private IComboRidget comboRidgetWithoutModel;

	@Override
	public void configureRidgets() {
		comboRidgetWithModel = getRidget(IComboRidget.class, "comboBoxWithModel"); //$NON-NLS-1$
		final SingleSelectionListBean colors = new SingleSelectionListBean(new Object[] { "white", "black", "red", "blue", "green", "brown", "yellow" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
		colors.setSelection("blue"); //$NON-NLS-1$
		comboRidgetWithModel.bindToModel(colors, SingleSelectionListBean.PROPERTY_VALUES, String.class, null, colors,
				SingleSelectionListBean.PROPERTY_SELECTION);

		comboRidgetWithoutModel = getRidget(IComboRidget.class, "comboBoxWithoutModel"); //$NON-NLS-1$
	}

	@OnActionCallback(ridgetId = "updateAllRidgetsFromModel")
	public void handleUpdateAllRidgetsFromModel() {
		updateAllRidgetsFromModel();
	}

	@OnActionCallback(ridgetId = "bindComboToModel")
	public void handleBindComboToModelAction() {
		final SingleSelectionListBean colors = new SingleSelectionListBean(new Object[] { "white", "black", "red", "blue", "green", "brown", "yellow" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
		colors.setSelection("red"); //$NON-NLS-1$
		comboRidgetWithoutModel.bindToModel(colors, SingleSelectionListBean.PROPERTY_VALUES, String.class, null, colors,
				SingleSelectionListBean.PROPERTY_SELECTION);
	}

	@OnActionCallback(ridgetId = "bindChoiceToModel")
	public void handleBindChoiceToModelAction() {
		final SingleSelectionListBean numbers = new SingleSelectionListBean(new Object[] { "choice 1", "choice 2", "choice 3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		numbers.setSelection("choice 1"); //$NON-NLS-1$
		final ISingleChoiceRidget compositeNumberModel = getRidget(ISingleChoiceRidget.class, "compositeNumberModel"); //$NON-NLS-1$
		compositeNumberModel.bindToModel(numbers, SingleSelectionListBean.PROPERTY_VALUES, numbers, SingleSelectionListBean.PROPERTY_SELECTION);
	}
}
