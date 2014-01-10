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
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.ILinkRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * {@link ILinkRidget} sample.
 */
public class LinkSubModuleView extends SubModuleView {

	public static final String ID = LinkSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(false).margins(20, 20).spacing(20, 5).applyTo(parent);

		UIControlsFactory.createLabel(parent, "Links:"); //$NON-NLS-1$
		UIControlsFactory.createLink(parent, SWT.NONE, "link1"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLink(parent, SWT.NONE, "link2"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLink(parent, SWT.NONE, "link3"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "URL:"); //$NON-NLS-1$
		final Text textLinkUrl = UIControlsFactory.createText(parent, SWT.SINGLE, "textLinkUrl"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).applyTo(textLinkUrl);

		final Label label = UIControlsFactory.createLabel(parent, "Browser:"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(false, true).applyTo(label);
		final Browser browser = UIControlsFactory.createBrowser(parent, SWT.NONE, "browser"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, true).applyTo(browser);
	}
}
