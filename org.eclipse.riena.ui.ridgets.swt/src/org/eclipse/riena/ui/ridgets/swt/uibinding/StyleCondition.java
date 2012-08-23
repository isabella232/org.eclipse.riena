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
package org.eclipse.riena.ui.ridgets.swt.uibinding;

import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.ui.ridgets.uibinding.IMappingCondition;

public final class StyleCondition implements IMappingCondition {

	private final int controlStyle;

	public StyleCondition(final int style) {
		controlStyle = style;
	}

	public boolean isMatch(final Object control) {
		if (control instanceof Widget) {
			if ((((Widget) control).getStyle() & getControlStyle()) == getControlStyle()) {
				return true;
			}
		}
		return false;
	}

	public int getControlStyle() {
		return controlStyle;
	}

}