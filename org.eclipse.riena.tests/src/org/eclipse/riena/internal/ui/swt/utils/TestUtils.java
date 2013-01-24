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
package org.eclipse.riena.internal.ui.swt.utils;

import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import junit.framework.Assert;

import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import org.eclipse.riena.core.marker.IMarkable;
import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.internal.ui.swt.test.UITestHelper;
import org.eclipse.riena.ui.core.marker.IMessageMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.swt.facades.SWTFacade;

/**
 * Utility class for tests.
 */
public final class TestUtils {

	private TestUtils() {
		// utility class
	}

	/**
	 * For a given Tree or Table, tests that the layouting results in
	 * correctly-calculated column widths.
	 * 
	 * @param control
	 *            the Tree or Table widget to check
	 * @param numberOfCols
	 *            the number of columns that the widget displays
	 */
	public static void assertColumnWidths(final Control control, final int numberOfCols) {
		final int expected = control.getSize().x / numberOfCols;
		Assert.assertTrue(String.valueOf(expected), expected > 0);

		for (int column = 0; column < numberOfCols; column++) {
			final int actual = getColumnWidth(control, column);
			// take into account rounding errors, e.g. total width 85 => col widths (29, 28, 28)
			final String message = String.format(
					"col %d, expected %d <= x <= %d but was %d", column, expected, (expected + 1), actual); //$NON-NLS-1$
			Assert.assertTrue(message, (expected <= actual && actual <= expected + 1));
		}
	}

	/**
	 * Asserts that the given ridget has {@code expectedCount} mandatory markers
	 * with the given {@code disabledState}.
	 * 
	 * @param ridget
	 *            never null
	 * @param expectedCount
	 *            the expected count of markers
	 * @param disabledState
	 *            the expected disabled state value
	 */
	public static void assertMandatoryMarker(final IMarkableRidget ridget, final int expectedCount,
			final boolean disabledState) {
		final Collection<MandatoryMarker> markers = ridget.getMarkersOfType(MandatoryMarker.class);
		Assert.assertEquals(expectedCount, markers.size());
		final Iterator<MandatoryMarker> iter = markers.iterator();
		while (iter.hasNext()) {
			final boolean isDisabled = iter.next().isDisabled();
			Assert.assertEquals(disabledState, isDisabled);
		}
	}

