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
package org.eclipse.riena.internal.demo.client;

import com.swtdesigner.SWTResourceManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 *
 */
public class DemoClientUIControlsFactory extends UIControlsFactory {

	public static Label createSectionLabel(final org.eclipse.swt.widgets.Composite parent, final String text) {
		final Label createLabel = createLabel(parent, text);
		createLabel.setFont(SWTResourceManager.getFont("Arial", 12, SWT.BOLD)); //$NON-NLS-1$
		return createLabel;
	}

	public static Composite createSeparator(final Composite parent) {
		final Composite composite1 = UIControlsFactory.createComposite(parent, SWT.NONE);
		composite1.setBackground(SWTResourceManager.getColor(121, 117, 168));
		return composite1;
	}

}
