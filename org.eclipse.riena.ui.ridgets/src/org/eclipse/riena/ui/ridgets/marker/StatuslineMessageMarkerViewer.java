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
package org.eclipse.riena.ui.ridgets.marker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.IMessageMarker;
import org.eclipse.riena.ui.core.marker.RowErrorMessageMarker;
import org.eclipse.riena.ui.ridgets.IBasicMarkableRidget;
import org.eclipse.riena.ui.ridgets.IStatuslineRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.listener.FocusEvent;
import org.eclipse.riena.ui.ridgets.listener.IFocusListener;
import org.eclipse.riena.ui.ridgets.listener.ISelectionListener;
import org.eclipse.riena.ui.ridgets.listener.SelectionEvent;

/**
 * Visualizes certain types of message markers by displaying the message in the
 * status line.
 */
public class StatuslineMessageMarkerViewer extends AbstractMessageMarkerViewer {

	private String statuslineMessage;
	private String originalStatuslineMessage;
	private IStatuslineRidget statusline = null;

	private final PropertyChangeListener markerPropertyChangeListener = new MarkerPropertyChangeListener();
	private final IFocusListener ridgetFocusListener = new RidgetFocusListener();

	enum Severity {
		NONE(0), INFO(1), WARNING(2), ERROR(3);
		private int index;

		Severity(final int index) {
			this.index = index;
		}

		boolean isLower(final Severity other) {
			return index < other.index;
		}
	}

	/**
	 * @param statuslineridget
	 *            The status line.
	 */
	public StatuslineMessageMarkerViewer(final IStatuslineRidget statuslineRidget) {
		this.statusline = statuslineRidget;
	}

	@Override
	public void addRidget(final IBasicMarkableRidget markableRidget) {
		super.addRidget(markableRidget);
		if (markableRidget instanceof ITableRidget) {
			((ITableRidget) markableRidget).addSelectionListener(new ISelectionListener() {

				public void ridgetSelected(final SelectionEvent event) {
					showMessages(markableRidget);
				}
			});
		}
		markableRidget.addPropertyChangeListener(markerPropertyChangeListener);
		markableRidget.addFocusListener(ridgetFocusListener);
	}

	@Override
	protected void showMessages(final IBasicMarkableRidget markableRidget) {
		if (markableRidget.hasFocus()) {
			Collection<IMessageMarker> messageMarker = this.getMessageMarker(markableRidget);

			// for tableRidgets MessageMarkers will be filtered
			if (markableRidget instanceof ITableRidget) {
				messageMarker = new HashSet<IMessageMarker>(messageMarker);
				filterRowErrorMessageMarker((ITableRidget) markableRidget, messageMarker);
			}

			final String message = constructMessage(messageMarker, getMessageSeparator());
			final Severity severity = getMaxSeverity(messageMarker);
			// show the message only if there is something to show
			if (message.length() > 0 && isVisible()) {
				setStatuslineMessage(message, severity);
			} else {
				hideMessages(markableRidget);
			}
		}
	}

	@Override
	protected void hideMessages(final IBasicMarkableRidget ridget) {
		if (ridget.hasFocus()) {
			resetStatuslineMessage();
		}
	}

	@Override
	protected String getMessageSeparator() {
		return " "; //$NON-NLS-1$
	}

	/**
	 * @param messageMarker
	 */
	private void filterRowErrorMessageMarker(final ITableRidget tableRidget,
			final Collection<IMessageMarker> messageMarker) {

		final List<Object> selection = tableRidget.getSelection();
		for (final IMessageMarker marker : this.getMessageMarker(tableRidget)) {
			if (marker instanceof RowErrorMessageMarker) {
				if (!selection.contains(((RowErrorMessageMarker) marker).getRowValue())) {
					messageMarker.remove(marker);
				}
			}
		}

		// filter markers with equal messages
		final Set<String> seenMessages = new HashSet<String>();
		for (final IMessageMarker marker : messageMarker) {
			if (seenMessages.contains(marker.getMessage())) {
				messageMarker.remove(marker);
			}
			seenMessages.add(marker.getMessage());
		}

	}

	private void setStatuslineMessage(final String message, final Severity severity) {
		if (getStatusLine() != null) {
			if (statuslineMessage == null) {
				originalStatuslineMessage = getStatusLine().getMessage();
			}
			switch (severity) {
			case ERROR:
				getStatusLine().error(message);
				break;
			case WARNING:
				getStatusLine().warning(message);
				break;
			case INFO:
				getStatusLine().info(message);
				break;
			default:
				getStatusLine().clear();
				getStatusLine().setMessage(message);
				break;
			}
			statuslineMessage = message;
		}
	}

	private void resetStatuslineMessage() {
		if (getStatusLine() != null) {
			if (statuslineMessage != null && statuslineMessage.equals(getStatusLine().getMessage())) {
				getStatusLine().clear();
				getStatusLine().setMessage(originalStatuslineMessage);
			}
			statuslineMessage = null;
		}
	}

	private Severity getMaxSeverity(final Collection<IMessageMarker> messageMarkers) {

		Severity severity = Severity.NONE;

		for (final IMessageMarker messageMarker : messageMarkers) {
			if (messageMarker instanceof ErrorMarker) {
				if (severity.isLower(Severity.ERROR)) {
					severity = Severity.ERROR;
				}
			}
		}

		return severity;

	}

	/**
	 * @since 5.0
	 */
	public IStatuslineRidget getStatusLine() {
		// if ( statusline == null ) {
		// IModuleApplicationController moduleApplicationController =
		// subModuleController.getModuleApplicationController();
		// if ( moduleApplicationController != null ) {
		// statusline = moduleApplicationController.getStatusline();
		// PostCondition.assertNotNull( "The statusline to show messages in must
		// not be
		// null!", statusline );
		// }
		// }
		return statusline;
	}

	private class MarkerPropertyChangeListener implements PropertyChangeListener {

		public void propertyChange(final PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(IBasicMarkableRidget.PROPERTY_MARKER)
					&& evt.getSource() instanceof IBasicMarkableRidget
					&& ((IBasicMarkableRidget) evt.getSource()).hasFocus()) {
				showMessages((IBasicMarkableRidget) evt.getSource());
			}
		}

	}

	private class RidgetFocusListener implements IFocusListener {

		public void focusGained(final FocusEvent event) {
			if (event.getNewFocusOwner() instanceof IBasicMarkableRidget) {
				showMessages((IBasicMarkableRidget) event.getNewFocusOwner());
			}
		}

		public void focusLost(final FocusEvent event) {
			if (event.getOldFocusOwner() instanceof IBasicMarkableRidget) {
				resetStatuslineMessage();
			}
		}

	}

}
