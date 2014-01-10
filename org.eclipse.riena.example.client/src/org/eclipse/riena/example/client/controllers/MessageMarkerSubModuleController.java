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
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;

import org.eclipse.riena.example.client.views.MessageMarkerSubModuleView;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.ErrorMessageMarker;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget;
import org.eclipse.riena.ui.ridgets.IMultipleChoiceRidget;
import org.eclipse.riena.ui.ridgets.IStatuslineRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.marker.IMessageMarkerViewer;
import org.eclipse.riena.ui.ridgets.marker.MessageBoxMessageMarkerViewer;
import org.eclipse.riena.ui.ridgets.marker.StatuslineMessageMarkerViewer;
import org.eclipse.riena.ui.ridgets.marker.TooltipMessageMarkerViewer;
import org.eclipse.riena.ui.ridgets.validation.MinLength;
import org.eclipse.riena.ui.ridgets.validation.ValidationFailure;
import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;

/**
 * Controller for the {@link MessageMarkerSubModuleView} example.
 */
public class MessageMarkerSubModuleController extends SubModuleController {

	private MessageBoxMessageMarkerViewer messageBoxMessageMarkerViewer;
	private IMultipleChoiceRidget activeViewers;

	@Override
	public void configureRidgets() {
		super.configureRidgets();

		final MessageMarkerExampleBean bean = new MessageMarkerExampleBean();

		final ITextRidget alwaysMarked = getRidget(ITextRidget.class, "alwaysMarked"); //$NON-NLS-1$
		alwaysMarked.addMarker(new ErrorMessageMarker("This textfield is inadequate in every way.")); //$NON-NLS-1$
		alwaysMarked.bindToModel(bean, "alwaysMarkedText"); //$NON-NLS-1$
		alwaysMarked.updateFromModel();

		final ITextRidget sometimesMarked = getRidget(ITextRidget.class, "sometimesMarked"); //$NON-NLS-1$
		sometimesMarked.addValidationRule(new MinLength(3), ValidationTime.ON_UPDATE_TO_MODEL);
		sometimesMarked.addValidationMessage("Textfield contains less than 3 characters."); //$NON-NLS-1$
		sometimesMarked.bindToModel(bean, "sometimesMarkedText"); //$NON-NLS-1$
		sometimesMarked.updateFromModel();

		final ITextRidget sometimesMarkedMultipleRules = getRidget(ITextRidget.class, "sometimesMarkedMultipleRules"); //$NON-NLS-1$
		final StartsWithA startsWithA = new StartsWithA();
		final EndsWithZ endsWithZ = new EndsWithZ();
		sometimesMarkedMultipleRules.addValidationRule(startsWithA, ValidationTime.ON_UPDATE_TO_MODEL);
		sometimesMarkedMultipleRules.addValidationRule(endsWithZ, ValidationTime.ON_UPDATE_TO_MODEL);
		sometimesMarkedMultipleRules.addValidationMessage("Text must begin with an A.", startsWithA); //$NON-NLS-1$
		sometimesMarkedMultipleRules.addValidationMessage("Text must end with a Z.", endsWithZ); //$NON-NLS-1$
		sometimesMarkedMultipleRules.bindToModel(bean, "sometimesMarkedMultipleRulesText"); //$NON-NLS-1$
		sometimesMarkedMultipleRules.updateFromModel();

		// Show error messages in the status line
		final IApplicationNode application = getNavigationNode().getParentOfType(IApplicationNode.class);
		final ApplicationController applicationController = (ApplicationController) application
				.getNavigationNodeController();
		final IStatuslineRidget statuslineRidget = applicationController.getStatusline();
		final StatuslineMessageMarkerViewer statuslineMessageMarkerViewer = new StatuslineMessageMarkerViewer(
				statuslineRidget);
		statuslineMessageMarkerViewer.addRidget(alwaysMarked);
		statuslineMessageMarkerViewer.addRidget(sometimesMarked);
		statuslineMessageMarkerViewer.addRidget(sometimesMarkedMultipleRules);

		// Show error messages as a tool tip, when hovering over the text
		final TooltipMessageMarkerViewer tooltipMessageMarkerViewer = new TooltipMessageMarkerViewer();
		tooltipMessageMarkerViewer.addRidget(alwaysMarked);
		tooltipMessageMarkerViewer.addRidget(sometimesMarked);
		tooltipMessageMarkerViewer.addRidget(sometimesMarkedMultipleRules);

		// Show error messages in a message box
		final IMessageBoxRidget messageBoxRidget = getRidget(IMessageBoxRidget.class, "messageBox"); //$NON-NLS-1$
		messageBoxRidget.setType(IMessageBoxRidget.Type.ERROR);
		messageBoxRidget.setTitle("Problems Summary"); //$NON-NLS-1$
		messageBoxRidget.setOptions(IMessageBoxRidget.OPTIONS_OK);
		messageBoxMessageMarkerViewer = new MessageBoxMessageMarkerViewer(messageBoxRidget);
		messageBoxMessageMarkerViewer.addRidget(alwaysMarked);
		messageBoxMessageMarkerViewer.addRidget(sometimesMarked);
		messageBoxMessageMarkerViewer.addRidget(sometimesMarkedMultipleRules);

		final MessageMarkerViewers viewers = new MessageMarkerViewers();
		viewers.addViewer(statuslineMessageMarkerViewer, "Statusline"); //$NON-NLS-1$
		viewers.addViewer(tooltipMessageMarkerViewer, "Tooltip"); //$NON-NLS-1$
		viewers.addViewer(messageBoxMessageMarkerViewer, "MessageBox"); //$NON-NLS-1$
		viewers.setSelectedViewers(Arrays.asList(new IMessageMarkerViewer[] { statuslineMessageMarkerViewer,
				tooltipMessageMarkerViewer }));

		activeViewers = getRidget(IMultipleChoiceRidget.class, "activeViewers"); //$NON-NLS-1$
		activeViewers.bindToModel(viewers.getViewers(), viewers.getViewerLabels(), viewers, "selectedViewers"); //$NON-NLS-1$
		activeViewers.updateFromModel();
	}

