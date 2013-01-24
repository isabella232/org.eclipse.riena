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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;

/**
 *
 */
public class FilterExampleSubModuleView extends SubModuleView {

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		final GridLayout gridLayout3 = new GridLayout();
		parent.setLayout(gridLayout3);
		parent.setBackgroundMode(SWT.INHERIT_FORCE);

		Group menuToolGroup;

		Group ridgetsGroup;

		Group navigationGroup;
		navigationGroup = new Group(parent, SWT.NONE);
		navigationGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		navigationGroup.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		final GridLayout gridLayout2 = new GridLayout();
		gridLayout2.verticalSpacing = 0;
		gridLayout2.numColumns = 2;
		navigationGroup.setLayout(gridLayout2);
		navigationGroup.setText("Navigation"); //$NON-NLS-1$

		final Label hideTheLabel = new Label(navigationGroup, SWT.NONE);
		hideTheLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		hideTheLabel
				.setText("* Hide the first two module groups (\"Shared View Demo\" and \"UIProcess\" )\n* Disable the two sub-modules \"Ridget\" and \"Navigation\"\n* Hide the two sub-modules \"Menu Item\" and \"External Definition\"\n* Disable the module \"Log Collector\"\n* Inside the module \"Playground\" disable the two sub-modules \"Tree\" and \"Tree Table\""); //$NON-NLS-1$

