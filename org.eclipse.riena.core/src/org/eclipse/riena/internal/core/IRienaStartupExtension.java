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
package org.eclipse.riena.internal.core;

import org.osgi.framework.Bundle;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;

/**
 * This extension interface defines a startup action that should be performed
 * after the bundle {@code org.eclipse.riena.core} has been started.
 */
@ExtensionInterface(id = "startups")
public interface IRienaStartupExtension {

	/**
	 * Individual startups can be marked to define when they should be executed.
	 * 
	 * @since 3.0
	 */
	public enum When {
		BEGINNING, END
	};

	/**
	 * Get the contributing bundle.
	 * 
	 * @return the contributing bundle
	 */
	Bundle getContributingBundle();

	/**
	 * Get the ´time´ when this startup action shall be performed.
	 * 
	 * @return either {@code null} or a value of {@code When}.
	 * 
	 * @since 3.0
	 */
	When getWhen();

	/**
	 * Get the name of a {@code Runnable} that shall be executed.
	 * 
	 * @return the name of the class
	 */
	@MapName("runClass")
	String getRunClassName();

	/**
	 * Create an instance of the {@code Runnable} that shall be executed.
	 * 
	 * @return the {@code Runnable}
	 */
	@MapName("runClass")
	Runnable createRunner();

	/**
	 * Return a comma separated list of bundle names that shall be activated.
	 * 
	 * @return the list og bundle names
	 */
	String getRequiredBundles();

	/**
	 * Activate the contributing bundle if {@code true} otherwise not.
	 * 
	 * @return true for self activation
	 */
	boolean isActivateSelf();

}