	// helping classes
	//////////////////

	public class MessageMarkerViewers {

		private final List<IMessageMarkerViewer> viewers = new ArrayList<IMessageMarkerViewer>();
		private final List<String> viewerLabels = new ArrayList<String>();
		private List<IMessageMarkerViewer> selectedViewers = new ArrayList<IMessageMarkerViewer>();

		public void addViewer(final IMessageMarkerViewer viewer, final String label) {
			viewers.add(viewer);
			viewerLabels.add(label);
			updateViewers();
		}

		public List<IMessageMarkerViewer> getViewers() {
			return viewers;
		}

		public List<String> getViewerLabels() {
			return viewerLabels;
		}

		public List<IMessageMarkerViewer> getSelectedViewers() {
			return selectedViewers;
		}

		public void setSelectedViewers(final List<IMessageMarkerViewer> selectedViewers) {
			this.selectedViewers = selectedViewers;
			updateViewers();
			if (this.selectedViewers.contains(messageBoxMessageMarkerViewer)) {
				this.selectedViewers.remove(messageBoxMessageMarkerViewer);
				activeViewers.updateFromModel();
			}
		}

		private void updateViewers() {
			for (final IMessageMarkerViewer viewer : viewers) {
				viewer.setVisible(false);
			}
			for (final IMessageMarkerViewer viewer : selectedViewers) {
				viewer.setVisible(true);
			}
		}

	}

	public static class MessageMarkerExampleBean {

		private String alwaysMarkedText = "Hopeless"; //$NON-NLS-1$
		private String sometimesMarkedText = "123"; //$NON-NLS-1$
		private String sometimesMarkedMultipleRulesText = "A to Z"; //$NON-NLS-1$

		public String getAlwaysMarkedText() {
			return alwaysMarkedText;
		}

		public void setAlwaysMarkedText(final String alwaysMarkedText) {
			this.alwaysMarkedText = alwaysMarkedText;
		}

		public String getSometimesMarkedText() {
			return sometimesMarkedText;
		}

		public void setSometimesMarkedText(final String sometimesMarkedText) {
			this.sometimesMarkedText = sometimesMarkedText;
		}

		public String getSometimesMarkedMultipleRulesText() {
			return sometimesMarkedMultipleRulesText;
		}

		public void setSometimesMarkedMultipleRulesText(final String sometimesMarkedMultipleRulesText) {
			this.sometimesMarkedMultipleRulesText = sometimesMarkedMultipleRulesText;
		}

	}

	private static class StartsWithA implements IValidator {

		public IStatus validate(final Object value) {
			if (value == null) {
				return ValidationRuleStatus.ok();
			}
			if (value instanceof String) {
				final String string = (String) value;
				if (string.toLowerCase().startsWith("a")) { //$NON-NLS-1$
					return ValidationRuleStatus.ok();
				}
				return ValidationRuleStatus.error(false, "Value does not start with A."); //$NON-NLS-1$
			}
			throw new ValidationFailure(getClass().getName() + " can only validate objects of type " //$NON-NLS-1$
					+ String.class.getName());
		}

	}

	private static class EndsWithZ implements IValidator {

		public IStatus validate(final Object value) {
			if (value == null) {
				return ValidationRuleStatus.ok();
			}
			if (value instanceof String) {
				final String string = (String) value;
				if (string.toLowerCase().endsWith("z")) { //$NON-NLS-1$
					return ValidationRuleStatus.ok();
				}
				return ValidationRuleStatus.error(false, "Value does not end with Z."); //$NON-NLS-1$
			}
			throw new ValidationFailure(getClass().getName() + " can only validate objects of type " //$NON-NLS-1$
					+ String.class.getName());
		}

	}

}
