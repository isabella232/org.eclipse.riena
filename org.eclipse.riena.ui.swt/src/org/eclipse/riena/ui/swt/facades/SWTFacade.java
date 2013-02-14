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
package org.eclipse.riena.ui.swt.facades;

import java.util.EventListener;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.ui.swt.CompletionCombo;
import org.eclipse.riena.ui.swt.EmbeddedTitleBar;
import org.eclipse.riena.ui.swt.InfoFlyout;

/**
 * Single-sourced access to SWT methods that are not available in RAP.
 * <p>
 * <b>Note:</b> The RCP implementation delegates to the appropriate SWT methods. The RAP implementation does nothing (as this functionality is missing in RAP).
 * 
 * @since 2.0
 */
public abstract class SWTFacade {

	private static final SWTFacade INSTANCE = FacadeFactory.newFacade(SWTFacade.class);

	/**
	 * Draw constant indicating whether the string drawing operation should handle mnemonics (value is 1&lt;&lt;3).
	 * 
	 * @since 3.0
	 */
	public static final int DRAW_MNEMONIC = 1 << 3;

	/**
	 * Traversal event detail field value indicating that the key which designates that focus should be given to the previous tab item was pressed; typically,
	 * this is either the LEFT-ARROW or UP-ARROW keys (value is 1&lt;&lt;5).
	 * 
	 * @since 3.0
	 */
	public static final int TRAVERSE_ARROW_PREVIOUS = 1 << 5;

	/**
	 * Traversal event detail field value indicating that the key which designates that focus should be given to the previous tab item was pressed; typically,
	 * this is either the RIGHT-ARROW or DOWN-ARROW keys (value is 1&lt;&lt;6).
	 * 
	 * @since 3.0
	 */
	public static final int TRAVERSE_ARROW_NEXT = 1 << 6;

	/**
	 * Traversal event detail field value indicating that a mnemonic key sequence was pressed (value is 1&lt;&lt;7).
	 */
	public static final int TRAVERSE_MNEMONIC = 1 << 7;

	/**
	 * Style constant for right to left orientation (value is 1&lt;&lt;26).
	 * <p>
	 * When orientation is not explicitly specified, orientation is inherited. This means that children will be assigned the orientation of their parent. To
	 * override this behavior and force an orientation for a child, explicitly set the orientation of the child when that child is created. <br>
	 * Note that this is a <em>HINT</em>.
	 * </p>
	 * <p>
	 * <b>Used By:</b>
	 * <ul>
	 * <li><code>Control</code></li>
	 * <li><code>Menu</code></li>
	 * <li><code>GC</code></li>
	 * </ul>
	 * </p>
	 * 
	 * @since 3.0
	 */
	public static final int RIGHT_TO_LEFT = 1 << 26;

	/**
	 * The mouse move event type (value is 5).
	 * 
	 * @see org.eclipse.swt.widgets.Widget#addListener
	 * @see org.eclipse.swt.widgets.Display#addFilter
	 * @see org.eclipse.swt.widgets.Event
	 * 
	 * @see org.eclipse.swt.widgets.Control#addMouseMoveListener
	 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove
	 * @see org.eclipse.swt.events.MouseEvent
	 * 
	 * @since 3.0
	 */
	public static final int MouseMove = 5;

	/**
	 * The mouse enter event type (value is 6).
	 * 
	 * @see org.eclipse.swt.widgets.Widget#addListener
	 * @see org.eclipse.swt.widgets.Display#addFilter
	 * @see org.eclipse.swt.widgets.Event
	 * 
	 * @see org.eclipse.swt.widgets.Control#addMouseTrackListener
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseEnter
	 * @see org.eclipse.swt.events.MouseEvent
	 * 
	 * @since 3.0
	 */
	public static final int MouseEnter = 6;

	/**
	 * The mouse exit event type (value is 7).
	 * 
	 * @see org.eclipse.swt.widgets.Widget#addListener
	 * @see org.eclipse.swt.widgets.Display#addFilter
	 * @see org.eclipse.swt.widgets.Event
	 * 
	 * @see org.eclipse.swt.widgets.Control#addMouseTrackListener
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseExit
	 * @see org.eclipse.swt.events.MouseEvent
	 * 
	 * @since 3.0
	 */
	public static final int MouseExit = 7;

