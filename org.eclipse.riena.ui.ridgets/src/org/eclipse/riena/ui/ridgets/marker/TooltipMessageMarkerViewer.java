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
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.core.marker.IMessageMarker;
import org.eclipse.riena.ui.ridgets.IBasicMarkableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;

/**
 * Visualizes certain types of message markers by setting the tooltip of the
 * Ridget.
 */
public class TooltipMessageMarkerViewer extends AbstractMessageMarkerViewer {

	private final PropertyChangeListener markerPropertyChangeListener = new MarkerPropertyChangeListener();

	private final HashMap<IBasicMarkableRidget, Tooltip> tooltips = new LinkedHashMap<IBasicMarkableRidget, Tooltip>();

	@Override
	public void addRidget(final IBasicMarkableRidget markableRidget) {
		super.addRidget(markableRidget);
		markableRidget.addPropertyChangeListener(markerPropertyChangeListener);
	}

	@Override
	public void removeRidget(final IBasicMarkableRidget markableRidget) {
		markableRidget.removePropertyChangeListener(markerPropertyChangeListener);
		super.removeRidget(markableRidget);
	}

	// protected methods
	////////////////////

	@Override
	protected void showMessages(final IBasicMarkableRidget markableRidget) {
		final Collection<IMessageMarker> messageMarker = this.getMessageMarker(markableRidget);
		final String message = constructMessage(messageMarker, getMessageSeparator());
		// show the message only if there is something to show
		final String current = markableRidget.getToolTipText();
		if (message.length() > 0 && isVisible()) {
			if (!message.equals(current)) {
				Tooltip tooltip = tooltips.get(markableRidget);
				if (tooltip == null) {
					tooltip = new Tooltip(current, message);
					tooltips.put(markableRidget, tooltip);
				} else {
					tooltip.messageTooltip = message;
				}
				markableRidget.setToolTipText(message);
			}
		} else {
			hideMessages(markableRidget);
		}
	}

	@Override
	protected void hideMessages(final IBasicMarkableRidget markableRidget) {
		if (tooltips.containsKey(markableRidget)) {
			final String tooltip = tooltips.get(markableRidget).originalTooltip;
			markableRidget.setToolTipText(tooltip);
			tooltips.remove(markableRidget);
		}
	}

	@Override
	protected String getMessageSeparator() {
		return "\n"; //$NON-NLS-1$
	}

	// helping classes
	//////////////////

	private final class MarkerPropertyChangeListener implements PropertyChangeListener {

		public void propertyChange(final PropertyChangeEvent evt) {
			if (evt.getSource() instanceof IBasicMarkableRidget) {
				if (IBasicMarkableRidget.PROPERTY_MARKER.equals(evt.getPropertyName())) {
					final IBasicMarkableRidget ridget = (IBasicMarkableRidget) evt.getSource();
					showMessages(ridget);
				} else if (IRidget.PROPERTY_TOOLTIP.equals(evt.getPropertyName())) {
					final IBasicMarkableRidget ridget = (IBasicMarkableRidget) evt.getSource();
					final String newTooltip = (String) evt.getNewValue();
					final Tooltip tip = tooltips.get(ridget);
					if (tip != null && !StringUtils.equals(newTooltip, tip.originalTooltip)) {
						showMessages(ridget);
						/*
						 * If the newTooltip is not the messageTooltip, it is
						 * new 'original' tooltip -> Update.
						 */
						if (!StringUtils.equals(newTooltip, tip.messageTooltip)) {
							tip.originalTooltip = newTooltip;
						}
					}
				}
			}
		}
	}

	/**
	 * Holds two tooltip values (Strings): (a) the original tooltip of the
	 * ridget and (b) the compute message tooltip
	 */
	private static final class Tooltip {
		/**
		 * Tooltip value to use when the 'message' tooltip is removed. May be
		 * null.
		 */
		private String originalTooltip;
		/** Tooltip value for the 'message' tooltip. */
		private String messageTooltip;

		Tooltip(final String originalTooltip, final String messageTooltip) {
			this.originalTooltip = originalTooltip;
			this.messageTooltip = messageTooltip;
		}

		@Override
		public String toString() {
			return String.format("[%s,%s]", originalTooltip, messageTooltip); //$NON-NLS-1$
		}
	}

}
