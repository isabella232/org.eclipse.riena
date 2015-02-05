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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.observable.list.WritableList;

import org.eclipse.riena.beans.common.TypedComparator;
import org.eclipse.riena.internal.example.client.beans.Car;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.listener.ISelectionListener;
import org.eclipse.riena.ui.ridgets.listener.SelectionEvent;
import org.eclipse.riena.ui.ridgets.swt.NumberColumnFormatter;

/**
 * Controller of a sub-module to demonstrate how cells of a table can be edited
 * with data-binding support and Ridgets.
 */
public class CarCatalogSubModuleController extends SubModuleController {

	@Override
	public void configureRidgets() {

		super.configureRidgets();

		final IToggleButtonRidget nativeToolTip = getRidget("nativeCheck"); //$NON-NLS-1$
		final ITableRidget table = getRidget(ITableRidget.class, "table"); //$NON-NLS-1$
		final String[] columnPropertyNames = { "make", "model", "power", "capacity", "speedup", "milage" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		final String[] columnHeaders = { "Make", "Model", "Power (KW)", "Capacity", "Speedup", "Milage" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		final List<Car> input = createInput();
		table.bindToModel(new WritableList(input, Car.class), Car.class, columnPropertyNames, columnHeaders);
		table.updateFromModel();
		table.setComparator(0, new TypedComparator<String>());
		table.setComparator(2, new TypedComparator<Integer>());
		table.setComparator(3, new TypedComparator<Integer>());
		table.setComparator(4, new TypedComparator<Float>());
		table.setComparator(5, new TypedComparator<Float>());
		table.setColumnEditable(1, true);
		table.setColumnEditable(2, true);
		table.setColumnEditable(3, true);
		table.setColumnEditable(4, true);
		table.setColumnEditable(5, true);
		table.setColumnFormatter(3, new NumberColumnFormatter(Integer.class, 0) {
			@Override
			protected Number getValue(final Object element) {
				return ((Car) element).getCapacity();
			}

			@Override
			public String getToolTip(final Object element) {
				return getText(element) + " ccm"; //$NON-NLS-1$
			}
		});
		table.setColumnFormatter(4, new NumberColumnFormatter(Float.class, 2) {
			@Override
			protected Number getValue(final Object element) {
				return ((Car) element).getSpeedup();
			}

			@Override
			public String getToolTip(final Object element) {
				return getText(element) + " (0-100 km/h)"; //$NON-NLS-1$
			}
		});
		table.setColumnFormatter(5, new NumberColumnFormatter(Float.class, 2) {
			@Override
			protected Number getValue(final Object element) {
				return ((Car) element).getMilage();
			}

			@Override
			public String getToolTip(final Object element) {
				return getText(element) + " (l/100 km)"; //$NON-NLS-1$
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

		nativeToolTip.setSelected(true);
		nativeToolTip.addListener(new IActionListener() {
			public void callback() {
				table.setNativeToolTip(nativeToolTip.isSelected());
			}
		});
	}

	private List<Car> createInput() {

		final List<Car> cars = new ArrayList<Car>();
		cars.add(new Car("Audi", "A1 1.2 TFSI", 63, 1197, 11.7f, 5.1f)); //$NON-NLS-1$ //$NON-NLS-2$
		cars.add(new Car("BMW", "116i", 90, 1995, 9.8f, 6.1f)); //$NON-NLS-1$ //$NON-NLS-2$
		cars.add(new Car("Jeep", "Grand Cherokee 3.0 V6", 177, 2987, 8.2f, 8.3f)); //$NON-NLS-1$ //$NON-NLS-2$
		cars.add(new Car("Peugeot", "207 75", 54, 1360, 14.4f, 6.3f)); //$NON-NLS-1$ //$NON-NLS-2$
		cars.add(new Car("Cadillac", "Escalade 6.2 V8", 301, 6162, 6.7f, 14.5f)); //$NON-NLS-1$ //$NON-NLS-2$
		cars.add(new Car("Mini", "Cooper", 90, 1598, 9.1f, 5.4f)); //$NON-NLS-1$ //$NON-NLS-2$
		cars.add(new Car("Volvo", "V70 T4", 132, 1596, 8.7f, 6.8f)); //$NON-NLS-1$ //$NON-NLS-2$
		cars.add(new Car("Toyota", "Prius 1.8 Hybrid", 100, 1798, 10.4f, 3.9f)); //$NON-NLS-1$ //$NON-NLS-2$
		cars.add(new Car("Peugeot", "205", 44, 0, 0f, 0f)); //$NON-NLS-1$ //$NON-NLS-2$
		cars.add(new Car("Toyota", "Corolla 1,3 LXi", 55, 0, 0f, 0f)); //$NON-NLS-1$ //$NON-NLS-2$
		cars.add(new Car("Opel", "Kadett E Caravan GL", 44, 1389, 0f, 0f)); //$NON-NLS-1$ //$NON-NLS-2$
		return cars;

	}

}
