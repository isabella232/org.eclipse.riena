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
package org.eclipse.riena.security.common.authentication;

import java.io.Serializable;
import java.security.Principal;

import org.eclipse.core.runtime.Assert;

/**
 * The <code>SimplePrincipal</code> is a <code>java.security.Principal</code>.
 * 
 */
public class SimplePrincipal implements Principal, Serializable {

	private static final long serialVersionUID = 6896855188435044771L;

	private final String name;

	/**
	 * Create a <code>SimplePrincipal</code> with a given <code>name</code>.
	 * 
	 * @param name
	 *            the name of the principal
	 * @pre name != null && name.length() > 0
	 */
	public SimplePrincipal(final String name) {
		super();
		Assert.isTrue(name != null && name.length() > 0, "the Name must not be null or empty"); //$NON-NLS-1$
		this.name = name;
	}

	/**
	 * Create a <code>SimplePrincipal</code> with a given name. The name is a
	 * composite of <code>proxyName</code> and <code>representedName</code>.
	 * 
	 * @param proxyName
	 *            name of the proxy
	 * @param representedName
	 */
	public SimplePrincipal(final String proxyName, final String representedName) {
		this(proxyName + "," + representedName); //$NON-NLS-1$
	}

	/**
	 * @see java.security.Principal#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof SimplePrincipal)) {
			return false;
		}
		final SimplePrincipal that = (SimplePrincipal) object;

		if (this.name == null) {
			return that.name == null;
		}

		return this.name.equals(that.name);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (name == null) {
			return getClass().hashCode();
		} else {
			return getClass().hashCode() & name.hashCode();
		}
	}

	/**
	 * overwrite super.toString()
	 * 
	 * @return string representation of object
	 */
	@Override
	public String toString() {
		return "(" + this.getClass().getSimpleName() + ":" + name + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}
