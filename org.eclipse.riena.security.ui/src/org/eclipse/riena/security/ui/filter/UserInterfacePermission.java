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
package org.eclipse.riena.security.ui.filter;

import java.security.Permission;

import org.eclipse.riena.ui.filter.impl.UIFilter;

/**
 * Default Permission related to {@link UIFilter}s
 * 
 */
public class UserInterfacePermission extends Permission {
	private static final long serialVersionUID = 7471224661472548403L;

	private final String action;

	public UserInterfacePermission(final String name, final String action) {
		super(name);
		this.action = action;
	}

	@Override
	public boolean equals(final Object p) {
		if (!(p instanceof UserInterfacePermission)) {
			return false;
		}
		final UserInterfacePermission np = (UserInterfacePermission) p;
		return np.getName().equals(getName()) && np.getActions().equals(getActions());
	}

	@Override
	public String getActions() {
		return action;
	}

	@Override
	public int hashCode() {
		return getName().hashCode() + action.hashCode();
	}

	@Override
	public boolean implies(final Permission p) {
		return equals(p);
	}

}