	/**
	 * The paint event type (value is 9).
	 * 
	 * @see org.eclipse.swt.widgets.Widget#addListener
	 * @see org.eclipse.swt.widgets.Display#addFilter
	 * @see org.eclipse.swt.widgets.Event
	 * 
	 * @see org.eclipse.swt.widgets.Control#addPaintListener
	 * @see org.eclipse.swt.events.PaintListener#paintControl
	 * @see org.eclipse.swt.events.PaintEvent
	 * 
	 * @since 3.0
	 */
	public static final int Paint = 9;

	/**
	 * The mouse hover event type (value is 32).
	 * 
	 * @see org.eclipse.swt.widgets.Widget#addListener
	 * @see org.eclipse.swt.widgets.Display#addFilter
	 * @see org.eclipse.swt.widgets.Event
	 * 
	 * @see org.eclipse.swt.widgets.Control#addMouseTrackListener
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseHover
	 * @see org.eclipse.swt.events.MouseEvent
	 * 
	 * @since 3.0
	 */
	public static final int MouseHover = 32;

	/**
	 * The mouse wheel event type (value is 37). This is a synonym for {@link #MouseVerticalWheel} (value is 37). Newer applications should use
	 * {@link #MouseVerticalWheel} instead of {@link #MouseWheel} to make code more understandable.
	 * 
	 * @see org.eclipse.swt.widgets.Widget#addListener
	 * @see org.eclipse.swt.widgets.Display#addFilter
	 * @see org.eclipse.swt.widgets.Event
	 */
	public static final int MouseWheel = 37;

	/**
	 * The applicable implementation of this class.
	 */
	public static final SWTFacade getDefault() {
		return INSTANCE;
	}

	/**
	 * Returns true if the trident animation library is available.
	 */
	public static final boolean hasTrident() {
		return Platform.getBundle("org.pushingpixels.trident") != null; //$NON-NLS-1$
	}

	/**
	 * Returns true if running on the RAP platform, false otherwise.
	 */
	public static final boolean isRAP() {
		return "rap".equals(SWT.getPlatform()); //$NON-NLS-1$
	}

	/**
	 * Returns true if running on the RCP platform, false otherwise.
	 */
	public static final boolean isRCP() {
		return !SWTFacade.isRAP();
	}

	/**
	 * Adds an SWT.EraseItem listener to the given table.
	 * 
	 * @param table
	 *            a {@link Table} instance; never null
	 * @param listener
	 *            a {@link Listener} instance; never null
	 */
	public abstract void addEraseItemListener(Table table, Listener listener);

	/**
	 * Adds an SWT.EraseItem listener to the given tree.
	 * 
	 * @param tree
	 *            a {@link Tree} instance; never null
	 * @param listener
	 *            a {@link Listener} instance; never null
	 */
	public abstract void addEraseItemListener(Tree tree, Listener listener);

	/**
	 * Adds the given listener as an SWT.MouseExit filter to the given display.
	 * 
	 * @param display
	 *            a Display instance; never null
	 * @param listener
	 *            a {@link Listener}; never null
	 * 
	 * @since 3.0
	 */
	public abstract void addFilterMouseExit(Display display, Listener listener);

	/**
	 * Adds the given listener as an SWT.MouseMove filter to the given display.
	 * 
	 * @param display
	 *            a Display instance; never null
	 * @param listener
	 *            a {@link Listener}; never null
	 * 
	 * @since 3.0
	 */
	public abstract void addFilterMouseMove(Display display, Listener listener);

	/**
	 * Adds the given listener as an SWT.MouseWheel filter to the given display.
	 * 
	 * @param display
	 *            a Display instance; never null
	 * @param listener
	 *            a {@link Listener}; never null
	 * 
	 * @since 3.0
	 */
	public abstract void addFilterMouseWheel(Display display, Listener listener);

	/**
	 * Adds all listeners of a certain {@code eventType} to the given control.
	 * <p>
	 * The implementation in this facade ensures that differences in the treatment of typed / untyped listeners between SWT and RWT are masked away.
	 * 
	 * @param control
	 *            a {@link Control} instance; never null
	 * @param eventType
	 *            one of the following event types: {@link SWT#Verify}, {@link SWT#Modify}
	 * @param listeners
	 *            an array of appropriate listeners. Note: actual types differ between SWT and RWT.
	 * @throws IllegalArgumentException
	 *             if an unsupported event type is used
	 * 
	 * @since 3.0
	 */
	public void addListeners(final Control control, final int eventType, final Object[] listeners) {
		if (eventType == SWT.Modify) {
			addModifyListeners(control, listeners);
		} else if (eventType == SWT.Verify) {
			addVerifyListeners(control, listeners);
		} else {
			throw new IllegalArgumentException("Unsupported eventType: " + eventType); //$NON-NLS-1$
		}
	}

