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

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

public class ContentFilterSubModuleView extends SubModuleView {
	private static final int NUM_COLUMNS = 2;

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setLayout(new FillLayout());
		final Group g = UIControlsFactory.createGroup(parent, "This demonstrates content filters");
		GridLayoutFactory.swtDefaults().numColumns(NUM_COLUMNS).equalWidth(true).spacing(50, 5).applyTo(g);

		UIControlsFactory.createLabel(g, "List");
		UIControlsFactory.createLabel(g, "Table");

		UIControlsFactory.createList(g, true, true, "list").setLayoutData(createContentLayoutData());
		UIControlsFactory.createTable(g, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, "table").setLayoutData(createContentLayoutData());

		UIControlsFactory.createSeparator(g, SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, NUM_COLUMNS, 1));

		UIControlsFactory.createLabel(g, "Tree");
		UIControlsFactory.createLabel(g, "");

		UIControlsFactory.createTree(g, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, "tree").setLayoutData(createContentLayoutData());
		UIControlsFactory.createLabel(g, "");

		UIControlsFactory.createSeparator(g, SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, NUM_COLUMNS, 1));

		UIControlsFactory.createButtonCheck(g, "Hide weekdays", "hideWeekdays");
		UIControlsFactory.createButtonCheck(g, "Hide weekend", "hideWeekend");
	}

	/**
	 * @return
	 */
	private GridData createContentLayoutData() {
		final GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.heightHint = 100;
		return layoutData;
	}
}
