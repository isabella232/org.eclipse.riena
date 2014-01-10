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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.IDateTextRidget;
import org.eclipse.riena.ui.swt.DatePickerComposite;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * SWT example for {@link IDateTextRidget}.
 */
public class TextDateSubModuleView extends SubModuleView {

	public static final String ID = TextDateSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(3, true));

		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, "Text Field"); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, "Model Value"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "dd.MM.yyyy:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextDate(parent), "indd.MM.yyyy"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "outdd.MM.yyyy"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "dd.MM.yyyy DatePicker:"); //$NON-NLS-1$
		UIControlsFactory.createDatePickerComposite(parent, "indd.MM.yyyyPicker"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "outdd.MM.yyyyPicker"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "dd.MM.yy:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextDate(parent), "indd.MM.yy"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "outdd.MM.yy"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "dd.MM:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextDate(parent), "indd.MM"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "outdd.MM"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "MM.yyyy:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextDate(parent), "inMM.yyyy"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "outMM.yyyy"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "yyyy:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextDate(parent), "inyyyy"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "outyyyy"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "HH:mm:ss:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextDate(parent), "inHH:mm:ss"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "outHH:mm:ss"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "HH:mm:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextDate(parent), "inHH:mm"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "outHH:mm"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "dd.MM.yyyy HH:mm:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextDate(parent), "indd.MM.yyyy_HH:mm"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "outdd.MM.yyyy_HH:mm"); //$NON-NLS-1$

		final Label spacer = UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		GridDataFactory.fillDefaults().span(3, 1).applyTo(spacer);

		UIControlsFactory.createLabel(parent, "dd.MM.yyyy:"); //$NON-NLS-1$

		UIControlsFactory.createTextDate(parent, "inJustEights"); //$NON-NLS-1$

		addUIControl(UIControlsFactory.createCombo(parent), "comboFonts"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "dd.MM.yyyy:"); //$NON-NLS-1$

		UIControlsFactory.createTextDate(parent, "inJustSpaces"); //$NON-NLS-1$

		addUIControl(UIControlsFactory.createCombo(parent), "comboSizes"); //$NON-NLS-1$

		final GridDataFactory gdf = GridDataFactory.fillDefaults();
		for (final Control child : parent.getChildren()) {
			if (child instanceof Text || child instanceof Combo || child instanceof DatePickerComposite) {
				gdf.applyTo(child);
			}
		}
	}
}
