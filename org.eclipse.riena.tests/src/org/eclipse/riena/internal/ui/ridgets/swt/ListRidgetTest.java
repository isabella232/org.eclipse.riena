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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonManager;
import org.eclipse.riena.beans.common.StringPojo;
import org.eclipse.riena.beans.common.TypedComparator;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.ui.swt.test.UITestHelper;
import org.eclipse.riena.ui.common.ISortableByColumn;
import org.eclipse.riena.ui.ridgets.IListRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget.SelectionType;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.listener.ClickEvent;
import org.eclipse.riena.ui.ridgets.listener.SelectionEvent;
import org.eclipse.riena.ui.ridgets.swt.MarkerSupport;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;

/**
 * Tests of the class {@link ListRidget}.
 */
public class ListRidgetTest extends AbstractTableListRidgetTest {

	@Override
	protected List createWidget(final Composite parent) {
		return new List(parent, SWT.MULTI);
	}

	@Override
	protected IListRidget createRidget() {
		return new ListRidget();
	}

	@Override
	protected List getWidget() {
		return (List) super.getWidget();
	}

	@Override
	protected IListRidget getRidget() {
		return (IListRidget) super.getRidget();
	}

	@Override
	protected void bindRidgetToModel() {
		getRidget().bindToModel(manager, "persons", Person.class, "firstname");
	}

	// test methods
	// /////////////

