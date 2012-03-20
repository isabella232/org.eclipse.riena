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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.PlatformObject;

/**
 * Delegates {@link IUIMonitor}-calls to the {@link IProgressVisualizerObserver}
 * registered instances
 * 
 * @see IProgressVisualizer
 */
public class ProgressVisualizer extends PlatformObject implements IProgressVisualizer {

	private final Collection<IProgressVisualizerObserver> observers;
	private ProcessInfo processInfo;

	/**
	 * creates a new instance of {@link ProgressVisualizer}
	 */
	public ProgressVisualizer() {
		observers = new ArrayList<IProgressVisualizerObserver>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.core.uiprocess.IUIProcess#finalUpdateUI()
	 */
	public void finalUpdateUI() {
		final Collection<IProgressVisualizerObserver> currentObservers = new ArrayList<IProgressVisualizerObserver>(
				observers);
		if (currentObservers.size() > 0) {
			for (final IProgressVisualizerObserver anObserver : currentObservers) {
				anObserver.finalUpdateUI(this);
				anObserver.removeProgressVisualizer(this);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.core.uiprocess.IUIProcess#initialUpdateUI(int)
	 */
	public void initialUpdateUI(final int totalWork) {
		final Collection<IProgressVisualizerObserver> currentObservers = new ArrayList<IProgressVisualizerObserver>(
				observers);
		for (final IProgressVisualizerObserver anObserver : currentObservers) {
			anObserver.addProgressVisualizer(this);
			anObserver.initialUpdateUI(this, totalWork);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.core.uiprocess.IUIProcess#updateProgress(int)
	 */
	public void updateProgress(final int progress) {
		final Collection<IProgressVisualizerObserver> currentObservers = new ArrayList<IProgressVisualizerObserver>(
				observers);
		for (final IProgressVisualizerObserver anObserver : currentObservers) {
			anObserver.updateProgress(this, progress);
		}
	}

	/**
	 * @return the processInfo describing the {@link UIProcess}
	 */
	public ProcessInfo getProcessInfo() {
		return processInfo;
	}

	/**
	 * @param processInfo
	 *            the processInfo to set
	 */
	public void setProcessInfo(final ProcessInfo processInfo) {
		this.processInfo = processInfo;
	}

	/**
	 * @see org.eclipse.riena.ui.core.uiprocess.IProgressVisualizer#addObserver(org
	 *      .eclipse.riena.ui.core.uiprocess.IProgressVisualizerObserver)
	 */
	public void addObserver(final IProgressVisualizerObserver anObserver) {
		if (anObserver != null) {
			observers.add(anObserver);
		}

	}

	public void removeObserver(final IProgressVisualizerObserver anObserver) {
		observers.remove(anObserver);
	}

	@Override
	public Object getAdapter(final Class adapter) {
		if (adapter == IProcessInfoAware.class || adapter == IProgressVisualizer.class) {
			return this;
		}
		return super.getAdapter(adapter);
	}

}
