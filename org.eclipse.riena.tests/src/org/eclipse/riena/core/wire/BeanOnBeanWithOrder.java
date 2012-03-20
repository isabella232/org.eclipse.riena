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
package org.eclipse.riena.core.wire;

/**
 *
 */
public class BeanOnBeanWithOrder extends BeanWithOrder {

	private Schtonk schtonk;
	private Stunk stunk;

	@InjectService(order = 1)
	public void bind1(final Schtonk schtonk) {
		this.schtonk = schtonk;
		SequenceUtil.add("BeanOnBeanWithOrder+schtonk");
	}

	public void unbind1(final Schtonk schtonk) {
		this.schtonk = schtonk;
		SequenceUtil.add("BeanOnBeanWithOrder+schtonk");
	}

	@InjectService(order = 0)
	public void bind1(final Stunk stunk) {
		this.stunk = stunk;
		SequenceUtil.add("BeanOnBeanWithOrder+stunk");
	}

	public void unbind1(final Stunk stunk) {
		this.stunk = stunk;
		SequenceUtil.add("BeanOnBeanWithOrder+stunk");
	}

	@OnWiringDone
	public void done1() {
		SequenceUtil.add("BeanOnBeanWithOrder+done");
	}

	/**
	 * @return
	 */
	public boolean hasStunk1() {
		return stunk != null;
	}

	/**
	 * @return
	 */
	public boolean hasSchtonk1() {
		return schtonk != null;
	}

}
