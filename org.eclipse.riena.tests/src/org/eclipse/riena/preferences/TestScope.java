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
package org.eclipse.riena.preferences;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScope;

/**
 *
 */
public class TestScope implements IScope {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.preferences.IScope#create(org.eclipse.core.runtime
	 * .preferences.IEclipsePreferences, java.lang.String)
	 */
	public IEclipsePreferences create(final IEclipsePreferences parent, final String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
