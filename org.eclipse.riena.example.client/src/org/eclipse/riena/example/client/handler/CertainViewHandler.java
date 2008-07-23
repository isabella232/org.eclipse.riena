/*******************************************************************************
 * Copyright (c) 2007, 2008 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.example.client.handler;

import java.util.List;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubApplication;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */
public class CertainViewHandler extends DummyHandler {

	/**
	 * @see org.eclipse.riena.example.client.handler.DummyHandler#getTitle()
	 */
	@Override
	protected String getTitle() {
		return "Certain view"; //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.riena.example.client.handler.DummyHandler#getMessage()
	 */
	@Override
	protected String getMessage() {
		String msg = "This command is only enabled for a certain view!\n\n"; //$NON-NLS-1$
		msg += "Information:\n"; //$NON-NLS-1$
		msg += "\t active sub-application:\t" + getActiveSubApplication().getLabel() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
		msg += "\t active module-group:\t" + getActiveModuleGroup().getLabel() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
		msg += "\t active module:\t" + getActiveModule().getLabel() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
		msg += "\t active sub-module:\t" + getActiveSubModule().getLabel(); //$NON-NLS-1$
		return msg;
	}

	private ISubApplication getActiveSubApplication() {
		String perspectiveId = getActivePage().getPerspective().getId();
		ISubApplication node = SwtPresentationManagerAccessor.getManager().getNavigationNode(perspectiveId,
				ISubApplication.class);
		return node;
	}

	private IModuleGroupNode getActiveModuleGroup() {
		ISubApplication parent = getActiveSubApplication();
		List<IModuleGroupNode> children = parent.getChildren();
		for (IModuleGroupNode moduleGroupNode : children) {
			if (moduleGroupNode.isActivated()) {
				return moduleGroupNode;
			}
		}
		return null;
	}

	private IModuleNode getActiveModule() {
		IModuleGroupNode parent = getActiveModuleGroup();
		List<IModuleNode> children = parent.getChildren();
		for (IModuleNode moduleNode : children) {
			if (moduleNode.isActivated()) {
				return moduleNode;
			}
		}
		return null;
	}

	private ISubModuleNode getActiveSubModule() {
		ISubModuleNode node = null;
		IModuleNode parent = getActiveModule();
		List<ISubModuleNode> children = parent.getChildren();
		for (ISubModuleNode subModuleNode : children) {
			if (subModuleNode.isActivated()) {
				node = subModuleNode;
				break;
			}
		}
		if (node == null) {
			for (ISubModuleNode subModuleNode : children) {
				node = getActiveSubModule(subModuleNode);
				if (node != null) {
					break;
				}
			}
		}
		return node;
	}

	private ISubModuleNode getActiveSubModule(ISubModuleNode parent) {
		ISubModuleNode node = null;
		List<ISubModuleNode> children = parent.getChildren();
		for (ISubModuleNode subModuleNode : children) {
			if (subModuleNode.isActivated()) {
				node = subModuleNode;
				break;
			}
		}
		if (node == null) {
			for (ISubModuleNode subModuleNode : children) {
				node = getActiveSubModule(subModuleNode);
				if (node != null) {
					break;
				}
			}
		}
		return node;

	}

	/**
	 * Returns the currently active page.
	 * 
	 * @return active page
	 */
	private IWorkbenchPage getActivePage() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}

}
