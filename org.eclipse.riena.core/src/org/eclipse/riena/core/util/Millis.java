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
package org.eclipse.riena.core.util;

import java.util.Scanner;

import org.eclipse.core.runtime.Assert;

/**
 * {@code Millis} is a little helper class that provides methods to convert from
 * seconds, minutes, hours and days to milli seconds.
 */
public final class Millis {

	private Millis() {
		// utility class
	}

	/**
	 * Return the given seconds in milli seconds.
	 * 
	 * @param seconds
	 * @return
	 */
	public static long seconds(final int seconds) {
		return 1000L * seconds;
	}

	/**
	 * Return the given minutes in milli seconds.
	 * 
	 * @param minutes
	 * @return
	 */
	public static long minutes(final int minutes) {
		return seconds(60 * minutes);
	}

	/**
	 * Return the given hours in milli seconds.
	 * 
	 * @param hours
	 * @return
	 */
	public static long hours(final int hours) {
		return minutes(60 * hours);
	}

	/**
	 * Return the given days in milli seconds.
	 * 
	 * @param days
	 * @return
	 */
	public static long days(final int days) {
		return hours(24 * days);
	}

	/**
	 * Return the given weeks in milli seconds.
	 * 
	 * @param weeks
	 * @return
	 */
	public static long weeks(final int weeks) {
		return days(weeks * 7);
	}

	/**
	 * Return the given months in milli seconds - assuming a month has 28 days.
	 * 
	 * @param months
	 * @return
	 */
	public static long months(final int months) {
		return days(months * 28);
	}

	/**
	 * Return the given years in milli seconds - assuming a year has 365 days.
	 * 
	 * @param months
	 * @return
	 */
	public static long years(final int years) {
		return days(years * 365);
	}

	/**
	 * Parse a simple period string and return the value in milli seconds. E.g.:
	 * <ul>
	 * <li>"1 d 12 m" - 1 day + 12 minutes</li>
	 * <li>"10 M 1 d 12 m 237 ms" - 10 months + 1 day + 12 minutes + 237 milli
	 * seconds</li>
	 * </ul>
	 * 
	 * @param period
	 * @return period in milli seconds
	 * @throws IllegalArgumentError
	 *             on parsing errors
	 */
	public static long valueOf(final String period) {
		Assert.isLegal(period != null, "period must not be null."); //$NON-NLS-1$
		final Scanner scanner = new Scanner(period);
		long millis = 0;
		long value = 0;
		while (scanner.hasNext()) {
			if (scanner.hasNextLong()) {
				value = scanner.nextLong();
			} else {
				millis += value * getFactor(scanner.next());
			}
		}
		return millis;
	}

	/**
	 * @param tokenizer
	 * @return
	 */
	private static long getFactor(final String unit) {
		if ("d".equals(unit)) { //$NON-NLS-1$
			return Millis.days(1);
		} else if ("h".equals(unit)) { //$NON-NLS-1$
			return Millis.hours(1);
		} else if ("m".equals(unit)) { //$NON-NLS-1$
			return Millis.minutes(1);
		} else if ("s".equals(unit)) { //$NON-NLS-1$
			return Millis.seconds(1);
		} else if ("ms".equals(unit)) { //$NON-NLS-1$
			return 1;
		} else if ("w".equals(unit)) { //$NON-NLS-1$
			return Millis.weeks(1);
		} else if ("M".equals(unit)) { //$NON-NLS-1$
			return Millis.months(1);
		} else if ("y".equals(unit)) { //$NON-NLS-1$
			return Millis.years(1);
		}
		throw new IllegalArgumentException("Wrong time unit. Expecting either: y, M, w, d, h, m, s or ms"); //$NON-NLS-1$
	}
}
