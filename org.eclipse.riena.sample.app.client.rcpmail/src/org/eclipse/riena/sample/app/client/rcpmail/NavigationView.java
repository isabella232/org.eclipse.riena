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
package org.eclipse.riena.sample.app.client.rcpmail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;

import org.eclipse.riena.ui.ridgets.controller.IController;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtBindingDelegate;

public class NavigationView extends ViewPart {
	public static final String ID = "org.eclipse.riena.sample.app.client.rcpmail.navigationView"; //$NON-NLS-1$

	private final DefaultSwtBindingDelegate delegate = new DefaultSwtBindingDelegate();
	private final IController controller = new NavigationController();
	private Tree tree;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		tree = new Tree(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		delegate.addUIControl(tree, "tree"); //$NON-NLS-1$

		delegate.injectAndBind(controller);
		parent.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(final DisposeEvent e) {
				delegate.unbind(controller);
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		tree.setFocus();
	}
}