	/**
	 * Asserts that given ridget has a certain number of markers of the given
	 * type
	 * 
	 * @param ridget
	 *            a IMarkable ridget; never null
	 * @param markerType
	 *            a marker type implementing IMessageMarker
	 * @param count
	 *            the expected count; 0 or greater
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void assertMessageCount(final IMarkable ridget, final Class markerType, final int count) {
		final Collection<IMessageMarker> collection = ridget.getMarkersOfType(markerType);
		if (count != collection.size()) {
			System.out.println(String.format("assertion failed on message count -- expected %d, got %d:", count, //$NON-NLS-1$
					collection.size()));
			for (final IMessageMarker messageMarker : collection) {
				System.out.println("\t" + messageMarker.getMessage()); //$NON-NLS-1$
			}
		}
		Assert.assertEquals(count, collection.size());
	}

	/**
	 * Asserts that an IMessageMarker with the given message is contained in the
	 * ridget.
	 * 
	 * @param ridget
	 *            a IMarkable ridget; never null
	 * @param markerType
	 *            a marker type implementing IMessageMarker
	 * @param message
	 *            the message to find
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void assertMessage(final IMarkable ridget, final Class markerType, final String message) {
		final Collection<IMessageMarker> collection = ridget.getMarkersOfType(markerType);
		boolean wasFound = false;
		final Iterator<IMessageMarker> iter = collection.iterator();
		while (!wasFound && iter.hasNext()) {
			final IMessageMarker marker = iter.next();
			wasFound = message.equals(marker.getMessage());
		}
		Assert.assertEquals(String.format("Message '%s'", message), true, wasFound); //$NON-NLS-1$
	}

	/**
	 * Asserts that in the given {@code control} the text and cursor position
	 * match the expected {@code before} value. It then applies the given key
	 * sequence and asserts that the resulting text and cursor position matches
	 * the expected {@code after} value.
	 * <p>
	 * One can use one '^' character to denote the expected cursor position, or
	 * two '^' characters to denote the expected selection.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * assertText(&quot;  &circ;.  .2008&quot;, &quot;1208&quot;, &quot;12.08&circ;.2008&quot;);
	 * </pre>
	 * 
	 * @param control
	 *            a non-null Text control
	 * @param before
	 *            the expected before value
	 * @param keySeq
	 *            a String with the characters to type
	 * @param after
	 *            the expected after value
	 */
	public static void assertText(final Text control, final String before, final String keySeq, final String after) {
		forceText(control, before);

		checkText(control, before);
		checkSelection(control, before);
		checkCaret(control, before);

		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), keySeq);

		checkText(control, after);
		checkCaret(control, after);
	}

	/**
	 * Asserts that in the given {@code control} the text and cursor position
	 * match the expected {@code before} value. It then applies the given
	 * keyCode and asserts that the resulting text and cursor position matches
	 * the expected {@code after} value.
	 * <p>
	 * One can use one '^' character to denote the expected cursor position, or
	 * two '^' characters to denote the expected selection.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * assertText(&quot;  &circ;.  .2008&quot;, &quot;1208&quot;, &quot;12.08&circ;.2008&quot;);
	 * </pre>
	 * 
	 * @param control
	 *            a non-null Text control
	 * @param before
	 *            the expected before value
	 * @param keySeq
	 *            the keyCode to type
	 * @param after
	 *            the expected after value
	 */
	public static void assertText(final Text control, final String before, final int keyCode, final String after) {
		forceText(control, before);

		checkText(control, before);
		checkSelection(control, before);
		checkCaret(control, before);

		control.setFocus();
		UITestHelper.sendKeyAction(control.getDisplay(), keyCode);

		checkText(control, after);
		checkCaret(control, after);
	}

	/**
	 * Returns true if the Arab locale ("ar_AE") is available.
	 */
	public static boolean isArabLocaleAvailable() {
		final Locale arabLocale = new Locale("ar", "AE"); //$NON-NLS-1$  //$NON-NLS-2$
		for (final Locale availableLocale : Locale.getAvailableLocales()) {
			if (availableLocale.equals(arabLocale)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return a localized version of a String representation of a number. The
	 * returned string will use the grouping separator and decimal separator for
	 * the current locale.
	 * <p>
	 * Examples:
	 * <ul>
	 * <li>DE - "1.234,56" -&gt; "1.234,56"</li>
	 * <li>US - "1.234,56" -&gt; "1,234.56"</li>
	 * </ul>
	 * 
	 * @param number
	 *            a String representation of a number, where '.' is used as the
	 *            grouping separator and ',' is used as the decimal separator.
	 * @return a localized String representation of a number
	 */
	public static String getLocalizedNumber(final String number) {
		final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		final char decimalSep = dfs.getDecimalSeparator();
		final char groupingSep = dfs.getGroupingSeparator();
		final String result = number.replace('.', '_').replace(',', decimalSep).replace('_', groupingSep);
		return result;
	}

	/**
	 * Print all markers in this markable.
	 */
	public static void printMarkers(final IMarkable markable) {
		final Collection<IMarker> markers = (Collection<IMarker>) markable.getMarkers();
		System.out.println(String.format("Have %d markers::", markers.size())); //$NON-NLS-1$
		for (final IMarker m : markers) {
			System.out.println(m.toString());
		}
	}

	// helping methods
	//////////////////

	private static void checkText(final Text control, final String input) {
		final String expected = removePositionMarkers(input);
		Assert.assertEquals(expected, control.getText());
	}

	private static void checkSelection(final Text control, final String input) {
		final int start = input.indexOf('^');
		final int end = input.lastIndexOf('^');
		String expected = ""; //$NON-NLS-1$
		if (start < end) {
			expected = input.substring(start + 1, end);
		}
		// System.out.println("exp sel: " + expected);
		Assert.assertEquals(expected, control.getSelectionText());
	}

	private static void checkCaret(final Text control, final String input) {
		final int start = input.indexOf('^');
		if (start != -1) {
			final int end = input.lastIndexOf('^');
			final int expected = start < end ? end - 1 : end;
			// System.out.println("exp car: " + expected);
			Assert.assertEquals(expected, control.getCaretPosition());
		}
	}

	private static int getColumnWidth(final Control control, final int colIndex) {
		if (control instanceof Tree) {
			return ((Tree) control).getColumn(colIndex).getWidth();
		} else if (control instanceof Table) {
			return ((Table) control).getColumn(colIndex).getWidth();
		} else if (control instanceof Grid) {
			return ((Grid) control).getColumn(colIndex).getWidth();
		}
		throw new IllegalArgumentException("unsupported control: " + control); //$NON-NLS-1$
	}

	private static String removePositionMarkers(final String input) {
		final StringBuilder result = new StringBuilder(input.length());
		for (int i = 0; i < input.length(); i++) {
			final char ch = input.charAt(i);
			if (ch != '^') {
				result.append(ch);
			}
		}
		return result.toString();
	}

	private static void forceText(final Text control, final String text) {
		final int start = text.indexOf('^');
		final int end = text.lastIndexOf('^');

		final SWTFacade facade = SWTFacade.getDefault();
		final Object[] listeners = facade.removeListeners(control, SWT.Verify);
		control.setText(removePositionMarkers(text));
		facade.addListeners(control, SWT.Verify, listeners);
		control.setFocus();
		if (start == end) {
			control.setSelection(start, start);
		} else {
			control.setSelection(start, end - 1);
		}
	}

}
