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
package org.eclipse.riena.navigation;

/**
 * @since 1.2
 */
public class StartupNodeInfo implements Comparable<StartupNodeInfo> {

	private final Level level;
	private final int sequence;
	private final String id;

	public enum Level {
		SUBAPPLICATION, MODULEGROUP, MODULE, SUBMODULE, CUSTOM
	}

	public StartupNodeInfo(final Level level, final int sequence, final String id) {
		this.level = level;
		this.sequence = sequence;
		this.id = id;
	}

	public int compareTo(final StartupNodeInfo o) {
		return sequence == o.sequence ? level.compareTo(o.level) : sequence - o.sequence;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + sequence;
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final StartupNodeInfo other = (StartupNodeInfo) obj;
		if (level == null) {
			if (other.level != null) {
				return false;
			}
		} else if (!level.equals(other.level)) {
			return false;
		}
		if (sequence != other.sequence) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new StringBuilder("startup module ").append(id).append("[level=").append(level).append(" sequence=") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				.append(sequence).append("]").toString(); //$NON-NLS-1$
	}

}
