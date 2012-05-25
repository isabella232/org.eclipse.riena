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
package org.eclipse.riena.navigation.ui.swt.e4;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.impl.TrimmedWindowImpl;

import org.eclipse.riena.navigation.IApplicationNode;

/**
 *
 */
@SuppressWarnings("restriction")
public class RienaPartRenderingEngine extends PartRenderingEngine {

	@Inject
	private IApplicationNode appNode;

	@Inject
	private IEventBroker eventBroker;

	@Inject
	public RienaPartRenderingEngine(@Named(E4Workbench.RENDERER_FACTORY_URI)
	@Optional
	final String factoryUrl) {
		super(factoryUrl);
	}

	@Override
	public Object createGui(final MUIElement element) {
		final Object gui = super.createGui(element);
		if (element instanceof TrimmedWindowImpl) {
			eventBroker.send(IRienaE4Events.APP_READY, new HashMap<String, Object>());
		}
		return gui;
	}

}
