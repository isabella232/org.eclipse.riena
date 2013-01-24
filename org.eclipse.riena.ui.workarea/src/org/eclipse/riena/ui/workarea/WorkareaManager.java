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
package org.eclipse.riena.ui.workarea;

import org.eclipse.riena.core.singleton.SingletonProvider;
import org.eclipse.riena.internal.ui.workarea.registry.WorkareaDefinitionRegistryFacade;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.ui.ridgets.controller.IController;

public final class WorkareaManager {

	private final static SingletonProvider<WorkareaManager> WM = new SingletonProvider<WorkareaManager>(
			WorkareaManager.class);

	private final WorkareaDefinitionRegistryFacade registry;

	static public WorkareaManager getInstance() {
		return WM.getInstance();
	}

	private WorkareaManager() {
		registry = WorkareaDefinitionRegistryFacade.getInstance();
	}

	public IWorkareaDefinition getDefinition(final Object key) {
		return registry.getDefinition(key);
	}

	public IWorkareaDefinition registerDefinition(final INavigationNode<?> node, final Object viewId) {
		return registerDefinition(node, null, viewId, false);
	}

	public IWorkareaDefinition registerDefinition(final String id, final Object viewId) {
		return registerDefinition(id, null, viewId, false);
	}

	public IWorkareaDefinition registerDefinition(final INavigationNode<?> node, final Object viewId,
			final boolean isViewShared) {
		return registerDefinition(node, null, viewId, isViewShared);
	}

	public IWorkareaDefinition registerDefinition(final String id, final Object viewId, final boolean isViewShared) {
		return registerDefinition(id, null, viewId, isViewShared);
	}

	public IWorkareaDefinition registerDefinition(final INavigationNode<?> node,
			final Class<? extends IController> controllerClass, final Object viewId) {
		return registerDefinition(node, controllerClass, viewId, false);
	}

	public IWorkareaDefinition registerDefinition(final String id, final Class<? extends IController> controllerClass,
			final Object viewId) {
		return registerDefinition(id, controllerClass, viewId, false);
	}

	public IWorkareaDefinition registerDefinition(final INavigationNode<?> node,
			final Class<? extends IController> controllerClass, final Object viewId, final boolean isViewShared) {
		return registry.registerDefinition(node, controllerClass, viewId, isViewShared);
	}

	public IWorkareaDefinition registerDefinition(final String id, final Class<? extends IController> controllerClass,
			final Object viewId, final boolean isViewShared) {
		return registry.registerDefinition(id, controllerClass, viewId, isViewShared);
	}
}
