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
package org.eclipse.riena.beans.common;

/**
 * Pojo with information about a SuperHero.
 * 
 * @since 2.0
 */
public final class SuperHero {

	private final String pseudonym;
	private final String name;
	private final Integer appearance;
	private boolean active;

	/**
	 * Create a new superhero, with active status.
	 * 
	 * @param pseudonym
	 *            the alias
	 * @param name
	 *            the name
	 * @param appearance
	 *            year of first appearance
	 */
	public SuperHero(final String pseudonym, final String name, final int appearance) {
		this(pseudonym, name, appearance, true);
	}

	/**
	 * Create a new superhero.
	 * 
	 * @param pseudonym
	 *            the alias
	 * @param name
	 *            the name
	 * @param appearance
	 *            year of first appearance
	 * @param active
	 *            true if still active, false otherwise.
	 */
	public SuperHero(final String pseudonym, final String name, final int appearance, final boolean active) {
		this.pseudonym = pseudonym;
		this.name = name;
		this.appearance = appearance;
		this.active = active;
	}

	/**
	 * Return the alias.
	 */
	public String getPseudonym() {
		return pseudonym;
	}

	/**
	 * Return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the year of first appearance.
	 */
	public Integer getAppearance() {
		return appearance;
	}

	/**
	 * Return true if still active, false otherwise.
	 */
	public boolean getActive() {
		return active;
	}

	/**
	 * Change the active status.
	 * 
	 * @param active
	 *            true if still active, false otherwise.
	 */
	public void setActive(final boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", pseudonym, name); //$NON-NLS-1$
	}
}