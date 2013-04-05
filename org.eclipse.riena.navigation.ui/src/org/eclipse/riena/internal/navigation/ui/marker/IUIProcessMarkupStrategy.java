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
package org.eclipse.riena.internal.navigation.ui.marker;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.ui.core.uiprocess.UIProcess;

/**
 * Interface for strategies marking nodes when a {@link UIProcess} finished.
 */
public interface IUIProcessMarkupStrategy {

	/**
	 * Adds the given marker to the given node and to all parent nodes.
	 * 
	 * @param baseNode
	 *            the node where the {@link UIProcess} was started
	 * @param marker
	 *            the marker to add
	 */
	void applyUIProcessMarker(INavigationNode<?> baseNode, IMarker marker);

}
