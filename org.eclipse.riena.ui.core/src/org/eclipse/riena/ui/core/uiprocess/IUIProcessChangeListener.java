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

import org.eclipse.core.runtime.IStatus;

/**
 * Callback interface that can be to {@link UIProcess} instances to listen for notifications when the UI process state changes.
 * 
 * @since 4.0
 * 
 */
public interface IUIProcessChangeListener {

	/**
	 * Notification that a UI process was waiting to run and has now been put in the sleeping state.
	 */
	void sleeping();

	/**
	 * Notification that a UI process (its underlying job) is being added to the queue of scheduled jobs. The parameter value includes the scheduling delay
	 * before the job should start running.
	 * 
	 * @param delay
	 *            the delay in milliseconds
	 */
	void scheduled(long delay);

	/**
	 * Notification that a UI process has completed execution, either due to cancelation, successful completion, or failure. The result status object indicates
	 * how the UI process finished, and the reason for failure, if applicable.
	 * 
	 * @param result
	 *            the execution result
	 */
	void done(IStatus result);

	/**
	 * Notification that a UI process was previously sleeping and has now been rescheduled to run.
	 */
	void awake();

	/**
	 * Notification that a UI process is about to be run.
	 */
	void aboutToRun();

	/**
	 * Notification that a UI process has started running.
	 */
	void running();

}
