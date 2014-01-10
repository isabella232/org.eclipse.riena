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
package org.eclipse.riena.ui.swt.uiprocess;

/**
 * a data object for process state
 */
public class ProgressInfoDataObject implements Comparable<ProgressInfoDataObject> {
	private static final String EMPTY = "";//$NON-NLS-1$
	private String processName = EMPTY;
	private final int value;
	private final int maxValue;
	private final int key;
	private final ProcessState processState;

	/**
	 * @param maxValue
	 * @param processName
	 * @param value
	 */
	public ProgressInfoDataObject(final int key, final int maxValue, final int value, final String processName,
			final ProcessState pState) {
		this.maxValue = maxValue;
		this.processName = processName;
		this.value = value;
		this.key = key;
		this.processState = pState;
	}

	/**
	 * @return the processName
	 */
	public String getProcessName() {
		return processName;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @return the maxValue
	 */
	public int getMaxValue() {
		return maxValue;
	}

	/**
	 * @return the processState
	 */
	public ProcessState getProcessState() {
		return processState;
	}

	public Integer getKey() {
		return Integer.valueOf(key);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final ProgressInfoDataObject other = (ProgressInfoDataObject) obj;
		return key == other.key;
	}

	@Override
	public int hashCode() {
		return key;
	}

	public int compareTo(final ProgressInfoDataObject other) {
		if (equals(other) && getProcessState().equals(other.getProcessState()) && getValue() == other.getValue()) {
			return 0;
		}
		return -1;

	}

}
