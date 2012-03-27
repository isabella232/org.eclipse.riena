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
package org.eclipse.riena.navigation.ui.swt.component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.WorkbenchWindow;

import org.eclipse.riena.internal.navigation.ui.swt.CoolbarUtils;
import org.eclipse.riena.ui.ridgets.swt.MenuManagerHelper;
import org.eclipse.riena.ui.swt.facades.SWTFacade;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * This composites has a list of the top-level menus of the Riena menu bar (a cool bar with an item for every top-level menu).
 */
public class MenuCoolBarComposite extends Composite {

	static final String MENU_DATA_KEY = "Menu"; //$NON-NLS-1$

	private final IWorkbenchWindow window;

	private CoolItem menuCoolItem;
	private ToolBar menuToolBar;
	private CoolBar menuCoolBar;

	// private final Map<MenuManager, MenuManagerState> menuManagerOldState;

	/**
	 * Creates an new instance of {@code MenuCoolBarComposite} given its parent and a style value describing its behavior and appearance.
	 * 
	 * @param parent
	 *            a composite which will be the parent of the new instance (cannot be null)
	 * @param style
	 *            the style of widget to construct
	 * @param window
	 *            an IWorkbenchWindow instance, used to determine the menu entries. May be null.
	 * @since 3.0
	 */
	public MenuCoolBarComposite(final Composite parent, final int style, final IWorkbenchWindow window) {
		super(parent, style);
		setLayout(new FillLayout());
		this.window = window;
		create();
	}

	public List<ToolItem> getTopLevelItems() {
		final ToolItem[] toolItems = menuToolBar.getItems();
		return Arrays.asList(toolItems);
	}

	/**
	 * Disposes all menu items and re-creates them.
	 * 
	 * @return an empty {@link List}
	 */
	public List<ToolItem> updateMenuItems() {
		if (menuCoolBar != null) {
			menuCoolBar.dispose();
		}
		create();
		layout();
		return Collections.emptyList();
	}

	// helping methods
	//////////////////

	/**
	 * Calculates and sets the size of the given cool item.
	 * 
	 * @param item
	 *            item of cool bar
	 */
	private void calcSize(final CoolItem item) {
		final Control control = item.getControl();
		Point pt = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		pt = item.computeSize(pt.x, pt.y);
		item.setSize(pt);
	}

	/**
	 * Creates a top-level menu and adds it to the Riena menu bar.
	 * 
	 * @param menuManager
	 * @param listener
	 */
	private ToolItem createAndAddMenu(final MenuManager menuManager, final ToolBarMenuListener listener) {
		if (menuManager.isVisible()) {
			final ToolItem toolItem = new ToolItem(menuToolBar, SWT.CHECK);
			toolItem.setText(menuManager.getMenuText());
			final MenuManagerHelper helper = new MenuManagerHelper();
			final Menu menu = helper.createMenu(menuToolBar, toolItem, menuManager);
			toolItem.setData(MENU_DATA_KEY, menu);
			calcSize(menuCoolItem);
			toolItem.addSelectionListener(listener);
			return toolItem;
		}
		return null;
	}

	/**
	 * Creates the cool and tool bar. These build the menu bar of the Riena sub-application.
	 */
	private void create() {
		menuCoolBar = new CoolBar(this, SWT.FLAT);
		menuCoolItem = CoolbarUtils.initCoolBar(menuCoolBar, getMenuBarFont());
		menuToolBar = (ToolBar) menuCoolItem.getControl();
		final ToolBarMenuListener listener = new ToolBarMenuListener();
		menuToolBar.addMouseListener(listener);
		SWTFacade.getDefault().addMouseTrackListener(menuToolBar, listener);
		fillMenuBar(listener);
	}

	private void fillMenuBar(final ToolBarMenuListener listener) {
		for (final IContributionItem contribItem : getTopLevelMenuEntries()) {
			if (contribItem instanceof MenuManager) {
				createAndAddMenu((MenuManager) contribItem, listener);
			}
		}
	}

	private Font getMenuBarFont() {
		return LnfManager.getLnf().getFont(LnfKeyConstants.MENUBAR_FONT);
	}

	/**
	 * Returns the top-level menu entries.
	 */
	@SuppressWarnings("restriction")
	private IContributionItem[] getTopLevelMenuEntries() {
		if (window instanceof WorkbenchWindow) {
			final MenuManager menuManager = ((WorkbenchWindow) window).getMenuManager();
			return menuManager.getItems();
		}
		return new IContributionItem[0];
	}
}
