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

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

/**
 * Implements {@link ClipboardFacade} for RCP.
 */
public class ClipboardFacadeRCP extends ClipboardFacade {

	@Override
	public void cut(final Text control) {
		control.cut();
	}

	@Override
	public String getTextFromClipboard(final Display display) {
		String result = null;
		final Clipboard clipboard = new Clipboard(display);
		try {
			result = (String) clipboard.getContents(TextTransfer.getInstance());
		} finally {
			clipboard.dispose();
		}
		return result;
	}
}
