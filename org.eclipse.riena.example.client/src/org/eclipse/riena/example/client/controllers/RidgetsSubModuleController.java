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

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;

/**
 * Controller of the sub module that shows a set of ridgets.
 */
public class RidgetsSubModuleController extends SubModuleController {

	private final static String ICON_SAMPLE = "sample.gif"; //$NON-NLS-1$
	private final static String ICON_RED = "ledred.png"; //$NON-NLS-1$
	private final static String ICON_GREEN = "ledlightgreen.png"; //$NON-NLS-1$

	private final RidgetsModel model;

	public RidgetsSubModuleController() {
		this(null);
	}

	public RidgetsSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
		model = new RidgetsModel();
		model.setToggleOneSelected(true);
		model.setToggleTwoSelected(false);
		model.setCheckOneSelected(true);
	}

	@Override
	public void configureRidgets() {
		super.configureRidgets();

		final IToggleButtonRidget toggleOne = getRidget(IToggleButtonRidget.class, "toggleOne"); //$NON-NLS-1$
		toggleOne.setText("&Toggle 1"); //$NON-NLS-1$
		toggleOne.setIcon(ICON_SAMPLE);
		toggleOne.bindToModel(model, "toggleOneSelected"); //$NON-NLS-1$
		toggleOne.updateFromModel();

		final IToggleButtonRidget toggleTwo = getRidget(IToggleButtonRidget.class, "toggleTwo"); //$NON-NLS-1$
		toggleTwo.setText("&Toggle 2"); //$NON-NLS-1$
		toggleTwo.bindToModel(model, "toggleTwoSelected"); //$NON-NLS-1$
		toggleTwo.updateFromModel();

		final IToggleButtonRidget checkOne = getRidget(IToggleButtonRidget.class, "checkOne"); //$NON-NLS-1$
		checkOne.setText("C&heck 1"); //$NON-NLS-1$
		checkOne.bindToModel(model, "checkOneSelected"); //$NON-NLS-1$
		checkOne.updateFromModel();

		final IActionRidget buttonOne = getRidget(IActionRidget.class, "buttonOne"); //$NON-NLS-1$
		buttonOne.setText("Button 1"); //$NON-NLS-1$
		buttonOne.setIcon(ICON_SAMPLE);
		buttonOne.addListener(new IActionListener() {
			public void callback() {
				buttonOne.setIcon(ICON_RED);
				System.out.println("Button clicked..."); //$NON-NLS-1$
			}
		});

		final IActionRidget buttonTwo = getRidget(IActionRidget.class, "buttonTwo"); //$NON-NLS-1$
		buttonTwo.setText("Button 2"); //$NON-NLS-1$
		buttonTwo.addListener(new IActionListener() {
			public void callback() {
				buttonTwo.setIcon(ICON_GREEN);
				System.out.println("Button clicked..."); //$NON-NLS-1$
			}
		});

		final IActionListener actionListener = new IActionListener() {
			public void callback() {
				System.out.println("Button clicked..."); //$NON-NLS-1$
			}
		};

		final IActionRidget imageButton = getRidget("imageButton"); //$NON-NLS-1$
		imageButton.setIcon("imageBtn"); //$NON-NLS-1$
		imageButton.addListener(actionListener);

		final IActionRidget arrowButton = getRidget("arrowButton"); //$NON-NLS-1$
		arrowButton.setIcon("arrowRight"); //$NON-NLS-1$
		arrowButton.addListener(actionListener);

		final IActionRidget arrowHotButton = getRidget("arrowHotButton"); //$NON-NLS-1$
		arrowHotButton.setIcon("arrowRight"); //$NON-NLS-1$
		arrowHotButton.addListener(actionListener);
	}

	// helping classes
	//////////////////

	/**
	 * The model of this sub module controller.
	 */
	private static final class RidgetsModel {

		private boolean toggleOneSelected;
		private boolean toggleTwoSelected;
		private boolean checkOneSelected;

		@SuppressWarnings("unused")
		public boolean isToggleOneSelected() {
			return toggleOneSelected;
		}

		public void setToggleOneSelected(final boolean toggleOneSelected) {
			this.toggleOneSelected = toggleOneSelected;
		}

		public void setToggleTwoSelected(final boolean toggleTwoSelected) {
			this.toggleTwoSelected = toggleTwoSelected;
		}

		@SuppressWarnings("unused")
		public boolean isToggleTwoSelected() {
			return toggleTwoSelected;
		}

		@SuppressWarnings("unused")
		public boolean isCheckOneSelected() {
			return checkOneSelected;
		}

		public void setCheckOneSelected(final boolean checkOneSelected) {
			this.checkOneSelected = checkOneSelected;
		}

	}

}
