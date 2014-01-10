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
package org.eclipse.riena.ui.core.uiprocess;

/**
 * Container holding instances of {@link IUIMonitorContainer}
 */
public interface IUIMonitorContainer {

	/**
	 * Adds an {@link IUIMonitor} to the container.
	 * 
	 * @param uiMonitor
	 *            the monitor to add
	 */
	void addUIMonitor(IUIMonitor uiMonitor);

}
