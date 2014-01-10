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
package org.eclipse.riena.example.client.controllers;

import org.eclipse.core.databinding.observable.list.WritableList;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonFactory;
import org.eclipse.riena.example.client.views.ContextMenuSubModuleView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IMenuItemRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;

/**
 * Controller for the {@link ContextMenuSubModuleView} example.
 */
public class ContextMenuSubModuleController extends SubModuleController {

	public ContextMenuSubModuleController() {
		this(null);
	}

	public ContextMenuSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	/**
	 * Binds and updates the ridgets.
	 */
	@Override
	public void configureRidgets() {
		final ITextRidget textField = getRidget(ITextRidget.class, "textField"); //$NON-NLS-1$
		textField.updateFromModel();

		final IMenuItemRidget textClear = getRidget(IMenuItemRidget.class, "textClear"); //$NON-NLS-1$
		textClear.addListener(new IActionListener() {
			public void callback() {
				textField.setText(""); //$NON-NLS-1$
			}
		});

		final IMenuItemRidget itemFoo = getRidget(IMenuItemRidget.class, "itemFoo"); //$NON-NLS-1$
		itemFoo.addListener(new IActionListener() {
			public void callback() {
				textField.setText("foo"); //$NON-NLS-1$
			}
		});

		final IMenuItemRidget itemBar = getRidget(IMenuItemRidget.class, "itemBar"); //$NON-NLS-1$
		itemBar.addListener(new IActionListener() {
			public void callback() {
				textField.setText("bar"); //$NON-NLS-1$
			}
		});

		final IMenuItemRidget itemBaz = getRidget(IMenuItemRidget.class, "itemBaz"); //$NON-NLS-1$
		itemBaz.addListener(new IActionListener() {
			public void callback() {
				textField.setText("baz"); //$NON-NLS-1$
			}
		});

		final IToggleButtonRidget markerButton = getRidget(IToggleButtonRidget.class, "markerButton"); //$NON-NLS-1$
		markerButton.addListener(new IActionListener() {
			public void callback() {
				final boolean state = markerButton.isSelected();
				textClear.setVisible(!state);
			}
		});

		final WritableList personList = new WritableList(PersonFactory.createPersonList(), Person.class);
		final ITableRidget table = getRidget(ITableRidget.class, "table"); //$NON-NLS-1$

		final String[] columnPropertyNames = { Person.PROPERTY_FIRSTNAME, Person.PROPERTY_LASTNAME };
		final String[] columnHeaders = { "FirstName", "LastName" }; //$NON-NLS-1$ //$NON-NLS-2$ 
		table.bindToModel(new WritableList(personList, Person.class), Person.class, columnPropertyNames, columnHeaders);
		table.updateFromModel();

		final IMenuItemRidget tableRemoveSelected = getRidget(IMenuItemRidget.class, "tableRemove"); //$NON-NLS-1$
		tableRemoveSelected.addListener(new IActionListener() {
			public void callback() {
				final int sel = table.getSelectionIndex();
				if (sel > -1) {
					personList.remove(sel);
					table.updateFromModel();
				}
			}
		});
	}
}
