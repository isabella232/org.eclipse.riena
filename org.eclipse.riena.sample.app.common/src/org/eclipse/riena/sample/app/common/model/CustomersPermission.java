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
package org.eclipse.riena.sample.app.common.model;

import java.security.Permission;
import java.util.StringTokenizer;

/**
 * Permission for the Customer class to allow or disallow certain methods
 * 
 */
public class CustomersPermission extends Permission {

	private static final long serialVersionUID = -606601630674230084L;

	private String actions;
	private String[] actionList;

	private CustomersPermission() { // for hessian only
		super(""); //$NON-NLS-1$
	}

	public CustomersPermission(final String name, final String actions) {
		super(name);
		this.actions = actions;
		actionList = makeActionList(actions);
	}

	private String[] makeActionList(final String actions) {
		final StringTokenizer st = new StringTokenizer(actions, ",", false); //$NON-NLS-1$
		final String[] list = new String[st.countTokens()];
		int i = 0;
		while (st.hasMoreTokens()) {
			list[i++] = st.nextToken();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.security.Permission#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof CustomersPermission) {
			final CustomersPermission cp = (CustomersPermission) obj;
			if (cp.getName().equals(this.getName())) {
				final String[] l2 = makeActionList(cp.getActions());
				if (actionList.length == l2.length) {
					for (final String element : actionList) {
						boolean found = false;
						for (int x = 0; x < l2.length && !found; x++) {
							if (element.equals(l2[x])) {
								found = true;
							}
						}
						if (!found) {
							return false;
						}
					}
					return true;
				}
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
		return actions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.security.Permission#hashCode()
	 */
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.security.Permission#implies(java.security.Permission)
	 */
	@Override
	public boolean implies(final Permission permission) {
		if (permission instanceof CustomersPermission) {
			final CustomersPermission cp = (CustomersPermission) permission;
			if (getName().equals(cp.getName())) {
				final String[] l2 = makeActionList(cp.getActions());
				if (l2.length <= actionList.length) {
					for (final String element : l2) {
						boolean found = false;
						for (int x = 0; x < actionList.length && !found; x++) {
							if (element.equals(actionList[x])) {
								found = true;
							}
						}
						if (!found) {
							return false;
						}
					}
					return true;
				}
			}
		}
		return false;
	}

}
