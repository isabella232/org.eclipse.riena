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

import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.workbench.renderers.swt.ContributedPartRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;

/**
 *
 */
@SuppressWarnings("restriction")
final class RienaE4PartRenderer extends ContributedPartRenderer {
	@Override
	public Object createWidget(final MUIElement element, final Object parent) {
		final Composite composite = (Composite) super.createWidget(element, parent);
		composite.getLayout();
		if (!element.getElementId().toLowerCase().contains("navigationviewpart")) {
			composite.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));
			final FormData formData = new FormData();
			formData.left = new FormAttachment(0, 210);
			formData.top = new FormAttachment(0);
			formData.right = new FormAttachment(100);
			formData.bottom = new FormAttachment(100);
			formData.width = 200;
			composite.setLayoutData(formData);
		}
		return composite;
	}
}