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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;

import org.eclipse.riena.ui.ridgets.IColumnFormatter;
import org.eclipse.riena.ui.ridgets.ICompletionComboRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractComboRidget;
import org.eclipse.riena.ui.swt.CompletionCombo;
import org.eclipse.riena.ui.swt.IFlashDelegate;

/**
 * Ridget for {@link CompletionCombo} widgets.
 */
public class CompletionComboRidget extends AbstractComboRidget implements ICompletionComboRidget {

	private final ModifyListener modifyListener = new ModifyListener() {
		public void modifyText(final ModifyEvent e) {
			setText(getUIControlText());
		}
	};

	private final IFlashDelegate flashDelegate = new IFlashDelegate() {
		public void flash() {
			CompletionComboRidget.this.flash();
		}
	};

	@Override
	public CompletionCombo getUIControl() {
		return (CompletionCombo) super.getUIControl();
	}

	@Override
	protected void addSelectionListener(final SelectionListener listener) {
		getUIControl().addSelectionListener(listener);
	}

	@Override
	protected void addTextModifyListener() {
		getUIControl().addModifyListener(modifyListener);
	}

	@Override
	protected void bindUIControl() {
		final CompletionCombo control = getUIControl();
		if (control != null) {
			control.setFlashDelegate(flashDelegate);
		}
		super.bindUIControl();
	}

	@Override
	protected void checkUIControl(final Object uiControl) {
		checkType(uiControl, CompletionCombo.class);
	}

	@Override
	protected void clearUIControlListSelection() {
		getUIControl().deselectAll();
		// Workaround for an SWT feature: when the user clicks in the list,
		// an asynchronous selection event is added to the end of the event 
		// queue, which will silenty an item. We asynchronously clear the list 
		// as well
		getUIControl().getDisplay().asyncExec(new Runnable() {
			public void run() {
				final CompletionCombo combo = getUIControl();
				if (combo != null && !combo.isDisposed()) {
					combo.clearSelection(); // this does not change the text
				}
			}
		});
	}

	@Override
	protected String[] getUIControlItems() {
		return getUIControl().getItems();
	}

	@Override
	protected IObservableList getUIControlItemsObservable() {
		return SWTObservables.observeItems(getUIControl());
	}

	@Override
	protected ISWTObservableValue getUIControlSelectionObservable() {
		final CompletionCombo control = getUIControl();
		final Realm realm = SWTObservables.getRealm(control.getDisplay());
		return (ISWTObservableValue) new CompletionComboSelectionProperty().observe(realm, control);
	}

	@Override
	protected String getUIControlText() {
		return getUIControl().getText();
	}

	@Override
	protected int indexOfInUIControl(final String item) {
		return getUIControl().indexOf(item);
	}

	@Override
	protected void removeAllFromUIControl() {
		getUIControl().removeAll();
	}

	@Override
	protected void removeSelectionListener(final SelectionListener listener) {
		getUIControl().removeSelectionListener(listener);
	}

	@Override
	protected void removeTextModifyListener() {
		getUIControl().removeModifyListener(modifyListener);
	}

	@Override
	protected void selectInUIControl(final int index) {
		getUIControl().select(index);
	}

	@Override
	protected void setItemsToControl(final String[] arrItems) {
		final Image[] arrImages = getImages(arrItems);
		getUIControl().setItems(arrItems, arrImages);
	}

	@Override
	protected void setTextToControl(final String text) {
		getUIControl().setText(text);
	}

	@Override
	protected void updateEditable() {
		getUIControl().setEditable(!isOutputOnly());
	}

	// helping methods
	//////////////////

	private Image[] getImages(final String[] arrItems) {
		Image[] result = null;
		final IColumnFormatter formatter = getColumnFormatter();
		if (formatter != null) {
			final IObservableList observableList = getObservableList();
			result = new Image[arrItems.length];
			for (int i = 0; i < result.length; i++) {
				final Object element = observableList.get(i);
				result[i] = (Image) formatter.getImage(element);
			}
		}
		return result;
	}

}