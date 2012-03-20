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
package org.eclipse.riena.sample.app.client.rcpmail;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(final IPageLayout layout) {
		final String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

		layout.addStandaloneView(NavigationView.ID, false, IPageLayout.LEFT, 0.25f, editorArea);
		final IFolderLayout folder = layout.createFolder("messages", IPageLayout.TOP, 0.5f, editorArea); //$NON-NLS-1$
		folder.addView(MessageView.ID);
		folder.addView(MarkerSubModuleView.ID);
		folder.addPlaceholder(MessageView.ID + ":*"); //$NON-NLS-1$

		layout.getViewLayout(NavigationView.ID).setCloseable(false);
	}
}
