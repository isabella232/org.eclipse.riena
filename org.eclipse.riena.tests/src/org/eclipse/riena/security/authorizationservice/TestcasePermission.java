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

import java.security.Permission;

/**
 * 
 */
public class TestcasePermission extends Permission {

	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 */
	public TestcasePermission(final String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.security.Permission#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof TestcasePermission) {
			if (((TestcasePermission) obj).getName().equals(this.getName())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.security.Permission#getActions()
	 */
	@Override
	public String getActions() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.security.Permission#hashCode()
	 */
	@Override
	public int hashCode() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.security.Permission#implies(java.security.Permission)
	 */
	@Override
	public boolean implies(final Permission permission) {
		if (permission.equals(this)) {
			return true;
		}
		return false;
	}

}
