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
package org.eclipse.riena.internal.navigation.ui.swt;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tracks and restores focus.
 * <p>
 * Will track all focus events on the given Shell and will remember the last
 * focused control (outside of the coolbars). If ESC is pressed on one of the
 * tracked CoolBars it restores the focus to the last focused control.
 * <p>
 * Automatically stops focus tracking when the Shell is disposed. Automatically
 * stops coolbar-key tracking a coolbar is disposed.
 * 
 * @see #RestoreFocusOnEscListener(Display)
 * @see #addControl(CoolBar)
 */
public final class RestoreFocusOnEscListener extends KeyAdapter implements Listener {

	/**
	 * If ESC is pressed on one of these controls we restore the focus to
	 * {@link #savedFocusControl}.
	 */
	private final Set<CoolBar> controlSet;

	/**
	 * The shell we are tracking.
	 */
	private final Shell shell;

	/**
	 * Restore focus to this control if ESC is pressed; could be null or
	 * disposed
	 */
	private Control savedFocusControl;

	/**
	 * Return the first CoolBar in this composite or null.
	 */
	public static CoolBar findCoolBar(final Composite composite) {
		CoolBar result = null;
		for (final Control child : composite.getChildren()) {
			if (child instanceof CoolBar) {
				result = (CoolBar) child;
				break;
			}
		}
		return result;
	}

	/**
	 * Create a new instance of this class.
	 * 
	 * @param shell
	 *            a non-null Shell; will be used to track focus events that
	 *            occur inside this Shell and outside of the tracked coolbars.
	 * @see #addControl(CoolBar)]
	 */
	public RestoreFocusOnEscListener(final Shell shell) {
		Assert.isNotNull(shell);
		controlSet = new HashSet<CoolBar>();
		this.shell = shell;
		this.shell.getDisplay().addFilter(SWT.FocusIn, this);
		this.shell.addListener(SWT.Dispose, new Listener() {
			public void handleEvent(final Event event) {
				event.display.removeFilter(SWT.FocusIn, this);
			}
		});
	}

	/**
	 * Start tracking key events on the given {@code coolBar} and restore the
	 * focus when ESC is pressed. Will stop tracking if the coolbar is disposed.
	 * 
	 * @param coolbar
	 *            a non-null {@link CoolBar}
	 */
	public void addControl(final CoolBar coolbar) {
		Assert.isNotNull(coolbar);
		Assert.isTrue(coolbar.getShell() == shell, "coolbar must be on same shell as this listener"); //$NON-NLS-1$
		if (controlSet.add(coolbar)) {
			for (final Control coolbarChild : coolbar.getChildren()) {
				coolbarChild.addKeyListener(this);
				coolbarChild.addDisposeListener(new DisposeListener() {
					public void widgetDisposed(final DisposeEvent e) {
						((Control) e.widget).removeKeyListener(RestoreFocusOnEscListener.this);
					}
				});
			}
		}
	}

	/**
	 * Stop tracking key events on the given coolbar.
	 * 
	 * @param coolbar
	 *            a non-null {@link CoolBar}
	 */
	public void removeControl(final CoolBar coolbar) {
		Assert.isNotNull(coolbar);
		if (controlSet.remove(coolbar)) {
			for (final Control coolbarChild : coolbar.getChildren()) {
				coolbarChild.removeKeyListener(this);
			}
		}
	}

	/**
	 * Should not be called directly.
	 * <p>
	 * When ESC is released, this listener restores the focus to the control
	 * that had it before the menu bar became focused.
	 */
	@Override
	public void keyReleased(final KeyEvent e) {
		if (e.keyCode == 27) { // 27 is ESC
			// System.out.println("ESC on " + e.widget);
			if (!SwtUtilities.isDisposed(savedFocusControl)) {
				savedFocusControl.setFocus();
			}
		}
	}

	/**
	 * Should not be called directly.
	 */
	public void handleEvent(final Event event) {
		if (event.widget instanceof Control) {
			final Control control = (Control) event.widget;
			if (contains(shell, control) && !contains(controlSet, control)) {
				// System.out.println("savedFocusControl: " + control);
				savedFocusControl = control;
			}
		}
	}

	// helping methods
	//////////////////

	private boolean contains(final Composite container, final Control control) {
		boolean result = container == control;
		Composite parent = control.getParent();
		while (!result && parent != null) {
			result = container == parent;
			parent = parent.getParent();
		}
		return result;
	}

	private boolean contains(final Collection<CoolBar> containers, final Control control) {
		boolean result = false;
		for (final Composite container : containers) {
			result = result || contains(container, control);
			if (result) {
				break;
			}
		}
		return result;
	}
}
