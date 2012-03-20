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

import java.util.List;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationContext;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ui.swt.views.SWTModuleController;
import org.eclipse.riena.ui.core.uiprocess.ProgressProviderBridge;
import org.eclipse.riena.ui.core.uiprocess.UIProcess;
import org.eclipse.riena.ui.swt.RienaMessageDialog;

/**
 * Implementation of a module controller that prevents closing the module while
 * a job is running.
 */
public class UIProcessesModuleController extends SWTModuleController {

	public UIProcessesModuleController(final IModuleNode navigationNode) {
		super(navigationNode);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Prevents disposing while a job is running.
	 */
	@Override
	public boolean allowsDispose(final INavigationNode<?> node, final INavigationContext context) {

		final List<UIProcess> processes = ProgressProviderBridge.instance().getRegisteredUIProcesses();
		for (final UIProcess process : processes) {
			final Job job = process.getJob();
			if (job.getState() == Job.RUNNING) {
				final Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
				final String title = "Running Jobs"; //$NON-NLS-1$
				String message = "Dispose was canceled\nbecause job ''{0}'' is still running!"; //$NON-NLS-1$
				message = NLS.bind(message, job.getName());
				RienaMessageDialog.openError(shell, title, message);
				return false;
			}
		}

		return super.allowsDispose(node, context);
	}

}
