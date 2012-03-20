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
package org.eclipse.riena.sample.app.client.rcpmail;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

public class MessagePopupAction extends Action {

	private final IWorkbenchWindow window;

	MessagePopupAction(final String text, final IWorkbenchWindow window) {
		super(text);
		this.window = window;
		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_OPEN_MESSAGE);
		// Associate the action with a pre-defined command, to allow key bindings.
		setActionDefinitionId(ICommandIds.CMD_OPEN_MESSAGE);
		setImageDescriptor(org.eclipse.riena.sample.app.client.rcpmail.Activator
				.getImageDescriptor("/icons/sample3.gif")); //$NON-NLS-1$
	}

	@Override
	public void run() {
		MessageDialog.openInformation(window.getShell(), "Open", "Open Message Dialog!"); //$NON-NLS-1$ //$NON-NLS-2$
	}
}