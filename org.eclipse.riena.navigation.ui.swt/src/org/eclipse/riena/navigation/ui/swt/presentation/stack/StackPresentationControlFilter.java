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
package org.eclipse.riena.navigation.ui.swt.presentation.stack;

import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.EmbeddedTitleBar;

/**
 * Instances of this class define a filter strategy for {@link Control}s
 * 
 * @since 3.0
 */
public abstract class StackPresentationControlFilter {

	/**
	 * Identifies instances of {@link EmbeddedTitleBar}
	 */
	public final static StackPresentationControlFilter TITLE_BAR_FILTER = new StackPresentationControlFilter() {

		@Override
		public boolean accept(final Control control) {
			return control instanceof EmbeddedTitleBar;
		}
	};

	/**
	 * Identifies the content composite of {@link SubModuleView}s
	 */
	public final static StackPresentationControlFilter CONTENT_COMPOSITE_FILTER = new StackPresentationControlFilter() {

		@Override
		public boolean accept(final Control control) {
			return control.getData(TitlelessStackPresentation.DATA_KEY_CONTENT_COMPOSITE) != null;
		}
	};

	/**
	 * Filters a given {@link Control}
	 * 
	 * @param control
	 *            - the {@link Control} to check
	 * @return - true if the {@link Control} is valid in the sense of the filter
	 */
	public abstract boolean accept(Control control);
}
