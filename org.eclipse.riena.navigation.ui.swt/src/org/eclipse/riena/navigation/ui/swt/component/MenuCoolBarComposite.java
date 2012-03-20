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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
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
 * This composites has a list of the top-level menus of the Riena menu bar (a
 * cool bar with an item for every top-level menu).
 */
public class MenuCoolBarComposite extends Composite {

	private static final String MENU_DATA_KEY = "Menu"; //$NON-NLS-1$

	private final IWorkbenchWindow window;

	private CoolItem menuCoolItem;
	private ToolBar menuToolBar;
	private CoolBar menuCoolBar;

	// private final Map<MenuManager, MenuManagerState> menuManagerOldState;

	/**
	 * Creates an new instance of {@code MenuCoolBarComposite} given its parent
	 * and a style value describing its behavior and appearance.
	 * 
	 * @param parent
	 *            a composite which will be the parent of the new instance
	 *            (cannot be null)
	 * @param style
	 *            the style of widget to construct
	 * @param window
	 *            an IWorkbenchWindow instance, used to determine the menu
	 *            entries. May be null.
	 * @since 3.0
	 */
	public MenuCoolBarComposite(final Composite parent, final int style, final IWorkbenchWindow window) {
		super(parent, style);
		setLayout(new FillLayout());
		this.window = window;
		// menuManagerOldState = new HashMap<MenuManager, MenuManagerState>();
		create();
	}

	public List<ToolItem> getTopLevelItems() {
		final ToolItem[] toolItems = menuToolBar.getItems();
		return Arrays.asList(toolItems);
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
	 */
	private ToolItem createAndAddMenu(final MenuManager menuManager) {
		if (menuManager.isVisible()) {
			final ToolItem toolItem = new ToolItem(menuToolBar, SWT.CHECK);
			toolItem.setText(menuManager.getMenuText());
			final MenuManagerHelper helper = new MenuManagerHelper();
			final Menu menu = helper.createMenu(menuToolBar, toolItem, menuManager);
			toolItem.setData(MENU_DATA_KEY, menu);
			calcSize(menuCoolItem);
			return toolItem;
		}
		return null;
	}

	/**
	 * Creates the cool and tool bar. These build the menu bar of the Riena
	 * sub-application.
	 */
	private void create() {
		menuCoolBar = new CoolBar(this, SWT.FLAT);
		menuCoolItem = CoolbarUtils.initCoolBar(menuCoolBar, getMenuBarFont());
		menuToolBar = (ToolBar) menuCoolItem.getControl();
		SWTFacade.getDefault().addMouseMoveListener(menuToolBar, new ToolBarMouseListener());
		fillMenuBar();
	}

	private void fillMenuBar() {
		for (final IContributionItem contribItem : getTopLevelMenuEntries()) {
			if (contribItem instanceof MenuManager) {
				final MenuManager topMenuManager = (MenuManager) contribItem;
				createAndAddMenu(topMenuManager);
				// menuManagerOldState.put(topMenuManager, new MenuManagerState(topMenuManager));
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

	// helping classes
	//////////////////

	/**
	 * If the mouse moves over an unselected item of the tool bar and another
	 * item was selected, deselect the other item and select the item below the
	 * mouse pointer.<br>
	 * <i>Does not work, if menu is visible.</i>
	 */
	private static final class ToolBarMouseListener implements MouseMoveListener {
		public void mouseMove(final MouseEvent e) {
			if (e.getSource() instanceof ToolBar) {
				final ToolBar toolBar = (ToolBar) e.getSource();

				ToolItem selectedItem = null;
				final ToolItem[] items = toolBar.getItems();
				for (final ToolItem item : items) {
					if (item.getSelection()) {
						selectedItem = item;
					}
				}

				final ToolItem hoverItem = toolBar.getItem(new Point(e.x, e.y));
				if (hoverItem != null) {
					if (!hoverItem.getSelection() && (selectedItem != null)) {
						selectedItem.setSelection(false);
						hoverItem.setSelection(true);
					}
				}
			}
		}
	}

	public List<ToolItem> updateMenuItems() {

		if (menuCoolBar != null) {
			menuCoolBar.dispose();
		}
		create();
		layout();
		return Collections.emptyList();

		//		final List<MenuManager> changedTopMenuManager = getChangedVisibilityItems();
		//		final List<ToolItem> changedMenus = new ArrayList<ToolItem>();
		//		if (!changedTopMenuManager.isEmpty()) {
		//			if (menuCoolBar != null) {
		//				menuCoolBar.dispose();
		//			}
		//			create();
		//			layout();
		//
		//			for (final ToolItem contribItem : getTopLevelItems()) {
		//				for (final MenuManager manager : changedTopMenuManager) {
		//					if (manager.getMenuText().equals(contribItem.getText())) {
		//						changedMenus.add(contribItem);
		//					}
		//				}
		//			}
		//		}
		//		return changedMenus;
	}

	//	private List<MenuManager> getChangedVisibilityItems() {
	//		final List<MenuManager> changedItems = new ArrayList<MenuManager>();
	//		for (final IContributionItem contribItem : getTopLevelMenuEntries()) {
	//			if (contribItem instanceof MenuManager) {
	//				final MenuManager topMenuManager = (MenuManager) contribItem;
	//				String oldToString = null;
	//				final Menu menu = getMenu(topMenuManager);
	//				if (!SwtUtilities.isDisposed(menu)) {
	//					oldToString = getMenu(topMenuManager).toString();
	//				}
	//				topMenuManager.updateAll(true);
	//				String newToString = null;
	//				if (!SwtUtilities.isDisposed(getMenu(topMenuManager))) {
	//					newToString = getMenu(topMenuManager).toString();
	//				}
	//				final MenuManagerState oldState = menuManagerOldState.get(topMenuManager);
	//				if (oldState.isChanged(topMenuManager) || !StringUtils.equals(oldToString, newToString)) {
	//					changedItems.add(topMenuManager);
	//				}
	//			}
	//		}
	//		return changedItems;
	//	}

	//	private Menu getMenu(final MenuManager topMenuManager) {
	//
	//		if (SwtUtilities.isDisposed(menuCoolBar)) {
	//			return null;
	//		}
	//		for (final Control menuBarChild : menuCoolBar.getChildren()) {
	//			if (menuBarChild instanceof ToolBar) {
	//				for (final ToolItem item : ((ToolBar) menuBarChild).getItems()) {
	//					if (item.getData().equals(topMenuManager)) {
	//						return (Menu) item.getData(MENU_DATA_KEY);
	//					}
	//				}
	//			}
	//		}
	//
	//		return null;
	//	}
	//
	//	private class MenuManagerState {
	//		private final String menuText;
	//		private final boolean visible;
	//		private final boolean enabled;
	//		private final int itemCount;
	//
	//		public MenuManagerState(final MenuManager menuManager) {
	//			this.menuText = menuManager.getMenuText();
	//			this.visible = menuManager.isVisible();
	//			this.enabled = menuManager.isEnabled();
	//			this.itemCount = menuManager.getMenu() != null ? menuManager.getMenu().getItemCount() : -1;
	//		}
	//
	//		private boolean isChanged(final MenuManager menuManager) {
	//			return menuManager.getMenu() == null || //
	//					itemCount == -1 || itemCount != menuManager.getMenu().getItemCount() || //
	//					visible != menuManager.isVisible() || enabled != menuManager.isEnabled() || //
	//					!menuManager.getMenuText().equals(menuText);
	//		}
	//	}
}
