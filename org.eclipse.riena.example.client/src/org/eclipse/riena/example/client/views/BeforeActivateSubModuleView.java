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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Example view with a (single) choice to select if it's allowed to activate the
 * next sub-module.
 */
public class BeforeActivateSubModuleView extends SubModuleView {

	@Override
	protected void basicCreatePartControl(final Composite parent) {

		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		final GridLayout layout = new GridLayout(2, false);
		layout.verticalSpacing = 10;
		parent.setLayout(layout);

		UIControlsFactory.createLabel(parent, "Allow activation of the next sub-module: "); //$NON-NLS-1$
		final ChoiceComposite choiceComposite = UIControlsFactory.createChoiceComposite(parent, SWT.NONE, false,
				"allowChoice"); //$NON-NLS-1$
		choiceComposite.setOrientation(SWT.HORIZONTAL);
	}

}
