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
package org.eclipse.riena.example.client.views;

import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.MessageBox;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Example view with only one message box.
 */
public class AllowActivateSubModuleView extends SubModuleView {

	@Override
	protected void basicCreatePartControl(final Composite parent) {

		final MessageBox messageBox = UIControlsFactory.createMessageBox(parent);
		addUIControl(messageBox, "messageBox"); //$NON-NLS-1$

	}

}
