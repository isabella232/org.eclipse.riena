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

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;

/**
 * Switch focus to the previous or next group in the navigation tree. The
 * direction of the movement (forward / backward) is determined by appending the
 * strings ':next' or ':previous' to the handler declaration.
 * <p>
 * Example:
 * 
 * <pre>
 *   &lt;command
 *     id=&quot;org.eclipse.riena.navigation.ui.nextGroup&quot;
 *     name=&quot;Next group&quot;
 *     categoryId=&quot;org.eclipse.riena.navigation.ui.swt&quot;
 *     defaultHandler=&quot;org.eclipse.riena.internal.navigation.ui.swt.handlers.SwitchModule:next&quot;/&gt;
 * </pre>
 * 
 * Implementation notes: if the last group is reached, the next group will be
 * the first group. If the first group is reached is reached, the previous group
 * will be the last group.
 */
public class SwitchModule extends AbstractNavigationHandler implements IExecutableExtension {

	/**
	 * True if navigating to the next module (forward), false if navigating to
	 * the previous module (backward)
	 */
	private boolean toNext;
	/**
	 * True, to perform submodule activation in addition to module activation.
	 * In that case the first submodule will be selected if navigating forward,
	 * and the last submodule will be selected if navigating backward.
	 * <p>
	 * If false, the active submodule within a module will be kept as is.
	 */
	private boolean activateSubmodule;

	public Object execute(final ExecutionEvent event) throws ExecutionException {
		doSwitch(toNext);
		return null;
	}

	public void setActivateSubmodule(final boolean activate) {
		activateSubmodule = activate;
	}

	public void doSwitch(final boolean forward) {
		// assumes there is only one application node
		final IApplicationNode application = ApplicationNodeManager.getApplicationNode();
		final INavigationNode<?>[] modules = collectModules(application);
		final INavigationNode<?> nextModule = forward ? findNextNode(modules) : findPreviousNode(modules, true);
		if (nextModule != null) {
			final INavigationNode<?> nextSubModule = findNextSubModule(forward, nextModule);
			if (nextSubModule != null) {
				nextSubModule.activate();
			} else {
				nextModule.activate();
			}
		}
	}

	/**
	 * This method is called by the framework. Not intented to be called by
	 * clients.
	 */
	public void setInitializationData(final IConfigurationElement config, final String propertyName, final Object data)
			throws CoreException {
		toNext = "next".equalsIgnoreCase(String.valueOf(data)); //$NON-NLS-1$
	}

	// helping methods
	//////////////////

	private INavigationNode<?> findNextSubModule(final boolean forward, final INavigationNode<?> nextModule) {
		INavigationNode<?> nextSubModule;
		if (activateSubmodule) {
			nextSubModule = getSubModule(nextModule, forward);
		} else {
			nextSubModule = findActive((List<INavigationNode<?>>) nextModule.getChildren());
		}
		return nextSubModule;
	}

	private INavigationNode<?> getSubModule(final INavigationNode<?> module, final boolean forward) {
		INavigationNode<?> result = null;
		final List<ISubModuleNode> subModules = ((IModuleNode) module).getChildren();
		final int subModuleCount = subModules.size();
		if (subModuleCount > 1) {
			final int index = forward ? 0 : subModuleCount - 1;
			result = subModules.get(index);
		}
		return result;
	}

}
