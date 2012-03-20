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
package org.eclipse.riena.ui.ridgets.swt.views;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.controller.AbstractWindowController;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Testcase for {@link AbstractDialogView}.
 */
@UITestCase
public class DialogViewTest extends RienaTestCase {

	private DialogViewStub dialogView;
	private AbstractWindowController dialogViewController;
	private Shell shell;

	private final static String BINDING_ID_FIRSTNAME = "txtFirstName";
	private final static String BINDING_ID_LASTNAME = "txtLastName";

	private final static String VALUE_FIRSTNAME = "Peter";
	private final static String VALUE_LASTNAME = "Lustig";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.tests.RienaTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		shell = new Shell();
		createDefaultRealm();
		dialogViewController = new AbstractWindowControllerStub();
		dialogView = new DialogViewStub(shell);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.tests.RienaTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		dialogView.close();
		shell.close();
		SwtUtilities.dispose(shell);
	}

	private void createDefaultRealm() {
		final Display display = Display.getDefault();
		final Realm realm = SWTObservables.getRealm(display);
		assertNotNull(realm);
		ReflectionUtils.invokeHidden(realm, "setDefault", realm);
	}

	public void testCreateController() throws Exception {
		assertNotNull(dialogView.getController());
		assertEquals(dialogViewController, dialogView.getController());
	}

	public void testBinding() throws Exception {
		// compares the textfield-text with the bound value
		// runs in a separate thread, because the build-method blocks
		shell.getDisplay().asyncExec(new Runnable() {
			public void run() {
				assertNotNull(dialogView.getTextFirstName());
				assertNotNull(dialogView.getTextLastName());
				assertEquals(dialogView.getTextFirstName(), VALUE_FIRSTNAME);
				assertEquals(dialogView.getTextLastName(), VALUE_LASTNAME);
				dialogView.close();
			}
		});
		dialogView.open();
	}

	private class DialogViewStub extends AbstractDialogView {

		private Text txtFirstName;
		private Text txtLastName;

		/**
		 * @param parent
		 */
		public DialogViewStub(final Composite parent) {
			super(parent.getShell());
		}

		@Override
		protected Control buildView(final Composite parent) {
			txtFirstName = UIControlsFactory.createText(parent);
			addUIControl(txtFirstName, BINDING_ID_FIRSTNAME);

			txtLastName = UIControlsFactory.createText(parent, SWT.None, BINDING_ID_LASTNAME);
			return parent;
		}

		@Override
		protected AbstractWindowController createController() {
			return dialogViewController;
		}

		public String getTextFirstName() {
			return txtFirstName.getText();
		}

		public String getTextLastName() {
			return txtLastName.getText();
		}
	}

	private final class AbstractWindowControllerStub extends AbstractWindowController {

		private final Person person;

		private AbstractWindowControllerStub() {
			person = new Person(VALUE_LASTNAME, VALUE_FIRSTNAME);
		}

		@Override
		public void configureRidgets() {
			super.configureRidgets();

			final ITextRidget txtFirstName = getRidget(BINDING_ID_FIRSTNAME);
			txtFirstName.bindToModel(person, "firstname");
			txtFirstName.updateFromModel();

			final ITextRidget txtLastName = getRidget(BINDING_ID_LASTNAME);
			txtLastName.bindToModel(person, "lastname");
			txtLastName.updateFromModel();
		}
	}
}
