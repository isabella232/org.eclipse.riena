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
package org.eclipse.riena.ui.core.marker;

import org.eclipse.riena.core.marker.IMarker;

/**
 * A marker that supplies a message explaining it. While the marker only effects
 * the UI widget bound to the Ridget to which it the marker is added the message
 * can be displayed by some other part of the UI. An example is a descriptive
 * message about a textfield which is marked as error or as mandatory that is
 * shown in the status line when the textfield is focused.
 */
public interface IMessageMarker extends IMarker {

	/**
	 * Name of the marker attribute that holds the message.
	 */
	String MESSAGE = "message"; //$NON-NLS-1$

	/**
	 * Returns a message that explains the marker.
	 * 
	 * @return A message explaining the marker; never null
	 */
	String getMessage();

}
