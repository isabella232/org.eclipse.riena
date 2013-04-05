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

import java.util.Date;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.beans.common.TypedBean;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IDateTimeRidget;
import org.eclipse.riena.ui.ridgets.swt.DefaultRealm;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Bind a java.util.Date to IDateTimeRidgets.
 */
public final class SnippetDateTimeRidget001 {

	private SnippetDateTimeRidget001() {
		// "utility class"
	}

	public static void main(final String[] args) {
		final Display display = new Display();
		new DefaultRealm();
		final Shell shell = UIControlsFactory.createShell(display);
		shell.setLayout(new GridLayout(2, false));

		final TypedBean<Date> value = new TypedBean<Date>(new Date());

		final GridDataFactory fill = GridDataFactory.fillDefaults();

		UIControlsFactory.createLabel(shell, "Value:"); //$NON-NLS-1$
		final Text txtValue = UIControlsFactory.createText(shell, SWT.READ_ONLY);
		txtValue.setEnabled(false);
		fill.applyTo(txtValue);

		UIControlsFactory.createLabel(shell, "Date:"); //$NON-NLS-1$
		final DateTime dtDate = UIControlsFactory.createDate(shell, SWT.MEDIUM);
		fill.applyTo(dtDate);
		final IDateTimeRidget dtDateRidget = (IDateTimeRidget) SwtRidgetFactory.createRidget(dtDate);
		dtDateRidget.bindToModel(value, TypedBean.PROP_VALUE);
		dtDateRidget.updateFromModel();

		UIControlsFactory.createLabel(shell, "Time:"); //$NON-NLS-1$
		final DateTime dtTime = UIControlsFactory.createTime(shell, SWT.MEDIUM);
		fill.applyTo(dtTime);
		final IDateTimeRidget dtTimeRidget = (IDateTimeRidget) SwtRidgetFactory.createRidget(dtTime);
		dtTimeRidget.bindToModel(value, TypedBean.PROP_VALUE);
		dtTimeRidget.updateFromModel();

		final Button btnSync = UIControlsFactory.createButton(shell);
		btnSync.setText("Sync"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().span(2, 1).applyTo(btnSync);
		final IActionRidget btnSyncRidget = (IActionRidget) SwtRidgetFactory.createRidget(btnSync);
		btnSyncRidget.addListener(new IActionListener() {
			private long days = 0;

			public void callback() {
				days++;
				final long millis = System.currentTimeMillis() + (days * 24 * 3600 * 1000);
				value.setValue(new Date(millis));
				dtDateRidget.updateFromModel();
				dtTimeRidget.updateFromModel();
			}
		});

		final DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(WidgetProperties.text().observe(txtValue), BeansObservables.observeValue(value,
				TypedBean.PROP_VALUE), null, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
