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
package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.example.client.views.LinkSubModuleView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IBrowserRidget;
import org.eclipse.riena.ui.ridgets.ILinkRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.listener.ISelectionListener;
import org.eclipse.riena.ui.ridgets.listener.SelectionEvent;

/**
 * Controller for the {@link LinkSubModuleView} example.
 */
public class LinkSubModuleController extends SubModuleController {

	public LinkSubModuleController() {
		this(null);
	}

	public LinkSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	@Override
	public void configureRidgets() {
		final ILinkRidget link1 = getRidget(ILinkRidget.class, "link1"); //$NON-NLS-1$
		link1.setText("<a>http://www.eclipse.org/</a>"); //$NON-NLS-1$

		final ILinkRidget link2 = getRidget(ILinkRidget.class, "link2"); //$NON-NLS-1$
		link2.setText("Visit <a href=\"http://www.eclipse.org/riena/\">Riena</a>"); //$NON-NLS-1$

		final ILinkRidget link3 = getRidget(ILinkRidget.class, "link3"); //$NON-NLS-1$
		link3.setText("Eclipse <a href=\"http://planeteclipse.org\">Blogs</a>, <a href=\"http://www.eclipse.org/community/news/\">News</a> and <a href=\"http://live.eclipse.org\">Events</a>"); //$NON-NLS-1$

		final ITextRidget textLinkUrl = getRidget(ITextRidget.class, "textLinkUrl"); //$NON-NLS-1$
		textLinkUrl.setText("http://www.eclipse.org"); //$NON-NLS-1$
		textLinkUrl.setOutputOnly(true);

		final IBrowserRidget browser = getRidget(IBrowserRidget.class, "browser"); //$NON-NLS-1$
		browser.bindToModel(textLinkUrl, ITextRidget.PROPERTY_TEXT);
		browser.updateFromModel();

		final ISelectionListener listener = new ISelectionListener() {
			public void ridgetSelected(final SelectionEvent event) {
				final String linkUrl = (String) event.getNewSelection().get(0);
				browser.setUrl(linkUrl);
			}
		};
		link1.addSelectionListener(listener);
		link2.addSelectionListener(listener);
		link3.addSelectionListener(listener);

	}
}
