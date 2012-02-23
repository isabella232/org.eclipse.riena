/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
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
import java.util.Set;

import org.eclipse.riena.core.marker.IMarkable;
import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.navigation.common.ITypecastingAdaptable;
import org.eclipse.riena.ui.core.IDisposable;
import org.eclipse.riena.ui.filter.IUIFilterable;

/**
 * Summary of all abilities common to all model objects Each object is
 * presentation able. Each object can be a Parent of one another.
 * <p>
 * 
 * The children ability and parent ability is not included because it is
 * different for different model nodes
 * 
 * @param <C>
 *            the type of child nodes
 */
public interface INavigationNode<C extends INavigationNode<?>> extends ITypecastingAdaptable, IMarkable, IUIFilterable,
		IDisposable, INavigationHistory {

	String PROPERTY_LABEL = "label"; //$NON-NLS-1$
	/**
	 * @since 4.0
	 */
	String PROPERTY_TOOLTIPTEXT = "toolTipText"; //$NON-NLS-1$

	String CONTEXTKEY_NAVIGATE_AFTER_DISPOSE = "riena.navigation.navigateAfterDispose"; //$NON-NLS-1$

	/**
	 * The states of the navigation node.
	 */
	public enum State {
		CREATED, PREPARED, ACTIVATED, DEACTIVATED, DISPOSED
	}

	/**
	 * Call this method to activate this node. The node forwards the activation
	 * request to the navigation processor. The navigation processor checks
	 * which nodes have to be deactivated before this node can be activated. The
	 * navigation processor creates an INavigationContext and checks all nodes
	 * with allowsDeactivate() and allowsActivate() dependent on the
	 * INavigationContext. Following the NavigationProcessor deactivates and
	 * activates the corresponding nodes using the activation context.
	 */
	void activate();

	/**
	 * Call this method to dispose this node. Depending on the type of the node
	 * the dispose does different things. What the node does is implemented in
	 * the navigation processor. The processor decides e.g. that if the first
	 * module node in a group is disposed, than the group is closed. Before
	 * closing the navigation processor deactivates all affected nodes. Than a
	 * dispose change is fired, and at the end the node itself is removed from
	 * the tree.
	 */
	void dispose();

	/**
	 * Call this method to prepare this node. A prepared node is created but not
	 * yet activated (or deactivated, or disposed).
	 * 
	 * @since 2.0
	 */
	void prepare();

	/**
	 * Called by a NavigationProcessor to activate the node within an
	 * INavigationContext.
	 * 
	 * @param pContext
	 *            the Context to activate within
	 */
	void activate(INavigationContext pContext);

	/**
	 * Called by a NavigationProcessor before activating the node within an
	 * INavigationContext.
	 * 
	 * @param pContext
	 *            the Context to activate within
	 */
	void onBeforeActivate(INavigationContext pContext);

	/**
	 * Called by a NavigationProcessor after activating the node within an
	 * INavigationContext.
	 * 
	 * @param pContext
	 *            the Context to activate within
	 */
	void onAfterActivate(INavigationContext pContext);

	/**
	 * Called by a NavigationProcessor to dispose the node within an
	 * INavigationContext.
	 * 
	 * @param pContext
	 *            the Context to activate within
	 */
	void dispose(INavigationContext pContext);

	/**
	 * Called by a NavigationProcessor before disposing the node within an
	 * INavigationContext.
	 * 
	 * @param pContext
	 *            the Context to activate within
	 */
	void onBeforeDispose(INavigationContext pContext);

	/**
	 * Called by a NavigationProcessor after disposing the node within an
	 * INavigationContext.
	 * 
	 * @param pContext
	 *            the Context to activate within
	 */
	void onAfterDispose(INavigationContext pContext);

	/**
	 * Called by a NavigationProcessor to deactivate the node within an
	 * INavigationContext.
	 * 
	 * @param pContext
	 *            the Context to deactivate within
	 */
	void deactivate(INavigationContext pContext);

	/**
	 * Called by a NavigationProcessor before deactivating the node within an
	 * INavigationContext.
	 * 
	 * @param pContext
	 *            the Context to activate within
	 */
	void onBeforeDeactivate(INavigationContext pContext);

	/**
	 * Called by a NavigationProcessor after deactivating the node within an
	 * INavigationContext.
	 * 
	 * @param pContext
	 *            the Context to activate within
	 */
	void onAfterDeactivate(INavigationContext pContext);

	/**
	 * Called by a NavigationProcessor to prepare the node within an
	 * INavigationContext.
	 * 
	 * @param pContext
	 *            the Context to prepare within
	 * @since 2.0
	 */
	void prepare(INavigationContext pContext);

	/**
	 * Called by a NavigationProcessor to check if a node can be activated
	 * within an INavigationContext.
	 * 
	 * @param pContext
	 *            the Context to check within
	 * @return {@code true} if the node can be activated; otherwise
	 *         {@code false}
	 */
	boolean allowsActivate(INavigationContext pContext);

	/**
	 * Called by a NavigationProcessor to check if a node can be deactivated
	 * within an INavigationContext.
	 * 
	 * @param pContext
	 *            the Context to check within
	 * @return {@code true} if the node can be deactivated; otherwise
	 *         {@code false}
	 */
	boolean allowsDeactivate(INavigationContext pContext);

	/**
	 * Called by a NavigationProcessor to check if a node can be disposed within
	 * an INavigationContext.
	 * 
	 * @param pContext
	 *            the Context to check within
	 * @return {@code true} if the node can be disposed; otherwise {@code false}
	 */
	boolean allowsDispose(INavigationContext pContext);

	/**
	 * Called from the navigation processor to remove a node during dispose
	 * Removes the passed node from the list child nodes
	 * 
	 * @param pChild
	 *            the child node to remove
	 */
	void removeChild(INavigationNode<?> pChild);

	/**
	 * Gets an ordered list of child nodes of this node.
	 * 
	 * @return the list of children
	 */
	List<C> getChildren();

	/**
	 * Adds the passed child to the list of child nodes
	 * 
	 * @param pChild
	 *            the child node to add
	 */
	void addChild(C pChild);

	/**
	 * Adds the passed child to the list of child nodes at the specified index
	 * 
	 * @param index
	 *            the index of the child node
	 * @param pChild
	 *            the child node to add
	 * @since 2.0
	 */
	void addChild(int index, C pChild);

	/**
	 * Gets the child at the specified index.
	 * 
	 * @param pIndex
	 *            index of the child
	 * @return the child or null if not available.
	 */
	C getChild(int pIndex);

	/**
	 * Searches for a node with the specified ID in the application model tree
	 * tree from this node on downwards including this node.
	 * 
	 * @param nodeId
	 *            ID of the requested node
	 * @return A node with the specified ID that is either this node or a
	 *         descendant of this node or null if no such node exists
	 */
	INavigationNode<?> findNode(NavigationNodeId nodeId);

	/**
	 * Gets the index of the child starting at 0.
	 * 
	 * @param pChild
	 *            the child to find
	 * @return the index or -1 if the passed child is not a child of the node
	 *         addressed
	 */
	int getIndexOfChild(INavigationNode<?> pChild);

	/**
	 * Gets the INavigationNodeController.
	 * 
	 * @return the navigationNodeController
	 */
	INavigationNodeController getNavigationNodeController();

	/**
	 * Look for the next in the hierarchy available controller.
	 * 
	 * @return next controller
	 */
	INavigationNodeController getNextNavigationNodeController();

	/**
	 * Sets the controller of this navigation node.
	 * 
	 * @param controller
	 *            controller of this navigation node
	 */
	void setNavigationNodeController(INavigationNodeController controller);

	/**
	 * Gets the label of this node.
	 * 
	 * @return the label
	 */
	String getLabel();

	/**
	 * Sets the label of this node.
	 * 
	 * @param pLabel
	 *            the label
	 */
	void setLabel(String pLabel);

	/**
	 * Gets the icon of this node.
	 * 
	 * @return icon or null if no icon should be shown.
	 */
	String getIcon();

	/**
	 * The icon to show.
	 * 
	 * @param icon
	 *            the icon.
	 */
	void setIcon(String icon);

	/**
	 * Gets the parent of this node.
	 * 
	 * @return the parent or null if not set.
	 */
	INavigationNode<?> getParent();

	/**
	 * Sets the parent of this node.
	 * 
	 * @param pParent
	 *            the parent node
	 */
	void setParent(INavigationNode<?> pParent);

	/**
	 * Gets the NavigationProcessor of this node.
	 * 
	 * @return the navigationProcessor
	 */
	INavigationProcessor getNavigationProcessor();

	/**
	 * Sets the NavigationProcessor of this node.
	 * 
	 * @param pProcessor
	 *            the NavigationProcessor
	 */
	void setNavigationProcessor(INavigationProcessor pProcessor);

	/**
	 * Adds listener that fire events that are type specific to the node type.
	 * 
	 * @param pListener
	 *            listener to add
	 */
	void addSimpleListener(ISimpleNavigationNodeListener pListener);

	/**
	 * Removes simple listener @see addSimpleListener that were previously
	 * added.
	 * 
	 * @param pListener
	 *            the listener to remove
	 */
	void removeSimpleListener(ISimpleNavigationNodeListener pListener);

	/**
	 * Returns true, if this navigation node should be presented expanded.
	 * 
	 * @return {@code true} if the node is expanded; {@code false} if the node
	 *         is collapsed
	 */
	boolean isExpanded();

	/**
	 * Sets the expanded state
	 * 
	 * @param pExpanded
	 *            {@code true} if the node is expanded; {@code false} if the
	 *            node is collapsed
	 */
	void setExpanded(boolean pExpanded);

	/**
	 * Answer true if the node is a leaf = does not have children
	 * 
	 * @return {@code true} if the node is a leaf; otherwise {@code false}
	 */
	boolean isLeaf();

	/**
	 * Returns the markable helper object contained in the navigation node.
	 * 
	 * @return the markable helper
	 */
	IMarkable getMarkable();

	/**
	 * Returns the next in hierarchy available context.
	 * 
	 * @param key
	 *            the key whose associated context is to be returned
	 * @return the context
	 */
	Object getContext(String key);

	/**
	 * Sets the context.
	 * 
	 * @param key
	 *            the key whose associated context is to be returned
	 * @param pContext
	 *            context to set
	 */
	void setContext(String key, Object pContext);

	/**
	 * Removes the next in hierarchy available context.
	 * 
	 * @param key
	 *            the key whose associated context is to be removed
	 */
	void removeContext(String key);

	/**
	 * Adds an action to the node. Actions can be associated with nodes to
	 * automatically show and hide actions corresponding to the activated node
	 * 
	 * @param pAction
	 *            the action to add
	 */
	void addAction(IAction pAction);

	/**
	 * Remove an action from the node
	 * 
	 * @param pAction
	 *            the action to remove
	 */
	void removeAction(IAction pAction);

	/**
	 * Returns the actions defined by this node.
	 * 
	 * @return the list of actions
	 */
	Set<IAction> getActions();

	/**
	 * All actions defined by this node and its parents
	 * 
	 * @return the list of actions.
	 */
	Set<IAction> getAllActions();

	/**
	 * Returns the current state of this node.
	 * 
	 * @return the current state
	 * @see State
	 */
	State getState();

	/**
	 * Returns whether the node is activated or not.
	 * 
	 * @return {@code true} if this node has the state ACTIVATED; otherwise
	 *         {@code false}
	 */
	boolean isActivated();

	/**
	 * Returns whether the node is prepared or not.
	 * <p>
	 * <i>Prepare means that the controller (among others) of the node is
	 * already created but the node was not activated so far.</i>
	 * 
	 * @return {@code true} if this node has the state PREPARED; otherwise
	 *         {@code false}
	 * @since 2.0
	 */
	boolean isPrepared();

	/**
	 * Returns whether the node is deactivated or not.
	 * 
	 * @return {@code true} if this node has the state DEACTIVATED; otherwise
	 *         {@code false}
	 */
	boolean isDeactivated();

	/**
	 * Returns whether the node is disposed or not.
	 * 
	 * @return {@code true} if this node has the state DISPOSED; otherwise
	 *         {@code false}
	 */
	boolean isDisposed();

	/**
	 * Returns whether the node is created or not.
	 * 
	 * @return {@code true} if this node has the state CREATED; otherwise
	 *         {@code false}
	 */
	boolean isCreated();

	/**
	 * Returns whether the node is selected or not.
	 * 
	 * @return {@code true} if this node is selected; otherwise {@code false}
	 */
	boolean isSelected();

	/**
	 * Sets whether the node is selected or not.
	 * 
	 * @param selected
	 *            {@code true} if this node is selected; otherwise {@code false}
	 */
	void setSelected(boolean selected);

	/**
	 * Returns parent node of type <code>clazz</code> or <code>null</code>.
	 * 
	 * @param clazz
	 *            the type of parent node.
	 * @return the parent node of type <code>clazz</code>.
	 */
	<N extends INavigationNode<?>> N getParentOfType(Class<N> clazz);

	/**
	 * Sets set blocked state of this NavigationNode. If true, the view which
	 * presents the node is blocked for all user input.
	 * 
	 * @param blocked
	 *            true blocks user input
	 */
	void setBlocked(boolean blocked);

	/**
	 * Returns the blocked state of this NagigationNode. True means that user
	 * input is blocked for the view which presents the node.
	 * 
	 * @return true if input is blocked, false otherwise
	 */
	boolean isBlocked();

	/**
	 * Shows or hides this node depending on the value of parameter
	 * 
	 * @param visible
	 *            if {@code true}, shows this node; otherwise, hides this node
	 */
	void setVisible(boolean visible);

	/**
	 * Returns whether the node is visible or hidden.
	 * 
	 * @return {@code true} if the node is visible; otherwise {@code false}
	 */
	boolean isVisible();

	/**
	 * Enables or disables this node depending on the value of parameter
	 * 
	 * @param enabled
	 *            if {@code true}, enables this node; otherwise, disables this
	 *            node
	 */
	void setEnabled(boolean enabled);

	/**
	 * Returns whether the node is enabled or disabled.
	 * 
	 * @return {@code true} if the node is enabled; otherwise {@code false}
	 */
	boolean isEnabled();

	/**
	 * Returns the ID that identifies the node. The ID is used to find navigate
	 * targets in the application model tree and to associated sub module nodes
	 * with their views.
	 * 
	 * @see #navigate(NavigationNodeId)
	 * @return The ID that identifies the node in the application model tree.
	 */
	NavigationNodeId getNodeId();

	/**
	 * Sets the ID of the node.
	 * 
	 * @param nodeId
	 *            The ID that identifies the node in the application model tree.
	 */
	void setNodeId(NavigationNodeId nodeId);

	/**
	 * Creates the specified navigation node and adds it to the application
	 * model if does not already exist.
	 * 
	 * @param targetId
	 *            ID of the node to create. Also refers to an extension point
	 *            describing the target node that is used to create it if it
	 *            does not exist.
	 * @see INavigationAssembler
	 */
	void create(NavigationNodeId targetId);

	/**
	 * Creates the specified navigation node and adds it to the application
	 * model if does not already exist. It also adds the NavigationArgument to
	 * the context of the NavigationNode.
	 * 
	 * @param targetId
	 *            ID of the node to create. Also refers to an extension point
	 *            describing the target node that is used to create it if it
	 *            does not exist.
	 * @param argument
	 *            The optional NavigationArgument
	 * @see INavigationAssembler
	 * @see NavigationArgument
	 */
	void create(NavigationNodeId targetId, NavigationArgument argument);

	/**
	 * Creates the specified navigation node on a worker-thread and adds it to
	 * the application model if does not already exist. It also adds the
	 * NavigationArgument to the context of the NavigationNode.
	 * 
	 * @param targetId
	 *            ID of the node to create. Also refers to an extension point
	 *            describing the target node that is used to create it if it
	 *            does not exist.
	 * @param argument
	 *            The optional NavigationArgument
	 * @see INavigationAssembler
	 * @see NavigationArgument
	 * @since 3.0
	 */
	void createAsync(NavigationNodeId targetId, NavigationArgument argument);

	/**
	 * Creates the specified navigation node on a worker-thread and adds it to
	 * the application model if does not already exist. It also adds the
	 * NavigationArgument to the context of the NavigationNode.
	 * 
	 * @param targetId
	 *            ID of the node to create. Also refers to an extension point
	 *            describing the target node that is used to create it if it
	 *            does not exist.
	 * @see INavigationAssembler
	 * @since 3.0
	 */
	void createAsync(final NavigationNodeId targetId);

	/**
	 * Moves this node to the node identified by the targetId. When moving a
	 * node as child to another keep in mind that you have to honor the strict
	 * type hierarchy of the nodes in the navigation model.
	 * 
	 * @param targetId
	 *            ID of the node where this node has to be added as child.
	 */
	void moveTo(NavigationNodeId targetId);

	/**
	 * Creates the specified navigation node (if it does not already exist) and
	 * navigates to it.
	 * 
	 * @param targetId
	 *            ID of the node to navigate to. Also refers to an extension
	 *            point describing the target node that is used to create it if
	 *            it does not exist.
	 * @see INavigationAssembler
	 */
	void navigate(NavigationNodeId targetId);

	/**
	 * Creates the specified navigation node (if it does not already exist) and
	 * navigates to it.
	 * 
	 * @see INavigationAssembler
	 * @param targetId
	 *            ID of the node to navigate to. Also refers to an extension
	 *            point describing the target node that is used to create it if
	 *            it does not exist.
	 * @param argument
	 *            Contains information passed on to the target node and/or used
	 *            during its creation.
	 */
	void navigate(NavigationNodeId targetId, NavigationArgument argument);

	/**
	 * Undoes the last navigate to this node i.e. activates the last source node
	 * of a navigate(..)-call that lead to the activation of this node.
	 * 
	 * @see #navigate(NavigationNodeId)
	 */
	void navigateBack();

	/**
	 * Jumps to the specified navigation node (Creates it if does not already
	 * exist). The source node of the jump is saved to allow later
	 * {@link #jumpBack(NavigationNodeId, NavigationArgument)}
	 * 
	 * @param targetId
	 *            ID of the node to jump to. Also refers to an extension point
	 *            describing the target node that is used to create it if it
	 *            does not exist.
	 */
	void jump(NavigationNodeId targetId);

	/**
	 * Jumps to the specified navigation node (Creates it if does not already
	 * exist). The source node of the jump is saved to allow later
	 * {@link #jumpBack(NavigationNodeId, NavigationArgument)}
	 * 
	 * @param targetId
	 *            ID of the node to jump to. Also refers to an extension point
	 *            describing the target node that is used to create it if it
	 *            does not exist.
	 * @param argument
	 *            Contains information passed on to the target node and/or used
	 *            during its creation.
	 */
	void jump(NavigationNodeId targetId, NavigationArgument argument);

	/**
	 * Jumps back to the source node of the last jump to this node.
	 */
	void jumpBack();

	/**
	 * 
	 * @return True if this {@link INavigationNode} has been jumped to.
	 *         Otherwise false.
	 */
	boolean isJumpTarget();

	/**
	 * Adds the given {@link IJumpTargetListener}
	 * 
	 * @param listener
	 *            The {@link IJumpTargetListener} to add
	 */
	void addJumpTargetListener(IJumpTargetListener listener);

	/**
	 * Removes the given {@link IJumpTargetListener}
	 * 
	 * @param listener
	 *            The {@link IJumpTargetListener} to add
	 */
	void removeJumpTargetListener(IJumpTargetListener listener);

	/**
	 * Called by a NavigationProcessor to add the given marker to the node
	 * within an INavigationContext.
	 * 
	 * @param pContext
	 */
	void addMarker(INavigationContext pContext, IMarker marker);

	/**
	 * Returns the valid type of the possible child nodes.
	 * 
	 * @return type of child nodes.
	 */
	Class<C> getValidChildType();

	/**
	 * Returns the NavigationArgument attached to this node.
	 * 
	 * @return the NavigationArgument
	 */
	NavigationArgument getNavigationArgument();

	/**
	 * Sets the text of the tool tip.
	 * 
	 * @param text
	 *            text of tool tip or {@code null} if no tool tip or the default
	 *            tool tip text should be displayed
	 * 
	 * @since 4.0
	 */
	void setToolTipText(String text);

	/**
	 * Returns the text of the tool tip.
	 * 
	 * @return text of tool tip or {@code null} if no tool tip or the default
	 *         tool tip text should be displayed
	 * 
	 * @since 4.0
	 */
	String getToolTipText();

}
