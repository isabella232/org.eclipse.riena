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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

/**
 * Delegation of a composite for scrolling in the navigation.
 * 
 * @since 3.0
 */
public interface INavigationCompositeDelegation {

	/**
	 * Returns the offset for the scroll buttons.
	 * 
	 * @return offset or zero, if no scroll buttons are used.
	 */
	int getBottomOffest();

	/**
	 * The composite inside the scroll area.
	 * 
	 * @return scrollable composite
	 */
	Composite getNavigationComposite();

	/**
	 * Returns the size of the vertical scroll bar of the given
	 * {@code Composite}.
	 * 
	 * @return size of scroll bar or zero size if scroll bar dosn't exists or
	 *         isn't visible.
	 */
	Point getVerticalScrollBarSize();

	/**
	 * Scrolls to the active module.
	 */
	void scroll();

	/**
	 * Updates the size of the scrolled composite.
	 * 
	 * @param height
	 *            height of the scrolled composite
	 */
	void updateSize(final int height);

}
