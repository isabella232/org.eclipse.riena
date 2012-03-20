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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.ui.ridgets.IEditableRidget;
import org.eclipse.riena.ui.ridgets.IValueRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Editing support for tables with data binding and Ridgets.
 */
public class TableRidgetEditingSupport extends EditingSupport {

	private final AbstractTableRidget tableRidget;
	private final PropertyDescriptor property;
	private final ColumnViewerEditorActivationListenerHelper activationListener;
	private final CellEditor cellEditor;
	private IValueRidget valueRidget;

	/**
	 * Creates the editing support for the given table and property.
	 * 
	 * @param tableRidget
	 *            Ridget of the table
	 * @param property
	 *            property of the column
	 * @param columnStyle
	 *            style of the table column
	 */
	public TableRidgetEditingSupport(final AbstractTableRidget tableRidget, final PropertyDescriptor property,
			final int columnStyle) {
		super(tableRidget.getTableViewer());
		this.tableRidget = tableRidget;
		this.property = property;
		activationListener = new ColumnViewerEditorActivationListenerHelper();
		cellEditor = createCellEditort(property, columnStyle);
	}

	/**
	 * Creates a proper cell editor for the given property.
	 * <p>
	 * According to the property type a cell editor is created.
	 * 
	 * @param style
	 *            style of the table column
	 * @return cell editor or {@code null} if no cell editor was created
	 */
	private CellEditor createCellEditort(final PropertyDescriptor property, final int style) {

		if (property == null) {
			return null;
		}

		final Class<?> type = property.getPropertyType();
		final Composite table = (Composite) getViewer().getControl();
		final int editorStyle = getAlignment(style);

		if (Boolean.class.equals(type) || Boolean.TYPE.equals(type)) {
			// final BooleanCellEditor editor = new BooleanCellEditor(table, editorStyle);
			// editor.setChangeOnActivation(true);
			// return editor;
			return null;
		} else {
			final TextCellEditor editor = new TextCellEditor(table, editorStyle);
			if (Integer.class.equals(type) || Integer.TYPE.equals(type)) {
				editor.getControl().setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_NUMERIC);
			} else if (Short.class.equals(type) || Short.TYPE.equals(type)) {
				editor.getControl().setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_NUMERIC);
			} else if (Long.class.equals(type) || Long.TYPE.equals(type)) {
				editor.getControl().setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_NUMERIC);
			} else if (BigInteger.class.equals(type)) {
				editor.getControl().setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_NUMERIC);
			} else if (Float.class.equals(type) || Float.TYPE.equals(type)) {
				editor.getControl().setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_DECIMAL);
			} else if (Double.class.equals(type) || Double.TYPE.equals(type)) {
				editor.getControl().setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_DECIMAL);
			} else if (BigDecimal.class.equals(type)) {
				editor.getControl().setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_DECIMAL);
			} else if (Date.class.equals(type)) {
				editor.getControl().setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_DATE);
			}
			return editor;

		}

	}

	/**
	 * Extras the alignment form the given style.
	 * 
	 * @param style
	 * @return alignment
	 */
	private int getAlignment(final int style) {
		if ((style & SWT.LEFT) == SWT.LEFT) {
			return SWT.LEFT;
		}
		if ((style & SWT.RIGHT) == SWT.RIGHT) {
			return SWT.RIGHT;
		}
		if ((style & SWT.CENTER) == SWT.CENTER) {
			return SWT.CENTER;
		}
		return SWT.DEFAULT;
	}

	@Override
	protected CellEditor getCellEditor(final Object element) {
		return cellEditor;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Binds the cell editor with a Ridget and the property.
	 */
	@Override
	protected void initializeCellEditorValue(final CellEditor cellEditor, final ViewerCell cell) {
		valueRidget = (IValueRidget) SwtRidgetFactory.createRidget(cellEditor.getControl());

		final Object valueHolder = cell.getElement();
		valueRidget.bindToModel(valueHolder, property.getName());
		valueRidget.updateFromModel();

		getViewer().getColumnViewerEditor().addEditorActivationListener(activationListener);

	}

	@Override
	protected void saveCellEditorValue(final CellEditor cellEditor, final ViewerCell cell) {
		if (valueRidget instanceof IEditableRidget) {
			((IEditableRidget) valueRidget).revalidate();
		} else if (valueRidget instanceof ToggleButtonRidget) {
			((ToggleButtonRidget) valueRidget).getValueBindingSupport().updateFromTarget();
		}
	}

	@Override
	protected boolean canEdit(final Object element) {
		return getCellEditor(element) != null;
	}

	@Override
	protected Object getValue(final Object element) {
		return null;
	}

	@Override
	protected void setValue(final Object element, final Object value) {
	}

	private class ColumnViewerEditorActivationListenerHelper extends ColumnViewerEditorActivationListener {

		@Override
		public void beforeEditorActivated(final ColumnViewerEditorActivationEvent event) {
		}

		@Override
		public void afterEditorActivated(final ColumnViewerEditorActivationEvent event) {
		}

		@Override
		public void beforeEditorDeactivated(final ColumnViewerEditorDeactivationEvent event) {
		}

		@Override
		public void afterEditorDeactivated(final ColumnViewerEditorDeactivationEvent event) {

			//			if (event.eventType == ColumnViewerEditorDeactivationEvent.EDITOR_SAVED) {
			//			} else if (event.eventType == ColumnViewerEditorDeactivationEvent.EDITOR_CANCELED) {
			//			}

			tableRidget.updateFromModel();

			if (valueRidget != null) {
				valueRidget.setUIControl(null);
			}
			valueRidget = null;
		}

	}

}
