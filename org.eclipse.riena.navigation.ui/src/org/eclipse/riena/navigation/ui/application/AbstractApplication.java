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
package org.eclipse.riena.navigation.ui.application;

import java.util.List;

import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.log.Logger;
import org.eclipse.ui.internal.progress.ProgressManager;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.service.Service;
import org.eclipse.riena.core.util.RAPDetector;
import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.navigation.ui.Activator;
import org.eclipse.riena.internal.navigation.ui.filter.IUIFilterApplier;
import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeProvider;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.StartupNodeInfo;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.model.NavigationNodeProvider;
import org.eclipse.riena.navigation.ui.login.ILoginDialogViewExtension;
import org.eclipse.riena.ui.core.uiprocess.ProgressProviderBridge;

/**
 * Abstract application defining the basic structure of a Riena application
 */
@SuppressWarnings("restriction")
public abstract class AbstractApplication implements IApplication, IApplicationModelCreator {

	/**
	 * The result code EXIT_ABORT can be used to indicate that the login process was aborted. As a result the application has to be terminated.
	 * 
	 * @since 3.0
	 */
	public static final Integer EXIT_ABORT = -1;

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), AbstractApplication.class);

	protected ILoginDialogViewExtension loginDialogViewExtension;

	public Object start(final IApplicationContext context) throws Exception {

		final Object result = initializePerformLogin(context);
		if (!EXIT_OK.equals(result)) {
			return convertLoginToApplicationResult(result);
		}
		initializeUI();
		final IApplicationNode applicationNode = createModel();
		initApplicationNode(applicationNode);
		return createView(context, applicationNode);
	}

	/**
	 * Important: This method is NOT API. It is used for the 3.x/e4 split.
	 * 
	 * @since 4.0
	 */
	public void initApplicationNode(final IApplicationNode applicationNode) {
		if (applicationNode == null) {
			throw new RuntimeException("Application did not return an ApplicationModel in method 'createModel' but returned NULL. Cannot continue"); //$NON-NLS-1$
		}
		applyUserInterfaceFilters(applicationNode);
		ApplicationNodeManager.registerApplicationNode(applicationNode);
		createStartupNodes(applicationNode);
		initializeNode(applicationNode);
		installProgressProviderBridge();
	}

	private void applyUserInterfaceFilters(final IApplicationNode applicationNode) {
		final IUIFilterApplier filter = Service.get(IUIFilterApplier.class);
		if (filter != null) {
			filter.applyFilter(applicationNode);
		}
	}

	/**
	 * Returns the exit Object or code of the application. May be overridden by subclasses. By default the login return code <code>EXIT_ABORT</code> is
	 * converted to application result code <code>EXIT_OK</code>, because aborting the login process is a normal way to terminate the application.
	 * 
	 * @param result
	 *            the login result code to convert.
	 * @return the exit Object or code of the application.
	 * @since 3.0
	 */
	protected Object convertLoginToApplicationResult(final Object result) {
		if (EXIT_ABORT.equals(result)) {
			return EXIT_OK;
		} else {
			return result;
		}
	}

	/**
	 * hook called before createModel()
	 * 
	 * @since 3.0
	 */
	protected void initializeUI() {
	}

	private void installProgressProviderBridge() {
		disableEclipseProgressManager();
		/**
		 * install the riena ProgressProvider which handles creation of IProgressMontitor instances for scheduled jobs. riena provides a special monitor for
		 * background processing.
		 */
		final ProgressProviderBridge instance = ProgressProviderBridge.instance();
		Job.getJobManager().setProgressProvider(instance);
		instance.setVisualizerFactory(new ProgressVisualizerLocator());
	}

	/**
	 * @since 3.0
	 */
	protected void disableEclipseProgressManager() {
		if (RAPDetector.isRAPavailable()) {
			return;
		}
		//		//we need to get the instance first as this is the only way to lock/disable the singleton
		ProgressManager.getInstance();
		//		//shutting down means uninstalling ProgressProvider and removing all Job-Listeners
		ProgressManager.shutdownProgressManager();
	}

	/**
	 * Overwrite to create own application model
	 * 
	 * @return IApplicationModelProvider - root of the configured application model
	 * @since 4.0
	 */
	public IApplicationNode createModel() {
		final IApplicationNode applicationModel = new ApplicationNode(new NavigationNodeId(ApplicationNode.DEFAULT_APPLICATION_TYPEID));
		return applicationModel;
	}

	/**
	 * @since 1.2
	 */
	protected void createStartupNodes(final IApplicationNode applicationNode) {
		final INavigationNodeProvider navigationNodeProvider = NavigationNodeProvider.getInstance();
		final List<StartupNodeInfo> startups = navigationNodeProvider.getSortedStartupNodeInfos();
		for (final StartupNodeInfo startup : startups) {
			LOGGER.log(LogService.LOG_INFO, "creating " + startup.toString()); //$NON-NLS-1$
			applicationNode.create(new NavigationNodeId(startup.getId()));
		}
	}

	protected void initializeNode(final IApplicationNode model) {
		initializeModelDefaults(model);
	}

	protected void initializeModelDefaults(final IApplicationNode model) {
		initializeNodeDefaults(model);
	}

	protected void initializeNodeDefaults(final IApplicationNode node) {
		for (final ISubApplicationNode child : node.getChildren()) {
			initializeNodeDefaults(child);
		}
	}

	protected void initializeNodeDefaults(final ISubApplicationNode node) {
		for (final IModuleGroupNode child : node.getChildren()) {
			initializeNodeDefaults(child);
		}
	}

	protected void initializeNodeDefaults(final IModuleGroupNode node) {
		for (final IModuleNode child : node.getChildren()) {
			initializeNodeDefaults(child);
		}
	}

	protected void initializeNodeDefaults(final IModuleNode node) {

		initializeNodeDefaultIcon(node);
		//		for (ISubModuleNode child : node.getChildren()) {
		//			initializeNodeDefaults(child);
		//		}
	}

	//
	//	protected void initializeNodeDefaults(ISubModuleNode node) {
	//
	//		initializeNodeDefaultIcon(node);
	//		for (ISubModuleNode child : node.getChildren()) {
	//			initializeNodeDefaults(child);
	//		}
	//	}

	protected void initializeNodeDefaultIcon(final INavigationNode<?> node) {
		//		if (node.getIcon() == null) {
		//			node.setIcon("defaultNode"); //$NON-NLS-1$
		//		}
	}

	abstract protected Object createView(IApplicationContext context, IApplicationNode pNode) throws Exception;

	protected Object initializePerformLogin(final IApplicationContext context) throws Exception {

		initializeLoginViewDefinition();

		if (isDialogLogin(context)) {
			return performLogin(context);
		} else {
			return EXIT_OK;
		}
	}

	protected boolean isDialogLogin(final IApplicationContext context) {
		return loginDialogViewExtension != null;
	}

	protected boolean isSplashLogin(final IApplicationContext context) {
		return false;
	}

	protected Object doPerformLogin(final IApplicationContext context) {

		return EXIT_OK;
	}

	protected Object doPerformSplashLogin(final IApplicationContext context) {

		return EXIT_OK;
	}

	protected Object performLogin(final IApplicationContext context) throws Exception {

		if (isSplashLogin(context)) {
			return doPerformSplashLogin(context);
		} else {
			return doPerformLogin(context);
		}
	}

	@InjectExtension
	public void update(final ILoginDialogViewExtension[] data) {

		if (data.length > 0) {
			loginDialogViewExtension = data[0];
		}
	}

	protected void initializeLoginViewDefinition() {
		Wire.instance(this).andStart(Activator.getDefault().getContext());
	}

	public void configure() {
	}
}
