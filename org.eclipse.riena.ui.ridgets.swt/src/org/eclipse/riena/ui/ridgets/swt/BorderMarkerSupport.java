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

import java.beans.PropertyChangeSupport;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.ui.ridgets.IBasicMarkableRidget;
import org.eclipse.riena.ui.ridgets.IControlDecoration;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.swt.BorderControlDecoration;
import org.eclipse.riena.ui.swt.IDecorationActivationStrategy;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;

/**
 * This class decorates a UI control that has an {@code ErrorMarker} with a border around the UI control.
 * 
 * @since 2.0
 */
public class BorderMarkerSupport extends MarkerSupport {

	/**
	 * Creates a new instance of {@code BorderMarkerSupport}.
	 */
	public BorderMarkerSupport() {
		super();
	}

	/**
	 * Creates a new instance of {@code BorderMarkerSupport}.
	 * 
	 * @param ridget
	 *            the Ridget whose markers should be visualized
	 * @param propertyChangeSupport
	 *            this will be informed if a marker has changed
	 */
	public BorderMarkerSupport(final IBasicMarkableRidget ridget, final PropertyChangeSupport propertyChangeSupport) {
		super(ridget, propertyChangeSupport);
	}

	/**
	 * Creates a decoration with an error marker for the given control.
	 * <p>
	 * The decoration draws a border a round the UI control.
	 * 
	 * @param control
	 *            the control to be decorated with an error marker
	 * @return decoration of the given control
	 */
	@Override
	protected IControlDecoration createErrorDecoration(final Control control) {
		final RienaDefaultLnf lnf = LnfManager.getLnf();
		final int width = lnf.getIntegerSetting(LnfKeyConstants.ERROR_MARKER_BORDER_WIDTH, 1);
		final Color borderColor = lnf.getColor(LnfKeyConstants.ERROR_MARKER_BORDER_COLOR);
		final boolean useVisibleControlArea = getRidget() != null ? getRidget().decorateVisibleControlArea() : false;
		return new BorderControlDecoration(control, width, borderColor, useVisibleControlArea, new IDecorationActivationStrategy() {
			public boolean isActive() {
				final Object data = control.getData(IRidget.class.getName());
				return data instanceof IRidget && ((IRidget) data).getUIControl() != null;
			}
		});
	}

}
