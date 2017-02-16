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
package org.eclipse.riena.example.client.views;

import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 *
 */
public class SvgPlaygroundView extends SubModuleView {
	public static final String ID = SvgPlaygroundView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setLayout(new RowLayout());
		UIControlsFactory.createButton(parent, "", "button1");
		UIControlsFactory.createButton(parent, "", "button2");
		UIControlsFactory.createButton(parent, "", "button3");
	}

}
