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

import org.eclipse.riena.navigation.IModuleGroupNodeExtension;

/**
 * Implementation of the interface {@code IModuleGroupNode2Extension}. This is
 * only used for conversion of the legacy extension
 * {@link IModuleGroupNodeExtension}.
 */
public class ModuleGroupNode2Extension extends Node2Extension implements IModuleGroupNode2Extension {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IModuleNode2Extension[] getChildNodes() {
		return (IModuleNode2Extension[]) super.getChildNodes();
	}

	/**
	 * {@inheritDoc}
	 */
	public IModuleNode2Extension[] getModuleNodes() {
		return getChildNodes();
	}

}
