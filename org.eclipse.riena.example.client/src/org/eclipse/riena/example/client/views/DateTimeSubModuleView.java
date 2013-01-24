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
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.IDateTimeRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * SWT {@link IDateTimeRidget} example.
 */
public class DateTimeSubModuleView extends SubModuleView {

	public static final String ID = DateTimeSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));

		final GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, false);
		final Group group1 = createGroup(parent, "dtDate", "dtTime", "txt1", SWT.MEDIUM); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		group1.setText("DateTime #1"); //$NON-NLS-1$
		gdf.applyTo(group1);

		final Group group2 = createGroup(parent, "dtDateOnly", null, "txt2", SWT.LONG); //$NON-NLS-1$ //$NON-NLS-2$
		group2.setText("DateTime #2"); //$NON-NLS-1$
		gdf.applyTo(group2);

		final Group group3 = createGroup(parent, null, "dtTimeOnly", "txt3", SWT.SHORT); //$NON-NLS-1$ //$NON-NLS-2$
		group3.setText("DateTime #3"); //$NON-NLS-1$
		gdf.applyTo(group3);

		final Group group4 = createGroup(parent, "dtCal", null, "txt4", SWT.CALENDAR); //$NON-NLS-1$ //$NON-NLS-2$
		group4.setText("DateTime #4"); //$NON-NLS-1$
		gdf.applyTo(group4);
	}

	// helping methods
	// ////////////////

	private Group createGroup(final Composite parent, final String dateId, final String timeId, final String textId,
			final int style) {
		final Group group = UIControlsFactory.createGroup(parent, ""); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(2).applyTo(group);
		if (dateId != null) {
			final Label label = UIControlsFactory.createLabel(group, "Date:"); //$NON-NLS-1$
			GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.BEGINNING).applyTo(label);
			DateTime dtDate;
			if (style != SWT.CALENDAR) {
				dtDate = UIControlsFactory.createDate(group, style);
			} else {
				dtDate = UIControlsFactory.createCalendar(group);
			}
			addUIControl(dtDate, dateId);
		}

		if (timeId != null) {
			UIControlsFactory.createLabel(group, "Time:"); //$NON-NLS-1$
			final DateTime dtTime = UIControlsFactory.createTime(group, style);
			addUIControl(dtTime, timeId);
		}

		UIControlsFactory.createLabel(group, "Model:"); //$NON-NLS-1$
		final Text text = UIControlsFactory.createText(group);
		GridDataFactory.fillDefaults().hint(220, SWT.DEFAULT).applyTo(text);
		addUIControl(text, textId);

		return group;
	}

}
