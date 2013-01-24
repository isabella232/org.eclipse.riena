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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;

import org.eclipse.riena.example.client.controllers.TreeSubModuleController;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * SWT {@link ITreeRidget} sample.
 */
public class TreeSubModuleView extends SubModuleView {

	public static final String ID = TreeSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));

		final Group group1 = createTreeGroup(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(group1);
	}

	@Override
	protected TreeSubModuleController createController(final ISubModuleNode node) {
		return new TreeSubModuleController(node);
	}

	// helping methods
	// ////////////////

	private Group createTreeGroup(final Composite parent) {
		final Group group = UIControlsFactory.createGroup(parent, "&Tree:"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(1).applyTo(group);

		final Tree tree = UIControlsFactory.createTree(group, SWT.FULL_SELECTION | SWT.MULTI);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(tree);
		addUIControl(tree, "tree"); //$NON-NLS-1$

		final Composite buttonComposite = createButtonComposite(group);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(buttonComposite);

		return group;
	}

	private Composite createButtonComposite(final Group group) {
		final Composite buttonComposite = UIControlsFactory.createComposite(group);
		GridLayoutFactory.fillDefaults().numColumns(6).equalWidth(true).applyTo(buttonComposite);

		final Label spacer = UIControlsFactory.createLabel(buttonComposite, ""); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.END, SWT.BEGINNING).applyTo(spacer);

		final Button buttonAddSibling = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonAddSibling, "buttonAddSibling"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().applyTo(buttonAddSibling);

		final Button buttonRename = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonRename, "buttonRename"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().applyTo(buttonRename);

		final Button buttonEnable = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonEnable, "buttonEnable"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().applyTo(buttonEnable);

		final Button buttonExpand = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonExpand, "buttonExpand"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().applyTo(buttonExpand);

		final Button buttonShow = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonShow, "buttonShow"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().applyTo(buttonShow);

		// next row

		UIControlsFactory.createLabel(buttonComposite, ""); //$NON-NLS-1$

		final Button buttonAddChild = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonAddChild, "buttonAddChild"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().applyTo(buttonAddChild);

		final Button buttonDelete = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonDelete, "buttonDelete"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().applyTo(buttonDelete);

		final Button buttonDisable = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonDisable, "buttonDisable"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().applyTo(buttonDisable);

		final Button buttonCollapse = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonCollapse, "buttonCollapse"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().applyTo(buttonCollapse);

		final Button buttonHide = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonHide, "buttonHide"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().applyTo(buttonHide);

		return buttonComposite;
	}

}
