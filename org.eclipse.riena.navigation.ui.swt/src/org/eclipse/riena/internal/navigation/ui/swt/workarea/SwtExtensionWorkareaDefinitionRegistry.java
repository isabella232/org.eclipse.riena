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
package org.eclipse.riena.internal.navigation.ui.swt.workarea;

import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.navigation.INavigationAssemblyExtension;
import org.eclipse.riena.navigation.extension.IModuleGroupNode2Extension;
import org.eclipse.riena.navigation.extension.IModuleNode2Extension;
import org.eclipse.riena.navigation.extension.INavigationAssembly2Extension;
import org.eclipse.riena.navigation.extension.ISubApplicationNode2Extension;
import org.eclipse.riena.navigation.extension.ISubModuleNode2Extension;
import org.eclipse.riena.navigation.model.AssembliesConverter;
import org.eclipse.riena.ui.ridgets.controller.IController;
import org.eclipse.riena.ui.ridgets.controller.IControllerFactory;
import org.eclipse.riena.ui.workarea.IWorkareaDefinition;
import org.eclipse.riena.ui.workarea.WorkareaDefinition;
import org.eclipse.riena.ui.workarea.spi.AbstractWorkareaDefinitionRegistry;

/**
 * Registry of all {@link IWorkareaDefinition}s defined for a SWT application
 * using extensions.
 */
public class SwtExtensionWorkareaDefinitionRegistry extends AbstractWorkareaDefinitionRegistry {

	/**
	 * Injects the extension of (legacy) assemblies.
	 * 
	 * @param data
	 *            extension of assemblies
	 */
	@InjectExtension(id = "assemblies")
	public void update(final INavigationAssemblyExtension[] data) {
		for (final INavigationAssemblyExtension nodeDefinition : data) {
			register(nodeDefinition);
		}
	}

	/**
	 * Registers the given (legacy) extension of an assembly. To do this the
	 * legacy extension must be converted first.
	 * 
	 * @param assembly
	 *            (legacy) assembly to register
	 */
	private void register(final INavigationAssemblyExtension assembly) {
		final INavigationAssembly2Extension assembly2 = AssembliesConverter.convert(assembly);
		register(assembly2);
	}

	/**
	 * Injects the extension of assemblies (assemblies2).
	 * 
	 * @param data
	 *            extension of assemblies
	 */
	@InjectExtension(id = "assemblies2")
	public void update(final INavigationAssembly2Extension[] data) {
		for (final INavigationAssembly2Extension nodeDefinition : data) {
			register(nodeDefinition);
		}
	}

	/**
	 * Registers the given extension of an assembly and all child
	 * <i>extensions</i>.
	 * 
	 * @param assembly
	 *            assembly to register
	 */
	private void register(final INavigationAssembly2Extension assembly) {

		if (assembly != null) {
			// register subapplication if it exists
			final ISubApplicationNode2Extension[] subApplications = assembly.getSubApplications();
			if ((subApplications != null) && (subApplications.length > 0)) {
				for (final ISubApplicationNode2Extension subApplication : subApplications) {
					register(subApplication);
				}
				return;
			}
			// register module group if it exists
			final IModuleGroupNode2Extension[] groups = assembly.getModuleGroups();
			if ((groups != null) && (groups.length > 0)) {
				for (final IModuleGroupNode2Extension group : groups) {
					register(group);
				}
				return;
			}
			// otherwise try module
			final IModuleNode2Extension[] modules = assembly.getModules();
			if ((modules != null) && (modules.length > 0)) {
				for (final IModuleNode2Extension module : modules) {
					register(module);
				}
				return;
			}
			// last resort is submodule
			final ISubModuleNode2Extension[] subModules = assembly.getSubModules();
			if ((subModules != null) && (subModules.length > 0)) {
				for (final ISubModuleNode2Extension subModule : subModules) {
					register(subModule);
				}
				return;
			}
		}
	}

	/**
	 * Registers the given <i>extension</i> of a sub application and and all
	 * child <i>extensions</i>.
	 * 
	 * @param subAppicationExt
	 *            extension of a sub application
	 */
	protected void register(final ISubApplicationNode2Extension subAppicationExt) {

		// create and register sub-application perspective definition
		final IWorkareaDefinition def = new WorkareaDefinition(subAppicationExt.getPerspectiveId());
		register(subAppicationExt.getNodeId(), def);

		// a sub-application must only contain module groups
		if (subAppicationExt.getModuleGroupNodes() != null) {
			for (final IModuleGroupNode2Extension groupDefinition : subAppicationExt.getModuleGroupNodes()) {
				register(groupDefinition);
			}
		}

	}

	/**
	 * Registers the given <i>extension</i> of a module group and and all child
	 * <i>extensions</i>.
	 * 
	 * @param groupExt
	 *            extension of a module group
	 */
	protected void register(final IModuleGroupNode2Extension groupExt) {

		// a module group must only contain modules
		if (groupExt.getModuleNodes() != null) {
			for (final IModuleNode2Extension moduleDefinition : groupExt.getModuleNodes()) {
				register(moduleDefinition);
			}
		}

	}

	/**
	 * Registers the given <i>extension</i> of a module and and all child
	 * <i>extensions</i>.
	 * 
	 * @param moduleExt
	 *            extension of a module
	 */
	protected void register(final IModuleNode2Extension moduleExt) {

		// a module must only contain submodules
		if (moduleExt.getSubModuleNodes() != null) {
			for (final ISubModuleNode2Extension submoduleDefinition : moduleExt.getSubModuleNodes()) {
				register(submoduleDefinition);
			}
		}

	}

	/**
	 * Registers the given <i>extension</i> of a sub-module and and all child
	 * <i>extensions</i>.
	 * 
	 * @param subModuleExt
	 *            extension of a sub-module
	 */
	protected void register(final ISubModuleNode2Extension subModuleExt) {

		// create and register view definition
		final WorkareaDefinition def = new WorkareaDefinition(new IControllerFactory() {
			public IController createController() {
				return subModuleExt.createController();
			}
		}, subModuleExt.getViewId());
		def.setViewShared(subModuleExt.isSharedView());
		def.setRequiredPreparation(subModuleExt.isRequiresPreparation());
		register(subModuleExt.getNodeId(), def);

		// a submodule may contain nested submodules
		if (subModuleExt.getSubModuleNodes() != null) {
			for (final ISubModuleNode2Extension nestedSubmoduleDefinition : subModuleExt.getSubModuleNodes()) {
				register(nestedSubmoduleDefinition);
			}
		}
	}

}
