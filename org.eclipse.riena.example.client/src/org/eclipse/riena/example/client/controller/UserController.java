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
package org.eclipse.riena.example.client.controller;

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
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget;
import org.eclipse.riena.ui.ridgets.IStatuslineRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.marker.StatuslineMessageMarkerViewer;
import org.eclipse.riena.ui.ridgets.validation.ValidationFailure;
import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;

/**
 *
 */
public class UserController extends SubModuleController {

	private UserModel user;
	private List<IMarkableRidget> inputRidgets;

	@Override
	public void configureRidgets() {

		user = new UserModel();
		inputRidgets = new LinkedList<IMarkableRidget>();

		super.configureRidgets();

		final ITextRidget txtFirst = getRidget(ITextRidget.class, "txtFirst");
		txtFirst.setMandatory(true);
		txtFirst.bindToModel(user, "firstname");
		inputRidgets.add(txtFirst);

		final ITextRidget txtLast = getRidget(ITextRidget.class, "txtLast");
		txtLast.setMandatory(true);
		txtLast.bindToModel(user, "lastname");
		inputRidgets.add(txtLast);

		final ITextRidget txtUser = getRidget(ITextRidget.class, "txtUser");
		txtUser.setMandatory(true);
		txtUser.bindToModel(user, "username");
		inputRidgets.add(txtUser);

		final ITextRidget txtPassword = getRidget(ITextRidget.class, "txtPassword");
		txtPassword.setMandatory(true);
		txtPassword.bindToModel(user, "password");
		inputRidgets.add(txtPassword);

		final ITextRidget txtConfirm = getRidget(ITextRidget.class, "txtConfirm");
		txtConfirm.setMandatory(true);
		txtConfirm.addValidationRule(new PasswordValidator(user), ValidationTime.ON_UPDATE_TO_MODEL);
		final StringBean comfirmModel = new StringBean();
		txtConfirm.bindToModel(comfirmModel, StringBean.PROP_VALUE);
		inputRidgets.add(txtConfirm);

		final IStatuslineRidget statuslineRidget = getApplicationController().getStatusline();
		final StatuslineMessageMarkerViewer statuslineMessageMarkerViewer = new StatuslineMessageMarkerViewer(statuslineRidget);
		statuslineMessageMarkerViewer.addRidget(txtConfirm);

		final IActionRidget btnSave = getRidget(IActionRidget.class, "btnSave");
		btnSave.addListener(new IActionListener() {

			public void callback() {
				doSave();
			}
		});

		final IActionRidget btnEdit = getRidget(IActionRidget.class, "btnEdit");
		btnEdit.addListener(new IActionListener() {

			public void callback() {
				doEdit();
			}
		});

		final IActionRidget btnDelete = getRidget(IActionRidget.class, "btnDelete");
		btnDelete.addListener(new IActionListener() {

			public void callback() {
				doDelete();
			}
		});

		setOutputOnly(false);

	}

	private void doSave() {

		final IMessageBoxRidget messageBox = getRidget(IMessageBoxRidget.class, "messageBox");
		messageBox.setTitle("Can't Save");
		if (!getNavigationNode().getMarkersOfType(MandatoryMarker.class).isEmpty()) {
			messageBox.setText("Please make sure the form is completely filled out!");
			messageBox.show();
			return;
		}
		if (!getNavigationNode().getMarkersOfType(ErrorMarker.class).isEmpty()) {
			messageBox.setText("Please make sure the form has no errors!");
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
		final IActionRidget btnEdit = getRidget(IActionRidget.class, "btnEdit");
		btnEdit.setEnabled(outputOnly);
		final IActionRidget saveBtn = getRidget(IActionRidget.class, "btnSave");
		saveBtn.setEnabled(!outputOnly);
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

}