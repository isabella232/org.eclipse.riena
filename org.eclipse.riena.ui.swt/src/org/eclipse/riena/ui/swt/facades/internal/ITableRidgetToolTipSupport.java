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
package org.eclipse.riena.ui.swt.facades.internal;

import org.eclipse.jface.viewers.ColumnViewer;

/**
 * Interface for jface tooltips on a TableRidget.
 * 
 * @since 6.1
 */
public interface ITableRidgetToolTipSupport {

	/**
	 * Disable the JFace tooltips on the TableRidget.
	 */
	void disableSupport();

	/**
	 * Enable the JFace tooltip support for the ColumnViewer.
	 * 
	 * @param viewer
	 *            the new ColumnViewer
	 */
	void enableSupport(ColumnViewer viewer);
}