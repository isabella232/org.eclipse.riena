/*******************************************************************************
 * Copyright (c) 2007, 2013 compeople AG and others.
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.navigation.ui.swt.Activator;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.listener.ModuleGroupNodeListener;
import org.eclipse.riena.navigation.listener.ModuleNodeListener;
import org.eclipse.riena.navigation.listener.NavigationTreeObserver;
import org.eclipse.riena.navigation.listener.SubApplicationNodeListener;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.ui.controllers.ModuleController;
import org.eclipse.riena.navigation.ui.swt.ApplicationUtility;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.EmbeddedBorderRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ModuleGroupRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.SubModuleViewRenderer;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewProvider;
import org.eclipse.riena.navigation.ui.swt.presentation.stack.TitlelessStackPresentation;
import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.ridgets.listener.ISelectionListener;
import org.eclipse.riena.ui.ridgets.listener.SelectionEvent;
import org.eclipse.riena.ui.swt.facades.SWTFacade;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

public class NavigationViewPart extends ViewPart implements IModuleNavigationComponentProvider {

	public final static String ID = "org.eclipse.riena.navigation.ui.swt.views.navigationViewPart"; //$NON-NLS-1$
	private static final Color NAVIGATION_BACKGROUND = LnfManager.getLnf().getColor(
			LnfKeyConstants.NAVIGATION_BACKGROUND);

	private IViewFactory viewFactory;
	private Composite parent;
	private ResizeListener resizeListener;
	private final List<ModuleGroupView> moduleGroupViews = new ArrayList<ModuleGroupView>();
	private Composite navigationMainComposite;
	private INavigationCompositeDelegation navigationCompositeDelegation;
	private NavigationTreeObserver navigationTreeObserver;
	private final Map<INavigationNode<?>, ModuleGroupView> moduleGroupNodesToViews;
	private final Map<INavigationNode<?>, ModuleView> moduleNodesToViews;

	public NavigationViewPart() {
		markAsNavigation();
		moduleGroupNodesToViews = new HashMap<INavigationNode<?>, ModuleGroupView>();
		moduleNodesToViews = new HashMap<INavigationNode<?>, ModuleView>();
	}

	protected IViewFactory getViewFactory() {
		if (viewFactory == null) {
			viewFactory = new NavigationViewFactory();
			Wire.instance(viewFactory).andStart(Activator.getDefault().getContext());
		}
		return viewFactory;
	}

	private void markAsNavigation() {
		setPartProperty(TitlelessStackPresentation.PROPERTY_NAVIGATION, String.valueOf(Boolean.TRUE));
	}

	public ISubApplicationNode getSubApplicationNode() {
		final String perspectiveID = getViewSite().getPage().getPerspective().getId();
		return SwtViewProvider.getInstance().getNavigationNode(perspectiveID, ISubApplicationNode.class);
	}

	@Override
	public void createPartControl(final Composite parent) {
		this.parent = parent;
		initLayoutParts();
		// build the view hierarchy
		buildNavigationViewHierarchy();
		initModelObserver();
		parent.setData(TitlelessStackPresentation.PROPERTY_SUBAPPLICATION_NODE, getSubApplicationNode());
	}

	private void initLayoutParts() {
		// configure layout
		parent.setLayout(new FormLayout());
		parent.setBackground(NAVIGATION_BACKGROUND);
		resizeListener = new ResizeListener();
		parent.addControlListener(resizeListener);

		navigationMainComposite = new Composite(parent, SWT.DOUBLE_BUFFERED);
		navigationMainComposite.setLayout(new FormLayout());
		navigationMainComposite.setBackground(NAVIGATION_BACKGROUND);
		navigationCompositeDelegation = createNavigationCompositeDelegation(navigationMainComposite);
		final boolean fastView = ApplicationUtility.isNavigationFastViewEnabled();
		final FormData formData = new FormData();
		formData.top = new FormAttachment(0, fastView ? AbstractNavigationCompositeDeligation.BORDER_MARGIN : 0);
		formData.left = new FormAttachment(0, fastView ? AbstractNavigationCompositeDeligation.BORDER_MARGIN : 0);
		formData.right = new FormAttachment(100, fastView ? -AbstractNavigationCompositeDeligation.BORDER_MARGIN : 0);
		formData.bottom = new FormAttachment(100, navigationCompositeDelegation.getBottomOffest()
				- (fastView ? AbstractNavigationCompositeDeligation.BORDER_MARGIN : 0));
		navigationMainComposite.setLayoutData(formData);
		if (fastView) {
			SWTFacade.getDefault().addPaintListener(parent, new PaintListener() {
				public void paintControl(final PaintEvent e) {

					final SubModuleViewRenderer viewRenderer = (SubModuleViewRenderer) LnfManager.getLnf().getRenderer(
							"SubModuleView.renderer"); //$NON-NLS-1$
					if (viewRenderer != null) {
						final Rectangle bounds = parent.getBounds();
						viewRenderer.setBounds(bounds);
						viewRenderer.paint(e.gc, null);
					}
				}
			});
		}
	}

	/**
	 * Creates a delegation of the composite for scrolling in the navigation.
	 * 
	 * @param superParent
	 *            parent of the navigation composite
	 * @param parent
	 *            composite of the navigation
	 * @return delegation
	 */
	private INavigationCompositeDelegation createNavigationCompositeDelegation(final Composite parent) {
		final boolean scrollBar = LnfManager.getLnf().getBooleanSetting(LnfKeyConstants.NAVIGATION_SCROLL_BAR, false);
		if (scrollBar) {
			return new ScrollBarNavigationCompositeDeligation(parent.getParent(), parent, this);
		} else {
			return createButtonsNavigationCompositeDelegation(parent);
		}
	}

	/**
	 * @param parent
	 * @return
	 * @since 5.0
	 */
	protected INavigationCompositeDelegation createButtonsNavigationCompositeDelegation(final Composite parent) {
		return new ScrollButtonsNavigationCompositeDeligation(parent.getParent(), parent, this);
	}

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		if (!SwtUtilities.isDisposed(parent) && resizeListener != null) {
			parent.removeControlListener(resizeListener);
		}
	}

	private void buildNavigationViewHierarchy() {
		// get the root of the SubApplication
		final ISubApplicationNode subApplicationNode = getSubApplicationNode();
		for (final IModuleGroupNode moduleGroupNode : subApplicationNode.getChildren()) {
			createModuleGroupView(moduleGroupNode);
		}
		updateNavigationSize();
	}

	private void initModelObserver() {
		navigationTreeObserver = new NavigationTreeObserver();
		navigationTreeObserver.addListener(new SubApplicationListener());
		navigationTreeObserver.addListener(new ModuleGroupListener());
		navigationTreeObserver.addListener(new ModuleListener());
		navigationTreeObserver.addListener(new SubModuleListener());
		navigationTreeObserver.addListenerTo(getSubApplicationNode());
	}

	/**
	 * Update the size of the navigation area when the application is resized
	 * (fix for bug 270620).
	 */
	private final class ResizeListener extends ControlAdapter {
		@Override
		public void controlResized(final ControlEvent e) {
			parent.layout();
			updateNavigationSize();
		}
	}

	/**
	 * The Listener updates the size of the navigation, if a child is added or
	 * removed.
	 */
	private class SubApplicationListener extends SubApplicationNodeListener {

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#childAdded(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void childAdded(final ISubApplicationNode source, final IModuleGroupNode child) {
			// create moduleGroupView
			createModuleGroupView(child);
			updateNavigationSize();
		}

		//		@Override
		//		public void filterAdded(ISubApplicationNode source, IUIFilter filter) {
		//			super.filterAdded(source, filter);
		//			updateNavigationSize();
		//		}
		//
		//		@Override
		//		public void filterRemoved(ISubApplicationNode source, IUIFilter filter) {
		//			super.filterRemoved(source, filter);
		//			updateNavigationSize();
		//		}
		//
		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#childRemoved(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void childRemoved(final ISubApplicationNode source, final IModuleGroupNode child) {
			unregisterModuleGroupView(child);
			if (source.isSelected()) {
				updateNavigationSize();
			}
		}

		@Override
		public void markerChanged(final ISubApplicationNode source, final IMarker marker) {
			if (marker instanceof HiddenMarker) {
				updateNavigationSize();
			}
		}

	}

	private class ModuleGroupListener extends ModuleGroupNodeListener {

		//		@Override
		//		public void filterAdded(IModuleGroupNode source, IUIFilter filter) {
		//			super.filterAdded(source, filter);
		//			updateNavigationSize();
		//		}
		//
		//		@Override
		//		public void filterRemoved(IModuleGroupNode source, IUIFilter filter) {
		//			super.filterRemoved(source, filter);
		//			updateNavigationSize();
		//		}

		@Override
		public void childAdded(final IModuleGroupNode source, final IModuleNode child) {
			// createModuleView
			final ModuleGroupView moduleGroupView = getModuleGroupViewForNode(source);
			createModuleView(child, moduleGroupView);
			// childAdded.activate();
			updateNavigationSize();
		}

		@Override
		public void childRemoved(final IModuleGroupNode source, final IModuleNode child) {
			moduleNodesToViews.remove(child);
			// updateNavigationSize();
		}

		@Override
		public void disposed(final IModuleGroupNode source) {
			super.disposed(source);
			unregisterModuleGroupView(source);
			// updateNavigationSize();
		}

		@Override
		public void markerChanged(final IModuleGroupNode source, final IMarker marker) {
			if (marker instanceof HiddenMarker) {
				updateNavigationSize();
			}
		}

		@Override
		public void nodeIdChange(final IModuleGroupNode source, final NavigationNodeId oldId,
				final NavigationNodeId newId) {
			super.nodeIdChange(source, oldId, newId);
			replaceNavigationNodeId(source, oldId, newId);
		}
	}

	private class SubModuleListener extends SubModuleNodeListener {

		@Override
		public void markerChanged(final ISubModuleNode source, final IMarker marker) {
			if (marker instanceof HiddenMarker) {
				updateNavigationSize();
			}
		}
	}

	private class ModuleListener extends ModuleNodeListener {
		@Override
		public void nodeIdChange(final IModuleNode source, final NavigationNodeId oldId, final NavigationNodeId newId) {
			super.nodeIdChange(source, oldId, newId);
			replaceNavigationNodeId(source, oldId, newId);
		}
	}

	public ModuleGroupView getModuleGroupViewForNode(final IModuleGroupNode source) {
		return moduleGroupNodesToViews.get(source);
	}

	/**
	 * @since 1.2
	 */
	public ModuleView getModuleViewForNode(final IModuleNode source) {
		return moduleNodesToViews.get(source);
	}

	private void createModuleGroupView(final IModuleGroupNode moduleGroupNode) {
		// ModuleGroupView are directly rendered into the bodyComposite
		final ModuleGroupView moduleGroupView = getViewFactory().createModuleGroupView(
				navigationCompositeDelegation.getNavigationComposite());
		NodeIdentificationSupport.setIdentification(moduleGroupView, "moduleGroupView", moduleGroupNode); //$NON-NLS-1$
		moduleGroupNodesToViews.put(moduleGroupNode, moduleGroupView);
		moduleGroupView.addUpdateListener(new ModuleGroupViewObserver());

		registerModuleGroupView(moduleGroupView);
		// create controller. the controller is implicit registered as
		// presentation in the node
		// now the ModuleGroupController can be replaced by your own implementation
		getViewFactory().createModuleGroupController(moduleGroupNode);
		//new ModuleGroupController(moduleGroupNode);
		moduleGroupView.bind((ModuleGroupNode) moduleGroupNode);

		moduleGroupView.setLayout(new FormLayout());
		final Composite moduleGroupBody = new Composite(moduleGroupView, SWT.NONE);
		moduleGroupBody.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.MODULE_GROUP_WIDGET_BACKGROUND));
		final FormData formData = new FormData();
		final int padding = getModuleGroupPadding();
		formData.top = new FormAttachment(0, padding);
		formData.left = new FormAttachment(0, padding);
		formData.bottom = new FormAttachment(100, -padding);
		formData.right = new FormAttachment(100, -padding);
		moduleGroupBody.setLayoutData(formData);

		// now it's time for the module views
		for (final IModuleNode moduleNode : moduleGroupNode.getChildren()) {
			createModuleView(moduleNode, moduleGroupView);
		}

	}

	private final class ModuleGroupViewObserver implements IComponentUpdateListener {
		public void update(final INavigationNode<?> node) {
			updateNavigationSize();
		}
	}

	/**
	 * Adds the give view to the list of the module views that are belonging to
	 * the sub-application of this navigation.
	 * 
	 * @param moduleView
	 *            view to register
	 */
	private void registerModuleGroupView(final ModuleGroupView moduleGroupView) {
		moduleGroupViews.add(moduleGroupView);
		final ModuleGroupViewComparator comparator = new ModuleGroupViewComparator();
		Collections.sort(moduleGroupViews, comparator);
	}

	class ModuleGroupViewComparator implements Comparator<ModuleGroupView> {

		public int compare(final ModuleGroupView moduleGroupView1, final ModuleGroupView moduleGroupView2) {
			final ModuleGroupNode moduleGroupNode1 = moduleGroupView1.getNavigationNode();
			final ModuleGroupNode moduleGroupNode2 = moduleGroupView2.getNavigationNode();
			return getSubApplicationNode().getIndexOfChild(moduleGroupNode1) < getSubApplicationNode().getIndexOfChild(
					moduleGroupNode2) ? -1 : 1;
		}

	}

	/**
	 * Removes that view that is belonging to the given node from the list of
	 * the module group views.
	 * 
	 * @param moduleGroupNode
	 *            node whose according view should be unregistered
	 */
	public void unregisterModuleGroupView(final IModuleGroupNode moduleGroupNode) {
		for (final ModuleGroupView moduleGroupView : moduleGroupViews) {
			if (moduleGroupView.getNavigationNode() == moduleGroupNode) {
				unregisterModuleGroupView(moduleGroupView, moduleGroupNode);
				break;
			}
		}
	}

	/**
	 * Remove the given view from the list of the module group views.
	 * 
	 * @param moduleGroupView
	 *            view to remove
	 */
	private void unregisterModuleGroupView(final ModuleGroupView moduleGroupView, final IModuleGroupNode node) {
		moduleGroupNodesToViews.remove(node);
		moduleGroupViews.remove(moduleGroupView);
	}

	private void replaceNavigationNodeId(final IModuleGroupNode node, final NavigationNodeId oldId,
			final NavigationNodeId newId) {
		node.setNodeId(oldId);
		final ModuleGroupView view = moduleGroupNodesToViews.remove(node);
		if (view != null) {
			node.setNodeId(newId);
			moduleGroupNodesToViews.put(node, view);
		}
	}

	private void replaceNavigationNodeId(final IModuleNode node, final NavigationNodeId oldId,
			final NavigationNodeId newId) {
		node.setNodeId(oldId);
		final ModuleView view = moduleNodesToViews.remove(node);
		if (view != null) {
			node.setNodeId(newId);
			moduleNodesToViews.put(node, view);
		}
	}

	private void createModuleView(final IModuleNode moduleNode, final ModuleGroupView moduleGroupView) {
		final Composite moduleGroupBody = (Composite) moduleGroupView.getChildren()[0];
		final FormLayout layout = new FormLayout();
		moduleGroupBody.setLayout(layout);

		final ModuleView moduleView = viewFactory.createModuleView(moduleGroupBody);
		moduleView.setModuleGroupNode(moduleGroupView.getNavigationNode());
		NodeIdentificationSupport.setIdentification(moduleView.getTitle(), "titleBar", moduleNode); //$NON-NLS-1$
		NodeIdentificationSupport.setIdentification(moduleView.getTree(), "tree", moduleNode); //$NON-NLS-1$
		moduleNodesToViews.put(moduleNode, moduleView);
		final ModuleController controller = getViewFactory().createModuleController(moduleNode);
		moduleView.bind((ModuleNode) moduleNode);
		if (controller instanceof SWTModuleController) {
			((SWTModuleController) controller).getTree().addSelectionListener(new ISelectionListener() {
				public void ridgetSelected(final SelectionEvent event) {
					// *after* the node in the navigation is  *really* selected
					// start scrolling
					navigationCompositeDelegation.scroll();
				}
			});
		}
		// the size of the module group depends on the module views
		moduleGroupView.registerModuleView(moduleView);
	}

	public void updateNavigationSize() {
		final int height = updateNavigationHeight();
		final boolean widthChanged = updateModuleGroupWidth(height);
		//navigationMainComposite.layout(true, true);
		if (widthChanged) {
			updateNavigationHeight();
		}
		navigationMainComposite.getParent().layout(true, true);
		navigationCompositeDelegation.scroll();
	}

	/**
	 * @return the new height
	 */
	private int updateNavigationHeight() {
		int height = calculateHeight();
		navigationMainComposite.layout();
		navigationCompositeDelegation.getNavigationComposite().layout();
		if (navigationMainComposite.getBounds().height == 0) {
			height = 0;
		}
		navigationCompositeDelegation.updateSize(height);
		return height;
	}

	/**
	 * If a vertical scroll bar exists, the width of the module groups must be
	 * updated.
	 * 
	 * @param height
	 *            height of the scrolled composite
	 * @return {@code true} width of the module groups has changed
	 */
	private boolean updateModuleGroupWidth(final int height) {
		boolean widthChanged = false;
		int scrollbarWidth = 0;
		final Point mainSize = navigationMainComposite.getSize();
		if (height > mainSize.x) {
			scrollbarWidth = navigationCompositeDelegation.getVerticalScrollBarSize().x;
		}

		for (final ModuleGroupView moduleGroupView : moduleGroupViews) {
			if (!moduleGroupView.isDisposed()) {
				final Point size = moduleGroupView.getSize();
				if (mainSize.x > size.x) {
					moduleGroupView.setSize(mainSize.x, size.y);
					widthChanged = true;
				}
			}
			if (moduleGroupView.getScrollbarWidth() != scrollbarWidth) {
				moduleGroupView.setScrollbarWidth(scrollbarWidth);
				widthChanged = true;
			}
		}
		return widthChanged;
	}

	public IModuleGroupNode getActiveModuleGroupNode() {
		final List<IModuleGroupNode> children = getSubApplicationNode().getChildren();
		for (final IModuleGroupNode child : children) {
			if (child.isActivated()) {
				return child;
			}
		}
		return null;
	}

	/**
	 * @since 4.0
	 */
	public int calculateHeight() {
		int height = 0;
		final int widthHint = navigationMainComposite.getSize().x;
		Collections.sort(moduleGroupViews, new ModuleGroupViewComparator());
		for (final ModuleGroupView moduleGroupView : moduleGroupViews) {
			height = moduleGroupView.calculateHeight(widthHint, height);
		}
		return height;
	}

	/**
	 * Returns the renderer of the module group.
	 * 
	 * @return the renderer instance
	 */
	private ModuleGroupRenderer getModuleGroupRenderer() {

		ModuleGroupRenderer renderer = (ModuleGroupRenderer) LnfManager.getLnf().getRenderer(
				LnfKeyConstants.MODULE_GROUP_RENDERER);
		if (renderer == null) {
			renderer = new ModuleGroupRenderer();
		}
		return renderer;

	}

	/**
	 * Returns the renderer of the module group.
	 * 
	 * @return the renderer instance
	 */
	private EmbeddedBorderRenderer getLnfBorderRenderer() {

		EmbeddedBorderRenderer renderer = (EmbeddedBorderRenderer) LnfManager.getLnf().getRenderer(
				LnfKeyConstants.SUB_MODULE_VIEW_BORDER_RENDERER);
		if (renderer == null) {
			renderer = new EmbeddedBorderRenderer();
		}
		return renderer;

	}

	private int getModuleGroupPadding() {
		return getModuleGroupRenderer().getModuleGroupPadding() + getLnfBorderRenderer().getBorderWidth();
	}

	@Override
	public void setFocus() {
		navigationMainComposite.setFocus();
	}

	public Composite getNavigationComponent() {
		return navigationMainComposite;
	}

	public Composite getScrolledComponent() {
		return navigationCompositeDelegation.getNavigationComposite();
	}

}
