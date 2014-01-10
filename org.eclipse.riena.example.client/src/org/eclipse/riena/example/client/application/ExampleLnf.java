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
package org.eclipse.riena.example.client.application;

import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;

/**
 * Look and Feel (Lnf) of the example application.<br>
 * The Lnf uses its own theme.
 */
public class ExampleLnf extends RienaDefaultLnf {

	/**
	 * ID of this Look and Feel
	 */
	private final static String LNF_ID = "ExampleLnf"; //$NON-NLS-1$

	/**
	 * Creates a new instance of {@code ExampleLnf}
	 */
	public ExampleLnf() {
		super(new ExampleTheme());
	}

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf#getLnfId()
	 */
	@Override
	protected String getLnfId() {
		return LNF_ID;
	}

}
