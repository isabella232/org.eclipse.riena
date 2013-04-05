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

import org.eclipse.riena.core.injector.extension.DefaultValue;
import org.eclipse.riena.core.injector.extension.ExtensionInterface;

/**
 * Extension interface for the extension point "permissionFilterMapping"
 * 
 */
@ExtensionInterface(id = "permissionFilterMapping")
public interface IPermissionFilterMappingExtension {

	@DefaultValue(value = "org.eclipse.riena.security.navigation.filter.UserInterfacePermission")
	Class<? extends Permission> getPermissionClass();

	String getPermissionName();

	String getPermissionAction();

	String getFilterID();

}