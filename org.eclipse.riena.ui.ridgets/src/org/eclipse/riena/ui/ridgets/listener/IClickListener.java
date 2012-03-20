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
package org.eclipse.riena.ui.ridgets.listener;

/**
 * A listener that is notified when something is clicked. The listener will
 * receive information about the mouse click in a {@link ClickEvent} instance.
 * 
 * @see ClickEvent
 * 
 * @since 2.0
 */
public interface IClickListener {

	/**
	 * This method is invoked when a click is registered.
	 * 
	 * @param event
	 *            a {@link ClickEvent} instance; never null. Contains
	 *            information about the mouse click.
	 * @see ClickEvent
	 */
	void callback(ClickEvent event);

}
