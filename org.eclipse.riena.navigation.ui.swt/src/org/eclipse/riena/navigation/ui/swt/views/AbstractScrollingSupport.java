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
package org.eclipse.riena.navigation.ui.swt.views;

import java.util.List;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.ui.swt.facades.SWTFacade;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * This class provides basic parts of scrolling logic for the navigation.
 * 
 * @since 3.0
 */
public abstract class AbstractScrollingSupport {

	protected static final int SCROLLING_STEP = 20;
	protected final IModuleNavigationComponentProvider navigationComponentProvider;

	public AbstractScrollingSupport(final IModuleNavigationComponentProvider navigationComponentProvider) {
		this.navigationComponentProvider = navigationComponentProvider;
		initMouseWheelObserver(getNavigationComponent());
	}

	/**
	 * Scrolls to the active navigation element.
	 */
	public abstract void scroll();

	/**
	 * Scrolls the navigation so that given composite is visible.
	 * 
	 * @param topComp
	 *            title composite
	 * @param bottomComp
	 *            body composite
	 * @return {@code true} scrolling was necessary.
	 */
	protected abstract boolean scrollTo(final Composite topComp, final Composite bottomComp);

	/**
	 * Scrolls the navigation so that the selected tree item is visible.
	 * 
	 * @param tree
	 *            tree of the navigation
	 * @return {@code true} scrolling was necessary.
	 */
	protected abstract boolean scrollTo(final Tree tree);

	/**
	 * Scrolls up.
	 * 
	 * @param pixels
	 *            amount of pixels for one scrolling step
	 */
	protected abstract void scrollUp(final int pixels);

	/**
	 * Scrolls down
	 * 
	 * @param pixels
	 *            amount of pixels for one scrolling step
	 */
	protected abstract void scrollDown(final int pixels);

	/**
	 * Scrolls to the given module node.
	 * 
	 * @param module
	 *            module node
	 * @return {@code true} scrolling was necessary
	 */
	protected boolean scrollTo(final IModuleNode module) {
		boolean result = false;
		if (module != null) {
			final ModuleView moduleView = navigationComponentProvider.getModuleViewForNode(module);
			if (moduleView == null) {
				return result;
			}
			final boolean isClosed = moduleView.getOpenHeight() == 0;
			if (isClosed) {
				result = scrollTo(moduleView.getTitle(), moduleView.getBody());
			} else {
				final int moduleHeight = moduleView.getParent().getSize().y;
				if (moduleHeight < getNavigationComponentHeight()) {
					// only show title if the whole module fits into the available height
					// prevents flicker
					result = scrollTo(moduleView.getTitle(), moduleView.getBody());
				}
				result = result || scrollTo(moduleView.getTree());
			}
		}
		return result;
	}

	/**
	 * Returns the active navigation node.
	 * 
	 * @return active navigation node; {@code null} if no active node was found
	 */
	protected INavigationNode<?> getActiveNode() {
		final IModuleGroupNode group = navigationComponentProvider.getActiveModuleGroupNode();
		IModuleNode module = null;
		ISubModuleNode submodule = null;
		if (group != null) {
			for (final IModuleNode candidate : group.getChildren()) {
				if (candidate.isActivated()) {
					module = candidate;
					break;
				}
			}
		}
		if (module != null) {
			submodule = getActiveSubModuleNode(module.getChildren());
		}
		return submodule != null ? submodule : module != null ? module : group;
	}

	/**
	 * Returns the active sub module node.
	 * 
	 * @param nodes
	 *            list of all (sibling) sub module nodes
	 * @return sub module node; {@code null} if no active node was found
	 */
	protected ISubModuleNode getActiveSubModuleNode(final List<ISubModuleNode> nodes) {
		ISubModuleNode result = null;
		for (final ISubModuleNode candidate : nodes) {
			if (candidate.isActivated()) {
				final ISubModuleNode activeChild = getActiveSubModuleNode(candidate.getChildren());
				result = activeChild != null ? activeChild : candidate;
				break;
			}
		}
		return result;
	}

	/**
	 * Returns the height of the scrolled component.
	 * 
	 * @return height
	 */
	protected int getScrolledComponentHeight() {
		return getScrolledComponent().getBounds().height;
	}

	protected Composite getScrolledComponent() {
		return navigationComponentProvider.getScrolledComponent();
	}

	/**
	 * Returns the height of the navigation component.
	 * 
	 * @return height
	 */
	protected int getNavigationComponentHeight() {
		return getNavigationComponent().getBounds().height;
	}

	protected Composite getNavigationComponent() {
		return navigationComponentProvider.getNavigationComponent();
	}

	/**
	 * Determines if scrolling is needed.
	 * 
	 * @return {@code true} if we need scrolling
	 */
	protected boolean mayScroll() {
		final int navigationComponentHeight = getNavigationComponentHeight();
		return getScrolledComponentHeight() > navigationComponentHeight && navigationComponentHeight > 0;
	}

	// helping methods
	//////////////////

	private void initMouseWheelObserver(final Composite navigationComponent) {
		if (SwtUtilities.isDisposed(navigationComponent)) {
			return;
		}
		final Display display = navigationComponent.getDisplay();
		final MouseWheelAdapter wheelAdapter = new MouseWheelAdapter();
		SWTFacade.getDefault().addFilterMouseWheel(display, wheelAdapter);
		navigationComponent.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(final DisposeEvent e) {
				SWTFacade.getDefault().removeFilterMouseWheel(display, wheelAdapter);
			}
		});
	}

	private final class MouseWheelAdapter implements Listener {

		// for saving last event time
		private int lastEventTime = 0;

		public void handleEvent(final Event event) {
			// only go further if the event has a new time stamp
			if (mayScroll() && acceptEvent(event)) {
				lastEventTime = event.time;
				final Rectangle navigationComponentBounds = getNavigationComponent().getBounds();

				// convert navigation bounds relative to display
				final Point navigationPtAtDisplay = getNavigationComponent().toDisplay(0, 0);
				navigationComponentBounds.x = navigationPtAtDisplay.x;
				navigationComponentBounds.y = navigationPtAtDisplay.y;

				if (event.widget instanceof Control) {
					final Control widget = (Control) event.widget;
					// convert widget event point relative to display
					final Point evtPt = widget.toDisplay(event.getBounds().x, event.getBounds().y);
					// now check if inside navigation
					if (navigationComponentBounds.contains(evtPt.x, evtPt.y)) {
						if (event.count > 0) {
							scrollUp(SCROLLING_STEP);
						} else {
							scrollDown(SCROLLING_STEP);
						}
					}

				}
			}
		}

		private boolean acceptEvent(final Event event) {
			// check that this is the latest event
			final boolean isCurrent = event.time > lastEventTime;
			// 282089: check this window is has the focus, to avoid scrolling when
			// the mouse pointer happens to be over another overlapping window
			final Control control = (Control) event.widget;
			final boolean isActive = control.getShell() == getActiveShell();
			// 282091: check that this navigation component is visible. Since
			// we are using a display filter the navigation componentes of _each_
			// subapplicatio are notified when scrolling!
			final boolean isVisible = getNavigationComponent().isVisible();
			return isCurrent && isActive && isVisible;
		}

		private Shell getActiveShell() {
			final IWorkbenchWindow activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			return activeWindow != null ? activeWindow.getShell() : null;
		}
	}

}
