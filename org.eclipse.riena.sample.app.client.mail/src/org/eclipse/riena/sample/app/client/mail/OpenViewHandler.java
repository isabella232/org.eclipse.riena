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
package org.eclipse.riena.sample.app.client.mail;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;

/**
 * Creates a new module group with an associated message view.
 */
public class OpenViewHandler extends AbstractHandler {

	private int count = 0;

	public Object execute(final ExecutionEvent event) {
		final IApplicationNode node = ApplicationNodeManager.getApplicationNode();
		final IModuleGroupNode group = (IModuleGroupNode) node.findNode(new NavigationNodeId(
				Application.ID_GROUP_MBOXES));
		final String title = "me@this.com (" + ++count + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		final IModuleNode moduleAccount1 = NodeFactory.createModule(
				new NavigationNodeId("account", Integer.toString(count)), //$NON-NLS-1$
				title, group);
		NodeFactory.createSubModule(
				new NavigationNodeId("Inbox", Integer.toString(count)), "Inbox", moduleAccount1, View.ID); //$NON-NLS-1$ //$NON-NLS-2$
		NodeFactory.createSubModule(
				new NavigationNodeId("Drafts", Integer.toString(count)), "Drafts", moduleAccount1, View.ID); //$NON-NLS-1$ //$NON-NLS-2$
		NodeFactory.createSubModule(
				new NavigationNodeId("Sent", Integer.toString(count)), "Sent", moduleAccount1, View.ID); //$NON-NLS-1$ //$NON-NLS-2$
		return null;
	}
}
