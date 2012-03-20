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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * This class provides scrolling logic for the navigation with scroll buttons.
 * 
 * @since 3.0
 */
public class ScrollButtonsSupport extends AbstractScrollingSupport {

	// the SWT composite
	private final ScrollControlComposite scrollControlComposite;
	// offset of the scrolled composite relative to the main navigation composite
	private int scrolledCompositeOffset = 0;

	/**
	 * 
	 * @param parent
	 *            the composite parent
	 * @param flags
	 *            SWT style flags
	 * @param navigationComponentProvider
	 */
	public ScrollButtonsSupport(final Composite parent,
			final IModuleNavigationComponentProvider navigationComponentProvider) {
		super(navigationComponentProvider);
		scrollControlComposite = new ScrollControlComposite(parent, SWT.NONE);
		setBodyCompositeOffset(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scroll() {
		scrollControlComposite.setVisible(mayScroll());
		scrollToActive();
	}

	/**
	 * @return the scrolled composite which is placed relative to the navigation
	 *         main composite
	 */
	public Composite getScrollComposite() {
		return scrollControlComposite;
	}

	/**
	 * Returns the height of the scroll buttons.
	 * 
	 * @return height of buttons
	 */
	public int getButtonHeight() {
		return LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.NAVIGATION_SCROLL_BUTTON_HEIGHT, 15);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void scrollUp(final int pixels) {
		final int offset = Math.min(scrolledCompositeOffset + pixels, 0);
		setBodyCompositeOffset(offset);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void scrollDown(final int pixels) {
		final int offset = Math.max(getNavigationComponentHeight() - getScrolledComponentHeight(),
				scrolledCompositeOffset - pixels);
		setBodyCompositeOffset(offset);
	}

	/**
	 * Triggers a single scroll-tick in the direction specified in the
	 * parameter.
	 * 
	 * @param direction
	 *            the direction in which to scroll
	 */
	private void scroll(final ScrollDirection direction) {
		if (direction == ScrollDirection.UP) {
			scrollUp(SCROLLING_STEP);
		} else {
			scrollDown(SCROLLING_STEP);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean scrollTo(final Composite topComp, final Composite bottomComp) {
		final int compBottomEdge = bottomComp.toDisplay(0, bottomComp.getSize().y).y;
		boolean result = scrollToBottom(compBottomEdge);
		final int compTopEdge = topComp.toDisplay(0, 0).y;
		result = result || scrollToTop(compTopEdge);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean scrollTo(final Tree tree) {
		boolean result = false;
		if (tree.getSelectionCount() > 0) {
			final TreeItem item = tree.getSelection()[0];
			final Rectangle itemBounds = item.getBounds();
			final int treeBottomEdge = tree.toDisplay(itemBounds.x, itemBounds.y + tree.getItemHeight()).y;
			result = scrollToBottom(treeBottomEdge);
			final int treeTopEdge = tree.toDisplay(itemBounds.x, itemBounds.y).y;
			result = result || scrollToTop(treeTopEdge);
		}
		return result;
	}

	// helping methods
	//////////////////

	private void scrollToActive() {
		if (!mayScroll()) {
			resetScrolling();
			return;
		}
		final INavigationNode<?> activeNode = getActiveNode();
		if (!scrollTo(activeNode)) {
			ensureFilledToBottom();
		}
	}

	private boolean scrollTo(final INavigationNode<?> activeNode) {
		boolean result = false;
		if (activeNode instanceof IModuleGroupNode) {
			final ModuleGroupView view = navigationComponentProvider
					.getModuleGroupViewForNode((IModuleGroupNode) activeNode);
			result = scrollTo(view, view);
		} else if (activeNode instanceof IModuleNode) {
			result = scrollTo((IModuleNode) activeNode);
		} else if (activeNode instanceof ISubModuleNode) {
			final IModuleNode module = (IModuleNode) activeNode.getParent();
			result = scrollTo(module);
		}
		return result;
	}

	private boolean scrollToBottom(final int bottomEdgeToDisplay) {
		final int bottomEdge = getNavigationComponent().toDisplay(0, getNavigationComponentHeight()).y;
		final boolean scroll = bottomEdgeToDisplay > bottomEdge;
		if (scroll) {
			scrollDown(bottomEdgeToDisplay - bottomEdge);
		}
		return scroll;
	}

	private boolean scrollToTop(final int topEdgeToDisplay) {
		final int topEdge = getNavigationComponent().toDisplay(0, 0).y;
		final boolean scroll = topEdgeToDisplay < topEdge;
		if (scroll) {
			scrollUp(topEdge - topEdgeToDisplay);
		}
		return scroll;
	}

	private void ensureFilledToBottom() {
		// use height
		final int bodyBottomY = scrolledCompositeOffset + getScrolledComponentHeight();
		if (bodyBottomY < getNavigationComponentHeight()) {
			setBodyCompositeOffset(scrolledCompositeOffset + (getNavigationComponentHeight() - bodyBottomY));
		}
	}

	private void resetScrolling() {
		setBodyCompositeOffset(0);
		scrollControlComposite.setVisible(false);
	}

	/**
	 * this method moves the scrolled component upwards and downwards for
	 * simulation of scrolling
	 * 
	 * @param yScrolledOffset
	 *            the new vertical offset of the scrolled composite
	 */
	private void setBodyCompositeOffset(final int yScrolledOffset) {
		if (yScrolledOffset != scrolledCompositeOffset) {
			scrolledCompositeOffset = yScrolledOffset;
			updateUI();
		}
	}

	private void updateUI() {
		final FormData formData = new FormData();
		formData.top = new FormAttachment(0, scrolledCompositeOffset);
		getScrolledComponent().setLayoutData(formData);
		getScrolledComponent().getParent().layout();
	}

	/**
	 * this composite contains the "up"- and "down" buttons. Manual scrolling is
	 * triggered here!
	 */
	private final class ScrollControlComposite extends Composite {

		private Button upButton;
		private Button downButton;

		public ScrollControlComposite(final Composite parent, final int style) {
			super(parent, style);
			getNavigationComponent().addControlListener(new NavigationResizeListener());
			setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.NAVIGATION_BACKGROUND));
			initControlButtons();
		}

		/*
		 * defines the layout
		 */
		private void initControlButtons() {
			setLayout(new FormLayout());

			//final ScrollDelegation delegationAdaper = new ScrollDelegation();
			final ScrollDelegate scrollingButtons = new ScrollDelegate();

			upButton = new Button(this, SWT.NONE);
			upButton.setBackground(this.getBackground());
			upButton.setImage(LnfManager.getLnf().getImage(LnfKeyConstants.NAVIGATION_SCROLL_UP_ICON));
			//upButton.addSelectionListener(delegationAdaper);
			upButton.addMouseListener(scrollingButtons);
			layoutUpButton();

			downButton = new Button(this, SWT.NONE);
			downButton.setBackground(this.getBackground());
			downButton.setImage(LnfManager.getLnf().getImage(LnfKeyConstants.NAVIGATION_SCROLL_DOWN_ICON));
			//downButton.addSelectionListener(delegationAdaper);
			downButton.addMouseListener(scrollingButtons);
			layoutDownButton();
		}

		private void layoutDownButton() {
			final FormData fd = new FormData();
			fd.left = new FormAttachment(upButton, 1);
			fd.right = new FormAttachment(100, -2);
			fd.height = getButtonHeight();
			downButton.setLayoutData(fd);
		}

		private void layoutUpButton() {
			final FormData fd = new FormData();
			fd.left = new FormAttachment(0, 2);
			fd.right = new FormAttachment(50, 0);
			fd.height = getButtonHeight();
			upButton.setLayoutData(fd);
		}

		private final class NavigationResizeListener extends ControlAdapter {
			@Override
			public void controlResized(final ControlEvent e) {
				setVisible(mayScroll());
			}
		}

		//		/**
		//		 * action delegation for manual scrolling
		//		 */
		//		private final class ScrollDelegation extends SelectionAdapter {
		//			@Override
		//			public void widgetSelected(final SelectionEvent e) {
		//				if (e.getSource() == upButton) {
		//					scrollUp(SCROLLING_STEP);
		//				} else {
		//					scrollDown(SCROLLING_STEP);
		//				}
		//			}
		//		}

		/**
		 * Responsible for effecting the manual vertical scrolling in the
		 * navigation tree.
		 */
		private final class ScrollDelegate implements MouseListener {

			private final ScrollRunnable scrollRunnable = new ScrollRunnable();

			/** The runnable that is invoked to trigger the actual scrolling. */
			private class ScrollRunnable implements Runnable {
				/**
				 * Maximum (and default) time interval between triggering
				 * scroll-ticks, in milliseconds
				 */
				private static final int MAX_SCROLL_INTERVAL = 150;

				/** Minimum time interval between scroll-ticks, in ms */
				private static final int MIN_SCROLL_INTERVAL = 30;

				/**
				 * By how much scrolling accelerates after each scroll-tick.
				 * Should be greater than 1 for speed-up, exactly 1 for constant
				 * speed, and less than 1 for slow-down.
				 */
				public static final double SCROLL_INTERVAL_SPEEDUP = 1.05;

				/** In which direction the scroll shall go */
				private volatile ScrollDirection scrollDirection;

				/**
				 * Current speed-adjusted scroll interval. Starts as {@value
				 * MAX_SCROLL_INTERVAL}, never drops below {@value
				 * MIN_SCROLL_INTERVAL}.
				 */
				private int currentScrollInterval;

				public ScrollRunnable() {
					reset();
				}

				void setDirection(final ScrollDirection scrollDirection) {
					this.scrollDirection = scrollDirection;
				}

				public void run() {
					if (getNavigationComponent().isDisposed() || getScrolledComponent().isDisposed()) {
						return;
					}
					scroll(scrollDirection);

					if (currentScrollInterval / SCROLL_INTERVAL_SPEEDUP > MIN_SCROLL_INTERVAL) {
						currentScrollInterval /= SCROLL_INTERVAL_SPEEDUP;
					}
					getDisplay().timerExec(currentScrollInterval, this); // continue scrolling, recurse
				}

				public void reset() {
					currentScrollInterval = MAX_SCROLL_INTERVAL;
				}
			}

			public void mouseDoubleClick(final MouseEvent e) {
			}

			public void mouseDown(final MouseEvent e) {
				if (e.getSource() == upButton) {
					scrollRunnable.setDirection(ScrollDirection.UP);
				} else {
					scrollRunnable.setDirection(ScrollDirection.DOWN);
				}
				getDisplay().timerExec(0, scrollRunnable); // start scrolling
			}

			public void mouseUp(final MouseEvent e) {
				getDisplay().timerExec(-1, scrollRunnable); // stop scrolling
				scrollRunnable.reset();
			}
		}
	}

	private enum ScrollDirection {
		UP, DOWN;
	}

}
