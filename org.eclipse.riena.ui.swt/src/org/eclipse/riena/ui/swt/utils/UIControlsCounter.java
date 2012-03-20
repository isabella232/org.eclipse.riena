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
package org.eclipse.riena.ui.swt.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.core.singleton.SessionSingletonProvider;
import org.eclipse.riena.core.singleton.SingletonProvider;
import org.eclipse.riena.core.util.ObjectCounter;
import org.eclipse.riena.internal.ui.swt.console.UIControlsStatisticCommandProvider;

/**
 * Statistic Counter of {@link Control}s created by {@link UIControlsFactory}.
 * Used by {@link UIControlsStatisticCommandProvider} to get statistics.
 * 
 * @since 3.0
 */
public class UIControlsCounter {

	private final static String STAR = "*"; //$NON-NLS-1$
	private static final String HEADER = " Control Usage Statistics "; //$NON-NLS-1$
	private static final int NOS = 10;

	private static final SingletonProvider<UIControlsCounter> UICC = new SessionSingletonProvider<UIControlsCounter>(
			UIControlsCounter.class);

	private final ObjectCounter<Class<?>> live = new ObjectCounter<Class<?>>();
	private final ObjectCounter<Class<?>> total = new ObjectCounter<Class<?>>();

	private final DisposeListener disposeListener = new LiveDisposeListener();

	public static final String CONTROL_STATS_PROPERTY_NAME = "riena.control.stats"; //$NON-NLS-1$
	private static final boolean CONTROL_STATS_REQUESTED = Boolean.getBoolean(CONTROL_STATS_PROPERTY_NAME);

	/**
	 * Asks whether control counting statistics are requested or not.
	 * 
	 * @return {@code true} for statistics should be activated; otherwise
	 *         {@code false}
	 */
	public static boolean isRequested() {
		return CONTROL_STATS_REQUESTED;
	}

	/**
	 * @return - the Singleton instance of {@link UIControlsCounter}
	 */
	public static UIControlsCounter getInstance() {
		return UICC.getInstance();
	}

	/**
	 * Register the given control for statistic reasons
	 * 
	 * @param control
	 *            - the control to register
	 */
	public void registerConstruction(final Object control) {
		if (isRequested()) {
			total.incrementAndGetCount(control.getClass());
			live.incrementAndGetCount(control.getClass());
			if (control instanceof Widget) {
				((Widget) control).addDisposeListener(disposeListener);
			}
		}
	}

	/**
	 *
	 */
	private final class LiveDisposeListener implements DisposeListener {
		public void widgetDisposed(final DisposeEvent e) {
			live.decrementAndGetCount(e.widget.getClass());
		}
	}

	/**
	 * Get a formatted String of Control usage statistics
	 * 
	 * @return - the statistic in String format
	 */
	public String getControlUsageStatistics() {
		final StringBuilder strb = new StringBuilder();
		appendNTimes(STAR, NOS, strb);
		strb.append(HEADER);
		appendNTimes(STAR, NOS, strb);
		newLine(strb);
		strb.append(" Live" + DELIMITER); //$NON-NLS-1$
		strb.append("Total" + DELIMITER); //$NON-NLS-1$
		strb.append("Type"); //$NON-NLS-1$
		newLine(strb);
		for (final String line : getSortedLines(live, total)) {
			strb.append(line);
			newLine(strb);
		}
		appendNTimes(STAR, 2 * NOS + HEADER.length(), strb);
		return strb.toString();
	}

	///// Helping Methods

	private static final String DELIMITER = "\t"; //$NON-NLS-1$
	private static final String LINE_FORMAT = "% 5d" + DELIMITER + "% 5d" + DELIMITER + "%s"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	private static List<String> getSortedLines(final ObjectCounter<Class<?>> live, final ObjectCounter<Class<?>> total) {
		final List<String> result = new ArrayList<String>();
		for (final Class<?> clazz : live) {
			result.add(String.format(LINE_FORMAT, live.getCount(clazz), total.getCount(clazz), clazz.getName()));
		}
		Collections.sort(result, new Comparator<String>() {
			public int compare(final String o1, final String o2) {
				final Integer i1 = Integer.valueOf(o1.split(DELIMITER)[0].trim());
				final Integer i2 = Integer.valueOf(o2.split(DELIMITER)[0].trim());
				return i2.compareTo(i1);
			}
		});
		return result;
	}

	private static void newLine(final StringBuilder strb) {
		strb.append("\n"); //$NON-NLS-1$
	}

	private static void appendNTimes(final String str, final int n, final StringBuilder strb) {
		for (int i = 0; i < n; i++) {
			strb.append(str);
		}
	}

}
