package org.eclipse.riena.navigation.ui.e4.part;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuSeparator;
import org.eclipse.jface.action.AbstractGroupMarker;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

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
				final MMenu mainMenu = window.getMainMenu();

				final MenuManager menuManager = new MenuManager();
				fill(mainMenu, menuManager);

				final IContributionItem[] items = menuManager.getItems();
				System.err.println("HeaderPart.create(...).new IEntriesProvider() {...}.getTopLevelMenuEntries(): " + items.length);
				return items;
			}
		});
		GridDataFactory.fillDefaults().grab(true, false).applyTo(menuCoolBarComposite);
		part.getTransientData().put(MENU_COMPOSITE_KEY, menuCoolBarComposite);

		//		menuCoolBarComposite.setBackground(c.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		//		final Composite coolBarComposite = createCoolBarComposite(shell, menuBarComposite);
	}

	/**
	 * Fill the given {@link MenuManager} with the {@link MMenu} items
	 */
	private void fill(final MMenu source, final IMenuManager target) {
		for (final MMenuElement c : source.getChildren()) {
			final String label = c.getLabel();
			final String id = c.getElementId();
			final ImageDescriptor image = getImage(c.getIconURI());
			if (c instanceof MMenu) {
				// => MenuManager
				final MenuManager subMenu = new MenuManager(label, image, id);
				fill((MMenu) c, subMenu);
				target.add(subMenu);
			} else if (c instanceof MMenuItem) {
				// => CommandContributionItem/ActionContributionItem
				final ActionContributionItem item = new ActionContributionItem(new Action(label, image) {
					@Override
					public void run() {
						System.out.println("HeaderPart.fill(...).new Action() {...}.run()");
					}
				});
				item.setId(id);
				target.add(item);
			} else if (c instanceof MMenuSeparator) {
				// => AbstractGroupMarker
				final AbstractGroupMarker separator = new Separator();
				separator.setId(id);
				target.add(separator);
			}
		}

		//		for (final IContributionItem item : target.getItems()) {
		//			if (item instanceof MenuManager) {
		//				final MenuManager menuManager = (MenuManager) item;
		//				final MMenu subMenu = MenuHelper.createMenu(menuManager);
		//				if (subMenu != null) {
		//					renderer.linkModelToContribution(subMenu, item);
		//					renderer.linkModelToManager(subMenu, menuManager);
		//					fill(renderer, subMenu, menuManager);
		//					source.getChildren().add(subMenu);
		//				}
		//			} else if (item instanceof CommandContributionItem) {
		//				final CommandContributionItem cci = (CommandContributionItem) item;
		//				final MMenuItem menuItem = MenuHelper.createItem(application, cci);
		//				target.remove(item);
		//				if (menuItem != null) {
		//					source.getChildren().add(menuItem);
		//				}
		//			} else if (item instanceof ActionContributionItem) {
		//				final MMenuItem menuItem = MenuHelper.createItem(application, (ActionContributionItem) item);
		//				target.remove(item);
		//				if (menuItem != null) {
		//					source.getChildren().add(menuItem);
		//				}
		//			} else if (item instanceof AbstractGroupMarker) {
		//				final MMenuSeparator separator = MenuFactoryImpl.eINSTANCE.createMenuSeparator();
		//				separator.setVisible(item.isVisible());
		//				separator.setElementId(item.getId());
		//				if (item instanceof GroupMarker) {
		//					separator.getTags().add(MenuManagerRenderer.GROUP_MARKER);
		//				}
		//				source.getChildren().add(separator);
		//				target.remove(item);
		//			} else {
		//				final MOpaqueMenuItem menuItem = MenuFactoryImpl.eINSTANCE.createOpaqueMenuItem();
		//				menuItem.setElementId(item.getId());
		//				menuItem.setVisible(item.isVisible());
		//				source.getChildren().add(menuItem);
		//				renderer.linkModelToContribution(menuItem, item);
		//			}
		//		}
	}

	/**
	 * @param iconURI
	 * @return
	 */
	private ImageDescriptor getImage(final String iconURI) {
		// TODO Auto-generated method stub
		return null;
	}
}
