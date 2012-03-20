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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.example.client.controllers.MasterDetailsSubModuleController3;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.IMasterDetailsRidget;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * A master/details ridget with 'direct writing' - edited details are applied
 * automatically to the model.
 * 
 * @see IMasterDetailsRidget
 * @see MasterDetailsSubModuleController3
 */
public class MasterDetailsSubModuleView3 extends SubModuleView {

	public static final String ID = MasterDetailsSubModuleView3.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		final FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		parent.setLayout(layout);
		createMasterDetails(parent);
	}

	// helping methods
	//////////////////

	private Group createMasterDetails(final Composite parent) {
		final Group result = UIControlsFactory.createGroup(parent,
				"Master/Details with 'auto apply' -- i.e. setDirectWriting(true):"); //$NON-NLS-1$
		final FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 20;
		layout.marginWidth = 20;
		result.setLayout(layout);

		final MasterDetailsComposite mdComposite = new MasterDetailsComposite(result, SWT.NONE, SWT.BOTTOM);
		final Composite details = mdComposite.getDetails();
		details.setLayout(new GridLayout(2, false));

		UIControlsFactory.createLabel(details, "First Name:"); //$NON-NLS-1$
		final Text txtFirst = UIControlsFactory.createText(details);
		txtFirst.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		mdComposite.addUIControl(txtFirst, "first"); //$NON-NLS-1$

		UIControlsFactory.createLabel(details, "Last Name:"); //$NON-NLS-1$
		final Text txtLast = UIControlsFactory.createText(details);
		txtLast.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		mdComposite.addUIControl(txtLast, "last"); //$NON-NLS-1$

		this.addUIControl(mdComposite, "master3"); //$NON-NLS-1$

		return result;
	}
}
