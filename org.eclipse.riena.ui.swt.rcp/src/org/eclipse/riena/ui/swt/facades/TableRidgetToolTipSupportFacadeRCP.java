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

import org.eclipse.riena.ui.swt.facades.internal.TableRidgetToolTipSupport;

/**
 * Implements {@link TableRidgetToolTipSupportFacade} for RCP.
 */
public class TableRidgetToolTipSupportFacadeRCP extends TableRidgetToolTipSupportFacade {

	@Override
	public void disable() {
		TableRidgetToolTipSupport.disable();

	}

	@Override
	public void enableFor(final ColumnViewer viewer) {
		TableRidgetToolTipSupport.enableFor(viewer);
	}

	@Override
	public boolean isSupported() {
		return true;
	}

}
