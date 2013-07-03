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
package org.eclipse.riena.ui.ridgets.swt.optional;

import org.eclipse.nebula.widgets.compositetable.CompositeTable;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Convenience class for creating optional SWT controls. These controls may not
 * be available for platforms other than RCP/SWT. *
 * <p>
 * Will apply consistent style settings and an optional binding id. Can create
 * special instances of certain generic controls, such as numeric or date Text
 * fields.
 * <p>
 * Factory methods are annotated for compatibility with the SWT Designer tool.
 * 
 * @wbp.factory
 * @since 2.0
 */
public final class OptionalUIControlsFactory extends UIControlsFactory {

	private OptionalUIControlsFactory() {
		// static helper class
	}

	/**
	 * Create a {@link CompositeTable} control.
	 * 
	 * @param parent
	 *            The parent composite; never null
	 * @param style
	 *            A combination of style bits that is legal for {@link Canvas}.
	 *            The suggested default is SWT.NONE
	 * 
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.NONE
	 */
	public static CompositeTable createCompositeTable(final Composite parent, final int style) {
		return registerConstruction(new CompositeTable(parent, style));
	}

	/**
	 * Create a {@link Grid} control.
	 * 
	 * @param parent
	 *            The parent composite; never null
	 * @param style
	 *            A combination of style bits that is legal for {@link Canvas}.
	 *            The suggested default is SWT.NONE
	 * @return Grid control
	 * 
	 * @since 4.0
	 */
	public static Grid createGrid(final Composite parent, final int style) {
		return registerConstruction(new Grid(parent, style));
	}

	/**
	 * TODO
	 * 
	 * @since 4.0
	 */
	public static Grid createGrid(final Composite parent, final int style, final String bindingId) {
		return bind(createGrid(parent, style), bindingId);
	}

}