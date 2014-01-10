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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.listener.ModuleGroupNodeListener;
import org.eclipse.riena.navigation.listener.ModuleNodeListener;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ModuleGroupRenderer;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewProvider;
import org.eclipse.riena.ui.filter.IUIFilter;
import org.eclipse.riena.ui.swt.facades.SWTFacade;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * View of a module group.
 */
public class ModuleGroupView extends Composite implements INavigationNodeView<ModuleGroupNode> {

	private static final int MODULE_GROUP_GAP = 3;

	private ModuleGroupNode moduleGroupNode;
	private ModuleGroupListener moduleGroupListener;
	private ModuleListener moduleListener;
	private PaintDelegation paintDelegation;
	private final List<IComponentUpdateListener> updateListeners;
	private final Map<ModuleNode, ModuleView> registeredModuleViews;
	private final List<INavigationNode<?>> disposingNodes;
	private int scrollbarWidth;

	public ModuleGroupView(final Composite parent, final int style) {
		super(parent, style | SWT.DOUBLE_BUFFERED);
		updateListeners = new ArrayList<IComponentUpdateListener>();
		registeredModuleViews = new LinkedHashMap<ModuleNode, ModuleView>();
		disposingNodes = new ArrayList<INavigationNode<?>>();
		setData(getClass().getName());
	}

	private List<ModuleView> getAllModuleViews() {
		return new ArrayList<ModuleView>(registeredModuleViews.values());
	}

	private List<ModuleView> getAllVisibleModuleViews() {
		final List<ModuleView> views = new ArrayList<ModuleView>();
		for (final ModuleView view : registeredModuleViews.values()) {
			if (view.isVisible()) {
				views.add(view);
			}
		}
		return views;
	}

	protected ModuleNode getNodeForView(final ModuleView view) {
		for (final Entry<ModuleNode, ModuleView> entry : registeredModuleViews.entrySet()) {
			if (view.equals(entry.getValue())) {
				return entry.getKey();
			}
		}

		return null;
	}

	/**
	 * @since 1.2
	 */
	@Override
	public boolean setFocus() {
		// accept focus is this group is the active group
		return getNavigationNode().isActivated() ? super.setFocus() : false;
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.INavigationNodeView#bind(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void bind(final ModuleGroupNode node) {
		moduleGroupNode = node;
		addListeners();
	}

