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
package org.eclipse.riena.internal.ui.ridgets.swt;

import static org.eclipse.riena.ui.swt.utils.SwtUtilities.*;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.internal.databinding.property.value.SimplePropertyObservableValue;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.internal.databinding.swt.ButtonSelectionProperty;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractToggleButtonRidget;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 * Adapter of the SWT Widget <code>Button</code> with the style SWT.CHECK,
 * SWT.RADIO or SWT.TOGGLE.
 */
// Note: TBR and ATBR could be merged - TBR is the only subclass
public class ToggleButtonRidget extends AbstractToggleButtonRidget {

	/**
	 * ToggleButtonRidgets use this property to store a reference to themselves
	 * in their assigned control.
	 */
	private static final String TOGGLE_BUTTON_RIDGET = "tbr"; //$NON-NLS-1$

	private SelectionObservableWithOutputOnly selectionObservable;

	@Override
	protected void bindUIControl() {
		super.bindUIControl();
		final Button control = getUIControl();
		if (control != null) {
			control.setData(TOGGLE_BUTTON_RIDGET, this);
			updateMandatoryMarkers();
		}
	}

	@Override
	protected void checkUIControl(final Object uiControl) {
		checkType(uiControl, Button.class);
		if (uiControl != null) {
			final Button button = (Button) uiControl;
			if (!(hasStyle(button, SWT.CHECK) || hasStyle(button, SWT.RADIO) || hasStyle(button, SWT.TOGGLE))) {
				throw new BindingException("Button must be a check box, a radio button or a toggle button"); //$NON-NLS-1$
			}
		}
	}

