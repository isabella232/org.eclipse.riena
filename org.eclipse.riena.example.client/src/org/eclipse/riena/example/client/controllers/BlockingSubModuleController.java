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
package org.eclipse.riena.example.client.controllers;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.example.client.views.BlockingSubModuleView;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.uiprocess.UIProcess;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.controller.AbstractWindowController;
import org.eclipse.riena.ui.ridgets.swt.views.AbstractDialogView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Example for blocking different parts of the user interface.
 * 
 * @see BlockingSubModuleView
 */
public class BlockingSubModuleController extends SubModuleController {

	public static final String RIDGET_BLOCK_MODULE = "blockModule"; //$NON-NLS-1$
	public static final String RIDGET_BLOCK_SUB_MODULE = "blockSubModule"; //$NON-NLS-1$
	public static final String RIDGET_BLOCK_SUB_APP = "blockSubApplication"; //$NON-NLS-1$
	public static final String RIDGET_BLOCK_APPLICATION = "blockApplication"; //$NON-NLS-1$
	public static final String RIDGET_BLOCK_DIALOG = "blockDialog"; //$NON-NLS-1$
	public static final String RIDGET_DISABLE_MODULE = "disableModule"; //$NON-NLS-1$
	public static final String RIDGET_STATUS = "status"; //$NON-NLS-1$
	private static final String BTN_BLOCK = "btnBlock"; //$NON-NLS-1$
	private static final String DIALOG_STATUS = "dialogStatus"; //$NON-NLS-1$

	private ILabelRidget status;

	public BlockingSubModuleController() {
		super();
	}

	public BlockingSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	@Override
	public void configureRidgets() {
		super.configureRidgets();

		final IActionRidget blockSubModule = getRidget(IActionRidget.class, RIDGET_BLOCK_SUB_MODULE);
		blockSubModule.setText("Block current SubModule = Blocking"); //$NON-NLS-1$
		blockSubModule.addListener(new IActionListener() {
			public void callback() {
				blockNode(getNavigationNode());
			}
		});

		final IActionRidget blockModule = getRidget(IActionRidget.class, RIDGET_BLOCK_MODULE);
		blockModule.setText("Block current Module = Playground"); //$NON-NLS-1$
		blockModule.addListener(new IActionListener() {
			public void callback() {
				blockNode(getModuleNode());
			}
		});

		final IActionRidget blockSubApp = getRidget(IActionRidget.class, RIDGET_BLOCK_SUB_APP);
		blockSubApp.setText("Block current SubApplication = Playground Tab"); //$NON-NLS-1$
		blockSubApp.addListener(new IActionListener() {
			public void callback() {
				blockNode(getSubApplicationNode());
			}
		});

		final IActionRidget disableModule = getRidget(IActionRidget.class, RIDGET_DISABLE_MODULE);
		disableModule.setText("Disable current Module = Playground"); //$NON-NLS-1$
		disableModule.addListener(new IActionListener() {
			public void callback() {
				disableNode(getModuleNode());
			}
		});

		final IActionRidget disableApplication = getRidget(IActionRidget.class, RIDGET_BLOCK_APPLICATION);
		disableApplication.setText("Block application"); //$NON-NLS-1$
		disableApplication.addListener(new IActionListener() {
			public void callback() {
				blockNode(getApplicationNode());
			}
		});

		final IActionRidget disableDialog = getRidget(IActionRidget.class, RIDGET_BLOCK_DIALOG);
		disableDialog.setText("Block dialog"); //$NON-NLS-1$
		disableDialog.addListener(new IActionListener() {
			public void callback() {
				new MyDialogView().open();
			}
		});

		status = getRidget(ILabelRidget.class, RIDGET_STATUS);
	}

	// helping methods
	//////////////////

	private void blockNode(final INavigationNode<?> node) {
		final BlockerUIProcess process = new BlockerUIProcess(node, status);
		process.setBlock(true);
		process.start();
	}

	private void disableNode(final INavigationNode<?> node) {
		final BlockerUIProcess process = new BlockerUIProcess(node, status);
		process.setDisable(true);
		process.start();
	}

	private INavigationNode<?> getModuleNode() {
		return getNavigationNode().getParentOfType(IModuleNode.class);
	}

	private INavigationNode<?> getSubApplicationNode() {
		return getNavigationNode().getParentOfType(ISubApplicationNode.class);
	}

