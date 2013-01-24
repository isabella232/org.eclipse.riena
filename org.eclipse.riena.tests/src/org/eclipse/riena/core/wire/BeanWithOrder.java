/*******************************************************************************
 * Copyright (c) 2007, 2013 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.wire;

/**
 *
 */
public class BeanWithOrder {

	private Schtonk schtonk;
	private Stunk stunk;

	@InjectService(order = 0)
	public void bind0(final Schtonk schtonk) {
		this.schtonk = schtonk;
		SequenceUtil.add("BeanWithOrder+schtonk");
	}

	public void unbind0(final Schtonk schtonk) {
		this.schtonk = schtonk;
		SequenceUtil.add("BeanWithOrder+schtonk");
	}

	@InjectService(order = 1)
	public void bind0(final Stunk stunk) {
		this.stunk = stunk;
		SequenceUtil.add("BeanWithOrder+stunk");
	}

	public void unbind0(final Stunk stunk) {
		this.stunk = stunk;
		SequenceUtil.add("BeanWithOrder+stunk");
	}

	@OnWiringDone
	public void done0() {
		SequenceUtil.add("BeanWithOrder+done");
	}

	/**
	 * @return
	 */
	public boolean hasStunk0() {
		return stunk != null;
	}

	/**
	 * @return
	 */
	public boolean hasSchtonk0() {
		return schtonk != null;
	}
}
