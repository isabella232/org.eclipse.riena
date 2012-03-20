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
package org.eclipse.riena.example.client.handler;

/**
 * 
 */
public class ActiveSubApplicationHandler extends DummyHandler {

	@Override
	protected String getTitle() {
		return "Active Sub-Application"; //$NON-NLS-1$
	}

	@Override
	protected String getMessage() {
		return "This command is only enabled for one sub-application (Playground)!\n"; //$NON-NLS-1$
	}

}
