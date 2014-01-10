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
package org.eclipse.riena.example.ping.client.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;

import org.eclipse.riena.core.ping.IPingable;
import org.eclipse.riena.core.ping.PingResult;
import org.eclipse.riena.core.ping.PingVisitor;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.example.ping.client.model.NoPingableFound;
import org.eclipse.riena.example.ping.client.model.PingResultTreeNode;
import org.eclipse.riena.example.ping.client.model.PingableTreeNode;
import org.eclipse.riena.example.ping.client.nls.Messages;
import org.eclipse.riena.example.ping.client.ridgets.ProgressbarRidget;
import org.eclipse.riena.example.ping.client.views.SonarView;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.uiprocess.UIProcess;
import org.eclipse.riena.ui.core.uiprocess.UISynchronizer;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.listener.ISelectionListener;
import org.eclipse.riena.ui.ridgets.listener.SelectionEvent;
import org.eclipse.riena.ui.ridgets.tree2.ITreeNode;

/**
 * This is the main controller for the sonar SubModule.
 */
public class SonarController extends SubModuleController {

	private static enum SearchDirection {
		previous, next;
	}

	private static final String STACKFRAME_ICON = "stackframe.gif"; //$NON-NLS-1$
	private static final String NEXT_ERROR_ICON = "select_next.gif"; //$NON-NLS-1$
	private static final String PREVIOUS_ERROR_ICON = "select_prev.gif"; //$NON-NLS-1$
	private static final String STOP_ICON = "stop.gif"; //$NON-NLS-1$
	private static final String START_ICON = "relaunch.gif"; //$NON-NLS-1$

	private ProgressbarRidget progressRidget;

	private IActionRidget startAction;
	private IActionRidget stopAction;
	private IActionRidget previousErrorAction;
	private IActionRidget nextErrorAction;

	private ITreeRidget treeRidget;

	private ITextRidget stackTraceTextRidget;

	private ILabelRidget pingLabel;
	private ILabelRidget failureLabel;
	private ILabelRidget failureMessageIconRidget;
	private ILabelRidget failureMessageTextRidget;

	private PingableTreeNode[] rootNodes;
	private Cursor oldCursor;

	@Override
	public void configureRidgets() {

		super.configureRidgets();

		stackTraceTextRidget = getRidget(SonarView.BID_STACK_TRACE_TEXT);

		treeRidget = getRidget(SonarView.BID_SONAR_TREE);
		treeRidget.addSelectionListener(new ISelectionListener() {
			public void ridgetSelected(final SelectionEvent event) {
				treeNodeSelected();
			}
		});

		failureMessageIconRidget = getRidget(SonarView.BID_FAILURE_MESSAGE_ICON_LABEL);
		failureMessageIconRidget.setIcon(STACKFRAME_ICON);
		failureMessageIconRidget.setEnabled(false);

		failureMessageTextRidget = getRidget(SonarView.BID_FAILURE_MESSAGE_TEXT_LABEL);
		failureMessageTextRidget.setEnabled(false);

		progressRidget = getRidget(SonarView.BID_PROGRESS_BAR);
		progressRidget.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		progressRidget.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
		progressRidget.updateFromModel();

		startAction = getRidget(SonarView.BID_START_BUTTON);
		startAction.setIcon(START_ICON);
		startAction.setToolTipText(Messages.start_tooltip);
		startAction.addListener(new IActionListener() {
			public void callback() {
				startSonar();
			}
		});

		stopAction = getRidget(SonarView.BID_STOP_BUTTON);
		stopAction.setIcon(STOP_ICON);
		stopAction.setToolTipText(Messages.stop_tooltip);
		stopAction.setEnabled(false);

		previousErrorAction = getRidget(SonarView.BID_PREVIOUS_ERROR_BUTTON);
		previousErrorAction.setIcon(PREVIOUS_ERROR_ICON);
		previousErrorAction.setToolTipText(Messages.previous_error_tooltip);
		previousErrorAction.setEnabled(false);
		previousErrorAction.addListener(new IActionListener() {
			public void callback() {
				selectPreviousFailure();
			}
		});

		nextErrorAction = getRidget(SonarView.BID_NEXT_ERROR_BUTTON);
		nextErrorAction.setIcon(NEXT_ERROR_ICON);
		nextErrorAction.setToolTipText(Messages.next_error_tooltip);
		nextErrorAction.setEnabled(false);
		nextErrorAction.addListener(new IActionListener() {
			public void callback() {
				selectNextFailure();
			}
		});

		pingLabel = getRidget(SonarView.BID_PING_LABEL);
		failureLabel = getRidget(SonarView.BID_FAILED_LABEL);
	}

