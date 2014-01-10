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
package org.eclipse.riena.monitor.client;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.Assert;

/**
 * The {@code Range} class implements a simple solution to specify ranges of
 * integers. A range may consist of several intervals and single values or a
 * single asterisk '*' that matches all.
 * <p>
 * E.g.
 * <ul>
 * <li>1..3 -> 1,2 and 3 are within the range</li>
 * <li>1..3, 5..8 -> 1,2,3,5,6,7 and 8 are within the range</li>
 * <li>1..3, 9 -> 1,2,3 and 9 are within the range</li>
 * <li>* -> matches all</li>
 * <li>etc.</li>
 * </ul>
 */
public class Range {

	private List<Match> list;

	/**
	 * @since 3.0
	 */
	public static final String ALL = "*"; //$NON-NLS-1$

	private static final String DELIM = ","; //$NON-NLS-1$
	private static final String TILL = ".."; //$NON-NLS-1$

	/**
	 * Parse the give range.
	 * 
	 * @param range
	 * @throws IllegalArgumentException
	 */
	public Range(final String range) {
		if (range.equals(ALL)) {
			list = null;
		} else {
			list = new ArrayList<Match>();
			final StringTokenizer tokenizer = new StringTokenizer(range, DELIM);
			String token = null;
			try {
				while (tokenizer.hasMoreTokens()) {
					token = tokenizer.nextToken().trim();
					if (token.contains(TILL)) {
						list.add(new Interval(token));
					} else {
						list.add(new Value(token));
					}
				}
			} catch (final Throwable t) {
				throw new IllegalArgumentException("Error parsing range '" + range + "' with token '" + token + "'.", t); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}
	}

	public boolean matches(final int value) {
		if (list == null) {
			return true;
		}
		for (final Match match : list) {
			if (match.matches(value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Parsing error exception
	 */
	public static class ParseException extends Exception {

		private static final long serialVersionUID = -7362170629769240938L;

		public ParseException(final String string, final Throwable t) {
			super(string, t);
		}

	}

	private interface Match {
		boolean matches(int value);
	}

	private static class Value implements Match {
		private final int value;

		public Value(final String token) {
			value = Integer.valueOf(token);
		}

		public boolean matches(final int value) {
			return this.value == value;
		}
	}

	private static class Interval implements Match {
		protected final int lower;
		protected final int upper;

		public Interval(final String token) {
			final int dotdot = token.indexOf(TILL);
			Assert.isLegal(dotdot > 0);
			lower = Integer.valueOf(token.substring(0, dotdot).trim());
			upper = Integer.valueOf(token.substring(dotdot + TILL.length()).trim());
		}

		public boolean matches(final int value) {
			return lower <= value && value <= upper;
		}
	}
}
