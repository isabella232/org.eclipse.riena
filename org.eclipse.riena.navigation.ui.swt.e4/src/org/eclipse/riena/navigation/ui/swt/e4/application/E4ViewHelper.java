/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.swt.e4.application;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.impl.AdvancedFactoryImpl;
import org.eclipse.e4.ui.model.application.ui.advanced.impl.PerspectiveStackImpl;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainer;
import org.eclipse.e4.ui.model.application.ui.basic.impl.BasicFactoryImpl;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.workarea.IWorkareaDefinition;
import org.eclipse.riena.ui.workarea.WorkareaManager;

/**
 * Helper class for rendering of {@link SubModuleView}s.
 */
@SuppressWarnings("restriction")
public class E4ViewHelper {

	public static final String CONTEXT_KEY_PERSPECTIVE = "sys.perspective"; //$NON-NLS-1$
	private static final String NAVIGATION_VIEW_PART_ID = "org.eclipse.riena.navigation.ui.swt/org.eclipse.riena.navigation.ui.swt.views.NavigationViewPart"; //$NON-NLS-1$
	private static final String VIEWS_EXTENSION_POINT = "org.eclipse.ui.views"; //$NON-NLS-1$
	private static final String BUNDLE_CLASS_PREFIX = "bundleclass://"; //$NON-NLS-1$

	@SuppressWarnings("unchecked")
	public MPart createSubModulePart(final MApplication wbApp, final ISubModuleNode source) {
		MPart part;
		final ISubApplicationNode subApp = source.getParentOfType(ISubApplicationNode.class);
		final MPerspective perspective = (MPerspective) subApp.getContext(CONTEXT_KEY_PERSPECTIVE);
		final MPartSashContainer sashContainer = (MPartSashContainer) perspective.getChildren().get(0);
		final IExtensionRegistry registry = wbApp.getContext().get(IExtensionRegistry.class);
		final IWorkareaDefinition definition = WorkareaManager.getInstance().getDefinition(source);
		final String viewId = (String) definition.getViewId();
		final IConfigurationElement[] configElements = registry.getConfigurationElementsFor(VIEWS_EXTENSION_POINT);
		final IConfigurationElement extension = findViewExtension(configElements, viewId);

		part = BasicFactoryImpl.INSTANCE.createPart();
		part.setContext(wbApp.getContext().createChild(source.getNodeId().toString()));
		part.getContext().set(SubModuleNode.class, (SubModuleNode) source);
		part.getContext().set(MPart.class, part);
		part.setContributionURI(BUNDLE_CLASS_PREFIX + extension.getDeclaringExtension().getNamespaceIdentifier()
				+ "/" + viewId); //$NON-NLS-1$
		part.setElementId(viewId + System.currentTimeMillis());
		part.setParent((MElementContainer<MUIElement>) ((InternalEObject) sashContainer));
		return part;
	}

	private IConfigurationElement findViewExtension(final IConfigurationElement[] configElements, final String viewId) {
		for (final IConfigurationElement iConfigurationElement : configElements) {

			final String configId = iConfigurationElement.getAttribute("id"); //$NON-NLS-1$
			if (viewId.equals(configId)) {
				return iConfigurationElement;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void createE4SubApplication(final MApplication wbApp, final PerspectiveStackImpl stack, final int i,
			final ISubApplicationNode subApp) {
		final MPerspective perspective = AdvancedFactoryImpl.INSTANCE.createPerspective();
		perspective.setParent((MElementContainer<MUIElement>) ((InternalEObject) stack));
		perspective.setElementId(subApp.getNodeId().toString() + ".perspective"); //$NON-NLS-1$
		perspective.setToBeRendered(true);
		perspective.setVisible(true);
		perspective.setContext(wbApp.getContext().createChild(
				subApp.getNodeId().toString() + System.currentTimeMillis()));
		perspective.getContext().set("node", subApp); //$NON-NLS-1$
		subApp.setContext(CONTEXT_KEY_PERSPECTIVE, perspective);

		final MPartSashContainer sashContainer = BasicFactoryImpl.INSTANCE.createPartSashContainer();
		sashContainer.setElementId(subApp.getNodeId().toString() + ".sash"); //$NON-NLS-1$
		sashContainer.setParent((MElementContainer<MUIElement>) ((InternalEObject) perspective));
		sashContainer.setHorizontal(true);

		final MPart mPart = BasicFactoryImpl.INSTANCE.createPart();
		mPart.setContext(perspective.getContext().createChild());
		mPart.getContext().set(MPart.class, mPart);
		mPart.setContributionURI(BUNDLE_CLASS_PREFIX + NAVIGATION_VIEW_PART_ID);
		mPart.setElementId(NAVIGATION_VIEW_PART_ID + i);
		mPart.setParent((MElementContainer<MUIElement>) ((InternalEObject) sashContainer));
		mPart.getContext().set(ISubApplicationNode.class, subApp);

		subApp.addListener(new PerspectiveActivator(subApp, stack));
	}

}
