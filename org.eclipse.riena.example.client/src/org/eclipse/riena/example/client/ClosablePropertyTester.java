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
package org.eclipse.riena.example.client;

import org.eclipse.core.expressions.PropertyTester;

import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;

public class ClosablePropertyTester extends PropertyTester {

	public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {
		if (property.equals("isClosable")) { //$NON-NLS-1$
			if (receiver instanceof SubModuleNode) {
				return ((SubModuleNode) receiver).isClosable();
			} else {
				return ((ModuleNode) receiver).isClosable();
			}
		}
		return false;
	}

}
