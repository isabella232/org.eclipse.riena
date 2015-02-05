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
package org.eclipse.riena.ui.swt.facades.internal;

import java.util.Collection;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.util.Policy;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerRow;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.internal.ui.ridgets.swt.TableRidgetLabelProvider;
import org.eclipse.riena.internal.ui.ridgets.swt.TableRidgetTableViewer;
import org.eclipse.riena.ui.core.marker.RowErrorMessageMarker;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Customizable tool tips for a TableRidget.
 * <p>
 * <i>(Note: The TableRidget must use <code>TableRidgetTableViewer</code>)</i>
 */
public class TableRidgetToolTipSupport extends ColumnViewerToolTipSupport implements ITableRidgetToolTipSupport {

	private static final String VIEWER_CELL_KEY = Policy.JFACE + "_VIEWER_CELL_KEY"; //$NON-NLS-1$

	private static final int DEFAULT_SHIFT_X = 10;
	private static final int DEFAULT_SHIFT_Y = 0;

	private TableRidgetTableViewer viewer;
	private String defaultToolTip;

	/**
	 * Enable ToolTip support for the viewer by creating an instance from this
	 * class. To get all necessary informations this support class consults the
	 * {@link TableRidgetLabelProvider}.
	 * 
	 * @param viewer
	 *            the viewer the support is attached to
	 * @param style
	 *            style passed to control tool tip behavior
	 * 
	 * @param manualActivation
	 *            <code>true</code> if the activation is done manually using
	 *            {@link #show(Point)}
	 */
	protected TableRidgetToolTipSupport(final ColumnViewer viewer, final int style) {
		super(viewer, ToolTip.NO_RECREATE, false);
		Assert.isLegal(viewer instanceof TableRidgetTableViewer);
		this.viewer = (TableRidgetTableViewer) viewer;
	}

	/**
	 * Create the support of JFace tooltips for a given Table.
	 * 
	 * @param viewer
	 *            The viewer component of the table
	 * @return The instance of the tooltip support.
	 */
	public static ITableRidgetToolTipSupport enableSupportFor(final ColumnViewer viewer) {
		final TableRidgetToolTipSupport support = new TableRidgetToolTipSupport(viewer, ToolTip.NO_RECREATE);
		support.init();
		return support;
	}

	TableRidgetTableViewer getViewer() {
		return viewer;
	}

	private void init() {
		final Control table = viewer.getTable();
		if (table != null) {
			final String tableToolTip = table.getToolTipText();
			defaultToolTip = tableToolTip != null ? tableToolTip : ""; //$NON-NLS-1$
			table.setToolTipText(defaultToolTip);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean shouldCreateToolTip(final Event event) {

		boolean rv = false;

		final Point point = new Point(event.x, event.y);
		final ViewerRow row = viewer.getViewerRow(point);

		viewer.getControl().setToolTipText(defaultToolTip);

		if (row != null) {
			final Widget item = row.getItem();
			final Object element = item.getData();

			final ViewerCell cell = row.getCell(point);

			if (cell == null) {
				return false;
			}

			final Table table = viewer.getTable();
			final int column = SwtUtilities.findColumn(table, point);
			TableRidgetLabelProvider labelProvider = null;
			if ((viewer.getLabelProvider() instanceof TableRidgetLabelProvider) && ((column != -1))) {
				labelProvider = (TableRidgetLabelProvider) viewer.getLabelProvider();
			}

			String text = getErrorToolTip(element);
			if (StringUtils.isEmpty(text)) {
				if ((labelProvider != null) && (column != -1)) {
					text = labelProvider.getToolTipText(element, column);
				}
			}
			if (StringUtils.isEmpty(text)) {
				if ((item instanceof TableItem) && (column != -1)) {
					text = ((TableItem) item).getText(column);
				}
			}
			if (StringUtils.isEmpty(text)) {
				text = defaultToolTip;
			}

			Image img = null;
			if ((labelProvider != null) && (column != -1)) {
				img = labelProvider.getToolTipImage(element, column);
			}

			if (text == null && img == null) {
				table.setToolTipText(defaultToolTip);
				rv = false;
			} else {
				Point shift = null;
				if (column != -1) {
					setPopupDelay(labelProvider.getToolTipDisplayDelayTime(element, column));
					setHideDelay(labelProvider.getToolTipTimeDisplayed(element, column));
					shift = labelProvider.getToolTipShift(element, column);
				}

				if (shift == null) {
					setShift(new Point(DEFAULT_SHIFT_X, DEFAULT_SHIFT_Y));
				} else {
					setShift(new Point(shift.x, shift.y));
				}

				setData(VIEWER_CELL_KEY, cell);

				setText(text);
				setImage(img);
				if (column != -1) {
					setStyle(labelProvider.getToolTipStyle(element, column));
					setForegroundColor(labelProvider.getToolTipForegroundColor(element, column));
					setBackgroundColor(labelProvider.getToolTipBackgroundColor(element, column));
					setFont(labelProvider.getToolTipFont(element, column));
				}

				rv = true;
			}
		}

		return rv;
	}

	private String getErrorToolTip(final Object element) {
		if (element != null) {
			final ITableRidget tableRidget = viewer.getTableRidget();
			final Collection<RowErrorMessageMarker> markers = tableRidget.getMarkersOfType(RowErrorMessageMarker.class);
			for (final RowErrorMessageMarker marker : markers) {
				if (marker.getRowValue() == element) {
					return marker.getMessage();
				}
			}
		}
		return null;
	}

	private void resetToolTip(final Control table) {
		if (table == null) {
			return;
		}
		if (table.getToolTipText() == null || !table.getToolTipText().equals(defaultToolTip)) {
			table.setToolTipText(defaultToolTip);
		}
	}

	public void disableSupport() {
		deactivate();
		if (viewer != null) {
			resetToolTip(viewer.getTable());
		}
	}

	public void enableSupport(final ColumnViewer viewer) {
		if (viewer instanceof TableRidgetTableViewer) {
			if (this.viewer != viewer) {
				disableSupport();
				this.viewer = (TableRidgetTableViewer) viewer;
				init();
			}
			activate();
		}
	}

}
