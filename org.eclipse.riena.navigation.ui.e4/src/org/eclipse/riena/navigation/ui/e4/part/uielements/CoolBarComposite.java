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
package org.eclipse.riena.navigation.ui.e4.part.uielements;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.internal.provisional.action.ToolBarManager2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import org.eclipse.riena.navigation.ui.swt.component.IEntriesProvider;

public class CoolBarComposite extends Composite {
	private final ToolBarManager2 manager;
	private final IEntriesProvider provider;
	private ToolBar toolBar;

	public CoolBarComposite(final Composite parent, final IEntriesProvider provider) {
		super(parent, SWT.NONE);
		this.provider = provider;
		final GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		setLayout(layout);
		manager = new ToolBarManager2();
	}

	public List<ToolItem> updateItems() {
		if (toolBar != null) {
			toolBar.dispose();
		}
		manager.removeAll();
		for (final IContributionItem i : provider.getTopLevelEntries()) {
			manager.add(i);
		}
		toolBar = manager.createControl(this);
		//		toolBar.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
		return Collections.EMPTY_LIST;
	}
}
