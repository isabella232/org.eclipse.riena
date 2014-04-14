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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.layout.DpiGridLayout;
import org.eclipse.riena.ui.swt.layout.DpiGridLayoutFactory;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 *
 */
public class DpiLayoutSubModuleView extends SubModuleView {

	public static final String ID = DpiLayoutSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		parent.setLayout(new GridLayout(1, false));

		final GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, false);
		final GridDataFactory compositeGdf = GridDataFactory.fillDefaults().grab(true, true);

		final int marginLeft = 10;
		final int marginWidth = 8;
		final int marginRight = 6;
		final int marginTop = 9;
		final int marginHeight = 7;
		final int marginBottom = 5;
		final int horizontalSpacing = 12;
		final int verticalSpacing = 11;

		final Group groupDpi = UIControlsFactory.createGroup(parent, "DPI-GridLayout"); //$NON-NLS-1$
		groupDpi.setLayout(new GridLayout(1, false));
		gdf.applyTo(groupDpi);
		final Composite compositeDpi = UIControlsFactory.createComposite(groupDpi, SWT.NONE, "compositeDpi"); //$NON-NLS-1$
		compositeGdf.applyTo(compositeDpi);
		final DpiGridLayout dpiLayout = new DpiGridLayout(2, false);
		dpiLayout.marginLeft = marginLeft;
		dpiLayout.marginWidth = marginWidth;
		dpiLayout.marginRight = marginRight;
		dpiLayout.marginTop = marginTop;
		dpiLayout.marginHeight = marginHeight;
		dpiLayout.marginBottom = marginBottom;
		dpiLayout.horizontalSpacing = horizontalSpacing;
		dpiLayout.verticalSpacing = verticalSpacing;
		compositeDpi.setLayout(dpiLayout);
		fillGroup(compositeDpi);

		final Group groupGrid = UIControlsFactory.createGroup(parent, "GridLayout"); //$NON-NLS-1$
		groupGrid.setLayout(new GridLayout(1, false));
		gdf.applyTo(groupGrid);
		final Composite compositeGrid = UIControlsFactory.createComposite(groupGrid, SWT.NONE, "compositeGrid"); //$NON-NLS-1$
		compositeGdf.applyTo(compositeGrid);
		final GridLayout girdLayout = new GridLayout(2, false);
		girdLayout.marginLeft = marginLeft;
		girdLayout.marginWidth = marginWidth;
		girdLayout.marginRight = marginRight;
		girdLayout.marginTop = marginTop;
		girdLayout.marginHeight = marginHeight;
		girdLayout.marginBottom = marginBottom;
		girdLayout.horizontalSpacing = horizontalSpacing;
		girdLayout.verticalSpacing = verticalSpacing;
		compositeGrid.setLayout(girdLayout);
		fillGroup(compositeGrid);

		final Group vGroupDpi = UIControlsFactory.createGroup(parent, "DPI-GridLayout"); //$NON-NLS-1$
		vGroupDpi.setLayout(new GridLayout(1, false));
		gdf.applyTo(vGroupDpi);
		final Composite vCompositeDpi = UIControlsFactory.createComposite(vGroupDpi, SWT.NONE, "compositeDpi"); //$NON-NLS-1$
		compositeGdf.applyTo(vCompositeDpi);
		DpiGridLayoutFactory.swtDefaults().numColumns(1).equalWidth(false).margins(marginWidth, marginHeight)
				.extendedMargins(marginLeft, marginRight, marginTop, marginBottom).spacing(horizontalSpacing, verticalSpacing).applyTo(vCompositeDpi);
		fillGroup(vCompositeDpi);

		final Group vGroupGrid = UIControlsFactory.createGroup(parent, "GridLayout"); //$NON-NLS-1$
		vGroupGrid.setLayout(new GridLayout(1, false));
		gdf.applyTo(vGroupGrid);
		final Composite vCompositeGrid = UIControlsFactory.createComposite(vGroupGrid, SWT.NONE, "compositeGrid"); //$NON-NLS-1$
		compositeGdf.applyTo(vCompositeGrid);
		GridLayoutFactory.swtDefaults().numColumns(1).equalWidth(false).margins(marginWidth, marginHeight)
				.extendedMargins(marginLeft, marginRight, marginTop, marginBottom).spacing(horizontalSpacing, verticalSpacing).applyTo(vCompositeGrid);
		fillGroup(vCompositeGrid);

	}

	private void fillGroup(final Composite composite) {

		composite.setBackground(LnfManager.getLnf().getColor("lightGray")); //$NON-NLS-1$

		final Label label = UIControlsFactory.createLabel(composite, "Label:", "tscLabel"); //$NON-NLS-1$ //$NON-NLS-2$
		GridDataFactory.swtDefaults().hint(125, 35).applyTo(label);
		label.setBackground(LnfManager.getLnf().getColor("green")); //$NON-NLS-1$

		final Text text = UIControlsFactory.createText(composite);
		GridDataFactory.swtDefaults().hint(225, 45).applyTo(text);
		text.setBackground(LnfManager.getLnf().getColor("yellow")); //$NON-NLS-1$

	}

}
