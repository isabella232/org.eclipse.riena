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

import com.swtdesigner.SWTResourceManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.internal.demo.client.DemoClientUIControlsFactory;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 *
 */
public class CustomerSearchView extends SubModuleView {
	@Override
	public void basicCreatePartControl(final Composite parent) {

		parent.setLayout(new FillLayout(SWT.VERTICAL));
		final Composite container = new Composite(parent, SWT.NONE);

		final Table ergebnis = UIControlsFactory.createTable(container, SWT.BORDER | SWT.FULL_SELECTION, "result"); //$NON-NLS-1$
		ergebnis.setLinesVisible(true);
		ergebnis.setHeaderVisible(true);
		ergebnis.setBounds(30, 137, 703, 264);

		final Text suchName = UIControlsFactory.createText(container, SWT.BORDER, "searchLastName"); //$NON-NLS-1$
		suchName.setBounds(120, 25, 201, 32);

		final Label nameLabel = UIControlsFactory.createLabel(container, "Lastname", SWT.NONE); //$NON-NLS-1$
		nameLabel.setBounds(30, 31, 82, 26);

		final Button sucheStarten = UIControlsFactory.createButton(container, "&Search", "search"); //$NON-NLS-1$//$NON-NLS-2$
		sucheStarten.setBounds(345, 25, 82, 32);

		final Label treffer = UIControlsFactory.createLabel(container, "no", SWT.RIGHT, "hits"); //$NON-NLS-1$ //$NON-NLS-2$
		treffer.setFont(SWTResourceManager.getFont("Arial", 11, SWT.BOLD)); //$NON-NLS-1$
		treffer.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		treffer.setBounds(665, 110, 33, 19);

		final Button openCustomerButton = UIControlsFactory.createButton(container, "&Open", "open"); //$NON-NLS-1$ //$NON-NLS-2$
		openCustomerButton.setBounds(250, 460, 133, 34);

		final Button newCustomerButton = UIControlsFactory.createButton(container, "New", "new"); //$NON-NLS-1$ //$NON-NLS-2$
		newCustomerButton.setBounds(407, 460, 133, 34);

		final Label hitsLabel = UIControlsFactory.createLabel(container, "Hits", SWT.NONE); //$NON-NLS-1$
		hitsLabel.setFont(SWTResourceManager.getFont("Arial", 11, SWT.BOLD)); //$NON-NLS-1$
		hitsLabel.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		hitsLabel.setForeground(SWTResourceManager.getColor(0, 0, 1));
		hitsLabel.setBounds(703, 110, 30, 19);

		final Composite composite1 = DemoClientUIControlsFactory.createSeparator(container);
		composite1.setBounds(30, 90, 706, 2);

		final Composite composite2 = DemoClientUIControlsFactory.createSeparator(container);
		composite2.setBounds(30, 450, 706, 2);
	}
}
