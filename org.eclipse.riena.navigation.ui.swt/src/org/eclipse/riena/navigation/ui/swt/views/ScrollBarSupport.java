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

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * This class provides scrolling logic for the navigation with a scroll bar.
 * 
 * @since 3.0
 */
public class ScrollBarSupport extends AbstractScrollingSupport {

	private final ScrolledComposite scrolledComposite;

	/**
	 * Creates a new instance of the support of scrolling with scroll bars.
	 * 
	 * @param scrolledComposite
	 *            the composite around the content that should be scrolled
	 * @param navigationComponentProvider
	 */
	public ScrollBarSupport(final ScrolledComposite scrolledComposite,
			final IModuleNavigationComponentProvider navigationComponentProvider) {
		super(navigationComponentProvider);
		this.scrolledComposite = scrolledComposite;
	}

	@Override
	public void scroll() {
		final IModuleNode activeModule = getActiveModule(getActiveNode());
		if (canScroll(activeModule)) {
			scrollTo(activeModule);
		}
	}

	/**
	 * Returns the active module.
	 * 
	 * @param activeNode
	 *            the active navigation node
	 * @return active module
	 */
	private IModuleNode getActiveModule(final INavigationNode<?> activeNode) {
		if (activeNode instanceof IModuleGroupNode) {
			return null;
		} else if (activeNode instanceof IModuleNode) {
			return (IModuleNode) activeNode;
		} else if (activeNode instanceof ISubModuleNode) {
			return (IModuleNode) activeNode.getParent();
		}
		return null;
	}

	@Override
	protected boolean scrollTo(final Composite topComp, final Composite bottomComp) {
		final int pixels = getScrollPixels(topComp, bottomComp);
		if (canScroll(pixels)) {
			scroll(pixels);
			return true;
		}
		return false;
	}

	@Override
	protected boolean scrollTo(final Tree tree) {
		final int pixels = getScrollPixels(tree);
		if (canScroll(pixels)) {
			scroll(pixels);
			return true;
		}
		return false;
	}

	/**
	 * Returns whether scrolling is necessary to make the given module (and
	 * maybe the selected sub-module) visible.
	 * 
	 * @param module
	 *            node of the module
	 * @return {@code true} scrolling is necessary; otherwise {@code false}
	 */
	private boolean canScroll(final IModuleNode module) {
		if (module == null) {
			return false;
		}
		final ModuleView moduleView = navigationComponentProvider.getModuleViewForNode(module);
		if (moduleView == null) {
			return false;
		}
		final boolean isClosed = moduleView.getOpenHeight() == 0;
		if (isClosed) {
			return canScroll(moduleView.getTitle(), moduleView.getBody());
		} else {
			return canScroll(moduleView.getTree());
		}
	}

	/**
	 * Returns whether scrolling is necessary to make the given composites
	 * visible.
	 * 
	 * @param topComp
	 *            top composite
	 * @param bottomComp
	 *            bottom composite
	 * @return {@code true} scrolling is necessary; otherwise {@code false}
	 */
	private boolean canScroll(final Composite topComp, final Composite bottomComp) {
		return canScroll(getScrollPixels(topComp, bottomComp));
	}

	private boolean canScroll(final Tree tree) {
		return canScroll(getScrollPixels(tree));
	}

	/**
	 * Returns whether scrolling is necessary to make the selected tree item
	 * visible.
	 * 
	 * @param tree
	 *            tree
	 * @return {@code true} scrolling is necessary; otherwise {@code false}
	 */
	private boolean canScroll(final int pixels) {
		return pixels != 0;
	}

	/**
	 * Returns the amount of pixels which are necessary for scrolling so that
	 * the given composites are visible.
	 * 
	 * @param topComp
	 *            top composite
	 * @param bottomComp
	 *            bottom composite
	 * @return amount of pixels
	 */
	private int getScrollPixels(final Composite topComp, final Composite bottomComp) {
		final int ty = scrolledComposite.getDisplay().map(topComp, scrolledComposite, 0, topComp.getBounds().y).y;
		if (ty < 0) {
			return ty;
		}

		final int clientHeight = scrolledComposite.getClientArea().height;
		final int by = scrolledComposite.getDisplay().map(bottomComp, scrolledComposite, 0,
				bottomComp.getBounds().height).y;
		if (by > clientHeight) {
			return by - clientHeight;
		}

		return 0;
	}

	/**
	 * Returns the amount of pixels which are necessary for scrolling so that
	 * the selected tree item is visible.
	 * 
	 * @param tree
	 *            tree
	 * @return amount of pixels
	 */
	private int getScrollPixels(final Tree tree) {
		if (SwtUtilities.isDisposed(tree)) {
			return 0;
		}
		if (tree.getSelectionCount() > 0) {
			final TreeItem item = tree.getSelection()[0];
			final Rectangle itemBounds = item.getBounds();
			int y = scrolledComposite.getDisplay().map(tree, scrolledComposite, 0, itemBounds.y).y;
			if (y < 0) {
				return y;
			}
			final int clientHeight = scrolledComposite.getClientArea().height;
			y = scrolledComposite.getDisplay().map(tree, scrolledComposite, 0, itemBounds.y + itemBounds.height).y;
			if (y > clientHeight) {
				return y - clientHeight;
			}
		}
		return 0;
	}

	@Override
	protected int getNavigationComponentHeight() {
		return scrolledComposite.getBounds().height;
	}

	@Override
	protected void scrollUp(final int pixels) {
		scroll(-pixels);
	}

	@Override
	protected void scrollDown(final int pixels) {
		scroll(pixels);
	}

	private void scroll(final int pixels) {
		final Point origin = scrolledComposite.getOrigin();
		origin.y += pixels;
		scrolledComposite.setOrigin(origin);
	}

}
