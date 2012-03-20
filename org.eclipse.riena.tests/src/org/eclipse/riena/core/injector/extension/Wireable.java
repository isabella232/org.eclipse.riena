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
package org.eclipse.riena.core.injector.extension;

import org.eclipse.riena.core.wire.WireWith;

/**
 *
 */
@WireWith(WirableWiring.class)
public class Wireable implements IWireable {

	// ignore Checkstyle warning: field is not private for testing.
	static boolean wired;

	/**
	 * @see org.eclipse.riena.core.injector.extension.IWireable#setWired(boolean)
	 */
	public void setWired(final boolean wired) {
		// TODO warning suppression. Ignoring FindBugs problem about
		// writing to static field. Since this is used only for testing
		// manipulation of multiple instances is not an issue.
		Wireable.wired = wired;
	}

	/**
	 * @see org.eclipse.riena.core.injector.extension.IWireable#isWired()
	 */
	public boolean isWired() {
		return wired;
	}
}
