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

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Item;
import org.eclipse.ui.menus.CommandContributionItem;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.core.resource.IIconManager;
import org.eclipse.riena.ui.core.resource.IconManagerProvider;
import org.eclipse.riena.ui.core.resource.IconSize;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.listener.IClickListener;
import org.eclipse.riena.ui.ridgets.swt.AbstractSWTWidgetRidget;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Ridget of an item (e.g. menu item).
 */
public abstract class AbstractItemRidget extends AbstractSWTWidgetRidget implements IActionRidget {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private Item item;
	private String text;
	private String iconID;
	private final ActionObserver actionObserver;
	private boolean textAlreadyInitialized;
	private boolean useRidgetIcon;
	private AbstractItemProperties itemProperties;
	private String itemId;

	/**
	 * Creates a new instance of {@link AbstractItemRidget}.
	 */
	public AbstractItemRidget() {
		actionObserver = new ActionObserver(this);
		textAlreadyInitialized = false;
		useRidgetIcon = false;
	}

	/**
	 * Creates a class that stores all properties of the given item.
	 * 
	 * @return item properties
	 */
	abstract AbstractItemProperties createProperties();

	@Override
	protected void bindUIControl() {
		final Item control = getUIControl();
		if (control != null) {
			item = control;
			itemId = super.getID();
			initText();
			updateUIText();
			updateUIIcon();
			setItemProperties(createProperties());
		}
	}

	@Override
	public void setUIControl(final Object uiControl) {
		super.setUIControl(uiControl);
	}

	@Override
	public final String getID() {
		String idString = super.getID();
		if (StringUtils.isEmpty(idString)) {
			idString = itemId;
		}
		return idString;
	}

	/**
	 * If the text of the ridget has no value, initialize it with the text of the UI control.
	 */
	private void initText() {
		if ((text == null) && (!textAlreadyInitialized)) {
			if ((getUIControl()) != null && !(getUIControl().isDisposed())) {
				text = getUIControl().getText();
				if (text == null) {
					text = EMPTY_STRING;
				}
				textAlreadyInitialized = true;
			}
		}
	}

	@Override
	protected void unbindUIControl() {
		item = null;
	}

	@Override
	public Item getUIControl() {
		return (Item) super.getUIControl();
	}

	public final void addListener(final IActionListener listener) {
		getActionObserver().addListener(listener);
	}

	public final void removeListener(final IActionListener listener) {
		getActionObserver().removeListener(listener);
	}

	/**
	 * Always returns true because mandatory markers do not make sense for this ridget.
	 */
	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
	}

	public String getIcon() {
		return this.iconID;
	}

	public void setIcon(final String icon) {
		setIcon(icon, IconSize.NONE);
	}

	public void setIcon(final String icon, final IconSize size) {
		final boolean oldUseRidgetIcon = useRidgetIcon;
		useRidgetIcon = true;
		final String oldIconID = this.iconID;
		final IIconManager manager = IconManagerProvider.getInstance().getIconManager();
		this.iconID = manager.getIconID(icon, size);
		if (hasChanged(oldIconID, iconID) || !oldUseRidgetIcon) {
			updateUIIcon();
		}
	}

	public final String getText() {
		return text;
	}

	public final void setText(final String newText) {
		final String oldText = this.text;
		this.text = newText;
		updateUIText();
		firePropertyChange(IActionRidget.PROPERTY_TEXT, oldText, this.text);
	}

	/**
	 * Returns form the data of the item the {@link CommandContributionItem}.
	 * 
	 * @return the {@code CommandContributionItem} or <{@code null} if the item has no {@code CommandContributionItem}.
	 */
	protected CommandContributionItem getContributionItem() {

		final Item uiItem = getUIControl();
		if (SwtUtilities.isDisposed(uiItem)) {
			return null;
		}

		if ((uiItem.getData() instanceof CommandContributionItem)) {
			return (CommandContributionItem) uiItem.getData();
		} else {
			return null;
		}

	}

	public boolean isVisible() {
		// check for "hidden.marker". This marker overrules any other visibility rule
		if (!getMarkersOfType(HiddenMarker.class).isEmpty()) {
			return false;
		}

		if (getUIControl() != null) {
			return !getUIControl().isDisposed();
		}
		// control is not bound
		return savedVisibleState;
	}

	@Override
	public boolean isEnabled() {

		boolean enabled = super.isEnabled();
		final CommandContributionItem commandItem = getContributionItem();
		if (commandItem != null) {
			enabled = enabled && commandItem.isEnabled();
		}
		return enabled;

	}

	// helping methods
	// ////////////////

	/**
	 * Updates (sets) the text of the menu item.
	 */
	private void updateUIText() {
		if (item != null) {
			item.setText(text);
		}
	}

	/**
	 * Updates (sets) the icon of the item.
	 * 
	 * @return {@code true} if image of control was set; otherwise {@code false} .
	 */
	protected boolean updateUIIcon() {
		final Item control = getUIControl();
		if (control != null) {
			Image image = null;
			if (getIcon() != null) {
				image = getManagedImage(getIcon());
			}
			if ((image != null) || useRidgetIcon) {
				control.setImage(image);
				return true;
			}
		}
		return false;
	}

	/**
	 * Does nothing; item's don't have a tooltip.
	 */
	@Override
	protected void updateToolTip() {
		// does nothing
	}

	protected ActionObserver getActionObserver() {
		return actionObserver;
	}

	private void setItemProperties(final AbstractItemProperties itemProperties) {
		this.itemProperties = itemProperties;
	}

	AbstractItemProperties getItemProperties() {
		return itemProperties;
	}

	/**
	 * Creates a new item on base of the stored item properties.
	 */
	void createItem() {
		final Item uiItem = getUIControl();
		if (SwtUtilities.isDisposed(uiItem)) {
			getItemProperties().createItem();
		}
	}

	public void fireAction() {
		actionObserver.widgetSelected(null);
	}

	@Override
	public void addClickListener(final IClickListener listener) {
		throw new UnsupportedOperationException("not supported"); //$NON-NLS-1$
	}

	@Override
	public void removeClickListener(final IClickListener listener) {
		throw new UnsupportedOperationException("not supported"); //$NON-NLS-1$
	}

}
