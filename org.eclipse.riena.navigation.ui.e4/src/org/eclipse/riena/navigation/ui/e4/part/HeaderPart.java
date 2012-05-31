package org.eclipse.riena.navigation.ui.e4.part;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.internal.workbench.ContributionsAnalyzer;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimElement;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuContribution;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuSeparator;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarContribution;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.e4.ui.model.application.ui.menu.MTrimContribution;
import org.eclipse.e4.ui.workbench.modeling.ExpressionContext;
import org.eclipse.e4.ui.workbench.renderers.swt.HandledContributionItem;
import org.eclipse.jface.action.AbstractGroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.internal.handlers.LegacyHandlerService;
import org.eclipse.ui.internal.menus.MenuPersistence;
import org.eclipse.ui.internal.services.EvaluationService;
import org.eclipse.ui.services.IEvaluationService;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.ui.e4.part.uielements.CoolBarComposite;
import org.eclipse.riena.navigation.ui.swt.component.IEntriesProvider;
import org.eclipse.riena.navigation.ui.swt.component.MenuCoolBarComposite;
import org.eclipse.riena.navigation.ui.swt.component.TitleComposite;

/**
 * Creates the Riena header.
 * 
 * @author jdu
 * 
 */
@SuppressWarnings("restriction")
public class HeaderPart {
	public static final String MENU_COMPOSITE_KEY = HeaderPart.class.getName() + ".rienaMenuCoolBarComposite"; //$NON-NLS-1$
	public static final String COOLBAR_COMPOSITE_KEY = HeaderPart.class.getName() + ".rienaCoolBarComposite"; //$NON-NLS-1$

	@Inject
	private MApplication application;

	@Inject
	private IEclipseContext eclipseContext;

	@Inject
	public void create(final Composite parent, final MTrimmedWindow window, final MPart part) {
		final Composite c = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().spacing(0, 0).applyTo(c);

		final TitleComposite titleComposite = new TitleComposite(c, ApplicationNodeManager.getApplicationNode());
		GridDataFactory.fillDefaults().grab(true, true).applyTo(titleComposite);

		// this must run once to create application model elements for all contributions to
		// org.eclipse.ui.menus
		copyLegacyExtensionsToModel();

		part.getTransientData().put(MENU_COMPOSITE_KEY, createMainMenu(c, window));
		part.getTransientData().put(COOLBAR_COMPOSITE_KEY, createCoolBarComposite(c, window));
	}

	private CoolBarComposite createCoolBarComposite(final Composite c, final MTrimmedWindow window) {
		return new CoolBarComposite(c, new IEntriesProvider() {
			public IContributionItem[] getTopLevelEntries() {
				final ExpressionContext eContext = new ExpressionContext(eclipseContext.getParent());
				final ArrayList<IContributionItem> items = new ArrayList<IContributionItem>();

				// the main toolbar id is "org.eclipse.ui.main.toolbar"
				// we need to find its children ids in order to filter only contributions to these children
				final List<String> parents = new ArrayList<String>();
				for (final MTrimContribution c : application.getTrimContributions()) {
					if (ContributionsAnalyzer.isVisible(c, eContext) && "org.eclipse.ui.main.toolbar".equals(c.getParentId())) {
						for (final MTrimElement e : c.getChildren()) {
							if (e instanceof MToolBar) {
								parents.add(e.getElementId());
							}
						}
					}
				}

				// now consider only contributions to the parents found above
				// other contributions (e.g. view menu contributions) will be not considered
				for (final MToolBarContribution c : application.getToolBarContributions()) {
					if (ContributionsAnalyzer.isVisible(c, eContext) && parents.contains(c.getParentId())) {
						for (final MToolBarElement e : c.getChildren()) {
							if (e instanceof MHandledItem) {
								final HandledContributionItem item = new HandledContributionItem();
								ContextInjectionFactory.inject(item, eclipseContext);
								item.setModel((MHandledItem) e);
								items.add(item);
							} else {
								System.out.println(e.getClass());
							}
						}
					}
				}
				return items.toArray(new IContributionItem[items.size()]);
			}
		});
	}

