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
package org.eclipse.riena.sample.app.client.helloworld.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.sample.app.client.helloworld.controllers.HelloServerSubModuleController;

public class HelloServerSubModuleView extends SubModuleView {

	public final static String ID = HelloServerSubModuleView.class.getName();

	@Override
	public void basicCreatePartControl(final Composite parent) {
		parent.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		final FormLayout layout = new FormLayout();
		parent.setLayout(layout);

		final Button messageButton = new Button(parent, SWT.PUSH);
		messageButton.setText("Get Message"); //$NON-NLS-1$
		addUIControl(messageButton, "actionFacade"); //$NON-NLS-1$

		final Text messageText = new Text(parent, SWT.NONE);
		messageText.setBackground(new Color(parent.getDisplay(), new RGB(135, 206, 235)));
		addUIControl(messageText, "textFacade"); //$NON-NLS-1$

		final Label info = new Label(parent, SWT.None);
		info.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		info.setEnabled(false);
		info.setText("This sample demonstrates usage of riena communication in a view."); //$NON-NLS-1$
		info.setSize(300, 200);

		// layout
		final FormData data1 = new FormData();
		data1.height = 20;
		data1.width = 90;
		data1.top = new FormAttachment(0, 25);
		data1.left = new FormAttachment(0, 5);
		messageButton.setLayoutData(data1);

		final FormData data2 = new FormData();
		data2.height = 20;
		data2.width = 215;
		data2.top = new FormAttachment(0, 25);
		data2.left = new FormAttachment(messageButton, 5);
		messageText.setLayoutData(data2);

		final FormData data3 = new FormData();
		data3.height = 20;
		data3.width = 400;
		data3.top = new FormAttachment(messageButton, 25);
		info.setLayoutData(data3);

	}

	@Override
	public void setFocus() {
		super.setFocus();
	}

	@Override
	protected HelloServerSubModuleController createController(final ISubModuleNode subModuleNode) {
		return new HelloServerSubModuleController(subModuleNode);
	}

}
