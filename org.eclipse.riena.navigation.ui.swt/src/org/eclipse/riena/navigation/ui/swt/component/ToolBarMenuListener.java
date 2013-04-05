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
package org.eclipse.riena.navigation.ui.swt.component;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import org.eclipse.riena.ui.swt.facades.SWTFacade;

/**
 * This class helps to reproduce a menu behavior using a ToolBar. It is only intended for usage by the {@link MenuCoolBarComposite}.
 * <p>
 * public for testing
 */
public class ToolBarMenuListener implements MouseListener, SelectionListener, MouseTrackListener, TraverseListener {
	/**
	 * The item, for which the menu is currently visible.
	 */
	private ToolItem activeItem;

	/**
	 * The selection state of the active item can change
	 */
	private boolean activeItemSelectionState;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDown(final MouseEvent e) {
		// because of timing issues, mouseDown(...) is used to handle item selections
		final ToolItem toolItem = getItem(e);
		activateItem(toolItem);
	}

	/**
	 * @param toolItem
	 */
	private void activateItem(final ToolItem toolItem) {
		if (toolItem != null && toolItem != activeItem) {
			showMenu(toolItem);
		} else {
			if (activeItem != null) {
				setActiveItemSelectionState(false);
			}
			activeItem = null;
		}
	}

	private ToolItem getItem(final MouseEvent e) {
		if (e.getSource() instanceof ToolBar) {
			final ToolBar bar = (ToolBar) e.getSource();
			return bar.getItem(new Point(e.x, e.y));
		}
		return null;
	}

	private void setActiveItemSelectionState(final boolean selectionState) {
		activeItem.setSelection(selectionState);
		activeItemSelectionState = selectionState;
	}

	/**
	 * Computes the position displays the {@link Menu}, attached to the given {@link ToolItem}. This method relies on the fact, that the tool item has a data
	 * field named {@link MenuCoolBarComposite}.MENU_DATA_KEY with the menu, which is already created. Also the menu must be already created.
	 * <p>
	 * See <tt>MenuCoolBarComposite.createAndAddMenu(...)</tt>
	 * 
	 * @param toolItem
	 *            the {@link ToolItem} for which the menu will be shown
	 */
	private void showMenu(final ToolItem toolItem) {
		final Rectangle itemBounds = toolItem.getBounds();
		final Point loc = toolItem.getParent().toDisplay(itemBounds.x, itemBounds.height + itemBounds.y);
		final Object data = toolItem.getData(MenuCoolBarComposite.MENU_DATA_KEY);
		Assert.isTrue(data instanceof Menu, "Every tool item must know its associated menu object under the key: " + MenuCoolBarComposite.MENU_DATA_KEY); //$NON-NLS-1$
		final Menu menu = (Menu) data;
		menu.setLocation(loc);
		activeItem = toolItem;
		setActiveItemSelectionState(true);
		menu.setVisible(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected(final SelectionEvent e) {
		if (e.getSource() instanceof ToolItem) {
			// because of timing issues, mouseDown(...) is used to handle item selections
			// here we only ensure that the item preserves its consistent selection state
			((ToolItem) e.getSource()).setSelection(activeItemSelectionState);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.TraverseListener#keyTraversed(org.eclipse.swt.events.TraverseEvent)
	 */
	public void keyTraversed(final TraverseEvent evt) {
		if (evt.detail == SWTFacade.TRAVERSE_MNEMONIC && evt.getSource() instanceof ToolBar) {
			activateItem(null);
			activateItem(getItem(evt.character, (ToolBar) evt.getSource()));
		}
	}

	private ToolItem getItem(final char mnemonic, final ToolBar toolBar) {
		String mnemonicStrg = "&" + mnemonic; //$NON-NLS-1$
		mnemonicStrg = mnemonicStrg.toLowerCase();

		for (final ToolItem item : toolBar.getItems()) {
			String label = item.getText();
			if (label != null) {
				label = label.toLowerCase();
				if (label.contains(mnemonicStrg)) {
					if (label.indexOf('&') == label.indexOf(mnemonicStrg)) {
						return item;
					}
				}
			}
		}

		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseEnter(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseEnter(final MouseEvent e) {
		activeItem = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp(final MouseEvent e) {
		// unused
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDoubleClick(final MouseEvent e) {
		// unused
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(final SelectionEvent e) {
		// unused
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseExit(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseExit(final MouseEvent e) {
		// unused
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseHover(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseHover(final MouseEvent e) {
		// unused
	}
}
