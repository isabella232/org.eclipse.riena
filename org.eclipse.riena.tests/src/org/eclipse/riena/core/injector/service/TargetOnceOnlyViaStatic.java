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
package org.eclipse.riena.core.injector.service;

/**
 * The target for injecting
 */
public class TargetOnceOnlyViaStatic {

	private static DepOne dep;
	private static int bindCounter;
	private static int unbindCounter;

	public static void bind(final DepOne dep) {
		TargetOnceOnlyViaStatic.dep = dep;
		bindCounter++;
	}

	public static void unbind(final DepOne dep) {
		if (TargetOnceOnlyViaStatic.dep == dep) {
			TargetOnceOnlyViaStatic.dep = null;
		}
		unbindCounter++;
	}

	public static void resetCounters() {
		bindCounter = 0;
		unbindCounter = 0;
	}

	public static int getBindCounter() {
		return bindCounter;
	}

	public static int getUnbindCounter() {
		return unbindCounter;
	}
}
