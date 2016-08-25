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
package org.eclipse.riena.internal.core;

import java.util.Arrays;
import java.util.Comparator;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.exception.IExceptionHandlerManager;
import org.eclipse.riena.core.service.Service;
import org.eclipse.riena.core.util.PropertiesUtils;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.internal.core.ignore.IgnoreFindBugs;

/**
 * Execute the startup actions
 */
public class StartupsSafeRunnable implements ISafeRunnable {

	private IRienaStartupExtension[] startups;

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), StartupsSafeRunnable.class);

	private static final Comparator<IRienaStartupExtension> STARTUPS_COMPERATOR = new Comparator<IRienaStartupExtension>() {

		public int compare(final IRienaStartupExtension startup1, final IRienaStartupExtension startup2) {
			return Integer.valueOf(getOrder(startup1)).compareTo(getOrder(startup2));
		}

		private int getOrder(final IRienaStartupExtension startup) {
			if (startup.getWhen() == IRienaStartupExtension.When.BEGINNING) {
				return 0;
			}
			if (startup.getWhen() == null) {
				return 1;
			}
			if (startup.getWhen() == IRienaStartupExtension.When.END) {
				return 2;
			}
			LOGGER.log(LogService.LOG_ERROR, "Unknown value for 'when' in startup definition: " + startup); //$NON-NLS-1$
			return 1;
		}
	};

	public void handleException(final Throwable exception) {

		final IExceptionHandlerManager manager = Service.get(IExceptionHandlerManager.class);
		if (manager != null) {
			manager.handleException(exception, "Error activating bundels.", LOGGER); //$NON-NLS-1$
			return;
		}
		LOGGER.log(LogService.LOG_ERROR, "Error activating bundels.", exception); //$NON-NLS-1$
	}

	/*
	 * Execute all startup actions
	 * 
	 * @see org.eclipse.core.runtime.ISafeRunnable#run()
	 */
	public void run() throws Exception {
		if (startups == null) {
			return;
		}
		Arrays.sort(startups, STARTUPS_COMPERATOR);

		for (final IRienaStartupExtension startup : startups) {
			handleRequiredBundle(startup);
			handleRunClass(startup);
			handleSelfActivation(startup);
		}
	}

	private void handleSelfActivation(final IRienaStartupExtension startup) throws BundleException {
		if (!startup.isActivateSelf()) {
			return;
		}
		final Bundle bundle = startup.getContributingBundle();
		if (bundle.getState() == Bundle.RESOLVED) {
			start(bundle);
		} else if (bundle.getState() == Bundle.STARTING && Constants.ACTIVATION_LAZY.equals(bundle.getHeaders().get(Constants.BUNDLE_ACTIVATIONPOLICY))) {
			try {
				bundle.start();
				LOGGER.log(LogService.LOG_INFO, "Startup <<lazy>>: '" + bundle.getSymbolicName() + "' succesful."); //$NON-NLS-1$ //$NON-NLS-2$
			} catch (final BundleException be) {
				LOGGER.log(LogService.LOG_WARNING,
						"Startup <<lazy>>: '" + bundle.getSymbolicName() //$NON-NLS-1$
								+ "' failed but may succeed (bundle state is in transition):\n\t\t" + be.getMessage() //$NON-NLS-1$
								+ (be.getCause() != null ? " cause: " + be.getCause() : "")); //$NON-NLS-1$ //$NON-NLS-2$
			} catch (final RuntimeException rte) {
				LOGGER.log(LogService.LOG_ERROR, "Startup <<lazy>>:: '" + bundle.getSymbolicName() //$NON-NLS-1$
						+ "' failed with exception.", rte); //$NON-NLS-1$
			}
		} else if (bundle.getState() == Bundle.INSTALLED) {
			LOGGER.log(LogService.LOG_ERROR, "Startup: '" + bundle.getSymbolicName() //$NON-NLS-1$
					+ "' failed. Startup extension is set but is only in state INSTALLED (not RESOLVED)."); //$NON-NLS-1$
		} else if (bundle.getState() == Bundle.ACTIVE) {
			LOGGER.log(LogService.LOG_DEBUG, "Startup: '" + bundle.getSymbolicName() + "' is already ACTIVE."); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private void handleRunClass(final IRienaStartupExtension startup) {
		final String runClassName = startup.getRunClassName();
		if (StringUtils.isEmpty(runClassName)) {
			return;
		}
		final Bundle bundle = startup.getContributingBundle();
		// try to load and execute the ´starter´ class
		try {
			startup.createRunner().run();
			LOGGER.log(LogService.LOG_INFO, "Startup: '" + bundle.getSymbolicName() + "' with starter '" //$NON-NLS-1$ //$NON-NLS-2$
					+ runClassName + "' succesful."); //$NON-NLS-1$
		} catch (final Exception e) {
			LOGGER.log(LogService.LOG_ERROR, "Startup: '" + bundle.getSymbolicName() + "' with starter '" //$NON-NLS-1$ //$NON-NLS-2$
					+ runClassName + "' failed.", e); //$NON-NLS-1$
		}
	}

	private void handleRequiredBundle(final IRienaStartupExtension startup) throws BundleException {
		final String[] bundleNames = PropertiesUtils.asArray(startup.getRequiredBundles());
		for (final String bundleName : bundleNames) {
			if (StringUtils.isEmpty(bundleName)) {
				continue;
			}
			final Bundle bundle = Platform.getBundle(bundleName);
			if (bundle != null) {
				if (bundle.getState() != Bundle.ACTIVE) {
					start(bundle);
				} else {
					LOGGER.log(LogService.LOG_INFO, "Startup required bundle: '" + bundleName //$NON-NLS-1$
							+ "' already started."); //$NON-NLS-1$
				}
			} else {
				LOGGER.log(LogService.LOG_WARNING, "Startup required bundle: '" + bundleName //$NON-NLS-1$
						+ "' not found."); //$NON-NLS-1$
			}
		}
	}

	private void start(final Bundle bundle) throws BundleException {
		if (bundle == null) {
			return;
		}
		try {
			bundle.start();
			LOGGER.log(LogService.LOG_INFO, "Startup: '" + bundle.getSymbolicName() + "' succesful."); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (final BundleException be) {
			LOGGER.log(LogService.LOG_ERROR, "Startup: '" + bundle.getSymbolicName() //$NON-NLS-1$
					+ "' failed with bundleexception.", be); //$NON-NLS-1$
		} catch (final RuntimeException rte) {
			LOGGER.log(LogService.LOG_ERROR, "Startup: '" + bundle.getSymbolicName() //$NON-NLS-1$
					+ "' failed with exception.", rte); //$NON-NLS-1$
		}
	}

	@IgnoreFindBugs(value = "EI_EXPOSE_REP2", justification = "deep cloning the ´startups´ is too expensive")
	@InjectExtension
	public void update(final IRienaStartupExtension[] startups) {
		this.startups = startups;
	}

}