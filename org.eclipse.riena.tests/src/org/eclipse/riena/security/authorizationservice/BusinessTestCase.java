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
package org.eclipse.riena.security.authorizationservice;

import org.eclipse.riena.security.common.authorization.Sentinel;

/**
 * 
 */
public class BusinessTestCase {

	boolean hasPermission() {
		return Sentinel.checkAccess(new TestcasePermission("testPerm"));
	}
}
