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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.navigation.ui.swt.views.SubModuleView#basicCreatePartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setLayout(new RowLayout());
		UIControlsFactory.createButton(parent, "", "button1");
		UIControlsFactory.createButton(parent, "", "button2");
		UIControlsFactory.createButton(parent, "", "button3");
		UIControlsFactory.createLabel(parent, "", "lbl");
		//		final Button btn1 = new Button(parent, SWT.NONE);
		//		btn1.setImage(ImageStore.getInstance().getImage("RuV_CONNECT_4", IconSize.));
		//		final Button btn2 = new Button(parent, SWT.NONE);
		//		btn2.setImage(ImageStore.getInstance().getImage("RuV_CONNECT_4", IconSize.D48));
		//		final Button btn3 = new Button(parent, SWT.NONE);
		//		btn3.setImage(ImageStore.getInstance().getImage("RuV_CONNECT_4", IconSize.F128));
		//		final Button btn4 = new Button(parent, SWT.NONE);
		//		btn4.setImage(ImageStore.getInstance().getImage("rechteck_a_verti", IconSize.F128));
		//		final Button btn5 = new Button(parent, SWT.NONE);
		//		btn5.setImage(ImageStore.getInstance().getImage("rechteck_a_hori", IconSize.F128));

	}

}
