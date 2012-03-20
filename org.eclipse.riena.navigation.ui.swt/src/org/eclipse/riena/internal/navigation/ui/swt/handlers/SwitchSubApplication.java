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
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;

/**
 * Switch focus to the previous or next sub application tab. The direction of
 * the movement (forward / backward) is determined by appending the strings
 * ':next' or ':previous' to the handler declaration.
 * <p>
 * Example:
 * 
 * <pre>
 *   &lt;command
 *     id=&quot;org.eclipse.riena.navigation.ui.nextSubApplication&quot;
 *     name=&quot;Next sub-application&quot;
 *     categoryId=&quot;org.eclipse.riena.navigation.ui.swt&quot;
 *     defaultHandler=&quot;org.eclipse.riena.internal.navigation.ui.swt.handlers.SwitchSubApplication:next&quot;/&gt;
 * </pre>
 * 
 * Implementation notes: if the last tab is reached, the next tab will be the
 * first tab. If the first tab is reached is reached, the previous tab will be
 * the last tab.
 */
public class SwitchSubApplication extends AbstractNavigationHandler implements IExecutableExtension {

	private boolean toNext;

	public Object execute(final ExecutionEvent event) throws ExecutionException {
		// assumes there is only one application node
		final IApplicationNode applicationNode = ApplicationNodeManager.getApplicationNode();
		final List<ISubApplicationNode> children = applicationNode.getChildren();
		final INavigationNode<?>[] nodes = children.toArray(new ISubApplicationNode[children.size()]);
		final INavigationNode<?> nextNode = toNext ? findNextNode(nodes) : findPreviousNode(nodes, true);
		if (nextNode != null) {
			nextNode.activate();
		}
		return null;
	}

	/**
	 * This method is called by the framework. Not intented to be called by
	 * clients.
	 */
	public void setInitializationData(final IConfigurationElement config, final String propertyName, final Object data)
			throws CoreException {
		toNext = "next".equalsIgnoreCase(String.valueOf(data)); //$NON-NLS-1$
	}

	/**
	 * Returns true if this node is selected.
	 */
	@Override
	protected boolean isSelected(final INavigationNode<?> node) {
		return node.isSelected();
	}

}
