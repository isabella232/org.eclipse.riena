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
package org.eclipse.riena.example.client;

import org.eclipse.core.expressions.PropertyTester;

import org.eclipse.riena.navigation.ui.swt.ApplicationUtility;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;

/**
 * Tests whether the Fastview property {@link LnfKeyConstants#NAVIGATION_FAST_VIEW} is enabled.
 */
public class FastViewPropertyTester extends PropertyTester {

	public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {
		return ApplicationUtility.isNavigationFastViewEnabled();
	}

}
