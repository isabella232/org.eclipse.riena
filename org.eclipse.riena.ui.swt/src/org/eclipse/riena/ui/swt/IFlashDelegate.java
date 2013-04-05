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
package org.eclipse.riena.ui.swt;

/**
 * An algorithm for performing a flash operation on a ridget.
 * <p>
 * Implementations may vary. For example they may flash the background or a
 * marker. Typically this requires knowledge of both the ridget (for maintaining
 * correct marker / color states) and the widget (for performing the flash
 * operation).
 * 
 * @since 3.0
 */
public interface IFlashDelegate {

	/**
	 * Trigger the flash operation.
	 */
	public void flash();

}
