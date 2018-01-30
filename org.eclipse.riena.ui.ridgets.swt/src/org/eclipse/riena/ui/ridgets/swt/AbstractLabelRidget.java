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
package org.eclipse.riena.ui.ridgets.swt;

import java.net.URL;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.ui.core.resource.IIconManager;
import org.eclipse.riena.ui.core.resource.IconManagerProvider;
import org.eclipse.riena.ui.core.resource.IconSize;
import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.ILabelRidget;

/**
 * Superclass of LabelRidget that does not depend on the Label SWT control. May be reused for custom Label controls.
 */
public abstract class AbstractLabelRidget extends AbstractValueRidget implements ILabelRidget {

	/**
	 * This property is used by the databinding to sync ridget and model. It is always fired before its sibling {@link ILabelRidget#PROPERTY_TEXT} to ensure
	 * that the model is updated before any listeners try accessing it.
	 * <p>
	 * This property is not API. Do not use in client code.
	 */
	private static final String PROPERTY_TEXT_INTERNAL = "textInternal"; //$NON-NLS-1$

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$
	private String text;
	private String iconID;
	private IconSize iconSize;
	private URL iconLocation;
	private boolean textAlreadyInitialized;
	private boolean useRidgetIcon;

	public AbstractLabelRidget() {
		textAlreadyInitialized = false;
		useRidgetIcon = false;
	}

	@Override
	protected AbstractMarkerSupport createMarkerSupport() {
		return new BasicMarkerSupport(this, propertyChangeSupport);
	}

	@Override
	protected IObservableValue getRidgetObservable() {
		return BeansObservables.observeValue(this, PROPERTY_TEXT_INTERNAL);
	}

	@Override
	protected void bindUIControl() {
		initText();
		updateUIText();
		updateUIIcon();
	}

	/**
	 * If the text of the ridget has no value, initialize it with the text of the UI control.
	 */
	private void initText() {
		if (text == null && !textAlreadyInitialized) {
			final Control control = getUIControl();
			if (control != null && !control.isDisposed()) {
				text = getUIControlText();
				if (text == null) {
					text = EMPTY_STRING;
				}
				textAlreadyInitialized = true;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The <i>full</i> name of the icon is returned, also called icon ID. The icon ID (can) contains the name, the size and the state.
	 */
	public String getIcon() {
		return iconID;
	}

	public URL getIconLocation() {
		return iconLocation;
	}

	public String getText() {
		return text;
	}

	/**
	 * This method is not API. Do not use in client code.
	 * 
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public final String getTextInternal() {
		return getText();
	}

	/**
	 * Always returns true because mandatory markers do not make sense for this ridget.
	 */
	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <i>Also sets the size {@code IconSize.NONE} for the icon.</i>
	 * 
	 * @see #setIcon(String,IconSize)
	 */
	public void setIcon(final String icon) {
		setIcon(icon, IconSize.NONE);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The name and the size of the icon will be managed by an implementation of {@code IIconManager}.
	 * 
	 * @since 2.0
	 */
	public void setIcon(final String icon, final IconSize size) {
		final boolean oldUseRidgetIcon = useRidgetIcon;
		useRidgetIcon = true;
		final String oldIcon = this.iconID;
		final IIconManager manager = IconManagerProvider.getInstance().getIconManager();
		this.iconID = manager.getIconID(icon, size);
		this.iconSize = size;
		if (hasChanged(oldIcon, icon) || !oldUseRidgetIcon) {
			updateUIIcon();
		}
	}

	public void setIconLocation(final URL location) {
		useRidgetIcon = true;
		final URL oldUrl = this.iconLocation;
		this.iconLocation = location;
		if (hasChanged(oldUrl, location)) {
			updateUIIcon();
		}
	}

	public void setText(final String text) {
		final String oldValue = this.text;
		this.text = text;
		updateUIText();
		firePropertyChange(PROPERTY_TEXT_INTERNAL, oldValue, this.text);
		firePropertyChange(ILabelRidget.PROPERTY_TEXT, oldValue, this.text);
	}

	/**
	 * This method is not API. Do not use in client code.
	 * 
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public final void setTextInternal(final String text) {
		setText(text);
	}

	// helping methods
	// ////////////////

	/**
	 * @return The controls text.
	 */
	protected abstract String getUIControlText();

	private void updateUIText() {
		if (getUIControl() != null) {
			setUIControlText(text);
		}
	}

	/**
	 * Sets the controls text.
	 */
	protected abstract void setUIControlText(String text);

	private void updateUIIcon() {
		if (getUIControl() != null) {
			Image image = null;
			if (getIcon() != null) {
				image = getManagedImage(getIcon(), iconSize);
			} else if (iconLocation != null) {
				final String key = iconLocation.toExternalForm();
				image = getManagedImage(key);
			}
			if ((image != null) || useRidgetIcon) {
				setUIControlImage(image);
			}
		}
	}

	/**
	 * Sets the controls image.
	 */
	protected abstract void setUIControlImage(Image image);

	private boolean hasChanged(final URL oldValue, final URL newValue) {
		if (oldValue == null && newValue == null) {
			return false;
		}
		if (oldValue == null || newValue == null) {
			return true;
		}

		// avoid URL.equals(...) since it opens a network connection :(
		final String str1 = oldValue.toExternalForm();
		final String str2 = newValue.toExternalForm();
		return !str1.equals(str2);
	}

}
