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
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.navigation.IModuleGroupNodeExtension;
import org.eclipse.riena.navigation.IModuleNodeExtension;
import org.eclipse.riena.navigation.INavigationAssemblyExtension;
import org.eclipse.riena.navigation.ISubApplicationNodeExtension;
import org.eclipse.riena.navigation.ISubModuleNodeExtension;
import org.eclipse.riena.navigation.extension.IModuleGroupNode2Extension;
import org.eclipse.riena.navigation.extension.IModuleNode2Extension;
import org.eclipse.riena.navigation.extension.INavigationAssembly2Extension;
import org.eclipse.riena.navigation.extension.ISubApplicationNode2Extension;
import org.eclipse.riena.navigation.extension.ISubModuleNode2Extension;
import org.eclipse.riena.navigation.extension.ModuleGroupNode2Extension;
import org.eclipse.riena.navigation.extension.ModuleNode2Extension;
import org.eclipse.riena.navigation.extension.NavigationAssembly2Extension;
import org.eclipse.riena.navigation.extension.SubApplicationNode2Extension;
import org.eclipse.riena.navigation.extension.SubModuleNode2Extension;
import org.eclipse.riena.ui.ridgets.controller.IController;
import org.eclipse.riena.ui.ridgets.controller.IControllerFactory;

/**
 * This utility class converts legacy assembly extension into new assembly(2).
 * extensions.
 */
public final class AssembliesConverter {

	private AssembliesConverter() {
		// utility class
	}

	/**
	 * Converts the given legacy assembly extension into new (2) assembly.
	 * 
	 * @param assembly
	 *            legacy assembly
	 * @return new assembly(2).
	 */
	public static INavigationAssembly2Extension convert(final INavigationAssemblyExtension assembly) {

		final NavigationAssembly2Extension assembly2 = new NavigationAssembly2Extension();
		assembly2.setAssembler(assembly.createNavigationAssembler());
		assembly2.setNavigationAssembler(assembly.getNavigationAssembler());
		assembly2.setId(assembly.getId());
		assembly2.setParentNodeId(assembly.getParentNodeId());
		try {
			assembly2.setStartOrder(assembly.getStartOrder());
		} catch (final NumberFormatException ex) {
			assembly2.setStartOrder(-1);
		}
		if (assembly.getSubApplicationNode() != null) {
			assembly2
					.setSubApplications(new ISubApplicationNode2Extension[] { convert(assembly.getSubApplicationNode()) });
		} else {
			assembly2.setSubApplications(new ISubApplicationNode2Extension[] {});
		}
		if (assembly.getModuleGroupNode() != null) {
			assembly2.setModuleGroups(new IModuleGroupNode2Extension[] { convert(assembly.getModuleGroupNode()) });
		} else {
			assembly2.setModuleGroups(new IModuleGroupNode2Extension[] {});
		}
		if (assembly.getModuleNode() != null) {
			assembly2.setModules(new IModuleNode2Extension[] { convert(assembly.getModuleNode()) });
		} else {
			assembly2.setModules(new IModuleNode2Extension[] {});
		}
		if (assembly.getSubModuleNode() != null) {
			assembly2.setSubModules(new ISubModuleNode2Extension[] { convert(assembly.getSubModuleNode()) });
		} else {
			assembly2.setSubModules(new ISubModuleNode2Extension[] {});
		}
		return assembly2;

	}

	private static ISubApplicationNode2Extension convert(final ISubApplicationNodeExtension nodeExt) {
		final SubApplicationNode2Extension node2Ext = new SubApplicationNode2Extension();
		node2Ext.setPerspectiveId(nodeExt.getViewId());
		node2Ext.setIcon(nodeExt.getIcon());
		node2Ext.setName(nodeExt.getLabel());
		node2Ext.setNodeId(nodeExt.getTypeId());
		node2Ext.setChildNodes(convert(nodeExt.getChildNodes()));
		return node2Ext;
	}

	static IModuleGroupNode2Extension[] convert(final IModuleGroupNodeExtension[] nodeExts) {
		if (nodeExts == null) {
			return new IModuleGroupNode2Extension[0];
		}
		final ModuleGroupNode2Extension[] node2Exts = new ModuleGroupNode2Extension[nodeExts.length];
		for (int i = 0; i < nodeExts.length; i++) {
			node2Exts[i] = convert(nodeExts[i]);
		}
		return node2Exts;
	}

	private static ModuleGroupNode2Extension convert(final IModuleGroupNodeExtension nodeExt) {
		final ModuleGroupNode2Extension node2Ext = new ModuleGroupNode2Extension();
		node2Ext.setNodeId(nodeExt.getTypeId());
		node2Ext.setChildNodes(convert(nodeExt.getChildNodes()));
		return node2Ext;
	}

	private static IModuleNode2Extension[] convert(final IModuleNodeExtension[] nodeExts) {
		if (nodeExts == null) {
			return new IModuleNode2Extension[0];
		}
		final ModuleNode2Extension[] node2Exts = new ModuleNode2Extension[nodeExts.length];
		for (int i = 0; i < nodeExts.length; i++) {
			node2Exts[i] = convert(nodeExts[i]);
		}
		return node2Exts;
	}

	private static ModuleNode2Extension convert(final IModuleNodeExtension nodeExt) {
		final ModuleNode2Extension node2Ext = new ModuleNode2Extension();
		node2Ext.setClosable(!nodeExt.isUnclosable());
		node2Ext.setIcon(nodeExt.getIcon());
		node2Ext.setName(nodeExt.getLabel());
		node2Ext.setNodeId(nodeExt.getTypeId());
		node2Ext.setChildNodes(convert(nodeExt.getChildNodes()));
		return node2Ext;
	}

	private static ISubModuleNode2Extension[] convert(final ISubModuleNodeExtension[] nodeExts) {
		if (nodeExts == null) {
			return new ISubModuleNode2Extension[0];
		}
		final SubModuleNode2Extension[] node2Exts = new SubModuleNode2Extension[nodeExts.length];
		for (int i = 0; i < nodeExts.length; i++) {
			node2Exts[i] = convert(nodeExts[i]);
		}
		return node2Exts;
	}

	private static SubModuleNode2Extension convert(final ISubModuleNodeExtension nodeExt) {
		final SubModuleNode2Extension node2Ext = new SubModuleNode2Extension();
		node2Ext.setControllerFactory(new IControllerFactory() {
			public IController createController() {
				return nodeExt.createController();
			}
		});
		node2Ext.setRequiresPreparation(nodeExt.isRequiresPreparation());
		node2Ext.setSelectable(nodeExt.isSelectable());
		node2Ext.setSharedView(nodeExt.isShared());
		node2Ext.setViewId(nodeExt.getViewId());
		node2Ext.setIcon(nodeExt.getIcon());
		node2Ext.setName(nodeExt.getLabel());
		node2Ext.setNodeId(nodeExt.getTypeId());
		node2Ext.setChildNodes(convert(nodeExt.getChildNodes()));
		return node2Ext;
	}
}
