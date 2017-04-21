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
package org.eclipse.riena.beans.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the name of the day in several languages. Read-only.
 * 
 * @since 2.0
 */
public final class DayPojo {

	private final String english;
	private final String german;
	private final String french;
	private final String spanish;
	private final String italian;

	public static List<DayPojo> createWeek() {
		final List<DayPojo> result = new ArrayList<DayPojo>(7);
		result.add(new DayPojo("Monday", "Montag", "lundi", "lunes", "lunedě")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		result.add(new DayPojo("Tuesday", "Dienstag", "mardi", "martes", "martedě")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		result.add(new DayPojo("Wednesday", "Mittwoch", "mercredi", "miércoles", "mercoledě")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		result.add(new DayPojo("Thursday", "Donnerstag", "jeudi", "jueves", "giovedě")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		result.add(new DayPojo("Friday", "Freitag", "vendredi", "viernes", "venerdě")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		result.add(new DayPojo("Saturday", "Samstag", "samedi", "sábado", "sabato")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		result.add(new DayPojo("Sunday", "Sonntag", "dimanche", "domingo", "domenica")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		return result;
	}

	private DayPojo(final String english, final String german, final String french, final String spanish,
			final String italian) {
		this.english = english;
		this.german = german;
		this.french = french;
		this.spanish = spanish;
		this.italian = italian;
	}

	public String getEnglish() {
		return english;
	}

	public String getGerman() {
		return german;
	}

	public String getFrench() {
		return french;
	}

	public String getSpanish() {
		return spanish;
	}

	public String getItalian() {
		return italian;
	}

	@Override
	public String toString() {
		return String.format("[en=%s, de=%s, fr=%s, es=%s, it=%s]", english, german, french, spanish, italian); //$NON-NLS-1$
	}
}