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
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuContribution;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuSeparator;
import org.eclipse.e4.ui.workbench.modeling.ExpressionContext;
import org.eclipse.e4.ui.workbench.renderers.swt.HandledContributionItem;
import org.eclipse.jface.action.AbstractGroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.internal.handlers.LegacyHandlerService;
import org.eclipse.ui.internal.menus.MenuPersistence;
import org.eclipse.ui.internal.services.EvaluationService;
import org.eclipse.ui.services.IEvaluationService;

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
@SuppressWarnings("restriction")
public class HeaderPart {
	public static final String MENU_COMPOSITE_KEY = HeaderPart.class.getName() + ".rienaMenuCoolBarComposite"; //$NON-NLS-1$

	@Inject
	private MApplication application;

	@Inject
	private IEclipseContext eclipseContext;

	@Inject
	public void create(final Composite parent, final MWindow window, final MPart part) {
		final Composite c = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().spacing(0, 0).applyTo(c);

		final TitleComposite titleComposite = new TitleComposite(c, ApplicationNodeManager.getApplicationNode());
		GridDataFactory.fillDefaults().grab(true, true).applyTo(titleComposite);

		// this must run once to create application model elements for all contributions to
		// org.eclipse.ui.menus
		copyLegacyExtensionsToModel();

		final MenuCoolBarComposite menuCoolBarComposite = new MenuCoolBarComposite(c, SWT.NONE, new IEntriesProvider() {

			public IContributionItem[] getTopLevelMenuEntries() {
				final Map<String, Collection<IContributionItem>> parentIdToElement = new HashMap<String, Collection<IContributionItem>>();
				//				final Map<String, IContributionItem> idToElement = new HashMap<String, IContributionItem>();

				// e4 specific menus
				//				fill(window.getMainMenu().getChildren(), idToElement, parentIdToElement, "org.eclipse.ui.main.menu");

				// 3.x specific menus
				final ExpressionContext eContext = new ExpressionContext(eclipseContext.getParent());
				for (final MMenuContribution c : application.getMenuContributions()) {
					if (ContributionsAnalyzer.isVisible(c, eContext)) {
						fill(c.getChildren(), parentIdToElement, c.getParentId());
					}
				}

				setParentChildRelation(parentIdToElement);

				final Collection<IContributionItem> c = getOrCreateMapElement(parentIdToElement, "org.eclipse.ui.main.menu");
				return c.toArray(new IContributionItem[parentIdToElement.size()]);
			}
		});
		GridDataFactory.fillDefaults().grab(true, false).applyTo(menuCoolBarComposite);
		part.getTransientData().put(MENU_COMPOSITE_KEY, menuCoolBarComposite);

		//		menuCoolBarComposite.setBackground(c.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		//		final Composite coolBarComposite = createCoolBarComposite(shell, menuBarComposite);
	}

	private void copyLegacyExtensionsToModel() {
		new MenuPersistence(application, eclipseContext).reRead();
		eclipseContext.set(IEvaluationService.class, new EvaluationService(eclipseContext));
		new LegacyHandlerService(eclipseContext).readRegistry();
	}

	private void setParentChildRelation(final Map<String, Collection<IContributionItem>> parentIdToElement) {

		for (final Entry<String, Collection<IContributionItem>> entry : new HashMap<String, Collection<IContributionItem>>(parentIdToElement).entrySet()) {
			for (final IContributionItem e : entry.getValue()) {
				//				System.err.println(entry.getKey() + " <<< " + e.getId());
				if (e instanceof IContributionManager) {
					for (final IContributionItem child : getOrCreateMapElement(parentIdToElement, e.getId())) {
						((IContributionManager) e).add(child);
					}
				}
			}
		}
	}

	/**
	 * fill the given {@link MenuManager} with the {@link MMenu} items
	 * 
	 * @param idToElement
	 */
	private void fill(final List<MMenuElement> elements, final Map<String, Collection<IContributionItem>> parentIdToElement, final String parentId) {
		//		final List<MMenuElement> elements = source.getChildren();
		for (final MMenuElement e : elements) {
			final String label = e.getLabel();
			final String id = e.getElementId();
			final ImageDescriptor image = getImage(e.getIconURI());
			//			final String parentId = e.getParent().getElementId() != null ? e.getParent().getElementId() : "";
			if (e instanceof MMenu) {
				// => MenuManager
				getOrCreateMapElement(parentIdToElement, parentId).add(new MenuManager(label, image, id));
				//				fill(((MMenu) e).getChildren(), idToElement, parentIdToElement, e.getElementId());
			} else if (e instanceof MHandledItem) {
				// => CommandContributionItem/ActionContributionItem
				final MHandledItem m = (MHandledItem) e;

				final HandledContributionItem item = new HandledContributionItem();
				ContextInjectionFactory.inject(item, eclipseContext);
				item.setModel(m);

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

	//	/**
	//	 * @param idToElement
	//	 * @param target
	//	 * @param parentId
	//	 * @param menuManager
	//	 */
	//	private void put(final Map<String, Collection<IContributionItem>> parentToElement, final String parentId, final IContributionItem element) {
	//		Collection<IContributionItem> elements = parentToElement.get(parentId);
	//		if (elements == null) {
	//			elements = new ArrayList<IContributionItem>();
	//			parentToElement.put(parentId, elements);
	//		}
	//		elements.add(element);
	//	}

	/**
	 * @param iconURI
	 * @return
	 */
	private ImageDescriptor getImage(final String iconURI) {
		// TODO Auto-generated method stub
		return null;
	}
}
