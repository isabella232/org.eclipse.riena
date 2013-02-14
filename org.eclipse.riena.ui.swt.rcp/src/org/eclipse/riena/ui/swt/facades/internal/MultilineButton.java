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
package org.eclipse.riena.ui.swt.facades.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.win32.BITMAP;
import org.eclipse.swt.internal.win32.LOGFONT;
import org.eclipse.swt.internal.win32.LOGFONTA;
import org.eclipse.swt.internal.win32.LOGFONTW;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.RECT;
import org.eclipse.swt.internal.win32.SIZE;
import org.eclipse.swt.internal.win32.TCHAR;
import org.eclipse.swt.internal.win32.TEXTMETRIC;
import org.eclipse.swt.internal.win32.TEXTMETRICA;
import org.eclipse.swt.internal.win32.TEXTMETRICW;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * This class is not API. It is a subclass of {@link Button} which works around Bug 400248.
 * <p>
 * This class can be removed after the SWT bug is fixed.
 * 
 * @since 5.0
 * 
 */
public class MultilineButton extends Button {

	static final int MARGIN = 4;
	static final int CHECK_WIDTH, CHECK_HEIGHT;

	/*
	 * copy from org.eclipse.swt.widgets.Button
	 */
	static {
		final int /* long */hBitmap = OS.LoadBitmap(0, OS.OBM_CHECKBOXES);
		if (hBitmap == 0) {
			CHECK_WIDTH = OS.GetSystemMetrics(OS.IsWinCE ? OS.SM_CXSMICON : OS.SM_CXVSCROLL);
			CHECK_HEIGHT = OS.GetSystemMetrics(OS.IsWinCE ? OS.SM_CYSMICON : OS.SM_CYVSCROLL);
		} else {
			final BITMAP bitmap = new BITMAP();
			OS.GetObject(hBitmap, BITMAP.sizeof, bitmap);
			OS.DeleteObject(hBitmap);
			CHECK_WIDTH = bitmap.bmWidth / 4;
			CHECK_HEIGHT = bitmap.bmHeight / 3;
		}
		//					WNDCLASS lpWndClass = new WNDCLASS ();
		//					OS.GetClassInfo (0, ButtonClass, lpWndClass);
		//					ButtonProc = lpWndClass.lpfnWndProc;
	}

	/**
	 * This class is not API. It is a subclass of {@link Button} which works around Bug 400248.
	 * <p>
	 * This class can be removed after the SWT bug is fixed.
	 */
	public MultilineButton(final Composite parent, final int style) {
		super(parent, style);
	}

	@Override
	protected void checkSubclass() {
		// This subclass is needed until Bug 400248 is fixed
	}

