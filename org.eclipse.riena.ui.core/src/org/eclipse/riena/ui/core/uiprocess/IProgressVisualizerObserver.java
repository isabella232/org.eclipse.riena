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
 * Instances of this class usually are managed by instances of
 * {@link IProgressVisualizer} and get called as delegates for dispatching
 * {@link IUIMonitor} calls. This convenience interface adds the current
 * {@link IProgressVisualizer} as a parameter to the {@link IUIMonitor}
 * callbacks.
 */
public interface IProgressVisualizerObserver {

	/**
	 * Adds the {@link IProgressVisualizer}
	 * 
	 * @param visualizer
	 *            the current visualizer
	 */
	void addProgressVisualizer(IProgressVisualizer visualizer);

	/**
	 * Removes the {@link IProgressVisualizer}
	 * 
	 * @param visualizer
	 *            the current visualizer
	 */
	void removeProgressVisualizer(IProgressVisualizer visualizer);

	/**
	 * @see IUIMonitor#updateProgress(int)
	 * @see ProcessInfo#setProgresStartegy(org.eclipse.riena.ui.core.uiprocess.ProcessInfo.ProgresStrategy)
	 * 
	 * @param visualizer
	 *            the current visualizer
	 * @param progress
	 *            the current progress. consider that the progress can grow in
	 *            two different ways. (unit based and cumulative)
	 */
	void updateProgress(IProgressVisualizer visualizer, int progress);

	/**
	 * @see IUIMonitor#initialUpdateUI(int)
	 * @see ProcessInfo#setProgresStartegy(org.eclipse.riena.ui.core.uiprocess.ProcessInfo.ProgresStrategy)
	 * 
	 * @param visualizer
	 *            the current visualizer
	 * @param totalWork
	 *            total number of work units
	 */
	void initialUpdateUI(IProgressVisualizer visualizer, int totalWork);

	/**
	 * @see IUIMonitor#finalUpdateUI()
	 * @param visualizer
	 *            the current visualizer
	 * 
	 */
	void finalUpdateUI(IProgressVisualizer visualizer);
}
