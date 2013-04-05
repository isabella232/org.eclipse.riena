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
package org.eclipse.riena.example.client.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * SWT {@link IComboRidget} sample.
 */
public class LogCollectorSubModuleView extends SubModuleView {

	public static final String ID = LogCollectorSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(4, false));
		final GridDataFactory fillFactory = GridDataFactory.fillDefaults();

		UIControlsFactory.createLabel(parent, "&Log level:"); //$NON-NLS-1$
		final Combo logLevel = UIControlsFactory.createCombo(parent);
		fillFactory.applyTo(logLevel);
		addUIControl(logLevel, "logLevelCombo"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "&Custom level:"); //$NON-NLS-1$
		final Combo customLevel = UIControlsFactory.createCombo(parent);
		fillFactory.applyTo(customLevel);
		addUIControl(customLevel, "customLevelCombo"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "&Message:"); //$NON-NLS-1$
		final Text message = UIControlsFactory.createText(parent);
		//		message.setSize(new Point());
		fillFactory.applyTo(message);
		addUIControl(message, "logMessage"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "&Custom message:"); //$NON-NLS-1$
		final Text customMessage = UIControlsFactory.createText(parent);
		//		message.setSize(new Point());
		fillFactory.applyTo(customMessage);
		addUIControl(customMessage, "customMessage"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "&Exception:"); //$NON-NLS-1$
		final Text exception = UIControlsFactory.createText(parent);
		fillFactory.applyTo(exception);
		addUIControl(exception, "logException"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$

		final Button logBbutton = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(logBbutton);
		addUIControl(logBbutton, "logButton"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$

		final Button customButton = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(customButton);
		addUIControl(customButton, "customButton"); //$NON-NLS-1$
	}

}
