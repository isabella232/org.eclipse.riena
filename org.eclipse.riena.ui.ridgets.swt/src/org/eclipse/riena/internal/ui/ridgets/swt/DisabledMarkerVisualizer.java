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

import java.util.EventListener;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.MarkerSupport;
import org.eclipse.riena.ui.swt.facades.SWTFacade;

/**
 * Responsible for Rendering the disabled state of any control.
 */
public class DisabledMarkerVisualizer {

	// the painter
	private static final EventListener DISABLED_MARKER_PAINTER = SWTFacade.getDefault().createDisabledPainter();
	// true if running in RCP
	private static boolean isRCP = SWTFacade.isRCP();
	// the ridget
	private final IRidget ridget;
	private static IRenderDisabledStateWidget[] renderDisabledStateWidgets;

	public DisabledMarkerVisualizer(final IRidget ridget) {
		this.ridget = ridget;
		Wire.instance(this).andStart();
	}

	/**
	 * This is the entry point for {@link MarkerSupport}
	 */
	public void updateDisabled() {
		final Control control = getControl();
		if (control == null) {
			return;
		}
		final boolean enabled = getRidget().isEnabled();
		updateDisabled(control, enabled);
	}

	/**
	 * Control connect and disconnect the Paintlistener.
	 */
	protected void removePaintlistener(final Control control) {
		final SWTFacade facade = SWTFacade.getDefault();
		facade.removePaintListener(control, DISABLED_MARKER_PAINTER);
	}

	protected void addPaintlistener(final Control control) {
		final SWTFacade facade = SWTFacade.getDefault();
		facade.addPaintListener(control, DISABLED_MARKER_PAINTER);
	}

	// helping methods
	//////////////////

	private Control getControl() {
		return (Control) getRidget().getUIControl();
	}

	private IRidget getRidget() {
		return ridget;
	}

	public void updateDisabled(final Control control, final boolean enabled) {
		// Bug 322030: only change the enablement of the control bound to the 
		// ridget. The enabled state of the controls within this control (if  
		// a composite) can depend on internal state not available here.
		// Examples: DatePickerComposite, CompletionCombo, CCombo
		control.setEnabled(enabled);
		updatePaintListener(control, enabled);
		control.redraw();
	}

	private void updatePaintListener(final Control control, final boolean enabled) {

		if (dontAddDisabledPainter(control)) {
			return;
		}

		removePaintlistener(control);

		if (!enabled) {
			addPaintlistener(control);
		}

		if (control instanceof Composite) {
			final Composite composite = (Composite) control;
			final Control[] children = getChildren(composite);
			for (final Control child : children) {
				updatePaintListener(child, enabled);
			}
		}
	}

	/**
	 * Checks if the given control/widget renders the disabled state by its own.
	 * 
	 * @param control
	 *            control to check
	 * @return {@code true} don't render disabled state with
	 *         {@link DisabledMarkerVisualizer}, {@code false} use
	 *         {@link DisabledMarkerVisualizer} to render disabled state
	 */
	protected boolean dontAddDisabledPainter(final Control control) {
		if (renderDisabledStateWidgets != null) {
			for (final IRenderDisabledStateWidget renderDisabledStateWidget : renderDisabledStateWidgets) {
				if (control.getClass().equals(renderDisabledStateWidget.getWidgetClass())) {
					return true;
				}
			}
		}
		return false;
	}

	private Control[] getChildren(final Composite parent) {
		Control[] result = parent.getChildren();
		if (isRCP && parent instanceof CCombo) {
			// workaround for Bug 318301
			result = new Control[2];
			result[0] = ReflectionUtils.getHidden(parent, "text"); //$NON-NLS-1$
			result[1] = ReflectionUtils.getHidden(parent, "arrow"); //$NON-NLS-1$
		}
		return result;
	}

	@InjectExtension
	public static void update(final IRenderDisabledStateWidget[] renderDisabledStateWidgets) {
		DisabledMarkerVisualizer.renderDisabledStateWidgets = renderDisabledStateWidgets;
	}

}
