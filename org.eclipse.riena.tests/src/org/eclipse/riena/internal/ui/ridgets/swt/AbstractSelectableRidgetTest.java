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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.observable.Realm;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonFactory;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.ridgets.IRidgetContentFilterHolder;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractSelectableRidget;

/**
 * {@link RienaTestCase} for {@link AbstractSelectableRidget}
 */
@NonUITestCase
public class AbstractSelectableRidgetTest extends RienaTestCase {

	private MySelectableRidget ridget;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Realm.runWithDefault(new AlwaysCurrent(), new Runnable() {

			public void run() {
				ridget = new MySelectableRidget();
			}
		});

	}

	public void testHasSelection() throws Exception {
		assertFalse(ridget.hasSelection());
		ridget.getSingleSelectionObservable().setValue(PersonFactory.createPersonList().get(0));
		assertTrue(ridget.hasSelection());
	}

	public void testGetSingleSelection() throws Exception {
		assertNull(ridget.getSingleSelection());
		final Person value = PersonFactory.createPersonList().get(0);
		ridget.getSingleSelectionObservable().setValue(value);
		assertSame(value, ridget.getSingleSelection());
		ridget.setSelectionType(ISelectableRidget.SelectionType.MULTI);
		assertNull(ridget.getSingleSelection());
	}

	public void testGeMultiSelection() throws Exception {
		ridget.setSelectionType(ISelectableRidget.SelectionType.MULTI);
		assertNull(ridget.getMultiSelection());
		final List<Person> value = Arrays.asList(new Person[] { PersonFactory.createPersonList().get(0), PersonFactory.createPersonList().get(1) });
		ridget.getMultiSelectionObservable().addAll(value);
		assertEquals(2, ridget.getMultiSelection().size());
		ridget.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
		assertNull(ridget.getMultiSelection());
		ridget.setSelectionType(ISelectableRidget.SelectionType.MULTI);
		ridget.getMultiSelectionObservable().clear();
		assertNull(ridget.getMultiSelection());
	}

	class MySelectableRidget extends AbstractSelectableRidget {

		@Override
		protected List<?> getRowObservables() {
			return null;
		}

		@Override
		protected void checkUIControl(final Object uiControl) {
		}

		@Override
		protected void bindUIControl() {
		}

		@Override
		public boolean isDisableMandatoryMarker() {
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.riena.ui.ridgets.swt.AbstractSelectableRidget#getFilterHolder()
		 */
		@Override
		protected IRidgetContentFilterHolder<?> getFilterHolder() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	class AlwaysCurrent extends Realm {

		@Override
		public boolean isCurrent() {
			return true;
		}

	}

}
