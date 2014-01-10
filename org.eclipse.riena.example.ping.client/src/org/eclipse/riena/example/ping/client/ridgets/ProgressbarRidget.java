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
package org.eclipse.riena.example.ping.client.ridgets;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.graphics.Color;

import org.eclipse.riena.example.ping.client.widgets.ProgressBarWidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractValueRidget;

/**
 * A Ridget for the progress bar used in sonar. Additionally to the progress you
 * may also set the back- and foreground of the bar.
 */
public class ProgressbarRidget extends AbstractValueRidget {

	protected static final String PROPERTY_SELECTION = "selection"; //$NON-NLS-1$
	protected static final String PROPERTY_MINIMUM = "minimum"; //$NON-NLS-1$
	protected static final String PROPERTY_MAXIMUM = "maximum"; //$NON-NLS-1$
	protected static final String PROPERTY_FOREGROUND = "foreground"; //$NON-NLS-1$
	protected static final String PROPERTY_BACKGROUND = "background"; //$NON-NLS-1$

	/**
	 * This property is used by the databinding to sync ridget and model. It is
	 * always fired before its sibling {@link #PROPERTY_SELECTION} to ensure
	 * that the model is updated before any listeners try accessing it.
	 * <p>
	 * This property is not API. Do not use in client code.
	 */
	private static final String PROPERTY_SELECTION_INTERNAL = "selectionInternal"; //$NON-NLS-1$

	private Integer selection;
	private Integer minimum;
	private Integer maximum;
	private Color foreground;
	private Color background;
	private boolean foregroundAlreadyInitialized;
	private boolean backgroundAlreadyInitialized;
	private boolean selectionAlreadyInitialized;
	private boolean minimumAlreadyInitialized;
	private boolean maximumAlreadyInitialized;

	@Override
	protected IObservableValue getRidgetObservable() {
		return BeansObservables.observeValue(this, PROPERTY_SELECTION_INTERNAL);
	}

	@Override
	protected void bindUIControl() {
		initMinimum();
		updateUIMinimum();
		initMaximum();
		updateUIMaximum();
		initSelection();
		updateUISelection();
		initForeground();
		updateUIForeground();
		initBackground();
		updateUIBackground();
	}

