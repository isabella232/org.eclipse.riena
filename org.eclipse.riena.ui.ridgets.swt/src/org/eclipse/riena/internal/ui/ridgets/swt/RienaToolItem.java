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

import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 *
 */
public class RienaToolItem extends ToolItem {

	/**
	 * @param parent
	 * @param style
	 * @param index
	 */
	public RienaToolItem(final ToolBar parent, final int style, final int index) {
		super(parent, style, index);
	}

	/**
	 * @param toolbar
	 * @param separator
	 */
	public RienaToolItem(final ToolBar parent, final int style) {
		super(parent, style);
	}

	@Override
	public void dispose() {
		System.out.println("someone is disposing a rienatoolitem ");
		super.dispose();
	}

	public void disposeToolItem() {
		super.dispose();
	}

	/**
	 * allow subclassing.
	 */
	@Override
	protected void checkSubclass() {
	}

}
