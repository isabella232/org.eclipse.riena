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
package org.eclipse.riena.ui.ridgets;

/**
 * Utility to check system properties for sub-modules.
 * 
 * @since 4.0
 */
public final class SubModuleUtils {

	private SubModuleUtils() {
		// Utility
	}

	/**
	 * This system property defines if also the view of a sub-module should be
	 * create (or only the controller) after the node was created.
	 */
	public static final String RIENA_PREPARE_VIEW_SYSTEM_PROPERTY = "riena.prepare.view"; //$NON-NLS-1$

	/**
	 * This is the default value (i.e. if the value is not explicitly defined)
	 * for the system property {@code RIENA_PREPARE_VIEW_SYSTEM_PROPERTY}
	 */
	public static final String PREPARE_VIEW_DEFAULT = Boolean.FALSE.toString();

	/**
	 * Returns whether also the view of a sub-module should be create after the
	 * node was created or only the controller of the sub-module.
	 * <p>
	 * For every sub-module node it is possible to define, if a sub-module
	 * should or shouldn't be prepared after the node was created. This will be
	 * defined with the property {@code RequiredPreparation} of the interface
	 * {@link IWorkareaDefinition}.
	 * 
	 * @return {@code true} create also view; {@code false} create only
	 *         controller
	 */
	public static boolean isPrepareView() {
		return Boolean.parseBoolean(System.getProperty(RIENA_PREPARE_VIEW_SYSTEM_PROPERTY, PREPARE_VIEW_DEFAULT));
	}

}
