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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import org.eclipse.riena.internal.ui.ridgets.swt.IContributionExtension.ICommandExtension;

/**
 * @since 6.2
 */
public class ToolbarItemContribution implements IContributionItem {

	private final ICommandExtension command;
	private boolean visible = true;

	public ToolbarItemContribution() {
		this.command = null;
	}

	public ToolbarItemContribution(final ICommandExtension command) {
		this.command = command;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void fill(final Composite parent) {
		System.out.println("***** FILL");

	}

	@Override
	public void fill(final Menu parent, final int index) {
		System.out.println("***** FILL");

	}

	@Override
	public void fill(final ToolBar parent, final int index) {
		final ToolItem item = parent.getItem(index - 1);
		final ToolItemScalingHelper sh = new ToolItemScalingHelper();
		sh.createSeparatorForScalingOnPosition(parent, item, -1, index);
	}

	@Override
	public void fill(final CoolBar parent, final int index) {
		System.out.println("***** FILL coolbar " + index + command);

	}

	@Override
	public String getId() {
		if (command == null) {
			return "xx";
		}
		return command.getId();
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDynamic() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isGroupMarker() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSeparator() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void saveWidgetState() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setParent(final IContributionManager parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVisible(final boolean visible) {
		this.visible = visible;

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(final String id) {
		// TODO Auto-generated method stub

	}

}