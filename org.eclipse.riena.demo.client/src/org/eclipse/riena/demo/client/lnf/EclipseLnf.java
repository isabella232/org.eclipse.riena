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
package org.eclipse.riena.demo.client.lnf;

import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;

/**
 * TODO JavaDoc
 */
public class EclipseLnf extends RienaDefaultLnf {

	/**
	 * ID of this Look and Feel
	 */
	private final static String LNF_ID = "EclipseLnf"; //$NON-NLS-1$

	/**
	 * Creates a new instance of {@code ExampleLnf}
	 */
	public EclipseLnf() {
		super(new EclipseTheme());
	}

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf#getLnfId()
	 */
	@Override
	protected String getLnfId() {
		return LNF_ID;
	}

}
