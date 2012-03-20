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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.example.client.controllers.MasterDetailsSubModuleController2;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.IMasterDetailsRidget;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;
import org.eclipse.riena.ui.swt.RienaMessageDialog;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Demonstrates use of a master/details ridget.
 * 
 * @see IMasterDetailsRidget
 * @see MasterDetailsSubModuleController2
 */
public class MasterDetailsSubModuleView2 extends SubModuleView {

	private Color colorLightBlue;

	public static final String ID = MasterDetailsSubModuleView2.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		colorLightBlue = new Color(parent.getDisplay(), 222, 232, 247);
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout());

		final Group groupMaster = createMasterDetails(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(groupMaster);
		final Group groupOptions = createOptions(parent);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(groupOptions);
	}

	@Override
	public void dispose() {
		colorLightBlue.dispose();
		super.dispose();
	}

	// helping methods
	//////////////////

	private Group createMasterDetails(final Composite parent) {
		final Group result = UIControlsFactory.createGroup(parent, "Master/Details:"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().margins(20, 20).applyTo(result);

		final MasterDetailsComposite mdComposite = new MasterDetailsComposite(result, SWT.NONE, SWT.TOP) {
			@Override
			protected Composite createButtons(final Composite parent) {
				final Composite result = UIControlsFactory.createComposite(parent, SWT.NONE);

				final RowLayout buttonLayout = new RowLayout(SWT.VERTICAL);
				buttonLayout.marginTop = 10;
				buttonLayout.fill = true;
				result.setLayout(buttonLayout);

				final Control btnNew = createButtonNew(result);
				addUIControl(btnNew, BIND_ID_NEW);

				final Control btnRemove = createButtonRemove(result);
				addUIControl(btnRemove, BIND_ID_REMOVE);

				return result;
			}

			@Override
			public boolean confirmRemove(final Object item) {
				final String title = "Confirm Remove"; //$NON-NLS-1$
				final String message = String.format("Delete '%s' ?", item.toString()); //$NON-NLS-1$
				final boolean result = RienaMessageDialog.openQuestion(getShell(), title, message);
				return result;
			}
		};
		mdComposite.setBackground(colorLightBlue);
		addUIControl(mdComposite, "master2"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, true).applyTo(mdComposite);

		final Composite details = mdComposite.getDetails();
		final GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.horizontalSpacing = 6;
		details.setLayout(gridLayout);
		{
			final Text txtFirst = UIControlsFactory.createText(details);
			txtFirst.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			mdComposite.addUIControl(txtFirst, "first"); //$NON-NLS-1$

			final Text txtLast = UIControlsFactory.createText(details);
			txtLast.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			mdComposite.addUIControl(txtLast, "last"); //$NON-NLS-1$

			final Button btnApply = new Button(details, SWT.PUSH | SWT.FLAT);
			mdComposite.addUIControl(btnApply, MasterDetailsComposite.BIND_ID_APPLY);
		}

		final Label lblStatus = UIControlsFactory.createLabel(result, "", SWT.CENTER, "lblStatus"); //$NON-NLS-1$ //$NON-NLS-2$
		lblStatus.setBackground(colorLightBlue);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(lblStatus);

		return result;
	}

	private Group createOptions(final Composite parent) {
		final Group result = UIControlsFactory.createGroup(parent, "Apply Enablement:"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(2).applyTo(result);

		UIControlsFactory.createButtonCheck(result, "No &Errors", "chkNoErrors"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonCheck(result, "No &Mandatories", "chkNoMandatories"); //$NON-NLS-1$ //$NON-NLS-2$

		return result;
	}
}
