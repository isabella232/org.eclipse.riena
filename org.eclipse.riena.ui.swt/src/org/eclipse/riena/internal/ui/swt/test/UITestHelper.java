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
package org.eclipse.riena.internal.ui.swt.test;

import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.ui.swt.facades.SWTFacade;

/**
 * Utility class for UI tests.
 */
public final class UITestHelper {

	/** Keycode for 'arrow down' (16777218) */
	public static final int KC_ARROW_DOWN = 16777218;
	/** Keycode for 'arrow up' (16777217) */
	public static final int KC_ARROW_UP = 16777217;
	/** Keycode for 'home' (16777223) */
	public static final int KC_HOME = 16777223;
	/** Keycode for 'end' (16777224) */
	public static final int KC_END = 16777224;
	/** Keycode for 'del' (127) */
	public static final int KC_DEL = 127;

	private UITestHelper() {
		// prevent instantiation
	}

	public static void fireSelectionEvent(final Widget widget) {
		final Event event = new Event();
		event.type = SWT.Selection;
		event.widget = widget;
		widget.notifyListeners(SWT.Selection, event);
	}

	public static void readAndDispatch(final Widget control) {
		while (!control.isDisposed() && control.getDisplay().readAndDispatch()) {
			Nop.reason("keep going"); //$NON-NLS-1$
		}
	}

	/**
	 * Send a key event with the given keycode.
	 * 
	 * @param display
	 *            a non-null {@link Display} instance
	 * @param keyCode
	 *            a SWT key code
	 */
	public static void sendKeyAction(final Display display, final int keyCode) {
		final EventSender sender = new EventSender(display, keyCode);
		send(display, sender);
	}

	/**
	 * Send the give message as a series of key events
	 * 
	 * @param display
	 *            a non-null {@link Display} instance
	 * @param message
	 *            a non-null String
	 */
	public static void sendString(final Display display, final String message) {
		final EventSender sender = new EventSender(display, message);
		send(display, sender);
	}

	private static void send(final Display display, final Runnable runnable) {
		final Thread thread = new Thread(runnable);
		final Shell activeShell = display.getActiveShell();
		if (activeShell != null) {
			activeShell.forceActive();
		}
		thread.start();
		waitAndDispatch(display, thread);
	}

	private static void waitAndDispatch(final Display display, final Thread thread) {
		final Shell shell = display.getActiveShell();
		while (!shell.isDisposed() && thread.isAlive()) {
			try {
				display.readAndDispatch();
				sleepForShortWhile();
			} catch (final SWTException exc) {
				// swallow this kind of exception
				if (!exc.toString().contains("Workbench has not been created yet.")) { //$NON-NLS-1$
					throw exc;
				}
			}
		}

		// There is a risk for a race condition here: If the scheduler preempted the main thread
		// executing this method after entering the while loop and assignd computing power to
		// the event posting thread and if that thread finishes its execution in one shot, the
		// main thread will not enter the first while-loop again and would loose all events
		// posted by the other thread.
		// Solution: Check here again if there are further events to be processed.
		while (display.readAndDispatch()) {
			sleepForShortWhile();
		}
	}

	private static void sleepForShortWhile() {
		try {
			Thread.sleep(20);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Wraps and sends keyboard events to the shell.
	 */
	private static class EventSender implements Runnable {

		private static final int MS_SHORT_WAIT = 10;
		private static final int MS_LONG_WAIT = 10;

		private final Display display;
		private final int keyCode;
		private final String message;
		private final SWTFacade swtFacade;

		EventSender(final Display display, final int keyCode) {
			this.display = display;
			this.keyCode = keyCode;
			this.message = null;
			this.swtFacade = SWTFacade.getDefault();
		}

		EventSender(final Display display, final String message) {
			this.display = display;
			this.keyCode = 0;
			this.message = message;
			this.swtFacade = SWTFacade.getDefault();
		}

		public void run() {
			if (message != null) {
				sendMessage();
			} else {
				sendKeyEvent();
			}
		}

		/**
		 * Posting a character key-down event apparently simulates pressing the
		 * <em>physical-keyboard</em> key that is bound to the character.
		 * 
		 * On Win32 at least, the OS's input manager for switching between
		 * different logical keyboard layouts is ignored. So with a physical
		 * QWERTZ keyboard, '/' is invariably sent as '7', and '&' as '6'.
		 */
		private void sendMessage() {
			for (int i = 0; i < message.length(); i++) {
				final char ch = message.charAt(i);
				final boolean isShift = doShift(ch);
				if (isShift) {
					postShift(true);
				}
				postCharacter(ch);
				if (isShift) {
					postShift(false);
				}
			}
		}

		/**
		 * @param ch
		 *            character to decide upon
		 * @return whether the character necessitates sending 'Shift' KeyDown/Up
		 *         events
		 */
		private boolean doShift(final char ch) {
			boolean result = false;
			if (Character.isLetter(ch)) {
				result = Character.isUpperCase(ch);
			} else if (ch == '/' && "US" != Locale.getDefault().getCountry()) { //$NON-NLS-1$
				result = true;
			}
			return result;
		}

		private void sendKeyEvent() {
			final Event event = new Event();
			event.type = SWT.KeyDown;
			event.keyCode = keyCode;
			doSleep(MS_LONG_WAIT);
			post(event);
			doSleep(MS_LONG_WAIT);
			event.type = SWT.KeyUp;
			post(event);
			doSleep(MS_LONG_WAIT);
		}

		private void postCharacter(final char ch) {
			final Event event = new Event();
			event.type = SWT.KeyDown;
			event.character = ch;
			doSleep(MS_LONG_WAIT);
			post(event);
			doSleep(MS_LONG_WAIT);
			event.type = SWT.KeyUp;
			post(event);
			doSleep(MS_LONG_WAIT);
		}

		private void postShift(final boolean keyDown) {
			final Event event = new Event();
			event.keyCode = SWT.SHIFT;
			event.type = keyDown ? SWT.KeyDown : SWT.KeyUp;
			post(event);
			doSleep(MS_SHORT_WAIT);
		}

		private boolean post(final Event event) {
			return swtFacade.postEvent(display, event);
		}

		private void doSleep(final int millis) {
			try {
				Thread.sleep(millis);
			} catch (final InterruptedException iex) {
				Nop.reason("ignore"); //$NON-NLS-1$
			}
		}

	}

}
