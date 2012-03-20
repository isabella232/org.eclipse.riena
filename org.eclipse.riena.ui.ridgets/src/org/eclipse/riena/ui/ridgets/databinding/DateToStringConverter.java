/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets.databinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.databinding.conversion.Converter;

/**
 * Converts a Date to a String with a given pattern.
 */
public class DateToStringConverter extends Converter {

	private final DateFormat format;

	/**
	 * Creates a DateToStringConverter.
	 * 
	 * @param pattern
	 *            The pattern to match e.g. MM/dd/yyyy.
	 */
	public DateToStringConverter(final String pattern) {
		super(Date.class, String.class);
		format = new SimpleDateFormat(pattern);
	}

	/**
	 * @see org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.Object)
	 */
	public Object convert(final Object fromObject) {
		if (fromObject == null) {
			return null;
		}
		synchronized (format) {
			return format.format((Date) fromObject);
		}
	}

}
