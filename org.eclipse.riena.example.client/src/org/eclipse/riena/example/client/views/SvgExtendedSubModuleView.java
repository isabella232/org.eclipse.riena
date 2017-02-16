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
package org.eclipse.riena.example.client.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.core.resource.IconSize;
import org.eclipse.riena.ui.swt.CompletionCombo;
import org.eclipse.riena.ui.swt.CompletionCombo.AutoCompletionMode;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.ImageStore;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 *
 */
public class SvgExtendedSubModuleView extends SubModuleView {
	public static final String ID = SvgExtendedSubModuleView.class.getName();
	public static Menu ridgetMenu;
	public static Menu menu;

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		final RowLayout verticalRowLayout = new RowLayout();
		verticalRowLayout.type = SWT.VERTICAL;
		parent.setLayout(verticalRowLayout);

		createComboBoxComposite(parent);
		createLabelComposite(parent);
		createPopUpMenuComposite(parent);
		createInfoFlyOutComposite(parent);
	}

	/**
	 * @param parent
	 */
	private void createPopUpMenuComposite(final Composite parent) {
		final Composite popUpComposite = new Composite(parent, SWT.NONE);
		final RowLayout layout = new RowLayout();
		layout.type = SWT.VERTICAL;
		popUpComposite.setLayout(layout);

		final Label lblText = UIControlsFactory.createLabel(popUpComposite, "textField with custom contextmenu", "lblPopUpText"); //$NON-NLS-1$ //$NON-NLS-2$
		lblText.setText("Textfield with custom contextmenu, menuItems controlled by Ridgets IconSize A16"); //$NON-NLS-1$

		final Text popUpTextWithRidget = new Text(popUpComposite, SWT.BORDER);
		popUpTextWithRidget.setText("Uses Ridget"); //$NON-NLS-1$
		popUpTextWithRidget.setMenu(createPopUpMenuRidget(popUpTextWithRidget));

		UIControlsFactory.createLabel(popUpComposite, "textField with custom contextmenu", "lblPopUpText"); //$NON-NLS-1$ //$NON-NLS-2$
		lblText.setText("Textfield with custom contextmenu  IconSize A16"); //$NON-NLS-1$

		final Text popUpTextWithoutRidget = new Text(popUpComposite, SWT.BORDER);
		popUpTextWithoutRidget.setText("No Ridget"); //$NON-NLS-1$
		popUpTextWithoutRidget.setMenu(createPopUpMenu(popUpTextWithoutRidget));
	}

	/**
	 * @param popUpTextWithoutRidget
	 * @return
	 */
	private Menu createPopUpMenu(final Text parent) {
		menu = UIControlsFactory.createMenu(parent);
		final MenuItem itemEdit = UIControlsFactory.createMenuItem(menu, "Set Text", SWT.CASCADE); //$NON-NLS-1$
		itemEdit.setImage(ImageStore.getInstance().getImage("cloud", IconSize.A16)); //$NON-NLS-1$

		final Menu menuEdit = UIControlsFactory.createMenu(itemEdit);
		final MenuItem itemFoo2 = new MenuItem(menuEdit, SWT.NONE);
		itemFoo2.setText("foo"); //$NON-NLS-1$
		itemFoo2.setImage(ImageStore.getInstance().getImage("cloud", IconSize.A16)); //$NON-NLS-1$
		itemEdit.setMenu(menuEdit);
		return menu;
	}

	/**
	 * @param popUpText
	 * @return
	 */
	private Menu createPopUpMenuRidget(final Text parent) {
		ridgetMenu = UIControlsFactory.createMenu(parent);
		UIControlsFactory.createMenuItem(ridgetMenu, "Test", "textTest"); //$NON-NLS-1$ //$NON-NLS-2$
		final MenuItem itemEdit = UIControlsFactory.createMenuItem(ridgetMenu, "Set Text", SWT.CASCADE); //$NON-NLS-1$

		final Menu menuEdit = UIControlsFactory.createMenu(itemEdit);
		UIControlsFactory.createMenuItem(menuEdit, "foo", "itemFoo"); //$NON-NLS-1$ //$NON-NLS-2$ 
		UIControlsFactory.createMenuItem(menuEdit, "bar", "itemBar"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createMenuItem(menuEdit, "baz", "itemBaz"); //$NON-NLS-1$ //$NON-NLS-2$
		itemEdit.setMenu(menuEdit);
		return ridgetMenu;
	}

	/**
	 * @param parent
	 */
	private void createLabelComposite(final Composite parent) {
		final Composite labelComposite = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
		layout.numColumns = 2;

		labelComposite.setLayout(layout);
		final Label lblText = new Label(labelComposite, SWT.NONE);
		lblText.setText("Label with different Imagesizes:"); //$NON-NLS-1$
		UIControlsFactory.createLabel(labelComposite, ""); //$NON-NLS-1$

		UIControlsFactory.createLabel(labelComposite, "", "lblMediumImage"); //$NON-NLS-1$ //$NON-NLS-2$
		final Label lblMediumText = new Label(labelComposite, SWT.NONE);
		lblMediumText.setText("Controlled by Ridget  IconSize D48"); //$NON-NLS-1$

		final Label lblBig = new Label(labelComposite, SWT.NONE);
		lblBig.setImage(ImageStore.getInstance().getImage("cloud", IconSize.E64)); //$NON-NLS-1$
		final Label lblBigText = new Label(labelComposite, SWT.NONE);
		lblBigText.setText("Image directly set in the Widget IconSize E64"); //$NON-NLS-1$

	}

	private Composite createComboBoxComposite(final Composite parent) {
		final Composite comboComposite = new Composite(parent, SWT.NONE);
		final RowLayout layout = new RowLayout();
		layout.type = SWT.VERTICAL;
		comboComposite.setLayout(layout);

		UIControlsFactory.createLabel(comboComposite, "ComboBox with SVG Image:"); //$NON-NLS-1$
		final CompletionCombo combo = UIControlsFactory.createCompletionComboWithImage(comboComposite, "combo"); //$NON-NLS-1$
		combo.setAutoCompletionMode(AutoCompletionMode.ALLOW_MISSMATCH);

		return comboComposite;

	}

	private Composite createInfoFlyOutComposite(final Composite parent) {
		final Composite flyOutComposite = new Composite(parent, SWT.NONE);
		final RowLayout layout = new RowLayout();
		layout.type = SWT.VERTICAL;
		flyOutComposite.setLayout(layout);

		UIControlsFactory.createButton(flyOutComposite, "push for flyout", "flyOutButton"); //$NON-NLS-1$ //$NON-NLS-2$

		return flyOutComposite;
	}

}