	private MenuCoolBarComposite createMainMenu(final Composite c, final MWindow window) {
		final MenuCoolBarComposite menuCoolBarComposite = new MenuCoolBarComposite(c, SWT.NONE, new IEntriesProvider() {

			public IContributionItem[] getTopLevelEntries() {
				final Map<String, Collection<IContributionItem>> parentIdToElement = new HashMap<String, Collection<IContributionItem>>();

				// e4 specific menus
				final MMenu mainMenu = window.getMainMenu();
				if (mainMenu != null) {
					fill(mainMenu.getChildren(), parentIdToElement, "org.eclipse.ui.main.menu");
				}

				// 3.x specific menus
				final ExpressionContext eContext = new ExpressionContext(eclipseContext.getParent());
				for (final MMenuContribution c : application.getMenuContributions()) {
					if (ContributionsAnalyzer.isVisible(c, eContext)) {
						fill(c.getChildren(), parentIdToElement, c.getParentId());
					}
				}

				setParentChildRelation(parentIdToElement);

				for (final IContributionItem iContributionItem : getOrCreateMapElement(parentIdToElement, "org.eclipse.ui.main.toolbar")) {
					System.err.println(iContributionItem);
				}

				final Collection<IContributionItem> c = getOrCreateMapElement(parentIdToElement, "org.eclipse.ui.main.menu");
				return c.toArray(new IContributionItem[parentIdToElement.size()]);
			}
		});
		GridDataFactory.fillDefaults().grab(true, false).applyTo(menuCoolBarComposite);
		return menuCoolBarComposite;
	}

	private void copyLegacyExtensionsToModel() {
		new MenuPersistence(application, eclipseContext).reRead();
		eclipseContext.set(IEvaluationService.class, new EvaluationService(eclipseContext));
		new LegacyHandlerService(eclipseContext).readRegistry();
	}

	private void setParentChildRelation(final Map<String, Collection<IContributionItem>> parentIdToElement) {
		for (final Entry<String, Collection<IContributionItem>> entry : new HashMap<String, Collection<IContributionItem>>(parentIdToElement).entrySet()) {
			for (final IContributionItem e : entry.getValue()) {
				if (e instanceof IContributionManager) {
					for (final IContributionItem child : getOrCreateMapElement(parentIdToElement, e.getId())) {
						((IContributionManager) e).add(child);
					}
				}
			}
		}
	}

	/**
	 * fill the given map with the {@link MMenu} items
	 * 
	 * @param idToElement
	 */
	private void fill(final List<MMenuElement> elements, final Map<String, Collection<IContributionItem>> parentIdToElement, final String parentId) {
		//		final List<MMenuElement> elements = source.getChildren();
		for (final MMenuElement e : elements) {
			final String label = e.getLabel();
			final String id = e.getElementId();
			if (e instanceof MMenu) {
				// => MenuManager
				getOrCreateMapElement(parentIdToElement, parentId).add(new MenuManager(label, id));
			} else if (e instanceof MHandledItem) {
				// => CommandContributionItem/ActionContributionItem
				final HandledContributionItem item = new HandledContributionItem();
				ContextInjectionFactory.inject(item, eclipseContext);
				item.setModel((MHandledItem) e);

				getOrCreateMapElement(parentIdToElement, parentId).add(item);
			} else if (e instanceof MMenuSeparator) {
				// => AbstractGroupMarker
				final AbstractGroupMarker separator = new Separator();
				separator.setId(id);
				getOrCreateMapElement(parentIdToElement, parentId).add(separator);
			}
		}
	}

	private Collection<IContributionItem> getOrCreateMapElement(final Map<String, Collection<IContributionItem>> parentIdToElement, final String parentId) {
		Collection<IContributionItem> elements = parentIdToElement.get(parentId);
		if (elements == null) {
			elements = new ArrayList<IContributionItem>();
			parentIdToElement.put(parentId, elements);
		}
		return elements;
	}

}
