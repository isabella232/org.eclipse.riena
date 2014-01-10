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
package org.eclipse.riena.internal.sample.app.server;

import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.riena.sample.app.common.calendar.ITestGregorianCalendar;

/**
 *
 */
public class TestGregorianCalendar implements ITestGregorianCalendar {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.sample.app.common.calendar.ITestGregorianCalendar#setTimes
	 * (java.util.GregorianCalendar, java.util.GregorianCalendar)
	 */
	public long diffTimes1(final GregorianCalendar from, final GregorianCalendar till) {
		System.out.println(from);
		System.out.println(till);
		return till.getTimeInMillis() - from.getTimeInMillis();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.sample.app.common.calendar.ITestGregorianCalendar#diffTimes
	 * (java.util.Date, java.util.Date)
	 */
	public long diffTimes2(final Date from, final Date till) {
		System.out.println(from);
		System.out.println(till);
		return till.getTime() - from.getTime();
	}

}