	private INavigationNode<?> getApplicationNode() {
		return getNavigationNode().getParentOfType(IApplicationNode.class);
	}

	// helping classes
	//////////////////

	private static class MyDialogView extends AbstractDialogView {

		protected MyDialogView() {
			super(null);
		}

		@Override
		protected Control buildView(final Composite parent) {
			parent.setLayout(new FillLayout());

			final Composite c = UIControlsFactory.createComposite(parent);
			UIControlsFactory.createLabel(c, "Click on the button to block the dialog for 10s", DIALOG_STATUS); //$NON-NLS-1$
			UIControlsFactory.createButton(c, "Block", BTN_BLOCK); //$NON-NLS-1$
			c.setLayout(new GridLayout());
			return c;
		}

		@Override
		protected AbstractWindowController createController() {
			return new MyDialogController();
		}

	}

	private static class MyDialogController extends AbstractWindowController {
		@Override
		public void configureRidgets() {
			super.configureRidgets();

			getRidget(IActionRidget.class, BTN_BLOCK).addListener(new IActionListener() {
				@Override
				public void callback() {
					new DialogBlockingProcess(MyDialogController.this).start();
				}
			});
		}
	}

	private static class DialogBlockingProcess extends UIProcess {

		private final MyDialogController dialogController;

		public DialogBlockingProcess(final MyDialogController dialogController) {
			super("Blocking Dialog"); //$NON-NLS-1$
			this.dialogController = dialogController;
		}

		@Override
		public void initialUpdateUI(final int totalWork) {
			dialogController.getRidget(ILabelRidget.class, DIALOG_STATUS).setText("Blocking for 10s, dialog is not closeable"); //$NON-NLS-1$
			dialogController.setBlocked(true);
		}

		@Override
		public void finalUpdateUI() {
			dialogController.setBlocked(false);
			dialogController.getRidget(ILabelRidget.class, DIALOG_STATUS).setText("Unblocked, dialog can be closed"); //$NON-NLS-1$
		}

		@Override
		public boolean runJob(final IProgressMonitor monitor) {
			try {
				Thread.sleep(10000);
			} catch (final InterruptedException e) {
				return false;
			}
			return true;
		}
	}

	/**
	 * Blocks the given {@link INavigationNode} for a number of seconds.
	 */
	private static class BlockerUIProcess extends UIProcess {

		private final INavigationNode<?> node;
		private final ILabelRidget labelRidget;
		private boolean disable;
		private boolean block;
		private String subAppToolTip;

		public BlockerUIProcess(final INavigationNode<?> node, final ILabelRidget labelRidget) {
			super("block", false); //$NON-NLS-1$
			this.node = node;
			this.labelRidget = labelRidget;
		}

		private ISubApplicationNode getSubApplication() {
			return node instanceof ISubApplicationNode ? (ISubApplicationNode) node : node.getParentOfType(ISubApplicationNode.class);
		}

		public void setBlock(final boolean doBlock) {
			this.block = doBlock;
		}

		public void setDisable(final boolean doDisable) {
			this.disable = doDisable;
		}

		@Override
		public void initialUpdateUI(final int totalWork) {
			final ISubApplicationNode subApplication = getSubApplication();
			if (subApplication != null) {
				subAppToolTip = subApplication.getToolTipText();
				subApplication.setToolTipText(String.format("Node '%s' is blocked", node.getLabel())); //$NON-NLS-1$
			}
			labelRidget.setText(String.format("Changing '%s' for 10s", node.getLabel())); //$NON-NLS-1$
			if (block) {
				node.setBlocked(true);
			}
			if (disable) {
				node.setEnabled(false);
			}
		}

		@Override
		public void finalUpdateUI() {
			labelRidget.setText(String.format("Restored '%s'", node.getLabel())); //$NON-NLS-1$
			if (block) {
				node.setBlocked(false);
			}
			if (disable) {
				node.setEnabled(true);
				node.activate();
			}
			final ISubApplicationNode subApplication = getSubApplication();
			if (subApplication != null) {
				subApplication.setToolTipText(subAppToolTip);
			}
		}

		@Override
		public boolean runJob(final IProgressMonitor monitor) {
			try {
				Thread.sleep(10000);
			} catch (final InterruptedException e) {
				return false;
			}
			return true;
		}
	}
}
