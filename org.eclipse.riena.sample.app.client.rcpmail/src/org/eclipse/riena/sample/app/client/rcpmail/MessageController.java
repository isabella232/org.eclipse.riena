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
package org.eclipse.riena.sample.app.client.rcpmail;

import org.eclipse.core.databinding.beans.PojoObservables;

import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractRidgetController;

/**
 * Ridget controller used by the {@link MessageView}.
 * 
 * @see MessageView
 */
public class MessageController extends AbstractRidgetController {

	/**
	 * Mock object holding the data of a single message.
	 */
	private static class MailMessage {
		private String subject = "This a message about the cool Eclipse RCP!"; //$NON-NLS-1$
		private String from = "nicole@mail.org"; //$NON-NLS-1$
		private String date = "10:34 am"; //$NON-NLS-1$
		private String message = "This RCP Application was generated from the PDE Plug-in Project wizard. This sample shows how to:\n" + //$NON-NLS-1$
				"- add a top-level menu and toolbar with actions\n" + //$NON-NLS-1$
				"- add keybindings to actions\n" + //$NON-NLS-1$
				"- create views that can't be closed and\n" + //$NON-NLS-1$
				"  multiple instances of the same view\n" + //$NON-NLS-1$
				"- perspectives with placeholders for new views\n" + //$NON-NLS-1$
				"- use the default about dialog\n" + //$NON-NLS-1$
				"- create a product definition\n"; //$NON-NLS-1$

		@SuppressWarnings("unused")
		public String getDate() {
			return date;
		}

		@SuppressWarnings("unused")
		public String getFrom() {
			return from;
		}

		@SuppressWarnings("unused")
		public String getMessage() {
			return message;
		}

		@SuppressWarnings("unused")
		public String getSubject() {
			return subject;
		}

		@SuppressWarnings("unused")
		public void setDate(final String date) {
			this.date = date;
		}

		@SuppressWarnings("unused")
		public void setFrom(final String from) {
			this.from = from;
		}

		@SuppressWarnings("unused")
		public void setMessage(final String message) {
			this.message = message;
		}

		@SuppressWarnings("unused")
		public void setSubject(final String subject) {
			this.subject = subject;
		}
	}

	private final MailMessage message = new MailMessage();

	@Override
	public void configureRidgets() {
		final ILabelRidget lblSubject = getRidget("subject"); //$NON-NLS-1$
		lblSubject.bindToModel(PojoObservables.observeValue(message, "subject")); //$NON-NLS-1$
		lblSubject.updateFromModel();

		final ILabelRidget lblFrom = getRidget("from"); //$NON-NLS-1$
		lblFrom.bindToModel(PojoObservables.observeValue(message, "from")); //$NON-NLS-1$
		lblFrom.updateFromModel();

		final ILabelRidget lblDate = getRidget("date"); //$NON-NLS-1$
		lblDate.bindToModel(PojoObservables.observeValue(message, "date")); //$NON-NLS-1$
		lblDate.updateFromModel();

		final ITextRidget txtMessage = getRidget("message"); //$NON-NLS-1$
		txtMessage.bindToModel(PojoObservables.observeValue(message, "message")); //$NON-NLS-1$
		txtMessage.setOutputOnly(true);
		txtMessage.updateFromModel();
	}

}
