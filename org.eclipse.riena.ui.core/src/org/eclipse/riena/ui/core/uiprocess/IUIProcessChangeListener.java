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
package org.eclipse.riena.ui.core.uiprocess;

/**
 * Callback interface that can be to {@link UIProcess} instances to listen for notifications when the UI process state changes.
 * 
 * @since 4.0
 * 
 */
public interface IUIProcessChangeListener {

	/**
	 * The first update of the UI. Synchronized with UI-Thread
	 * 
	 * @param totalWork
	 *            the total number of work units into which the main task is been subdivided.
	 */
	void afterInitialUpdateUI(int totalWork);

	/**
	 * Called when the process has finished. Synchronized with UI-Thread
	 */
	void afterFinalUpdateUI();

}
