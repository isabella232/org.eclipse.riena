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
package org.eclipse.riena.communication.client.ui.hooks;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.communication.core.hooks.CallContext;
import org.eclipse.riena.communication.core.hooks.ICallHook;
import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.RienaStatus;

/**
 * This call hook may be used at development time to get log information, if
 * calling a long running (remote) service on the user-interface thread, which
 * causes the ui to freeze. To use this hook it has to be configured as
 * extension:
 * <p>
 * {@code <extension
 *         point="org.eclipse.riena.communication.core.callHooks">
 *       <callHook
 *             class="org.eclipse.riena.communication.client.ui.hooks.UIThreadAlertCallHook"
 *             name="org.eclipse.riena.communication.client.ui.hooks.uiThreadAlertCallHook">
 *       </callHook>
 * </extension>}
 */
public class UIThreadAlertCallHook implements ICallHook {

	private final static Logger LOGGER = Log4r.getLogger(UIThreadAlertCallHook.class);

	public void beforeCall(final CallContext context) {

		if (RienaStatus.isDevelopment() && LOGGER.isLoggable(LogService.LOG_DEBUG) && Display.getCurrent() != null) {
			LOGGER.log(LogService.LOG_DEBUG, "Service " + context.getInterfaceName() + "." + context.getMethodName() //$NON-NLS-1$ //$NON-NLS-2$
					+ " is running on the user-interface thread. This may cause the ui freeze."); //$NON-NLS-1$
		}
	}

	public void afterCall(final CallContext context) {
		// do nothing
	}
}
