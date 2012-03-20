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
package org.eclipse.riena.navigation.ui.swt.presentation.stack;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.presentations.IPresentablePart;
import org.eclipse.ui.presentations.IStackPresentationSite;
import org.eclipse.ui.presentations.StackDropResult;
import org.eclipse.ui.presentations.StackPresentation;

import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.listener.NavigationTreeObserver;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.navigation.ui.controllers.SubApplicationController;
import org.eclipse.riena.navigation.ui.swt.ApplicationUtility;
import org.eclipse.riena.navigation.ui.swt.component.SubApplicationSwitcherWidget;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ModuleGroupRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.SubModuleViewRenderer;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewProvider;
import org.eclipse.riena.navigation.ui.swt.views.AbstractNavigationCompositeDeligation;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.EmbeddedTitleBar;
import org.eclipse.riena.ui.swt.UIConstants;
import org.eclipse.riena.ui.swt.facades.SWTFacade;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;

/**
 * <pre>
 * +-----------------------------------------------------------------+
 * |                                                                 |
 * |                  +---------+---------+                          |
 * |                  | SubApp1 | SubApp2 |                          |
 * +-----------------------------------------------------------------+
 * | *1           *2                                                 |
 * | +---------+  +------------------------------------------------+ |
 * | | Module1 |  | Module1 - Sub1                                 | |
 * | +---------+  +------------------------------------------------+ |
 * | | o Sub1  |  |                                                | |
 * | | o Sub2  |  | *3                                             | |
 * | +---------+  |                                                | |
 * | | Module2 |  |                                                | |
 * | +---------+  |                                                | |
 * | +---------+  |                                                | |
 * | | Module2 |  |                                                | |
 * | +---------+  |                                                | |
 * |              |                                                | |
 * |              |                                                | |
 * |              +------------------------------------------------+ |
 * +-----------------------------------------------------------------+
 * 
 * legend: *1 - navigation
 *         *2 - sub-module view (with title bar and border)
 *         *3 - content area of the sub-module view (active sub module)
 * </pre>
 */
public class TitlelessStackPresentation extends StackPresentation {
	private static boolean navigationVisible;

	/**
	 * @since 3.0
	 * 
	 */
	public static final String DATA_KEY_CONTENT_COMPOSITE = UIConstants.DATA_KEY_CONTENT_COMPOSITE;
	/**
	 * Property to distinguish the view of the navigation.
	 */
	public static final String PROPERTY_NAVIGATION = "navigation"; //$NON-NLS-1$
	/**
	 * @since 3.0
	 */
	public static final String PROPERTY_SUBAPPLICATION_NODE = "subapplication.node"; //$NON-NLS-1$

	/**
	 * Property to distinguish the view of the status line.
	 */
	public static final String PROPERTY_STATUSLINE = "statusLine"; //$NON-NLS-1$

	/**
	 * Left padding of the navigation.<br>
	 * Gap between left shell border and navigation.
	 * 
	 * @since 1.2
	 */
	public static final int DEFAULT_PADDING_LEFT = 2;

	/**
	 * Right padding of the sub-module view.<br>
	 * Gap between right shell border and sub-module view.
	 * 
	 * @since 1.2
	 */
	public static final int DEFAULT_PADDING_RIGHT = DEFAULT_PADDING_LEFT;

	/**
	 * Bottom padding of the navigation/the sub-module view.<br>
	 * Gap between bottom shell border and sub-module view.
	 */
	public static final int PADDING_BOTTOM = 2;

	/**
	 * Gap between navigation and sub-module view
	 * 
	 * @since 1.2
	 */
	public static final int DEFAULT_NAVIGATION_SUB_MODULE_GAP = 4;

	private final Set<IPresentablePart> knownParts = new HashSet<IPresentablePart>();
	private IPresentablePart current;
	private IPresentablePart navigation;
	private final Composite parent;
	private Composite placeHolder;
	private SubModuleViewRenderer renderer;
	private boolean hasListener;

	public TitlelessStackPresentation(final Composite parent, final IStackPresentationSite stackSite) {
		super(stackSite);
		this.parent = new Composite(parent, SWT.DOUBLE_BUFFERED);
		createPlaceHolder(parent);
		createSubModuleViewArea();
		if (isNavigationFastViewEnabled()) {
			final CloseNavigationMouseListener closeNavigationMouseListener = new CloseNavigationMouseListener();
			parent.getDisplay().addFilter(SWT.MouseDown, closeNavigationMouseListener);
		}
	}

