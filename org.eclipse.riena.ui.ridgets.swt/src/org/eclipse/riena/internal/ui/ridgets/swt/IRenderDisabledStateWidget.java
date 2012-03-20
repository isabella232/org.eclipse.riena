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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;

/**
 * Extension to configure all the widgets that render the disabled state by
 * their own.
 */
@ExtensionInterface(id = "renderDisabledStateWidgets")
public interface IRenderDisabledStateWidget {

	/**
	 * Returns the class of widget that renders the disabled state by its own.
	 * 
	 * @return class of widget
	 */
	@MapName("class")
	Class<? extends Widget> getWidgetClass();

}
