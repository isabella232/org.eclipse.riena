/*******************************************************************************
 * Copyright (c) 2007, 2008 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.example.client.controllers;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.beans.common.StringBean;
import org.eclipse.riena.beans.common.StringManager;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.example.client.views.ComboSubModuleView;
import org.eclipse.riena.internal.example.client.Activator;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 * Controller for the {@link ComboSubModuleView} example.
 */
public class LogCollectorSubModuleController extends SubModuleController {

	private final StringManager logLevels;
	private final StringManager customLevels;
	private final StringBean logMessageBean = new StringBean("Log text"); //$NON-NLS-1$
	private final StringBean customMessageBean = new StringBean("Custom text"); //$NON-NLS-1$
	private final StringBean exceptionBean = new StringBean(NullPointerException.class.getName());
	private IComboRidget logLevelCombo;
	private IComboRidget customLevelCombo;
	private ITextRidget logMessage;
	private ITextRidget customMessage;
	private ITextRidget logException;

	private final static Logger LOGGER = Activator.getDefault().getLogger(LogCollectorSubModuleController.class);

	public LogCollectorSubModuleController() {
		this(null);
	}

	public LogCollectorSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
		logLevels = new StringManager("DEBUG", "INFO", "WARN", "ERROR"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		logLevels.setSelectedItem("DEBUG"); //$NON-NLS-1$

		customLevels = new StringManager("USAGE", "STATS", "SEND"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		customLevels.setSelectedItem("USAGE"); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.controllers.SubModuleController#afterBind()
	 */
	@Override
	public void afterBind() {
		super.afterBind();
		bindModels();
	}

	private void bindModels() {
		logLevelCombo.bindToModel(logLevels, "items", String.class, null, logLevels, "selectedItem"); //$NON-NLS-1$ //$NON-NLS-2$ 
		logLevelCombo.updateFromModel();

		logMessage.bindToModel(logMessageBean, "value"); //$NON-NLS-1$
		logMessage.updateFromModel();

		logException.bindToModel(exceptionBean, "value"); //$NON-NLS-1$
		logException.updateFromModel();

		customLevelCombo.bindToModel(customLevels, "items", String.class, null, customLevels, "selectedItem"); //$NON-NLS-1$ //$NON-NLS-2$ 
		customLevelCombo.updateFromModel();

		customMessage.bindToModel(customMessageBean, "value"); //$NON-NLS-1$
		customMessage.updateFromModel();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	@Override
	public void configureRidgets() {

		logLevelCombo = (IComboRidget) getRidget("logLevelCombo"); //$NON-NLS-1$
		logMessage = (ITextRidget) getRidget("logMessage"); //$NON-NLS-1$
		logException = (ITextRidget) getRidget("logException"); //$NON-NLS-1$

		final IActionRidget logButtonSave = (IActionRidget) getRidget("logButton"); //$NON-NLS-1$
		logButtonSave.setText("&Log"); //$NON-NLS-1$
		logButtonSave.addListener(new IActionListener() {
			public void callback() {
				Throwable throwable = null;
				try {
					if (!StringUtils.isDeepEmpty(logException.getText())) {
						throwable = (Throwable) Class.forName(logException.getText()).newInstance();
					}
				} catch (Exception e) {
					throwable = new IllegalArgumentException(
							"Can not instantiate logException: " + logException.getText(), e); //$NON-NLS-1$
				}
				LOGGER.log(4 - logLevelCombo.getSelectionIndex(), logMessage.getText(), throwable);
			}
		});

		customLevelCombo = (IComboRidget) getRidget("customLevelCombo"); //$NON-NLS-1$
		customMessage = (ITextRidget) getRidget("customMessage"); //$NON-NLS-1$

		final IActionRidget customButtonSave = (IActionRidget) getRidget("customButton"); //$NON-NLS-1$
		customButtonSave.setText("&CustomLog"); //$NON-NLS-1$
		customButtonSave.addListener(new IActionListener() {
			public void callback() {
				LOGGER.log(-customLevelCombo.getSelectionIndex(), customMessage.getText());
			}
		});
	}
}
