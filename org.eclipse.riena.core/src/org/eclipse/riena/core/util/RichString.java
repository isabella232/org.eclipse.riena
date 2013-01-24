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
package org.eclipse.riena.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.Assert;

/**
 * The {@code RichString} <i>extends</i> the {@code String} class with
 * additional functionality.
 */
public class RichString implements CharSequence {

	private final String poorString;

	// Delimiter of pairs in a map.
	private static final String PAIR_DELIM = ";"; //$NON-NLS-1$
	// Delimiter of items in a list
	private static final char LIST_DELIM = ',';
	// Escape character for lists
	private static final char ESCAPE_CHAR = '\\';
	// Parsing states
	private static final int STATE_CHAR = 0;
	private static final int STATE_BACK_SLASH = 1;
	// Guess!
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/**
	 * Create a {@code RichString}.
	 * 
	 * @param poorString
	 *            the original string
	 */
	public RichString(final String poorString) {
		this.poorString = poorString == null ? EMPTY_STRING : poorString;
	}

	/**
	 * Is this {@code RichString} empty?
	 * 
	 * @return empty?
	 */
	public boolean isEmpty() {
		return poorString.length() == 0;
	}

	/**
	 * Transform this rich string representation of a list into a
	 * {@code List<String>}.<br>
	 * This method supports the escaped meta characters: '\,' which is a literal
	 * ',' and '\\' which is a '\'.
	 * 
	 * <pre>
	 * The format of the string is: 
	 *  string = [ value ] | value { , value }
	 * </pre>
	 * 
	 * @return a {@code List<String>} maybe empty but never {@code null}
	 * @throws IllegalArgumentException
	 *             in case of errors
	 */
	public List<String> toList() {
		if (isEmpty()) {
			return new ArrayList<String>(0);
		}
		final List<String> result = new ArrayList<String>();
		final StringBuilder value = new StringBuilder();
		char ch = 0;
		int state = STATE_CHAR;
		for (int i = 0; i < poorString.length(); i++) {
			ch = poorString.charAt(i);
			switch (state) {
			case STATE_CHAR:
				if (ch == ESCAPE_CHAR) {
					state = STATE_BACK_SLASH;
				} else if (ch == LIST_DELIM) {
					result.add(value.toString());
					value.setLength(0);
				} else {
					value.append(ch);
				}
				break;
			case STATE_BACK_SLASH:
				if (ch == ESCAPE_CHAR || ch == LIST_DELIM) {
					value.append(ch);
				} else {
					Assert.isLegal(false, "Unknown escaped character: " + ch + "."); //$NON-NLS-1$ //$NON-NLS-2$
				}
				state = STATE_CHAR;
				break;
			default:
				break;
			}
		}
		// final comma
		if (value.length() != 0 || ch == LIST_DELIM) {
			result.add(value.toString());
		}
		return result;
	}

	/**
	 * Transform this rich string representation of a list into a {@code Set}.<br>
	 * This method supports the escaped meta characters: '\,' which is a literal
	 * ',' and '\\' which is a '\'.
	 * 
	 * <pre>
	 * The format of the string is: 
	 *  string = [ value ] | value { , value }
	 * </pre>
	 * 
	 * @return a {@code Set<String>} maybe empty but never {@code null}
	 * @throws IllegalArgumentException
	 *             in case of errors
	 */
	public Set<String> toSet() {
		if (isEmpty()) {
			return new HashSet<String>(0);
		}
		return new HashSet<String>(toList());
	}

	/**
	 * Transform this rich string representation of a list into a
	 * {@code String[]}.<br>
	 * This method supports the escaped meta characters: '\,' which is a literal
	 * ',' and '\\' which is a '\'.
	 * 
	 * <pre>
	 * The format of the string is: 
	 *  string = [ value ] | value { , value }
	 * </pre>
	 * 
	 * @return a {@code String[]} maybe empty but never {@code null}
	 * @throws IllegalArgumentException
	 *             in case of errors
	 */
	public String[] toArray() {
		final List<String> list = toList();
		return list.toArray(new String[list.size()]);
	}

	/**
	 * Transform this rich string representation of a map into a map.
	 * 
	 * <pre>
	 * The format of the string is: 
	 * string := [ pair ] | pair { ; pair }
	 * pair := key = value
	 * </pre>
	 * 
	 * @return a map maybe empty but never {@code null}
	 * @throws IllegalArgumentException
	 *             for any errors
	 * @see IExecutableExtension.setInitializationData()
	 */
	public Map<String, String> toMap() {
		if (isEmpty()) {
			return new HashMap<String, String>(0);
		}
		final Map<String, String> result = new HashMap<String, String>();
		final StringTokenizer parts = new StringTokenizer(poorString, PAIR_DELIM);
		while (parts.hasMoreTokens()) {
			final String part = parts.nextToken();
			final int equal = part.indexOf('=');
			Assert.isLegal(equal > 0, "Error within definition. Expecting a string of the form: " //$NON-NLS-1$
					+ " [ <key> \"=\" <value> ] { [ \"" + PAIR_DELIM + "\" <key> \"=\" <value> ] }"); //$NON-NLS-1$ //$NON-NLS-2$
			result.put(part.substring(0, equal).trim(), part.substring(equal + 1).trim());
		}
		return result;
	}

	/**
	 * @see java.lang.CharSequence#length()
	 */
	public int length() {
		return poorString.length();
	}

	/**
	 * @see java.lang.CharSequence#charAt(int)
	 */
	public char charAt(final int index) {
		return poorString.charAt(index);
	}

	/**
	 * @see java.lang.CharSequence#subSequence(int, int)
	 */
	public CharSequence subSequence(final int start, final int end) {
		return poorString.subSequence(start, end);
	}
}
