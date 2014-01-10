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
package org.eclipse.riena.ui.core.uiprocess;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * {@link ProcessInfo} hold meta information of the owning {@link UIProcess}
 */
public class ProcessInfo {
	public static final String PROPERTY_TITLE = "title"; //$NON-NLS-1$
	public static final String PROPERTY_ICON = "icon"; //$NON-NLS-1$
	public static final String PROPERTY_NOTE = "note"; //$NON-NLS-1$
	public static final String PROPERTY_MAX_PROGRESS = "maxProgress"; //$NON-NLS-1$
	public static final String PROPERTY_CANCELED = "cancel"; //$NON-NLS-1$
	public static final String PROPERTY_DIALOG_VISIBLE = "dialog.visible"; //$NON-NLS-1$
	public static final String PROPERTY_CONTEXT = "context"; //$NON-NLS-1$
	public static final String PROPERTY_STYLE = "style"; //$NON-NLS-1$
	public static final String PROPERTY_PROGRESS_STRATEGY = "progress.strategy"; //$NON-NLS-1$
	/**
	 * @since 3.0
	 */
	public static final String PROPERTY_CANCEL_VISIBLE = "cancel.visible"; //$NON-NLS-1$
	/**
	 * @since 3.0
	 */
	public static final String PROPERTY_CANCEL_ENABLED = "cancel.enabled"; //$NON-NLS-1$

	/// properties
	private String title;
	private String icon;
	private String note;
	private int maxProgress;
	private boolean dialogVisible;
	private Object context;
	private boolean canceled;
	private boolean ignoreCancel;
	private ProgresStrategy progresStartegy;
	private boolean cancelEnabled;
	private boolean cancelVisible;

	private final PropertyChangeSupport ppSupport;

	public ProcessInfo() {
		ppSupport = new PropertyChangeSupport(this);
		progresStartegy = ProgresStrategy.UNIT;
		maxProgress = 0;
		dialogVisible = true;
		cancelVisible = true;
		cancelEnabled = true;
	}

	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		ppSupport.addPropertyChangeListener(listener);
	}

	public void setIgnoreCancel(final boolean ignoreCancel) {
		this.ignoreCancel = ignoreCancel;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(final String title) {
		final String old = this.title;
		this.title = title;
		ppSupport.firePropertyChange(PROPERTY_TITLE, old, title);
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(final String icon) {
		final String old = this.icon;
		this.icon = icon;
		ppSupport.firePropertyChange(PROPERTY_ICON, old, icon);
	}

	/**
	 * @return the progresStartegy
	 */
	public ProgresStrategy getProgresStartegy() {
		return progresStartegy;
	}

	/**
	 * @param progresStartegy
	 *            the progresStartegy to set
	 */
	public void setProgresStartegy(final ProgresStrategy progresStartegy) {
		final ProgresStrategy old = this.progresStartegy;
		this.progresStartegy = progresStartegy;
		ppSupport.firePropertyChange(PROPERTY_PROGRESS_STRATEGY, old, progresStartegy);
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(final String note) {
		final String old = this.note;
		this.note = note;
		ppSupport.firePropertyChange(PROPERTY_NOTE, old, note);

	}

	/**
	 * @return the maxProgress
	 */
	public int getMaxProgress() {
		return maxProgress;
	}

	/**
	 * @param maxProgress
	 *            the maxProgress to set
	 */
	public void setMaxProgress(final int maxProgress) {
		final int old = this.maxProgress;
		this.maxProgress = maxProgress;
		ppSupport.firePropertyChange(PROPERTY_MAX_PROGRESS, old, maxProgress);
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void cancel() {
		if (!ignoreCancel) {
			canceled = true;
		}
		ppSupport.firePropertyChange(PROPERTY_CANCELED, false, true);
	}

	public void setDialogVisible(final boolean visible) {
		final boolean old = this.dialogVisible;
		this.dialogVisible = visible;
		ppSupport.firePropertyChange(PROPERTY_DIALOG_VISIBLE, old, visible);
	}

	public boolean isDialogVisible() {
		return dialogVisible;
	}

	/**
	 * @param cancelEnabled
	 *            the cancelEnabled to set
	 * @since 3.0
	 */
	public void setCancelEnabled(final boolean cancelEnabled) {
		final boolean oldValue = isCancelEnabled();
		this.cancelEnabled = cancelEnabled;
		ppSupport.firePropertyChange(PROPERTY_CANCEL_ENABLED, oldValue, isCancelEnabled());
	}

	/**
	 * @return the cancelEnabled
	 * @since 3.0
	 */
	public boolean isCancelEnabled() {
		return cancelEnabled;
	}

	/**
	 * @param cancelVisible
	 *            the cancelVisible to set
	 * @since 3.0
	 */
	public void setCancelVisible(final boolean cancelVisible) {
		final boolean oldValue = isCancelVisible();
		this.cancelVisible = cancelVisible;
		ppSupport.firePropertyChange(PROPERTY_CANCEL_VISIBLE, oldValue, isCancelVisible());
	}

	/**
	 * @return the cancelVisible
	 * @since 3.0
	 */
	public boolean isCancelVisible() {
		return cancelVisible;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public void setContext(final Object context) {
		final Object old = this.context;
		this.context = context;
		ppSupport.firePropertyChange(PROPERTY_CONTEXT, old, context);
	}

	/**
	 * @return the context
	 */
	public Object getContext() {
		return context;
	}

	/**
	 * You can choose between these two strateguis to describe how progress will
	 * be handled
	 */
	public enum ProgresStrategy {
		UNIT, CUMULATIVE
	}

}
