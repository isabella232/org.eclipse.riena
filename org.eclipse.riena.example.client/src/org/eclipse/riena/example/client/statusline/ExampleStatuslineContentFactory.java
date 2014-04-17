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
package org.eclipse.riena.example.client.statusline;

import org.eclipse.swt.SWT;

import org.eclipse.riena.ui.swt.DefaultStatuslineContentFactory;
import org.eclipse.riena.ui.swt.IStatusLineContentFactory;
import org.eclipse.riena.ui.swt.Statusline;
import org.eclipse.riena.ui.swt.StatuslineNumber;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Example implementation of {@link IStatusLineContentFactory}
 */
public class ExampleStatuslineContentFactory extends DefaultStatuslineContentFactory {

	@Override
	protected StatuslineNumber createStatuslineNumber(final Statusline statusline) {
		return new StatuslineNumber(statusline, SWT.NONE) {

			@Override
			protected int getFixWidthLabel() {
				return SwtUtilities.convertXToDpi(115);
			}
		};
	}
}