	/**
	 * Adds listeners to this view and also to node of the module group.
	 */
	protected void addListeners() {

		moduleGroupListener = new ModuleGroupListener();
		getNavigationNode().addListener(moduleGroupListener);
		moduleListener = new ModuleListener();

		paintDelegation = new PaintDelegation();
		SWTFacade.getDefault().addPaintListener(this, paintDelegation);
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.INavigationNodeView#unbind()
	 */
	public void unbind() {
		getNavigationNode().removeListener(moduleGroupListener);
		SWTFacade.getDefault().removePaintListener(this, paintDelegation);
		moduleGroupNode = null;
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.INavigationNodeView#getNavigationNode()
	 */
	public ModuleGroupNode getNavigationNode() {
		return moduleGroupNode;
	}

	/**
	 * The Listener fires updates, if a child (sub-module) is added or removed.
	 */
	private final class ModuleListener extends ModuleNodeListener {

		@Override
		public void childAdded(final IModuleNode source, final ISubModuleNode child) {
			super.childAdded(source, child);
			fireUpdated(child);
		}

		@Override
		public void beforeDisposed(final IModuleNode source) {
			disposingNodes.add(source);
		}

		@Override
		public void disposed(final IModuleNode source) {
			disposingNodes.remove(source);
		}

		@Override
		public void childRemoved(final IModuleNode source, final ISubModuleNode child) {
			if (disposingNodes.contains(source)) {
				return;
			}
			fireUpdated(child);
		}

		@Override
		public void filterAdded(final IModuleNode source, final IUIFilter filter) {
			super.filterAdded(source, filter);
			fireUpdated(null);
		}

		@Override
		public void filterRemoved(final IModuleNode source, final IUIFilter filter) {
			super.filterRemoved(source, filter);
			fireUpdated(null);
		}

		@Override
		public void presentSingleSubModuleChanged(final IModuleNode source) {
			super.presentSingleSubModuleChanged(source);
			fireUpdated(null);
		}

		@Override
		public void labelChanged(final IModuleNode source) {
			super.labelChanged(source);
			fireUpdated(null);
		}
	}

	/**
	 * The Listener fires updates, if a child (module) is added or removed.
	 */
	private final class ModuleGroupListener extends ModuleGroupNodeListener {

		@Override
		public void filterAdded(final IModuleGroupNode source, final IUIFilter filter) {
			super.filterAdded(source, filter);
			fireUpdated(null);
		}

		@Override
		public void filterRemoved(final IModuleGroupNode source, final IUIFilter filter) {
			super.filterRemoved(source, filter);
			fireUpdated(null);
		}

		@Override
		public void beforeDisposed(final IModuleGroupNode source) {
			disposingNodes.add(source);
		}

		@Override
		public void childAdded(final IModuleGroupNode source, final IModuleNode child) {
			fireUpdated(child);
		}

		@Override
		public void childRemoved(final IModuleGroupNode source, final IModuleNode child) {
			unregisterModuleView(child);
			if (disposingNodes.contains(source)) {
				return;
			}
			fireUpdated(child);
		}

		@Override
		public void deactivated(final IModuleGroupNode source) {
			super.deactivated(source);
			redraw();
		}

		@Override
		public void disposed(final IModuleGroupNode source) {
			super.disposed(source);
			disposingNodes.remove(source);
			unbind();
			dispose();
		}

		@Override
		public void nodeIdChange(final IModuleGroupNode source, final NavigationNodeId oldId,
				final NavigationNodeId newId) {
			if (source.equals(getNavigationNode())) {
				SwtViewProvider.getInstance().replaceNavigationNodeId(source, oldId, newId);
			}
		}

		@Override
		public void markerChanged(final IModuleGroupNode source, final IMarker marker) {
			super.markerChanged(source, marker);
			updateEnabled();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 4.0
	 */
	public int calculateHeight(final int widthHint, int heightHint) {
		if (isDisposed()) {
			return heightHint;
		}

		Point p = new Point(0, 0);
		if (getNavigationNode() != null && getNavigationNode().isVisible()) {
			p = computeSize(widthHint, SWT.DEFAULT);
		}
		final FormData fd = new FormData();
		fd.top = new FormAttachment(0, heightHint);
		fd.left = new FormAttachment(0, 0);
		heightHint += p.y;
		fd.width = p.x - getScrollbarWidth();
		fd.bottom = new FormAttachment(0, heightHint);
		if (!equals(fd, getCurrentFormData())) {
			setLayoutData(fd);
			layout();
		}
		if (p.y > 0) {
			heightHint += MODULE_GROUP_GAP;
		}
		return heightHint;
	}

	/**
	 * Updates the enabled state of all module views.
	 * 
	 * @since 3.0
	 */
	public void updateEnabled() {
		for (final ModuleView moduleView : getAllModuleViews()) {
			moduleView.updateEnabled();
		}
	}

	/**
	 * Compares the to given {@code FormData}s.
	 * 
	 * @param fd1
	 *            first {@code FormData}
	 * @param fd2
	 *            second {@code FormData}
	 * @return {@code true} if equals; otherwise false
	 */
	private boolean equals(final FormData fd1, final FormData fd2) {

		if (fd1 == fd2) {
			return true;
		}
		if ((fd1 == null) || (fd2 == null)) {
			return false;
		}
		if (fd1.height != fd2.height) {
			return false;
		}
		if (fd1.width != fd2.width) {
			return false;
		}
		if (!equals(fd1.bottom, fd2.bottom)) {
			return false;
		}
		if (!equals(fd1.left, fd2.left)) {
			return false;
		}
		if (!equals(fd1.right, fd2.right)) {
			return false;
		}
		if (!equals(fd1.top, fd2.top)) {
			return false;
		}

		return true;

	}

	/**
	 * Compares the to given {@code FormAttachment}s.
	 * 
	 * @param fd1
	 *            first {@code FormAttachment}
	 * @param fd2
	 *            second {@code FormAttachment}
	 * @return {@code true} if equals; otherwise false
	 */
	private boolean equals(final FormAttachment fa1, final FormAttachment fa2) {

		if (fa1 == fa2) {
			return true;
		}
		if ((fa1 == null) || (fa2 == null)) {
			return false;
		}
		if (fa1.alignment != fa2.alignment) {
			return false;
		}
		if (fa1.denominator != fa2.denominator) {
			return false;
		}
		if (fa1.numerator != fa2.numerator) {
			return false;
		}
		if (fa1.offset != fa2.offset) {
			return false;
		}

		return true;

	}

	/**
	 * Returns the current layout data of this view.
	 * 
	 * @return current layout data; {@code null} if layout data is not a
	 *         {@code FormData}
	 */
	private FormData getCurrentFormData() {

		final Object data = getLayoutData();
		if (data instanceof FormData) {
			return (FormData) data;
		} else {
			return null;
		}

	}

	@Override
	public Point computeSize(final int wHint, final int hHint) {
		final GC gc = new GC(Display.getCurrent());
		getRenderer().setItems(getAllVisibleModuleViews());
		getRenderer().setNavigationNode(getNavigationNode());
		final Point size = getRenderer().computeSize(gc, wHint, hHint);
		gc.dispose();
		return size;
	}

	private class PaintDelegation implements PaintListener {

		/**
		 * Computes the size of the widget of the module group and paints it.
		 * 
		 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
		 */
		public void paintControl(final PaintEvent e) {
			setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.MODULE_GROUP_WIDGET_BACKGROUND));
			getRenderer().setMarkers(getNavigationNode().getMarkers());
			getRenderer().setItems(getAllVisibleModuleViews());
			getRenderer().setActive(getNavigationNode().isActivated());
			final ModuleGroupView view = (ModuleGroupView) e.getSource();
			final Rectangle bounds = view.getBounds();
			getRenderer().setBounds(0, 0, bounds.width, bounds.height);
			getRenderer().setNavigationNode(getNavigationNode());
			getRenderer().paint(e.gc, null);
		}
	}

	private ModuleGroupRenderer getRenderer() {
		return (ModuleGroupRenderer) LnfManager.getLnf().getRenderer(LnfKeyConstants.MODULE_GROUP_RENDERER);
	}

	/**
	 * Adds the give view to the list of the module views that are belonging to
	 * this module group.
	 * 
	 * @param moduleView
	 *            view to register
	 */
	public void registerModuleView(final ModuleView moduleView) {
		moduleView.getNavigationNode().addListener(moduleListener);
		// we need that to calculate the bounds of the ModuleGroupView
		registeredModuleViews.put(moduleView.getNavigationNode(), moduleView);
		// observer moduleView for expand/collapse
		moduleView.addUpdateListener(new ModuleViewObserver());
	}

	/**
	 * Removes that view that is belonging to the given node from the list of
	 * the module views.
	 * 
	 * @param moduleNode
	 *            node whose according view should be unregistered
	 */
	public void unregisterModuleView(final IModuleNode moduleNode) {
		for (final ModuleView moduleView : getAllModuleViews()) {
			if (moduleView.getNavigationNode() == moduleNode || moduleView.getNavigationNode() == null) {
				unregisterModuleView(moduleView);
				break;
			}
		}
	}

	/**
	 * Remove the given view from the list of the module views.
	 * 
	 * @param moduleView
	 *            view to remove
	 */
	public void unregisterModuleView(final ModuleView moduleView) {
		final ModuleNode node = getNodeForView(moduleView);
		if (node != null) {
			node.removeListener(moduleListener);
			registeredModuleViews.remove(node);
		}
	}

	private class ModuleViewObserver implements IComponentUpdateListener {

		public void update(final INavigationNode<?> node) {
			fireUpdated(node);
		}

	}

	protected void fireUpdated(final INavigationNode<?> node) {
		for (final IComponentUpdateListener listener : updateListeners) {
			listener.update(node);
		}
	}

	public void addUpdateListener(final IComponentUpdateListener listener) {
		updateListeners.add(listener);
	}

	/**
	 * @return the scrollbarWidth
	 * @since 3.0
	 */
	public int getScrollbarWidth() {
		return scrollbarWidth;
	}

	/**
	 * @param scrollbarWidth
	 *            the scrollbarWidth to set
	 * @since 3.0
	 */
	public void setScrollbarWidth(final int scrollbarWidth) {
		this.scrollbarWidth = scrollbarWidth;
	}

}
