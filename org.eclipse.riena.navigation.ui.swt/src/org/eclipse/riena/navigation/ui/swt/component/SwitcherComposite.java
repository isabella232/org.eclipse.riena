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
package org.eclipse.riena.navigation.ui.swt.component;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ShellBorderRenderer;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.WidgetIdentificationSupport;

/**
 * Composite with the sub-application switcher.
 */
public class SwitcherComposite extends Composite {

	private final IApplicationNode node;

	/**
	 * Creates a new instance of {@code SwitcherComposite} and initializes it.
	 * 
	 * @param parent
	 *            a composite which will be the parent of the new instance (cannot be null)
	 * @param node
	 *            node of the application
	 */
	public SwitcherComposite(final Composite parent, final IApplicationNode node) {
		super(parent, SWT.DOUBLE_BUFFERED);
		this.node = node;
		init(parent);
	}

	/**
	 * Creates and positions the composite for the sub-application switcher.
	 * 
	 * @param parent
	 *            parent of composite
	 * @return composite
	 */
	private void init(final Composite parent) {

		Assert.isTrue(parent.getLayout() instanceof FormLayout);

		final int padding = getShellPadding();

		final FormData formData = new FormData();
		formData.top = new FormAttachment(0, getSwitchterTopMargin() + padding);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, -0);
		formData.height = getSwitchterHeight();
		setLayoutData(formData);
		setLayout(new FillLayout());
		final Widget switcher = new SubApplicationSwitcherWidget(this, SWT.NONE, node);
		WidgetIdentificationSupport.setDefaultIdentification(switcher);

	}

	/**
	 * Returns the margin between the top of the shell and the widget with the sub-application switchers.
	 * 
	 * @return margin
	 */
	public static int getSwitchterTopMargin() {

		final int margin = LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.SUB_APPLICATION_SWITCHER_TOP_MARGIN);
		return margin;

	}

	/**
	 * Returns the of the sub-application switcher.
	 * 
	 * @return height
	 */
	public static int getSwitchterHeight() {

		final int margin = LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.SUB_APPLICATION_SWITCHER_HEIGHT);
		return margin;

	}

	/**
	 * Returns the padding between shell border and content.
	 * 
	 * @return padding
	 */
	public static int getShellPadding() {

		final ShellBorderRenderer borderRenderer = (ShellBorderRenderer) LnfManager.getLnf().getRenderer(LnfKeyConstants.TITLELESS_SHELL_BORDER_RENDERER);
		return borderRenderer.getCompleteBorderWidth();

	}

}
