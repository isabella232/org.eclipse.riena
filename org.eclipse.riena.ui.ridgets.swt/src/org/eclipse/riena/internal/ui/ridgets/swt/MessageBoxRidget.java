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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;

import org.eclipse.riena.ui.ridgets.AbstractRidget;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget;
import org.eclipse.riena.ui.swt.MessageBox;
import org.eclipse.riena.ui.swt.facades.DialogConstantsFacade;

/**
 * The ridget for a message box.
 */
public class MessageBoxRidget extends AbstractRidget implements IMessageBoxRidget {

	private MessageBox messageBox;
	private String title;
	private String text;
	private boolean visible;
	private boolean enabled = true;
	private boolean focusable;
	private Type type = Type.PLAIN;
	private MessageBoxOption[] options = OPTIONS_OK;

	/**
	 * Default constructor.
	 */
	public MessageBoxRidget() {
		super();

		focusable = true;
	}

	public MessageBoxOption[] getOptions() {
		return options;
	}

	public String getText() {
		return text;
	}

	public String getTitle() {
		return title;
	}

	public Type getType() {
		return type;
	}

	public void setOptions(final MessageBoxOption[] options) {
		this.options = options;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setType(final Type type) {
		this.type = type;
	}

	public MessageBoxOption show() {

		if (messageBox != null) {
			if (type == null) {
				type = Type.PLAIN;
			}
			return show(type);
		} else {
			return null;
		}
	}

	private MessageBoxOption show(final Type type) {

		messageBox.show(getTitle(), getText(), getType(type), getButtonLabels(type));

		return getResultOption();
	}

	private String[] getButtonLabels(final Type type) {

		final String[] labels = new String[options.length];
		for (int i = 0; i < options.length; i++) {
			labels[i] = getButtonLabel(options[i]);
		}

		return labels;
	}

	private String getButtonLabel(final MessageBoxOption option) {
		String result;
		if (OK.equals(option)) {
			result = DialogConstantsFacade.getDefault().getOkLabel();
		} else if (CANCEL.equals(option)) {
			result = DialogConstantsFacade.getDefault().getCancelLabel();
		} else if (YES.equals(option)) {
			result = DialogConstantsFacade.getDefault().getYesLabel();
		} else if (NO.equals(option)) {
			result = DialogConstantsFacade.getDefault().getNoLabel();
		} else {
			result = option.getLabel();
		}
		return result;
	}

	private MessageBoxOption getResultOption() {

		final int result = messageBox.getResult();

		if (result == SWT.DEFAULT) {
			return CLOSED;
		} else {
			return getOptions()[result];
		}
	}

	private int getType(final Type type) {

		switch (type) {
		case PLAIN:
			return MessageDialog.NONE;
		case INFORMATION:
			return MessageDialog.INFORMATION;
		case WARNING:
			return MessageDialog.WARNING;
		case ERROR:
			return MessageDialog.ERROR;
		case HELP:
			return MessageDialog.INFORMATION;
		case QUESTION:
			return MessageDialog.QUESTION;
		default:
			return MessageDialog.NONE;
		}
	}

	public String getID() {
		if (getUIControl() != null) {
			return getUIControl().getPropertyName();
		}

		return null;
	}

	public String getToolTipText() {
		// not supported
		return null;
	}

	public MessageBox getUIControl() {
		return messageBox;
	}

	public boolean hasFocus() {
		return messageBox.hasFocus();
	}

	public boolean isFocusable() {
		return focusable;
	}

	public boolean isVisible() {
		return messageBox != null && visible;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void requestFocus() {
		if (isFocusable()) {
			messageBox.requestFocus();
		}
	}

	public void setFocusable(final boolean focusable) {
		this.focusable = focusable;
	}

	/**
	 * @throws UnsupportedOperationException
	 *             this class does not support this operation
	 */
	public void setToolTipText(final String toolTipText) {
		throw new UnsupportedOperationException("not supported"); //$NON-NLS-1$
	}

	public void setUIControl(final Object uiControl) {
		assertUIControlType(uiControl, MessageBox.class);
		messageBox = (MessageBox) uiControl;
		updateUIControl();
	}

	public void setVisible(final boolean visible) {
		this.visible = visible;
		updateUIControl();
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
		updateUIControl();
	}

	@Override
	public void updateFromModel() {
		// do nothing
	}

	private void updateUIControl() {
		if (messageBox != null) {
			updateVisible();
			updateEnabled();
		}
	}

	private void updateVisible() {
		messageBox.setVisible(visible);
	}

	private void updateEnabled() {
		messageBox.setVisible(enabled);
	}

	/**
	 * Checks the type of the UI-control. If the test fails, some kind of
	 * unchecked exception is thrown.
	 * 
	 * @param uiControl
	 *            The UI-control to be checked.
	 * 
	 * @param requiredUIControlType
	 *            The required type.
	 */
	protected void assertUIControlType(final Object uiControl, final Class<MessageBox> requiredUIControlType) {
		Assert.isTrue(uiControl == null || requiredUIControlType.isAssignableFrom(uiControl.getClass()),
				"Wrong UI-control type. Expected " + requiredUIControlType); //$NON-NLS-1$
	}

}
