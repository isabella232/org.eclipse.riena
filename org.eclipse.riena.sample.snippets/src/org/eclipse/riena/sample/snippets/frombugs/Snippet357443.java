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
package org.eclipse.riena.sample.snippets.frombugs;

import org.eclipse.riena.beans.common.ListBean;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IListRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Two list ridgets
 */
public final class Snippet357443 {

	public static void main(String[] args) {
		Display display = Display.getDefault();
		try {
			Shell shell = new Shell();
			shell.setLayout(new FillLayout(SWT.VERTICAL));

			final Control c1 = UIControlsFactory.createList(shell, true, true);
			c1.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
			final Control c2 = UIControlsFactory.createList(shell, true, true);

			Button c3 = UIControlsFactory.createButton(shell);
			Button c4 = UIControlsFactory.createButton(shell);
			Button c5 = UIControlsFactory.createButton(shell);

			final IListRidget r1 = (IListRidget) SwtRidgetFactory.createRidget(c1);
			r1.bindToModel(new ListBean("r1-a", "r1-b", "r1-c"), ListBean.PROPERTY_VALUES);
			r1.updateFromModel();
			final IListRidget r2 = (IListRidget) SwtRidgetFactory.createRidget(c2);
			r2.bindToModel(new ListBean("r2-a", "r2-b", "r2-c"), ListBean.PROPERTY_VALUES);
			r2.updateFromModel();
			
			IActionRidget a1 = (IActionRidget) SwtRidgetFactory.createRidget(c3);
			a1.setText("Toggle bound ridgets"); //$NON-NLS-1$
			a1.addListener(new IActionListener() {
				int i;
				public void callback() {
					i++;
					r1.setUIControl(null);
					r2.setUIControl(null);
					if (i % 2 == 0) {
						r1.setUIControl(c1);
						r2.setUIControl(c2);
					} else {
						r1.setUIControl(c2);
						r2.setUIControl(c1);
					}
				}
			});
			
			IActionRidget a2 = (IActionRidget) SwtRidgetFactory.createRidget(c4);
			a2.setText("Toggle r1 enablement"); //$NON-NLS-1$
			a2.addListener(new IActionListener() {
				public void callback() {
					r1.setEnabled(!r1.isEnabled());
				}
			});
			
			IActionRidget a3 = (IActionRidget) SwtRidgetFactory.createRidget(c5);
			a3.setText("Toggle r2 enablement"); //$NON-NLS-1$
			a3.addListener(new IActionListener() {
				public void callback() {
					r2.setEnabled(!r2.isEnabled());
				}
			});

			c3.setFocus();

			shell.setSize(600, 200);
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} finally {
			display.dispose();
		}
	}

	private static Label createLabel(Shell shell, String caption) {
		Label result = new Label(shell, SWT.NONE);
		result.setText(caption);
		return result;
	}
}