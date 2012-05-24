package org.eclipse.riena.navigation.ui.e4.part;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuSeparator;
import org.eclipse.e4.ui.model.application.ui.menu.MOpaqueMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;
import org.eclipse.e4.ui.workbench.renderers.swt.MenuManagerRenderer;
import org.eclipse.jface.action.AbstractGroupMarker;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.internal.menus.MenuHelper;
import org.eclipse.ui.menus.CommandContributionItem;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.ui.swt.component.MenuCoolBarComposite;
import org.eclipse.riena.navigation.ui.swt.component.MenuCoolBarComposite.IEntriesProvider;
import org.eclipse.riena.navigation.ui.swt.component.TitleComposite;

/**
 * Creates the Riena header.
 * 
 * @author jdu
 * 
 */
public class HeaderPart {
	public static final String MENU_COMPOSITE_KEY = HeaderPart.class.getName() + ".rienaMenuCoolBarComposite"; //$NON-NLS-1$

	@Inject
	private MApplication application;

	@Inject
	public void create(final Composite parent, final MWindow window, final MPart part) {
		final Composite c = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().spacing(0, 0).applyTo(c);

		final TitleComposite titleComposite = new TitleComposite(c, ApplicationNodeManager.getApplicationNode());
		GridDataFactory.fillDefaults().grab(true, true).applyTo(titleComposite);

		final MenuCoolBarComposite menuCoolBarComposite = new MenuCoolBarComposite(c, SWT.NONE, new IEntriesProvider() {
			public IContributionItem[] getTopLevelMenuEntries() {
				System.err.println("HeaderPart.create(...).new IEntriesProvider() {...}.getTopLevelMenuEntries()");
				final MMenu mainMenu = window.getMainMenu();

				final MenuManager menuManager = new MenuManager();
				fill(new MenuManagerRenderer(), mainMenu, menuManager);

				return menuManager.getItems();
			}
		});
		GridDataFactory.fillDefaults().grab(true, false).applyTo(menuCoolBarComposite);
		part.getTransientData().put(MENU_COMPOSITE_KEY, menuCoolBarComposite);

		menuCoolBarComposite.setBackground(c.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		//		final Composite coolBarComposite = createCoolBarComposite(shell, menuBarComposite);
	}

	private void fill(final MenuManagerRenderer renderer, final MMenu menu, final IMenuManager manager) {
		for (final IContributionItem item : manager.getItems()) {
			if (item instanceof MenuManager) {
				final MenuManager menuManager = (MenuManager) item;
				final MMenu subMenu = MenuHelper.createMenu(menuManager);
				if (subMenu != null) {
					renderer.linkModelToContribution(subMenu, item);
					renderer.linkModelToManager(subMenu, menuManager);
					fill(renderer, subMenu, menuManager);
					menu.getChildren().add(subMenu);
				}
			} else if (item instanceof CommandContributionItem) {
				final CommandContributionItem cci = (CommandContributionItem) item;
				final MMenuItem menuItem = MenuHelper.createItem(application, cci);
				manager.remove(item);
				if (menuItem != null) {
					menu.getChildren().add(menuItem);
				}
			} else if (item instanceof ActionContributionItem) {
				final MMenuItem menuItem = MenuHelper.createItem(application, (ActionContributionItem) item);
				manager.remove(item);
				if (menuItem != null) {
					menu.getChildren().add(menuItem);
				}
			} else if (item instanceof AbstractGroupMarker) {
				final MMenuSeparator separator = MenuFactoryImpl.eINSTANCE.createMenuSeparator();
				separator.setVisible(item.isVisible());
				separator.setElementId(item.getId());
				if (item instanceof GroupMarker) {
					separator.getTags().add(MenuManagerRenderer.GROUP_MARKER);
				}
				menu.getChildren().add(separator);
				manager.remove(item);
			} else {
				final MOpaqueMenuItem menuItem = MenuFactoryImpl.eINSTANCE.createOpaqueMenuItem();
				menuItem.setElementId(item.getId());
				menuItem.setVisible(item.isVisible());
				menu.getChildren().add(menuItem);
				renderer.linkModelToContribution(menuItem, item);
			}
		}
	}
}
