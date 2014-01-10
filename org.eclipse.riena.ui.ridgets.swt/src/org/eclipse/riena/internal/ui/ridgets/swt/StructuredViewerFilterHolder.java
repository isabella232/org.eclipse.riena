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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import org.eclipse.riena.ui.ridgets.IRidgetContentFilter;
import org.eclipse.riena.ui.ridgets.IRidgetContentFilterHolder;

/**
 * A content filter which works on {@link StructuredViewer} targets.
 * 
 * @see IRidgetContentFilterHolder
 */
public class StructuredViewerFilterHolder implements IRidgetContentFilterHolder<StructuredViewer> {
	private final Map<IRidgetContentFilter, ViewerFilter> ridgetFilterToViewerFilter = new HashMap<IRidgetContentFilter, ViewerFilter>();
	private final List<StructuredViewer> activeOnViewers = new ArrayList<StructuredViewer>();

	public void add(final IRidgetContentFilter filter) {
		if (ridgetFilterToViewerFilter.containsKey(filter)) {
			return;
		}
		final ViewerFilter viewerFilter = new ViewerFilter() {
			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
				return filter.isElementVisible(parentElement, element);
			}
		};
		ridgetFilterToViewerFilter.put(filter, viewerFilter);
		for (final StructuredViewer structuredViewer : activeOnViewers) {
			if (structuredViewer != null) {
				structuredViewer.addFilter(viewerFilter);
				structuredViewer.refresh();
			}
		}
	}

	public void remove(final IRidgetContentFilter filter) {
		final ViewerFilter viewerFilter = ridgetFilterToViewerFilter.remove(filter);
		for (final StructuredViewer structuredViewer : activeOnViewers) {
			if (viewerFilter != null) {
				structuredViewer.removeFilter(viewerFilter);
				structuredViewer.refresh();
			}
		}
	}

	public void activate(final StructuredViewer structuredViewer) {
		if (structuredViewer == null) {
			return;
		}
		activeOnViewers.add(structuredViewer);
		for (final ViewerFilter viewerFilter : ridgetFilterToViewerFilter.values()) {
			structuredViewer.addFilter(viewerFilter);
			structuredViewer.refresh();
		}
	}

	public void deactivate(final StructuredViewer structuredViewer) {
		if (structuredViewer == null) {
			return;
		}
		activeOnViewers.remove(structuredViewer);
		for (final ViewerFilter viewerFilter : ridgetFilterToViewerFilter.values()) {
			structuredViewer.removeFilter(viewerFilter);
			structuredViewer.refresh();
		}
	}
}
