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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget.MessageBoxOption;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget.Type;
import org.eclipse.riena.ui.ridgets.ITextRidget;

public class MessageBoxSubModuleController extends SubModuleController {

	private static final MessageBoxOption MAYBE = new IMessageBoxRidget.MessageBoxOption("Maybe"); //$NON-NLS-1$
	private static final MessageBoxOption BLUE = new IMessageBoxRidget.MessageBoxOption("Blue"); //$NON-NLS-1$
	private static final MessageBoxOption YELLOW = new IMessageBoxRidget.MessageBoxOption("Yellow"); //$NON-NLS-1$

	private IMessageBoxRidget messageBoxRidget;

	public MessageBoxSubModuleController() {
		super(null);
	}

	@Override
	public void configureRidgets() {

		super.configureRidgets();

		messageBoxRidget = getRidget("messageBox"); //$NON-NLS-1$

		final ITextRidget messageTitle = getRidget("messageTitle"); //$NON-NLS-1$
		messageTitle.addPropertyChangeListener(ITextRidget.PROPERTY_TEXT, new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				messageBoxRidget.setTitle((String) evt.getNewValue());
			}
		});
		messageTitle.setText("Bridgekeeper"); //$NON-NLS-1$

		final ITextRidget messageText = getRidget("messageText"); //$NON-NLS-1$
		messageText.addPropertyChangeListener(ITextRidget.PROPERTY_TEXT, new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				messageBoxRidget.setText((String) evt.getNewValue());
			}
		});
		messageText.setText("What is your favourite colour?"); //$NON-NLS-1$

		final MessageBoxSettingsBean bean = new MessageBoxSettingsBean();

		final IComboRidget messageTypes = getRidget("messageType"); //$NON-NLS-1$
		messageTypes.bindToModel(bean, "messageTypes", LabeledMessageType.class, null, bean, "selectedMessageType"); //$NON-NLS-1$ //$NON-NLS-2$
		messageTypes.updateFromModel();

		final IComboRidget messageOptions = getRidget("messageOptions"); //$NON-NLS-1$
		messageOptions.bindToModel(bean, "messageOptions", LabeledOptions.class, null, bean, "selectedMessageOptions"); //$NON-NLS-1$ //$NON-NLS-2$
		messageOptions.updateFromModel();

		final IActionRidget showMessage = getRidget("showMessage"); //$NON-NLS-1$
		showMessage.setText("Show Message Box"); //$NON-NLS-1$
		showMessage.addListener(new ShowMessageActionListener());
	}

	private final class ShowMessageActionListener implements IActionListener {
		public void callback() {
			final IMessageBoxRidget.MessageBoxOption selectedOption = messageBoxRidget.show();

			// Filling a textfield based on the selected option. Just an
			// example...
			final ITextRidget selectedOptionTextField = getRidget("selectedOption"); //$NON-NLS-1$
			if (selectedOption.equals(IMessageBoxRidget.OK)) {
				selectedOptionTextField.setText("You clicked 'OK'."); //$NON-NLS-1$
			} else if (selectedOption.equals(IMessageBoxRidget.CANCEL)) {
				selectedOptionTextField.setText("You clicked 'Cancel'."); //$NON-NLS-1$
			} else if (selectedOption.equals(IMessageBoxRidget.YES)) {
				selectedOptionTextField.setText("You said 'Yes'."); //$NON-NLS-1$
			} else if (selectedOption.equals(IMessageBoxRidget.NO)) {
				selectedOptionTextField.setText("You said 'No'."); //$NON-NLS-1$
			} else if (selectedOption.equals(IMessageBoxRidget.CLOSED)) {
				selectedOptionTextField.setText("You closed the window without clicking a button."); //$NON-NLS-1$
			} else if (selectedOption.equals(MAYBE)) {
				selectedOptionTextField.setText("You are undecided."); //$NON-NLS-1$
			} else if (selectedOption.equals(BLUE)) {
				selectedOptionTextField.setText("Your favourite colour is blue."); //$NON-NLS-1$
			} else if (selectedOption.equals(YELLOW)) {
				selectedOptionTextField.setText("Your favourite colour is yellow."); //$NON-NLS-1$
			}
		}
	}

	public final class MessageBoxSettingsBean {

		private final List<LabeledMessageType> messageTypes;
		private LabeledMessageType selectedMessageType;
		private final List<LabeledOptions> messageOptions;
		private LabeledOptions selectedMessageOptions;

		private MessageBoxSettingsBean() {
			messageTypes = new ArrayList<LabeledMessageType>();
			messageTypes.add(new LabeledMessageType("Plain (no Icon)", IMessageBoxRidget.Type.PLAIN)); //$NON-NLS-1$
			messageTypes.add(new LabeledMessageType("Information", IMessageBoxRidget.Type.INFORMATION)); //$NON-NLS-1$
			messageTypes.add(new LabeledMessageType("Warning", IMessageBoxRidget.Type.WARNING)); //$NON-NLS-1$
			messageTypes.add(new LabeledMessageType("Error", IMessageBoxRidget.Type.ERROR)); //$NON-NLS-1$
			messageTypes.add(new LabeledMessageType("Help", IMessageBoxRidget.Type.HELP)); //$NON-NLS-1$
			final LabeledMessageType question = new LabeledMessageType("Question", IMessageBoxRidget.Type.QUESTION); //$NON-NLS-1$
			messageTypes.add(question);
			setSelectedMessageType(question);

			messageOptions = new ArrayList<LabeledOptions>();
			messageOptions.add(new LabeledOptions("just OK", IMessageBoxRidget.OPTIONS_OK)); //$NON-NLS-1$
			messageOptions.add(new LabeledOptions("OK and Cancel", IMessageBoxRidget.OPTIONS_OK_CANCEL)); //$NON-NLS-1$
			messageOptions.add(new LabeledOptions("Yes or No", IMessageBoxRidget.OPTIONS_YES_NO)); //$NON-NLS-1$
			messageOptions.add(new LabeledOptions("Yes, No or Cancel", IMessageBoxRidget.OPTIONS_YES_NO_CANCEL)); //$NON-NLS-1$
			messageOptions.add(new LabeledOptions("Yes, No or Maybe (partially custom)", //$NON-NLS-1$
					new IMessageBoxRidget.MessageBoxOption[] { IMessageBoxRidget.YES, IMessageBoxRidget.NO, MAYBE }));
			final LabeledOptions blueOrYellow = new LabeledOptions("Blue or Yellow (custom)", //$NON-NLS-1$
					new IMessageBoxRidget.MessageBoxOption[] { BLUE, YELLOW });
			messageOptions.add(blueOrYellow);
			setSelectedMessageOptions(blueOrYellow);
		}

		public List<LabeledMessageType> getMessageTypes() {
			return messageTypes;
		}

		public LabeledMessageType getSelectedMessageType() {
			return selectedMessageType;
		}

		public void setSelectedMessageType(final LabeledMessageType selectedMessageType) {
			this.selectedMessageType = selectedMessageType;
			messageBoxRidget.setType(this.selectedMessageType.getType());
		}

		public List<LabeledOptions> getMessageOptions() {
			return messageOptions;
		}

		public LabeledOptions getSelectedMessageOptions() {
			return selectedMessageOptions;
		}

		public void setSelectedMessageOptions(final LabeledOptions selectedMessageOptions) {
			this.selectedMessageOptions = selectedMessageOptions;
			messageBoxRidget.setOptions(this.selectedMessageOptions.getOptions());
		}
	}

	private static class LabeledMessageType {

		private final IMessageBoxRidget.Type type;
		private final String label;

		public LabeledMessageType(final String label, final Type type) {
			this.label = label;
			this.type = type;
		}

		public IMessageBoxRidget.Type getType() {
			return type;
		}

		@Override
		public String toString() {
			return label;
		}
	}

	private static class LabeledOptions {

		private final IMessageBoxRidget.MessageBoxOption[] options;
		private final String label;

		public LabeledOptions(final String label, final IMessageBoxRidget.MessageBoxOption[] options) {
			this.label = label;
			this.options = options;
		}

		public IMessageBoxRidget.MessageBoxOption[] getOptions() {
			return options;
		}

		@Override
		public String toString() {
			return label;
		}
	}
}
