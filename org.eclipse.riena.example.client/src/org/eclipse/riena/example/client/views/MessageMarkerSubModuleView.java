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
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.MessageBox;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Example for message marker viewers.
 */
public class MessageMarkerSubModuleView extends SubModuleView {

	public static final String ID = MessageMarkerSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		GridLayoutFactory.swtDefaults().numColumns(2).margins(20, 20).spacing(10, 10).applyTo(parent);

		final GridDataFactory gdfSpanTwo = GridDataFactory.fillDefaults().span(2, 1);

		final Label title = UIControlsFactory.createLabel(parent,
				"Messages about markers displayed outside the UI widget"); //$NON-NLS-1$
		gdfSpanTwo.applyTo(title);

		createText(parent, "alwaysMarked"); //$NON-NLS-1$
		createLabel(parent, "Textfield with a permanent ErrorMarker"); //$NON-NLS-1$

		createText(parent, "sometimesMarked"); //$NON-NLS-1$
		createLabel(parent, "Textfield with an ErrorMarker when containing less than 3 characters"); //$NON-NLS-1$

		createText(parent, "sometimesMarkedMultipleRules"); //$NON-NLS-1$
		createLabel(parent, "Textfield with ErrorMarkers when not starting with A and/or ending with Z"); //$NON-NLS-1$

		final Label lbl = createLabel(parent, "Where to show the messages"); //$NON-NLS-1$
		gdfSpanTwo.applyTo(lbl);

		final ChoiceComposite choice = new ChoiceComposite(parent, SWT.NONE, true);
		addUIControl(choice, "activeViewers"); //$NON-NLS-1$

		final MessageBox messageBox = new MessageBox(parent);
		addUIControl(messageBox, "messageBox"); //$NON-NLS-1$
	}

	// helping methods
	//////////////////

	private Label createLabel(final Composite parent, final String caption) {
		final Label result = UIControlsFactory.createLabel(parent, caption);
		GridDataFactory.fillDefaults().applyTo(result);
		return result;
	}

	private void createText(final Composite parent, final String id) {
		final Text text = UIControlsFactory.createText(parent);
		GridDataFactory.fillDefaults().hint(100, SWT.DEFAULT).applyTo(text);
		addUIControl(text, id);
	}

}
