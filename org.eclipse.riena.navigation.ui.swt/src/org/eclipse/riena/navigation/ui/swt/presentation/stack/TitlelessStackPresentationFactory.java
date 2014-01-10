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
package org.eclipse.riena.navigation.ui.swt.presentation.stack;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.presentations.AbstractPresentationFactory;
import org.eclipse.ui.presentations.IStackPresentationSite;
import org.eclipse.ui.presentations.StackPresentation;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.ISubApplicationNode;

public class TitlelessStackPresentationFactory extends AbstractPresentationFactory {

	private final static Map<IStackPresentationSite, StackPresentation> PRESENTATIONS = new HashMap<IStackPresentationSite, StackPresentation>();

	/**
	 * @see org.eclipse.ui.presentations.AbstractPresentationFactory#createEditorPresentation(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.presentations.IStackPresentationSite)
	 */
	@Override
	public StackPresentation createEditorPresentation(final Composite parent, final IStackPresentationSite site) {
		return getPresentation(parent, site);
	}

	/**
	 * @see org.eclipse.ui.presentations.AbstractPresentationFactory#createStandaloneViewPresentation(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.presentations.IStackPresentationSite, boolean)
	 */
	@Override
	public StackPresentation createStandaloneViewPresentation(final Composite parent,
			final IStackPresentationSite site, final boolean showTitle) {
		return null;
	}

	/**
	 * @see org.eclipse.ui.presentations.AbstractPresentationFactory#createViewPresentation(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.presentations.IStackPresentationSite)
	 */
	@Override
	public StackPresentation createViewPresentation(final Composite parent, final IStackPresentationSite site) {
		return getPresentation(parent, site);
	}

	private StackPresentation getPresentation(final Composite parent, final IStackPresentationSite site) {
		if (PRESENTATIONS.get(site) == null) {
			PRESENTATIONS.put(site, new TitlelessStackPresentation(parent, site));
		}
		return getPresentation(site);
	}

	public StackPresentation getPresentation(final IStackPresentationSite site) {
		return PRESENTATIONS.get(site);
	}

	/**
	 * @since 4.0
	 */
	public static TitlelessStackPresentation getActiveTitlelessStackPresentation() {
		final ISubApplicationNode activeSubApplicationNode = ApplicationNodeManager.locateActiveSubApplicationNode();
		for (final StackPresentation stackPresenation : PRESENTATIONS.values()) {
			if (stackPresenation instanceof TitlelessStackPresentation) {
				final TitlelessStackPresentation titlelessStackPresenation = (TitlelessStackPresentation) stackPresenation;
				final ISubApplicationNode subApplicationNode = titlelessStackPresenation.getSubApplicationNode();
				if (subApplicationNode != null && activeSubApplicationNode.equals(subApplicationNode)) {
					return titlelessStackPresenation;
				}
			}
		}
		return null;
	}
}
