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
package org.eclipse.riena.example.client.optional.views;

import java.util.Arrays;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.nebula.widgets.compositetable.AbstractNativeHeader;
import org.eclipse.nebula.widgets.compositetable.CompositeTable;
import org.eclipse.nebula.widgets.compositetable.ResizableGridRowLayout;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.common.IComplexComponent;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.swt.optional.OptionalUIControlsFactory;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Example for various marker types.
 * 
 * @see IMarkableRidget
 */
public class OptionalMarkerSubModuleView extends SubModuleView {

	public static final String ID = OptionalMarkerSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(2, false));

		final Group group1 = createMarkerOptionsGroup(parent);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(group1);
		final Group group2 = createVisibilityOptionsGroup(parent);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(group2);
		final Group group3 = createControlsGroup(parent);
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(group3);
	}

	// helping methods
	// ////////////////

	private Group createMarkerOptionsGroup(final Composite parent) {
		final Group group = UIControlsFactory.createGroup(parent, "Marker Options:"); //$NON-NLS-1$
		group.setLayout(createGridLayout(4));

		UIControlsFactory.createButtonCheck(group, "&mandatory", "checkMandatory");//$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonCheck(group, "&error", "checkError"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonCheck(group, "&disabled", "checkDisabled"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonCheck(group, "&output", "checkOutput"); //$NON-NLS-1$ //$NON-NLS-2$

		return group;
	}

	private Group createVisibilityOptionsGroup(final Composite parent) {
		final Group group = UIControlsFactory.createGroup(parent, "Visibility Options:"); //$NON-NLS-1$
		group.setLayout(createGridLayout(2));

		UIControlsFactory.createButtonCheck(group, "&hidden", "checkHidden"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonCheck(group, "hidden &parent", "checkHiddenParent"); //$NON-NLS-1$ //$NON-NLS-2$

		return group;
	}

	private Group createControlsGroup(final Composite parent) {

		final Group group = UIControlsFactory.createGroup(parent, "UI-Controls:"); //$NON-NLS-1$
		final int defaultVSpacing = new GridLayout().verticalSpacing;
		GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(false).margins(20, 20).spacing(10, defaultVSpacing).applyTo(group);

		// CompositeTable

		final Label labelCompTable = UIControlsFactory.createLabel(group, "Composite\nTable:"); //$NON-NLS-1$ 
		GridDataFactory.fillDefaults().grab(false, true).applyTo(labelCompTable);

		final CompositeTable compTable = OptionalUIControlsFactory.createCompositeTable(group, SWT.BORDER);
		new Header(compTable, SWT.NONE);
		new Row(compTable, SWT.NONE);
		compTable.setRunTime(true);
		addUIControl(compTable, "compTable"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).hint(SWT.DEFAULT, 150).applyTo(compTable);

		// Grid

		final Label labelGrid = UIControlsFactory.createLabel(group, "Grid:"); //$NON-NLS-1$ 
		GridDataFactory.fillDefaults().grab(false, true).applyTo(labelGrid);

		final Composite gridComposite = UIControlsFactory.createComposite(group);
		GridLayoutFactory.fillDefaults().numColumns(3).equalWidth(false).applyTo(gridComposite);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(gridComposite);

		final Grid grid = OptionalUIControlsFactory.createGrid(gridComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL, "grid"); //$NON-NLS-1$
		grid.setHeaderVisible(true);
		GridDataFactory.fillDefaults().grab(true, false).hint(SWT.DEFAULT, 150).span(3, 1).applyTo(grid);
		final GridColumn columnWord = new GridColumn(grid, SWT.LEFT);
		columnWord.setWidth(110);
		final GridColumn columnUppercase = new GridColumn(grid, SWT.LEFT);
		columnUppercase.setWidth(110);
		final GridColumn columnACount = new GridColumn(grid, SWT.LEFT);
		columnACount.setWidth(110);

		final Button markBtn = UIControlsFactory.createButton(gridComposite, "Mark", "markRowBtn");
		GridDataFactory.fillDefaults().grab(false, false).hint(80, SWT.DEFAULT).applyTo(markBtn);
		final Button unmarkBtn = UIControlsFactory.createButton(gridComposite, "Unmark", "unmarkRowBtn");
		GridDataFactory.fillDefaults().grab(false, false).hint(80, SWT.DEFAULT).indent(15, SWT.DEFAULT).applyTo(unmarkBtn);

		return group;

	}

	private GridLayout createGridLayout(final int numColumns) {
		final GridLayout layout = new GridLayout(numColumns, false);
		layout.marginWidth = 20;
		layout.marginHeight = 20;
		return layout;
	}

	// helping classes
	// ////////////////

	/**
	 * A header control for {@link CompositeTable}.
	 */
	private static final class Header extends AbstractNativeHeader {
		public Header(final Composite parent, final int style) {
			super(parent, style);
			setWeights(new int[] { 100, 100 });
			setColumnText(new String[] { "Last Name", "First Name" }); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * A row control with two text fields used by {@link CompositeTable}.
	 */
	private static final class Row extends Composite implements IComplexComponent {
		private final Text txtLast;
		private final Text txtFirst;

		public Row(final Composite parent, final int style) {
			super(parent, style);
			this.setLayout(new ResizableGridRowLayout());
			txtLast = new Text(this, SWT.BORDER);
			SWTBindingPropertyLocator.getInstance().setBindingProperty(txtLast, "txtLast"); //$NON-NLS-1$
			txtFirst = new Text(this, SWT.BORDER);
			SWTBindingPropertyLocator.getInstance().setBindingProperty(txtFirst, "txtFirst"); //$NON-NLS-1$
		}

		public java.util.List<Object> getUIControls() {
			return Arrays.asList(new Object[] { txtLast, txtFirst });
		}
	}

}
