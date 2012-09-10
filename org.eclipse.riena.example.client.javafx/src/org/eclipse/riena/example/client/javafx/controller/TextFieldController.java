/**
 * 
 */
package org.eclipse.riena.example.client.javafx.controller;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.IStatuslineRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.listener.ClickEvent;
import org.eclipse.riena.ui.ridgets.listener.IClickListener;
import org.eclipse.riena.ui.ridgets.marker.StatuslineMessageMarkerViewer;
import org.eclipse.riena.ui.ridgets.validation.ValidationFailure;
import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;

/**
 * @author tsc
 * 
 */
public class TextFieldController extends SubModuleController {

	@Override
	public void configureRidgets() {

		super.configureRidgets();

		final ITextRidget textModel1 = getRidget(ITextRidget.class,
				"modelTextField"); //$NON-NLS-1$
		textModel1.setText("type something"); //$NON-NLS-1$
		textModel1.setOutputOnly(true);
		textModel1.addClickListener(new IClickListener() {
			@Override
			public void callback(final ClickEvent event) {
				if (event.getButton() == 1) {
					// final IMessageBoxRidget messageBox =
					// getRidget("messageBox");
					// messageBox.setTitle("Value");
					// messageBox.setText("The value of the text field: "
					// + textModel1.getText());
					// messageBox.show();
					System.out.println("The value of the text field: "
							+ textModel1.getText());
				}
			}
		});

		final ITextRidget textField = getRidget(ITextRidget.class, "textField"); //$NON-NLS-1$
		textField.setMandatory(true);
		textField.bindToModel(textModel1, ITextRidget.PROPERTY_TEXT);
		textField.updateFromModel();

		final ITextRidget textModel2 = getRidget(ITextRidget.class,
				"modelTextFieldDirect"); //$NON-NLS-1$
		textModel2.setText("type something"); //$NON-NLS-1$
		textModel2.setOutputOnly(true);

		final ITextRidget textDirectWrite = getRidget(ITextRidget.class,
				"textFieldDirect"); //$NON-NLS-1$
		textDirectWrite.setDirectWriting(true);
		textDirectWrite.setMandatory(true);
		textDirectWrite.bindToModel(textModel2, ITextRidget.PROPERTY_TEXT);
		textDirectWrite.updateFromModel();

		final ITextRidget textModel3 = getRidget(ITextRidget.class,
				"modelTextFieldValidation"); //$NON-NLS-1$
		textModel3.setText("Type Something"); //$NON-NLS-1$
		textModel3.setOutputOnly(true);

		final ITextRidget textValidation = getRidget(ITextRidget.class,
				"textFieldValidation"); //$NON-NLS-1$
		textValidation.setMandatory(true);
		textValidation.addValidationRule(new StartsWithUpper(),
				ValidationTime.ON_UPDATE_TO_MODEL);
		textValidation.bindToModel(textModel3, ITextRidget.PROPERTY_TEXT);
		textValidation.updateFromModel();

		final IStatuslineRidget statuslineRidget = getApplicationController()
				.getStatusline();
		final StatuslineMessageMarkerViewer statuslineMessageMarkerViewer = new StatuslineMessageMarkerViewer(
				statuslineRidget);
		statuslineMessageMarkerViewer.addRidget(textValidation);

	}

	private static class StartsWithUpper implements IValidator {

		@Override
		public IStatus validate(final Object value) {
			if (value == null) {
				return ValidationRuleStatus.ok();
			}
			if (value instanceof String) {
				final String string = (String) value;
				if (string.isEmpty()) {
					return ValidationRuleStatus.ok();
				}
				if (Character.isUpperCase(string.charAt(0))) { //$NON-NLS-1$
					return ValidationRuleStatus.ok();
				}
				return ValidationRuleStatus.error(false,
						"Value does not start with upper-case."); //$NON-NLS-1$
			}
			throw new ValidationFailure(getClass().getName()
					+ " can only validate objects of type " //$NON-NLS-1$
					+ String.class.getName());
		}

	}

	/**
	 * Returns the controller of the parent sub-application.
	 * 
	 * @return sub-application controller
	 */
	private ApplicationController getApplicationController() {
		return (ApplicationController) getNavigationNode().getParentOfType(
				IApplicationNode.class).getNavigationNodeController();
	}

}