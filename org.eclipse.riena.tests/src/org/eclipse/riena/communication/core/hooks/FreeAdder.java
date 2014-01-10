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
package org.eclipse.riena.communication.core.hooks;

/**
 *
 */
public class FreeAdder {

	public String add(final String s1, final String s2) {
		return s1 + s2;
	}

	public String add(final Number n1, final Number n2) {
		return n1.toString() + n2.toString();
	}

	public String add(final Integer i1, final Integer i2) {
		return Integer.toString(i1 + i2);
	}

}
