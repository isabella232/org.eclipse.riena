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
 * Helper class that creates a collection of super heroes.
 * 
 * @since 2.0
 */
public final class SuperHeroFactory {

	/**
	 * Create a collection of super heroes.
	 * 
	 * @return a List instance; never null.
	 */
	public static List<SuperHero> createInput() {
		final List<SuperHero> nodes = new ArrayList<SuperHero>(7);
		nodes.add(new SuperHero("Superman", "Clark Kent", 1938)); //$NON-NLS-1$ //$NON-NLS-2$
		nodes.add(new SuperHero("Batman", "Bruce Wayne", 1939, false)); //$NON-NLS-1$ //$NON-NLS-2$
		nodes.add(new SuperHero("Zorro", "Don Diego de la Vega", 1919, false)); //$NON-NLS-1$ //$NON-NLS-2$
		nodes.add(new SuperHero("Flash Gordon", "Gordon Ferrao", 1934)); //$NON-NLS-1$ //$NON-NLS-2$
		nodes.add(new SuperHero("Hulk", "Bruce Banner", 1962)); //$NON-NLS-1$ //$NON-NLS-2$
		nodes.add(new SuperHero("Spider-Man", "Peter Parker", 1962)); //$NON-NLS-1$ //$NON-NLS-2$
		nodes.add(new SuperHero("Silver Surfer", "Norrin Radd", 1966)); //$NON-NLS-1$ //$NON-NLS-2$
		return nodes;
	}

	private SuperHeroFactory() {
		// factory, do not instantiate
	}
}
