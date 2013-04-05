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
package org.eclipse.riena.example.client.controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.beans.common.SingleSelectionListBean;
import org.eclipse.riena.beans.common.StringBean;
import org.eclipse.riena.beans.common.TypedBean;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.IDateTextRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 * Controller for the {@link IDateTextRidget} example.
 */
public class TextDateSubModuleController extends SubModuleController {

	private FontManager fontManager;
	private IDateTextRidget justEights;

	/**
	 * Binds and updates the ridgets.
	 */
	@Override
	public void configureRidgets() {
		final String[] ids = {
				"dd.MM.yyyy", "dd.MM.yy", "dd.MM", "MM.yyyy", "yyyy", "HH:mm:ss", "HH:mm", "dd.MM.yyyy_HH:mm", "dd.MM.yyyyPicker" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
		final DataBindingContext dbc = new DataBindingContext();
		for (final String id : ids) {
			bind(dbc, id);
		}

		// date

		bindToModel("dd.MM.yyyy", new StringBean("01.10.2008")); //$NON-NLS-1$ //$NON-NLS-2$
		bindToModel("dd.MM.yy", new StringBean("01.10.08")); //$NON-NLS-1$ //$NON-NLS-2$
		bindToModel("dd.MM", new StringBean("01.10")); //$NON-NLS-1$ //$NON-NLS-2$
		bindToModel("MM.yyyy", new StringBean("10.2008")); //$NON-NLS-1$ //$NON-NLS-2$
		bindToModel("yyyy", new StringBean("2008")); //$NON-NLS-1$ //$NON-NLS-2$

		final IDateTextRidget txtDatePicker = getRidget(IDateTextRidget.class, "indd.MM.yyyyPicker"); //$NON-NLS-1$
		txtDatePicker.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		txtDatePicker.setFocusable(true);
		txtDatePicker.setMandatory(true);
		txtDatePicker.bindToModel(new StringBean("01.10.2008"), TypedBean.PROP_VALUE); //$NON-NLS-1$
		txtDatePicker.updateFromModel();

		// time && date/time

		bindToModel("HH:mm:ss", new StringBean("23:55:00")); //$NON-NLS-1$ //$NON-NLS-2$
		bindToModel("HH:mm", new StringBean("23:55")); //$NON-NLS-1$ //$NON-NLS-2$
		bindToModel("dd.MM.yyyy_HH:mm", new StringBean("01.10.2008 23:55")); //$NON-NLS-1$ //$NON-NLS-2$

		justEights = getRidget(IDateTextRidget.class, "inJustEights"); //$NON-NLS-1$
		justEights.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		justEights.setOutputOnly(true);
		justEights.bindToModel(new StringBean("88.88.8888"), StringBean.PROP_VALUE); //$NON-NLS-1$
		justEights.updateFromModel();

		final IDateTextRidget justSpaces = getRidget(IDateTextRidget.class, "inJustSpaces"); //$NON-NLS-1$
		justSpaces.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		justSpaces.setOutputOnly(true);
		justSpaces.bindToModel(new StringBean("  .  .    "), StringBean.PROP_VALUE); //$NON-NLS-1$
		justSpaces.updateFromModel();

		fontManager = new FontManager(Display.getCurrent());
		fontManager.addRidget(justEights);
		fontManager.addRidget(justSpaces);

		final IComboRidget comboFonts = getRidget(IComboRidget.class, "comboFonts"); //$NON-NLS-1$
		final SingleSelectionListBean fonts = new SingleSelectionListBean(new Object[] {
				"Arial", "Courier New", "Verdana" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		fonts.setSelection("Arial"); //$NON-NLS-1$
		comboFonts.bindToModel(fonts, SingleSelectionListBean.PROPERTY_VALUES, String.class, null, fonts,
				SingleSelectionListBean.PROPERTY_SELECTION);
		comboFonts.addPropertyChangeListener(IComboRidget.PROPERTY_SELECTION, new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				final String name = (String) evt.getNewValue();
				fontManager.setName(name);
			}
		});
		comboFonts.updateFromModel();

		final SingleSelectionListBean sizes = new SingleSelectionListBean(new Object[] {
				"6", "7", "8", "9", "10", "11", "12" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
		sizes.setSelection("9"); //$NON-NLS-1$
		final IComboRidget comboSizes = getRidget(IComboRidget.class, "comboSizes"); //$NON-NLS-1$
		comboSizes.bindToModel(sizes, SingleSelectionListBean.PROPERTY_VALUES, String.class, null, sizes,
				SingleSelectionListBean.PROPERTY_SELECTION);
		comboSizes.addPropertyChangeListener(IComboRidget.PROPERTY_SELECTION, new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				final String size = (String) evt.getNewValue();
				fontManager.setSize(size);
			}
		});
		comboSizes.updateFromModel();
	}

	@Override
	public void afterBind() {
		super.afterBind();
		// dispose fontManager when the text control is disposed
		if (justEights.getUIControl() != null) {
			((Control) justEights.getUIControl()).addDisposeListener(new DisposeListener() {
				public void widgetDisposed(final DisposeEvent e) {
					if (fontManager != null) {
						fontManager.dispose();
						fontManager = null;
					}
				}
			});
		}
	}

	// helping methods
	//////////////////

	private void bind(final DataBindingContext dbc, final String id) {
		final IDateTextRidget inputRidget = getRidget(IDateTextRidget.class, "in" + id); //$NON-NLS-1$
		final ITextRidget outputRidget = getRidget(ITextRidget.class, "out" + id); //$NON-NLS-1$
		outputRidget.setOutputOnly(true);
		dbc.bindValue(BeansObservables.observeValue(inputRidget, ITextRidget.PROPERTY_TEXT), BeansObservables
				.observeValue(outputRidget, ITextRidget.PROPERTY_TEXT), new UpdateValueStrategy(
				UpdateValueStrategy.POLICY_UPDATE), new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER));
	}

	private void bindToModel(final String id, final StringBean value) {
		final IDateTextRidget ridget = getRidget(IDateTextRidget.class, "in" + id); //$NON-NLS-1$
		ridget.setFormat(id.replace('_', ' '));
		ridget.bindToModel(value, TypedBean.PROP_VALUE);
		ridget.updateFromModel();
	}

	// helping classes
	//////////////////

	/**
	 * Helper class for applying fonts to ridgets.
	 */
	private static final class FontManager {

		private final Display display;

		private Font font;
		private String name;
		private String size;
		private final Set<IRidget> ridgets;

		FontManager(final Display display) {
			Assert.isNotNull(display);
			this.display = display;
			name = ""; //$NON-NLS-1$
			size = ""; //$NON-NLS-1$
			ridgets = new HashSet<IRidget>();
		}

		void addRidget(final IRidget control) {
			ridgets.add(control);
		}

		public synchronized void dispose() {
			ridgets.clear();
			if (font != null) {
				font.dispose();
				font = null;
			}
		}

		void setName(final String name) {
			Assert.isNotNull(name);
			if (!this.name.equals(name)) {
				this.name = name;
				updateControls();
			}
		}

		void setSize(final String size) {
			Assert.isNotNull(size);
			if (!this.size.equals(size)) {
				this.size = size;
				updateControls();
			}
		}

		private synchronized void updateControls() {
			if (font != null) {
				font.dispose();
			}
			font = new Font(display, name, Integer.valueOf(size).intValue(), SWT.NORMAL);
			for (final IRidget ridget : ridgets) {
				final Object control = ridget.getUIControl();
				if (control instanceof Control) {
					((Control) control).setFont(font);
				}
			}
		}
	}
}