		final Button navigationButton = new Button(navigationGroup, SWT.TOGGLE);
		final GridData gdNavigationButton = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false);
		gdNavigationButton.widthHint = 80;
		navigationButton.setLayoutData(gdNavigationButton);
		navigationButton.setText("activate"); //$NON-NLS-1$
		addUIControl(navigationButton, "navigationBtn"); //$NON-NLS-1$
		menuToolGroup = new Group(parent, SWT.NONE);
		menuToolGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		menuToolGroup.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		final GridLayout gridLayout1 = new GridLayout();
		gridLayout1.verticalSpacing = 0;
		gridLayout1.numColumns = 2;
		menuToolGroup.setLayout(gridLayout1);
		menuToolGroup.setText("Menu / Tool Bar"); //$NON-NLS-1$

		final Label disableAllLabel = new Label(menuToolGroup, SWT.WRAP);
		disableAllLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		disableAllLabel
				.setText("* Disable all menu items of the menu \"File\"\n* Disable the tool item \"Exit\"\n* Hide th menu \"Navigation menu\""); //$NON-NLS-1$

		final Button menuToolButton = new Button(menuToolGroup, SWT.TOGGLE);
		final GridData gdMenuToolButton = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false);
		gdMenuToolButton.widthHint = 80;
		menuToolButton.setLayoutData(gdMenuToolButton);
		menuToolButton.setText("activate"); //$NON-NLS-1$
		addUIControl(menuToolButton, "menuToolItemBtn"); //$NON-NLS-1$
		ridgetsGroup = new Group(parent, SWT.NONE);
		ridgetsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		ridgetsGroup.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.numColumns = 2;
		ridgetsGroup.setLayout(gridLayout);
		ridgetsGroup.setText("Ridgets (module \"Playground\")"); //$NON-NLS-1$

		final Label inTheLabel = new Label(ridgetsGroup, SWT.NONE);
		inTheLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		inTheLabel
				.setText("* In the sub-module \"Text\" hide every text field, that displays a model value\n* In the sub-module \"Text\" set for every ridget the maximum length \"12\""); //$NON-NLS-1$

		final Button ridgetButton = new Button(ridgetsGroup, SWT.TOGGLE);
		final GridData gdActivateButton1 = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false);
		gdActivateButton1.widthHint = 80;
		ridgetButton.setLayoutData(gdActivateButton1);
		ridgetButton.setText("activate"); //$NON-NLS-1$
		addUIControl(ridgetButton, "ridgetBtn"); //$NON-NLS-1$

		ridgetsGroup = new Group(parent, SWT.NONE);
		ridgetsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		ridgetsGroup.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.numColumns = 2;
		ridgetsGroup.setLayout(gridLayout);
		ridgetsGroup.setText("Permission based UIFilters"); //$NON-NLS-1$

		final Label inTheLabel2 = new Label(ridgetsGroup, SWT.NONE);
		inTheLabel2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		inTheLabel2.setText("Adds a new SubModule which is honored by a Permission Filter"); //$NON-NLS-1$

		final Button addFilteredNodeAction = new Button(ridgetsGroup, SWT.PUSH);
		final GridData gdActivateButton12 = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false);
		gdActivateButton12.widthHint = 80;
		addFilteredNodeAction.setLayoutData(gdActivateButton12);
		addFilteredNodeAction.setText("add node"); //$NON-NLS-1$
		addUIControl(addFilteredNodeAction, "addNode"); //$NON-NLS-1$

		final Group ridgetsGroup1 = new Group(parent, SWT.NONE);
		ridgetsGroup1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final GridLayout gridLayout4 = new GridLayout();
		gridLayout4.verticalSpacing = 0;
		gridLayout4.numColumns = 2;
		ridgetsGroup1.setLayout(gridLayout4);
		ridgetsGroup1.setText("Ridgets (sample)"); //$NON-NLS-1$

		final Label theSampleTextLabel = new Label(ridgetsGroup1, SWT.NONE);
		final GridData gdTheSampleTextLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		theSampleTextLabel.setLayoutData(gdTheSampleTextLabel);
		theSampleTextLabel.setText("The sample text ridget has a mandatory marker"); //$NON-NLS-1$

		final Label disableTheFourLabel = new Label(ridgetsGroup1, SWT.NONE);
		final GridData gdDisableTheFourLabel = new GridData(SWT.FILL, SWT.CENTER, true, false);
		disableTheFourLabel.setLayoutData(gdDisableTheFourLabel);
		disableTheFourLabel.setText("* Disable the three sample ridgets"); //$NON-NLS-1$

		final Button ridgetsDisableButton = new Button(ridgetsGroup1, SWT.TOGGLE);
		ridgetsDisableButton.setLayoutData(new GridData(80, SWT.DEFAULT));
		ridgetsDisableButton.setText("activate"); //$NON-NLS-1$
		addUIControl(ridgetsDisableButton, "ridgetDisableBtn"); //$NON-NLS-1$

		final Label hiddeTheFourLabel = new Label(ridgetsGroup1, SWT.NONE);
		hiddeTheFourLabel.setText("* Hide the three sample ridgets"); //$NON-NLS-1$

		final Button ridgetsHideButton = new Button(ridgetsGroup1, SWT.TOGGLE);
		ridgetsHideButton.setLayoutData(new GridData(80, SWT.DEFAULT));
		ridgetsHideButton.setText("activate"); //$NON-NLS-1$
		addUIControl(ridgetsHideButton, "ridgetHideBtn"); //$NON-NLS-1$

		final Label onlyAllowLabel = new Label(ridgetsGroup1, SWT.NONE);
		onlyAllowLabel.setText("* Only allow the two characters \"0\" and \"1\" in the sample text ridget"); //$NON-NLS-1$

		final Button ridgets01Button = new Button(ridgetsGroup1, SWT.TOGGLE);
		ridgets01Button.setLayoutData(new GridData(80, SWT.DEFAULT));
		ridgets01Button.setText("activate"); //$NON-NLS-1$
		addUIControl(ridgets01Button, "ridget01Btn"); //$NON-NLS-1$

		final Composite composite = new Composite(ridgetsGroup1, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		final GridLayout gridLayout5 = new GridLayout();
		gridLayout5.horizontalSpacing = 15;
		gridLayout5.numColumns = 4;
		composite.setLayout(gridLayout5);

		final Label sampleLabel = new Label(composite, SWT.NONE);
		sampleLabel.setText("Sample Label"); //$NON-NLS-1$
		addUIControl(sampleLabel, "sampleLabel"); //$NON-NLS-1$

		final Text sampleText = new Text(composite, SWT.BORDER);
		final GridData gdSampleText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gdSampleText.minimumWidth = 80;
		sampleText.setLayoutData(gdSampleText);
		addUIControl(sampleText, "sampleText"); //$NON-NLS-1$

		final Button button = new Button(composite, SWT.NONE);
		button.setText("Sample Button"); //$NON-NLS-1$
		addUIControl(button, "sampleBtn"); //$NON-NLS-1$

		initializeToolBar();

	}

	private void initializeToolBar() {
	}

}
