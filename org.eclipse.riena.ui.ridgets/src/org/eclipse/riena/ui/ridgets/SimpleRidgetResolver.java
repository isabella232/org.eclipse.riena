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

import java.util.Map;

/**
 * @since 5.0
 * 
 */
public class SimpleRidgetResolver implements IRidgetResolver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.navigation.ui.controllers.IRidgetResolver#getRidget(java.lang.String, java.util.Map)
	 */
	public IRidget getRidget(final String id, final Map<String, IRidget> ridgets) {
		return ridgets.get(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.navigation.ui.controllers.IRidgetResolver#addRidget(java.lang.String, org.eclipse.riena.ui.ridgets.IRidget,
	 * org.eclipse.riena.ui.ridgets.IRidgetContainer, java.util.Map)
	 */
	public <R extends IRidget> IRidget addRidget(final String id, final R ridget, final IRidgetContainer toContainer, final Map<String, IRidget> ridgets) {
		return ridgets.put(id, ridget);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidgetResolver#removeRidget(java.lang.String, java.util.Map)
	 */
	public IRidget removeRidget(final String id, final Map<String, IRidget> ridgets) {
		return ridgets.remove(id);
	}
}
