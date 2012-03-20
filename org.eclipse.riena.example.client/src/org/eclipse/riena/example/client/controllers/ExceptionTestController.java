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

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.uiprocess.UIProcess;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;

/**
 *
 */
public class ExceptionTestController extends SubModuleController {

	@Override
	public void configureRidgets() {
		// TODO Auto-generated method stub
		super.configureRidgets();

		final IActionRidget localNullPtr = getRidget("localNullPointerAction"); //$NON-NLS-1$
		localNullPtr.addListener(new IActionListener() {

			public void callback() {
				throw new NullPointerException("test nullpointer exception "); //$NON-NLS-1$

			}
		});

		final IActionRidget uiprocessNullPtr1 = getRidget("uiprocessNullPointerActionRunJob"); //$NON-NLS-1$
		uiprocessNullPtr1.addListener(new IActionListener() {

			public void callback() {
				final UIProcess process = new UIProcess("TestException", true, getNavigationNode()) { //$NON-NLS-1$

					@Override
					public boolean runJob(final IProgressMonitor monitor) {
						throw new NullPointerException("nullpointer in runJob"); //$NON-NLS-1$
					}

				};
				process.start();

			}
		});
		final IActionRidget uiprocessNullPtr2 = getRidget("uiprocessNullPointerActionUpdateUI"); //$NON-NLS-1$
		uiprocessNullPtr2.addListener(new IActionListener() {

			public void callback() {
				final UIProcess process = new UIProcess("TestException", true, getNavigationNode()) { //$NON-NLS-1$

					@Override
					public boolean runJob(final IProgressMonitor monitor) {
						notifyUpdateUI();
						return false;
					}

					@Override
					public void updateUi() {
						throw new NullPointerException("nullpointer in updateUI"); //$NON-NLS-1$
					}

				};
				process.start();

			}
		});
		final IActionRidget uiprocessNullPtr3 = getRidget("uiprocessNullPointerActionFinalUpdateUI"); //$NON-NLS-1$
		uiprocessNullPtr3.addListener(new IActionListener() {

			public void callback() {
				final UIProcess process = new UIProcess("TestException", true, getNavigationNode()) { //$NON-NLS-1$

					@Override
					public void finalUpdateUI() {
						throw new NullPointerException("nullpointer in finalUpdateUI"); //$NON-NLS-1$
					}

				};
				process.start();

			}
		});
	}

}
