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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.navigation.ui.swt.facades.NavigationFacade;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.WidgetIdentificationSupport;

/**
 * Abstract implementation of a delegation of a composite for scrolling in the
 * navigation.
 * 
 * @since 3.0
 */
public abstract class AbstractNavigationCompositeDeligation implements INavigationCompositeDelegation {

	protected static final Color NAVIGATION_BACKGROUND = LnfManager.getLnf().getColor(
			LnfKeyConstants.NAVIGATION_BACKGROUND);
	/**
	 * @since 4.0
	 */
	public static final int BORDER_MARGIN = 3;

	private final Composite superParent;
	private final Composite parent;
	private final IModuleNavigationComponentProvider navigationProvider;
	private final Composite navigationComposite;
	private AbstractScrollingSupport scrollingSupport;

	public AbstractNavigationCompositeDeligation(final Composite superParent, final Composite parent,
			final IModuleNavigationComponentProvider navigationProvider) {
		this.superParent = superParent;
		this.parent = parent;
		this.navigationProvider = navigationProvider;
		this.navigationComposite = createNavigationComposite(getParent());
		getScrolledComposite().setLayoutData(getLayoutData());
	}

	private FormData getLayoutData() {
		final boolean fastView = NavigationFacade.getDefault().getApplicationUtility().isNavigationFastViewEnabled();
		final FormData formData = new FormData();
		formData.top = new FormAttachment(getParent(), 0);
		formData.left = new FormAttachment(0, fastView ? BORDER_MARGIN : 0);
		formData.right = new FormAttachment(100, fastView ? -BORDER_MARGIN : 0);
		formData.bottom = new FormAttachment(100, fastView ? -BORDER_MARGIN : 0);
		return formData;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 
	 * @return 0 because the default implementation has no scroll buttons.
	 */
	public int getBottomOffest() {
		return 0;
	}

	/**
	 * Returns the size of the vertical scroll bar of the given
	 * {@code Composite}.
	 * 
	 * @return size of scroll bar or zero size if scroll bar dosn't exists or
	 *         isn't visible.
	 */
	public Point getVerticalScrollBarSize() {
		return new Point(0, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	public void scroll() {
		getScrollingSupport().scroll();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * In the default implementation do nothing.
	 */
	public void updateSize(final int height) {
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 */
	public Composite getNavigationComposite() {
		return navigationComposite;
	}

	/**
	 * Creates the composite inside the scroll area.
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new composite inside
	 *            the scroll area
	 * @return
	 */
	protected Composite createNavigationComposite(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.DOUBLE_BUFFERED);
		composite.setBackground(NAVIGATION_BACKGROUND);
		composite.setLayout(new FormLayout());
		WidgetIdentificationSupport.setIdentification(composite, "NavigationView"); //$NON-NLS-1$
		return composite;
	}

	protected Composite getParent() {
		return parent;
	}

	protected Composite getSuperParent() {
		return superParent;
	}

	protected abstract Composite getScrolledComposite();

	/**
	 * Returns the support of the scrolling logic.
	 * 
	 * @return support of scrolling logic
	 */
	protected AbstractScrollingSupport getScrollingSupport() {
		return scrollingSupport;
	}

	/**
	 * Sets the support of the scrolling logic.
	 * 
	 * @param scrollingSupport
	 *            support of scrolling logic
	 */
	protected void setScrollingSupport(final AbstractScrollingSupport scrollingSupport) {
		this.scrollingSupport = scrollingSupport;
	}

	protected IModuleNavigationComponentProvider getNavigationProvider() {
		return navigationProvider;
	}

}
