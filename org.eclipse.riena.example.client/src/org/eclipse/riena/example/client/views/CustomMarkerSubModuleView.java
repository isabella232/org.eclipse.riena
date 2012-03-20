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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * This view demonstrates the usage of custom markers.
 */
public class CustomMarkerSubModuleView extends SubModuleView {

	public static final String ID = CustomMarkerSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {

		parent.setLayout(new GridLayout(1, false));

		final Group optGroup = createMarkerOptionsGroup(parent);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(optGroup);
		final Group crlGroup = createControlsGroup(parent);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(crlGroup);
	}

	private Group createMarkerOptionsGroup(final Composite parent) {
		final Group group = UIControlsFactory.createGroup(parent, "Custom Marker Options:"); //$NON-NLS-1$
		group.setLayout(createGridLayout(3));

		UIControlsFactory.createButtonCheck(group, "default output", "checkOutput"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonCheck(group, "custom output", "customCheckOutput"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonCheck(group, "custom 2 output", "custom2CheckOutput");//$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonCheck(group, "default mandatory", "checkMandatory"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonCheck(group, "custom mandatory", "customCheckMandatory"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonCheck(group, "custom 2 mandatory", "custom2CheckMandatory");//$NON-NLS-1$ //$NON-NLS-2$

		return group;
	}

	private Group createControlsGroup(final Composite parent) {
		final Group group = UIControlsFactory.createGroup(parent, "UI-Controls:"); //$NON-NLS-1$
		final int defaultVSpacing = new GridLayout().verticalSpacing;
		GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(false).margins(20, 20).spacing(10, defaultVSpacing)
				.applyTo(group);

		final GridDataFactory hFillFactory = GridDataFactory.fillDefaults().grab(true, false);

		UIControlsFactory.createLabel(group, "Name:", "labeltextName"); //$NON-NLS-1$ //$NON-NLS-2$
		final Text textName = UIControlsFactory.createText(group, SWT.SINGLE, "textName"); //$NON-NLS-1$
		hFillFactory.applyTo(textName);
		UIControlsFactory.createLabel(group, "Price:", "labeltextPrice"); //$NON-NLS-1$ //$NON-NLS-2$
		final Text textPrice = UIControlsFactory.createTextDecimal(group, "textPrice"); //$NON-NLS-1$
		hFillFactory.applyTo(textPrice);

		UIControlsFactory.createLabel(group, "Amount:", "labeltextAmount"); //$NON-NLS-1$ //$NON-NLS-2$
		final Text textAmount = UIControlsFactory.createTextNumeric(group, "textAmount"); //$NON-NLS-1$
		hFillFactory.applyTo(textAmount);

		return group;
	}

	private GridLayout createGridLayout(final int numColumns) {
		final GridLayout layout = new GridLayout(numColumns, false);
		layout.marginWidth = 20;
		layout.marginHeight = 20;
		return layout;
	}

}
