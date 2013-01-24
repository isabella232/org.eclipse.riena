/*******************************************************************************
 * Copyright (c) 2007, 2013 compeople AG and others.
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
 * A container managing instances of {@link IProgressVisualizerObserver}.
 * {@link IUIMonitor} callbacks usually get delegated here to the
 * {@link IProgressVisualizerObserver}s.
 */
public interface IProgressVisualizer extends IUIMonitor, IProcessInfoAware {

	/**
	 * Adds the {@link IProgressVisualizerObserver} to the container
	 * 
	 * @param observer
	 *            the {@code IProgressVisualizerObserver} to add
	 */
	void addObserver(IProgressVisualizerObserver observer);

	/**
	 * Removes the {@link IProgressVisualizerObserver} from the container
	 * 
	 * @param observer
	 *            the {@code IProgressVisualizerObserver} to remove
	 */
	void removeObserver(IProgressVisualizerObserver observer);

}