	/**
	 * Adds a MouseMoveListener to the given control.
	 * 
	 * @param control
	 *            a {@link Control} instance; never null
	 * @param listener
	 *            an Object that implements the MouseMoveListener interface, or null
	 * @since 3.0
	 */
	public abstract void addMouseMoveListener(Control control, MouseMoveListener listener);

	/**
	 * Adds a MouseTrackListener to the given control.
	 * 
	 * @param control
	 *            a {@link Control} instance; never null
	 * @param listener
	 *            an {@link MouseTrackListener}; never null
	 */
	public abstract void addMouseTrackListener(Control control, MouseTrackListener listener);

	/**
	 * Adds an SWT.PaintItem listener to the given tree.
	 * 
	 * @param tree
	 *            a {@link Tree} instance; never null
	 * @param listener
	 *            a {@link Listener} instance; never null
	 */
	public abstract void addPaintItemListener(Tree tree, Listener listener);

	/**
	 * Adds a PaintListener to the given control.
	 * 
	 * @param control
	 *            a {@link Control} instance; never null
	 * @param listener
	 *            an EventListener that implements the PaintListener interface, or null
	 */
	public abstract void addPaintListener(Control control, EventListener listener);

	/**
	 * Attaches a platform specific {@link ModuleNavigationListener} instance to the given tree
	 * 
	 * @param tree
	 *            a {@link Tree} instance; never null
	 * @since 3.0
	 */
	public abstract void attachModuleNavigationListener(Tree tree);

	/**
	 * Copies the value of the {@code keyLocation} field from the source Event to the target Event.
	 * 
	 * @param from
	 *            the source Event instance; never null
	 * @param target
	 *            the target Event instance; never null
	 * 
	 * @since 3.0
	 */
	public abstract void copyEventKeyLocation(Event source, Event target);

	/**
	 * Creates a platform specific {@link CompletionCombo} instance.
	 * 
	 * @param parent
	 *            the parent {@link Composite}; never null
	 * @param style
	 *            the style bits of the widget to construct. Must be a value supported by {@link Composite}
	 * @return an {@link CompletionCombo} instance; never null
	 * 
	 * @since 3.0
	 */
	public abstract CompletionCombo createCompletionCombo(Composite parent, int style);

	/**
	 * Creates a platform specific {@link CompletionCombo} instance (with support for images).
	 * 
	 * @param parent
	 *            the parent {@link Composite}; never null
	 * @param style
	 *            the style bits of the widget to construct. Must be a value supported by {@link Composite}
	 * @return an {@link CompletionCombo} instance; never null
	 * 
	 * @since 3.0
	 */
	public abstract CompletionCombo createCompletionComboWithImage(Composite parent, int style);

	/**
	 * Create a custom cursor from the given {@code cursorImage}. If the image is null, or custom cursors are not supported, create a standard cursor with the
	 * given {@code alternateStyle}.
	 * 
	 * @param display
	 *            a Display instance; never null
	 * @param cursorImage
	 *            the Image to be used for the cursor; may be null
	 * @param alternateStyle
	 *            an alternate style for the cursor; must be one of the SWT.CURSOR_XYZ constants
	 * @return a Cursor instance; never null. Clients must dispose the returned instance once no longer needed.
	 */
	public abstract Cursor createCursor(Display display, Image cursorImage, int alternateStyle);

	/**
	 * Returns a paint listener for modifying the disabled look of a control.
	 * 
	 * @return a PaintListener or null (in RAP)
	 */
	public abstract EventListener createDisabledPainter();

	/**
	 * Attaches a drag-to-resize-listener with an SWT Tracker to the bottom right corner of the given control.
	 * 
	 * @param control
	 *            a {@link Control} instance; never null
	 * 
	 * @since 3.0
	 */
	public abstract void createGrabCornerListenerWithTracker(Control control);

	/**
	 * Creates a platform specific {@link InfoFlyout} instance.
	 * 
	 * @param parent
	 *            the parent {@link Composite}; never null
	 * @return an {@link InfoFlyout} instance; never null
	 * 
	 * @since 3.0
	 */
	public abstract InfoFlyout createInfoFlyout(Composite parent);

