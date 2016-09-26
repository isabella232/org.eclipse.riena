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
import org.eclipse.riena.internal.ui.ridgets.swt.ToolItemScalingHelper;
import org.eclipse.riena.ui.ridgets.swt.MenuManagerHelper;
import org.eclipse.riena.ui.swt.facades.SWTFacade;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * This composites has a list of the top-level menus of the Riena menu bar (a cool bar with an item for every top-level menu).
 */
public class MenuCoolBarComposite extends Composite {
	static final String MENU_DATA_KEY = "Menu"; //$NON-NLS-1$

	//	private final IWorkbenchWindow window;

	private CoolItem menuCoolItem;
	private ToolBar menuToolBar;
	private CoolBar menuCoolBar;
	private final IEntriesProvider provider;
	private final ToolItemScalingHelper menuScalingHelper;

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
	 * @deprecated use {@link MenuCoolBarComposite#MenuCoolBarComposite(Composite, int, IEntriesProvider)} instead
	 */
	@Deprecated
	public MenuCoolBarComposite(final Composite parent, final int style, final IWorkbenchWindow window) {
		this(parent, style, new IEntriesProvider() {
			/**
			 * Returns the top-level menu entries.
			 */
			@SuppressWarnings("restriction")
			public IContributionItem[] getTopLevelEntries() {
				if (window instanceof WorkbenchWindow) {
					final MenuManager menuManager = ((WorkbenchWindow) window).getMenuManager();
					return menuManager.getItems();
				}
				return new IContributionItem[0];
			}
		});
	}

	/**
	 * Creates an new instance of {@code MenuCoolBarComposite} given its parent and a style value describing its behavior and appearance.
	 * 
	 * @param parent
	 *            a composite which will be the parent of the new instance (cannot be null)
	 * @param style
	 *            the style of widget to construct
	 * @param provider
	 *            the menu entries provider
	 */
	public MenuCoolBarComposite(final Composite parent, final int style, final IEntriesProvider provider) {
		super(parent, style);
		this.provider = provider;
		setLayout(new FillLayout());
		menuScalingHelper = new ToolItemScalingHelper();
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
		menuToolBar.addTraverseListener(listener);
		fillMenuBar(listener);
	}

	private void fillMenuBar(final ToolBarMenuListener listener) {
		if (provider == null) {
			return;
		}

		final IContributionItem[] listOfItems = provider.getTopLevelEntries();
		final java.util.Iterator<IContributionItem> contributionIterator = Arrays.asList(listOfItems).iterator();

		ToolBar toolBar;

		while (contributionIterator.hasNext()) {
			final IContributionItem contributionItem = contributionIterator.next();
			if (contributionItem instanceof MenuManager) {
				createAndAddMenu((MenuManager) contributionItem, listener);
				if (contributionIterator.hasNext()) {
					toolBar = ((ToolBar) this.menuCoolItem.getControl());
					if (toolBar.getItemCount() > 1) {
						final ToolItem toolItem = toolBar.getItem(toolBar.getItemCount() - 1);
						menuScalingHelper.createSeparatorForScaling(toolBar, toolItem, toolBar.getItemCount(), -1);
					}
				}
			}

		}
	}

	private Font getMenuBarFont() {
		return LnfManager.getLnf().getFont(LnfKeyConstants.MENUBAR_FONT);
	}

}
