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
package org.eclipse.riena.internal.ui.ridgets.swt.optional;

import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.widgets.grid.Grid;

/**
 * A viewer of a {@link Grid} control that also know the corresponding Ridget.
 */
public class GridRidgetGridTableViewer extends GridTableViewer {

	private final GridRidget gridRidget;

	/**
	 * Creates a grid viewer on the grid control that's binded with the given
	 * Ridget.
	 * 
	 * @param gridRidget
	 *            Ridget of the {@link Grid}
	 */
	public GridRidgetGridTableViewer(final GridRidget gridRidget) {
		super(gridRidget.getUIControl());
		this.gridRidget = gridRidget;
	}

	public GridRidget getGridRidget() {
		return gridRidget;
	}

}
