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
package org.eclipse.riena.example.client.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

public class UIProcessDemoSubModuleView extends SubModuleView {

	public final static String ID = UIProcessDemoSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, true));
		final Group group = createUIProcessGroup(parent);
		GridDataFactory.fillDefaults().applyTo(group);

	}

	private Group createUIProcessGroup(final Composite parent) {
		final Group group = UIControlsFactory.createGroup(parent, "&UIProcess visualization:"); //$NON-NLS-1$
		group.setLayout(new GridLayout(1, true));

		final Button startUIProcess = UIControlsFactory.createButton(group);
		int xHint = UIControlsFactory.getWidthHint(startUIProcess) + 10;
		GridDataFactory.fillDefaults().hint(xHint, SWT.DEFAULT).applyTo(startUIProcess);
		addUIControl(startUIProcess, "actionRidget"); //$NON-NLS-1$

		final Button startJob = UIControlsFactory.createButton(group);
		xHint = UIControlsFactory.getWidthHint(startJob);
		GridDataFactory.fillDefaults().hint(xHint, SWT.DEFAULT).applyTo(startJob);
		addUIControl(startJob, "actionRidgetJob"); //$NON-NLS-1$

		final Button startWithListener = UIControlsFactory.createButton(group);
		xHint = UIControlsFactory.getWidthHint(startWithListener);
		GridDataFactory.fillDefaults().hint(xHint, SWT.DEFAULT).applyTo(startWithListener);
		addUIControl(startWithListener, "actionRidgetListener"); //$NON-NLS-1$

		return group;
	}

}
