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

import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;

import org.eclipse.riena.core.util.ObjectUtils;
import org.eclipse.riena.ui.ridgets.IMasterDetailsRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractMasterDetailsRidget;
import org.eclipse.riena.ui.swt.AbstractMasterDetailsComposite;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;

/**
 * A ridget for the {@link MasterDetailsComposite}.
 */
public class MasterDetailsRidget extends AbstractMasterDetailsRidget implements IMasterDetailsRidget {

	private final DirtyDetailsChecker dirtyDetailsChecker;

	public MasterDetailsRidget() {
		super();
		dirtyDetailsChecker = new DirtyDetailsChecker();
	}

	@Override
	public MasterDetailsComposite getUIControl() {
		return (MasterDetailsComposite) super.getUIControl();
	}

	@Override
	protected void checkUIControl(final Object uiControl) {
		super.checkUIControl(uiControl);
		checkType(uiControl, MasterDetailsComposite.class);
	}

	@Override
	protected void bindUIControl() {
		final MasterDetailsComposite control = getUIControl();
		if (control != null) {
			final Table table = control.getTable();
			table.addSelectionListener(dirtyDetailsChecker);
		}
	}

	@Override
	/**
	 * Do not unbind detail ridgets here as this method is called after details ridgets are bound and before master is bound. 
	 * TODO This callback has to be renamed!
	 */
	protected void unbindUIControl() {

		final MasterDetailsComposite control = getUIControl();
		if (control != null) {
			final Table table = control.getTable();
			table.removeSelectionListener(dirtyDetailsChecker);
		}
	}

	@Override
	protected final void bindTableToModel(final IObservableList rowObservables, final Class<? extends Object> rowClass,
			final String[] columnPropertyNames, final String[] columnHeaders) {
		getTableRidget().bindToModel(rowObservables, rowClass, columnPropertyNames, columnHeaders);
	}

	@Override
	protected void configureTableRidget() {
		// unused
	}

	@Override
	protected final void setTableSelection(final Object value) {
		getTableRidget().setSelection(value);
	}

	@Override
	protected final Object getTableSelection() {
		final List<Object> selection = getTableRidget().getSelection();
		return selection.isEmpty() ? null : selection.get(0);
	}

	@Override
	protected final IObservableValue getSelectionObservable() {
		return getTableRidget().getSingleSelectionObservable();
	}

	@Override
	protected final void revealTableSelection() {
		getUIControl().getTable().showSelection();
	}

	@Override
	protected final void clearTableSelection() {
		dirtyDetailsChecker.clearSavedSelection();
		getTableRidget().clearSelection();
	}

	// helping methods
	// ////////////////

	private ITableRidget getTableRidget() {
		return getRidget(ITableRidget.class, AbstractMasterDetailsComposite.BIND_ID_TABLE);
	}

	/**
	 * Non API; public for testing only.
	 */
	@Override
	public void handleApply() {
		super.handleApply();
		final Table table = getUIControl().getTable();
		/*
		 * Fix for bug 283694: if only one element is in the table, remove the
		 * selection on apply, so it can be selected again for editing.
		 */
		if (table.getItemCount() == 1) {
			getTableRidget().clearSelection();
		} else {
			table.select(table.getSelectionIndex());
		}
		table.setFocus();
	}

	@Override
	public void updateFromModel() {
		final Object oldSelection = getTableSelection();
		super.updateFromModel();
		final Object tableSelection = getTableSelection();
		if (!ObjectUtils.equals(oldSelection, tableSelection)) {
			handleSelectionChange(tableSelection);
		} else if (tableSelection != null) {
			updateDetails(tableSelection);
		}

		if (dirtyDetailsChecker != null) {
			dirtyDetailsChecker.clearSavedSelection();
		}
	}

	// helping classes
	// ////////////////

	/**
	 * If the details area is dirty, it will ask for confirmation when changing
	 * selection.
	 * <p>
	 * Implementation note: because of we are notified after the selection
	 * change the listener will revert to the previous selection, if
	 * confirmation is denied. This will <b>not</b> result in clearing the
	 * details area.
	 */
	private final class DirtyDetailsChecker extends SelectionAdapter {

		private int oldIndex = -1; // single selection
		private Object oldSelection = new Object();

		@Override
		public void widgetSelected(final SelectionEvent e) {
			final Object newSelection = e.item.getData();
			final Table table = (Table) e.widget;
			if (oldIndex == table.getSelectionIndex() || oldSelection.equals(newSelection)) { // already selected
				return;
			}
			if (areDetailsChanged()) {
				if (!getUIControl().confirmDiscardChanges()) {
					table.setSelection(oldIndex);
					return;
				}
			}
			oldIndex = table.getSelectionIndex();
			oldSelection = newSelection;
			handleSelectionChange(newSelection);
		}

		void clearSavedSelection() {
			oldIndex = -1;
			oldSelection = new Object();
		}
	}

}
