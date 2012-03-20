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
package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.example.client.views.TextSubModuleView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.listener.ClickEvent;
import org.eclipse.riena.ui.ridgets.listener.IClickListener;

/**
 * Controller for the {@link TextSubModuleView} example.
 */
public class TextSubModuleController extends SubModuleController {

	public TextSubModuleController() {
		this(null);
	}

	public TextSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	/**
	 * Binds and updates the ridgets.
	 */
	@Override
	public void configureRidgets() {

		final ITextRidget textModel1 = getRidget(ITextRidget.class, "textModel1"); //$NON-NLS-1$
		textModel1.setText("type something"); //$NON-NLS-1$
		textModel1.setOutputOnly(true);
		textModel1.addClickListener(new IClickListener() {
			public void callback(final ClickEvent event) {
				if (event.getButton() == 1) {
					final IMessageBoxRidget messageBox = getRidget("messageBox");
					messageBox.setTitle("Value");
					messageBox.setText("The value of the text field: " + textModel1.getText());
					messageBox.show();
				}
			}
		});

		final ITextRidget textField = getRidget(ITextRidget.class, "textField"); //$NON-NLS-1$
		textField.bindToModel(textModel1, ITextRidget.PROPERTY_TEXT);
		textField.updateFromModel();

		final ITextRidget textModel2 = getRidget(ITextRidget.class, "textModel2"); //$NON-NLS-1$
		textModel2.setText("type something"); //$NON-NLS-1$
		textModel2.setOutputOnly(true);
		final ITextRidget textDirectWrite = getRidget(ITextRidget.class, "textDirectWrite"); //$NON-NLS-1$
		textDirectWrite.setDirectWriting(true);
		textDirectWrite.bindToModel(textModel2, ITextRidget.PROPERTY_TEXT);
		textDirectWrite.updateFromModel();

		getRidget(IActionRidget.class, "setlabel").addListener(new IActionListener() { //$NON-NLS-1$

					public void callback() {
						getNavigationNode().setLabel("new Label " + System.currentTimeMillis()); //$NON-NLS-1$
					}
				});
		getRidget(IActionRidget.class, "setparentlabel").addListener(new IActionListener() { //$NON-NLS-1$

					public void callback() {
						getNavigationNode().getParent().setLabel("new ParentLabel " + System.currentTimeMillis()); //$NON-NLS-1$
					}
				});
	}
}
