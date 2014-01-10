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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonManager;
import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.tests.base.TestMultiSelectionBean;
import org.eclipse.riena.ui.tests.base.TestSingleSelectionBean;

/**
 * Tests of the class TableRidgetTableViewer (and also inner classes).
 */
@NonUITestCase
public class TableRidgetTableViewerTest extends TestCase {

	protected PersonManager manager;
	protected Person person1;
	protected Person person2;
	protected Person person3;

	protected TestSingleSelectionBean singleSelectionBean;
	protected TestMultiSelectionBean multiSelectionBean;
	private TableRidget ridget;
	private Shell shell;
	private Table table;

	@Override
	public void setUp() throws Exception {

		super.setUp();

		final Display display = Display.getDefault();
		final Realm realm = SWTObservables.getRealm(display);
		assertNotNull(realm);
		ReflectionUtils.invokeHidden(realm, "setDefault", realm); //$NON-NLS-1$

		shell = new Shell(SWT.SYSTEM_MODAL | SWT.ON_TOP);
		table = new Table(shell, SWT.NONE);

		ridget = new TableRidget();
		ridget.setUIControl(table);

		manager = new PersonManager(createPersonList());
		final Iterator<Person> it = manager.getPersons().iterator();
		person1 = it.next();
		person2 = it.next();
		person3 = it.next();

	}

	@Override
	protected void tearDown() throws Exception {
		ridget = null;
		table = null;
		shell.dispose();
		shell = null;

		super.tearDown();
	}

	/**
	 * Tests the method {@code checkStateChanged} of the inner class {@code TableRidgetCheckStateListener}.
	 * 
	 * @throws Exception
	 *             handled by junit
	 */
	public void testCheckStateChanged() throws Exception {

		final String[] properties1 = new String[] { "hasCat", "firstname" }; //$NON-NLS-1$ //$NON-NLS-2$
		final String[] headers1 = new String[] { "Cat", "First Name" }; //$NON-NLS-1$ //$NON-NLS-2$
		ridget.bindToModel(manager, "persons", Person.class, properties1, headers1); //$NON-NLS-1$

		final TableRidgetTableViewer viewer = new TableRidgetTableViewer(ridget);

		final Object listener = ReflectionUtils.newInstanceHidden(
				"org.eclipse.riena.internal.ui.ridgets.swt.TableRidgetTableViewer$TableRidgetCheckStateListener", ridget); //$NON-NLS-1$

		assertTrue(person1.isHasCat());
		CheckStateChangedEvent event = new CheckStateChangedEvent(viewer, person1, true);
		ReflectionUtils.invokeHidden(listener, "checkStateChanged", event); //$NON-NLS-1$
		assertTrue(person1.isHasCat());

		event = new CheckStateChangedEvent(viewer, person1, false);
		ReflectionUtils.invokeHidden(listener, "checkStateChanged", event); //$NON-NLS-1$
		assertFalse(person1.isHasCat());

		assertFalse(person2.isHasCat());
		event = new CheckStateChangedEvent(viewer, person2, true);
		ReflectionUtils.invokeHidden(listener, "checkStateChanged", event); //$NON-NLS-1$
		assertTrue(person2.isHasCat());

	}

	private Collection<Person> createPersonList() {
		final Collection<Person> newList = new ArrayList<Person>();

		Person person = new Person("Doe", "John"); //$NON-NLS-1$ //$NON-NLS-2$
		person.setHasCat(true);
		newList.add(person);

		person = new Person("Jackson", "Janet"); //$NON-NLS-1$ //$NON-NLS-2$
		person.setHasCat(false);
		newList.add(person);

		person = new Person("Jackson", "Jermaine"); //$NON-NLS-1$ //$NON-NLS-2$
		person.setHasCat(true);
		newList.add(person);

		return newList;
	}

}
