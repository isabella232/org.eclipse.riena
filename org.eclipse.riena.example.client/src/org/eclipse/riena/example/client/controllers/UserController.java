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
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;

import org.eclipse.riena.beans.common.StringBean;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget;
import org.eclipse.riena.ui.ridgets.IStatuslineRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.listener.FocusEvent;
import org.eclipse.riena.ui.ridgets.listener.IFocusListener;
import org.eclipse.riena.ui.ridgets.marker.StatuslineMessageMarkerViewer;
import org.eclipse.riena.ui.ridgets.validation.ValidationFailure;
import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;

/**
 *
 */
public class UserController extends SubModuleController {

	enum PwdQuality {
		LOW, MEDIUM, HIGH
	}

	private UserModel user;
	private List<IMarkableRidget> inputRidgets;

	@Override
	public void configureRidgets() {

		user = new UserModel();
		inputRidgets = new LinkedList<IMarkableRidget>();

		super.configureRidgets();

		final ITextRidget txtFirst = getRidget(ITextRidget.class, "txtFirst"); //$NON-NLS-1$
		txtFirst.setMandatory(true);
		txtFirst.bindToModel(user, "firstname"); //$NON-NLS-1$
		inputRidgets.add(txtFirst);

		final ITextRidget txtLast = getRidget(ITextRidget.class, "txtLast"); //$NON-NLS-1$
		txtLast.setMandatory(true);
		txtLast.bindToModel(user, "lastname"); //$NON-NLS-1$
		inputRidgets.add(txtLast);

		final ITextRidget txtUser = getRidget(ITextRidget.class, "txtUser"); //$NON-NLS-1$
		txtUser.setMandatory(true);
		txtUser.bindToModel(user, "username"); //$NON-NLS-1$
		inputRidgets.add(txtUser);

		final ITextRidget txtPassword = getRidget(ITextRidget.class, "txtPassword"); //$NON-NLS-1$
		txtPassword.setMandatory(true);
		txtPassword.setDirectWriting(true);
		txtPassword.bindToModel(user, "password"); //$NON-NLS-1$
		inputRidgets.add(txtPassword);

		final ILabelRidget lblLights = getRidget(ILabelRidget.class, "lblLights"); //$NON-NLS-1$
		lblLights.setVisible(false);

		txtPassword.addPropertyChangeListener(ITextRidget.PROPERTY_TEXT, new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				final String pwd = user.getPassword();
				if (StringUtils.isEmpty(pwd)) {
					lblLights.setVisible(false);
					lblLights.setToolTipText(null);
				} else {
					lblLights.setVisible(true);
					final PwdQuality quality = calculatePasswordQuality(pwd);
					updateImage(lblLights, quality);
				}
			}
		});

		final ITextRidget txtConfirm = getRidget(ITextRidget.class, "txtConfirm"); //$NON-NLS-1$
		txtConfirm.setMandatory(true);
		txtConfirm.addValidationRule(new PasswordValidator(user), ValidationTime.ON_UPDATE_TO_MODEL);
		final StringBean comfirmModel = new StringBean();
		txtConfirm.bindToModel(comfirmModel, StringBean.PROP_VALUE);
		inputRidgets.add(txtConfirm);
		txtPassword.addFocusListener(new IFocusListener() {

			public void focusLost(final FocusEvent event) {
				txtConfirm.revalidate();
			}

			public void focusGained(final FocusEvent event) {
			}
		});

		final IStatuslineRidget statuslineRidget = getApplicationController().getStatusline();
		final StatuslineMessageMarkerViewer statuslineMessageMarkerViewer = new StatuslineMessageMarkerViewer(statuslineRidget);
		statuslineMessageMarkerViewer.addRidget(txtConfirm);

		final IActionRidget btnSave = getRidget(IActionRidget.class, "btnSave"); //$NON-NLS-1$
		btnSave.addListener(new IActionListener() {

			public void callback() {
				doSave();
			}
		});

		final IActionRidget btnEdit = getRidget(IActionRidget.class, "btnEdit"); //$NON-NLS-1$
		btnEdit.addListener(new IActionListener() {

			public void callback() {
				doEdit();
			}
		});

		final IActionRidget btnDelete = getRidget(IActionRidget.class, "btnDelete"); //$NON-NLS-1$
		btnDelete.addListener(new IActionListener() {

			public void callback() {
				doDelete();
			}
		});

		setOutputOnly(false);

	}

	private void doSave() {

		final IMessageBoxRidget messageBox = getRidget(IMessageBoxRidget.class, "messageBox"); //$NON-NLS-1$
		messageBox.setTitle("Can't Save"); //$NON-NLS-1$
		if (!getNavigationNode().getMarkersOfType(MandatoryMarker.class).isEmpty()) {
			messageBox.setText("Please make sure the form is completely filled out!"); //$NON-NLS-1$
			messageBox.show();
			return;
		}
		if (!getNavigationNode().getMarkersOfType(ErrorMarker.class).isEmpty()) {
			messageBox.setText("Please make sure the form has no errors!"); //$NON-NLS-1$
			messageBox.show();
			return;
		}
		getNavigationNode().setLabel(user.getUsername());
		setOutputOnly(true);

	}

	private void doEdit() {
		setOutputOnly(false);
	}

	private void doDelete() {
		getNavigationNode().dispose();
	}

	private void setOutputOnly(final boolean outputOnly) {
		for (final IMarkableRidget ridget : inputRidgets) {
			ridget.setOutputOnly(outputOnly);
		}
		final IActionRidget btnEdit = getRidget(IActionRidget.class, "btnEdit"); //$NON-NLS-1$
		btnEdit.setEnabled(outputOnly);
		final IActionRidget saveBtn = getRidget(IActionRidget.class, "btnSave"); //$NON-NLS-1$
		saveBtn.setEnabled(!outputOnly);
	}

	private static class PasswordValidator implements IValidator {

		private final UserModel userModel;

		public PasswordValidator(final UserModel userModel) {
			this.userModel = userModel;
		}

		public IStatus validate(final Object value) {
			if (value == null) {
				return ValidationRuleStatus.ok();
			}
			if (value instanceof String) {
				final String confirm = (String) value;
				if (confirm.isEmpty()) {
					return ValidationRuleStatus.ok();
				}
				if (StringUtils.isEmpty(userModel.getPassword())) {
					return ValidationRuleStatus.ok();
				}
				if (StringUtils.equals(confirm, userModel.getPassword())) {
					return ValidationRuleStatus.ok();
				}
				return ValidationRuleStatus.error(false, "Passwords don't match!"); //$NON-NLS-1$
			}
			throw new ValidationFailure(getClass().getName() + " can only validate objects of type " //$NON-NLS-1$
					+ String.class.getName());
		}

	}

	private PwdQuality calculatePasswordQuality(final String pwd) {

		if (pwd == null) {
			return PwdQuality.LOW;
		}
		if (pwd.length() < 8) {
			return PwdQuality.LOW;
		} else {
			if (containsLetterDigit(pwd)) {
				return PwdQuality.HIGH;
			} else {
				return PwdQuality.MEDIUM;
			}
		}

	}

	private boolean containsLetterDigit(final String pwd) {

		if (pwd == null) {
			return false;
		}

		boolean upperLetter = false;
		boolean lowerLetter = false;
		boolean digit = false;

		for (int i = 0; i < pwd.length(); i++) {
			final char ch = pwd.charAt(i);
			if (Character.isDigit(ch)) {
				digit = true;
			} else if (Character.isLetter(ch)) {
				if (Character.isUpperCase(ch)) {
					upperLetter = true;
				} else {
					lowerLetter = true;
				}
			}
		}

		return upperLetter && lowerLetter && digit;

	}

	private void updateImage(final ILabelRidget labelRidget, final PwdQuality quality) {
		String icon = ""; //$NON-NLS-1$
		String tooltip = "The quality of your password is "; //$NON-NLS-1$
		switch (quality) {
		case LOW:
			icon = "lights_red.png"; //$NON-NLS-1$
			tooltip += "low"; //$NON-NLS-1$
			break;
		case MEDIUM:
			icon = "lights_yellow.png"; //$NON-NLS-1$
			tooltip += "medium"; //$NON-NLS-1$
			break;
		case HIGH:
			icon = "lights_green.png"; //$NON-NLS-1$
			tooltip += "high"; //$NON-NLS-1$
			break;
		default:
			break;
		}
		labelRidget.setIcon(icon);
		labelRidget.setToolTipText(tooltip);
	}

	/**
	 * Returns the controller of the parent sub-application.
	 * 
	 * @return sub-application controller
	 */
	private ApplicationController getApplicationController() {
		return (ApplicationController) getNavigationNode().getParentOfType(IApplicationNode.class).getNavigationNodeController();
	}

	private static class UserModel {

		private String firstname;
		private String lastname;
		private String username;
		private String password;

		public String getFirstname() {
			return firstname;
		}

		public void setFirstname(final String firstname) {
			this.firstname = firstname;
		}

		public String getLastname() {
			return lastname;
		}

		public void setLastname(final String lastname) {
			this.lastname = lastname;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(final String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(final String password) {
			this.password = password;
		}

	}

}