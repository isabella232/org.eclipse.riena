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
package org.eclipse.riena.example.ping.client.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import org.eclipse.riena.example.ping.client.nls.Messages;
import org.eclipse.riena.example.ping.client.widgets.ProgressBarWidget;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * The view of the sonar submodule.
 */
public class SonarView extends SubModuleView {

	public static final String VIEW_ID = "ping.sonar.view"; //$NON-NLS-1$
	public static final String BID_FAILURE_MESSAGE_ICON_LABEL = "failureMessageIconLabel"; //$NON-NLS-1$
	public static final String BID_FAILURE_MESSAGE_TEXT_LABEL = "failureMessageTextLabel"; //$NON-NLS-1$
	public static final String BID_FAILED_LABEL = "failedLabel"; //$NON-NLS-1$
	public static final String BID_PING_LABEL = "pingLabel"; //$NON-NLS-1$
	public static final String BID_STACK_TRACE_TEXT = "stackTraceText"; //$NON-NLS-1$
	public static final String BID_SONAR_TREE = "sonarTree"; //$NON-NLS-1$
	public static final String BID_PROGRESS_BAR = "progressBar"; //$NON-NLS-1$
	public static final String BID_PREVIOUS_ERROR_BUTTON = "previousErrorButton"; //$NON-NLS-1$
	public static final String BID_NEXT_ERROR_BUTTON = "nextErrorButton"; //$NON-NLS-1$
	public static final String BID_STOP_BUTTON = "stopButton"; //$NON-NLS-1$
	public static final String BID_START_BUTTON = "startButton"; //$NON-NLS-1$

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setLayout(new FillLayout());

		final SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);

		// left side

		final Composite left = UIControlsFactory.createComposite(sashForm, SWT.FILL);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.horizontalSpacing = 5;
		gridLayout.verticalSpacing = 5;
		gridLayout.marginHeight = 10;
		gridLayout.marginLeft = 10;
		gridLayout.marginRight = 0;
		left.setLayout(gridLayout);

		final Composite buttonComposite = UIControlsFactory.createComposite(left);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).applyTo(buttonComposite);
		final GridLayout bcLayout = new GridLayout(9, false);
		bcLayout.horizontalSpacing = 5;
		bcLayout.verticalSpacing = 5;
		bcLayout.marginHeight = 0;
		bcLayout.marginWidth = 0;
		buttonComposite.setLayout(bcLayout);

		final Button startButton = UIControlsFactory.createButton(buttonComposite);
		addUIControl(startButton, BID_START_BUTTON);
		GridDataFactory.swtDefaults().applyTo(startButton);

		final Button stopButton = UIControlsFactory.createButton(buttonComposite);
		addUIControl(stopButton, BID_STOP_BUTTON);
		GridDataFactory.swtDefaults().applyTo(stopButton);

		UIControlsFactory.createLabel(buttonComposite, Messages.pinged);
		UIControlsFactory.createLabel(buttonComposite, "  0", BID_PING_LABEL); //$NON-NLS-1$

		final Label bcSpacer = UIControlsFactory.createLabel(buttonComposite, ""); //$NON-NLS-1$
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(bcSpacer);

		UIControlsFactory.createLabel(buttonComposite, Messages.failed);
		UIControlsFactory.createLabel(buttonComposite, "  0", BID_FAILED_LABEL); //$NON-NLS-1$

		final Button nextErrorButton = UIControlsFactory.createButton(buttonComposite);
		addUIControl(nextErrorButton, BID_NEXT_ERROR_BUTTON);
		GridDataFactory.swtDefaults().applyTo(nextErrorButton);

		final Button previousErrorButton = UIControlsFactory.createButton(buttonComposite);
		addUIControl(previousErrorButton, BID_PREVIOUS_ERROR_BUTTON);
		GridDataFactory.swtDefaults().applyTo(previousErrorButton);

		final Tree sonarTree = UIControlsFactory.createTree(left, SWT.BORDER, BID_SONAR_TREE);
		sonarTree.setSize(300, 200);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(sonarTree);

		// right side

		final Composite right = UIControlsFactory.createComposite(sashForm, SWT.FILL);
		gridLayout = new GridLayout(1, false);
		gridLayout.horizontalSpacing = 5;
		gridLayout.verticalSpacing = 5;
		gridLayout.marginHeight = 10;
		gridLayout.marginLeft = 0;
		gridLayout.marginRight = 10;
		right.setLayout(gridLayout);

		final ProgressBarWidget progressBar = new ProgressBarWidget(right);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(progressBar, BID_PROGRESS_BAR);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(progressBar);

		final Composite failureMessageComposite = UIControlsFactory.createComposite(right, SWT.FILL);
		gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = 5;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		failureMessageComposite.setLayout(gridLayout);

		final Label failureMessageIconLabel = UIControlsFactory.createLabel(failureMessageComposite,
				"", BID_FAILURE_MESSAGE_ICON_LABEL); //$NON-NLS-1$
		GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(failureMessageIconLabel);

		final Label failureMessageTextLabel = UIControlsFactory.createLabel(failureMessageComposite,
				Messages.failure_message, BID_FAILURE_MESSAGE_TEXT_LABEL);
		GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(failureMessageTextLabel);

		final Text stackTraceText = UIControlsFactory.createTextMulti(right, true, true, BID_STACK_TRACE_TEXT);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(stackTraceText);

		sashForm.setWeights(new int[] { 1, 1 });
	}
}
