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

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;

import org.eclipse.riena.core.util.ListenerList;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IDefaultActionManager;
import org.eclipse.riena.ui.ridgets.IEmbeddedTitleBarRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.listener.IWindowRidgetListener;
import org.eclipse.riena.ui.ridgets.swt.AbstractSWTRidget;
import org.eclipse.riena.ui.ridgets.swt.BasicMarkerSupport;
import org.eclipse.riena.ui.swt.EmbeddedTitleBar;
import org.eclipse.riena.ui.swt.IEmbeddedTitleBarListener;

/**
 * Ridget for {@link EmbeddedTitleBar}.
 */
public class EmbeddedTitleBarRidget extends AbstractSWTRidget implements IEmbeddedTitleBarRidget {
	private String text = StringUtils.EMPTY_STRING;
	private String icon;
	private final ListenerList<IWindowRidgetListener> windowRidgetListeners;
	private boolean closeable;
	private boolean active;
	private final IEmbeddedTitleBarListener titleBarListener;
	private DefaultActionManager actionManager;
	private boolean blocked;

	/**
	 * Creates a new instance of {@code EmbeddedTitleBarRidget}.
	 */
	public EmbeddedTitleBarRidget() {
		closeable = false;
		active = true;
		windowRidgetListeners = new ListenerList<IWindowRidgetListener>(IWindowRidgetListener.class);
		titleBarListener = new TitleBarListener();
	}

	/**
	 * Creates a new instance of {@code EmbeddedTitleBarRidget}.
	 * 
	 * @param window
	 *            UI Control
	 */
	public EmbeddedTitleBarRidget(final EmbeddedTitleBar window) {
		this();
		setUIControl(window);
	}

	@Override
	protected AbstractMarkerSupport createMarkerSupport() {
		return new BasicMarkerSupport(this, propertyChangeSupport);
	}

	@Override
	public EmbeddedTitleBar getUIControl() {
		return (EmbeddedTitleBar) super.getUIControl();
	}

	public void setTitle(final String title) {
		if (StringUtils.equals(this.text, title)) {
			return;
		}
		final String oldValue = this.text;
		this.text = title;
		updateTextInControl();
		firePropertyChange(ILabelRidget.PROPERTY_TEXT, oldValue, this.text);
	}

	/**
	 * Returns the title of the title bar.
	 * 
	 * @return text of title
	 */
	public String getTitle() {
		return text;
	}

	public void setIcon(final String icon) {
		final String oldIcon = this.icon;
		this.icon = icon;
		if (hasChanged(oldIcon, icon)) {
			updateIconInControl();
		}
	}

	/**
	 * Returns the icon of the title bar.
	 * 
	 * @return icon
	 */
	public String getIcon() {
		return icon;
	}

	@Override
	protected void bindUIControl() {
		updateTextInControl();
		updateToolTipTextInControl();
		if (getUIControl() != null) {
			getUIControl().addEmbeddedTitleBarListener(titleBarListener);
		}
	}

	@Override
	protected void checkUIControl(final Object uiControl) {
		checkType(uiControl, EmbeddedTitleBar.class);
	}

	@Override
	protected void unbindUIControl() {
		super.unbindUIControl();
		if (getUIControl() != null) {
			getUIControl().removeEmbeddedTitleBarListener(titleBarListener);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Implementation note:</b> activation, deactivation and disposal of the returned {@link IDefaultActionManager} must be handled by the controller.
	 */
	public IDefaultActionManager addDefaultAction(final IRidget focusRidget, final IActionRidget actionRidget) {
		if (actionManager == null) {
			actionManager = new DefaultActionManager(this);
		}
		actionManager.addAction(focusRidget, actionRidget);
		return actionManager;
	}

	public void addWindowRidgetListener(final IWindowRidgetListener listener) {
		windowRidgetListeners.add(listener);
	}

	public void dispose() {
		getUIControl().dispose();
		if (actionManager != null) {
			actionManager.dispose();
			actionManager = null;
		}
	}

	/**
	 * Always returns true because mandatory markers do not make sense for this ridget.
	 */
	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
	}

	public void layout() {
		final EmbeddedTitleBar control = getUIControl();
		if (control != null) {
			// we want to layout the whole view, which is the parent of the EmbeddedTitleBar
			control.getParent().layout(true, true);
		}
	}

	public void removeWindowRidgetListener(final IWindowRidgetListener listener) {
		windowRidgetListeners.remove(listener);
	}

	public void setActive(final boolean active) {
		if (this.active != active) {
			this.active = active;
			updateActive();
		}
	}

	public void setCloseable(final boolean closeable) {
		if (this.closeable != closeable) {
			this.closeable = closeable;
			updateCloseable();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IWindowRidget#setBlocked(boolean)
	 */
	public void setBlocked(final boolean blocked) {
		if (this.blocked != blocked) {
			this.blocked = blocked;
			updateBlocked();
		}
	}

	/**
	 * Updates the text of the UI control.
	 */
	private void updateTextInControl() {
		final EmbeddedTitleBar control = getUIControl();
		if (control != null) {
			control.setTitle(this.text);
		}
	}

	/**
	 * Updates the text of the UI control.
	 */
	private void updateToolTipTextInControl() {
		final EmbeddedTitleBar control = getUIControl();
		if (control != null) {
			control.setToolTipText(getToolTipText());
		}
	}

	/**
	 * Updates the icon of the UI control.
	 */
	private void updateIconInControl() {
		final EmbeddedTitleBar control = getUIControl();
		if (control != null) {
			Image image = null;
			if (icon != null) {
				image = getManagedImage(icon);
			}
			// if (image == getMissingImage()) {
			// image = null;
			// }
			control.setImage(image);
		}
	}

	private void updateCloseable() {
		final EmbeddedTitleBar control = getUIControl();
		if (control != null) {
			control.setCloseable(closeable);
		}
	}

	private void updateActive() {
		final EmbeddedTitleBar control = getUIControl();
		if (control != null) {
			control.setWindowActive(active);
		}
	}

	private void updateBlocked() {
		final EmbeddedTitleBar control = getUIControl();
		if (control != null) {
			control.setBlocked(blocked);
		}
	}

	/**
	 * Listener of the title bar.
	 */
	private class TitleBarListener implements IEmbeddedTitleBarListener {
		public void windowActivated(final MouseEvent e) {
			for (final IWindowRidgetListener listener : windowRidgetListeners.getListeners()) {
				listener.activated();
			}
		}

		public void windowClosed(final MouseEvent e) {
			for (final IWindowRidgetListener listener : windowRidgetListeners.getListeners()) {
				listener.closed();
			}
		}
	}
}
