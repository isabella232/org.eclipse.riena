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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import org.eclipse.riena.core.exception.MurphysLawFailure;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.uiprocess.IUIProcessChangeListener;
import org.eclipse.riena.ui.core.uiprocess.UIProcess;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;

public class UIProcessDemoSubModuleController extends SubModuleController {

	private boolean registered;
	private UIProcess process;
	private UIProcess processWithListener;

	public UIProcessDemoSubModuleController() {
		this(null);
	}

	public UIProcessDemoSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	public IActionRidget getActionRidget() {
		return getRidget(IActionRidget.class, "actionRidget"); //$NON-NLS-1$
	}

	public IActionRidget getActionRidgetJob() {
		return getRidget(IActionRidget.class, "actionRidgetJob"); //$NON-NLS-1$
	}

	public IActionRidget getActionRidgetListener() {
		return getRidget(IActionRidget.class, "actionRidgetListener"); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	@Override
	public void configureRidgets() {
		if (getActionRidget() != null && !registered) {
			initUIProcessAction();
			initJobAction();
			initListenerAction();
			registered = true;
		}
	}

	private void initJobAction() {
		getActionRidgetJob().setText("start job"); //$NON-NLS-1$
		getActionRidgetJob().addListener(new JobProcessAction());

	}

	private void initUIProcessAction() {
		getActionRidget().setText("start UIProcess"); //$NON-NLS-1$
		getActionRidget().addListener(new UIProcessAction());
	}

	private void initListenerAction() {
		getActionRidgetListener().setText("start with listener"); //$NON-NLS-1$
		getActionRidgetListener().addListener(new ListenerAction());
	}

	void runWithListener() {
		boolean addListenerAfterProcessStart = false;
		if (processWithListener == null) {
			addListenerAfterProcessStart = true;
			processWithListener = new UIProcess("sample uiProcess with listener", true, getNavigationNode()) {
				@Override
				public boolean runJob(final IProgressMonitor monitor) {
					return doSomeStuff(this, monitor);
				}

				@Override
				protected int getTotalWork() {
					return 10;
				}

				@Override
				public void initialUpdateUI(final int totalWork) {
					System.out.println("UIProcessDemoSubModuleController.runWithListener().new UIProcess() {...}.initialUpdateUI()");
				}

				@Override
				public void finalUpdateUI() {
					System.out.println("UIProcessDemoSubModuleController.runWithListener().new UIProcess() {...}.finalUpdateUI()");
				}
			};
			processWithListener.addUIProcessChangedListener(new IUIProcessChangeListener() {
				public void afterInitialUpdateUI(final int totalWork) {
					System.out
							.println("UIProcessDemoSubModuleController.runWithListener().new IUIProcessChangeListener() {...}.afterInitialUpdateUI()  - added before process start");
				}

				public void afterFinalUpdateUI() {
					System.out
							.println("UIProcessDemoSubModuleController.runWithListener().new IUIProcessChangeListener() {...}.afterFinalUpdateUI() - added before process start");
				}
			});
			processWithListener.setNote("check sysout for listener events");
		}

		processWithListener.start();

		if (addListenerAfterProcessStart) {
			processWithListener.addUIProcessChangedListener(new IUIProcessChangeListener() {
				public void afterInitialUpdateUI(final int totalWork) {
					System.out
							.println("UIProcessDemoSubModuleController.runWithListener().new IUIProcessChangeListener() {...}.afterInitialUpdateUI() - added after process start");
				}

				public void afterFinalUpdateUI() {
					System.out
							.println("UIProcessDemoSubModuleController.runWithListener().new IUIProcessChangeListener() {...}.afterFinalUpdateUI() - added after process start");
				}
			});
		}
	}

	void runUIProcess() {
		if (process == null) {
			process = new UIProcess("sample uiProcess", true, getNavigationNode()) { //$NON-NLS-1$

				@Override
				public void initialUpdateUI(final int totalWork) {
					super.initialUpdateUI(totalWork);
					//					setBlocked(true);
				}

				@Override
				public boolean runJob(final IProgressMonitor monitor) {
					return doSomeStuff(this, monitor);
				}

				@Override
				public void finalUpdateUI() {
					super.finalUpdateUI();
					setBlocked(false);
				}

				@Override
				protected int getTotalWork() {
					return 10;
				}
			};
			process.setNote("sample uiProcess note " + getNavigationNode().getLabel() + ".."); //$NON-NLS-1$ //$NON-NLS-2$
			process.setTitle("sample uiProcess"); //$NON-NLS-1$
			process.setCancelEnabled(false);
		}

		if (process.start()) {
			process.setTitle("sample uiProcess"); //$NON-NLS-1$
		}

	}

	void runJob() {
		final Job job = new Job("eclipse job") { //$NON-NLS-1$
			@Override
			public IStatus run(final IProgressMonitor monitor) {
				try {
					monitor.beginTask("eclipse job", 10); //$NON-NLS-1$
					for (int i = 0; i < 10; i++) {
						try {
							Thread.sleep(500);
						} catch (final InterruptedException e) {
							throw new MurphysLawFailure("Sleeping failed", e); //$NON-NLS-1$
						}
						monitor.worked(1);
						if (monitor.isCanceled()) {
							return Status.CANCEL_STATUS;
						}
					}
				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}
		};
		job.setProperty(UIProcess.PROPERTY_CONTEXT, getNavigationNode());
		job.setUser(true);// to be visualized the job has to be user
		job.schedule();
	}

	/**
	 * @param monitor
	 * @return
	 */
	private boolean doSomeStuff(final UIProcess process, final IProgressMonitor monitor) {
		try {
			Thread.sleep(500);
		} catch (final InterruptedException e) {
			throw new MurphysLawFailure("Sleeping failed", e); //$NON-NLS-1$
		}
		for (int i = 0; i < 10; i++) {
			if (monitor.isCanceled()) {
				monitor.done();
				return false;
			}
			try {
				Thread.sleep(500);
			} catch (final InterruptedException e) {
				throw new MurphysLawFailure("Sleeping failed", e); //$NON-NLS-1$
			}
			if (process != null) {
				process.setTitle("sample uiProcess worked [" + i + "]"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			monitor.worked(1);
		}
		return true;
	}

	private class ListenerAction implements IActionListener {

		public void callback() {
			runWithListener();
		}

	}

	private class UIProcessAction implements IActionListener {

		public void callback() {
			runUIProcess();
		}

	}

	private class JobProcessAction implements IActionListener {

		public void callback() {
			runJob();
		}

	}

}