	@Override
	public void afterBind() {
		super.afterBind();

		((Tree) treeRidget.getUIControl()).addTreeListener(new TreeListener() {

			public void treeExpanded(final TreeEvent e) {
				if (e.item.getData() instanceof PingResultTreeNode) {
					final PingResultTreeNode node = (PingResultTreeNode) e.item.getData();
					UISynchronizer.createSynchronizer().asyncExec(new Runnable() {
						public void run() {
							final List<ITreeNode> children = node.getChildren();
							if (children != null) {
								for (final ITreeNode iTreeNode : children) {
									treeRidget.refresh(iTreeNode);
								}
							}
						}
					});
				}
			}

			public void treeCollapsed(final TreeEvent e) {
				Nop.reason("nothing to do here"); //$NON-NLS-1$
			}
		});
	}

	/**
	 * Updates the UI state.
	 */
	protected void updateUI() {
		updateNextPreviousButtons();
		updateAllRidgetsFromModel();
	}

	/**
	 * Returns the selected TreeNode or <code>null</code> if there is none.
	 * 
	 * @return the selected TreeNode.
	 */
	protected PingResultTreeNode getSelectedTreeNode() {
		final List<Object> selection = treeRidget.getSelection();
		if (selection.size() > 0) {
			return (PingResultTreeNode) selection.get(0);
		}
		return null;
	}

	/**
	 * Called if a TreeNode is selected. It shows the failure message of the
	 * selected node (if there is one) and updates the state of the
	 * next/previous failure buttons.
	 */
	protected void treeNodeSelected() {
		boolean hasFailure = false;
		final PingResultTreeNode selectedNode = getSelectedTreeNode();
		if (selectedNode != null && selectedNode.getPingResult() != null) {
			stackTraceTextRidget.setText(selectedNode.getPingResult().getPingFailure());
			hasFailure = selectedNode.getPingResult().hasPingFailed();
		}
		failureMessageIconRidget.setEnabled(hasFailure);
		failureMessageTextRidget.setEnabled(hasFailure);

		updateNextPreviousButtons();
	}

	/**
	 * Updates the enabled state of the next/previous failure button, depending
	 * on whether there is a next/previous failure to show.
	 */
	protected void updateNextPreviousButtons() {
		nextErrorAction.setEnabled(getFailureNode(SearchDirection.next) != null);
		previousErrorAction.setEnabled(getFailureNode(SearchDirection.previous) != null);
	}

	/**
	 * Selects the first node that has a failure message, if there is one.
	 */
	protected void selectFirstFailure() {
		final PingResultTreeNode nextFailureNode = getFailureNode(SearchDirection.next, null);
		if (nextFailureNode != null) {
			treeRidget.setSelection(nextFailureNode);
		}
		updateNextPreviousButtons();
	}

	/**
	 * Selects the next node that has a failure message, if there is one.
	 */
	protected void selectNextFailure() {
		final PingResultTreeNode nextFailureNode = getFailureNode(SearchDirection.next);
		if (nextFailureNode != null) {
			treeRidget.setSelection(nextFailureNode);
		}
		updateNextPreviousButtons();
	}

	/**
	 * Selects the previous node that has a failure message, if there is one.
	 */
	protected void selectPreviousFailure() {
		final PingResultTreeNode nextFailureNode = getFailureNode(SearchDirection.previous);
		if (nextFailureNode != null) {
			treeRidget.setSelection(nextFailureNode);
		}
		updateNextPreviousButtons();
	}

	/**
	 * Returns the next/previous node that has a failure message, starting the
	 * search from the currently selected node.
	 * 
	 * @param searchDirection
	 *            if <code>true</code> the next failure will searched, otherwise
	 *            the
	 * @return the failure node if there is one.
	 */
	private PingResultTreeNode getFailureNode(final SearchDirection searchDirection) {
		final PingResultTreeNode selectedTreeNode = getSelectedTreeNode();
		return getFailureNode(searchDirection, selectedTreeNode);
	}

	/**
	 * Returns the next/previous node that has a failure message, starting the
	 * search from the given node.
	 * 
	 * @param searchDirection
	 *            if <code>true</code> the next failure will searched, otherwise
	 *            the
	 * @param startFrom
	 *            the node to start the search from. If <code>null</code>, the
	 *            search is started from root.
	 * @return the failure node if there is one.
	 */
	private PingResultTreeNode getFailureNode(final SearchDirection searchDirection, final PingResultTreeNode startFrom) {
		final List<PingResultTreeNode> nodes = flattenTree();
		if (nodes.size() == 0) {
			return null;
		}
		int end = nodes.size();
		int increment = 1;
		if (searchDirection == SearchDirection.previous) {
			end = 0;
			increment = -1;
		}
		int start = nodes.indexOf(startFrom);
		if (start < 0) {
			if (searchDirection == SearchDirection.next) {
				start = 0;
			} else {
				start = nodes.size() - 1;
			}
		} else {
			start += increment;
		}
		for (int i = start; i != end; i += increment) {
			if (i > nodes.size() || i < 0) {
				return null;
			}
			final PingResultTreeNode currentNode = nodes.get(i);
			if (currentNode.hasPingFailed()) {
				return currentNode;
			}
		}
		return null;
	}

