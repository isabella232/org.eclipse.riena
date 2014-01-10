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
package org.eclipse.riena.ui.swt.facades;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

/**
 * Facade for the SWT Clipboard class and copy / cut / paste invocation on
 * widgets.
 * 
 * @since 3.0
 */
public abstract class ClipboardFacade {

	private static final ClipboardFacade INSTANCE = FacadeFactory.newFacade(ClipboardFacade.class);

	/**
	 * The applicable implementation of this class.
	 */
	public static final ClipboardFacade getDefault() {
		return INSTANCE;
	}

	public abstract void cut(Text control);

	/**
	 * Get the contents of the system clipboard as a String
	 * 
	 * @param display
	 *            a {@link Display} instance; never null
	 * @return the String obtained from the clipboard or null if no data of this
	 *         type is available
	 */
	public abstract String getTextFromClipboard(Display display);
}
