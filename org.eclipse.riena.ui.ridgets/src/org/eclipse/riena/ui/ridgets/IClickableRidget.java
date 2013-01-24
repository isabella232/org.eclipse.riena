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
package org.eclipse.riena.ui.ridgets;

import org.eclipse.riena.ui.ridgets.listener.ClickEvent;
import org.eclipse.riena.ui.ridgets.listener.IClickListener;

/**
 * A Ridget with support for mouse clicks.
 * 
 * @since 4.0
 */
public interface IClickableRidget {

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the Ridget is clicked. The listener will receive information about
	 * the mouse button and the Ridget via a {@link ClickEvent}.
	 * <p>
	 * Table also adds information about the column and the row.
	 * 
	 * @param listener
	 *            a non-null {@link IClickListener} instance
	 * @since 4.0
	 */
	void addClickListener(IClickListener listener);

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the Ridget is clicked.
	 * 
	 * @param listener
	 *            a non-null {@link IClickListener} instance
	 * @since 4.0
	 */
	void removeClickListener(IClickListener listener);

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the bound control is double-clicked.
	 * <p>
	 * Adding the same listener several times has no effect.
	 * 
	 * @param listener
	 *            a non-null {@link IActionListener} instance
	 * @throws RuntimeException
	 *             if listener is null
	 */
	void addDoubleClickListener(IActionListener listener);

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the bound control is double-clicked.
	 * 
	 * @param listener
	 *            a non-null {@link IActionListener} instance
	 * @throws RuntimeException
	 *             if listener is null
	 */
	void removeDoubleClickListener(IActionListener listener);

}
