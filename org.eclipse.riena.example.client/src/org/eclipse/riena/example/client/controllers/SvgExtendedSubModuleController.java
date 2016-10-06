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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.swt.graphics.Image;

import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.resource.IconSize;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.ICompletionComboRidget;
import org.eclipse.riena.ui.ridgets.IInfoFlyoutRidget;
import org.eclipse.riena.ui.ridgets.IInfoFlyoutRidget.InfoFlyoutData;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.IMenuItemRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.holder.SelectableListHolder;
import org.eclipse.riena.ui.ridgets.swt.ColumnFormatter;
import org.eclipse.riena.ui.swt.utils.ImageStore;

/**
 *
 */
public class SvgExtendedSubModuleController extends SubModuleController {

	@Override
	public void configureRidgets() {
		//ComboBox
		final SelectableListHolder<String> input = createInput();
		final IComboRidget combo1 = configureCombo(input, "combo", "selection1", "text1"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		combo1.setMarkSelectionMismatch(true);
		combo1.setColumnFormatter(new ImageColumFormatter());
		combo1.setSelection(0);

		//Label
		final ILabelRidget labelRidget = getRidget(ILabelRidget.class, "lblSmallImage"); //$NON-NLS-1$
		labelRidget.setIcon("cloudc"); //$NON-NLS-1$

		final ILabelRidget labelRidgetMediumImage = getRidget(ILabelRidget.class, "lblMediumImage"); //$NON-NLS-1$
		labelRidgetMediumImage.setIcon("cloud", IconSize.D48); //$NON-NLS-1$

		//PopUpMenu
		final IMenuItemRidget testItem = getRidget(IMenuItemRidget.class, "textTest"); //$NON-NLS-1$
		testItem.setIcon("cloud", IconSize.A16); //$NON-NLS-1$

		final IMenuItemRidget itemSmall = getRidget(IMenuItemRidget.class, "itemFoo"); //$NON-NLS-1$
		itemSmall.setIcon("cloud", IconSize.A16); //$NON-NLS-1$

		//InfoFlyOut
		final IActionRidget flyoutbutton = getRidget(IActionRidget.class, "flyOutButton"); //$NON-NLS-1$
		flyoutbutton.addListener(new IActionListener() {

			public void callback() {
				final IInfoFlyoutRidget flyout = getInfoFlyout();
				final InfoFlyoutData info = new InfoFlyoutData("cloudc", //$NON-NLS-1$
						"Works with Svg's too"); //$NON-NLS-1$
				flyout.addInfo(info);
			}
		});

	}

	private SelectableListHolder<String> createInput() {
		final List<String> values = new ArrayList<String>();
		values.add("little"); //$NON-NLS-1$
		values.add("medium"); //$NON-NLS-1$
		values.add("big"); //$NON-NLS-1$

		Collections.sort(values, new Comparator<String>() {
			public int compare(final String entry1, final String entry2) {
				return entry1.compareTo(entry2);
			}
		});
		return new SelectableListHolder<String>(values);
	}

	private IComboRidget configureCombo(final Object input, final String comboId, final String selectionId, final String textId) {
		final ITextRidget selectionRidget = getRidget(ITextRidget.class, selectionId);
		selectionRidget.setOutputOnly(true);

		final ITextRidget textRidget = getRidget(ITextRidget.class, textId);
		textRidget.setOutputOnly(true);

		final IComboRidget comboBox = getRidget(ICompletionComboRidget.class, comboId);
		final WritableValue selection = new WritableValue() {
			@Override
			public void doSetValue(final Object value) {
			}
		};

		//		comboBox.bindToModel(input, ListBean.PROPERTY_VALUES, String.class, null, selection, "value"); //$NON-NLS-1$
		comboBox.bindToModel(((SelectableListHolder<String>) input), ""); //$NON-NLS-1$
		comboBox.updateFromModel();

		return comboBox;
	}

	private static final class ImageColumFormatter extends ColumnFormatter {
		@Override
		public String getText(final Object element) {
			return ((String) element);
		}

		@Override
		public Object getImage(final Object element) {
			final String key = (String) element;
			Image result = null;
			if (key == "little") { //$NON-NLS-1$
				result = ImageStore.getInstance().getImage("cloud", IconSize.A16); //$NON-NLS-1$
			}
			if (key == "medium") { //$NON-NLS-1$
				result = ImageStore.getInstance().getImage("cloud", IconSize.B22); //$NON-NLS-1$
			}
			if (key == "big") { //$NON-NLS-1$
				result = ImageStore.getInstance().getImage("cloud", IconSize.C32); //$NON-NLS-1$
			}
			return result;
		}
	}

}
