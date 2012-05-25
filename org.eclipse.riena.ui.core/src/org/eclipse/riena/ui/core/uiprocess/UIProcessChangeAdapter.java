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
package org.eclipse.riena.ui.core.uiprocess;

/**
 * A convenience implementation of {@link IUIProcessChangeListener} which allows to implement (override) only relevant listener methods.
 * 
 * @since 4.0
 * 
 */
public abstract class UIProcessChangeAdapter implements IUIProcessChangeListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.core.uiprocess.IUIProcessChangeListener#onInitialUpdateUI(int)
	 */
	public void afterInitialUpdateUI(final int totalWork) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.core.uiprocess.IUIProcessChangeListener#onFinalUpdateUI()
	 */
	public void afterFinalUpdateUI() {
	}

}
