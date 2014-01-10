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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.IDecimalTextRidget;
import org.eclipse.riena.ui.ridgets.INumericTextRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * SWT example for {@link INumericTextRidget} and {@link IDecimalTextRidget}.
 */
public class TextNumericSubModuleView extends SubModuleView {

	public static final String ID = TextNumericSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(3, true));

		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, "Text Field"); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, "Model Value"); //$NON-NLS-1$

		// numeric ridgets

		UIControlsFactory.createLabel(parent, "String (Numeric):"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextNumeric(parent), "inStringNum"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "outStringNum"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Integer (5):"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextNumeric(parent), "inInteger"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "outInteger"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Long:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextNumeric(parent), "inLong"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "outLong"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Big Integer:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextNumeric(parent), "inBigInteger"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "outBigInteger"); //$NON-NLS-1$

		// decimal ridgets

		UIControlsFactory.createLabel(parent, "String (Decimal):"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextDecimal(parent), "inStringDec"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "outStringDec"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Double:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextDecimal(parent), "inDouble"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "outDouble"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Float:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextDecimal(parent), "inFloat"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "outFloat"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Big Decimal (30,10):"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextDecimal(parent), "inBigDecimal"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "outBigDecimal"); //$NON-NLS-1$

		// customized ridgets (see controller)

		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Range[100,1000]:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextNumeric(parent), "inRange"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "outRange"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Max. 8 digits:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextNumeric(parent), "inMaxEight"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "outMaxEight"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Unformatted; Min. 4 digits:  "); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextNumeric(parent), "inMinThree"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "outMinThree"); //$NON-NLS-1$

		//		UIControlsFactory.createLabel(parent, "With leading Zero:"); //$NON-NLS-1$
		//		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		//		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		//
		//		UIControlsFactory.createLabel(parent, "Bank Code:"); //$NON-NLS-1$
		//		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		//		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$

		final GridDataFactory gdf = GridDataFactory.fillDefaults();
		for (final Control child : parent.getChildren()) {
			if (child instanceof Text) {
				gdf.applyTo(child);
			}
		}
	}
}
