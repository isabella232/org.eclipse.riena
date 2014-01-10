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
package org.eclipse.riena.navigation;

/**
 * Interface for the observation of the jump target state of an
 * {@link INavigationNode}.
 */
public interface IJumpTargetListener {

	public enum JumpTargetState {
		ENABLED, DISABLED;

		public JumpTargetState valueOf(final Boolean flag) {
			return flag ? ENABLED : DISABLED;
		}
	}

	/**
	 * Called when "jumpBack" from the node is enabled oder disabled
	 */
	void jumpTargetStateChanged(INavigationNode<?> node, JumpTargetState jumpTargetState);

}