	/**
	 * Returns the tree structure as a flattened List in depth first order.
	 * 
	 * @return the tree structure as a flattened List.
	 */
	protected List<PingResultTreeNode> flattenTree() {
		final PingableTreeNode[] nodes = getRootNodes();
		if (nodes == null || nodes.length == 0) {
			return Collections.EMPTY_LIST;
		}
		final List<PingResultTreeNode> result = new ArrayList<PingResultTreeNode>();
		for (final PingableTreeNode current : nodes) {
			flattenTree(current, result);
		}
		return result;
	}

	private void flattenTree(final PingResultTreeNode current, final List<PingResultTreeNode> result) {
		result.add(current);
		final List<ITreeNode> children = current.getChildren();
		for (final ITreeNode child : children) {
			flattenTree((PingResultTreeNode) child, result);
		}
	}

	/**
	 * Returns the root nodes.
	 * 
	 * @return the root nodes.
	 */
	protected PingableTreeNode[] getRootNodes() {
		return rootNodes;
	}

	/**
	 * Sets the root nodes of the tree.
	 * 
	 * @param nodes
	 *            the root nodes.
	 */
	protected void setRootNodes(final PingableTreeNode[] nodes) {
		this.rootNodes = nodes;
		treeRidget.bindToModel(nodes, PingResultTreeNode.class, ITreeNode.PROPERTY_CHILDREN, ITreeNode.PROPERTY_PARENT,
				"label", null, null, "icon", "icon"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		treeRidget.updateFromModel();
	}

	/**
	 * Starts a new {@link SonarUIProcess}.
	 */
	protected void startSonar() {
		final SonarUIProcess process = new SonarUIProcess();
		process.start();
	}

	/**
	 * Creates the root nodes for the given {@link IPingable} services.
	 * 
	 * @param pingableServices
	 *            the pingables acting as root nodes.
	 * @return the root nodes.
	 */
	protected PingableTreeNode[] createRootNodes(final List<IPingable> pingableServices) {
		final PingableTreeNode[] result = new PingableTreeNode[pingableServices.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = new PingableTreeNode(pingableServices.get(i));
		}
		return result;
	}

	/**
	 * Retrieves all osgi services that implement {@link IPingable}.
	 * 
	 * @return all {@link IPingable} services.
	 */
	@SuppressWarnings("unchecked")
	protected List<IPingable> getPingableServices() {
		// fetch ALL services
		final BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		ServiceReference[] allServiceReferences = null;
		try {
			allServiceReferences = context.getAllServiceReferences(null, null);
		} catch (final InvalidSyntaxException e) {
			throw new RuntimeException("This should never happen since we do not use a filter?!?", e); //$NON-NLS-1$
		}

		// no services at all :-(
		if (allServiceReferences == null) {
			return Collections.EMPTY_LIST;
		}

		final List<IPingable> result = new ArrayList<IPingable>();

		// filter the pingable services
		for (final ServiceReference serviceReference : allServiceReferences) {
			final Object service = context.getService(serviceReference);
			if (service instanceof IPingable) {
				result.add((IPingable) service);
			}
		}
		if (result.isEmpty()) {
			result.add(new NoPingableFound());
		}
		return result;
	}

	protected void setShowHourGlassCursor(final boolean show) {
		final Control uiControl = (Control) getWindowRidget().getUIControl();
		if (show) {
			oldCursor = uiControl.getCursor();
			final Cursor waitCursor = uiControl.getDisplay().getSystemCursor(SWT.CURSOR_WAIT);
			uiControl.setCursor(waitCursor);
		} else {
			if (oldCursor != null) {
				uiControl.setCursor(oldCursor);
				oldCursor = null;
			}
		}
	}

	/**
	 * The {@link UIProcess} that pings all
	 * {@link SonarController#getRootNodes() IPingables}.
	 */
	private class SonarUIProcess extends UIProcess {

		private volatile int pinged = 0;
		private volatile int pingedRootNodes = 0;
		private volatile boolean canceled;
		private volatile int failureCount = 0;
		private volatile boolean endRun;
		private volatile PingResultTreeNode currentNode;
		private volatile boolean expand = false;
		private final IActionListener stopActionListener = new IActionListener() {
			public void callback() {
				canceled = true;
				end();
			}
		};

		/**
		 * Default constructor.
		 */
		public SonarUIProcess() {
			super(Messages.sonar, false);
		}

		@Override
		public void initialUpdateUI(final int totalWork) {
			final List<IPingable> pingableServices = getPingableServices();
			final PingableTreeNode[] nodes = createRootNodes(pingableServices);
			setRootNodes(nodes);

			progressRidget.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
			progressRidget.setMinimum(0);
			progressRidget.setMaximum(nodes.length);
			progressRidget.setSelection(pinged);

			stackTraceTextRidget.setText(""); //$NON-NLS-1$

			pingLabel.setText(Integer.toString(pinged));
			failureLabel.setText(Integer.toString(failureCount));

			stopAction.addListener(stopActionListener);
			stopAction.setEnabled(true);
			startAction.setEnabled(false);
			nextErrorAction.setEnabled(false);
			previousErrorAction.setEnabled(false);
			failureMessageIconRidget.setEnabled(false);
			failureMessageTextRidget.setEnabled(false);

			SonarController.this.setShowHourGlassCursor(false);
		}

		@Override
		public boolean runJob(final IProgressMonitor monitor) {
			for (final PingableTreeNode pingableNode : getRootNodes()) {

				if (canceled) {
					break;
				}

				PingVisitor visitor = new PingVisitor();
				visitor = visitor.ping(pingableNode.getPingable());
				if (canceled) {
					break;
				}

				final List<PingResult> pingResults = visitor.getPingResults();
				++pinged;
				++pingedRootNodes;
				final PingResult pingResult = pingResults.get(0);
				pingableNode.setPingResult(pingResult);
				if (pingResult.hasPingFailed()) {
					++failureCount;
				}

				createChildNodesUISync(pingableNode);

				monitor.worked(pinged);
				notifyUpdateUI();
			}
			return true;
		}

		/**
		 * Calls {@link #createChildNodes(PingResultTreeNode)} using a
		 * {@link UISynchronizer}.
		 * 
		 * @param parent
		 *            the node for which to create the child nodes.
		 */
		private void createChildNodesUISync(final PingResultTreeNode parent) {
			UISynchronizer.createSynchronizer().asyncExec(new Runnable() {
				public void run() {
					createChildNodes(parent);
				}
			});
		}

		/**
		 * Recursively creates child nodes for the given node based on its
		 * {@link PingResult}.
		 * 
		 * @param parent
		 *            the node for which to create the child nodes.
		 */
		private void createChildNodes(final PingResultTreeNode parent) {
			currentNode = parent;
			expand = true;
			updateUi();

			final PingResult pingResult = parent.getPingResult();
			final Iterable<PingResult> nestedResults = pingResult.getNestedResults();
			boolean failed = false;

			for (final PingResult nested : nestedResults) {
				++pinged;
				final PingResultTreeNode child = new PingResultTreeNode(parent, nested.getPingableName());
				child.setPingResult(nested);
				if (nested.hasPingFailed()) {
					++failureCount;
					failed = true;
				}
				// updateUi();
				treeRidget.setSelection(child);
				createChildNodes(child);
			}
			currentNode = parent;
			expand = failed;
			updateUi();
		}

		@Override
		public void updateUi() {
			if (canceled) {
				return;
			}

			if (currentNode != null) {
				if (expand) {
					treeRidget.expand(currentNode);
				} else {
					treeRidget.collapse(currentNode);
				}
			}

			treeRidget.updateFromModel();

			if (failureCount > 0) {
				progressRidget.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
			}
			progressRidget.setSelection(pingedRootNodes);
			pingLabel.setText(Integer.toString(pinged));
			failureLabel.setText(Integer.toString(failureCount));
		}

		@Override
		public void finalUpdateUI() {
			end();
		}

		@Override
		protected int getTotalWork() {
			if (getRootNodes() == null) {
				return 0;
			}
			return getRootNodes().length;
		}

		/**
		 * En-/disables all actions as appropriate and selects first failure
		 * node. The code is run just once, so any subsequent calls have not
		 * effect.
		 */
		protected void end() {
			if (endRun) {
				return;
			}
			endRun = true;

			stopAction.removeListener(stopActionListener);
			stopAction.setEnabled(false);
			startAction.setEnabled(true);

			treeRidget.updateFromModel();

			SonarController.this.updateUI();
			selectFirstFailure();

			SonarController.this.setShowHourGlassCursor(false);
		}
	}
}
