/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets.marker;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.riena.ui.core.marker.IMessageMarker;
import org.eclipse.riena.ui.ridgets.IBasicMarkableRidget;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget;

/**
 * Visualizes certain types of message markers by displaying the message in a
 * message box.
 */
public class MessageBoxMessageMarkerViewer extends AbstractMessageMarkerViewer {

	private IMessageBoxRidget messageBoxRidget;

	public MessageBoxMessageMarkerViewer(IMessageBoxRidget messageBoxRidget) {
		this.messageBoxRidget = messageBoxRidget;
		setVisible(false);
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.marker.AbstractMessageMarkerViewer#hideMessages(org.eclipse.riena.ui.internal.ridgets.IBasicMarkableRidget)
	 */
	@Override
	protected void hideMessages(IBasicMarkableRidget ridget) {
		// automatically hidden when the message box is closed
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.marker.AbstractMessageMarkerViewer#showMessages(org.eclipse.riena.ui.internal.ridgets.IBasicMarkableRidget)
	 */
	@Override
	protected void showMessages(IBasicMarkableRidget ridget) {
		if (isVisible()) {
			String message = getMessage();
			if (message.length() > 0) {
				messageBoxRidget.setText(message);
				messageBoxRidget.show();
			}
			setVisible(false);
		}
	}

	/**
	 * Construct a Message of all Adapter for display, remove the marker!
	 * 
	 * @return a complete Message
	 */
	private String getMessage() {
		Collection<IMessageMarker> allMessageMarker = new LinkedHashSet<IMessageMarker>();
		IBasicMarkableRidget nextMarkableAdapter = null;
		Collection<IBasicMarkableRidget> localMarkableAdapter = new HashSet<IBasicMarkableRidget>();
		localMarkableAdapter.addAll(getRidgets());
		for (Iterator<IBasicMarkableRidget> i = localMarkableAdapter.iterator(); i.hasNext();) {
			nextMarkableAdapter = i.next();
			allMessageMarker.addAll(getMessageMarker(nextMarkableAdapter, false));
		}
		return constructMessage(allMessageMarker);
	}

	private String constructMessage(Collection<IMessageMarker> pMessageMarker) {
		StringWriter sw = new StringWriter();
		if (pMessageMarker != null) {
			List<IMessageMarker> sortedMarkers = new ArrayList<IMessageMarker>(pMessageMarker);
			Collections.sort(sortedMarkers, new MessageMarkerComparator());
			for (IMessageMarker nextMarker : sortedMarkers) {
				if (sw.toString().trim().length() > 0) {
					sw.write("\n"); //$NON-NLS-1$
				}
				// TODO [ev] StringBuilder + Check for empty message
				if (nextMarker.getMessage() != null) {
					sw.write(nextMarker.getMessage());
				}
			}
		}
		return sw.toString().trim();
	}

}
