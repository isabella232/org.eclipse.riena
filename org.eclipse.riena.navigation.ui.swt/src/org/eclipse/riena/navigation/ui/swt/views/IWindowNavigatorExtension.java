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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.riena.core.injector.extension.CreateLazy;
import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;

/**
 * The definition for the window navigator of the main window (see also
 * <code>IWindowNavigator</code>).
 * 
 * @since 4.0
 */
@ExtensionInterface(id = "windowNavigator")
public interface IWindowNavigatorExtension {

	@CreateLazy
	@MapName(value = "navigatorClass")
	IWindowNavigator createWindowNavigator();

}
