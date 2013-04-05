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
package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 * Controller for the {@link MarkerHidingSubModuleController} example.
 */
public class MarkerHidingSubModuleController extends SubModuleController {

	@Override
	public void configureRidgets() {
		final ITextRidget textRidget = getRidget(ITextRidget.class, "textRidget"); //$NON-NLS-1$
		textRidget.setMandatory(false);
		textRidget.setErrorMarked(false);

		final IActionRidget btnMandatory = getRidget(IActionRidget.class, "btnMandatory"); //$NON-NLS-1$
		btnMandatory.addListener(new IActionListener() {

			private boolean showMandatory;

			public void callback() {
				showMandatory = !showMandatory;
				textRidget.setMandatory(showMandatory);
			}
		});

		final IActionRidget btnError = getRidget(IActionRidget.class, "btnError"); //$NON-NLS-1$
		btnError.addListener(new IActionListener() {

			private boolean showError;

			public void callback() {
				showError = !showError;
				textRidget.setErrorMarked(showError);
			}
		});

		final IActionRidget btnHideMandatory = getRidget(IActionRidget.class, "btnHideMandatory"); //$NON-NLS-1$
		btnHideMandatory.addListener(new IActionListener() {

			private boolean hideMandatory;

			@SuppressWarnings("unchecked")
			public void callback() {
				hideMandatory = !hideMandatory;
				if (hideMandatory) {
					textRidget.hideMarkersOfType(MandatoryMarker.class);
				} else {
					textRidget.showMarkersOfType(MandatoryMarker.class);
				}
			}
		});

		final IActionRidget btnHideError = getRidget(IActionRidget.class, "btnHideError"); //$NON-NLS-1$
		btnHideError.addListener(new IActionListener() {

			private boolean hideError;

			@SuppressWarnings("unchecked")
			public void callback() {
				hideError = !hideError;
				if (hideError) {
					textRidget.hideMarkersOfType(ErrorMarker.class);
				} else {
					textRidget.showMarkersOfType(ErrorMarker.class);
				}
			}
		});
	}
}
