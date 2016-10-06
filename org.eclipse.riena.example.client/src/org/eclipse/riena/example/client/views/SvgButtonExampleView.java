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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.core.resource.IconSize;
import org.eclipse.riena.ui.swt.ImageButton;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.ImageStore;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 *
 */
public class SvgButtonExampleView extends SubModuleView {
	public static final String ID = SvgButtonExampleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		final RowLayout verticalRowLayout = new RowLayout();
		verticalRowLayout.type = SWT.VERTICAL;
		parent.setLayout(verticalRowLayout);

		createButtonComposite(parent);
	}

	/**
	 * @param parent
	 */
	private void createButtonComposite(final Composite parent) {
		final Composite buttonComposite = new Composite(parent, SWT.NONE);

		final GridLayout layout = new GridLayout();
		layout.numColumns = 2;

		final GridData data = new GridData();
		data.horizontalAlignment = SWT.BEGINNING;

		buttonComposite.setLayout(layout);

		//Ridget controlled buttons
		final Label lbl = new Label(buttonComposite, SWT.NONE);
		lbl.setText("Ridget Controlled Buttons :"); //$NON-NLS-1$
		UIControlsFactory.createLabel(buttonComposite, ""); //$NON-NLS-1$

		UIControlsFactory.createButton(buttonComposite, "", "btnAImage"); //$NON-NLS-1$ //$NON-NLS-2$
		final Label lblButtonNOIS = new Label(buttonComposite, SWT.NONE);
		lblButtonNOIS.setText("setIcon(\"cloud\", IconSize.A16)"); //$NON-NLS-1$

		UIControlsFactory.createButton(buttonComposite, "", "btn1ParamA"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createLabel(buttonComposite, "setIcon(\"clouda\")"); //$NON-NLS-1$

		UIControlsFactory.createButton(buttonComposite, "", "btnBImage"); //$NON-NLS-1$ //$NON-NLS-2$
		final Label lbl2 = new Label(buttonComposite, SWT.NONE);
		lbl2.setText("setIcon(\"cloud\", IconSize.B22)"); //$NON-NLS-1$

		UIControlsFactory.createButton(buttonComposite, "", "btn1ParamB"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createLabel(buttonComposite, "setIcon(\"cloudb\")"); //$NON-NLS-1$

		UIControlsFactory.createButton(buttonComposite, "", "btnCImage"); //$NON-NLS-1$ //$NON-NLS-2$
		final Label lbl3 = new Label(buttonComposite, SWT.NONE);
		lbl3.setText("setIcon(\"cloud\", IconSize.C32)"); //$NON-NLS-1$

		UIControlsFactory.createButton(buttonComposite, "", "btn1ParamC"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createLabel(buttonComposite, "setIcon(\"cloudc\")"); //$NON-NLS-1$

		UIControlsFactory.createButton(buttonComposite, "", "btnDImage"); //$NON-NLS-1$ //$NON-NLS-2$
		final Label lbl4 = new Label(buttonComposite, SWT.NONE);
		lbl4.setText("setIcon(\"cloud\", IconSize.D48)"); //$NON-NLS-1$

		UIControlsFactory.createButton(buttonComposite, "", "btn1ParamD"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createLabel(buttonComposite, "setIcon(\"cloudd\")"); //$NON-NLS-1$

		// Buttons without ridgets
		UIControlsFactory.createLabel(buttonComposite, "Buttons without Ridgets"); //$NON-NLS-1$
		UIControlsFactory.createLabel(buttonComposite, ""); //$NON-NLS-1$

		final Button btn1 = new Button(buttonComposite, SWT.PUSH);
		btn1.setImage(ImageStore.getInstance().getImage("cloud", IconSize.B22)); //$NON-NLS-1$
		final Label lblbtn1 = new Label(buttonComposite, SWT.NONE);
		lblbtn1.setText("Image directly set on the Widget getImage(\"cloud\",IconSize.B22)"); //$NON-NLS-1$

		final Button btn2 = new Button(buttonComposite, SWT.PUSH);
		btn2.setImage(ImageStore.getInstance().getImage("cloudb")); //$NON-NLS-1$
		final Label lblbtn2 = new Label(buttonComposite, SWT.NONE);
		lblbtn2.setText("Image directly set on the Widget getImage(\"cloudb\")"); //$NON-NLS-1$

		//Image Buttons
		UIControlsFactory.createLabel(buttonComposite, "ImageButtons: ", SWT.NONE); //$NON-NLS-1$
		UIControlsFactory.createLabel(buttonComposite, "", SWT.NONE); //$NON-NLS-1$

		UIControlsFactory.createImageButton(buttonComposite, SWT.NONE, "imageButton"); //$NON-NLS-1$
		final Label lblImageButtonR = new Label(buttonComposite, SWT.NONE);
		lblImageButtonR.setText("controlled by ridget with IconSize A16"); //$NON-NLS-1$

		final ImageButton imageButton = new ImageButton(buttonComposite, SWT.NONE);
		imageButton.setImage(ImageStore.getInstance().getImage("cloud", IconSize.B22)); //$NON-NLS-1$
		imageButton.setHoverImage(ImageStore.getInstance().getImage("cloud", IconSize.A16)); //$NON-NLS-1$
		final Label lblImageButton = new Label(buttonComposite, SWT.NONE);
		lblImageButton.setText("ImageButton with IconSize B22 and A16 as HoverImage"); //$NON-NLS-1$

		UIControlsFactory.createImageButton(buttonComposite, SWT.NONE, "imageButtonNoIconSize"); //$NON-NLS-1$
		UIControlsFactory.createLabel(buttonComposite, "controlled by ridgetd with no IconSize"); //$NON-NLS-1$

		UIControlsFactory.createImageButton(buttonComposite, SWT.NONE, "imageButtonB22"); //$NON-NLS-1$
		UIControlsFactory.createLabel(buttonComposite, "controlled by ridgetd with IconSize.B22"); //$NON-NLS-1$

	}

}