	private void createPlaceHolder(final Composite parent) {
		placeHolder = new Composite(parent, SWT.NONE);
		placeHolder.setLayout(new FormLayout());
		placeHolder.setBounds(0, 0, 0, 0);
		placeHolder.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
	}

	@Override
	public void addPart(final IPresentablePart newPart, final Object cookie) {
		initializeSubModuleChangeListener();
		if (isNavigation(newPart)) {
			navigation = newPart;
		}
		knownParts.add(newPart);
	}

	private void observeNavigation() {
		final NavigationTreeObserver observer = new NavigationTreeObserver();
		observer.addListener(new StackPresentationSubModuleListener());
		observer.addListenerTo(getSubApplicationNode());
	}

	/**
	 * Pre- and after- hooking of {@link SubModuleView} - layouting
	 */
	private class StackPresentationSubModuleListener extends SubModuleNodeListener {

		@Override
		public void beforeActivated(final ISubModuleNode source) {
			if (current != null) {
				placeHolder.setBounds(calcPlaceHolderBounds());
				placeHolder.setVisible(true);
				placeHolder.moveAbove(current != null ? current.getControl() : null);
				placeHolder.update();
			}
		}

		@Override
		public void afterActivated(final ISubModuleNode source) {
			current.setBounds(calcSubModuleInnerBounds());
			current.setVisible(true);
			if (ApplicationUtility.isNavigationFastViewEnabled()) {
				setNavigationVisible(navigationVisible);
			}
			hideComposite(placeHolder);
			redrawSubModuleTitle();
			current.getControl().moveAbove(placeHolder != null ? placeHolder : null);
		}

		@Override
		public void afterDeactivated(final ISubModuleNode source) {
			super.afterDeactivated(source);
			if (ApplicationUtility.isNavigationFastViewEnabled()) {
				navigationVisible = isNavigationVisible();
			}
		}

	}

	private void hideComposite(final Control control) {
		if (control == null || control.isDisposed()) {
			return;
		}
		control.setBounds(new Rectangle(0, 0, 0, 0));
		control.setVisible(false);
	}

	private Rectangle calcPlaceHolderBounds() {
		final Rectangle tmp = calcSubModuleInnerBounds();
		Rectangle placeHolderBounds = null;
		final EmbeddedTitleBar tb = (EmbeddedTitleBar) locateControl(current.getControl(),
				StackPresentationControlFilter.TITLE_BAR_FILTER);
		if (tb != null) {
			placeHolderBounds = new Rectangle(tmp.x, tmp.y + tb.getBounds().height, tmp.width, tmp.height);
		} else {
			placeHolderBounds = new Rectangle(tmp.x, tmp.y + 10, tmp.width, tmp.height);
		}
		return placeHolderBounds;
	}

	private Control locateControl(final Control ctrl, final StackPresentationControlFilter filter) {
		if (ctrl == null) {
			return null;
		}
		if (filter.accept(ctrl)) {
			return ctrl;
		}
		if (ctrl instanceof Composite) {
			final Control[] children = ((Composite) ctrl).getChildren();
			for (final Control child : children) {
				final Control result = locateControl(child, filter);
				if (result != null) {
					return result;
				}
			}
		}

		return null;
	}

	/**
	 * @since 4.0
	 */
	public ISubApplicationNode getSubApplicationNode() {
		if (navigation == null) {
			return null;
		}
		return (ISubApplicationNode) findDataObject(navigation.getControl(), PROPERTY_SUBAPPLICATION_NODE);
	}

	private Object findDataObject(final Control control, final String dataKey) {
		Object data = control.getData(dataKey);
		if (data != null) {
			return data;
		}

		if (control instanceof Composite) {
			final Composite composite = (Composite) control;

			final Control[] children = composite.getChildren();
			final int childCount = children.length;
			for (int i = 0; i < childCount && data == null; i++) {
				data = findDataObject(children[i], dataKey);
			}
		}
		return data;
	}

