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
package org.eclipse.riena.navigation.ui.swt.views;

import java.io.IOException;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;
import org.eclipse.jface.bindings.Binding;
import org.eclipse.jface.bindings.Scheme;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.keys.IBindingService;
import org.eclipse.ui.presentations.AbstractPresentationFactory;
import org.eclipse.ui.statushandlers.AbstractStatusHandler;
import org.eclipse.ui.statushandlers.StatusAdapter;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.exception.IExceptionHandlerManager;
import org.eclipse.riena.core.service.Service;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.navigation.ui.swt.Activator;
import org.eclipse.riena.internal.navigation.ui.swt.IAdvisorHelper;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.navigation.ui.swt.presentation.stack.TitlelessStackPresentationFactory;
import org.eclipse.riena.ui.swt.facades.BindingServiceFacade;
import org.eclipse.riena.ui.swt.uiprocess.SwtUISynchronizer;

public class ApplicationAdvisor extends WorkbenchAdvisor {

	private final ApplicationController controller;
	private final IAdvisorHelper advisorHelper;
	private final AbstractPresentationFactory presentationFactory;

	/**
	 * @noreference This constructor is not intended to be referenced by
	 *              clients.
	 */
	public ApplicationAdvisor(final ApplicationController controller, final IAdvisorHelper factory) {
		this.controller = controller;
		this.advisorHelper = factory;
		presentationFactory = new TitlelessStackPresentationFactory();
	}

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {
		configurer.setPresentationFactory(presentationFactory);
		final WorkbenchWindowAdvisor workbenchWindowAdvisor = new ApplicationViewAdvisor(configurer, controller,
				advisorHelper);
		Wire.instance(workbenchWindowAdvisor).andStart(Activator.getDefault().getContext());
		return workbenchWindowAdvisor;
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return null;
	}

	@Override
	public synchronized AbstractStatusHandler getWorkbenchErrorHandler() {
		return new AbstractStatusHandler() {
			@Override
			public void handle(final StatusAdapter statusAdapter, final int style) {
				Service.get(IExceptionHandlerManager.class).handleException(statusAdapter.getStatus().getException(),
						statusAdapter.getStatus().getMessage());
			}
		};
	}

	@Override
	public void postStartup() {
		installDefaultBinding();
		controller.getNavigationNode().activate();
	}

	@Override
	public boolean preShutdown() {
		boolean result = super.preShutdown();
		if (result) {
			controller.getNavigationNode().dispose();
		}
		result &= controller.getNavigationNode().isDisposed();

		if (result) {
			SwtUISynchronizer.setWorkbenchShutdown(true);
		}

		return result;
	}

	// helping methods
	//////////////////

	private void installDefaultBinding() {
		final BindingServiceFacade facade = BindingServiceFacade.getDefault();

		final IBindingService bindingService = (IBindingService) PlatformUI.getWorkbench().getService(
				IBindingService.class);
		final String schemeId = advisorHelper.getKeyScheme();
		final Scheme rienaScheme = facade.getScheme(bindingService, schemeId);
		try {
			// saving will activate (!) the scheme:
			final Binding[] bindings = facade.getBindings(bindingService);
			facade.savePreferences(bindingService, rienaScheme, bindings);
		} catch (final IOException ioe) {
			final Logger logger = Log4r.getLogger(Activator.getDefault(), this.getClass());
			logger.log(LogService.LOG_ERROR, "Could not activate scheme: " + schemeId, ioe); //$NON-NLS-1$
		}
	}
}