	/**
	 * Create a custom tooltip on an {@link EmbeddedTitleBar} control
	 * 
	 * @param parent
	 *            the parent {@link EmbeddedTitleBar} instance; never null
	 * @since 3.0
	 */
	public abstract void createEmbeddedTitleBarToolTip(EmbeddedTitleBar parent);

	/**
	 * Create a custom tooltip on a SubModule navigation control (i.e. in implementation terms: a TreeItem in the Tree).
	 * 
	 * @param parent
	 *            a {@link Tree} instance; never null
	 * @param labelProvider
	 *            an {@link ILabelProvider} instance; never null. It has to return the text for the tooltip, presumably based on the corresponding sub-module
	 *            navigation node.
	 * @since 3.0
	 */
	public abstract void createSubModuleToolTip(Tree parent, ILabelProvider labelProvider);

	/**
	 * @since 4.0
	 */
	public abstract void createSubApplicationToolTip(Control parent);

	/**
	 * Returns an SWT.EraseItem / SWT.PaintItem listener, that will paint all tree cells empty when the tree is disabled.
	 * 
	 * @return a Listener or null (in RAP)
	 */
	public abstract Listener createTreeItemEraserAndPainter();

	/**
	 * Returns a multiline radio button. This method is needed until SWT Bug 400248 is fixed.
	 * 
	 * @param parent
	 *            the parent composite
	 * @param style
	 *            the requested button style which will be bitwise OR-ed with SWT.WRAP
	 * @return the radio {@link Button}
	 * @since 5.0
	 */
	public abstract Button createMultilineButton(Composite parent, int style);

	/**
	 * Posts the given event on the display.
	 * 
	 * @param display
	 *            a {@link Display} instance; never null
	 * @param event
	 *            an {@link Event} instance; never null
	 * @return true if the event was generated, false otherwise
	 */
	public abstract boolean postEvent(Display display, Event event);

	/**
	 * Removes an SWT.EraseItem listener from the given table.
	 * 
	 * @param table
	 *            a {@link Table} instance; never null
	 * @param listener
	 *            a {@link Listener} instance; never null
	 */
	public abstract void removeEraseItemListener(Table table, Listener listener);

	/**
	 * Removes an SWT.EraseItem listener from the given tree.
	 * 
	 * @param tree
	 *            a {@link Tree} instance; never null
	 * @param listener
	 *            a {@link Listener} instance; never null
	 */
	public abstract void removeEraseItemListener(Tree tree, Listener listener);

	/**
	 * Removes the given listener as an SWT.MouseWheel filter to the given display.
	 * 
	 * @param display
	 *            a Display instance; never null
	 * @param listener
	 *            a {@link Listener}; never null
	 * 
	 * @since 3.0
	 */
	public abstract void removeFilterMouseWheel(Display display, Listener listener);

	/**
	 * Removes a MouseMoveListener from the given control.
	 * 
	 * @param control
	 *            a {@link Control} instance; never null
	 * @param listener
	 *            an Object that implements the MouseMoveListener interface, or null
	 */
	public abstract void removeMouseMoveListener(Control control, Object listener);

	/**
	 * Removes a MouseTrackListener from the given control.
	 * 
	 * @param control
	 *            a {@link Control} instance; never null
	 * @param listener
	 *            a {@link MouseTrackListener} instance; never null
	 */
	public abstract void removeMouseTrackListener(Control control, MouseTrackListener listener);

	/**
	 * Removes an SWT.PaintItem listener from the given tree.
	 * 
	 * @param tree
	 *            a {@link Tree} instance; never null
	 * @param listener
	 *            a {@link Listener} instance; never null
	 */
	public abstract void removePaintItemListener(Tree tree, Listener listener);

	/**
	 * Removes a PaintListener from the given control.
	 * 
	 * @param control
	 *            a {@link Control} instance; never null
	 * @param listener
	 *            an EventListener that implements the PaintListener interface, or null
	 */
	public abstract void removePaintListener(Control control, EventListener listener);

