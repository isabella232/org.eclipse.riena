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

import org.eclipse.riena.navigation.INavigationAssembler;

/**
 * Implementation of the interface {@code INavigationAssembly2Extension}. This
 * is only used for conversion of the legacy extension
 * {@link INavigationAssembly2Extension} (and unit tests).
 * */
public class NavigationAssembly2Extension implements INavigationAssembly2Extension {

	private ISubApplicationNode2Extension[] subApplications;
	private IModuleGroupNode2Extension[] moduleGroups;
	private IModuleNode2Extension[] modules;
	private ISubModuleNode2Extension[] subModules;
	private String parentNodeId;
	private int startOrder;
	private String navigationAssembler;
	private String id;
	private INavigationAssembler assembler;

	/**
	 * {@inheritDoc}
	 */
	public ISubApplicationNode2Extension[] getSubApplications() {
		return subApplications;
	}

	/**
	 * {@inheritDoc}
	 */
	public IModuleGroupNode2Extension[] getModuleGroups() {
		return moduleGroups;
	}

	/**
	 * {@inheritDoc}
	 */
	public IModuleNode2Extension[] getModules() {
		return modules;
	}

	/**
	 * {@inheritDoc}
	 */
	public ISubModuleNode2Extension[] getSubModules() {
		return subModules;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getParentNodeId() {
		return parentNodeId;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getStartOrder() {
		return startOrder;
	}

	/**
	 * {@inheritDoc}
	 */
	public INavigationAssembler createNavigationAssembler() {
		return assembler;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getNavigationAssembler() {
		return navigationAssembler;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getId() {
		return id;
	}

	public void setNavigationAssembler(final String getNavigationAssembler) {
		this.navigationAssembler = getNavigationAssembler;
	}

	/**
	 * Sets the ID of this assembly.
	 * 
	 * @param assembly
	 *            ID
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * Sets all sub-application definitions of this assembly.
	 * 
	 * @param subApplications
	 *            sub-application definitions
	 */
	public void setSubApplications(final ISubApplicationNode2Extension[] subApplications) {
		this.subApplications = subApplications;
	}

	/**
	 * Sets all module group definitions of this assembly.
	 * 
	 * @param moduleGroups
	 *            module group definitions
	 */
	public void setModuleGroups(final IModuleGroupNode2Extension[] moduleGroups) {
		this.moduleGroups = moduleGroups;
	}

	/**
	 * Sets all module definitions of this assembly.
	 * 
	 * @param modules
	 *            module definitions
	 */
	public void setModules(final IModuleNode2Extension[] modules) {
		this.modules = modules;
	}

	/**
	 * Sets all sub-module definitions of this assembly.
	 * 
	 * @param subModules
	 *            sub-module definitions
	 */
	public void setSubModules(final ISubModuleNode2Extension[] subModules) {
		this.subModules = subModules;
	}

	/**
	 * Sets the ID of the parent indicating where to insert a node or subtree
	 * created with this definition in the application model tree.
	 * 
	 * @param parentNodeId
	 *            ID of the parent node
	 */
	public void setParentNodeId(final String parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	/**
	 * Sets the index that this assembly takes in the system startup sequence. 0
	 * or less indicates that automatic startup of this assembly is not desired.
	 * 
	 * @param startOrder
	 *            >0 start order; otherwise no auto start
	 */
	public void setStartOrder(final int startOrder) {
		this.startOrder = startOrder;
	}

	public void setAssembler(final INavigationAssembler assembler) {
		this.assembler = assembler;
	}

}
