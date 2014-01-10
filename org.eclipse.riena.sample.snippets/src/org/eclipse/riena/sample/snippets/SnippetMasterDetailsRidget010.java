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

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonFactory;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.AbstractMasterDetailsDelegate;
import org.eclipse.riena.ui.ridgets.IMasterDetailsRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.ridgets.validation.NotEmpty;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Demonstrates the {@link IMasterDetailsRidget#setRemoveCancelsNew(boolean)
 * flag. When enabled the Remove button can be used to abort editing a new
 * entry.
 */
public final class SnippetMasterDetailsRidget010 {

	private SnippetMasterDetailsRidget010() {
		// "utility class"
	}

	/**
	 * A master details widget with a text fields for renaming a person.
	 */
	private static final class PersonMasterDetails extends MasterDetailsComposite {

		PersonMasterDetails(final Composite parent, final int style) {
			super(parent, style, SWT.BOTTOM);
			setMargins(5, 5);
		}

		@Override
		protected void createDetails(final Composite parent) {
			GridLayoutFactory.fillDefaults().numColumns(2).margins(20, 20).spacing(10, 10).equalWidth(false)
					.applyTo(parent);
			final GridDataFactory hFill = GridDataFactory.fillDefaults().grab(true, false);

			UIControlsFactory.createLabel(parent, "Last Name:"); //$NON-NLS-1$
			final Text txtLast = UIControlsFactory.createText(parent);
			hFill.applyTo(txtLast);
			addUIControl(txtLast, "txtLast"); //$NON-NLS-1$

			UIControlsFactory.createLabel(parent, "First Name:"); //$NON-NLS-1$
			final Text txtFirst = UIControlsFactory.createText(parent);
			hFill.applyTo(txtFirst);
			addUIControl(txtFirst, "txtFirst"); //$NON-NLS-1$
		}

		@Override
		protected int getDetailsStyle() {
			return SWT.BORDER;
		}
	}

	/**
	 * A IMasterDetailsDelegate that renames a person.
	 */
	private static final class PersonDelegate extends AbstractMasterDetailsDelegate {

		private final Person workingCopy = createWorkingCopy();

		public void configureRidgets(final IRidgetContainer container) {
			final ITextRidget txtLast = (ITextRidget) container.getRidget("txtLast"); //$NON-NLS-1$
			txtLast.bindToModel(workingCopy, Person.PROPERTY_LASTNAME);
			txtLast.addValidationRule(new NotEmpty(), ValidationTime.ON_UI_CONTROL_EDIT);
			txtLast.updateFromModel();

			final ITextRidget txtFirst = (ITextRidget) container.getRidget("txtFirst"); //$NON-NLS-1$
			txtFirst.bindToModel(workingCopy, Person.PROPERTY_FIRSTNAME);
			txtFirst.updateFromModel();
		}

		public Person copyBean(final Object source, final Object target) {
			final Person from = (Person) source;
			final Person to = (Person) target;
			to.setFirstname(from.getFirstname());
			to.setLastname(from.getLastname());
			return to;
		}

		public Person createWorkingCopy() {
			return new Person("", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}

		public Person getWorkingCopy() {
			return workingCopy;
		}

		@Override
		public boolean isChanged(final Object source, final Object target) {
			final Person p1 = (Person) source;
			final Person p2 = (Person) target;
			final boolean equal = p1.getFirstname().equals(p2.getFirstname())
					&& p1.getLastname().equals(p2.getLastname());
			return !equal;
		}

		@Override
		public String isValid(final IRidgetContainer container) {
			final ITextRidget txtLast = (ITextRidget) container.getRidget("txtLast"); //$NON-NLS-1$
			if (txtLast.isErrorMarked()) {
				return "'Last Name' is not valid."; //$NON-NLS-1$
			}
			return null;
		}
	}

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		final Shell shell = new Shell(display);
		shell.setText(SnippetMasterDetailsRidget010.class.getSimpleName());
		shell.setLayout(new FillLayout());

		final PersonMasterDetails details = new PersonMasterDetails(shell, SWT.NONE);

		final IMasterDetailsRidget ridget = (IMasterDetailsRidget) SwtRidgetFactory.createRidget(details);
		ridget.setDelegate(new PersonDelegate());
		final WritableList input = new WritableList(PersonFactory.createPersonList(), Person.class);
		final String[] properties = { Person.PROPERTY_LASTNAME, Person.PROPERTY_FIRSTNAME };
		final String[] headers = { "Last Name", "First Name" }; //$NON-NLS-1$ //$NON-NLS-2$
		ridget.bindToModel(input, Person.class, properties, headers);
		ridget.updateFromModel();

		ridget.setRemoveCancelsNew(true);

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
