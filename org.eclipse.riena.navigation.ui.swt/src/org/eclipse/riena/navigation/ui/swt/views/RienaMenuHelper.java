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
package org.eclipse.riena.navigation.ui.swt.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;
import org.eclipse.jface.action.ContributionManager;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.internal.provisional.action.IToolBarContributionItem;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISourceProvider;
import org.eclipse.ui.ISourceProviderListener;
import org.eclipse.ui.PlatformUI;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.core.Activator;
import org.eclipse.riena.internal.core.StartupsSafeRunnable;
import org.eclipse.riena.internal.ui.ridgets.swt.AbstractItemRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.IContributionExtension.ICommandExtension;
import org.eclipse.riena.internal.ui.ridgets.swt.ToolItemScalingHelper;
import org.eclipse.riena.internal.ui.swt.facades.WorkbenchFacade;
import org.eclipse.riena.navigation.ui.swt.component.MenuCoolBarComposite;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.controller.IController;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.uibinding.DefaultBindingManager;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingManager;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingPropertyLocator;
import org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * @since 5.0
 * 
 */
public class RienaMenuHelper {

	private static IBindingManager menuItemBindingManager;
	private static int itemId = 0;
	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), StartupsSafeRunnable.class);
	private final List<Object> uiControls;

	/**
	 * Creates a new instance of this helper class and creates binding manager.
	 */
	public RienaMenuHelper() {
		uiControls = new ArrayList<Object>();
		if (menuItemBindingManager == null) {
			menuItemBindingManager = createMenuItemBindingManager(SWTBindingPropertyLocator.getInstance(), SwtControlRidgetMapper.getInstance());
		}
		Wire.instance(this).andStart();
	}

	private IBindingManager createMenuItemBindingManager(final IBindingPropertyLocator propertyStrategy, final IControlRidgetMapper<Object> mapper) {
		return new DefaultBindingManager(propertyStrategy, mapper);
	}

	/**
	 * Creates Ridgets for the menu items and the cool bar items and binds the Ridgets of the items with the UI widgets.
	 * 
	 * @param controller
	 *            the controller for all the ridgets of the menu and the tool bar.
	 */
	public void bindMenuAndToolItems(final IController controller, final Composite menuParent, final Composite toolbarParent) {
		createItemRidgets(controller, menuParent, toolbarParent);
		menuItemBindingManager.bind(controller, getUIControls());
	}

	/**
	 * Unbinds uiControls from their ridgets.
	 * 
	 * @param controller
	 *            the controller for all the ridgets of the menu and the tool bar.
	 */
	public void unbind(final IController controller) {
		menuItemBindingManager.unbind(controller, getUIControls());
	}

	private final List<ICommandExtension> items = new ArrayList<ICommandExtension>();

	/**
	 * Creates for every menu item and tool item a ridget and adds
	 * 
	 * @param controller
	 */
	private void createItemRidgets(final IController controller, final Composite menuParent, final Composite toolbarParent) {

		final List<IRidget> ridgetsToRemove = new ArrayList<IRidget>();
		final Collection<? extends IRidget> ridgets = controller.getRidgets();
		for (final IRidget ridget : ridgets) {
			if (ridget instanceof AbstractItemRidget) {
				ridgetsToRemove.add(ridget);
			}
		}
		ridgets.removeAll(ridgetsToRemove);

		// items of Riena "menu bar"
		final List<MenuCoolBarComposite> menuCoolBarComposites = getMenuCoolBarComposites(menuParent);
		for (final MenuCoolBarComposite menuBarComp : menuCoolBarComposites) {
			createRidgetsForItems(menuBarComp.getTopLevelItems(), controller);
		}

		//update toolbar
		if (PlatformUI.isWorkbenchRunning()) {
			try {
				final ICoolBarManager coolBarManager2 = ((ApplicationWindow) PlatformUI.getWorkbench().getActiveWorkbenchWindow()).getCoolBarManager2();
				final ContributionManager toolbarManager2 = (ContributionManager) ((IToolBarContributionItem) coolBarManager2.getItems()[0])
						.getToolBarManager();
				toolbarManager2.update(true);
			} catch (final Exception e) {
				LOGGER.log(LogService.LOG_WARNING, "the toolbar could not be updated:" + e.getLocalizedMessage() + "\n"); //$NON-NLS-1$
				for (final StackTraceElement element : e.getStackTrace()) {
					LOGGER.log(LogService.LOG_WARNING, element.toString());
				}
			}

		}

		// create Separator for Toolbar
		final ToolItemScalingHelper toolBarScalingHelper = new ToolItemScalingHelper();
		if (toolBarScalingHelper.needScaleBasedSpacing()) {
			final List<CoolBar> coolBars = getCoolBars(toolbarParent);
			for (final CoolBar coolBar : coolBars) {
				final List<ToolBar> toolBars = getToolBars(coolBar);
				for (final ToolBar toolBar : toolBars) {
					toolBarScalingHelper.createSeparatorContributionsForToolBars(toolBars);
				}
			}
		}

		// items of cool bar
		final List<ToolItem> toolItems = getAllToolItems(toolbarParent);
		for (final ToolItem toolItem : toolItems) {
			createRidget(controller, toolItem);
		}
	}

	/**
	 * Returns the composites that contains the menu bar of the sub-application.
	 * 
	 * @param composite
	 * @return composite with menu bar
	 */
	public List<MenuCoolBarComposite> getMenuCoolBarComposites(final Composite composite) {

		final List<MenuCoolBarComposite> composites = new ArrayList<MenuCoolBarComposite>();
		if (SwtUtilities.isDisposed(composite)) {
			return composites;
		}

		if (composite instanceof MenuCoolBarComposite) {
			composites.add((MenuCoolBarComposite) composite);
		}

		final Control[] children = composite.getChildren();
		for (final Control child : children) {
			if (child instanceof Composite) {
				composites.addAll(getMenuCoolBarComposites((Composite) child));
			}
		}

		return composites;
	}

	private void createRidgetsForItems(final List<ToolItem> toolItems, final IController controller) {
		for (final ToolItem toolItem : toolItems) {
			if (!SWTBindingPropertyLocator.getInstance().hasBindingProperty(toolItem)) {
				createRidget(controller, toolItem);
			}
			if (toolItem.getData() instanceof MenuManager) {
				final MenuManager manager = (MenuManager) toolItem.getData();
				createRidget(controller, manager.getMenu());
			}
		}
	}

	/**
	 * Creates for the given item a ridget and adds it to the given controller.
	 * 
	 * @param controller
	 * @param item
	 */
	private void createRidget(final IController controller, final Item item) {

		if (item.isDisposed()) {
			return;
		}

		if (isSeparator(item)) {
			// no ridget for separator
			// and
			// no ridget for tool items with control 
			// (both tool items has the style SWT.SEPARATOR)
			return;
		}

		String id;
		if (item instanceof MenuItem) {
			id = getItemId((MenuItem) item);
		} else {
			id = getItemId((ToolItem) item);
		}
		if (StringUtils.isEmpty(id)) {
			return;
		}

		final IRidget ridget = menuItemBindingManager.createRidget(item);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(item, id);

		getUIControls().add(item);
		controller.addRidget(id, ridget);

		if (item instanceof MenuItem) {
			final MenuItem menuItem = (MenuItem) item;
			createRidget(controller, menuItem.getMenu());
		}
	}

	private void createRidget(final IController controller, final Menu menu) {
		if (menu == null) {
			return;
		}

		final MenuItem[] items = menu.getItems();
		for (final MenuItem item : items) {
			createRidget(controller, item);
		}
	}

	private boolean isSeparator(final Item item) {
		return (item.getStyle() & SWT.SEPARATOR) == SWT.SEPARATOR;
	}

	/**
	 * Returns all items of all cool bars.
	 * 
	 * @return list of tool items
	 */
	private List<ToolItem> getAllToolItems(final Composite parentComposite) {
		final List<ToolItem> items = new ArrayList<ToolItem>();

		final List<CoolBar> coolBars = getCoolBars(parentComposite);
		for (final CoolBar coolBar : coolBars) {
			final List<ToolBar> toolBars = getToolBars(coolBar);
			for (final ToolBar toolBar : toolBars) {
				items.addAll(Arrays.asList(toolBar.getItems()));
			}
		}
		final List<ToolBar> toolBars = getToolBars(parentComposite);
		for (final ToolBar toolBar : toolBars) {
			items.addAll(Arrays.asList(toolBar.getItems()));
		}

		return items;
	}

	/**
	 * Returns all cool bars below the given composite (except cool bar of menu).
	 * 
	 * @param composite
	 * @return list of cool bars
	 */
	private List<CoolBar> getCoolBars(final Composite composite) {
		final List<CoolBar> coolBars = new ArrayList<CoolBar>();
		if (composite == null) {
			return coolBars;
		}

		final Control[] children = composite.getChildren();
		for (final Control child : children) {
			if (child instanceof CoolBar) {
				if (getParentOfType(child, MenuCoolBarComposite.class) == null) {
					coolBars.add((CoolBar) child);
				}
				continue;
			}
			if (child instanceof Composite) {
				coolBars.addAll(getCoolBars((Composite) child));
			}
		}

		return coolBars;
	}

	/**
	 * Returns all tool bars of the given cool bar.
	 * 
	 * @param parentComposite
	 *            cool bar
	 * @return list of tool bars
	 */
	private List<ToolBar> getToolBars(final Composite parentComposite) {
		final List<ToolBar> toolBars = new ArrayList<ToolBar>();
		if (parentComposite == null) {
			return toolBars;
		}

		final Control[] children = parentComposite.getChildren();
		for (final Control child : children) {
			if (child instanceof ToolBar) {
				if (getParentOfType(child, MenuCoolBarComposite.class) == null) {
					toolBars.add((ToolBar) child);
				}
			}
		}

		return toolBars;
	}

	private Composite getParentOfType(final Control control, final Class<? extends Control> clazz) {
		final Composite parent = control.getParent();
		if (parent == null) {
			return null;
		}
		if (clazz.isAssignableFrom(parent.getClass())) {
			return parent;
		}
		return getParentOfType(parent, clazz);
	}

	/**
	 * Returns the identifier of this contribution item.
	 * 
	 * @param item
	 * @return identifier, or {@code null} if none
	 */
	private String getItemId(final Item item, final String prefix) {
		String id = null;
		if (item.getData() instanceof IContributionItem) {
			final IContributionItem contributionItem = (IContributionItem) item.getData();
			id = contributionItem.getId();
		}
		if (StringUtils.isEmpty(id)) {
			id = SWTBindingPropertyLocator.getInstance().locateBindingProperty(item);
		}
		if (StringUtils.isEmpty(id)) {
			id = Integer.toString(++itemId);
		} else {
			if (!id.startsWith(prefix)) {
				id = prefix + id;
			}
		}
		return id;
	}

	/**
	 * Returns the identifier of the given menu item.
	 * 
	 * @param item
	 *            menu item
	 * @return identifier, or {@code null} if none
	 */
	private String getItemId(final MenuItem item) {
		return getItemId(item, IActionRidget.BASE_ID_MENUACTION);
	}

	/**
	 * Returns the identifier of the given tool item.
	 * 
	 * @param item
	 *            tool item
	 * @return identifier, or {@code null} if none
	 */
	private String getItemId(final ToolItem item) {
		return getItemId(item, IActionRidget.BASE_ID_TOOLBARACTION);
	}

	private List<Object> getUIControls() {
		return uiControls;
	}

	/**
	 * Adds the given listener to every service provider.
	 * 
	 * @param listener
	 *            The listener to add; must not be <code>null</code>.
	 */
	public void addSourceProviderListener(final ISourceProviderListener listener) {
		final ISourceProvider[] sourceProviders = getSourceProviders();
		for (final ISourceProvider sourceProvider : sourceProviders) {
			sourceProvider.addSourceProviderListener(listener);
		}
	}

	/**
	 * Removes the given listener from every service provider.
	 * 
	 * @param listener
	 *            The listener to remove; must not be <code>null</code>.
	 */
	public void removeSourceProviderListener(final ISourceProviderListener listener) {
		final ISourceProvider[] sourceProviders = getSourceProviders();
		for (final ISourceProvider sourceProvider : sourceProviders) {
			sourceProvider.removeSourceProviderListener(listener);
		}
	}

	public ISourceProvider[] getSourceProviders() {
		return WorkbenchFacade.getInstance().getSourceProviders();
	}
}