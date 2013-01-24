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
package org.eclipse.riena.ui.swt.facades;

import org.eclipse.jface.viewers.ColumnViewer;

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

	public abstract void disable();

	public abstract void enableFor(ColumnViewer viewer);

}
