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
package org.eclipse.riena.ui.swt.lnf;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;

/**
 * Extension interface for a LnF.
 * 
 * @since 3.0
 */
@ExtensionInterface(id = "defaultLnf")
public interface IDefaultLnfExtension {

	/**
	 * Returns the default LnF used in the Application.
	 * 
	 * @return the default LnF
	 */
	@MapName("class")
	RienaDefaultLnf createDefaultLnf();

}
