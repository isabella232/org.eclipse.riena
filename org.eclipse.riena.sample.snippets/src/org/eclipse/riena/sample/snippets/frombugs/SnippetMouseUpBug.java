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
package org.eclipse.riena.sample.snippets.frombugs;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.ui.swt.ImageButton;

/**
 * In a special situation the mouse up event isn't fired.
 * <ul>
 * <li>The first button "push" has the focus.</li>
 * <li>Press the third button, the image button with the warning icon</li>
 * <li>Because of focus lost of the push button a dialog is shown.</li>
 * <li>Also the image button receives a mouse down and a mouse exit event.</li>
 * <li>Now the image button displays the pressed icon (question icon).</li>
 * <li>After closing the dialog the pressed icon of the image button is still displayed, that's wrong.<br>
 * A mouse up event isn't fired!</li>
 * </ul>
 * Without showing the dialog the mouse up event is fired.
 * 
 */
public class SnippetMouseUpBug {

	public static void main(final String[] args) {

		final Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 10).spacing(20, 10).applyTo(shell);

			final BtnListener btnListener = new BtnListener();

			final Button b1 = new Button(shell, SWT.PUSH);
			b1.setText("push"); //$NON-NLS-1$
			b1.addMouseListener(btnListener);
			b1.addMouseTrackListener(btnListener);
			b1.addFocusListener(new FocusListener() {

				public void focusLost(final FocusEvent e) {
					System.out.println("focusLost() " + getBtnText(e)); //$NON-NLS-1$
					showDialog(shell);
					//
					// Evil workaround!
					// 
					//					display.asyncExec(new Runnable() {
					//						public void run() {
					//							try {
					//								Thread.sleep(250);
					//							} catch (final InterruptedException ex) {
					//							}
					//						}
					//					}); 
					//					display.asyncExec(new Runnable() {
					//						public void run() {
					//							showDialog(shell);
					//						}
					//					});
				}

				public void focusGained(final FocusEvent e) {
				}
			});

			final Button b2 = new Button(shell, SWT.TOGGLE);
			b2.setText("toggle"); //$NON-NLS-1$
			b2.addMouseListener(btnListener);
			b2.addMouseTrackListener(btnListener);

			final ImageButton b3 = new ImageButton(shell, SWT.NONE);
			b3.setImage(display.getSystemImage(SWT.ICON_WARNING));
			b3.setFocusedImage(display.getSystemImage(SWT.ICON_ERROR));
			b3.setHoverImage(display.getSystemImage(SWT.ICON_INFORMATION));
			b3.setPressedImage(display.getSystemImage(SWT.ICON_QUESTION));
			b3.addMouseListener(btnListener);
			b3.addMouseTrackListener(btnListener);

			b1.setFocus();

			shell.setSize(200, 200);
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} finally {
			display.dispose();
		}

	}

	/**
	 * Creates and shows a dialog.
	 * 
	 * @param parentShell
	 *            parent shell
	 */
	private static void showDialog(final Shell parentShell) {
		final Shell dialog = new Shell(parentShell, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		dialog.setText("Dialog"); //$NON-NLS-1$
		dialog.setSize(120, 100);

		final Button buttonOK = new Button(dialog, SWT.PUSH);
		buttonOK.setText("OK"); //$NON-NLS-1$
		buttonOK.setBounds(25, 20, 80, 25);

		buttonOK.addSelectionListener(new SelectionListener() {
			public void widgetSelected(final SelectionEvent e) {
				dialog.close();
			}

			public void widgetDefaultSelected(final SelectionEvent e) {
				dialog.close();
			}
		});

		dialog.open();
	}

	private static String getBtnText(final TypedEvent e) {
		if (e.getSource() instanceof Button) {
			return ((Button) e.getSource()).getText();
		}
		if (e.getSource() instanceof ImageButton) {
			return "ImageButton"; //$NON-NLS-1$
		}
		return "?"; //$NON-NLS-1$
	}

	/**
	 * This listener logs mouse (and mouse track) events.
	 */
	private static class BtnListener implements MouseListener, MouseTrackListener {

		public void mouseEnter(final MouseEvent e) {
			System.out.println("mouseEnter() " + getBtnText(e)); //$NON-NLS-1$
		}

		public void mouseExit(final MouseEvent e) {
			System.out.println("mouseExit() " + getBtnText(e)); //$NON-NLS-1$
		}

		public void mouseHover(final MouseEvent e) {
			System.out.println("mouseHover() " + getBtnText(e)); //$NON-NLS-1$
		}

		public void mouseDoubleClick(final MouseEvent e) {
			System.out.println("mouseDoubleClick() " + getBtnText(e)); //$NON-NLS-1$
		}

		public void mouseDown(final MouseEvent e) {
			System.out.println("mouseDown() " + getBtnText(e)); //$NON-NLS-1$
		}

		public void mouseUp(final MouseEvent e) {
			System.out.println("mouseUp() " + getBtnText(e)); //$NON-NLS-1$
		}

	}

}
