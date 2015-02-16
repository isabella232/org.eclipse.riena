/*******************************************************************************
 * Copyright (c) 2007, 2015 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.example.client.views.SharedViewDemoSubModuleView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ICompositeRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;

/**
 * Second controller for {@link SharedViewDemoSubModuleView}.
 * Code is the same as {@link SharedViewDemoSubModuleController}.
 */
public class SharedViewDemoSecondSubModuleController extends SubModuleController {

	private final Person personBean;

	public SharedViewDemoSecondSubModuleController() {
		this(null);
	}

	public SharedViewDemoSecondSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
		personBean = new Person("", "", null); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public void configureRidgets() {
		final ITextRidget txtFirst = getRidget(ITextRidget.class, "txtFirst"); //$NON-NLS-1$
		txtFirst.bindToModel(personBean, Person.PROPERTY_FIRSTNAME);

		final ITextRidget txtLast = getRidget(ITextRidget.class, "txtLast"); //$NON-NLS-1$
		txtLast.addMarker(new MandatoryMarker());
		txtLast.bindToModel(personBean, Person.PROPERTY_LASTNAME);

		final IToggleButtonRidget btnFemale = getRidget(IToggleButtonRidget.class, "btnFemale"); //$NON-NLS-1$
		btnFemale.addMarker(new MandatoryMarker());
		btnFemale.bindToModel(this, "female"); //$NON-NLS-1$

		final IToggleButtonRidget btnMale = getRidget(IToggleButtonRidget.class, "btnMale"); //$NON-NLS-1$
		btnMale.addMarker(new MandatoryMarker());
		btnFemale.bindToModel(this, "male"); //$NON-NLS-1$

		final IActionRidget btnDefault = getRidget(IActionRidget.class, "btnDefault"); //$NON-NLS-1$
		addDefaultAction(getRidget(ICompositeRidget.class, "view"), btnDefault); //$NON-NLS-1$
		btnDefault.addListener(new IActionListener() {
			public void callback() {
				System.out.println(btnDefault.getText() + " pushed in " + SharedViewDemoSecondSubModuleController.class); //$NON-NLS-1$
			}
		});
		updateAllRidgetsFromModel();
	}

	public boolean isMale() {
		return Person.MALE.equals(personBean.getGender());
	}

	public void setMale(final boolean male) {
		if (male) {
			personBean.setGender(Person.MALE);
		}
	}

	public boolean isFemale() {
		return Person.FEMALE.equals(personBean.getGender());
	}

	public void setFemale(final boolean female) {
		if (female) {
			personBean.setGender(Person.FEMALE);
		}
	}

}
