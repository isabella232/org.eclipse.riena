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
package org.eclipse.riena.example.client.views;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * A demo simulating how exceptions are handled.
 */
public class ExceptionTestView extends SubModuleView {

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setLayout(new GridLayout(1, true));

		UIControlsFactory.createLabel(parent, "Testing various Exceptions"); //$NON-NLS-1$

		final Button localNullPointer = UIControlsFactory.createButton(parent, "throw NullPointerException", //$NON-NLS-1$
				"localNullPointerAction"); //$NON-NLS-1$
		addUIControl(localNullPointer);

		final Button uiprocessNullPointer1 = UIControlsFactory.createButton(parent,
				"throw NullPointerException in UIProcess (runJob)", "uiprocessNullPointerActionRunJob"); //$NON-NLS-1$ //$NON-NLS-2$
		addUIControl(uiprocessNullPointer1);

		final Button uiprocessNullPointer2 = UIControlsFactory.createButton(parent,
				"throw NullPointerException in UIProcess (finalUpdateUI)", "uiprocessNullPointerActionFinalUpdateUI"); //$NON-NLS-1$ //$NON-NLS-2$
		addUIControl(uiprocessNullPointer2);

		final Button uiprocessNullPointer3 = UIControlsFactory.createButton(parent,
				"throw NullPointerException in UIProcess (updateUI)", "uiprocessNullPointerActionUpdateUI"); //$NON-NLS-1$ //$NON-NLS-2$
		addUIControl(uiprocessNullPointer3);

		final Button serverNullPointer = UIControlsFactory.createButton(parent,
				"throw NullPointerException on server", "serverNullPointerAction"); //$NON-NLS-1$ //$NON-NLS-2$
		addUIControl(serverNullPointer);
	}

}
