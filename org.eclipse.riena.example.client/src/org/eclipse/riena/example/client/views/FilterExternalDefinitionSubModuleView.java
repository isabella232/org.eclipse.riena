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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

public class FilterExternalDefinitionSubModuleView extends SubModuleView {

	@Override
	protected void basicCreatePartControl(final Composite parent) {

		parent.setLayout(new GridLayout(1, false));

		final Group group1 = createFiltersGroup(parent);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(group1);

	}

	private Group createFiltersGroup(final Composite parent) {

		final Group group = UIControlsFactory.createGroup(parent, "UI-Filters (External definition)"); //$NON-NLS-1$

		final int defaultVSpacing = new GridLayout().verticalSpacing;
		GridLayoutFactory.swtDefaults().numColumns(1).equalWidth(false).margins(10, 20).spacing(10, defaultVSpacing)
				.applyTo(group);

		final Button addFilter = UIControlsFactory.createButton(group);
		addFilter.setText("Enable offline filter"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).span(1, 1).applyTo(addFilter);
		addUIControl(addFilter, "addOffline"); //$NON-NLS-1$

		final Button removeFilters = UIControlsFactory.createButton(group);
		removeFilters.setText("Disable offline filter"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).span(1, 1).applyTo(removeFilters);
		addUIControl(removeFilters, "removeOffline"); //$NON-NLS-1$

		return group;

	}
}
