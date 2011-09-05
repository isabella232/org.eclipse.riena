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
package org.eclipse.riena.internal.communication.factory.hessian;

import org.eclipse.riena.core.injector.extension.DefaultValue;
import org.eclipse.riena.core.injector.extension.ExtensionInterface;

/**
 * {@code ExtensionInterface} for the configuration of the
 * {@code RemoteServiceFactoryHessian}.
 */
@ExtensionInterface(id = "configuration")
public interface IRemoteServiceFactoryHessianExtension {

	boolean isZipClientRequest();

	@DefaultValue("0")
	int getReadTimeout();

	@DefaultValue("0")
	int getConnectTimeout();

}