/*******************************************************************************
 * Copyright (c) 2007, 2015 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets.listener;

/**
 * {@link IProgressListener} listeners are notified when the browsers loading progress changed. The browser can fire multiple events with the same progress
 * values.
 * 
 * @since 6.1
 */
public interface IProgressListener {

	/**
	 * This method is called when the browser has finished a loading step.
	 * 
	 * @param event
	 *            a {@link ProgressEvent} describing the progress; never null
	 */
	void progressChanged(ProgressEvent event);

	/**
	 * This method is called when the browser has completed the document. After this call progress events can still occur and calling
	 * {@link #progressChanged(ProgressEvent)}.
	 * 
	 * @param event
	 *            a {@link ProgressEvent} describing the progress; never null
	 */
	void progressCompleted(ProgressEvent event);

}