	/**
	 * Removes all listeners of a certain {@code eventType} from the given control.
	 * <p>
	 * <b>Do not use {@link Widget#removeListener(int, Listener)} !</b> The implementation in this facade ensures that differences in the treatment of typed /
	 * untyped listeners between SWT and RWT are masked away.
	 * 
	 * @param control
	 *            a {@link Control} instance; never null
	 * @param eventType
	 *            one of the following event types: {@link SWT#Verify}, {@link SWT#Modify}
	 * @return an array of removed listeners; never null. Avoid casting to actual types, as they differ between SWT and RWT.
	 * @throws IllegalArgumentException
	 *             if an unsupported event type is used
	 * 
	 * @since 3.0
	 */
	public Object[] removeListeners(final Control control, final int eventType) {
		if (eventType == SWT.Modify) {
			return removeModifyListeners(control);
		} else if (eventType == SWT.Verify) {
			return removeVerifyListeners(control);
		}
		throw new IllegalArgumentException("Unsupported eventType: " + eventType); //$NON-NLS-1$
	}

	/**
	 * This method is called before the first InfoFlyout message is processed
	 * 
	 * @param flyout
	 *            a {@link InfoFlyout} instance, never null
	 * @since 3.0
	 */
	public void beforeInfoFlyoutShow(final InfoFlyout flyout) {
		// nothing by default
	}

	/**
	 * This method is called after all InfoFlyout messages are processed
	 * 
	 * @param flyout
	 *            a {@link InfoFlyout} instance, never null
	 * @since 3.0
	 */
	public void afterInfoFlyoutShow(final InfoFlyout flyout) {
		// nothing by default
	}

	/**
	 * Perform a platform specific traversal action as indicated by the value of the {@code traversal} argument. The following values are supported:
	 * <ul>
	 * <li>SWT.TRAVERSE_ARROW_NEXT</li>
	 * <li>SWT.TRAVERSE_ARROW_PREVIOUS</li>
	 * <li>SWT.TRAVERSE_ESCAPE</li>
	 * <li>SWT.TRAVERSE_PAGE_NEXT</li>
	 * <li>SWT.TRAVERSE_PAGE_PREVIOUS</li>
	 * <li>SWT.TRAVERSE_RETURN</li>
	 * <li>SWT.TRAVERSE_TAB_NEXT</li>
	 * <li>SWT.TRAVERSE_TAB_PREVIOUS</li>
	 * </ul>
	 * 
	 * @param control
	 *            the {@link Control} to traverse; never null
	 * @param traversal
	 *            one of the traversal values listed above
	 * @return true on successful traversal, false otherwise
	 * 
	 * @since 3.0
	 */
	public abstract boolean traverse(Control control, int traversal);

	/**
	 * Sets the amount that the receiver's value will be modified by when the up/down (or right/left) arrows are pressed to the argument, which must be at least
	 * one.
	 * 
	 * @param scrollBar
	 *            the receiver of the new increment
	 * @param value
	 *            the new increment (must be greater than zero)
	 * @since 3.0
	 */
	public abstract void setIncrement(ScrollBar scrollBar, int value);

	// protecected methods
	//////////////////////

	/**
	 * Adds an array of {@link SWT#Modify} listeners to the given control.
	 * 
	 * @param control
	 *            a {@link Control} instance; never null
	 * @param listeners
	 *            an array of appropriate listeners. Note: actual types differ between SWT and RWT.
	 * 
	 * @since 3.0
	 */
	protected abstract void addModifyListeners(final Control control, Object[] listeners);

	/**
	 * Adds an array of {@link SWT#Verify} listeners to the given control.
	 * 
	 * @param control
	 *            a {@link Control} instance; never null
	 * @param listeners
	 *            an array of appropriate listeners. Note: actual types differ between SWT and RWT.
	 * 
	 * @since 3.0
	 */
	protected abstract void addVerifyListeners(final Control control, Object[] listeners);

	/**
	 * Removes all {@link SWT#Modify} listeners from the given control.
	 * 
	 * @param control
	 *            a {@link Control} instance; never null
	 * @return an array of removed listeners; never null. Avoid casting to actual types, as they differ between SWT and RWT.
	 * 
	 * @since 3.0
	 */
	protected abstract Object[] removeModifyListeners(final Control control);

	/**
	 * 8 Removes all {@link SWT#Verify} listeners from the given control.
	 * 
	 * @param control
	 *            a {@link Control} instance; never null
	 * @return an array of removed listeners; never null. Avoid casting to actual types, as they differ between SWT and RWT.
	 * 
	 * @since 3.0
	 */
	protected abstract Object[] removeVerifyListeners(final Control control);
}
