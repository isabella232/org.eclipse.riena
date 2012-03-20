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
package org.eclipse.riena.example.client.views;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * View of the sub module that shows a set of UI controls.
 */
public class RidgetsSubModuleView extends SubModuleView {

	public static final String ID = RidgetsSubModuleView.class.getName();

	private Image scaledQuestionImage;
	private Image scaledWarningImage;

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new FormLayout());

		final Group buttonGroup = createButtonGroup(parent);
		buttonGroup.setLayoutData(createFormData(null));

		final Group imageButtonGroup = createImageButtonGroup(parent);
		imageButtonGroup.setLayoutData(createFormData(buttonGroup));
	}

	@Override
	public void dispose() {
		super.dispose();
		if (scaledQuestionImage != null) {
			scaledQuestionImage.dispose();
			scaledQuestionImage = null;
		}
		if (scaledWarningImage != null) {
			scaledWarningImage.dispose();
			scaledWarningImage = null;
		}
	}

	// helping methods
	//////////////////

	/**
	 * Creates a group with different buttons.
	 * 
	 * @param parent
	 *            the parent of the group
	 * @return the Group; never null.
	 */
	private Group createButtonGroup(final Composite parent) {
		final Group buttonGroup = UIControlsFactory.createGroup(parent, "Buttons"); //$NON-NLS-1$
		buttonGroup.setLayout(new RowLayout(SWT.VERTICAL));

		UIControlsFactory.createButtonToggle(buttonGroup, "", "toggleOne"); //$NON-NLS-1$ //$NON-NLS-2$

		final Button toggleTwo = UIControlsFactory.createButtonToggle(buttonGroup, "", "toggleTwo"); //$NON-NLS-1$ //$NON-NLS-2$
		final Display display = parent.getDisplay();
		Image image = display.getSystemImage(SWT.ICON_QUESTION);
		Assert.isTrue(scaledQuestionImage == null);
		scaledQuestionImage = new Image(display, image.getImageData().scaledTo(16, 16));
		toggleTwo.setImage(scaledQuestionImage);

		UIControlsFactory.createButtonCheck(buttonGroup, "", "checkOne"); //$NON-NLS-1$ //$NON-NLS-2$

		UIControlsFactory.createButton(buttonGroup, "", "buttonOne"); //$NON-NLS-1$ //$NON-NLS-2$

		final Button buttonTwo = UIControlsFactory.createButton(buttonGroup, "", "buttonTwo"); //$NON-NLS-1$ //$NON-NLS-2$
		image = display.getSystemImage(SWT.ICON_WARNING);
		Assert.isTrue(scaledWarningImage == null);
		scaledWarningImage = new Image(display, image.getImageData().scaledTo(16, 16));
		buttonTwo.setImage(scaledWarningImage);

		return buttonGroup;
	}

	private Group createImageButtonGroup(final Composite parent) {
		final Group imageButtonGroup = UIControlsFactory.createGroup(parent, "Image Buttons"); //$NON-NLS-1$
		imageButtonGroup.setLayout(new RowLayout(SWT.VERTICAL));

		UIControlsFactory.createImageButton(imageButtonGroup, SWT.NONE, "imageButton"); //$NON-NLS-1$
		UIControlsFactory.createImageButton(imageButtonGroup, SWT.NONE, "arrowButton"); //$NON-NLS-1$
		UIControlsFactory.createImageButton(imageButtonGroup, SWT.HOT, "arrowHotButton"); //$NON-NLS-1$

		return imageButtonGroup;
	}

	private FormData createFormData(final Control topControl) {
		final FormData result = new FormData();
		if (topControl != null) {
			result.top = new FormAttachment(topControl, 10);
		} else {
			result.top = new FormAttachment(0, 5);
		}
		result.left = new FormAttachment(0, 5);
		result.right = new FormAttachment(100, -5);
		return result;
	}
}
