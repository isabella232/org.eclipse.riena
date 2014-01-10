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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * View of the sub module that demonstrates UI filters for ridgets.
 */
public class FilterSubModuleView extends SubModuleView {

	public static final String ID = FilterSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {

		parent.setLayout(new GridLayout(2, false));

		final Group group1 = createControlsGroup(parent);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(group1);
		final Group group2 = createMarkersGroup(parent);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(group2);
		final Group group3 = createLocalFiltersGroup(parent);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(group3);
		final Group group4 = createGlobalFiltersGroup(parent);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(group4);

	}

	/**
	 * Creates a group with come different UI controls.
	 * 
	 * @param parent
	 *            parent composite
	 * @return group
	 */
	private Group createControlsGroup(final Composite parent) {

		final Group group = UIControlsFactory.createGroup(parent, "UI-Controls"); //$NON-NLS-1$

		final int defaultVSpacing = new GridLayout().verticalSpacing;
		GridLayoutFactory.swtDefaults().numColumns(3).equalWidth(false).margins(20, 20).spacing(10, defaultVSpacing)
				.applyTo(group);

		UIControlsFactory.createLabel(group, "Labels"); //$NON-NLS-1$
		final Label label1 = UIControlsFactory.createLabel(group, "ui_label1"); //$NON-NLS-1$
		addUIControl(label1, "ui_label1"); //$NON-NLS-1$
		final Label label2 = UIControlsFactory.createLabel(group, "ui_label2"); //$NON-NLS-1$
		addUIControl(label2, "ui_label2"); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, "Texts"); //$NON-NLS-1$
		final Text text1 = UIControlsFactory.createText(group);
		addUIControl(text1, "ui_text1"); //$NON-NLS-1$
		final Text text2 = UIControlsFactory.createText(group);
		addUIControl(text2, "ui_text2"); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, "Buttons"); //$NON-NLS-1$
		final Button button1 = UIControlsFactory.createButton(group);
		button1.setText("ui_button1"); //$NON-NLS-1$
		addUIControl(button1, "ui_button1"); //$NON-NLS-1$
		final Button button2 = UIControlsFactory.createButton(group);
		button2.setText("ui_button2"); //$NON-NLS-1$
		addUIControl(button2, "ui_button2"); //$NON-NLS-1$

		return group;

	}

	/**
	 * Creates a group for adding markers.
	 * 
	 * @param parent
	 *            parent composite
	 * @return group
	 */
	private Group createMarkersGroup(final Composite parent) {

		final Group group = UIControlsFactory.createGroup(parent, "Markers"); //$NON-NLS-1$

		final int defaultVSpacing = new GridLayout().verticalSpacing;
		GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(false).margins(10, 20).spacing(10, defaultVSpacing)
				.applyTo(group);

		UIControlsFactory.createLabel(group, "Ridget ID"); //$NON-NLS-1$
		final Combo ridgetID = UIControlsFactory.createCombo(group);
		addUIControl(ridgetID, "ridgetToMarkID"); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, "Marker"); //$NON-NLS-1$
		final Combo markers = UIControlsFactory.createCombo(group);
		addUIControl(markers, "markers"); //$NON-NLS-1$

		final Button addMarker = UIControlsFactory.createButton(group);
		addMarker.setText("Add Marker"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(addMarker);
		addUIControl(addMarker, "addMarker"); //$NON-NLS-1$

		final Button removeMarker = UIControlsFactory.createButton(group);
		removeMarker.setText("Remove Marker"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(removeMarker);
		addUIControl(removeMarker, "removeMarker"); //$NON-NLS-1$

		return group;

	}

	/**
	 * Creates a group for adding <i>local</i> UI filters.
	 * 
	 * @param parent
	 *            parent composite
	 * @return group
	 */
	private Group createLocalFiltersGroup(final Composite parent) {

		final Group group = UIControlsFactory.createGroup(parent, "UI-Filters (local)"); //$NON-NLS-1$

		final int defaultVSpacing = new GridLayout().verticalSpacing;
		GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(false).margins(10, 20).spacing(10, defaultVSpacing)
				.applyTo(group);

		final Label explanation = UIControlsFactory.createLabel(group, "For ridgets of this sub module.\n\n"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(explanation);

		UIControlsFactory.createLabel(group, "Ridget ID"); //$NON-NLS-1$
		final Combo ridgetID = UIControlsFactory.createCombo(group);
		addUIControl(ridgetID, "ridgetID"); //$NON-NLS-1$

		final ChoiceComposite filterType = new ChoiceComposite(group, SWT.NONE, false);
		filterType.setOrientation(SWT.HORIZONTAL);
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(filterType);
		addUIControl(filterType, "filterType"); //$NON-NLS-1$

		final Combo filterTypeValues = UIControlsFactory.createCombo(group);
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(filterTypeValues);
		addUIControl(filterTypeValues, "filterTypeValues"); //$NON-NLS-1$

		final Button addFilter = UIControlsFactory.createButton(group);
		addFilter.setText("Add Filter"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(addFilter);
		addUIControl(addFilter, "addFilter"); //$NON-NLS-1$

		final Button removeFilters = UIControlsFactory.createButton(group);
		removeFilters.setText("Remove All Filters"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(removeFilters);
		addUIControl(removeFilters, "removeFilters"); //$NON-NLS-1$

		return group;

	}

	/**
	 * Creates a group for adding <i>global</i> UI filters.
	 * 
	 * @param parent
	 *            parent composite
	 * @return group
	 */
	private Group createGlobalFiltersGroup(final Composite parent) {

		final Group group = UIControlsFactory.createGroup(parent, "UI-Filters (global)"); //$NON-NLS-1$

		final int defaultVSpacing = new GridLayout().verticalSpacing;
		GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(false).margins(10, 20).spacing(10, defaultVSpacing)
				.applyTo(group);

		final Label explanation = UIControlsFactory.createLabel(group, "For ridgets of the whole application.\n\n"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(explanation);

		UIControlsFactory.createLabel(group, "Ridget ID"); //$NON-NLS-1$
		final Text ridgetID = UIControlsFactory.createText(group);
		addUIControl(ridgetID, "globalRidgetID"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).span(1, 1).applyTo(ridgetID);

		final ChoiceComposite filterType = new ChoiceComposite(group, SWT.NONE, false);
		filterType.setOrientation(SWT.HORIZONTAL);
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(filterType);
		addUIControl(filterType, "globalFilterType"); //$NON-NLS-1$

		final Combo filterTypeValues = UIControlsFactory.createCombo(group);
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(filterTypeValues);
		addUIControl(filterTypeValues, "globalFilterTypeValues"); //$NON-NLS-1$

		final Button addFilter = UIControlsFactory.createButton(group);
		addFilter.setText("Add Filter"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(addFilter);
		addUIControl(addFilter, "globalAddFilter"); //$NON-NLS-1$

		final Button removeFilters = UIControlsFactory.createButton(group);
		removeFilters.setText("Remove All Filters"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(removeFilters);
		addUIControl(removeFilters, "globalRemoveFilters"); //$NON-NLS-1$

		return group;

	}

}
