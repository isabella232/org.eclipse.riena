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
package org.eclipse.riena.example.client.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * SWT {@link ITextRidget} sample.
 */
public class TextSubModuleView extends SubModuleView {
	public TextSubModuleView() {
	}

	public static final String ID = TextSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(2, true));

		UIControlsFactory.createLabel(parent, "Text Field:"); //$NON-NLS-1$
		final Text textField = UIControlsFactory.createText(parent);
		addUIControl(textField, "textField"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Model:"); //$NON-NLS-1$
		final Text textModel1 = UIControlsFactory.createText(parent);
		addUIControl(textModel1, "textModel1"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Text Field (direct writing):"); //$NON-NLS-1$
		final Text textDirectWrite = UIControlsFactory.createText(parent);
		addUIControl(textDirectWrite, "textDirectWrite"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Model:"); //$NON-NLS-1$
		final Text textModel2 = UIControlsFactory.createText(parent);
		addUIControl(textModel2, "textModel2"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Text Area:"); //$NON-NLS-1$
		final Text textArea = UIControlsFactory.createText(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		final int heightHint = (textArea.getLineHeight() * 5) + (textArea.getBorderWidth() * 2);
		GridDataFactory.fillDefaults().hint(SWT.DEFAULT, heightHint).applyTo(textArea);
		addUIControl(textArea, "textArea"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Password Field:"); //$NON-NLS-1$
		final Text textPassword = UIControlsFactory.createText(parent, SWT.SINGLE | SWT.PASSWORD);
		textPassword.setEchoChar('*');
		addUIControl(textPassword, "textPassword"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Max. Length (<= 10 char):"); //$NON-NLS-1$
		final Text textField10 = UIControlsFactory.createText(parent);
		textField10.setTextLimit(10);
		addUIControl(textField10, "textField10"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "set Label:"); //$NON-NLS-1$
		UIControlsFactory.createButton(parent, "setLabel", "setlabel"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createLabel(parent, "set ParentLabel:"); //$NON-NLS-1$
		UIControlsFactory.createButton(parent, "setParentLabel", "setparentlabel"); //$NON-NLS-1$ //$NON-NLS-2$

		addUIControl(UIControlsFactory.createMessageBox(parent), "messageBox");
	}

}
