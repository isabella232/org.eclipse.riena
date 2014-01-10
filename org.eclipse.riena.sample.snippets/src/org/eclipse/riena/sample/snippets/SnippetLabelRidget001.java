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
package org.eclipse.riena.sample.snippets;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Text field ridget with minimum length validation rule and direct writing.
 */
public final class SnippetLabelRidget001 {

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = UIControlsFactory.createShell(display);
			GridLayoutFactory.fillDefaults().numColumns(2).margins(10, 10).spacing(20, 10).applyTo(shell);

			UIControlsFactory.createLabel(shell, "DateTime:"); //$NON-NLS-1$
			final Label dateTimeWidget = UIControlsFactory.createLabel(shell, ""); //$NON-NLS-1$
			GridDataFactory.fillDefaults().grab(true, false).applyTo(dateTimeWidget);

			final DatePojo datePojo = new DatePojo();

			final ILabelRidget dateTimeRidget = (ILabelRidget) SwtRidgetFactory.createRidget(dateTimeWidget);
			dateTimeRidget.bindToModel(PojoObservables.observeValue(datePojo, "time")); //$NON-NLS-1$

			final Timer t = new Timer();
			try {
				final TimerTask task = new TimerTask() {
					@Override
					public void run() {
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								try {
									datePojo.update(); // update bean to the current value for date & time
									dateTimeRidget.updateFromModel(); // update Ridget from bean
								} catch (final SWTException e) {
									t.cancel();
								}
							}
						});
					}
				};
				t.schedule(task, new Date(), 1000);

				shell.setSize(300, 100);
				shell.open();
				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}
			} finally {
				t.cancel();
			}
		} finally {
			display.dispose();
		}
	}

	private static class DatePojo {

		private Date time;

		DatePojo() {
			update();
		}

		public void update() {
			time = Calendar.getInstance().getTime();
		}

		@SuppressWarnings("unused")
		public Date getTime() {
			return time;
		}
	}
}