	public void testRidgetMapping() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(ListRidget.class, mapper.getRidgetClass(getWidget()));
	}

	public void testUpdateFromModel() {
		final List control = getWidget();
		final ITableRidget ridget = getRidget();

		final int oldCount = manager.getPersons().size();

		assertEquals(oldCount, ridget.getObservableList().size());
		assertEquals(oldCount, control.getItemCount());

		manager.getPersons().remove(person1);

		final int newCount = oldCount - 1;

		assertEquals(newCount, manager.getPersons().size());
		assertEquals(oldCount, ridget.getObservableList().size());
		assertEquals(oldCount, control.getItemCount());

		ridget.updateFromModel();

		assertEquals(newCount, manager.getPersons().size());
		assertEquals(newCount, ridget.getObservableList().size());
		assertEquals(newCount, control.getItemCount());
	}

	public void testUpdateFromModelPreservesSelection() {
		final ITableRidget ridget = getRidget();

		ridget.setSelection(person2);

		assertSame(person2, ridget.getSelection().get(0));

		manager.getPersons().remove(person1);
		ridget.updateFromModel();

		assertSame(person2, ridget.getSelection().get(0));
	}

	public void testContainsOption() {
		final ITableRidget ridget = getRidget();

		assertTrue(ridget.containsOption(person1));
		assertTrue(ridget.containsOption(person2));
		assertTrue(ridget.containsOption(person3));

		assertFalse(ridget.containsOption(null));
		assertFalse(ridget.containsOption(new Person("", "")));

		final java.util.List<Person> persons = Arrays.asList(new Person[] { person3 });
		final PersonManager manager = new PersonManager(persons);
		ridget.bindToModel(manager, "persons", Person.class, new String[] { "firstname" }, new String[] { "" });
		ridget.updateFromModel();

		assertFalse(ridget.containsOption(person1));
		assertTrue(ridget.containsOption(person3));
	}

	public void testSetSelectionType() {
		final ITableRidget ridget = getRidget();
		final List control = getWidget();

		assertEquals(SelectionType.SINGLE, ridget.getSelectionType());
		assertTrue((control.getStyle() & SWT.MULTI) != 0);

		ridget.setSelection(new int[] { 0, 1 });

		// single selection is enforced
		assertEquals(1, ridget.getSelectionIndices().length);
		assertEquals(1, control.getSelectionCount());

		// multiple selection is now allowed
		ridget.setSelectionType(SelectionType.MULTI);
		ridget.setSelection(new int[] { 0, 1 });

		assertEquals(2, ridget.getSelectionIndices().length);
		assertEquals(2, control.getSelectionCount());
	}

	@Override
	public void testAddClickListener() {
		final IListRidget ridget = getRidget();
		ridget.updateFromModel();
		final List control = getWidget();

		try {
			ridget.addClickListener(null);
			fail();
		} catch (final RuntimeException npe) {
			ok();
		}

		final FTClickListener listener1 = new FTClickListener();
		ridget.addClickListener(listener1);

		final FTClickListener listener2 = new FTClickListener();
		ridget.addClickListener(listener2);
		ridget.addClickListener(listener2);

		final int clickedRow = 2; // 3rd row
		control.setSelection(clickedRow);
		final Event mdEvent = new Event();
		mdEvent.widget = control;
		mdEvent.button = 2;
		mdEvent.type = SWT.MouseDown;
		control.notifyListeners(SWT.MouseDown, mdEvent);
		mdEvent.type = SWT.MouseUp;
		control.notifyListeners(SWT.MouseUp, mdEvent);

		assertEquals(1, listener1.getCount());
		assertEquals(1, listener2.getCount());

		ClickEvent event = listener2.getEvent();
		assertEquals(getRidget(), event.getSource());
		assertEquals(2, event.getButton());
		assertEquals(0, event.getColumnIndex());
		assertSame(person3, event.getRow());

		ridget.removeClickListener(listener1);
		mdEvent.type = SWT.MouseDown;
		control.notifyListeners(SWT.MouseDown, mdEvent);
		mdEvent.type = SWT.MouseUp;
		control.notifyListeners(SWT.MouseUp, mdEvent);

		assertEquals(1, listener1.getCount());
		assertEquals(2, listener2.getCount());

		control.deselectAll();
		mdEvent.type = SWT.MouseDown;
		control.notifyListeners(SWT.MouseDown, mdEvent);
		mdEvent.type = SWT.MouseUp;
		control.notifyListeners(SWT.MouseUp, mdEvent);

		event = listener2.getEvent();
		assertEquals(3, listener2.getCount());
		assertEquals(2, event.getButton());
		assertEquals(0, event.getColumnIndex());
		assertEquals(null, event.getRow());
	}

	public void testAddDoubleClickListener() {
		final IListRidget ridget = getRidget();
		final List control = getWidget();

		try {
			ridget.addDoubleClickListener(null);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		final FTActionListener listener1 = new FTActionListener();
		ridget.addDoubleClickListener(listener1);

		final FTActionListener listener2 = new FTActionListener();
		ridget.addDoubleClickListener(listener2);
		ridget.addDoubleClickListener(listener2);

		final Event doubleClick = new Event();
		doubleClick.widget = control;
		doubleClick.type = SWT.MouseDoubleClick;
		control.notifyListeners(SWT.MouseDoubleClick, doubleClick);

		assertEquals(1, listener1.getCount());
		assertEquals(1, listener2.getCount());

		ridget.removeDoubleClickListener(listener1);

		control.notifyListeners(SWT.MouseDoubleClick, doubleClick);

		assertEquals(1, listener1.getCount());
	}

	public void testSetComparator() {
		final IListRidget ridget = getRidget();
		final List control = getWidget();

		// sorts from a to z
		final Comparator<Object> comparator = new TypedComparator<String>();

		try {
			ridget.setComparator(-1, comparator);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		try {
			ridget.setComparator(1, comparator);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		final int lastItemIndex = control.getItemCount() - 1;

		assertEquals("John", control.getItem(0));
		assertEquals("Frank", control.getItem(lastItemIndex));

		ridget.setComparator(0, comparator);
		ridget.setSortedColumn(0);

		assertEquals("Frank", control.getItem(0));
		assertEquals("John", control.getItem(lastItemIndex));

		ridget.setComparator(0, null);

		assertEquals("John", control.getItem(0));
		assertEquals("Frank", control.getItem(lastItemIndex));
	}

	public void testGetSortedColumn() {
		final IListRidget ridget = getRidget();

		try {
			ridget.setSortedColumn(1);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		try {
			ridget.setSortedColumn(-2);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		assertEquals(-1, ridget.getSortedColumn());

		ridget.setComparator(0, new TypedComparator<String>());

		assertEquals(-1, ridget.getSortedColumn());

		ridget.setSortedColumn(0);

		assertEquals(0, ridget.getSortedColumn());

		ridget.setComparator(0, null);

		assertEquals(-1, ridget.getSortedColumn());

		ridget.setSortedColumn(-1);

		assertEquals(-1, ridget.getSortedColumn());

		// no comparator in column 0
		ridget.setSortedColumn(0);

		assertEquals(-1, ridget.getSortedColumn());
	}

	public void testIsColumnSortable() {
		final IListRidget ridget = getRidget();

		try {
			assertFalse(ridget.isColumnSortable(-1));
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		try {
			assertFalse(ridget.isColumnSortable(1));
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		assertFalse(ridget.isColumnSortable(0));

		ridget.setComparator(0, new TypedComparator<String>());

		assertTrue(ridget.isColumnSortable(0));

		ridget.setComparator(0, null);

		assertFalse(ridget.isColumnSortable(0));
	}

	public void testSetColumnSortable() {
		final IListRidget ridget = getRidget();

		try {
			// TODO warning suppression: Ignore FindBugs warning about the
			// UnsupportedOperationException being thrown since this is
			// what the test is all about.
			ridget.setColumnSortable(0, true);
			fail();
		} catch (final UnsupportedOperationException uoe) {
			ok();
		}
	}

	public void testSetSortedAscending() {
		final IListRidget ridget = getRidget();
		final List control = getWidget();

		ridget.bindToModel(manager, "persons", Person.class, new String[] { "lastname" }, new String[] { "" });
		ridget.updateFromModel();
		final int lastItemIndex = control.getItemCount() - 1;

		assertEquals(-1, ridget.getSortedColumn());
		assertTrue(ridget.isSortedAscending());

		ridget.setSortedAscending(false);

		assertFalse(ridget.isSortedAscending());

		ridget.setComparator(0, new TypedComparator<String>());
		ridget.setSortedColumn(0);

		assertFalse(ridget.isSortedAscending());

		assertEquals("Zappa", control.getItem(0));
		assertEquals("Doe", control.getItem(lastItemIndex));

		ridget.setSortedAscending(true);

		assertTrue(ridget.isSortedAscending());
		assertEquals("Doe", control.getItem(0));
		assertEquals("Zappa", control.getItem(lastItemIndex));

		ridget.setComparator(0, null);

		assertEquals(-1, ridget.getSortedColumn());
		assertTrue(ridget.isSortedAscending());
	}

	public void testSetSortedAscendingFiresEvents() {
		final IListRidget ridget = getRidget();

		assertTrue(ridget.isSortedAscending());

		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, ISortableByColumn.PROPERTY_SORT_ASCENDING,
				Boolean.TRUE, Boolean.FALSE));

		ridget.setSortedAscending(false);

		verifyPropertyChangeEvents();
		expectNoPropertyChangeEvent();

		ridget.setSortedAscending(false);

		verifyPropertyChangeEvents();
		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, ISortableByColumn.PROPERTY_SORT_ASCENDING,
				Boolean.FALSE, Boolean.TRUE));

		ridget.setSortedAscending(true);

		verifyPropertyChangeEvents();
		expectNoPropertyChangeEvent();

		ridget.setSortedAscending(true);

		verifyPropertyChangeEvents();
	}

	public void testSetSortedColumnFiresEvents() {
		final IListRidget ridget = getRidget();

		assertEquals(-1, ridget.getSortedColumn());

		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, ISortableByColumn.PROPERTY_SORTED_COLUMN,
				Integer.valueOf(-1), Integer.valueOf(0)));

		ridget.setSortedColumn(0);

		verifyPropertyChangeEvents();
		expectNoPropertyChangeEvent();

		ridget.setSortedColumn(0);

		verifyPropertyChangeEvents();
		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, ISortableByColumn.PROPERTY_SORTED_COLUMN,
				Integer.valueOf(0), Integer.valueOf(-1)));

		ridget.setSortedColumn(-1);

		verifyPropertyChangeEvents();
	}

	public void testHasMoveableColumns() {
		final IListRidget ridget = getRidget();

		assertFalse(ridget.hasMoveableColumns());

		// TODO warning suppression: Ignore FindBugs warning about the
		// UnsupportedOperationException being thrown since this is
		// what the test is all about.
		try {
			ridget.setMoveableColumns(true);
			fail();
		} catch (final UnsupportedOperationException ex) {
			ok();
		}
	}

	/**
	 * Tests that for single selection, the ridget selection state and the ui
	 * selection state cannot be changed by the user when ridget is set to
	 * "output only".
	 */
	public void testOutputSingleSelectionCannotBeChangedFromUI() {
		final IListRidget ridget = getRidget();
		final List control = getWidget();

		ridget.setSelectionType(SelectionType.SINGLE);

		assertEquals(0, ridget.getSelection().size());
		assertEquals(0, control.getSelectionCount());

		ridget.setOutputOnly(true);
		control.setFocus();
		// press space to select row 0
		UITestHelper.sendString(control.getDisplay(), " ");

		assertEquals(0, ridget.getSelection().size());
		assertEquals(0, control.getSelectionCount());

		ridget.setOutputOnly(false);
		control.setFocus();
		// press space to select row 0
		UITestHelper.sendString(control.getDisplay(), " ");

		assertEquals(1, ridget.getSelection().size());
		assertEquals(1, control.getSelectionCount());
	}

	/**
	 * Tests that for multiple selection, the ridget selection state and the ui
	 * selection state cannot be changed by the user when ridget is set to
	 * "output only".
	 */
	public void testOutputMultipleSelectionCannotBeChangedFromUI() {
		final IListRidget ridget = getRidget();
		final List control = getWidget();

		ridget.setSelectionType(SelectionType.MULTI);

		assertEquals(0, ridget.getSelection().size());
		assertEquals(0, control.getSelectionCount());

		ridget.setOutputOnly(true);
		control.setFocus();
		// press space to select row 0
		UITestHelper.sendString(control.getDisplay(), " ");

		assertEquals(0, ridget.getSelection().size());
		assertEquals(0, control.getSelectionCount());

		ridget.setOutputOnly(false);
		control.setFocus();
		// press space to select row 0
		UITestHelper.sendString(control.getDisplay(), " ");

		assertEquals(1, ridget.getSelection().size());
		assertEquals(1, control.getSelectionCount());
	}

	/**
	 * Tests that toggling output state on/off does not change the selection.
	 */
	public void testTogglingOutputDoesNotChangeSelection() {
		final IListRidget ridget = getRidget();

		ridget.setSelection(0);

		assertEquals(0, ridget.getSelectionIndex());

		ridget.setOutputOnly(true);

		assertEquals(0, ridget.getSelectionIndex());

		ridget.setSelection((Object) null);

		assertEquals(-1, ridget.getSelectionIndex());

		ridget.setOutputOnly(false);

		assertEquals(-1, ridget.getSelectionIndex());
	}

	/**
	 * Tests that changing the selection in ridget works as expected, even when
	 * the ridget is disabled.
	 */
	public void testDisabledListIsEmptyFromRidget() {
		if (!MarkerSupport.isHideDisabledRidgetContent()) {
			System.out.println("Skipping ListRidgetTest.testDisabledListIsEmptyFromRidget()");
			return;
		}

		final IListRidget ridget = getRidget();
		final List control = getWidget();
		// the single selection is bound to another object in the parent class
		getRidget().bindSingleSelectionToModel(manager, "selectedPerson");

		ridget.setSelection(person1);

		assertEquals(person1.getFirstname(), control.getItem(control.getSelectionIndex()));
		assertEquals(person1, ridget.getSelection().get(0));
		assertEquals(person1, manager.getSelectedPerson());

		ridget.setEnabled(false);

		assertEquals(-1, control.getSelectionIndex());
		assertEquals(person1, ridget.getSelection().get(0));
		assertEquals(person1, manager.getSelectedPerson());

		ridget.setSelection(person2);

		assertEquals(-1, control.getSelectionIndex());
		assertEquals(person2, ridget.getSelection().get(0));
		assertEquals(person2, manager.getSelectedPerson());

		ridget.setEnabled(true);

		assertEquals(person2.getFirstname(), control.getItem(control.getSelectionIndex()));
		assertEquals(person2, ridget.getSelection().get(0));
		assertEquals(person2, manager.getSelectedPerson());
	}

	/**
	 * Tests that changing the selection in a bound model works as expected,
	 * even when the ridget is disabled.
	 */
	public void testDisabledListIsEmptyFromModel() {
		if (!MarkerSupport.isHideDisabledRidgetContent()) {
			System.out.println("Skipping ListRidgetTest.testDisabledListIsEmptyFromModel()");
			return;
		}

		final IListRidget ridget = getRidget();
		final List control = getWidget();
		// the single selection is bound to another object in the parent class
		getRidget().bindSingleSelectionToModel(manager, "selectedPerson");

		manager.setSelectedPerson(person1);
		ridget.updateSingleSelectionFromModel();

		assertEquals(person1.getFirstname(), control.getItem(control.getSelectionIndex()));
		assertEquals(person1, ridget.getSelection().get(0));
		assertEquals(person1, manager.getSelectedPerson());

		ridget.setEnabled(false);

		assertEquals(-1, control.getSelectionIndex());
		assertEquals(person1, ridget.getSelection().get(0));
		assertEquals(person1, manager.getSelectedPerson());

		manager.setSelectedPerson(person2);
		ridget.updateSingleSelectionFromModel();

		assertEquals(-1, control.getSelectionIndex());
		assertEquals(person2, ridget.getSelection().get(0));
		assertEquals(person2, manager.getSelectedPerson());

		ridget.setEnabled(true);

		assertEquals(person2.getFirstname(), control.getItem(control.getSelectionIndex()));
		assertEquals(person2, ridget.getSelection().get(0));
		assertEquals(person2, manager.getSelectedPerson());
	}

	/**
	 * Tests that disabling / enabling the ridget does not fire selection events
	 * (because the list is modified internally).
	 */
	public void testDisabledDoesNotFireSelection() {
		final IListRidget ridget = getRidget();
		final FTPropertyChangeListener listener = new FTPropertyChangeListener();
		ridget.addPropertyChangeListener(ISelectableRidget.PROPERTY_SELECTION, listener);

		ridget.setSelection(person1);
		int count = listener.getCount();

		ridget.setEnabled(false);

		assertEquals(count, listener.getCount());

		ridget.setSelection(person2);
		count = listener.getCount();

		ridget.setEnabled(true);

		assertTrue(count < listener.getCount());
	}

	/**
	 * Tests that disabling / enabling works as intended, even when no model is
	 * bound to the ridget.
	 */
	public void testDisableWithoutBoundModel() {
		final ListRidget ridget = (ListRidget) createRidget();
		final List control = getWidget();
		ridget.setUIControl(control);

		assertNull(ridget.getObservableList());

		ridget.setEnabled(false);

		assertFalse(ridget.isEnabled());
		assertFalse(control.isEnabled());

		ridget.setEnabled(true);

		assertTrue(ridget.isEnabled());
		assertTrue(control.isEnabled());
	}

	/**
	 * Tests that the disabled state is applied to a new control when set into
	 * the ridget.
	 */
	public void testDisableAndClearOnBind() {
		if (!MarkerSupport.isHideDisabledRidgetContent()) {
			System.out.println("Skipping ListRidgetTest.testDisableAndClearOnBind()");
			return;
		}

		final IListRidget ridget = getRidget();
		final List control = getWidget();
		// the single selection is bound to another object in the parent class
		getRidget().bindSingleSelectionToModel(manager, "selectedPerson");

		ridget.setUIControl(null);
		ridget.setEnabled(false);
		manager.setSelectedPerson(person1);
		ridget.updateSingleSelectionFromModel();
		ridget.setUIControl(control);

		assertFalse(control.isEnabled());
		assertEquals("", control.getItem(0)); // items 'hidden' == ""
		assertEquals(-1, control.getSelectionIndex()); // no selection
		assertEquals(person1, ridget.getSelection().get(0));

		ridget.setEnabled(true);

		assertTrue(control.isEnabled());
		assertTrue(control.getItem(0).length() > 0); // ! ""
		assertTrue(control.getSelectionIndex() > -1); // selection was restored
		assertEquals(manager.getPersons().size(), control.getItemCount());
		assertEquals(person1, ridget.getSelection().get(0));
	}

	public void testAddSelectionListener() {
		final IListRidget ridget = getRidget();
		final List control = getWidget();

		try {
			ridget.addSelectionListener(null);
			fail();
		} catch (final RuntimeException npe) {
			ok();
		}

		final TestSelectionListener selectionListener = new TestSelectionListener();
		ridget.addSelectionListener(selectionListener);

		ridget.setSelection(person1);
		assertEquals(1, selectionListener.getCount());
		ridget.removeSelectionListener(selectionListener);
		ridget.setSelection(person2);
		assertEquals(1, selectionListener.getCount());
		ridget.clearSelection();

		ridget.addSelectionListener(selectionListener);
		ridget.setSelectionType(SelectionType.SINGLE);
		assertEquals(0, ridget.getSelection().size());
		assertEquals(0, control.getSelectionCount());

		control.setFocus();
		UITestHelper.sendKeyAction(control.getDisplay(), UITestHelper.KC_ARROW_DOWN);

		assertEquals(1, ridget.getSelection().size());
		assertEquals(1, control.getSelectionCount());
		assertEquals(2, selectionListener.getCount());
		final SelectionEvent selectionEvent = selectionListener.getSelectionEvent();
		assertEquals(ridget, selectionEvent.getSource());
		assertTrue(selectionEvent.getOldSelection().isEmpty());
		assertEquals(ridget.getSelection(), selectionEvent.getNewSelection());
		// System.out.println("SelectionEvent: " + selectionListener.getSelectionEvent());

		UITestHelper.sendKeyAction(control.getDisplay(), UITestHelper.KC_ARROW_DOWN);

		assertEquals(1, ridget.getSelection().size());
		assertEquals(1, control.getSelectionCount());
		assertEquals(3, selectionListener.getCount());
		final SelectionEvent selectionEvent2 = selectionListener.getSelectionEvent();
		assertEquals(ridget, selectionEvent.getSource());
		assertEquals(selectionEvent.getNewSelection(), selectionEvent2.getOldSelection());
		assertEquals(ridget.getSelection(), selectionEvent2.getNewSelection());
		// System.out.println("SelectionEvent: " + selectionListener.getSelectionEvent());

		ridget.removeSelectionListener(selectionListener);
	}

	public void testSimplifiedBinding() {
		final List control = new List(getShell(), SWT.NONE);
		final IListRidget ridget = createRidget();
		ridget.setUIControl(control);
		final SimplifiedModel model = new SimplifiedModel();
		ridget.bindToModel(model, "values");

		assertEquals(null, ridget.getObservableList());

		ridget.updateFromModel();

		assertEquals(3, ridget.getObservableList().size());
		assertEquals(model.getValues().size(), control.getItemCount());
		assertEquals(SimplifiedModel.NAME_ONE, control.getItem(0));
		assertEquals(SimplifiedModel.NAME_TWO, control.getItem(1));
		assertEquals(SimplifiedModel.NAME_THREE, control.getItem(2));
	}

	public void testRebindToSimpleModel() {
		final IListRidget ridget = getRidget();
		final List control = getWidget();

		// already bound to Persons
		assertTrue(ridget.getObservableList().get(0) instanceof Person);

		// rebind to different model
		final SimplifiedModel model = new SimplifiedModel();
		ridget.bindToModel(model, "values");
		ridget.updateFromModel();

		assertTrue(ridget.getObservableList().get(0) instanceof String);
		assertEquals(SimplifiedModel.NAME_ONE, control.getItem(0));
		assertEquals(SimplifiedModel.NAME_TWO, control.getItem(1));
		assertEquals(SimplifiedModel.NAME_THREE, control.getItem(2));
	}

	/**
	 * As per bug 301182
	 */
	public void testRefreshNull() {
		final IListRidget ridget = createRidget();
		final List control = createWidget(getShell());
		ridget.setUIControl(control);

		final StringPojo word1 = new StringPojo("eclipse");
		final StringPojo word2 = new StringPojo("riena");
		final WritableList values = new WritableList(Arrays.asList(word1, word2), StringPojo.class);
		final String[] columns = { "value" };
		ridget.bindToModel(values, StringPojo.class, columns, null);
		ridget.updateFromModel();

		assertEquals("eclipse", control.getItem(0));
		assertEquals("riena", control.getItem(1));

		word1.setValue("alpha");
		word2.setValue("beta");

		assertEquals("eclipse", control.getItem(0));
		assertEquals("riena", control.getItem(1));

		ridget.refresh(null);

		assertEquals("alpha", control.getItem(0));
		assertEquals("beta", control.getItem(1));
	}

	/**
	 * As per bug 301182
	 */
	public void testRefresh() {
		final IListRidget ridget = createRidget();
		final List control = createWidget(getShell());
		ridget.setUIControl(control);

		final StringPojo word1 = new StringPojo("eclipse");
		final StringPojo word2 = new StringPojo("riena");
		final WritableList values = new WritableList(Arrays.asList(word1, word2), StringPojo.class);
		final String[] columns = { "value" };
		ridget.bindToModel(values, StringPojo.class, columns, null);
		ridget.updateFromModel();

		assertEquals("eclipse", control.getItem(0));
		assertEquals("riena", control.getItem(1));

		word1.setValue("alpha");
		word2.setValue("beta");

		assertEquals("eclipse", control.getItem(0));
		assertEquals("riena", control.getItem(1));

		ridget.refresh(word1);

		assertEquals("alpha", control.getItem(0));
		assertEquals("riena", control.getItem(1));
	}

	public void testGetOptionWithSorting() {
		final IListRidget ridget = getRidget();
		ridget.bindToModel(manager, "persons", Person.class, "lastname");
		ridget.updateFromModel();

		assertEquals(0, ridget.indexOfOption(person1));
		assertEquals(person1, ridget.getOption(0));

		ridget.setComparator(0, new StringComparator());
		ridget.setSortedColumn(0); // sort by last name
		ridget.setSortedAscending(false);

		final int last = ridget.getOptionCount() - 1;
		assertEquals(last, ridget.indexOfOption(person1));
		assertEquals(person1, ridget.getOption(last));

		ridget.setSortedAscending(true);

		assertEquals(0, ridget.indexOfOption(person1));
		assertEquals(person1, ridget.getOption(0));
	}

	public void testSetSelectionWithSorting() {
		final IListRidget ridget = getRidget();
		ridget.bindToModel(manager, "persons", Person.class, "lastname");
		ridget.updateFromModel();

		assertEquals(-1, ridget.getSelectionIndex());
		assertTrue(ridget.getSelection().isEmpty());

		ridget.setSelection(0);

		assertEquals(0, ridget.getSelectionIndex());
		assertEquals(person1, ridget.getSelection().get(0));

		ridget.setComparator(0, new StringComparator());
		ridget.setSortedColumn(0); // sort by last name
		ridget.setSortedAscending(false);

		final int last = ridget.getOptionCount() - 1;
		assertEquals(last, ridget.getSelectionIndex());
		assertEquals(person1, ridget.getSelection().get(0));

		ridget.setSortedAscending(true);

		assertEquals(0, ridget.getSelectionIndex());
		assertEquals(person1, ridget.getSelection().get(0));
	}

	// helping methods
	// ////////////////

	@Override
	protected void clearUIControlRowSelection() {
		getWidget().deselectAll();
		fireSelectionEvent();
	}

	@Override
	protected int getUIControlSelectedRowCount() {
		return getWidget().getSelectionCount();
	}

	@Override
	protected int getUIControlSelectedRow() {
		return getWidget().getSelectionIndex();
	}

	@Override
	protected Object getRowValue(final int i) {
		// return getRidget().getRowObservables().get(i);
		final IObservableList rowObservables = ReflectionUtils.invokeHidden(getRidget(), "getRowObservables");
		return rowObservables.get(i);
	}

	@Override
	protected int[] getSelectedRows() {
		// IObservableList rowObservables = getRidget().getRowObservables();
		final IObservableList rowObservables = ReflectionUtils.invokeHidden(getRidget(), "getRowObservables");
		final Object[] elements = getRidget().getMultiSelectionObservable().toArray();
		final int[] result = new int[elements.length];
		for (int i = 0; i < elements.length; i++) {
			final Object element = elements[i];
			result[i] = rowObservables.indexOf(element);
		}
		return result;
	}

	@Override
	protected int[] getUIControlSelectedRows() {
		return getWidget().getSelectionIndices();
	}

	@Override
	protected void setUIControlRowSelection(final int[] indices) {
		getWidget().setSelection(indices);
		fireSelectionEvent();
	}

	@Override
	protected void setUIControlRowSelectionInterval(final int start, final int end) {
		getWidget().setSelection(start, end);
		fireSelectionEvent();
	}

	@Override
	protected boolean supportsMulti() {
		return true;
	}

	// helping classes
	// ////////////////

	/**
	 * PropertyChangeListener that counts how often it is invoked.
	 */
	private static final class FTPropertyChangeListener implements PropertyChangeListener {

		private int count;

		public int getCount() {
			return count;
		}

		public void propertyChange(final PropertyChangeEvent evt) {
			count++;
			// System.out.println(count + "\t" + evt.getOldValue() + " -> " + evt.getNewValue());
		}
	}

	private static final class SimplifiedModel {
		private final java.util.List<String> values;
		private final static String NAME_ONE = "Janet";
		private final static String NAME_TWO = "Jermaine";
		private final static String NAME_THREE = "John";

		public SimplifiedModel() {
			values = Arrays.asList(new String[] { NAME_ONE, NAME_TWO, NAME_THREE });
		}

		public java.util.List<String> getValues() {
			return values;
		}
	}

	private static final class StringComparator implements Comparator<Object> {
		public int compare(final Object o1, final Object o2) {
			final String s1 = (String) o1;
			final String s2 = (String) o2;
			return s1.compareTo(s2);
		}
	}

}
