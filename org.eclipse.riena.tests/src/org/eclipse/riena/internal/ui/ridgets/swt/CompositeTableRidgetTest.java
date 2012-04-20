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

import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.nebula.widgets.compositetable.AbstractNativeHeader;
import org.eclipse.swt.nebula.widgets.compositetable.CompositeTable;
import org.eclipse.swt.nebula.widgets.compositetable.ResizableGridRowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonFactory;
import org.eclipse.riena.beans.common.TypedComparator;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.ui.ridgets.swt.optional.CompositeTableRidget;
import org.eclipse.riena.internal.ui.swt.test.UITestHelper;
import org.eclipse.riena.ui.common.IComplexComponent;
import org.eclipse.riena.ui.common.ISortableByColumn;
import org.eclipse.riena.ui.ridgets.AbstractCompositeRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRowRidget;
import org.eclipse.riena.ui.ridgets.ISelectableIndexedRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.swt.optional.ICompositeTableRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Tests of the class {@link CompositeTableRidget}.
 */
public class CompositeTableRidgetTest extends AbstractTableListRidgetTest {

	@Override
	protected Widget createWidget(final Composite parent) {
		final CompositeTable control = new CompositeTable(parent, SWT.NONE);
		// If you make RowData to small, you'll get errors in the tests,
		// since the CompositeTable only creates as rows as fit on the screen.
		// The assumption is that this is large enough for at least 7 rows.
		control.setLayoutData(new RowData(300, 300));
		new Header(control, SWT.NONE);
		new Row(control, SWT.NONE);
		control.setRunTime(true);
		return control;
	}

	@Override
	protected IRidget createRidget() {
		return new CompositeTableRidget();
	}

	@Override
	protected CompositeTable getWidget() {
		return (CompositeTable) super.getWidget();
	}

	@Override
	protected ICompositeTableRidget getRidget() {
		return (ICompositeTableRidget) super.getRidget();
	}

	@Override
	protected void bindRidgetToModel() {
		final IObservableList rowObservables = BeansObservables.observeList(manager, "persons");
		getRidget().bindToModel(rowObservables, Person.class, RowRidget.class);
	}

	// test methods
	///////////////

