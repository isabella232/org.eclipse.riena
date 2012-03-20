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
package org.eclipse.riena.navigation.extension;

import org.eclipse.riena.navigation.IModuleNodeExtension;

/**
 * Implementation of the interface {@code IModuleNode2Extension}. This is only
 * used for conversion of the legacy extension {@link IModuleNodeExtension}.
 */
public class ModuleNode2Extension extends Node2Extension implements IModuleNode2Extension {

	private boolean closable;

	/**
	 * Creates a new instance of {@code ModuleNode2Extension} and sets the
	 * default value of the property {@code closable}.
	 */
	public ModuleNode2Extension() {
		closable = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISubModuleNode2Extension[] getChildNodes() {
		return (ISubModuleNode2Extension[]) super.getChildNodes();
	}

	/**
	 * {@inheritDoc}
	 */
	public ISubModuleNode2Extension[] getSubModuleNodes() {
		return getChildNodes();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isClosable() {
		return closable;
	}

	/**
	 * Sets whether the user can close the module.
	 * 
	 * @param closable
	 *            {@code true} if this module is closable, {@code false}
	 *            otherwise. Default is {@code true}.
	 */
	public void setClosable(final boolean closable) {
		this.closable = closable;
	}

}