	@Override
	public void selectPart(final IPresentablePart toSelect) {
		if (current == toSelect) {
			return;
		}
		if (isNavigation(toSelect)) {
			selectNavigation(toSelect);
		} else if (toSelect != null) {
			current = toSelect;
		}
	}

	/**
	 * @param toSelect
	 */
	private void selectNavigation(final IPresentablePart toSelect) {
		observeNavigation();
		final Rectangle navi = calcNavigationBounds(parent);
		toSelect.setBounds(navi);
		redrawSubModuleTitle();
		if (!isNavigationFastViewEnabled()) {
			toSelect.setVisible(true);
		}
	}

	/**
	 * This is called when the window is resized.
	 * <ol>
	 * <li>compute new size for navigation</li>
	 * <li>all non-current views are set to size zero (until they become current
	 * and are shown)</li>
	 * <li>the current view is resized</li>
	 * </ol>
	 */
	@Override
	public void setBounds(final Rectangle bounds) {
		parent.setBounds(bounds);
		if (navigation != null) {
			final Rectangle navi = calcNavigationBounds(parent);
			navigation.setBounds(navi);
		}
		if (current != null) {
			final Rectangle innerBounds = calcSubModuleInnerBounds();
			for (final IPresentablePart part : knownParts) {
				if (part != current && !isNavigation(part)) {
					part.setBounds(new Rectangle(0, 0, 0, 0));
				}
			}
			current.setBounds(innerBounds);
		}
		parent.setVisible(true);
	}

	@Override
	public void removePart(final IPresentablePart oldPart) {
		if (isNavigation(oldPart)) {
			navigation = null;
		} else if (oldPart == current) {
			current = null;
		}
		knownParts.remove(oldPart);
	}

	@Override
	public void dispose() {
		if (getRenderer() != null) {
			getRenderer().dispose();
		}
	}

	/**
	 * This presentation does not support drag and drop.
	 * 
	 * @see org.eclipse.ui.presentations.StackPresentation#dragOver(org.eclipse.swt.widgets.Control,
	 *      org.eclipse.swt.graphics.Point)
	 */
	@Override
	public StackDropResult dragOver(final Control currentControl, final Point location) {
		return null;
	}

	@Override
	public Control getControl() {
		return parent;
	}

	/**
	 * Return the control of the active part, so that the correct TAB-ordering
	 * can be set.
	 */
	@Override
	public Control[] getTabList(final IPresentablePart part) {
		if (current != null) {
			return new Control[] { current.getControl() };
		}
		return new Control[] {};
	}

	@Override
	public void setActive(final int newState) {
		/*
		 * Be very care careful what you do here, to avoid causing flicker. This
		 * method may be called with AS_INACTIVE and AS_ACTIVE states repeatedly
		 * for the same part.
		 */
	}

	/**
	 * The state (minimized, maximized or restored) is not relevant for the
	 * <code>TitlelessStackPresentation</code>.
	 * 
	 * @see org.eclipse.ui.presentations.StackPresentation#setState(int)
	 */
	@Override
	public void setState(final int state) {
		// nothing to do
	}

	@Override
	public void setVisible(final boolean isVisible) {
		parent.setVisible(isVisible);
	}

	@Override
	public void showPaneMenu() {
		// nothing to do
	}

	@Override
	public void showSystemMenu() {
		// nothing to do
	}

	// helping methods
	// ////////////////

	/**
	 * Calculates the inner (i.e. usable) bounds of the sub-module view.
	 * 
	 * @param part
	 * 
	 * @return inner bounds sub-module view
	 */
	private Rectangle calcSubModuleInnerBounds() {
		return getSubModuleViewRenderer().computeInnerBounds(calcSubModuleOuterBounds());
	}

	/**
	 * Calculates the bounds of the sub-module view.
	 * 
	 * @return outer bounds sub-module view
	 */
	private Rectangle calcSubModuleOuterBounds() {
		final Rectangle naviBounds = calcNavigationBounds(parent);

		final int x = isNavigationFastViewEnabled() ? getShellSubModuleGap() : naviBounds.x + naviBounds.width
				+ getNavigationSubModuleGap();
		final int y = naviBounds.y;
		final int width = parent.getBounds().width - x - getShellSubModuleGap();
		final int height = naviBounds.height;
		final Rectangle outerBounds = new Rectangle(x, y, width, height);

		return outerBounds;
	}

