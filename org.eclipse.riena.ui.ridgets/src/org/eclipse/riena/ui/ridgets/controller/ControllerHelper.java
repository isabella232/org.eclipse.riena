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
package org.eclipse.riena.ui.ridgets.controller;

import java.util.Collection;

import org.eclipse.riena.ui.ridgets.AbstractRidget;
import org.eclipse.riena.ui.ridgets.IComplexRidget;
import org.eclipse.riena.ui.ridgets.IRidget;

/**
 * @since 6.0
 * 
 */
public class ControllerHelper {

	/**
	 * Checks all ridgets recursively in this controller, if a previous call to setFocus() failed and tries to set the focus again.
	 * <p>
	 * SWT has the limitation that it doesn't set the focus if the parent composite is disabled. Therefore we have to try to restore the first previous call to
	 * setFocus(), while the view is blocked.
	 * 
	 * @param collection
	 *            the collection to check
	 * @param isBlocked
	 *            <code>true</code> if the parent controller is blocked
	 * @see IController#setBlocked(boolean)
	 */
	public static void restoreFocusRequestFromRidget(final Collection<? extends IRidget> collection, final boolean isBlocked) {
		for (final IRidget ridget : collection) {
			if (ridget instanceof IComplexRidget) {
				restoreFocusRequestFromRidget(((IComplexRidget) ridget).getRidgets(), isBlocked);
			} else {
				if (ridget instanceof AbstractRidget) {
					if (((AbstractRidget) ridget).isRetryRequestFocus()) {
						if (!isBlocked) {
							ridget.requestFocus();
						}
						((AbstractRidget) ridget).setRetryRequestFocus(false);
					}
				}
			}
		}
	}
}
