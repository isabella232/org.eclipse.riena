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
package org.eclipse.riena.core.injector.extension;

/**
 *
 */
public class LazyThing implements ILazyThing {

	// Ignoring Checkstyle warning about the field not being private:
	// appears to be ok for testing.
	static boolean instantiated;

	public LazyThing() {
		// TODO warning suppression. Ignoring FindBugs problem about
		// writing to static field. Since this is used only for testing
		// manipulation of multiple instances is not an issue.
		instantiated = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.core.extension.ILazyThing#doSomething()
	 */
	public void doSomething() {
	}

}
