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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

public class ChangeLogoSubModuleView extends SubModuleView {

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setLayout(new GridLayout(1, true));

		final Group group = UIControlsFactory.createGroup(parent, "This demonstrates how the application logo can be replaced at runtime");
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setLayout(new GridLayout(2, false));

		UIControlsFactory.createCombo(group, "logoChoice");

		UIControlsFactory.createButton(group, "Set Application Logo", "btnChangeLogo").setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, true, false));
	}
}
