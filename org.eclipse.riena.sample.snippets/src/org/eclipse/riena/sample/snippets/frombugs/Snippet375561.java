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

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.swt.BorderMarkerSupport;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.lnf.ILnfMarkerSupportExtension;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Border marker support exceeding UI control size
 */
public class Snippet375561 {

	public static void main(final String[] args) {
		setBorderMarkerSupport();

		final Display display = Display.getDefault();
		try {
			final Shell shell = new Shell(display);
			GridLayoutFactory.swtDefaults().margins(20, 20).equalWidth(true).numColumns(2).applyTo(shell);
			final Group group = new Group(shell, SWT.NONE);
			group.setLayout(new GridLayout());
			group.setLayoutData(new GridData(GridData.FILL_BOTH));

			//			// combo
			//			Composite comp = new Composite(group, SWT.NONE);
			//			GridLayoutFactory.swtDefaults().margins(30, 0).applyTo(comp);
			//
			//			CCombo cc = UIControlsFactory.createCCombo(comp);
			//			GridDataFactory.swtDefaults().hint(500, SWT.DEFAULT).applyTo(cc);
			//			ICComboRidget ridget = (ICComboRidget) SwtRidgetFactory
			//					.createRidget(cc);
			//			ridget.setErrorMarked(true);

			// text
			final Composite comp1 = new Composite(group, SWT.NONE);
			GridLayoutFactory.fillDefaults().applyTo(comp1);
			final Control cc1 = UIControlsFactory.createCompletionCombo(comp1);

			final IMarkableRidget ridget1 = (IMarkableRidget) SwtRidgetFactory.createRidget(cc1);
			ridget1.setErrorMarked(true);

			comp1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			cc1.setLayoutData(new GridData(GridData.FILL_BOTH));

			shell.setSize(400, 400);
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

	/**
	 * 
	 */
	private static void setBorderMarkerSupport() {
		final String BORDER_MARKER_SUPPORT = "borderMarkerSupport";

		LnfManager.getLnf().putLnfSetting(LnfKeyConstants.MARKER_SUPPORT_ID, BORDER_MARKER_SUPPORT);
		LnfManager.getLnf().update(new ILnfMarkerSupportExtension[] { new ILnfMarkerSupportExtension() {
			public String getId() {
				return BORDER_MARKER_SUPPORT;
			}

			public AbstractMarkerSupport createMarkerSupport() {
				return new BorderMarkerSupport();
			}
		} });
	}

}
