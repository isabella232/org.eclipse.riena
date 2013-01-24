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
package org.eclipse.riena.navigation.extension;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;
import org.eclipse.riena.navigation.INavigationAssembler;

/**
 * Interface with the common methods of an extension of a navigation assembly.
 * 
 * @since 2.0
 */
@ExtensionInterface
public interface ICommonNavigationAssemblyExtension {

	/**
	 * Creates a navigation assembler that creates a node or a subtree for the
	 * application model tree.
	 * 
	 * @return navigation assembler
	 */
	@MapName("assembler")
	INavigationAssembler createNavigationAssembler();

	/**
	 * Returns the class name of a navigation assembler that creates a node or a
	 * subtree for the application model tree.
	 * 
	 * @return class name of navigation assembler
	 */
	@MapName("assembler")
	String getNavigationAssembler();

	/**
	 * Returns the ID of this assembly.
	 * 
	 * @return assembly ID
	 */
	String getId();

}
