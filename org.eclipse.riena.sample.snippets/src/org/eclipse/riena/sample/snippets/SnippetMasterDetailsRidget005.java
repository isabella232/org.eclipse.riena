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

import java.util.Arrays;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonFactory;
import org.eclipse.riena.ui.ridgets.AbstractMasterDetailsDelegate;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.IMasterDetailsRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Demonstrates how to dynamically update the ridget in the detail area
 * depending on the current selection.
 */
public final class SnippetMasterDetailsRidget005 {

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
			GridLayoutFactory.fillDefaults().numColumns(1).margins(20, 20).spacing(10, 10).equalWidth(false)
					.applyTo(parent);
			final GridDataFactory hFill = GridDataFactory.fillDefaults().grab(true, false);

			UIControlsFactory.createLabel(parent, "First Name:"); //$NON-NLS-1$
			final Combo combo = UIControlsFactory.createCombo(parent, "combo"); //$NON-NLS-1$
			hFill.applyTo(combo);
		}

		@Override
		protected Button createButtonNew(final Composite compButton) {
			return null; // no 'New' button
		}
	}

	/**
	 * A IMasterDetailsDelegate that renames a person.
	 */
	private static final class PersonDelegate extends AbstractMasterDetailsDelegate {

		private final Person workingCopy = createWorkingCopy();
		private IComboRidget combo;

		public void configureRidgets(final IRidgetContainer container) {
			final WritableList options = new WritableList(Arrays.asList("empty"), String.class); //$NON-NLS-1$
			final IObservableValue selection = PojoObservables.observeValue(workingCopy, Person.PROPERTY_FIRSTNAME);
			combo = (IComboRidget) container.getRidget("combo"); //$NON-NLS-1$
			combo.bindToModel(options, String.class, null, selection);
		}

		@Override
		public void prepareItemSelected(final Object newSelection) {
			if (newSelection == null) {
				return;
			}
			final Person person = (Person) newSelection;
			WritableList values = null;
			if (person.getLastname().startsWith("Jackson")) { //$NON-NLS-1$
				values = new WritableList(
						Arrays.asList("Jackie", "Tito", "Jermaine", "Marlon", "Michael", "Janet", "Joseph", "Katherine"), String.class); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
			} else {
				values = new WritableList(Arrays.asList("Joey", "Jack", "Jane", "Frank", "John"), String.class); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			}
			final IObservableValue selection = PojoObservables.observeValue(workingCopy, Person.PROPERTY_FIRSTNAME);
			combo.bindToModel(values, String.class, null, selection);
			combo.setSelection(person.getFirstname());
			combo.updateFromModel();
		}

		public Person copyBean(final Object source, final Object target) {
			final Person from = source != null ? (Person) source : createWorkingCopy();
			final Person to = target != null ? (Person) target : createWorkingCopy();
			to.setFirstname(from.getFirstname());
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
			final boolean equal = p1.getFirstname().equals(p2.getFirstname());
			return !equal;
		}
	}

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		final Shell shell = UIControlsFactory.createShell(display);
		shell.setText(SnippetMasterDetailsRidget005.class.getSimpleName());
		shell.setLayout(new FillLayout());

		final PersonMasterDetails details = new PersonMasterDetails(shell, SWT.NONE);
		final IMasterDetailsRidget ridget = (IMasterDetailsRidget) SwtRidgetFactory.createRidget(details);
		ridget.setDelegate(new PersonDelegate());

		final String text = "Select a 'Jackson' do see different suggestion in the Combo.\n" //$NON-NLS-1$
				+ "This is updated dynamically when an item is selected."; //$NON-NLS-1$
		final Label info = UIControlsFactory.createLabel(details, text, SWT.WRAP);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(info);

		final WritableList input = new WritableList(PersonFactory.createPersonList(), Person.class);
		final String[] properties = { Person.PROPERTY_LASTNAME, Person.PROPERTY_FIRSTNAME };
		final String[] headers = { "Last Name", "First Name" }; //$NON-NLS-1$ //$NON-NLS-2$
		ridget.bindToModel(input, Person.class, properties, headers);
		ridget.updateFromModel();

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
