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
package org.eclipse.riena.demo.client.views;

import com.swtdesigner.SWTResourceManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.internal.demo.client.DemoClientUIControlsFactory;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * EmailView
 */
public class EmailView extends SubModuleView {
	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setLayout(new FillLayout(SWT.VERTICAL));

		final Font boldFont11 = SWTResourceManager.getFont("Arial", 11, SWT.BOLD); //$NON-NLS-1$
		final Font normalFont11 = SWTResourceManager.getFont("Arial", 11, SWT.NORMAL); //$NON-NLS-1$
		final Composite container = new Composite(parent, SWT.NONE);

		final Table table = UIControlsFactory.createTable(container, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION,
				"emailsTable"); //$NON-NLS-1$
		table.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL)); //$NON-NLS-1$
		table.setBackground(SWTResourceManager.getColor(255, 255, 254));
		table.setLocation(25, 23);
		table.setSize(703, 190);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		final TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(188);

		final TableColumn tableColumn1 = new TableColumn(table, SWT.NONE);
		tableColumn1.setWidth(349);

		final TableColumn tableColumn2 = new TableColumn(table, SWT.NONE);
		tableColumn2.setWidth(143);

		Label l = UIControlsFactory.createLabel(container, "Subject", SWT.WRAP); //$NON-NLS-1$
		l.setLocation(25, 254);
		l.setSize(58, 18);
		l.setFont(boldFont11);
		l.setForeground(SWTResourceManager.getColor(1, 1, 1));

		final Label subjectLabel = UIControlsFactory.createLabel(container, "", SWT.NONE, "emailSubject"); //$NON-NLS-1$ //$NON-NLS-2$
		subjectLabel.setLocation(103, 254);
		subjectLabel.setSize(619, 17);
		subjectLabel.setForeground(SWTResourceManager.getColor(1, 1, 1));

		l = UIControlsFactory.createLabel(container, "From", SWT.WRAP); //$NON-NLS-1$
		l.setLocation(25, 278);
		l.setSize(40, 18);
		l.setFont(boldFont11);
		l.setForeground(SWTResourceManager.getColor(1, 1, 1));

		final Label fromLabel = UIControlsFactory.createLabel(container, "", SWT.NONE, "emailFrom"); //$NON-NLS-1$ //$NON-NLS-2$
		fromLabel.setLocation(103, 277);
		fromLabel.setSize(302, 17);
		fromLabel.setFont(SWTResourceManager.getFont("Arial", 11, SWT.NORMAL)); //$NON-NLS-1$
		fromLabel.setForeground(SWTResourceManager.getColor(1, 1, 1));

		l = UIControlsFactory.createLabel(container, "To", SWT.WRAP); //$NON-NLS-1$
		l.setLocation(411, 277);
		l.setSize(23, 18);
		l.setFont(boldFont11);
		l.setForeground(SWTResourceManager.getColor(1, 1, 1));

		final Label toLabel = UIControlsFactory.createLabel(container, "", SWT.NONE, "emailTo"); //$NON-NLS-1$ //$NON-NLS-2$
		toLabel.setLocation(440, 277);
		toLabel.setSize(282, 17);
		toLabel.setFont(SWTResourceManager.getFont("Arial", 11, SWT.NORMAL)); //$NON-NLS-1$
		toLabel.setForeground(SWTResourceManager.getColor(1, 1, 1));

		l = UIControlsFactory.createLabel(container, "Date", SWT.WRAP); //$NON-NLS-1$
		l.setLocation(25, 302);
		l.setSize(37, 18);
		l.setFont(boldFont11);
		l.setForeground(SWTResourceManager.getColor(1, 1, 1));

		final Label dateLabel = UIControlsFactory.createLabel(container, "", "emailDate"); //$NON-NLS-1$ //$NON-NLS-2$
		dateLabel.setLocation(103, 300);
		dateLabel.setSize(142, 17);
		dateLabel.setFont(normalFont11);
		dateLabel.setForeground(SWTResourceManager.getColor(1, 1, 1));

		// message contents

		final Text emailBody = UIControlsFactory.createText(container, SWT.MULTI | SWT.WRAP, "emailBody"); //$NON-NLS-1$
		emailBody.setLocation(31, 326);
		emailBody.setSize(697, 112);
		emailBody.setFont(normalFont11);

		//create button
		final Button openCustomerButton = UIControlsFactory.createButton(container, "Open Customer", "openCustomer"); //$NON-NLS-1$ //$NON-NLS-2$
		openCustomerButton.setBounds(580, 474, 133, 38);

		final Composite composite = DemoClientUIControlsFactory.createSeparator(container);
		composite.setBounds(25, 235, 706, 2);

		final Composite composite2 = DemoClientUIControlsFactory.createSeparator(container);
		composite2.setBounds(25, 457, 706, 2);
	}
}
