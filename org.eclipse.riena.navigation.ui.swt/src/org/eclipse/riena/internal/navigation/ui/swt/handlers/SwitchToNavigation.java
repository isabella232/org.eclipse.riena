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
package org.eclipse.riena.internal.navigation.ui.swt.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import org.eclipse.riena.navigation.ui.swt.views.NavigationViewPart;

/**
 * Switch focus to the NavigationView.
 */
public class SwitchToNavigation extends AbstractHandler {

	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		final IWorkbenchPage page = window.getActivePage();
		IViewPart navigationView = null;
		if (page != null) {
			final IViewReference[] viewRefs = page.getViewReferences();
			for (final IViewReference ref : viewRefs) {
				if (NavigationViewPart.ID.equals(ref.getId())) {
					navigationView = ref.getView(false);
					break;
				}
			}
		}
		if (navigationView != null) {
			navigationView.setFocus();
		}
		return null;
	}
}
