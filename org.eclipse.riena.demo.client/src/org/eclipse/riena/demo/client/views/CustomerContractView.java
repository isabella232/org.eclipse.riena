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
package org.eclipse.riena.demo.client.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.internal.demo.client.DemoClientUIControlsFactory;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 *
 */
public class CustomerContractView extends SubModuleView {
	@Override
	public void basicCreatePartControl(final Composite parent) {

		final Composite container = new Composite(parent, SWT.NONE);
		parent.setLayout(new FillLayout(SWT.VERTICAL));

		final Label personalLabel = DemoClientUIControlsFactory.createSectionLabel(container, "Name"); //$NON-NLS-1$
		personalLabel.setBounds(25, 20, 66, 28);

		final Label lFirstname = UIControlsFactory.createLabel(container, "Firstname"); //$NON-NLS-1$
		lFirstname.setBounds(130, 23, 94, 22);

		final Text firstname = UIControlsFactory.createText(container, SWT.BORDER, "firstname"); //$NON-NLS-1$
		firstname.setBounds(236, 20, 181, 32);

		final Label lLastname = UIControlsFactory.createLabel(container, "Lastname"); //$NON-NLS-1$
		lLastname.setBounds(130, 63, 93, 23);

		final Text lastname = UIControlsFactory.createText(container, SWT.BORDER, "lastname"); //$NON-NLS-1$
		lastname.setBounds(235, 62, 182, 32);

		final Composite group = new Composite(container, SWT.NONE);
		group.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		group.setLayout(new FillLayout());
		group.setBounds(130, 120, 537, 310);

		final MasterDetailsComposite mdComposite = UIControlsFactory.createMasterDetails(group, "contracts"); //$NON-NLS-1$
		final Composite details = mdComposite.getDetails();
		details.setLayout(new GridLayout(2, false));

		UIControlsFactory.createLabel(details, "ContractNo"); //$NON-NLS-1$
		final Text contractNo = UIControlsFactory.createText(details, SWT.BORDER, "contractno"); //$NON-NLS-1$
		final GridData gdcontractNo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gdcontractNo.widthHint = 115;
		contractNo.setLayoutData(gdcontractNo);

		UIControlsFactory.createLabel(details, "Description"); //$NON-NLS-1$
		final Text description = UIControlsFactory.createText(details, SWT.BORDER, "description"); //$NON-NLS-1$
		final GridData gdDescription = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gdDescription.widthHint = 432;
		description.setLayoutData(gdDescription);

		UIControlsFactory.createLabel(details, "Value"); //$NON-NLS-1$
		final Text value = UIControlsFactory.createText(details, SWT.BORDER, "value"); //$NON-NLS-1$
		final GridData gdValue = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gdValue.widthHint = 114;
		value.setLayoutData(gdValue);

		UIControlsFactory.createLabel(details, "Status"); //$NON-NLS-1$
		final Text status = UIControlsFactory.createText(details, SWT.BORDER, "status"); //$NON-NLS-1$
		final GridData gdStatus = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gdStatus.widthHint = 114;
		status.setLayoutData(gdStatus);

		mdComposite.setBounds(5, 15, 520, 350);

		final Button saveButton = UIControlsFactory.createButton(container, "Save", "save_action"); //$NON-NLS-1$ //$NON-NLS-2$
		saveButton.setBounds(625, 465, 109, 38);

		final Label contractsLabel = DemoClientUIControlsFactory.createSectionLabel(container, "Contracts"); //$NON-NLS-1$
		contractsLabel.setBounds(25, 136, 81, 28);

		final Composite composite = DemoClientUIControlsFactory.createSeparator(container);
		composite.setBounds(30, 105, 706, 2);

		final Composite composite2 = DemoClientUIControlsFactory.createSeparator(container);
		composite2.setBounds(30, 450, 706, 2);
	}
}
