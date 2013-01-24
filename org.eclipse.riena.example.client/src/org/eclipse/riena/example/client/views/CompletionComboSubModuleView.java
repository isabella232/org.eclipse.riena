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
package org.eclipse.riena.example.client.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.CompletionCombo;
import org.eclipse.riena.ui.swt.CompletionCombo.AutoCompletionMode;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Example for the CompletionCombo widget (combo with autocompletion as you
 * type).
 */
public class CompletionComboSubModuleView extends SubModuleView {
	public CompletionComboSubModuleView() {
	}

	public static final String ID = CompletionComboSubModuleView.class.getName();

	private CompletionCombo combo1;

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		GridLayoutFactory.swtDefaults().numColumns(1).applyTo(parent);

		final GridDataFactory gdfFill = GridDataFactory.fillDefaults().grab(true, false);

		final Group grpCustom = createGroup(parent, "CompletionCombo (custom widget)"); //$NON-NLS-1$
		GridLayoutFactory.swtDefaults().numColumns(4).equalWidth(false).spacing(15, 5).margins(20, 20)
				.applyTo(grpCustom);
		gdfFill.applyTo(grpCustom);
		createCustom(grpCustom);
	}

	@Override
	public void setFocus() {
		combo1.setFocus();
	}

	// helping methods
	//////////////////

	private void createCustom(final Composite parent) {
		UIControlsFactory.createLabel(parent, "CompletionCombo"); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, "Selection"); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, "Text"); //$NON-NLS-1$

		final GridDataFactory fill = GridDataFactory.fillDefaults().hint(100, SWT.DEFAULT);

		// row

		UIControlsFactory.createLabel(parent, "allow missmatch:"); //$NON-NLS-1$
		combo1 = UIControlsFactory.createCompletionCombo(parent, "combo1"); //$NON-NLS-1$
		fill.applyTo(combo1);
		combo1.setAutoCompletionMode(AutoCompletionMode.ALLOW_MISSMATCH);

		final Text selection1 = UIControlsFactory.createText(parent, SWT.BORDER, "selection1"); //$NON-NLS-1$ 
		fill.applyTo(selection1);

		UIControlsFactory.createText(parent, SWT.BORDER, "text1"); //$NON-NLS-1$ 

		// row

		UIControlsFactory.createLabel(parent, "allow no missmatch:"); //$NON-NLS-1$
		final CompletionCombo combo2 = UIControlsFactory.createCompletionCombo(parent, "combo2"); //$NON-NLS-1$
		fill.applyTo(combo2);

		final Text selection2 = UIControlsFactory.createText(parent, SWT.BORDER, "selection2"); //$NON-NLS-1$ 
		fill.applyTo(selection2);

		UIControlsFactory.createText(parent, SWT.BORDER, "text2"); //$NON-NLS-1$

		// row

		UIControlsFactory.createLabel(parent, "1st letter completion:"); //$NON-NLS-1$
		final CompletionCombo combo3 = UIControlsFactory.createCompletionCombo(parent, "combo3"); //$NON-NLS-1$
		combo3.setAutoCompletionMode(AutoCompletionMode.FIRST_LETTER_MATCH);
		fill.applyTo(combo3);

		final Text selection3 = UIControlsFactory.createText(parent, SWT.BORDER, "selection3"); //$NON-NLS-1$ 
		fill.applyTo(selection3);

		UIControlsFactory.createText(parent, SWT.BORDER, "text3"); //$NON-NLS-1$

		// row

		final Label spacer = UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		GridDataFactory.fillDefaults().span(4, 1).applyTo(spacer);

		// row

		UIControlsFactory.createLabel(parent, "CompletionComboWithImage"); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, "Selection"); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, "Text"); //$NON-NLS-1$

		// row

		UIControlsFactory.createLabel(parent, "allow missmatch:"); //$NON-NLS-1$
		final CompletionCombo combo4 = UIControlsFactory.createCompletionComboWithImage(parent, "combo4"); //$NON-NLS-1$
		fill.applyTo(combo4);
		combo4.setAutoCompletionMode(AutoCompletionMode.ALLOW_MISSMATCH);

		final Text selection4 = UIControlsFactory.createText(parent, SWT.BORDER, "selection4"); //$NON-NLS-1$ 
		fill.applyTo(selection4);

		UIControlsFactory.createText(parent, SWT.BORDER, "text4"); //$NON-NLS-1$

		// row

		UIControlsFactory.createLabel(parent, "allow no missmatch:"); //$NON-NLS-1$
		final CompletionCombo combo5 = UIControlsFactory.createCompletionComboWithImage(parent, "combo5"); //$NON-NLS-1$
		fill.applyTo(combo5);

		final Text selection5 = UIControlsFactory.createText(parent, SWT.BORDER, "selection5"); //$NON-NLS-1$ 
		fill.applyTo(selection5);

		UIControlsFactory.createText(parent, SWT.BORDER, "text5"); //$NON-NLS-1$

		// row

		UIControlsFactory.createLabel(parent, "1st letter completion:"); //$NON-NLS-1$
		final CompletionCombo combo6 = UIControlsFactory.createCompletionComboWithImage(parent, "combo6"); //$NON-NLS-1$
		combo6.setAutoCompletionMode(AutoCompletionMode.FIRST_LETTER_MATCH);
		fill.applyTo(combo6);

		final Text selection6 = UIControlsFactory.createText(parent, SWT.BORDER, "selection6"); //$NON-NLS-1$ 
		fill.applyTo(selection6);

		UIControlsFactory.createText(parent, SWT.BORDER, "text6"); //$NON-NLS-1$

		//		final Button button = UIControlsFactory.createButton(parent, "Default Button"); //$NON-NLS-1$
		//		button.addSelectionListener(new SelectionAdapter() {
		//			@Override
		//			public void widgetSelected(final SelectionEvent e) {
		//				System.out.println("!! Default Button clicked !!"); //$NON-NLS-1$
		//				java.awt.Toolkit.getDefaultToolkit().beep();
		//			}
		//		});
		//		button.getShell().setDefaultButton(button);
	}

	private Group createGroup(final Composite parent, final String title) {
		final Group result = UIControlsFactory.createGroup(parent, "Combo boxes"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(true).margins(20, 20).applyTo(result);
		result.setText(title);
		return result;
	}
}