	@Override
	protected void checkUIControl(final Object uiControl) {
		checkType(uiControl, ProgressBarWidget.class);
	}

	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
	}

	/**
	 * Returns the corresponding {@link ProgressBarWidget}.
	 * 
	 * @return the corresponding {@link ProgressBarWidget}.
	 */
	protected ProgressBarWidget getProgressBarWidget() {
		return (ProgressBarWidget) getUIControl();
	}

	/**
	 * Returns the minimum value for the progress.
	 * 
	 * @return the minimum value for the progress.
	 */
	public int getMinimum() {
		if (minimum == null) {
			return 0;
		}
		return minimum;
	}

	/**
	 * Sets the minimum value for the progress.
	 * 
	 * @param minimum
	 *            the new value.
	 */
	public void setMinimum(final int minimum) {
		final Integer oldMinimum = this.minimum;
		this.minimum = minimum;
		updateUIMinimum();
		firePropertyChange(PROPERTY_MINIMUM, oldMinimum, Integer.valueOf(minimum));
	}

	private void updateUIMinimum() {
		if (getUIControl() != null) {
			getProgressBarWidget().setMinimum(minimum);
		}
	}

	private void initMinimum() {
		if (minimum == null && !minimumAlreadyInitialized) {
			if (getProgressBarWidget() != null && !getProgressBarWidget().isDisposed()) {
				minimum = getProgressBarWidget().getMinimum();
				minimumAlreadyInitialized = true;
			}
		}
	}

	/**
	 * Returns the maximum value for the progress.
	 * 
	 * @return the maximum value for the progress.
	 */
	public int getMaximum() {
		if (maximum == null) {
			return 0;
		}
		return maximum;
	}

	/**
	 * Sets the maximum value for the progress.
	 * 
	 * @param maximum
	 *            the new value.
	 */
	public void setMaximum(final int maximum) {
		final Integer oldMaximum = this.maximum;
		this.maximum = maximum;
		updateUIMaximum();
		firePropertyChange(PROPERTY_MAXIMUM, oldMaximum, Integer.valueOf(maximum));
	}

	private void updateUIMaximum() {
		if (getUIControl() != null) {
			getProgressBarWidget().setMaximum(maximum);
		}
	}

	private void initMaximum() {
		if (maximum == null && !maximumAlreadyInitialized) {
			if (getProgressBarWidget() != null && !getProgressBarWidget().isDisposed()) {
				maximum = getProgressBarWidget().getMaximum();
				maximumAlreadyInitialized = true;
			}
		}
	}

	/**
	 * Returns the value of the progress.
	 * 
	 * @return the value of the progress.
	 */
	public int getSelection() {
		if (selection == null) {
			return 0;
		}
		return selection;
	}

	/**
	 * This method is not API. Do not use in client code.
	 * 
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public final int getSelectionInternal() {
		return getSelection();
	}

	/**
	 * Sets the value for the progress.
	 * 
	 * @param selection
	 *            the new progress.
	 */
	public void setSelection(final int selection) {
		final Integer oldSelection = this.selection;
		this.selection = selection;
		updateUISelection();
		firePropertyChange(PROPERTY_SELECTION_INTERNAL, oldSelection, Integer.valueOf(selection));
		firePropertyChange(PROPERTY_SELECTION, oldSelection, Integer.valueOf(selection));
	}

	/**
	 * This method is not API. Do not use in client code.
	 * 
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public final void setSelectionInternal(final int selection) {
		setSelection(selection);
	}

	private void updateUISelection() {
		if (getUIControl() != null) {
			getProgressBarWidget().setSelection(selection);
		}
	}

	private void initSelection() {
		if (selection == null && !selectionAlreadyInitialized) {
			if (getProgressBarWidget() != null && !getProgressBarWidget().isDisposed()) {
				selection = getProgressBarWidget().getSelection();
				selectionAlreadyInitialized = true;
			}
		}
	}

	/**
	 * Returns the color used to paint the progress bar.
	 * 
	 * @return the color used to paint the progress bar.
	 */
	public Color getForeground() {
		return foreground;
	}

	/**
	 * Sets the color used to paint the progress bar.
	 * 
	 * @param foreground
	 *            the new color.
	 */
	public void setForeground(final Color foreground) {
		final Color oldForeground = this.foreground;
		this.foreground = foreground;
		updateUIForeground();
		firePropertyChange(PROPERTY_FOREGROUND, oldForeground, foreground);
	}

	private void updateUIForeground() {
		if (getUIControl() != null) {
			getProgressBarWidget().setForeground(foreground);
		}
	}

	private void initForeground() {
		if (foreground == null && !foregroundAlreadyInitialized) {
			if (getProgressBarWidget() != null && !getProgressBarWidget().isDisposed()) {
				foreground = getProgressBarWidget().getForeground();
				foregroundAlreadyInitialized = true;
			}
		}
	}

	/**
	 * Returns the background color.
	 * 
	 * @return the background color.
	 */
	public Color getBackground() {
		return background;
	}

	/**
	 * Sets the background color.
	 * 
	 * @param background
	 *            the new color.
	 */
	public void setBackground(final Color background) {
		final Color oldBackground = this.background;
		this.background = background;
		updateUIBackground();
		firePropertyChange(PROPERTY_BACKGROUND, oldBackground, background);
	}

	private void updateUIBackground() {
		if (getUIControl() != null) {
			getProgressBarWidget().setBackground(background);
		}
	}

	private void initBackground() {
		if (background == null && !backgroundAlreadyInitialized) {
			if (getProgressBarWidget() != null && !getProgressBarWidget().isDisposed()) {
				background = getProgressBarWidget().getBackground();
				backgroundAlreadyInitialized = true;
			}
		}
	}

}
