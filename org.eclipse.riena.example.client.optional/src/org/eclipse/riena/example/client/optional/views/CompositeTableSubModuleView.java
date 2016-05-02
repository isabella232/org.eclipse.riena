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
package org.eclipse.riena.example.client.optional.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.nebula.widgets.compositetable.AbstractNativeHeader;
import org.eclipse.nebula.widgets.compositetable.CompositeTable;
import org.eclipse.nebula.widgets.compositetable.ResizableGridRowLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.common.IComplexComponent;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.swt.optional.OptionalUIControlsFactory;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * SWT {@link ITableRidget} sample with complex table rows.
 */
public class CompositeTableSubModuleView extends SubModuleView {

	public static final String ID = CompositeTableSubModuleView.class.getName();

	private static class Header extends AbstractNativeHeader {
		public Header(final Composite parent, final int style) {
			super(parent, style);
			setWeights(new int[] { 100, 100, 100 });
			setColumnText(new String[] { "Name", "Gender", "Pet" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}

	public static class Row extends Composite implements IComplexComponent {
		private final List<Object> controls = new ArrayList<Object>();

		public Row(final Composite parent, final int style) {
			super(parent, style);
			setBackground(parent.getBackground());
			setLayout(new ResizableGridRowLayout());
			createColumnName();
			createColumnGender();
			createColumnPet();
		}

		public List<Object> getUIControls() {
			return Collections.unmodifiableList(controls);
		}

		private void addUIControl(final Object uiControl, final String bindingId) {
			controls.add(uiControl);
			SWTBindingPropertyLocator.getInstance().setBindingProperty(uiControl, bindingId);
		}

		private void createColumnName() {
			final Composite cell = createCell();
			createText(cell, "first"); //$NON-NLS-1$
			createText(cell, "last"); //$NON-NLS-1$
		}

		private void createColumnGender() {
			final Composite cell = createCell();
			final ChoiceComposite gender = new ChoiceComposite(cell, SWT.NONE, false);
			addUIControl(gender, "gender"); //$NON-NLS-1$
		}

		private void createColumnPet() {
			final Composite cell = createCell();
			final ChoiceComposite pet = new ChoiceComposite(cell, SWT.NONE, true);
			addUIControl(pet, "pets"); //$NON-NLS-1$
		}

		private Composite createCell() {
			final Composite cell = new Composite(this, SWT.NONE);
			cell.setBackground(getBackground());
			GridLayoutFactory.swtDefaults().numColumns(1).applyTo(cell);
			return cell;
		}

		private void createText(final Composite parent, final String bindingId) {
			final Text txtFirst = UIControlsFactory.createText(parent);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(txtFirst);
			addUIControl(txtFirst, bindingId);
		}

	}

	private CompositeTable table;

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));

		final Group group1 = createTableGroup(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(group1);
	}

	@Override
	public void setFocus() {
		if (canRestoreFocus()) {
			super.setFocus();
		} else if (table.getRowControls().length > 0) {
			table.getRowControls()[0].setFocus();
		}
	}

	// helping methods
	// ////////////////

	private Group createTableGroup(final Composite parent) {
		final Group group = UIControlsFactory.createGroup(parent, "Composite Table:"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(1).applyTo(group);

		table = OptionalUIControlsFactory.createCompositeTable(group, SWT.BORDER);
		new Header(table, SWT.NONE);
		new Row(table, SWT.NONE);
		table.setInsertHint("Press 'Add' to add more rows..."); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, true).applyTo(table);

		table.setRunTime(true);

		addUIControl(table, "table"); //$NON-NLS-1$

		final Composite buttonComposite = createButtonComposite(group);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(buttonComposite);

		return group;
	}

	private Composite createButtonComposite(final Group group) {
		final Composite buttonComposite = UIControlsFactory.createComposite(group);
		GridLayoutFactory.fillDefaults().numColumns(3).equalWidth(false).applyTo(buttonComposite);

		final Button buttonAdd = UIControlsFactory.createButton(buttonComposite);
		final int widthHint = UIControlsFactory.getWidthHint(buttonAdd);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.END, SWT.BEGINNING).hint(widthHint, SWT.DEFAULT).applyTo(buttonAdd);
		addUIControl(buttonAdd, "buttonAdd"); //$NON-NLS-1$

		final Button buttonDelete = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonDelete, "buttonDelete"); //$NON-NLS-1$

		final Button buttonDump = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonDump, "buttonDump"); //$NON-NLS-1$

		return buttonComposite;
	}
}
