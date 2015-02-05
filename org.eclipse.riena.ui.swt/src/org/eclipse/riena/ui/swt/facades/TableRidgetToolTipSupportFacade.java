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
package org.eclipse.riena.ui.swt.facades;

import org.eclipse.jface.viewers.ColumnViewer;

import org.eclipse.riena.ui.swt.facades.internal.ITableRidgetToolTipSupport;

/**
 * @since 4.0
 */
public abstract class TableRidgetToolTipSupportFacade {

	private static final TableRidgetToolTipSupportFacade INSTANCE = FacadeFactory
			.newFacade(TableRidgetToolTipSupportFacade.class);

	/**
	 * The applicable implementation of this class.
	 */
	public static final TableRidgetToolTipSupportFacade getDefault() {
		return INSTANCE;
	}

	/**
	 * Returns whether this kind of tool tips (JFace) are supported by the
	 * platform (RCP/RAP)
	 * 
	 * @return {@code true} JFace tool tips are supported; {@code false} not
	 *         supported
	 */
	public abstract boolean isSupported();

	/**
	 * Create the support of JFace tooltips for a given Table. Use the resulting {@link ITableRidgetToolTipSupport} to disable the JFace tool tips.
	 * 
	 * @param viewer
	 *            The viewer component of the table
	 * @return The instance of the tooltip support.
	 */
	public abstract ITableRidgetToolTipSupport enableFor(ColumnViewer viewer);

}
