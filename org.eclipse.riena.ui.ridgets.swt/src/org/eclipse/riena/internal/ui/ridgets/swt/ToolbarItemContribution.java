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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import org.eclipse.riena.internal.ui.ridgets.swt.IContributionExtension.ICommandExtension;

/**
 * This class is our own implementation of the IContributions to support creation of Separators for the toolbar.
 * 
 * @since 6.2
 */
public class ToolbarItemContribution implements IContributionItem {

	private final ICommandExtension command;
	private boolean visible = true;
	private boolean isSeparator = true;

	public ToolbarItemContribution() {
		this.command = null;
	}

	public ToolbarItemContribution(final ICommandExtension command) {
		this.command = command;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void fill(final Composite parent) {

	}

	@Override
	public void fill(final Menu parent, final int index) {

	}

	@Override
	public void fill(final ToolBar parent, final int index) {
		if (isFirstItemOfToolBar(index)) {
			createFirstSeparator(parent);
			return;
		}
		final ToolItem item = parent.getItem(index - 1);
		final ToolItemScalingHelper scalingHelper = new ToolItemScalingHelper();
		scalingHelper.createSeparatorForScalingOnPosition(parent, item, -1, index);
	}

	private boolean isFirstItemOfToolBar(final int index) {
		return index == 0;
	}

	private void createFirstSeparator(final ToolBar parent) {
		final ToolItemScalingHelper scalingHelper = new ToolItemScalingHelper();

		final ToolItem separator = new ToolItem(parent, SWT.SEPARATOR, 0);
		separator.setWidth(scalingHelper.calculateSclaingBasedSpacingBetweenToolBars());
		final Composite composite = new Composite(parent, SWT.NONE);
		//		composite.setBackground(new Color(SwtUtilities.getDisplay(), 255, 0, 0));
		composite.setData("Separator", "Separator Composite");
		separator.setControl(composite);
		separator.setEnabled(false);
		separator.setData("isFirstSeparator", true);
	}

	@Override
	public void fill(final CoolBar parent, final int index) {

	}

	@Override
	public String getId() {
		if (command == null) {
			return "xx"; //$NON-NLS-1$
		}
		return command.getId();
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isDynamic() {
		return false;
	}

	@Override
	public boolean isGroupMarker() {
		return false;
	}

	@Override
	public boolean isSeparator() {
		return isSeparator;
	}

	public void setIsSeparator(final boolean isSeparator) {
		this.isSeparator = isSeparator;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void saveWidgetState() {

	}

	@Override
	public void setParent(final IContributionManager parent) {

	}

	@Override
	public void setVisible(final boolean visible) {
		this.visible = visible;

	}

	@Override
	public void update() {

	}

	@Override
	public void update(final String id) {

	}

}