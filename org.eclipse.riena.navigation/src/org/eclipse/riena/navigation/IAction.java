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
package org.eclipse.riena.navigation;

/**
 * A System independent action definition. (e.g. jface.Action is SWT specific).
 * 
 * @see INavigationNode
 */
public interface IAction {

	/**
	 * The Callback-method that will be invoked.
	 */
	void run();

}
