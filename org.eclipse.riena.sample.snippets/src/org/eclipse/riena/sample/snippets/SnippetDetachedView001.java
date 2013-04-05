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
package org.eclipse.riena.sample.snippets;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.ViewPart;

import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.DetachedViewsManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Snippet showing how to use the {@link DetachedViewsManager} to open / hide /
 * close another view.
 * <p>
 * In a full Riena application (i.e. with Navigation) you could use an
 * ISimpleNavigationNode listener to show / hide the view when a specific node
 * is selected. See the DetachedSubModuleView class in the
 * org.eclipse.riena.example.client bundle for a more detailed example.
 */
public class SnippetDetachedView001 {

	public static void main(final String[] args) {
		final Display display = Display.getDefault();

		try {
			final Shell shell = new Shell();
			shell.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
			GridLayoutFactory.fillDefaults().numColumns(3).margins(10, 10).spacing(20, 10).applyTo(shell);

			final Button buttonShow = UIControlsFactory.createButton(shell, "Show", "buttonShow"); //$NON-NLS-1$ //$NON-NLS-2$
			final Button buttonHide = UIControlsFactory.createButton(shell, "Hide", "buttonHide"); //$NON-NLS-1$ //$NON-NLS-2$
			final Button buttonClose = UIControlsFactory.createButton(shell, "Close", "buttonClose"); //$NON-NLS-1$ //$NON-NLS-2$

			final DetachedViewsManager dvManager = new DetachedViewsManager(shell);

			final IActionRidget ridgetShow = (IActionRidget) SwtRidgetFactory.createRidget(buttonShow);
			ridgetShow.addListener(new IActionListener() {
				public void callback() {
					dvManager.showView("myView", SampleView.class, SWT.RIGHT); //$NON-NLS-1$
				}
			});

			final IActionRidget ridgetHide = (IActionRidget) SwtRidgetFactory.createRidget(buttonHide);
			ridgetHide.addListener(new IActionListener() {
				public void callback() {
					dvManager.hideView("myView"); //$NON-NLS-1$
				}
			});

			final IActionRidget ridgetClose = (IActionRidget) SwtRidgetFactory.createRidget(buttonClose);
			ridgetClose.addListener(new IActionListener() {
				public void callback() {
					dvManager.closeView("myView"); //$NON-NLS-1$
				}
			});

			shell.setBounds(new Rectangle(0, 0, 300, 300));
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}

			// dispose the detached view manager when no longer needed
			dvManager.dispose();
		} finally {
			display.dispose();
		}
	}

	// helping classes
	//////////////////

	/**
	 * A ViewPart used in this snipped.
	 */
	public static final class SampleView extends ViewPart {
		@Override
		public void createPartControl(final Composite parent) {
			parent.setLayout(new FillLayout());
			final String text = String.valueOf(System.currentTimeMillis());
			UIControlsFactory.createLabel(parent, text);
		}

		@Override
		public void setFocus() {
			// unused
		}
	}

}