	@Override
	public Button getUIControl() {
		return (Button) super.getUIControl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.swt.AbstractToggleButtonRidget#propertyEnabledChanged(java.beans.PropertyChangeEvent)
	 */
	@Override
	protected void propertyEnabledChanged(final PropertyChangeEvent evt) {
		super.propertyEnabledChanged(evt);
		
		// if we just got enabled, we have to "invalidate" the cached value in our observable
		final boolean nu = evt.getNewValue() != null ? (Boolean) evt.getNewValue() : false;
		final boolean old = evt.getNewValue() != null ? (Boolean) evt.getOldValue() : false;
		if (nu && !old && selectionObservable != null) {
			selectionObservable.resetCachedValue();
		}
	}

	@Override
	protected IObservableValue getUIControlSelectionObservable() {
		if (selectionObservable == null) {
			selectionObservable = new SelectionObservableWithOutputOnly(getUIControl());
		}
		return selectionObservable;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The disabled state for mandatory markers on this ridget is computed as
	 * follows:
	 * <ul>
	 * <li>if this ridget is selected, return true</li>
	 * <li>if an enabled sibling control (i.e. a Button of the same type &ndash;
	 * where type is check, radio or toggle &ndash; located in the same parent
	 * Composite) is checked, return true</li>
	 * <li>otherwise, return false</li>
	 * </ul>
	 * <p>
	 * Correspondingly, this method only returns a fully accurate result when
	 * this ridget has a bound UI-control. Without a UI-control is it not
	 * possible to look at the siblings when computing the result.
	 * 
	 * @return true if mandatory markers should be disabled, false otherwise
	 */
	@Override
	public boolean isDisableMandatoryMarker() {
		boolean isSelected = isSelected();
		if (!isSelected && getUIControl() != null) {
			isSelected = siblingsAreSelected();
		}
		return isSelected;
	}

	@Override
	protected void unbindUIControl() {
		if (selectionObservable != null) {
			selectionObservable.dispose();
			selectionObservable = null;
		}
		super.unbindUIControl();
	}

	@Override
	protected void setUIControlSelection(final boolean selected) {
		getUIControl().setSelection(selected);
	}

	@Override
	protected String getUIControlText() {
		return getUIControl().getText();
	}

	@Override
	protected void setUIControlText(final String text) {
		getUIControl().setText(text);
	}

	@Override
	protected void setUIControlImage(final Image image) {
		getUIControl().setImage(image);
	}

	@Override
	public void setSelected(final boolean selected) {
		final Button selectedUIControl = getUIControl();
		if (!isSelected() && selected && isRadioSingleSelection(selectedUIControl)) {
			for (final Button sibling : getSiblings(selectedUIControl)) {
				final String bindingId = SWTBindingPropertyLocator.getInstance().locateBindingProperty(sibling);
				final IToggleButtonRidget siblingRidget = getController().getRidget(IToggleButtonRidget.class,
						bindingId);
				siblingRidget.setSelected(false);
			}
		}
		super.setSelected(selected);
	}

	@Override
	protected void updateMandatoryMarkers() {
		final boolean disableMarker = isDisableMandatoryMarker();
		final Button control = getUIControl();
		if (control != null) {
			final Button[] siblings = getSiblings(control);
			for (final Button sibling : siblings) {
				final Object ridget = sibling.getData(TOGGLE_BUTTON_RIDGET);
				if (ridget instanceof ToggleButtonRidget) {
					((ToggleButtonRidget) ridget).disableMandatoryMarkers(disableMarker);
				}
			}
		}
		disableMandatoryMarkers(disableMarker);
	}

	// helping methods
	//////////////////

	private Button[] getSiblings(final Button control) {
		final List<Button> result = new ArrayList<Button>();
		final boolean isCheck = isCheck(control);
		final boolean isRadio = isRadio(control);
		final boolean isPush = isToggle(control);
		final Control[] siblings = control.getParent().getChildren();
		for (final Control candidate : siblings) {
			if (candidate != control && candidate instanceof Button) {
				if ((isCheck && isCheck(candidate)) || (isRadio && isRadio(candidate))
						|| (isPush && isToggle(candidate))) {
					result.add((Button) candidate);
				}
			}
		}
		return result.toArray(new Button[result.size()]);
	}

	private boolean isCheck(final Control control) {
		return control != null && (control.getStyle() & SWT.CHECK) > 0;
	}

	private boolean isRadio(final Control control) {
		return control != null && (control.getStyle() & SWT.RADIO) > 0;
	}

	private boolean isRadioSingleSelection(final Control control) {
		return isRadio(control) && (control.getStyle() & SWT.NO_RADIO_GROUP) == 0;
	}

	private boolean isToggle(final Control control) {
		return control != null && (control.getStyle() & SWT.TOGGLE) > 0;
	}

	private boolean siblingsAreSelected() {
		boolean result = false;
		final Button control = getUIControl();
		final Button[] siblings = getSiblings(control);
		for (int i = 0; !result && i < siblings.length; i++) {
			final Button sibling = siblings[i];
			if (sibling.isEnabled()) {
				result = sibling.getSelection();
			}
		}
		return result;
	}

	// helping classes
	//////////////////

	/**
	 * Custom IObservableValue that will revert selection changes when the
	 * ridget is output-only.
	 * 
	 * @see http://bugs.eclipse.org/271762
	 * @see http://bugs.eclipse.org/321935
	 */
	private final class SelectionObservableWithOutputOnly extends SimplePropertyObservableValue implements
			SelectionListener {

		private final Button button;

		@SuppressWarnings("restriction")
		public SelectionObservableWithOutputOnly(final Button source) {
			super(getValueBindingSupport().getContext().getValidationRealm(), source, new ButtonSelectionProperty());
			Assert.isNotNull(source);
			this.button = source;
			this.button.addSelectionListener(this);
		}

		/**
		 * Resets the superclass field "cachedValue". Alternatively, it would be sufficient to set the field to <code>null</code> by reflection, but we want to avoid such a hack.
		 * 
		 * @see SimplePropertyObservableValue
		 * @see ReflectionUtils#setHidden(Object, String, Object)
		 */
		private void resetCachedValue() {
			super.doGetValue();
		}

		@Override
		protected Object doGetValue() {
			return isOutputOnly() ? Boolean.valueOf(isSelected()) : super.doGetValue();
		}

		public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
			if (isOutputOnly()) {
				button.setSelection(isSelected());
			}
		}

		public void widgetDefaultSelected(final org.eclipse.swt.events.SelectionEvent e) {
			// unused
		}

		@Override
		public synchronized void dispose() {
			if (!button.isDisposed()) {
				button.removeSelectionListener(this);
			}
		}

	}
}
