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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.observable.list.WritableList;

import org.eclipse.riena.beans.common.StringBean;
import org.eclipse.riena.example.client.views.CheckBoxTableSubModuleView;
import org.eclipse.riena.internal.example.client.beans.Extra;
import org.eclipse.riena.internal.example.client.beans.Extra.Category;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IDecimalTextRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.listener.ISelectionListener;
import org.eclipse.riena.ui.ridgets.listener.SelectionEvent;
import org.eclipse.riena.ui.ridgets.swt.NumberColumnFormatter;

/**
 * Controller for the {@link CheckBoxTableSubModuleView} example.
 */
public class CheckBoxTableSubModuleController extends SubModuleController {

	private final StringBean choiceSelection = new StringBean("free"); //$NON-NLS-1$
	private double totalPrice;
	private ISingleChoiceRidget choice;
	private ITableRidget table;
	private IDecimalTextRidget total;
	private List<Extra> input;

	@Override
	public void configureRidgets() {

		super.configureRidgets();

		choice = getRidget(ISingleChoiceRidget.class, "choice"); //$NON-NLS-1$
		final List<String> labels = Arrays.asList(new String[] { "Free Choice", "Standard (output-only)", "No Extras (disabled)" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		final List<String> options = Arrays.asList(new String[] { "free", "standard", "no" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		choice.bindToModel(options, labels, choiceSelection, StringBean.PROP_VALUE);
		choice.updateFromModel();

		table = getRidget(ITableRidget.class, "table"); //$NON-NLS-1$
		final String[] columnPropertyNames = { "selected", "name", "category", "price" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
		input = createInput();
		table.setNativeToolTip(false);
		final WritableList extrasList = new WritableList(input, Extra.class);
		table.bindToModel(extrasList, Extra.class, columnPropertyNames, null);
		table.updateFromModel();
		table.setSortedColumn(-1);
		table.setColumnFormatter(3, new NumberColumnFormatter(Double.class, 2) {
			@Override
			protected Number getValue(final Object element) {
				return ((Extra) element).getPrice();
			}
		});
		for (final Extra extra : input) {
			extra.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(final PropertyChangeEvent evt) {
					if (evt.getPropertyName().equals(Extra.PROPERTY_SELECTED)) {
						updateTotal();
					}
				}
			});
		}

		total = getRidget(IDecimalTextRidget.class, "total"); //$NON-NLS-1$
		total.setPrecision(2);
		total.bindToModel(this, "totalPrice"); //$NON-NLS-1$
		total.setOutputOnly(true);

		choice.addSelectionListener(new ISelectionListener() {
			public void ridgetSelected(final SelectionEvent event) {
				updateTableMarker();
			}
		});

		table.addSelectionListener(new ISelectionListener() {
			public void ridgetSelected(final SelectionEvent event) {
				if (event.getNewSelection() != null) {
					System.out.println(event.getNewSelection());
				} else {
					System.out.println("no selection"); //$NON-NLS-1$
				}
			}
		});

		updateTableMarker();
		updateTotal();

	}

	private void updateTotal() {

		double price = 0.0;
		for (final Extra extra : input) {
			if (extra.isSelected()) {
				price += extra.getPrice();
			}
		}
		setTotalPrice(price);
		total.updateFromModel();

	}

	private void updateTableMarker() {
		if (choiceSelection.getValue().equals("standard")) { //$NON-NLS-1$
			table.setEnabled(true);
			table.setOutputOnly(true);
			for (final Extra extra : input) {
				extra.setDefault();
			}
		} else if (choiceSelection.getValue().equals("no")) { //$NON-NLS-1$
			table.setEnabled(false);
			table.setOutputOnly(false);
			for (final Extra extra : input) {
				extra.setSelected(false);
			}
		} else {
			table.setEnabled(true);
			table.setOutputOnly(false);
			for (final Extra extra : input) {
				extra.setDefault();
			}
		}
	}

	private List<Extra> createInput() {
		final List<Extra> extras = new ArrayList<Extra>();

		extras.add(new Extra("Manual Transmission", Category.PERFORMANCE, 0.0f, true)); //$NON-NLS-1$
		extras.add(new Extra("Variable sport steering", Category.PERFORMANCE, 0.0f, false)); //$NON-NLS-1$
		extras.add(new Extra("Sport automatic transmission with shift paddles", Category.PERFORMANCE, 500.0f, false)); //$NON-NLS-1$

		extras.add(new Extra("Split fold-down-rear seat", Category.CONVENIENCE, 475.0f, true)); //$NON-NLS-1$
		extras.add(new Extra("Heated Steering Wheel", Category.CONVENIENCE, 190.0f, false)); //$NON-NLS-1$
		extras.add(new Extra("Power front seats with driver seat memory", Category.CONVENIENCE, 995.0f, false)); //$NON-NLS-1$
		extras.add(new Extra("Heated front seats", Category.CONVENIENCE, 500.0f, true)); //$NON-NLS-1$
		extras.add(new Extra("Parking Assistant", Category.CONVENIENCE, 500.0f, false)); //$NON-NLS-1$
		extras.add(new Extra("Rear manual side window shades", Category.CONVENIENCE, 575.0f, false)); //$NON-NLS-1$

		extras.add(new Extra("Satellite radio", Category.ENTERTAINMENT, 350.0f, false)); //$NON-NLS-1$
		extras.add(new Extra("Navigation system", Category.ENTERTAINMENT, 1150.0f, true)); //$NON-NLS-1$
		extras.add(new Extra("Surround sound system", Category.ENTERTAINMENT, 850.0f, false)); //$NON-NLS-1$

		extras.add(new Extra("Park Distance Control", Category.SAFETY, 750.0f, true)); //$NON-NLS-1$
		extras.add(new Extra("Anti-theft alarm system", Category.SAFETY, 400.0f, false)); //$NON-NLS-1$
		extras.add(new Extra("Rear-view camera", Category.SAFETY, 400.0f, false)); //$NON-NLS-1$
		extras.add(new Extra("Speed Limit Info", Category.SAFETY, 100.0f, false)); //$NON-NLS-1$
		extras.add(new Extra("Xenon headlights", Category.SAFETY, 900.0f, true)); //$NON-NLS-1$
		extras.add(new Extra("Active Blind Spot Detection", Category.SAFETY, 300.0f, false)); //$NON-NLS-1$

		return extras;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(final double totalPrice) {
		this.totalPrice = totalPrice;
	}

}
