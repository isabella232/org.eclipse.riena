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
package org.eclipse.riena.internal.navigation.ui.swt.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewId;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewProvider;

/**
 * Switch focus to the 'work area'.
 */
public class SwitchToWorkarea extends AbstractHandler {

	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final String viewId = getViewId(getActiveNode());
		if (viewId != null) {
			final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
			final IWorkbenchPage page = window.getActivePage();
			for (final IViewReference viewRef : page.getViewReferences()) {
				if (viewId.equals(getFullId(viewRef))) {
					final IViewPart view = viewRef.getView(false);
					if (view != null) {
						view.setFocus();
					}
					break;
				}
			}
		}
		return null;
	}

	// helping methods
	//////////////////

	private INavigationNode<?> getActiveNode() {
		final IApplicationNode appNode = ApplicationNodeManager.getApplicationNode();
		return appNode.getNavigationProcessor().getSelectedNode();
	}

	private String getFullId(final IViewReference viewRef) {
		String result = viewRef.getId();
		if (viewRef.getSecondaryId() != null) {
			result = result + ":" + viewRef.getSecondaryId(); //$NON-NLS-1$
		}
		return result;
	}

	private String getViewId(final INavigationNode<?> node) {
		String result = null;
		if (node != null) {
			final SwtViewId viewId = SwtViewProvider.getInstance().getSwtViewId(node);
			if (viewId != null) {
				result = viewId.getCompoundId();
			}
		}
		return result;
	}

}
