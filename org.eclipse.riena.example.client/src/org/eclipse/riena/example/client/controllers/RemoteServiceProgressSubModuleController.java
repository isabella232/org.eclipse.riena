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

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.riena.communication.core.progressmonitor.IRemoteProgressMonitorRegistry;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.example.client.communication.IInfoService;
import org.eclipse.riena.example.client.communication.InfoServiceFake;
import org.eclipse.riena.example.client.communication.RemoteCallProcess;
import org.eclipse.riena.example.client.communication.ServiceProgressVisualizer;
import org.eclipse.riena.internal.example.client.Activator;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.uiprocess.UISynchronizer;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;

public class RemoteServiceProgressSubModuleController extends SubModuleController {

	public static final String SERVICE_CALL_ACTION_MANUAL = "serviceCallActionManual"; //$NON-NLS-1$
	public static final String SERVICE_CALL_ACTION_UIPROCESS = "serviceCallActionUIProcess"; //$NON-NLS-1$

	private IRemoteProgressMonitorRegistry remoteProgressMonitorRegistry;
	private final IInfoService remoteService;

	public RemoteServiceProgressSubModuleController() {
		remoteService = new InfoServiceFake();
		Inject.service(IRemoteProgressMonitorRegistry.class).useRanking().into(this).andStart(Activator.getDefault().getContext());
	}

	public void bind(final IRemoteProgressMonitorRegistry remoteProgressMonitorRegistry) {
		this.remoteProgressMonitorRegistry = remoteProgressMonitorRegistry;
	}

	public void unbind(final IRemoteProgressMonitorRegistry remoteProgressMonitorRegistry) {
		this.remoteProgressMonitorRegistry = null;
	}

	@Override
	public void configureRidgets() {
		getActionRidgetManual().setText("[Manual Way]"); //$NON-NLS-1$
		registerManualRemoteServiceCallback();
		getActionRidgetUIProcess().setText("[UIProcess Way]"); //$NON-NLS-1$
		registerUIProcessRemoteServiceCallback();
	}

	private IActionRidget getActionRidgetManual() {
		return getRidget(IActionRidget.class, SERVICE_CALL_ACTION_MANUAL);
	}

	private IActionRidget getActionRidgetUIProcess() {
		return getRidget(IActionRidget.class, SERVICE_CALL_ACTION_UIPROCESS);
	}

	//
	/// The manual way shows that ServiceProgressVisualizer implicit visualizes Service calls with a progress box

	private void registerManualRemoteServiceCallback() {
		getActionRidgetManual().addListener(new RemoteServiceManualCallbackAdapter());
	}

	class RemoteServiceManualCallbackAdapter implements IActionListener {

		public void callback() {
			simulateManualRemoteServiceCall();
		}

	}

	private void simulateManualRemoteServiceCall() {
		//fork
		new CommunicationSimulator().start();
	}

	class CommunicationSimulator extends Thread {
		@Override
		public void run() {
			blockSubModule(true);

			final ServiceProgressVisualizer serviceProgress = new ServiceProgressVisualizer("remote"); //$NON-NLS-1$

			// add monitor
			remoteProgressMonitorRegistry.addProgressMonitor(remoteService, serviceProgress, IRemoteProgressMonitorRegistry.RemovalPolicy.AFTER_ALL_CALLS);

			remoteService.getInfo("foo"); //$NON-NLS-1$

			// remove monitor
			remoteProgressMonitorRegistry.removeAllProgressMonitors(remoteService);

			blockSubModule(false);
		}

		private void blockSubModule(final boolean block) {
			UISynchronizer.createSynchronizer().syncExec(new Runnable() {

				public void run() {
					setBlocked(block);
				}

			});
		}

	}

	//
	/// The UIProcess way ..

	private void registerUIProcessRemoteServiceCallback() {
		getActionRidgetUIProcess().addListener(new RemoteServiceUIProcessCallbackAdapter());
	}

	class RemoteServiceUIProcessCallbackAdapter implements IActionListener {

		public void callback() {
			simulateUIProcessRemoteServiceCall();
		}

	}

	private void simulateUIProcessRemoteServiceCall() {
		final RemoteCallProcess<IInfoService> process = new RemoteCallProcess<IInfoService>("remote", true, //$NON-NLS-1$
				getNavigationNode()) {

			@Override
			public void initialUpdateUI(final int totalWork) {
				super.initialUpdateUI(totalWork);
				setBlocked(true);
			}

			@Override
			public boolean runJob(final IProgressMonitor monitor) {
				getService().getInfo("foo"); //$NON-NLS-1$
				return true;
			}

			@Override
			public void finalUpdateUI() {
				super.finalUpdateUI();
				setBlocked(false);
			}

			@Override
			protected IInfoService getService() {
				return remoteService;
			}

		};

		process.start();
	}

}
