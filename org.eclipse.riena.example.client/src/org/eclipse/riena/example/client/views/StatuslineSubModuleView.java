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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * View of the sub-module that is an example how to display something in the
 * status line.
 */
public class StatuslineSubModuleView extends SubModuleView {

	public static final String ID = StatuslineSubModuleView.class.getName();

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.SubModuleView#basicCreatePartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));

		final GridDataFactory fillFactory = GridDataFactory.fillDefaults();

		final Group messageGroup = createMessageGroup(parent);
		fillFactory.applyTo(messageGroup);

		final Group numberGroup = createNumberGroup(parent);
		fillFactory.applyTo(numberGroup);

	}

	/**
	 * Creates the group with all the controls to regulate and show the message
	 * in the status line.
	 * 
	 * @param parent
	 * @return message group
	 */
	private Group createMessageGroup(final Composite parent) {

		final Group group = UIControlsFactory.createGroup(parent, "Message:"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(2).applyTo(group);

		Label label = UIControlsFactory.createLabel(group, "Text:"); //$NON-NLS-1$
		addUIControl(label, "textLabel"); //$NON-NLS-1$

		final Text text = new Text(group, SWT.BORDER | SWT.SINGLE);
		GridData gridData = new GridData();
		gridData.widthHint = 200;
		text.setLayoutData(gridData);
		addUIControl(text, "messageText"); //$NON-NLS-1$

		label = UIControlsFactory.createLabel(group, "Severity:"); //$NON-NLS-1$
		addUIControl(label, "severityLabel"); //$NON-NLS-1$

		final Combo severityCombo = UIControlsFactory.createCombo(group);
		addUIControl(severityCombo, "severity"); //$NON-NLS-1$

		final Button showBtn = UIControlsFactory.createButton(group);
		showBtn.setText("Show"); //$NON-NLS-1$
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.BEGINNING;
		gridData.horizontalSpan = 2;
		showBtn.setLayoutData(gridData);
		addUIControl(showBtn, "showMessage"); //$NON-NLS-1$

		return group;

	}

	/**
	 * Creates the group with all the controls to regulate and show the number
	 * in the status line.
	 * 
	 * @param parent
	 * @return number group
	 */
	private Group createNumberGroup(final Composite parent) {

		final Group group = UIControlsFactory.createGroup(parent, "Number:"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(2).applyTo(group);

		final Label label = UIControlsFactory.createLabel(group, "Number:"); //$NON-NLS-1$
		addUIControl(label, "numberLabel"); //$NON-NLS-1$

		final Text text = new Text(group, SWT.BORDER | SWT.SINGLE);
		GridData gridData = new GridData();
		gridData.widthHint = 42;
		text.setLayoutData(gridData);
		addUIControl(text, "numberText"); //$NON-NLS-1$

		final Button showBtn = UIControlsFactory.createButton(group);
		showBtn.setText("Show"); //$NON-NLS-1$
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.BEGINNING;
		gridData.horizontalSpan = 2;
		showBtn.setLayoutData(gridData);
		addUIControl(showBtn, "showNumber"); //$NON-NLS-1$

		return group;

	}

}
