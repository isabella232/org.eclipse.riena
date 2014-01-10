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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.internal.ui.swt.test.UITestHelper;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget.MessageBoxOption;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.listener.FocusEvent;
import org.eclipse.riena.ui.ridgets.listener.IFocusListener;
import org.eclipse.riena.ui.swt.MessageBox;

/**
 * Tests for the MessageBoxRidget.
 */
public class MessageBoxRidgetTest extends AbstractRidgetTestCase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetTest#createWidget
	 * (org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected MessageBox createWidget(final Composite parent) {
		return new MockMessageBox(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetNonWidgetTest
	 * #createRidget()
	 */
	@Override
	protected IRidget createRidget() {
		return new MessageBoxRidget();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetNonWidgetTest
	 * #getRidget()
	 */
	@Override
	protected IMessageBoxRidget getRidget() {
		return (IMessageBoxRidget) super.getRidget();
	}

	@Override
	protected MockMessageBox getWidget() {
		return (MockMessageBox) super.getWidget();
	}

	public void testSetText() throws Exception {

		// show message box to transfer values set
		getRidget().show();

		assertNull(getWidget().text);
		assertNull(getRidget().getText());

		getRidget().setText("TestMessageText");
		// show message box to transfer values set
		getRidget().show();

		assertEquals("TestMessageText", getWidget().text);
		assertEquals("TestMessageText", getRidget().getText());
	}

	public void testSetTitle() throws Exception {

		// show message box to transfer values set
		getRidget().show();

		assertNull(getWidget().title);
		assertNull(getRidget().getTitle());

		getRidget().setTitle("TestMessageTitle");
		// show message box to transfer values set
		getRidget().show();

		assertEquals("TestMessageTitle", getWidget().title);
		assertEquals("TestMessageTitle", getRidget().getTitle());
	}

	public void testSetType() throws Exception {

		// show message box to transfer values set
		getRidget().show();

		assertEquals(MessageDialog.NONE, getWidget().type);
		assertEquals(IMessageBoxRidget.Type.PLAIN, getRidget().getType());

		getRidget().setType(IMessageBoxRidget.Type.ERROR);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(MessageDialog.ERROR, getWidget().type);
		assertEquals(IMessageBoxRidget.Type.ERROR, getRidget().getType());

		getRidget().setType(IMessageBoxRidget.Type.WARNING);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(MessageDialog.WARNING, getWidget().type);
		assertEquals(IMessageBoxRidget.Type.WARNING, getRidget().getType());

		getRidget().setType(IMessageBoxRidget.Type.INFORMATION);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(MessageDialog.INFORMATION, getWidget().type);
		assertEquals(IMessageBoxRidget.Type.INFORMATION, getRidget().getType());

		getRidget().setType(IMessageBoxRidget.Type.HELP);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(MessageDialog.INFORMATION, getWidget().type);
		assertEquals(IMessageBoxRidget.Type.HELP, getRidget().getType());

		getRidget().setType(IMessageBoxRidget.Type.QUESTION);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(MessageDialog.QUESTION, getWidget().type);
		assertEquals(IMessageBoxRidget.Type.QUESTION, getRidget().getType());

		getRidget().setType(null);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(MessageDialog.NONE, getWidget().type);
		assertEquals(IMessageBoxRidget.Type.PLAIN, getRidget().getType());
	}

	public void testSetOptions() throws Exception {

		// show message box to transfer values set
		getRidget().show();

		assertEquals(1, getWidget().buttonLabels.length);
		assertEquals(IDialogConstants.OK_LABEL, getWidget().buttonLabels[0]);
		assertEquals(IMessageBoxRidget.OPTIONS_OK, getRidget().getOptions());

		getRidget().setOptions(IMessageBoxRidget.OPTIONS_YES_NO);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(2, getWidget().buttonLabels.length);
		assertEquals(IDialogConstants.YES_LABEL, getWidget().buttonLabels[0]);
		assertEquals(IDialogConstants.NO_LABEL, getWidget().buttonLabels[1]);
		assertEquals(IMessageBoxRidget.OPTIONS_YES_NO, getRidget().getOptions());

		getRidget().setOptions(IMessageBoxRidget.OPTIONS_OK_CANCEL);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(2, getWidget().buttonLabels.length);
		assertEquals(IDialogConstants.OK_LABEL, getWidget().buttonLabels[0]);
		assertEquals(IDialogConstants.CANCEL_LABEL, getWidget().buttonLabels[1]);
		assertEquals(IMessageBoxRidget.OPTIONS_OK_CANCEL, getRidget().getOptions());

		getRidget().setOptions(IMessageBoxRidget.OPTIONS_YES_NO_CANCEL);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(3, getWidget().buttonLabels.length);
		assertEquals(IDialogConstants.YES_LABEL, getWidget().buttonLabels[0]);
		assertEquals(IDialogConstants.NO_LABEL, getWidget().buttonLabels[1]);
		assertEquals(IDialogConstants.CANCEL_LABEL, getWidget().buttonLabels[2]);
		assertEquals(IMessageBoxRidget.OPTIONS_YES_NO_CANCEL, getRidget().getOptions());

		final MessageBoxOption[] semiCustomOptions = new IMessageBoxRidget.MessageBoxOption[] { IMessageBoxRidget.OK,
				new IMessageBoxRidget.MessageBoxOption("TestCustomOptionText") };
		getRidget().setOptions(semiCustomOptions);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(2, getWidget().buttonLabels.length);
		assertEquals("OK", getWidget().buttonLabels[0]);
		assertEquals("TestCustomOptionText", getWidget().buttonLabels[1]);
		assertEquals(semiCustomOptions, getRidget().getOptions());

		final MessageBoxOption[] customOptions = new IMessageBoxRidget.MessageBoxOption[] {
				new IMessageBoxRidget.MessageBoxOption("TestCustomOptionText1"),
				new IMessageBoxRidget.MessageBoxOption("TestCustomOptionText2"),
				new IMessageBoxRidget.MessageBoxOption("TestCustomOptionText3") };
		getRidget().setOptions(customOptions);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(3, getWidget().buttonLabels.length);
		assertEquals("TestCustomOptionText1", getWidget().buttonLabels[0]);
		assertEquals("TestCustomOptionText2", getWidget().buttonLabels[1]);
		assertEquals("TestCustomOptionText3", getWidget().buttonLabels[2]);
		assertEquals(customOptions, getRidget().getOptions());

		getRidget().setOptions(IMessageBoxRidget.OPTIONS_OK);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(1, getWidget().buttonLabels.length);
		assertEquals(IDialogConstants.OK_LABEL, getWidget().buttonLabels[0]);
		assertEquals(IMessageBoxRidget.OPTIONS_OK, getRidget().getOptions());
	}

	public void testUsedInSharedView() throws Exception {

		getRidget().setText("TestMessageText1");
		getRidget().setTitle("TestMessageTitle1");
		getRidget().setType(IMessageBoxRidget.Type.QUESTION);
		getRidget().setOptions(IMessageBoxRidget.OPTIONS_OK_CANCEL);
		// show message box to transfer values set
		getRidget().show();

		final IMessageBoxRidget ridget2 = new MessageBoxRidget();
		ridget2.setText("TestMessageText2");
		ridget2.setTitle("TestMessageTitle2");
		ridget2.setType(IMessageBoxRidget.Type.WARNING);
		ridget2.setOptions(new IMessageBoxRidget.MessageBoxOption[] { new IMessageBoxRidget.MessageBoxOption(
				"TestCustomOption") });

		getRidget().setUIControl(null);
		ridget2.setUIControl(getWidget());
		// show message box to transfer values set
		ridget2.show();

		assertEquals("TestMessageText2", getWidget().text);
		assertEquals("TestMessageTitle2", getWidget().title);
		assertEquals(MessageDialog.WARNING, getWidget().type);
		assertEquals(1, getWidget().buttonLabels.length);
		assertEquals("TestCustomOption", getWidget().buttonLabels[0]);

		getRidget().setUIControl(getWidget());
		// show message box to transfer values set
		getRidget().show();
		ridget2.setUIControl(null);

		assertEquals("TestMessageText1", getWidget().text);
		assertEquals("TestMessageTitle1", getWidget().title);
		assertEquals(MessageDialog.QUESTION, getWidget().type);
		assertEquals(2, getWidget().buttonLabels.length);
		assertEquals(IDialogConstants.OK_LABEL, getWidget().buttonLabels[0]);
		assertEquals(IDialogConstants.CANCEL_LABEL, getWidget().buttonLabels[1]);
	}

	public void testShow() throws Exception {

		getRidget().setTitle("TestMessageTitle");
		getRidget().setText("TestMessageText");
		getRidget().setType(IMessageBoxRidget.Type.QUESTION);
		getRidget().setOptions(IMessageBoxRidget.OPTIONS_YES_NO);

		// simulate 'yes' button, i.e. button with id 0 is clicked.
		setMessageBoxReturnValue(0);

		assertEquals(IMessageBoxRidget.YES, getRidget().show());

		// simulate 'close' button, i.e. button with id SWT.DEFAULT is clicked.
		setMessageBoxReturnValue(SWT.DEFAULT);

		assertEquals(IMessageBoxRidget.CLOSED, getRidget().show());

		final IMessageBoxRidget.MessageBoxOption option3 = new IMessageBoxRidget.MessageBoxOption("TestCustomOption3");
		getRidget().setOptions(
				new IMessageBoxRidget.MessageBoxOption[] { new IMessageBoxRidget.MessageBoxOption("TestCustomOption1"),
						new IMessageBoxRidget.MessageBoxOption("TestCustomOption2"), option3,
						new IMessageBoxRidget.MessageBoxOption("TestCustomOption4") });

		setMessageBoxReturnValue(2);

		assertEquals(option3, getRidget().show());

		// simulate 'close' button, i.e. button with id SWT.DEFAULT is clicked.
		setMessageBoxReturnValue(SWT.DEFAULT);

		assertEquals(IMessageBoxRidget.CLOSED, getRidget().show());

		getRidget().setUIControl(null);

		assertNull(getRidget().show());
	}

	public void testGetID() throws Exception {

		getWidget().setPropertyName("testId");
		assertEquals("testId", getRidget().getID());
	}

	public void testGetSetUIControl() throws Exception {

		assertEquals(getWidget(), getRidget().getUIControl());
		getRidget().setUIControl(null);
		assertEquals(null, getRidget().getUIControl());
		getRidget().setUIControl(getWidget());
		assertEquals(getWidget(), getRidget().getUIControl());
	}

	public void testGetFocusable() {

		final IRidget aRidget = getRidget();

		assertTrue(aRidget.isFocusable());

		aRidget.setFocusable(false);

		assertFalse(aRidget.isFocusable());

		aRidget.setFocusable(true);

		assertTrue(aRidget.isFocusable());
	}

	public void testSetFocusable() {

		final IRidget aRidget = getRidget();
		final MockMessageBox aControl = getWidget();
		// getOtherControl().moveAbove(aControl); ???

		aControl.requestFocus();
		// TODO: focus not gained here, because control has to be shown to requested focus
		if (aControl.hasFocus()) { // skip if control cannot receive focus

			aRidget.setFocusable(false);
			getOtherControl().setFocus();

			assertTrue(getOtherControl().isFocusControl());

			UITestHelper.sendString(getOtherControl().getDisplay(), "\t");

			assertFalse(aControl.hasFocus());

			aRidget.setFocusable(true);

			getOtherControl().setFocus();
			UITestHelper.sendString(getOtherControl().getDisplay(), "\t");

			assertTrue(aControl.hasFocus());
		}
	}

	public void testRequestFocus() throws Exception {

		final MessageBox aControl = getWidget();
		aControl.requestFocus();
		// TODO: focus not gained here, because control has to be shown to requested focus
		if (aControl.hasFocus()) { // skip if control cannot receive focus
			assertTrue(getOtherControl().setFocus());

			assertFalse(aControl.hasFocus());
			assertFalse(getRidget().hasFocus());

			final List<FocusEvent> focusGainedEvents = new ArrayList<FocusEvent>();
			final List<FocusEvent> focusLostEvents = new ArrayList<FocusEvent>();
			final IFocusListener focusListener = new IFocusListener() {
				public void focusGained(final FocusEvent event) {
					focusGainedEvents.add(event);
				}

				public void focusLost(final FocusEvent event) {
					focusLostEvents.add(event);
				}
			};
			getRidget().addFocusListener(focusListener);

			getRidget().requestFocus();

			assertTrue(aControl.hasFocus());
			assertTrue(getRidget().hasFocus());
			assertEquals(1, focusGainedEvents.size());
			assertEquals(getRidget(), focusGainedEvents.get(0).getNewFocusOwner());
			assertEquals(0, focusLostEvents.size());

			assertTrue(getOtherControl().setFocus());

			assertFalse(aControl.hasFocus());
			assertFalse(getRidget().hasFocus());
			assertEquals(1, focusGainedEvents.size());
			assertEquals(1, focusLostEvents.size());
			assertEquals(getRidget(), focusLostEvents.get(0).getOldFocusOwner());

			getRidget().removeFocusListener(focusListener);

			getRidget().requestFocus();
			assertTrue(getOtherControl().setFocus());

			assertEquals(1, focusGainedEvents.size());
			assertEquals(1, focusLostEvents.size());
		}
	}

	@Override
	public void testFiresTooltipProperty() {
		// tooltips are not supported
	}

	@Override
	public void testIsVisible() {

		assertFalse(getRidget().isVisible());

		getRidget().setVisible(true);

		super.testIsVisible();
	}

	private void setMessageBoxReturnValue(final int returnValue) {
		getRidget().setUIControl(new MessageBox(getShell()) {

			@Override
			public void show(final String title, final String text, final int type, final String[] buttonLabels) {
			}

			@Override
			public int getResult() {
				return returnValue;
			}
		});
	}

	private void assertEquals(final MessageBoxOption[] expected, final MessageBoxOption[] actual) {
		if (Arrays.equals(expected, actual)) {
			return;
		}
		failNotEquals(null, expected, actual);
	}

	private final static class MockMessageBox extends MessageBox {

		private String title;
		private String text;
		private int type;
		private String[] buttonLabels;

		private MockMessageBox(final Composite parent) {
			super(parent);
		}

		@Override
		public void show(final String title, final String text, final int type, final String[] buttonLabels) {
			// save values her to check after showing that the call of show was performed with the correct arguments.
			this.title = title;
			this.text = text;
			this.type = type;
			this.buttonLabels = buttonLabels;
		}
	}

}
