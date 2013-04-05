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

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import org.eclipse.riena.ui.ridgets.controller.IController;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtBindingDelegate;

public class MessageView extends ViewPart {

	public static final String ID = "org.eclipse.riena.sample.app.client.rcpmail.view"; //$NON-NLS-1$

	private final DefaultSwtBindingDelegate delegate = new DefaultSwtBindingDelegate();
	private final IController controller = new MessageController();

	@Override
	public void createPartControl(final Composite parent) {
		final Composite top = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		top.setLayout(layout);
		// top banner
		final Composite banner = new Composite(top, SWT.NONE);
		banner.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_BEGINNING, true,
				false));
		layout = new GridLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 10;
		layout.numColumns = 2;
		banner.setLayout(layout);

		// setup bold font
		final Font boldFont = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);

		Label l = new Label(banner, SWT.WRAP);
		l.setText("Subject:"); //$NON-NLS-1$
		l.setFont(boldFont);
		delegate.addUIControl(new Label(banner, SWT.WRAP), "subject"); //$NON-NLS-1$

		l = new Label(banner, SWT.WRAP);
		l.setText("From:"); //$NON-NLS-1$
		l.setFont(boldFont);
		delegate.addUIControl(new Label(banner, SWT.WRAP), "from"); //$NON-NLS-1$

		l = new Label(banner, SWT.WRAP);
		l.setText("Date:"); //$NON-NLS-1$
		l.setFont(boldFont);
		delegate.addUIControl(new Label(banner, SWT.WRAP), "date"); //$NON-NLS-1$

		// message contents
		final Text text = new Text(top, SWT.MULTI | SWT.WRAP);
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
		delegate.addUIControl(text, "message"); //$NON-NLS-1$

		delegate.injectAndBind(controller);
		parent.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(final DisposeEvent e) {
				delegate.unbind(controller);
			}
		});
	}

	@Override
	public void setFocus() {
	}
}