	@Override
	public Point computeSize(final int wHint, final int hHint, final boolean changed) {
		final int style = getStyle();
		final Image image = getImage();
		final String text = getText();

		checkWidget();
		int width = 0, height = 0;
		final int border = getBorderWidth();
		if ((style & SWT.ARROW) != 0) {
			if ((style & (SWT.UP | SWT.DOWN)) != 0) {
				width += OS.GetSystemMetrics(OS.SM_CXVSCROLL);
				height += OS.GetSystemMetrics(OS.SM_CYVSCROLL);
			} else {
				width += OS.GetSystemMetrics(OS.SM_CXHSCROLL);
				height += OS.GetSystemMetrics(OS.SM_CYHSCROLL);
			}
		} else {
			if ((style & SWT.COMMAND) != 0) {
				final SIZE size = new SIZE();
				if (wHint != SWT.DEFAULT) {
					size.cx = wHint;
					OS.SendMessage(handle, OS.BCM_GETIDEALSIZE, 0, size);
					width = size.cx;
					height = size.cy;
				} else {
					OS.SendMessage(handle, OS.BCM_GETIDEALSIZE, 0, size);
					width = size.cy;
					height = size.cy;
					size.cy = 0;
					while (size.cy != height) {
						size.cx = width++;
						size.cy = 0;
						OS.SendMessage(handle, OS.BCM_GETIDEALSIZE, 0, size);
					}
				}
			} else {
				int extra = 0;
				boolean hasImage = image != null, hasText = true;
				if (OS.COMCTL32_MAJOR < 6) {
					if ((style & SWT.PUSH) == 0) {
						final int bits = OS.GetWindowLong(handle, OS.GWL_STYLE);
						hasImage = (bits & (OS.BS_BITMAP | OS.BS_ICON)) != 0;
						if (hasImage) {
							hasText = false;
						}
					}
				}
				if (hasImage) {
					if (image != null) {
						final Rectangle rect = image.getBounds();
						width = rect.width;
						if (hasText && text.length() != 0) {
							width += MARGIN * 2;
						}
						height = rect.height;
						extra = MARGIN * 2;
					}
				}
				if (hasText) {
					int /* long */oldFont = 0;
					final int /* long */hDC = OS.GetDC(handle);
					final int /* long */newFont = OS.SendMessage(handle, OS.WM_GETFONT, 0, 0);
					if (newFont != 0) {
						oldFont = OS.SelectObject(hDC, newFont);
					}
					final TEXTMETRIC lptm = OS.IsUnicode ? (TEXTMETRIC) new TEXTMETRICW() : new TEXTMETRICA();
					OS.GetTextMetrics(hDC, lptm);
					final int length = text.length();
					if (length == 0) {
						height = Math.max(height, lptm.tmHeight);
					} else {
						extra = Math.max(MARGIN * 2, lptm.tmAveCharWidth);
						final TCHAR buffer = new TCHAR(getCodePage(), text, true);
						final RECT rect = new RECT();
						//									int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE;
						int flags = OS.DT_CALCRECT;
						if ((style & SWT.WRAP) == 0) {
							flags |= OS.DT_SINGLELINE;
						}
						if ((style & SWT.WRAP) != 0 && wHint != SWT.DEFAULT) {
							flags = OS.DT_CALCRECT | OS.DT_WORDBREAK;
							rect.right = wHint - width - 2 * border;
							if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
								rect.right -= CHECK_WIDTH + 3;
							} else {
								rect.right -= 6;
							}
							if (OS.COMCTL32_MAJOR < 6 || !OS.IsAppThemed()) {
								rect.right -= 2;
								if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
									rect.right -= 2;
								}
							}
						}
						OS.DrawText(hDC, buffer, -1, rect, flags);
						width += rect.right - rect.left;
						height = Math.max(height, rect.bottom - rect.top);
					}
					if (newFont != 0) {
						OS.SelectObject(hDC, oldFont);
					}
					OS.ReleaseDC(handle, hDC);
				}
				if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
					width += CHECK_WIDTH + extra;
					height = Math.max(height, CHECK_HEIGHT + 3);
				}
				if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
					width += 12;
					height += 10;
				}
			}
		}
		if (wHint != SWT.DEFAULT) {
			width = wHint;
		}
		if (hHint != SWT.DEFAULT) {
			height = hHint;
		}
		width += border * 2;
		height += border * 2;
		return new Point(width, height);
	}

	/*
	 * copy of org.eclipse.swt.widgets.Control.getCodePage()
	 */
	int getCodePage() {
		if (OS.IsUnicode) {
			return OS.CP_ACP;
		}
		final int /* long */hFont = OS.SendMessage(handle, OS.WM_GETFONT, 0, 0);
		final LOGFONT logFont = OS.IsUnicode ? (LOGFONT) new LOGFONTW() : new LOGFONTA();
		OS.GetObject(hFont, LOGFONT.sizeof, logFont);
		final int cs = logFont.lfCharSet & 0xFF;
		final int[] lpCs = new int[8];
		if (OS.TranslateCharsetInfo(cs, lpCs, OS.TCI_SRCCHARSET)) {
			return lpCs[1];
		}
		return OS.GetACP();
	}
}