	public void testRidgetMapping() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(CompositeTableRidget.class, mapper.getRidgetClass(getWidget()));
	}

	public void testBindToModelWithListHolder() {
		final Shell shell = new Shell(SWT.SYSTEM_MODAL | SWT.ON_TOP);
		try {
			shell.setLayout(new RowLayout(SWT.VERTICAL));
			final CompositeTable widget = (CompositeTable) createWidget(shell);

			final ICompositeTableRidget ridget = (ICompositeTableRidget) createRidget();
			ridget.setUIControl(widget);

			final MyModel model = new MyModel();
			ridget.bindToModel(model, "persons", Person.class, RowRidget.class);
			ridget.updateFromModel();

			shell.setSize(130, 100);
			shell.setLocation(0, 0);
			shell.open();
			UITestHelper.readAndDispatch(getWidget());

			assertNotNull(widget.getRowControls());
			assertEquals(model.getPersons().size(), widget.getRowControls().length);
		} finally {
			shell.dispose();
		}
	}

	public void testBindToModelWithColumnHeader() {
		final Shell shell = new Shell(SWT.SYSTEM_MODAL | SWT.ON_TOP);
		try {
			shell.setLayout(new RowLayout(SWT.VERTICAL));
			final CompositeTable widget = (CompositeTable) createWidget(shell);

			final ICompositeTableRidget ridget = (ICompositeTableRidget) createRidget();
			ridget.setUIControl(widget);

			final MyModel model = new MyModel();
			ridget.bindToModel(model, "persons", Person.class, RowRidget.class, new String[] { "First Name",
					"Last Name" });
			ridget.updateFromModel();

			shell.setSize(130, 100);
			shell.setLocation(0, 0);
			shell.open();
			UITestHelper.readAndDispatch(getWidget());

			assertNotNull(widget.getRowControls());
			assertEquals(model.getPersons().size(), widget.getRowControls().length);
			final AbstractNativeHeader header = (AbstractNativeHeader) widget.getHeader();
			assertEquals("First Name", header.getColumns()[0].getText());
			assertEquals("Last Name", header.getColumns()[1].getText());
		} finally {
			shell.dispose();
		}
	}

	@Override
	public void testUpdateSingleSelectionFromRidgetOnRebind() {
		final ISelectableIndexedRidget ridget = getRidget();
		final CompositeTable control = getWidget();

		setUIControlRowSelectionInterval(2, 2);

		assertEquals(2, getUIControlSelectedRow());

		ridget.setUIControl(null); // unbind
		control.setSelection(0, 1); // select row 1 without binding
		UITestHelper.readAndDispatch(control);

		assertEquals(1, getUIControlSelectedRow());
		assertEquals(getRowValue(2), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(2), multiSelectionBean.getSelectionList().get(0));

		ridget.setUIControl(control); // rebind; row 2 should be selected

		assertEquals(2, getUIControlSelectedRow());
		assertEquals(getRowValue(2), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(2), multiSelectionBean.getSelectionList().get(0));
	}

	public void testUpdateFromModelPreservesSelection() {
		final ISelectableRidget ridget = getRidget();

		ridget.setSelection(person2);

		assertSame(person2, ridget.getSelection().get(0));

		manager.getPersons().remove(person1);
		ridget.updateFromModel();

		assertSame(person2, ridget.getSelection().get(0));
	}

	public void testUpdateFromModelRemovesSelection() {
		final ISelectableRidget ridget = getRidget();

		ridget.setSelection(person2);

		assertSame(person2, ridget.getSelection().get(0));

		manager.getPersons().remove(person2);
		ridget.updateFromModel();

		assertTrue(ridget.getSelection().isEmpty());
	}

	public void testContainsOption() {
		final ICompositeTableRidget ridget = getRidget();

		assertTrue(ridget.containsOption(person1));
		assertTrue(ridget.containsOption(person2));
		assertTrue(ridget.containsOption(person3));

		assertFalse(ridget.containsOption(null));
		assertFalse(ridget.containsOption(new Person("", "")));

		manager.getPersons().remove(person1);
		manager.getPersons().remove(person2);

		assertTrue(ridget.containsOption(person1));
		assertTrue(ridget.containsOption(person2));
		assertTrue(ridget.containsOption(person3));

		ridget.updateFromModel();

		assertFalse(ridget.containsOption(person1));
		assertFalse(ridget.containsOption(person2));
		assertTrue(ridget.containsOption(person3));
	}

	public void testSetComparator() throws Exception {
		final ICompositeTableRidget ridget = getRidget();
		final CompositeTable control = getWidget();
		final Comparator<Object> comparatorFirst = new FirstNameComparator();
		final Comparator<Object> comparatorLast = new LastNameComparator();

		try {
			ridget.setComparator(-1, comparatorFirst);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		// not supported
		//		try {
		//			ridget.setComparator(2, comparatorFirst);
		//			fail();
		//		} catch (RuntimeException rex) {
		//			ok();
		//		}

		ridget.setSortedAscending(true);
		final int lastItemIndex = control.getNumRowsInCollection() - 1;

		assertEquals("John", getFirstNameFromRow(control, 0));
		assertEquals("Frank", getFirstNameFromRow(control, lastItemIndex));

		ridget.setComparator(0, comparatorFirst);

		assertEquals("John", getFirstNameFromRow(control, 0));
		assertEquals("Frank", getFirstNameFromRow(control, lastItemIndex));

		ridget.setSortedColumn(0);

		assertEquals("Frank", getFirstNameFromRow(control, 0));
		assertEquals("John", getFirstNameFromRow(control, lastItemIndex));

		ridget.setComparator(0, null);

		assertEquals("John", getFirstNameFromRow(control, 0));
		assertEquals("Frank", getFirstNameFromRow(control, lastItemIndex));

		ridget.setComparator(1, comparatorLast);
		ridget.setSortedColumn(1);
		UITestHelper.readAndDispatch(control);

		assertEquals("Doe", getLastNameFromRow(control, 0));
		assertEquals("Zappa", getLastNameFromRow(control, lastItemIndex));

		ridget.setSortedAscending(false);

		assertEquals("Zappa", getLastNameFromRow(control, 0));
		assertEquals("Doe", getLastNameFromRow(control, lastItemIndex));
	}

	public void testGetSortedColumn() {
		final ICompositeTableRidget ridget = getRidget();

		// not supported
		//		try {
		//			ridget.setSortedColumn(2);
		//			fail();
		//		} catch (RuntimeException rex) {
		//			ok();
		//		}

		assertEquals(-1, ridget.getSortedColumn());

		ridget.setComparator(0, new FirstNameComparator());

		assertEquals(-1, ridget.getSortedColumn());

		ridget.setSortedColumn(0);

		assertEquals(0, ridget.getSortedColumn());

		ridget.setComparator(0, null);

		assertEquals(-1, ridget.getSortedColumn());

		ridget.setComparator(1, new FirstNameComparator());
		ridget.setSortedColumn(1);

		assertEquals(1, ridget.getSortedColumn());

		ridget.setSortedColumn(-1);

		assertEquals(-1, ridget.getSortedColumn());

		// no comparator in column 0
		ridget.setSortedColumn(0);

		assertEquals(-1, ridget.getSortedColumn());
	}

	public void testIsColumnSortable() {
		final ICompositeTableRidget ridget = getRidget();

		try {
			assertFalse(ridget.isColumnSortable(-1));
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		// not supported
		//			try {
		//				assertFalse(ridget.isColumnSortable(2));
		//				fail();
		//			} catch (RuntimeException rex) {
		//				ok();
		//			}

		for (int i = 0; i < 2; i++) {
			assertFalse(ridget.isColumnSortable(i));

			// columns are sortable by default, when they have a comparator
			ridget.setComparator(i, new TypedComparator<String>());

			assertTrue(ridget.isColumnSortable(i));

			ridget.setColumnSortable(i, false);

			assertFalse(ridget.isColumnSortable(i));

			ridget.setColumnSortable(i, true);

			assertTrue(ridget.isColumnSortable(i));

			// columns are not sortable without a comparator
			ridget.setComparator(i, null);

			assertFalse(ridget.isColumnSortable(i));
		}
	}

	public void testSetSortedAscending() {
		final CompositeTable control = getWidget();
		final ICompositeTableRidget ridget = getRidget();

		// ridget.bindToModel(manager, "persons", Person.class, new String[] { "lastname", "firstname" }, null);
		final int lastItemIndex = control.getNumRowsInCollection() - 1;

		assertEquals(-1, ridget.getSortedColumn());
		assertFalse(ridget.isSortedAscending());

		ridget.setComparator(0, new LastNameComparator());
		ridget.setSortedColumn(0);

		assertTrue(ridget.isSortedAscending());
		assertEquals("Doe", getLastNameFromRow(control, 0));
		assertEquals("Zappa", getLastNameFromRow(control, lastItemIndex));

		ridget.setSortedAscending(false);

		assertFalse(ridget.isSortedAscending());
		assertEquals("Zappa", getLastNameFromRow(control, 0));
		assertEquals("Doe", getLastNameFromRow(control, lastItemIndex));

		ridget.setSortedAscending(true);

		assertTrue(ridget.isSortedAscending());
		assertEquals("Doe", getLastNameFromRow(control, 0));
		assertEquals("Zappa", getLastNameFromRow(control, lastItemIndex));

		ridget.setComparator(0, null);

		assertEquals(-1, ridget.getSortedColumn());
		assertFalse(ridget.isSortedAscending());
	}

	public void testSetSortedAscendingFiresEvents() {
		final ICompositeTableRidget ridget = getRidget();

		ridget.setSortedAscending(true);

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
		final ICompositeTableRidget ridget = getRidget();

		assertEquals(-1, ridget.getSortedColumn());

		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, ISortableByColumn.PROPERTY_SORTED_COLUMN,
				Integer.valueOf(-1), Integer.valueOf(0)));

		ridget.setSortedColumn(0);

		verifyPropertyChangeEvents();
		expectNoPropertyChangeEvent();

		ridget.setSortedColumn(0);

		verifyPropertyChangeEvents();
		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, ISortableByColumn.PROPERTY_SORTED_COLUMN,
				Integer.valueOf(0), Integer.valueOf(1)));

		ridget.setSortedColumn(1);

		verifyPropertyChangeEvents();
	}

	public void testSetColumnSortabilityFiresEvents() {
		final ICompositeTableRidget ridget = getRidget();

		expectNoPropertyChangeEvent();

		ridget.setColumnSortable(0, true);

		verifyPropertyChangeEvents();
		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, ISortableByColumn.PROPERTY_COLUMN_SORTABILITY, null,
				Integer.valueOf(0)));

		ridget.setColumnSortable(0, false);

		verifyPropertyChangeEvents();
		expectNoPropertyChangeEvent();

		ridget.setColumnSortable(0, false);

		verifyPropertyChangeEvents();
		expectPropertyChangeEvents(new PropertyChangeEvent(ridget, ISortableByColumn.PROPERTY_COLUMN_SORTABILITY, null,
				Integer.valueOf(0)));

		ridget.setColumnSortable(0, true);

		verifyPropertyChangeEvents();
		expectNoPropertyChangeEvent();

		ridget.setColumnSortable(0, true);

		verifyPropertyChangeEvents();
	}

	public void testColumnHeaderChangesSortability() {
		final ICompositeTableRidget ridget = getRidget();

		ridget.setColumnSortable(0, true);
		ridget.setComparator(0, new FirstNameComparator());
		ridget.setColumnSortable(1, true);
		ridget.setComparator(1, new FirstNameComparator());

		ridget.setSortedColumn(-1);

		assertEquals(-1, ridget.getSortedColumn());
		assertFalse(ridget.isSortedAscending());

		final Event e = new Event();
		e.type = SWT.Selection;
		e.widget = getColumn((CompositeTableRidget) ridget, 0);
		e.widget.notifyListeners(SWT.Selection, e);

		assertEquals(0, ridget.getSortedColumn());
		assertTrue(ridget.isSortedAscending());

		e.widget.notifyListeners(SWT.Selection, e);

		assertEquals(0, ridget.getSortedColumn());
		assertFalse(ridget.isSortedAscending());

		e.widget.notifyListeners(SWT.Selection, e);

		assertEquals(-1, ridget.getSortedColumn());
		assertFalse(ridget.isSortedAscending());

		e.widget.notifyListeners(SWT.Selection, e);

		assertEquals(0, ridget.getSortedColumn());
		assertTrue(ridget.isSortedAscending());
	}

	public void testBindsNeedsUpdateFromModel() {
		final ICompositeTableRidget ridget = new CompositeTableRidget();

		final IObservableList rowObservables = BeansObservables.observeList(manager, "persons");
		ridget.bindToModel(rowObservables, Person.class, RowRidget.class);

		assertEquals(0, ridget.getOptionCount());

		ridget.updateFromModel();

		assertEquals(rowObservables.size(), ridget.getOptionCount());
	}

	public void testChildRidgetsHaveController() {
		final ICompositeTableRidget ridget = getRidget();
		final CompositeTable control = getWidget();
		ridget.updateFromModel();

		assertTrue(ridget.getOptionCount() > 0);

		final Control aRow = control.getRowControls()[0];
		final IRowRidget rowRidget = (IRowRidget) aRow.getData("rowRidget");

		final ITextRidget txtFirst = rowRidget.getRidget(ITextRidget.class, "first");
		assertEquals(rowRidget, txtFirst.getController());

		final ITextRidget txtLast = (ITextRidget) rowRidget.getRidget("last");
		assertEquals(rowRidget, txtLast.getController());
	}

	public void testAddSelectionListener() {
		final ICompositeTableRidget ridget = getRidget();

		try {
			ridget.addSelectionListener(null);
			fail();
		} catch (final UnsupportedOperationException npe) {
			ok();
		}

		final TestSelectionListener selectionListener = new TestSelectionListener();

		try {
			ridget.addSelectionListener(selectionListener);
			fail();
		} catch (final UnsupportedOperationException npe) {
			ok();
		}
	}

	public void testGetOptionWithSorting() {
		final ICompositeTableRidget ridget = getRidget();

		assertEquals(0, ridget.indexOfOption(person1));
		assertEquals(person1, ridget.getOption(0));

		ridget.setComparator(1, new LastNameComparator());
		ridget.setSortedColumn(1); // sort by last name
		ridget.setSortedAscending(false);

		final int last = ridget.getOptionCount() - 1;
		assertEquals(last, ridget.indexOfOption(person1));
		assertEquals(person1, ridget.getOption(last));

		ridget.setSortedAscending(true);

		assertEquals(0, ridget.indexOfOption(person1));
		assertEquals(person1, ridget.getOption(0));
	}

	public void testSetSelectionWithSorting() {
		final ICompositeTableRidget ridget = getRidget();

		assertEquals(-1, ridget.getSelectionIndex());
		assertTrue(ridget.getSelection().isEmpty());

		ridget.setSelection(0);

		assertEquals(0, ridget.getSelectionIndex());
		assertEquals(person1, ridget.getSelection().get(0));

		ridget.setComparator(1, new LastNameComparator());
		ridget.setSortedColumn(1); // sort by last name
		ridget.setSortedAscending(false);

		final int last = ridget.getOptionCount() - 1;
		assertEquals(last, ridget.getSelectionIndex());
		assertEquals(person1, ridget.getSelection().get(0));

		ridget.setSortedAscending(true);

		assertEquals(0, ridget.getSelectionIndex());
		assertEquals(person1, ridget.getSelection().get(0));
	}

	@Override
	public void testAddClickListener() {

		final ICompositeTableRidget ridget = getRidget();
		try {
			final FTClickListener listener1 = new FTClickListener();
			ridget.addClickListener(listener1);
			fail();
		} catch (final UnsupportedOperationException uoe) {
			ok();
		}

	}

	// helping methods
	//////////////////

	private String getFirstNameFromRow(final CompositeTable control, final int rowIndex) {
		UITestHelper.readAndDispatch(control);
		control.setSelection(0, rowIndex - control.getTopRow());
		UITestHelper.readAndDispatch(control);
		final Control[] rowControls = control.getRowControls();
		if (rowIndex == 0) {
			return ((Row) rowControls[0]).txtFirst.getText();
		} else if (rowIndex == 7) {
			final int index = rowControls.length - 1;
			return ((Row) rowControls[index]).txtFirst.getText();
		}
		throw new IllegalArgumentException("index= " + rowIndex);
	}

	private String getLastNameFromRow(final CompositeTable control, final int rowIndex) {
		UITestHelper.readAndDispatch(control);
		control.setSelection(0, rowIndex - control.getTopRow());
		UITestHelper.readAndDispatch(control);
		final Control[] rowControls = control.getRowControls();
		if (rowIndex == 0) {
			return ((Row) rowControls[0]).txtLast.getText();
		} else if (rowIndex == 7) {
			final int index = rowControls.length - 1;
			return ((Row) rowControls[index]).txtLast.getText();
		}
		throw new IllegalArgumentException("index= " + rowIndex);
	}

	private TableColumn getColumn(final CompositeTableRidget ridget, final int columnIndex) {
		TableColumn result = null;
		final Object table = ReflectionUtils.invokeHidden(ridget, "getHeader", (Object[]) null);
		if (table instanceof AbstractNativeHeader) {
			result = ((AbstractNativeHeader) table).getColumns()[columnIndex];
		}
		return result;
	}

	/**
	 * Not supported.
	 */
	@Override
	protected void clearUIControlRowSelection() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Object getRowValue(final int i) {
		final List<?> rowObservables = ReflectionUtils.invokeHidden(getRidget(), "getRowObservables");
		return rowObservables.get(i);
	}

	@Override
	protected int[] getSelectedRows() {
		final List<?> rowObservables = ReflectionUtils.invokeHidden(getRidget(), "getRowObservables");
		final Object[] elements = getRidget().getMultiSelectionObservable().toArray();
		final int[] result = new int[elements.length];
		for (int i = 0; i < elements.length; i++) {
			final Object element = elements[i];
			result[i] = rowObservables.indexOf(element);
		}
		return result;
	}

	@Override
	protected int getUIControlSelectedRow() {
		final CompositeTable control = getWidget();
		final Point selection = control.getSelection();
		return selection == null || selection.y == -1 ? -1 : selection.y + control.getTopRow();
	}

	@Override
	protected int getUIControlSelectedRowCount() {
		final CompositeTable control = getWidget();
		Point selection = null;
		// workaround: check if we have selection, because 
		// CompositeTable cannot unselect
		if (getRidget().getSelectionIndex() != -1) {
			selection = control.getSelection();
		}
		return selection == null || selection.y == -1 ? 0 : 1;
	}

	@Override
	protected int[] getUIControlSelectedRows() {
		final int index = getUIControlSelectedRow();
		return index == -1 ? new int[0] : new int[] { index };
	}

	@Override
	protected void setUIControlRowSelection(final int[] indices) {
		throw new UnsupportedOperationException("not supported");
	}

	@Override
	protected void setUIControlRowSelectionInterval(final int start, final int end) {
		assertTrue("multiple selection is not supported", start == end);
		final CompositeTable control = getWidget();
		final int row = start - control.getTopRow();
		control.setSelection(0, row);
		UITestHelper.readAndDispatch(control);
	}

	@Override
	protected boolean supportsMulti() {
		return false;
	}

	// helping classes
	//////////////////

	/**
	 * A header control for the CompositeTable.
	 */
	public static class Header extends AbstractNativeHeader {
		public Header(final Composite parent, final int style) {
			super(parent, style);
			setWeights(new int[] { 100, 100 });
			setColumnText(new String[] { "First Name", "LastName" });
		}
	}

	/**
	 * A row control for the CompositeTable.
	 * 
	 * @see RowRidget
	 */
	public static class Row extends Composite implements IComplexComponent {
		private final Text txtFirst;
		private final Text txtLast;

		public Row(final Composite parent, final int style) {
			super(parent, style);
			setLayout(new ResizableGridRowLayout());
			txtFirst = UIControlsFactory.createText(this);
			txtFirst.setText("first");
			txtLast = UIControlsFactory.createText(this);
			txtLast.setText("last");
			final SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
			locator.setBindingProperty(txtFirst, "first");
			locator.setBindingProperty(txtLast, "last");
		}

		public List<Object> getUIControls() {
			return Arrays.asList(new Object[] { txtFirst, txtLast });
		}

		@Override
		public String toString() {
			return txtFirst.getText() + " " + txtLast.getText();
		}
	}

	/**
	 * A row ridget for the CompositeTableRidget.
	 * 
	 * @see Row
	 */
	public static class RowRidget extends AbstractCompositeRidget implements IRowRidget {
		private Person rowData;

		public void setData(final Object rowData) {
			this.rowData = (Person) rowData;
		}

		@Override
		public void configureRidgets() {
			final ITextRidget txtFirst = getRidget("first"); //$NON-NLS-1$
			txtFirst.bindToModel(rowData, Person.PROPERTY_FIRSTNAME);
			txtFirst.updateFromModel();

			final ITextRidget txtLast = getRidget("last"); //$NON-NLS-1$
			txtLast.bindToModel(rowData, Person.PROPERTY_LASTNAME);
			txtLast.updateFromModel();
		}
	}

	/**
	 * Compares Persons by first name.
	 */
	private static final class FirstNameComparator implements Comparator<Object>, Serializable {
		private static final long serialVersionUID = 1L;

		public int compare(final Object o1, final Object o2) {
			final Person p1 = (Person) o1;
			final Person p2 = (Person) o2;
			return p1.getFirstname().compareTo(p2.getFirstname());
		}
	}

	/**
	 * Compares Persons by last name.
	 */
	private static final class LastNameComparator implements Comparator<Object>, Serializable {
		private static final long serialVersionUID = 1L;

		public int compare(final Object o1, final Object o2) {
			final Person p1 = (Person) o1;
			final Person p2 = (Person) o2;
			return p1.getLastname().compareTo(p2.getLastname());
		}
	}

	private static class MyModel {
		private final List<Person> persons = PersonFactory.createPersonList();

		public List<Person> getPersons() {
			return persons;
		}
	}

}
