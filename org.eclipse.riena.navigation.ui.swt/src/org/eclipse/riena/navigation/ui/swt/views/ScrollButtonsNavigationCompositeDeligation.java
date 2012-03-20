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

import org.eclipse.swt.widgets.Composite;

/**
 * Delegation of a composite for scrolling in the navigation with scroll
 * buttons.
 * 
 * @since 3.0
 */
public class ScrollButtonsNavigationCompositeDeligation extends AbstractNavigationCompositeDeligation {

	public ScrollButtonsNavigationCompositeDeligation(final Composite superParent, final Composite parent,
			final IModuleNavigationComponentProvider navigationProvider) {
		super(superParent, parent, navigationProvider);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 
	 * @return (negative) height of the scroll buttons
	 */
	@Override
	public int getBottomOffest() {
		return -getScrollingSupport().getButtonHeight();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Create the support with the scrolling logic (also the composite for
	 * scrolling with buttons).
	 */
	@Override
	protected Composite createNavigationComposite(final Composite parent) {
		final Composite naviComp = super.createNavigationComposite(parent);
		setScrollingSupport(new ScrollButtonsSupport(getSuperParent(), getNavigationProvider()));
		return naviComp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Composite getScrolledComposite() {
		return getScrollingSupport().getScrollComposite();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ScrollButtonsSupport getScrollingSupport() {
		return (ScrollButtonsSupport) super.getScrollingSupport();
	}

}
