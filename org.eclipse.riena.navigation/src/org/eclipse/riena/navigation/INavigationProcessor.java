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
package org.eclipse.riena.navigation;

import java.util.List;

import org.eclipse.riena.core.marker.IMarker;

/**
 * Manages the Navigation. Is called by a navigation node to navigate to it the
 * navigation processor works with the INavigationNode. What does the navigation
 * processor? The navigation processor decides how many nodes in his scope can
 * be active at the same time -> the default navigation processor allows only
 * one node of each type.
 */
public interface INavigationProcessor extends INavigationHistory, INavigationHistoryListenerable {

	/**
	 * Activates a node. Checks which other nodes have to be activated or
	 * deactivated before this node can be activated and calls the
	 * allowsDeactivate() and allowsActivate() methods.
	 * 
	 * @see INavigationNode#activate()
	 * @see INavigationNode#allowsActivate(INavigationContext)
	 * @see INavigationNode#deactivate(INavigationContext)
	 * @see INavigationNode#allowsDeactivate(INavigationContext)
	 * @param toActivate
	 *            The node to activate.
	 */
	void activate(INavigationNode<?> toActivate);

	/**
	 * Prepares the given node.
	 * 
	 * @param toPrepare
	 *            the node to prepare
	 * 
	 * @see INavigationNode#prepare()
	 * @since 2.0
	 */
	void prepare(INavigationNode<?> toPrepare);

	/**
	 * Disposes a node. Checks which other nodes have to be disposed (children
	 * and maybe parents it the node is their only child) and calls the
	 * allowsDispose() methods.
	 * 
	 * @see INavigationNode#dispose()
	 * @see INavigationNode#allowsDeactivate(INavigationContext)
	 * @param toDispose
	 *            The node to dispose.
	 */
	void dispose(INavigationNode<?> toDispose);

	/**
	 * Creates the specified navigation node and adds it to the application
	 * model if does not already exist.
	 * 
	 * @param sourceNode
	 *            An existing node in the application model tree.
	 * @param targetId
	 *            ID of the node to create. Also refers to an extension point
	 *            used to create the target node if it does not exist.
	 * @return target node
	 * @see INavigationAssembler
	 * @since 2.0
	 */
	INavigationNode<?> create(INavigationNode<?> sourceNode, NavigationNodeId targetId);

	/**
	 * Creates the specified navigation node and adds it to the application
	 * model if does not already exist. Also adds the NavigationArgument to the
	 * node of the targetId.
	 * 
	 * @param sourceNode
	 *            An existing node in the application model tree.
	 * @param targetId
	 *            ID of the node to create. Also refers to an extension point
	 *            used to create the target node if it does not exist.
	 * @param argument
	 *            Contains information passed on to the target node and/or used
	 *            during its creation.
	 * @return target node
	 * @see INavigationAssembler
	 * @since 2.0
	 */
	INavigationNode<?> create(INavigationNode<?> sourceNode, NavigationNodeId targetId, NavigationArgument argument);

	/**
	 * Moves the sourceNode to the node identified by the targetId. When moving
	 * a node as child to another keep in mind that you have to honor the strict
	 * type hierarchy of the nodes in the navigation model.
	 * 
	 * @param sourceNode
	 *            An existing node in the application model tree.
	 * @param targetId
	 *            ID of the node where the sourceNode has to be added as child.
	 * @since 2.0
	 */
	void move(INavigationNode<?> sourceNode, NavigationNodeId targetId);

	/**
	 * Navigates from the specified source node to the specified target node.
	 * The target node is created and added to the application model if no node
	 * with the specified id exists.
	 * 
	 * @param sourceNode
	 *            The source node.
	 * @param targetId
	 *            ID of the target node. Also refers to an extension point used
	 *            to create the target node if it does not exist.
	 * @param argument
	 *            Contains information passed on to the target node and/or used
	 *            during its creation.
	 * @return target node
	 * 
	 * @see INavigationAssembler
	 * @since 2.0
	 */
	INavigationNode<?> navigate(INavigationNode<?> sourceNode, NavigationNodeId targetId, NavigationArgument argument);

	/**
	 * Undoes the last navigate to the specified target node i.e. activates the
	 * last source node of a navigate(..)-call that lead to the activation of
	 * the target node.
	 * 
	 * @param targetNode
	 *            the navigation node whose last navigate should be undo
	 * 
	 * @see #navigate(INavigationNode, NavigationNodeId, NavigationArgument)
	 */
	void navigateBack(INavigationNode<?> targetNode);

	/**
	 * Jumps to the specified navigation node (Creates it if does not already
	 * exist). The source node of the jump is saved to allow later
	 * {@link #jumpBack(NavigationNodeId, NavigationArgument)}
	 * 
	 * @param sourceNode
	 *            The source node.
	 * @param targetId
	 *            ID of the node to jump to. Also refers to an extension point
	 *            describing the target node that is used to create it if it
	 *            does not exist.
	 * @param argument
	 *            Contains information passed on to the target node and/or used
	 *            during its creation.
	 * @since 2.0
	 */
	void jump(INavigationNode<?> sourceNode, NavigationNodeId targetId, NavigationArgument argument);

	/**
	 * Jumps back to the source node of the last jump to this node.
	 * 
	 * @param sourceNode
	 *            The source node.
	 * @since 2.0
	 */
	void jumpBack(INavigationNode<?> sourceNode);

	/**
	 * 
	 * @param node
	 *            The source node.
	 * @return True if the given {@link INavigationNode} has been jumped to.
	 *         Otherwise false.
	 * @since 2.0
	 */
	boolean isJumpTarget(INavigationNode<?> node);

	/**
	 * Adds the given {@link IJumpTargetListener}
	 * 
	 * @param node
	 *            The observed node
	 * @param listener
	 *            The {@link IJumpTargetListener} to add
	 * @since 2.0
	 */
	void addJumpTargetListener(INavigationNode<?> node, IJumpTargetListener listener);

	/**
	 * Removes the given {@link IJumpTargetListener}
	 * 
	 * @param node
	 *            The observed node
	 * @param listener
	 *            The {@link IJumpTargetListener} to add
	 * @since 2.0
	 */
	void removeJumpTargetListener(INavigationNode<?> node, IJumpTargetListener listener);

	/**
	 * Answers the currently selected navigation node in the NavigationTree
	 * 
	 * @return the currently selected SubModuleNode in the NavigationTree or
	 *         null
	 */
	INavigationNode<?> getSelectedNode();

	/**
	 * Adds the given marker to the given navigation node.
	 * 
	 * @param toMark
	 *            navigation node
	 * @param marker
	 *            marker that will be added
	 */
	void addMarker(INavigationNode<?> toMark, IMarker marker);

	/**
	 * @since 3.0
	 */
	void markNodesToCollapse(final INavigationNode<?> toActivate);

	/**
	 * Returns the history of navigation nodes.
	 * 
	 * @return the history of navigation nodes.
	 * @since 3.0
	 */
	List<INavigationNode<?>> getHistory();
}
