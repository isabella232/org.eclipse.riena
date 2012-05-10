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
package org.eclipse.riena.sample.app.client.helloworld.applications;

import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.swt.application.SwtApplication;
import org.eclipse.riena.sample.app.client.helloworld.views.CustomerSearchSubModuleView;
import org.eclipse.riena.sample.app.client.helloworld.views.HelloServerSubModuleView;
import org.eclipse.riena.sample.app.client.helloworld.views.HelloWorldSubModuleView;
import org.eclipse.riena.ui.workarea.WorkareaManager;

/**
 * Very simple application with only one sub application, one module group, one module and one sub module.
 */
public class HelloWorldApplication extends SwtApplication {

	private IApplicationNode application;

	/**
	 * Creates the model of the application "Hello world".
	 */
	@Override
	public IApplicationNode createModel() {

		application = new ApplicationNode("Hello World Application"); //$NON-NLS-1$
		final ISubApplicationNode subApplication = new SubApplicationNode(new NavigationNodeId("sapRiena"), "Riena Samples"); //$NON-NLS-1$ //$NON-NLS-2$
		WorkareaManager.getInstance().registerDefinition(subApplication, "helloWorldSubApplication"); //$NON-NLS-1$
		application.addChild(subApplication);

		IModuleGroupNode moduleGroup = new ModuleGroupNode(new NavigationNodeId("mg")); //$NON-NLS-1$
		subApplication.addChild(moduleGroup);

		// simple hello world
		final IModuleNode helloWorldModule = new ModuleNode(new NavigationNodeId("mWorld"), "Hello World"); //$NON-NLS-1$ //$NON-NLS-2$
		moduleGroup.addChild(helloWorldModule);

		ISubModuleNode helloWorldSubModule = new SubModuleNode(new NavigationNodeId("smWorld"), "Hello World"); //$NON-NLS-1$ //$NON-NLS-2$
		WorkareaManager.getInstance().registerDefinition(helloWorldSubModule, HelloWorldSubModuleView.ID);
		helloWorldModule.addChild(helloWorldSubModule);

		// hello server
		final IModuleNode helloServerModule = new ModuleNode(new NavigationNodeId("mServer"), "Hello Server"); //$NON-NLS-1$ //$NON-NLS-2$
		moduleGroup.addChild(helloServerModule);

		final ISubModuleNode helloServerSubModule = new SubModuleNode(new NavigationNodeId("smServer"), "Hello Server"); //$NON-NLS-1$ //$NON-NLS-2$
		WorkareaManager.getInstance().registerDefinition(helloServerSubModule, HelloServerSubModuleView.ID);
		helloServerModule.addChild(helloServerSubModule);

		// customer search sample
		final IModuleNode cSearchModule = new ModuleNode(new NavigationNodeId("mSearch"), "Customer Search"); //$NON-NLS-1$ //$NON-NLS-2$
		moduleGroup.addChild(cSearchModule);

		final ISubModuleNode cSearchSubModule = new SubModuleNode(new NavigationNodeId("smSearch"), "Customer Search"); //$NON-NLS-1$ //$NON-NLS-2$
		WorkareaManager.getInstance().registerDefinition(cSearchSubModule, CustomerSearchSubModuleView.ID);
		cSearchModule.addChild(cSearchSubModule);

		final ISubApplicationNode subApplication2 = new SubApplicationNode(new NavigationNodeId("sapSimple"), "Simple Subapplication"); //$NON-NLS-1$ //$NON-NLS-2$
		WorkareaManager.getInstance().registerDefinition(subApplication2, "org.eclipse.riena.sample.app.client.second"); //$NON-NLS-1$
		application.addChild(subApplication2);
		moduleGroup = new ModuleGroupNode(new NavigationNodeId("mg2")); //$NON-NLS-1$
		subApplication2.addChild(moduleGroup);
		final IModuleNode module = new ModuleNode(new NavigationNodeId("m1"), "Module 1"); //$NON-NLS-1$ //$NON-NLS-2$
		moduleGroup.addChild(module);
		helloWorldSubModule = new SubModuleNode(new NavigationNodeId("smWorld"), "Hello World"); //$NON-NLS-1$ //$NON-NLS-2$
		WorkareaManager.getInstance().registerDefinition(helloWorldSubModule, HelloWorldSubModuleView.ID);
		module.addChild(helloWorldSubModule);

		return application;

	}

}
