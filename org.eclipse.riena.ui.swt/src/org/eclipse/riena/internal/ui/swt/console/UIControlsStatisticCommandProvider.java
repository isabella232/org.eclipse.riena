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
package org.eclipse.riena.internal.ui.swt.console;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.ui.swt.utils.UIControlsCounter;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Provider implementation for the "controlstats" console command. Prints usage
 * statistic of {@link Control}s created by {@link UIControlsFactory}
 */
public class UIControlsStatisticCommandProvider implements CommandProvider {

	public String getHelp() {
		return "---Control Usage Statistics Of UIControlsFactory---\n\tcontrolstats - show usage of control types\n"; //$NON-NLS-1$ 
	}

	/**
	 * Handles the "controlstats" command of the console
	 * 
	 * @param ci
	 *            - the {@link CommandInterpreter}
	 * @throws Exception
	 */
	public void _controlstats(final CommandInterpreter ci) throws Exception {
		if (!UIControlsCounter.isRequested()) {
			System.out.println("Usage statistics is deactivated. Please set the system property '" //$NON-NLS-1$
					+ UIControlsCounter.CONTROL_STATS_PROPERTY_NAME + "' to 'true'."); //$NON-NLS-1$
		} else {
			System.out.println(UIControlsCounter.getInstance().getControlUsageStatistics());
		}
	}

}