	/**
	 * Calculates the bounds of the navigation on the left side.
	 * 
	 * @return bounds of navigation
	 */
	public static Rectangle calcNavigationBounds(final Composite parent) {
		final GC gc = new GC(parent);
		try {
			final Point size = getModuleGroupRenderer().computeSize(gc, SWT.DEFAULT, SWT.DEFAULT);
			final int x = getShellNavigationGap();
			final int width = size.x
					+ (isNavigationFastViewEnabled() ? 2 * AbstractNavigationCompositeDeligation.BORDER_MARGIN : 0);
			final int height = parent.getBounds().height - PADDING_BOTTOM
					- LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.STATUSLINE_HEIGHT);
			return new Rectangle(x, 0, width, height);
		} finally {
			gc.dispose();
		}
	}

	private static boolean isNavigationFastViewEnabled() {
		return ApplicationUtility.isNavigationFastViewEnabled();
	}

	/**
	 * Creates the area within witch the view of the current active sub-module
	 * is displayed.
	 */
	private void createSubModuleViewArea() {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		SWTFacade.getDefault().addPaintListener(parent, new PaintListener() {
			/**
			 * Paints the border of the current active sub-module.
			 */
			public void paintControl(final PaintEvent e) {
				if (current != null) {
					final SubModuleViewRenderer viewRenderer = getRenderer();
					if (viewRenderer != null) {
						final Rectangle bounds = calcSubModuleOuterBounds();
						viewRenderer.setBounds(bounds);
						viewRenderer.paint(e.gc, null);
					}
				}
			}
		});

	}

	/**
	 * Returns the currently active page.
	 */
	private IWorkbenchPage getActivePage() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}

	/**
	 * Returns the renderer of the sub-module view.<br>
	 * Renderer renders the border of the sub-module view and not the content of
	 * the view.
	 * 
	 * @return renderer of sub-module view
	 */
	private SubModuleViewRenderer getRenderer() {
		if (renderer == null) {
			renderer = (SubModuleViewRenderer) LnfManager.getLnf().getRenderer("SubModuleView.renderer"); //$NON-NLS-1$
		}
		return renderer;
	}

	/**
	 * Returns the renderer of a module group.
	 */
	private static ModuleGroupRenderer getModuleGroupRenderer() {
		return (ModuleGroupRenderer) LnfManager.getLnf().getRenderer(LnfKeyConstants.MODULE_GROUP_RENDERER);
	}

	/**
	 * Returns the renderer of the sub module view
	 */
	private SubModuleViewRenderer getSubModuleViewRenderer() {
		return (SubModuleViewRenderer) LnfManager.getLnf().getRenderer(LnfKeyConstants.SUB_MODULE_VIEW_RENDERER);
	}

	/**
	 * Install a sub module change listener with the navigation tree, that will
	 * repaint the sub module title when the node has changed.
	 * <p>
	 * This is necessary because when "shared" nodes (i.e. parts) are selected,
	 * we do <b>not</b> receive a {@link #selectPart(IPresentablePart)}
	 * notification if the part is <b>already</b> selected. However the shared
	 * parts may still have different titles.
	 */
	private synchronized void initializeSubModuleChangeListener() {
		if (hasListener) {
			return;
		}
		final SubApplicationController controller = getSubApplicationController();
		if (controller != null) {
			final NavigationTreeObserver navigationTreeObserver = new NavigationTreeObserver();
			navigationTreeObserver.addListener(new SubModuleNodeListener() {
				@Override
				public void activated(final ISubModuleNode source) {
					redrawSubModuleTitle();
				}
			});
			navigationTreeObserver.addListenerTo(controller.getNavigationNode());
			hasListener = true;
		}
	}

	/**
	 * Return the SubApplicationController instance for this presentation.
	 * 
	 * @return a SubApplicationController instance or null
	 */
	private SubApplicationController getSubApplicationController() {
		SubApplicationController result = null;
		final IWorkbenchPage page = getActivePage();
		if (page != null) {
			final String id = page.getPerspective().getId();
			final ISubApplicationNode subApplication = SwtViewProvider.getInstance().getNavigationNode(id,
					ISubApplicationNode.class);
			result = (SubApplicationController) subApplication.getNavigationNodeController();
		}
		return result;
	}

	/**
	 * Returns true if the given part is the navigation tree.
	 */
	private boolean isNavigation(final IPresentablePart part) {
		return part.getPartProperty(PROPERTY_NAVIGATION) != null;
	}

	/**
	 * Redraws the custom sub module title contained in the parent
	 */
	private void redrawSubModuleTitle() {
		if (parent != null && !parent.isDisposed()) {
			parent.redraw();
		}
	}

	/**
	 * Returns the gap between the right side of the navigation and the left
	 * side of the active sub module.
	 * 
	 * @return gap
	 */
	private int getNavigationSubModuleGap() {
		final RienaDefaultLnf lnf = LnfManager.getLnf();
		return lnf.getIntegerSetting(LnfKeyConstants.NAVIGATION_SUB_MODULE_GAP, DEFAULT_NAVIGATION_SUB_MODULE_GAP);
	}

	/**
	 * Returns the gap between the border of the shell and the left side of the
	 * navigation.<br>
	 * <i>Note: The shell has also a padding (
	 * {@linkplain LnfKeyConstants.TITLELESS_SHELL_PADDING}).</i>
	 * 
	 * @return gap
	 */
	private static int getShellNavigationGap() {
		final RienaDefaultLnf lnf = LnfManager.getLnf();
		return lnf.getIntegerSetting(LnfKeyConstants.TITLELESS_SHELL_NAVIGATION_HORIZONTAL_GAP, DEFAULT_PADDING_LEFT);
	}

	/**
	 * Returns the gap between right side of the active sub module and the
	 * border of the shell.<br>
	 * <i>Note: The shell has also a padding (
	 * {@linkplain LnfKeyConstants.TITLELESS_SHELL_PADDING}).</i>
	 * 
	 * @return gap
	 */
	private static int getShellSubModuleGap() {
		final RienaDefaultLnf lnf = LnfManager.getLnf();
		return lnf.getIntegerSetting(LnfKeyConstants.TITLELESS_SHELL_SUB_MODULE_HORIZONTAL_GAP, DEFAULT_PADDING_RIGHT);
	}

	/**
	 * Sets the visibility of the navigation, if the Property
	 * {@link LnfKeyConstants#NAVIGATION_FAST_VIEW} is set to true.
	 * 
	 * @since 4.0
	 */
	public void setNavigationVisible(final boolean visible) {
		if (isNavigationFastViewEnabled()) {
			navigation.setVisible(visible);
		}
	}

	/**
	 * Returns the visibility of the navigation.
	 * 
	 * @return the visibility of the navigation. Always false if the property
	 *         {@link LnfKeyConstants#NAVIGATION_FAST_VIEW} is set to false.
	 * 
	 * @since 4.0
	 */
	public boolean isNavigationVisible() {
		if (isNavigationFastViewEnabled()) {
			return navigation.getControl().isVisible();
		}
		return true;
	}

	private final class CloseNavigationMouseListener implements Listener {
		public void handleEvent(final Event event) {
			if (event.button == 1 && navigation != null && isNavigationVisible()
					&& isSelectedWidgetAllowedToCloseNavigation(event.widget)) {
				final Point widgetLocationOnDisplay = ((Control) event.widget).toDisplay(new Point(0, 0));
				final Rectangle mouseLocation = event.getBounds();
				final Control navigationControl = navigation.getControl();
				final Rectangle navigationBounds = navigationControl.getBounds();
				final Point navigationLocationOnDisplay = navigationControl.toDisplay(0, 0);
				final Point mousePositionOnDisplay = new Point(mouseLocation.x + widgetLocationOnDisplay.x,
						mouseLocation.y + widgetLocationOnDisplay.y);
				// TODO cleanup
				if (mousePositionOnDisplay.x > navigationLocationOnDisplay.x + navigationBounds.width
						|| mousePositionOnDisplay.y > navigationLocationOnDisplay.y + navigationBounds.height
						|| mousePositionOnDisplay.y < navigationLocationOnDisplay.y) {
					setNavigationVisible(false);
				}
			}
		}

		private boolean isSelectedWidgetAllowedToCloseNavigation(final Widget widget) {
			return widget instanceof Control && !(widget instanceof ToolBar)
					&& !(widget instanceof SubApplicationSwitcherWidget);
		}
	}
}
