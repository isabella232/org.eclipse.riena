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
package org.eclipse.riena.ui.ridgets.swt;

import java.beans.PropertyChangeSupport;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;

import org.eclipse.riena.internal.ui.ridgets.swt.DisabledMarkerVisualizer;
import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.IBasicMarkableRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Helper class for Ridgets to delegate their marker issues to that just handles
 * the basic markers for uneditable Ridgets: the HiddenMarker to toggle
 * visibility and the DisabledMarker that toggles the enabled state.
 * 
 * @since 3.0
 */
public class BasicMarkerSupport extends AbstractMarkerSupport {

	private static boolean alwaysSkipRedraw = false;
	private static boolean osChecked = false;
	private boolean initialEnabled = true;
	private boolean initialVisible;

	/**
	 * @since 3.0
	 */
	protected DisabledMarkerVisualizer disabledMarkerVisualizer;

	public BasicMarkerSupport() {
		super();
		initialVisible = true;
	}

	public BasicMarkerSupport(final IBasicMarkableRidget ridget, final PropertyChangeSupport propertyChangeSupport) {
		super(ridget, propertyChangeSupport);
		initialVisible = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(final IBasicMarkableRidget ridget, final PropertyChangeSupport propertyChangeSupport) {
		super.init(ridget, propertyChangeSupport);

		final Control control = getUIControl();
		if (!SwtUtilities.isDisposed(control)) {
			control.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(final DisposeEvent e) {
					final Control control = getUIControl();
					if (e.widget == control) {
						// workaround for Bug 304869
						if (!(control instanceof CCombo)) {
							clearAllMarkers(control);
						}
					}
				}
			});
		}

		// workaround for 272863
		if (!osChecked) {
			osChecked = true;
			final String osname = System.getProperty("org.osgi.framework.os.name"); //$NON-NLS-1$
			if (osname != null && osname.equalsIgnoreCase("macosx")) { //$NON-NLS-1$
				alwaysSkipRedraw = true;
			}
		}

		createDisabledMarkerVisualizer();
	}

	@Override
	public void updateMarkers() {
		updateUIControl();
	}

	// protected methods
	////////////////////

	/**
	 * Does nothing. Subclasses may override.
	 * 
	 * @param control
	 *            the control
	 */
	protected void clearAllMarkers(final Control control) {
		clearDisabled(control);
		clearVisible(control);
	}

	private void clearDisabled(final Control control) {
		disabledMarkerVisualizer.updateDisabled(control, initialEnabled);
	}

	private void clearVisible(final Control control) {
		control.setVisible(initialVisible);
	}

	/**
	 * @since 3.0
	 */
	@Override
	public void bind() {
		if (getUIControl() != null) {
			//save initial state of control
			saveState();
		}
	}

	/**
	 * @since 3.0
	 */
	@Override
	public void unbind() {
		if (getUIControl() != null) {
			//restore initial state of control
			clearAllMarkers(getUIControl());
		}
	}

	/**
	 * @since 3.0
	 */
	protected void saveState() {
		this.initialEnabled = getUIControl().getEnabled();
		this.initialVisible = getUIControl().getVisible();
	}

	/**
	 * @since 3.0
	 */
	public boolean isInitialEnabled() {
		return initialEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control getUIControl() {
		return (Control) super.getUIControl();
	}

	@Override
	protected void handleMarkerAttributesChanged() {
		updateUIControl();
		super.handleMarkerAttributesChanged();
	}

	protected void updateUIControl(final Control control) {
		updateVisible(control);
		updateDisabled(control);
	}

	protected void updateVisible(final Control control) {
		control.setVisible(!hasHiddenMarkers());
	}

	protected void updateDisabled(final Control control) {
		final boolean on = LnfManager.getLnf().getBooleanSetting(LnfKeyConstants.DISABLED_MARKER_ADVANCED);
		if (on) {
			// delegate to the visualizer for rendering
			getDisabledMarkerVisualizer().updateDisabled();
		} else {
			control.setEnabled(getRidget().isEnabled());
		}
	}

	// helping methods
	//////////////////

	private void createDisabledMarkerVisualizer() {
		this.disabledMarkerVisualizer = new DisabledMarkerVisualizer(getRidget());
	}

	private DisabledMarkerVisualizer getDisabledMarkerVisualizer() {
		return disabledMarkerVisualizer;
	}

	private void startRedraw(final Control control) {
		if (!skipRedrawForBug258176(control)) {
			control.setRedraw(true);
		}
		control.redraw();
	}

	private void stopRedraw(final Control control) {
		if (!skipRedrawForBug258176(control)) {
			control.setRedraw(false);
		}
	}

	/**
	 * These controls are affected by bug 258176 in SWT.
	 */
	private boolean skipRedrawForBug258176(final Control control) {
		if (alwaysSkipRedraw) {
			return true;
		}
		return (control instanceof Combo) || (control instanceof Table) || (control instanceof List);
	}

	private void updateUIControl() {
		final Control control = getUIControl();
		if (control != null) {
			stopRedraw(control);
			try {
				updateUIControl(control);
			} finally {
				startRedraw(control);
			}
		}
	}

}
