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
package org.eclipse.riena.ui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.swt.utils.ImageStore;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link ImageButton}.
 */
@UITestCase
public class ImageButtonTest extends RienaTestCase {

	private Shell shell;
	private ImageButton button;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		shell = new Shell();
		button = new ImageButton(shell, SWT.NONE);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		SwtUtilities.dispose(button);
		SwtUtilities.dispose(shell);
	}

	/**
	 * Tests the <i>private</i> method {@code addListeners()}. The method is
	 * called in the constructor of the {@code ImageButton}. So the method will
	 * no be call one more time in the this test method.
	 */
	public void testAddListeners() {

		// PaintListener added?
		assertEquals(1, button.getListeners(SWT.Paint).length);
		// MouseListener added?
		assertEquals(1, button.getListeners(SWT.MouseDown).length);
		// MouseTrackListener added?
		assertEquals(1, button.getListeners(SWT.MouseEnter).length);
		// MouseMoveListener added?
		assertEquals(1, button.getListeners(SWT.MouseMove).length);
		// FocusListener added?
		assertEquals(1, button.getListeners(SWT.FocusIn).length);
		// KeyListener added?
		assertEquals(1, button.getListeners(SWT.KeyUp).length);
		// TraverseListener added?
		assertEquals(1, button.getListeners(SWT.Traverse).length);
		// DisposeListener added?
		assertEquals(1, button.getListeners(SWT.Dispose).length);

		button = new ImageButton(shell, SWT.HOT);
		final Button hoverButton = ReflectionUtils.getHidden(button, "hoverButton");
		// MouseListener added?
		assertEquals(1, hoverButton.getListeners(SWT.MouseDown).length);
		// MouseTrackListener added?
		assertEquals(1, hoverButton.getListeners(SWT.MouseEnter).length);
		// MouseMoveListener added?
		assertEquals(1, hoverButton.getListeners(SWT.MouseMove).length);

	}

	/**
	 * Tests the <i>private</i> method {@code removeListeners()}. The
	 * constructor of the {@code ImageButton} adds all the listeners.
	 */
	public void testRemoveListeners() {

		ReflectionUtils.invokeHidden(button, "removeListeners");

		// PaintListener removed?
		assertEquals(0, button.getListeners(SWT.Paint).length);
		// MouseListener removed?
		assertEquals(0, button.getListeners(SWT.MouseDown).length);
		// MouseTrackListener removed?
		assertEquals(0, button.getListeners(SWT.MouseEnter).length);
		// MouseMoveListener removed?
		assertEquals(0, button.getListeners(SWT.MouseMove).length);
		// FocusListener removed?
		assertEquals(0, button.getListeners(SWT.FocusIn).length);
		// KeyListener removed?
		assertEquals(0, button.getListeners(SWT.KeyUp).length);
		// TraverseListener removed?
		assertEquals(0, button.getListeners(SWT.Traverse).length);
		// DisposeListener removed?
		assertEquals(0, button.getListeners(SWT.Dispose).length);

		button = new ImageButton(shell, SWT.HOT);
		ReflectionUtils.invokeHidden(button, "removeListeners");
		final Button hoverButton = ReflectionUtils.getHidden(button, "hoverButton");
		// MouseListener added?
		assertEquals(0, hoverButton.getListeners(SWT.MouseDown).length);
		// MouseTrackListener added?
		assertEquals(0, hoverButton.getListeners(SWT.MouseEnter).length);
		// MouseMoveListener added?
		assertEquals(0, hoverButton.getListeners(SWT.MouseMove).length);

	}

	/**
	 * Tests the <i>private</i> method {@code getImageToDraw()}.
	 */
	public void testGetImageToDraw() {

		Image retImage = ReflectionUtils.invokeHidden(button, "getImageToDraw");
		assertNull(retImage);

		final Image image = ImageStore.getInstance().getImage("imagebutton.png");
		button.setImage(image);
		retImage = ReflectionUtils.invokeHidden(button, "getImageToDraw");
		assertSame(image, retImage);

		final Image focusedImage = ImageStore.getInstance().getImage("spirit.png");
		button.setFocusedImage(focusedImage);
		final Image hoverFocusedImage = ImageStore.getInstance().getImage("spirit_h_.png");
		button.setHoverFocusedImage(hoverFocusedImage);
		final Image hoverImage = ImageStore.getInstance().getImage("imagebutton_h_.png");
		button.setHoverImage(hoverImage);
		final Image pressedImage = ImageStore.getInstance().getImage("imagebutton_p_.png");
		button.setPressedImage(pressedImage);
		final Image disabledImage = ImageStore.getInstance().getImage("eclipse.gif");
		button.setDisabledImage(disabledImage);

		ReflectionUtils.invokeHidden(button, "setFocused", true);
		retImage = ReflectionUtils.invokeHidden(button, "getImageToDraw");
		assertSame(focusedImage, retImage);

		ReflectionUtils.invokeHidden(button, "setHover", true);
		retImage = ReflectionUtils.invokeHidden(button, "getImageToDraw");
		assertSame(hoverFocusedImage, retImage);

		ReflectionUtils.invokeHidden(button, "setPressed", true);
		retImage = ReflectionUtils.invokeHidden(button, "getImageToDraw");
		assertSame(pressedImage, retImage);

		button.setEnabled(false);
		retImage = ReflectionUtils.invokeHidden(button, "getImageToDraw");
		assertSame(disabledImage, retImage);

		SwtUtilities.dispose(image);
		SwtUtilities.dispose(focusedImage);
		SwtUtilities.dispose(hoverFocusedImage);
		SwtUtilities.dispose(hoverImage);
		SwtUtilities.dispose(pressedImage);
		SwtUtilities.dispose(disabledImage);

	}

	/**
	 * Tests the method {@code computeSize}.
	 */
	public void testComputeSize() {

		Point size = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
		assertEquals(new Point(0, 0), size);

		final Image image = ImageStore.getInstance().getImage("eclipse.gif");
		button.setImage(image);
		size = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
		assertEquals(new Point(16, 16), size);

		final Image focusedImage = new Image(shell.getDisplay(), 20, 21);
		button.setFocusedImage(focusedImage);
		size = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
		assertEquals(new Point(20, 21), size);

		size = button.computeSize(11, 12, false);
		assertEquals(new Point(11, 12), size);

		final Image miniImage = new Image(shell.getDisplay(), 1, 1);
		button.setImage(miniImage);
		button.setFocusedImage(miniImage);
		size = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
		assertEquals(new Point(1, 1), size);

		button.setUseIdealHeight(true);
		size = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
		assertTrue(size.y > 1);
		assertEquals(1, size.x);

		SwtUtilities.dispose(image);
		SwtUtilities.dispose(focusedImage);
		SwtUtilities.dispose(miniImage);

	}

	/**
	 * Tests the <i>private</i> method {@code computeImagePos(PaintEvent,Image)}
	 * .
	 */
	public void testComputeImagePos() {

		final Event e = new Event();
		e.widget = button;
		final PaintEvent event = new PaintEvent(e);
		event.width = 20;
		event.height = 40;
		final Image image = new Image(shell.getDisplay(), 10, 16);
		Point retPos = ReflectionUtils.invokeHidden(button, "computeImagePos", event, image);
		assertEquals(new Point(5, 12), retPos);

		final Image bigImage = new Image(shell.getDisplay(), 50, 60);
		retPos = ReflectionUtils.invokeHidden(button, "computeImagePos", event, bigImage);
		assertEquals(new Point(0, 0), retPos);

		SwtUtilities.dispose(image);
		SwtUtilities.dispose(bigImage);

	}

	/**
	 * Tests the <i>private</i> method {@code updateHoverButton()}.
	 */
	public void testUpdateHoverButton() {

		shell.setVisible(true);

		button = new ImageButton(shell, SWT.HOT);
		final Button hoverButton = ReflectionUtils.getHidden(button, "hoverButton");
		assertFalse(hoverButton.isVisible());

		ReflectionUtils.invokeHidden(button, "setPressed", true);
		ReflectionUtils.invokeHidden(button, "updateHoverButton");
		assertTrue(hoverButton.isVisible());

		ReflectionUtils.invokeHidden(button, "setPressed", false);
		ReflectionUtils.invokeHidden(button, "updateHoverButton");
		assertFalse(hoverButton.isVisible());

		ReflectionUtils.invokeHidden(button, "setHover", true);
		ReflectionUtils.invokeHidden(button, "updateHoverButton");
		assertTrue(hoverButton.isVisible());

		ReflectionUtils.invokeHidden(button, "setPressed", true);
		ReflectionUtils.invokeHidden(button, "updateHoverButton");
		assertTrue(hoverButton.isVisible());

		shell.setVisible(false);

	}

}
