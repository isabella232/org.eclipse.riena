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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.TypedComparator;
import org.eclipse.riena.beans.common.WordNode;
import org.eclipse.riena.internal.ui.swt.test.UITestHelper;
import org.eclipse.riena.internal.ui.swt.utils.TestUtils;
import org.eclipse.riena.ui.common.ISortableByColumn;
import org.eclipse.riena.ui.ridgets.IColumnFormatter;
import org.eclipse.riena.ui.ridgets.IGroupedTreeTableRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget.SelectionType;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.ITreeTableRidget;
import org.eclipse.riena.ui.ridgets.listener.SelectionEvent;
import org.eclipse.riena.ui.ridgets.swt.ColumnFormatter;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.tree2.ITreeNode;
import org.eclipse.riena.ui.ridgets.tree2.TreeNode;

/**
 * Tests of the class {@link TreeTableRidget}.
 */
public class TreeTableRidgetTest extends AbstractSWTRidgetTest {

	private PersonNode[] roots;
	private PersonNode node1;
	private PersonNode node2;
	private PersonNode node3;
	private PersonNode node4;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		roots = initializeTreeModel();
		final String[] valueAccessors = new String[] { "firstname", "lastname" };
		final String[] columnHeaders = new String[] { "First Name", "Last Name" };
		getRidget().bindToModel(roots, PersonNode.class, "children", "parent", valueAccessors, columnHeaders);
	}

	@Override
	protected Control createWidget(final Composite parent) {
		final Tree result = new Tree(parent, SWT.MULTI);
		result.setHeaderVisible(true);
		new TreeColumn(result, SWT.NONE);
		new TreeColumn(result, SWT.NONE);
		return result;
	}

	@Override
	protected ITreeTableRidget createRidget() {
		return new TreeTableRidget();
	}

	@Override
	protected Tree getWidget() {
		return (Tree) super.getWidget();
	}

	@Override
	protected TreeTableRidget getRidget() {
		return (TreeTableRidget) super.getRidget();
	}

	// test methods
	// /////////////

	public void testRidgetMapping() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(TreeTableRidget.class, mapper.getRidgetClass(getWidget()));
	}

	public void testBindToModel() {
		final TreeTableRidget ridget = getRidget();
		final Tree control = getWidget();

		ridget.expandAll();

		assertEquals(9, TreeUtils.getItemCount(control));
		assertEquals(node1.getFirstname(), getUIControlItem(0).getText(0));
		assertEquals(node2.getFirstname(), getUIControlItem(1).getText(0));
		assertEquals(node3.getFirstname(), getUIControlItem(2).getText(0));
		assertEquals(node4.getFirstname(), getUIControlItem(3).getText(0));

		assertEquals(node1.getLastname(), getUIControlItem(0).getText(1));
		assertEquals(node2.getLastname(), getUIControlItem(1).getText(1));
		assertEquals(node3.getLastname(), getUIControlItem(2).getText(1));
		assertEquals(node4.getLastname(), getUIControlItem(3).getText(1));
	}

	public void testTableColumnsNumAndHeader() {
		final Tree control = getWidget();

		final TreeColumn[] columns = control.getColumns();
		assertEquals(2, columns.length);
		assertEquals("First Name", columns[0].getText());
		assertEquals("Last Name", columns[1].getText());
		assertTrue(control.getHeaderVisible());
	}

	public void testTableColumnsNumAndHeaderWithMismatch() {
		final String[] properties1 = new String[] { "firstname", "lastname" };
		final String[] headers1 = new String[] { "First Name" };

		try {
			getRidget().bindToModel(roots, PersonNode.class, "children", "parent", properties1, headers1);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
	}

	public void testTableColumnsWithNullHeader() {
		final Tree control = getWidget();

		control.setHeaderVisible(true);
		control.getColumn(0).setText("foo");
		control.getColumn(1).setText("bar");

		final String[] properties1 = new String[] { "firstname", "lastname" };
		// null should hide the headers
		getRidget().bindToModel(roots, PersonNode.class, "children", "parent", properties1, null);

		assertFalse(control.getHeaderVisible());
	}

	public void testTableColumnsWithNullHeaderEntry() {
		final Tree control = getWidget();

		control.getColumn(0).setText("foo");
		control.getColumn(1).setText("bar");

		final String[] properties1 = new String[] { "firstname", "lastname" };
		final String[] headers = new String[] { "First Name", null };
		getRidget().bindToModel(roots, PersonNode.class, "children", "parent", properties1, headers);

		assertEquals("First Name", control.getColumn(0).getText());
		assertEquals("", control.getColumn(1).getText());
	}

	public void testSetGroupingEnabled() {
		final TreeTableRidget ridget = getRidget();

		assertFalse(ridget.isGroupingEnabled());

		ridget.setGroupingEnabled(true);

		assertTrue(ridget.isGroupingEnabled());

		ridget.setGroupingEnabled(false);

		assertFalse(ridget.isGroupingEnabled());
	}

	public void testSetGroupingEnabledFiresEvents() {
		final TreeTableRidget ridget = getRidget();

		expectPropertyChangeEvent(IGroupedTreeTableRidget.PROPERTY_GROUPING_ENABLED, Boolean.FALSE, Boolean.TRUE);
		ridget.setGroupingEnabled(true);
		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();
		ridget.setGroupingEnabled(true);
		verifyPropertyChangeEvents();

		expectPropertyChangeEvent(IGroupedTreeTableRidget.PROPERTY_GROUPING_ENABLED, Boolean.TRUE, Boolean.FALSE);
		ridget.setGroupingEnabled(false);
		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();
		ridget.setGroupingEnabled(false);
		verifyPropertyChangeEvents();
	}

	public void testSetComparator() {
		final TreeTableRidget ridget = getRidget();
		final Tree control = getWidget();

		// sorts from a to z
		final Comparator<Object> comparator = new TypedComparator<String>();

		try {
			ridget.setComparator(-1, comparator);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		try {
			ridget.setComparator(2, comparator);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		ridget.setSortedAscending(true);

		assertEquals("Benjamin", control.getItem(0).getText(0));
		assertEquals("Zebra", control.getItem(1).getText(0));
		assertEquals("Adam", control.getItem(2).getText(0));

		ridget.setComparator(0, comparator);

		assertEquals("Benjamin", control.getItem(0).getText(0));
		assertEquals("Zebra", control.getItem(1).getText(0));
		assertEquals("Adam", control.getItem(2).getText(0));

		ridget.setSortedColumn(0);

		assertEquals("Adam", control.getItem(0).getText(0));
		assertEquals("Benjamin", control.getItem(1).getText(0));
		assertEquals("Zebra", control.getItem(2).getText(0));

		ridget.setComparator(0, null);

		assertEquals("Benjamin", control.getItem(0).getText(0));
		assertEquals("Zebra", control.getItem(1).getText(0));
		assertEquals("Adam", control.getItem(2).getText(0));
	}

	public void testGetSortedColumn() {
		final TreeTableRidget ridget = getRidget();

		try {
			ridget.setSortedColumn(2);
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

		ridget.setComparator(1, new TypedComparator<String>());
		ridget.setSortedColumn(1);

		assertEquals(1, ridget.getSortedColumn());

		ridget.setSortedColumn(-1);

		assertEquals(-1, ridget.getSortedColumn());

		// no comparator in column 0
		ridget.setSortedColumn(0);

		assertEquals(-1, ridget.getSortedColumn());
	}

	public void testIsColumnSortable() {
		final TreeTableRidget ridget = getRidget();

		try {
			assertFalse(ridget.isColumnSortable(-1));
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		try {
			assertFalse(ridget.isColumnSortable(2));
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

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
		final Tree control = getWidget();
		final TreeTableRidget ridget = getRidget();

		ridget.bindToModel(roots, PersonNode.class, "children", "parent", new String[] { "firstname", "lastname" },
				null);

		assertEquals(-1, ridget.getSortedColumn());
		assertFalse(ridget.isSortedAscending());

		ridget.setComparator(0, new TypedComparator<String>());
		ridget.setSortedColumn(0);

		assertTrue(ridget.isSortedAscending());
		assertEquals("Adam", control.getItem(0).getText(0));
		assertEquals("Benjamin", control.getItem(1).getText(0));
		assertEquals("Zebra", control.getItem(2).getText(0));

		ridget.setSortedAscending(false);

		assertFalse(ridget.isSortedAscending());
		assertEquals("Zebra", control.getItem(0).getText(0));
		assertEquals("Benjamin", control.getItem(1).getText(0));
		assertEquals("Adam", control.getItem(2).getText(0));

		ridget.setSortedAscending(true);

		assertTrue(ridget.isSortedAscending());
		assertEquals("Adam", control.getItem(0).getText(0));
		assertEquals("Benjamin", control.getItem(1).getText(0));
		assertEquals("Zebra", control.getItem(2).getText(0));

		ridget.setComparator(0, null);

		assertEquals(-1, ridget.getSortedColumn());
		assertFalse(ridget.isSortedAscending());
	}

	public void testSetSortedAscendingFiresEvents() {
		final TreeTableRidget ridget = getRidget();

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
		final TreeTableRidget ridget = getRidget();

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
		final TreeTableRidget ridget = getRidget();

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
		final TreeTableRidget ridget = getRidget();
		final Tree tree = getWidget();

		ridget.setColumnSortable(0, true);
		ridget.setComparator(0, new TypedComparator<String>());
		ridget.setColumnSortable(1, true);
		ridget.setComparator(1, new TypedComparator<String>());

		ridget.setSortedColumn(-1);

		assertEquals(-1, ridget.getSortedColumn());
		assertFalse(ridget.isSortedAscending());

		final Event e = new Event();
		e.type = SWT.Selection;
		e.widget = tree.getColumn(0);
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

	public void testSortColumnTwo() {
		final ITreeTableRidget ridget = getRidget();
		final Tree control = getWidget();

		final WordNode root = new WordNode("root");
		new WordNode(root, "ZA");
		new WordNode(root, "AAA");
		new WordNode(root, "BCAA");
		ridget.bindToModel(new Object[] { root }, WordNode.class, "children", "parent",
				new String[] { "word", "ACount" }, null);
		ridget.setComparator(0, new TypedComparator<String>());
		ridget.setComparator(1, new TypedComparator<Integer>());
		ridget.expandAll();

		assertEquals("ZA", control.getItem(0).getItem(0).getText());
		assertEquals("AAA", control.getItem(0).getItem(1).getText());
		assertEquals("BCAA", control.getItem(0).getItem(2).getText());

		ridget.setSortedColumn(0);
		ridget.setSortedAscending(true);

		assertEquals("AAA", control.getItem(0).getItem(0).getText());
		assertEquals("BCAA", control.getItem(0).getItem(1).getText());
		assertEquals("ZA", control.getItem(0).getItem(2).getText());
		//TreeUtils.print((Tree) ridget.getUIControl());

		ridget.setSortedColumn(1);

		assertEquals("ZA", control.getItem(0).getItem(0).getText());
		assertEquals("BCAA", control.getItem(0).getItem(1).getText());
		assertEquals("AAA", control.getItem(0).getItem(2).getText());
	}

	/**
	 * Tests that for single selection, the ridget selection state and the ui
	 * selection state cannot be changed by the user when ridget is set to
	 * "output only".
	 */
	public void testOutputSingleSelectionCannotBeChangedFromUI() {
		final ITreeTableRidget ridget = getRidget();
		final Tree control = getWidget();

		ridget.setSelectionType(SelectionType.SINGLE);

		assertEquals(0, ridget.getSelection().size());
		assertEquals(0, control.getSelectionCount());

		ridget.setOutputOnly(true);
		control.setFocus();
		// move down and up to select row 0; space does not select in tables
		UITestHelper.sendKeyAction(control.getDisplay(), UITestHelper.KC_ARROW_DOWN);
		UITestHelper.sendKeyAction(control.getDisplay(), UITestHelper.KC_ARROW_UP);

		assertEquals(0, ridget.getSelection().size());
		assertEquals(0, control.getSelectionCount());

		ridget.setOutputOnly(false);
		control.setFocus();
		// move down and up to select row 0; space does not select in tables
		UITestHelper.sendKeyAction(control.getDisplay(), UITestHelper.KC_ARROW_DOWN);
		UITestHelper.sendKeyAction(control.getDisplay(), UITestHelper.KC_ARROW_UP);

		assertEquals(1, ridget.getSelection().size());
		assertEquals(1, control.getSelectionCount());
	}

	/**
	 * Tests that for multiple selection, the ridget selection state and the ui
	 * selection state cannot be changed by the user when ridget is set to
	 * "output only".
	 */
	public void testOutputMultipleSelectionCannotBeChangedFromUI() {
		final ITreeTableRidget ridget = getRidget();
		final Tree control = getWidget();

		ridget.setSelectionType(SelectionType.MULTI);

		assertEquals(0, ridget.getSelection().size());
		assertEquals(0, control.getSelectionCount());

		ridget.setOutputOnly(true);
		control.setFocus();
		// move down and up to select row 0; space does not select in tables
		UITestHelper.sendKeyAction(control.getDisplay(), UITestHelper.KC_ARROW_DOWN);
		UITestHelper.sendKeyAction(control.getDisplay(), UITestHelper.KC_ARROW_UP);

		assertEquals(0, ridget.getSelection().size());
		assertEquals(0, control.getSelectionCount());

		ridget.setOutputOnly(false);
		control.setFocus();
		// move down and up to select row 0; space does not select in tables
		UITestHelper.sendKeyAction(control.getDisplay(), UITestHelper.KC_ARROW_DOWN);
		UITestHelper.sendKeyAction(control.getDisplay(), UITestHelper.KC_ARROW_UP);

		assertEquals(1, ridget.getSelection().size());
		assertEquals(1, control.getSelectionCount());
	}

	/**
	 * Tests that toggling output state on/off does not change the selection.
	 */
	public void testTogglingOutputDoesNotChangeSelection() {
		final ITreeTableRidget ridget = getRidget();

		ridget.setSelection(node1);

		assertEquals(1, ridget.getSelection().size());

		ridget.setOutputOnly(true);

		assertEquals(1, ridget.getSelection().size());

		ridget.setSelection((Object) null);

		assertEquals(0, ridget.getSelection().size());

		ridget.setOutputOnly(false);

		assertEquals(0, ridget.getSelection().size());
	}

	public void testSetColumnFormatter() {
		final ITreeTableRidget ridget = getRidget();
		final Tree tree = getWidget();
		final IColumnFormatter formatter = new ColumnFormatter() {
			@Override
			public String getText(final Object element) {
				final PersonNode node = (PersonNode) element;
				return node.getLastname().toUpperCase();
			}
		};
		final String lastName = node1.getLastname();
		final String lastNameUpperCase = lastName.toUpperCase();

		try {
			ridget.setColumnFormatter(-1, formatter);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		try {
			ridget.setColumnFormatter(99, formatter);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		ridget.setColumnFormatter(1, formatter);

		assertEquals(lastName, tree.getItem(0).getText(1));

		ridget.updateFromModel();

		assertEquals(lastNameUpperCase, tree.getItem(0).getText(1));

		ridget.setColumnFormatter(1, null);

		assertEquals(lastNameUpperCase, tree.getItem(0).getText(1));

		ridget.updateFromModel();

		assertEquals(lastName, tree.getItem(0).getText(1));
	}

	public void testAddSelectionListener() {
		final TreeTableRidget ridget = getRidget();
		final Tree control = getWidget();

		try {
			ridget.addSelectionListener(null);
			fail();
		} catch (final RuntimeException npe) {
			ok();
		}

		final TestSelectionListener selectionListener = new TestSelectionListener();
		ridget.addSelectionListener(selectionListener);

		ridget.setSelection(node1);
		assertEquals(1, selectionListener.getCount());
		ridget.removeSelectionListener(selectionListener);
		ridget.setSelection(node2);
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

	/**
	 * As per Bug 295305
	 */
	public void testAutoCreateTableColumn() {
		final ITreeTableRidget ridget = createRidget();
		final Tree control = new Tree(getShell(), SWT.FULL_SELECTION | SWT.SINGLE);
		ridget.setUIControl(control);

		assertEquals(0, control.getColumnCount());

		final String[] columns3 = { "firstname", "lastname", "entry" };
		ridget.bindToModel(roots, PersonNode.class, "children", "parent", columns3, null);

		assertEquals(3, control.getColumnCount());

		final String[] columns1 = { "firstname" };
		ridget.bindToModel(roots, PersonNode.class, "children", "parent", columns1, null);

		assertEquals(1, control.getColumnCount());
	}

	/**
	 * As per Bug 295305
	 */
	public void testAutoCreateColumnsWithNoLayout() {
		final ITreeTableRidget ridget = createRidget();
		final Tree control = new Tree(getShell(), SWT.FULL_SELECTION | SWT.SINGLE);
		ridget.setUIControl(control);

		getShell().setLayout(null);
		control.setSize(300, 100);
		final String[] columns3 = { "firstname", "lastname", "entry" };
		ridget.bindToModel(roots, PersonNode.class, "children", "parent", columns3, null);

		assertEquals(null, control.getParent().getLayout());
		assertEquals(null, control.getLayout());
		for (int i = 0; i < 3; i++) {
			assertEquals("col #" + i, 100, control.getColumn(i).getWidth());
		}
	}

	/**
	 * As per Bug 295305
	 */
	public void testAutoCreateColumnsWithTableLayout() {
		final ITreeTableRidget ridget = createRidget();
		final Tree control = new Tree(getShell(), SWT.FULL_SELECTION | SWT.SINGLE);
		control.setLayout(new TableLayout());
		ridget.setUIControl(control);

		final String[] columns3 = { "firstname", "lastname", "entry" };
		ridget.bindToModel(roots, PersonNode.class, "children", "parent", columns3, null);

		final Class<?> shellLayout = getShell().getLayout().getClass();
		assertSame(shellLayout, control.getParent().getLayout().getClass());
		assertTrue(control.getLayout() instanceof TableLayout);
		TestUtils.assertColumnWidths(control, 3);
	}

	/**
	 * As per Bug 295305
	 */
	public void testAutoCreateColumnsWithTreeColumnLayout() {
		final ITreeTableRidget ridget = createRidget();
		for (final Control control : getShell().getChildren()) {
			control.dispose();
		}
		final Tree control = new Tree(getShell(), SWT.FULL_SELECTION | SWT.SINGLE);
		ridget.setUIControl(control);
		getShell().setLayout(new TreeColumnLayout());

		final String[] columns3 = { "firstname", "lastname", "entry" };
		ridget.bindToModel(roots, PersonNode.class, "children", "parent", columns3, null);

		assertTrue(control.getParent().getLayout() instanceof TreeColumnLayout);
		assertEquals(null, control.getLayout());
		final int expected = control.getSize().x / 3;

		assertTrue(String.valueOf(expected), expected > 0);
		for (int i = 0; i < 3; i++) {
			assertEquals("col #" + i, expected, control.getColumn(i).getWidth());
		}
	}

	/**
	 * As per Bug 295305
	 */
	public void testSetColumnWidths() {
		final ITreeTableRidget ridget = createRidget();
		final Tree control = new Tree(getShell(), SWT.FULL_SELECTION | SWT.SINGLE);
		ridget.setUIControl(control);

		try {
			ridget.setColumnWidths(new Object[] { null });
			fail();
		} catch (final RuntimeException rex) {
			assertTrue(rex.getMessage().contains("null"));
		}

		try {
			ridget.setColumnWidths(new Object[] { new Object() });
			fail();
		} catch (final RuntimeException rex) {
			assertTrue(rex.getMessage().contains("Object"));
		}

		ridget.setColumnWidths(new Object[] { new ColumnPixelData(20), new ColumnPixelData(40), new ColumnPixelData(60) });
		final String[] columns3 = { "firstname", "lastname", "entry" };
		ridget.bindToModel(roots, PersonNode.class, "children", "parent", columns3, null);

		final int[] expected = { 20, 40, 60 };
		for (int i = 0; i < 3; i++) {
			final int actual = control.getColumn(i).getWidth();
			final String msg = String.format("col #%d, exp:%d, act:%d", i, expected[i], actual);
			assertEquals(msg, expected[i], actual);
		}
	}

	/**
	 * As per Bug 295305
	 */
	public void testPreserveColumnWidths() {
		final int[] widths = { 50, 100, 150 };
		final ITreeTableRidget ridget = createRidget();
		final Tree control = new Tree(getShell(), SWT.FULL_SELECTION | SWT.SINGLE);
		for (final int width : widths) {
			final TreeColumn column = new TreeColumn(control, SWT.NONE);
			column.setWidth(width);
		}
		ridget.setUIControl(control);

		final String[] columns3 = { "firstname", "lastname", "entry" };
		ridget.bindToModel(roots, PersonNode.class, "children", "parent", columns3, null);
		ridget.updateFromModel();

		for (int i = 0; i < 3; i++) {
			final int actual = control.getColumn(i).getWidth();
			final String msg = String.format("col #%d, exp:%d, act:%d", i, widths[i], actual);
			assertEquals(msg, widths[i], actual);
		}
	}

	/**
	 * As per Bug 296639
	 */
	public void testSetSelectionWithNoBoundControl() {
		final ITreeRidget ridget = getRidget();

		ridget.setSelection(node2);

		assertNotNull(ridget.getUIControl());
		assertEquals(node2, ridget.getSelection().get(0));

		ridget.setUIControl(null);
		ridget.setSelection(node4);

		assertNull(ridget.getUIControl());
		assertEquals(node4, ridget.getSelection().get(0));
	}

	/**
	 * As per Bug 298033 comment #1
	 */
	public void testUpdateSingleSelectionFromModelWithNoBoundControl() {
		final ITreeRidget ridget = getRidget();

		ridget.setSelectionType(SelectionType.SINGLE);
		ridget.setSelection(node2);

		assertEquals(node2, ridget.getSelection().get(0));

		// remove selected element while not bound to a control
		ridget.setUIControl(null);
		removeNode(node1, node2);
		ridget.updateFromModel();

		assertNull(ridget.getUIControl());
		assertTrue(ridget.getSelection().isEmpty());
	}

	/**
	 * As per Bug 298033 comment #1
	 */
	public void testUpdateMultiSelectionFromModelWithNoBoundControl() {
		final ITreeRidget ridget = getRidget();

		ridget.setSelectionType(SelectionType.MULTI);
		ridget.setSelection(Arrays.asList(node2, node4));

		assertEquals(2, ridget.getSelection().size());
		assertEquals(node2, ridget.getSelection().get(0));
		assertEquals(node4, ridget.getSelection().get(1));

		// remove selected element while not bound to a control
		ridget.setUIControl(null);
		removeNode(node1, node2);
		ridget.updateFromModel();

		assertNull(ridget.getUIControl());
		assertEquals(1, ridget.getSelection().size());
		assertEquals(node4, ridget.getSelection().get(0));
	}

	/**
	 * As per Bug 298033, 304733
	 */
	public void testUpdateSingleSelectionFromModelWithBoundControl() {
		final ITreeRidget ridget = getRidget();

		ridget.setSelectionType(SelectionType.SINGLE);
		ridget.setSelection(node2);

		assertEquals(node2, ridget.getSelection().get(0));

		// remove selected element while bound to a control
		removeNode(node1, node2);
		ridget.updateFromModel();

		assertNotNull(ridget.getUIControl());
		assertTrue(ridget.getSelection().isEmpty());
	}

	/**
	 * As per Bug 298033, 304733
	 */
	public void testUpdateMultiSelectionFromModelWithBoundControl() {
		final ITreeRidget ridget = getRidget();

		ridget.setSelectionType(SelectionType.MULTI);
		ridget.setSelection(Arrays.asList(node2, node4));

		assertEquals(2, ridget.getSelection().size());
		assertEquals(node2, ridget.getSelection().get(0));
		assertEquals(node4, ridget.getSelection().get(1));

		// remove selected element while bound to a control
		removeNode(node1, node2);
		ridget.updateFromModel();

		assertNotNull(ridget.getUIControl());
		assertEquals(1, ridget.getSelection().size());
		assertEquals(node4, ridget.getSelection().get(0));
	}

	// helping methods
	// ////////////////

	private PersonNode[] initializeTreeModel() {
		// root nodes
		final PersonNode root1 = new PersonNode(new Person("Root", "Benjamin"));
		node1 = root1;
		final PersonNode root2 = new PersonNode(new Person("Root", "Zebra"));
		final PersonNode root3 = new PersonNode(new Person("Root", "Adam"));
		// children beaneath node1
		node2 = new PersonNode(node1, new Person("Benjason", "Elen"));
		node4 = new PersonNode(node1, new Person("Benjason", "Zora"));
		new PersonNode(node1, new Person("Benjason", "Anna"));
		// children beneath node2
		node3 = new PersonNode(node2, new Person("Zebrason", "Ben"));
		new PersonNode(node2, new Person("Zebrason", "Zulu"));
		new PersonNode(node2, new Person("Zebrason", "Arno"));

		return new PersonNode[] { root1, root2, root3 };
	}

	/**
	 * Return the TreeItem corresponding to the following mock "index" scheme:
	 * 0: item for node1, 1: item for node2, 2: item for node3, 3: item for
	 * node4.
	 * <p>
	 * This method will fully expand the tree to ensure all tree items are
	 * created.
	 */
	private TreeItem getUIControlItem(final int index) {
		getRidget().expandAll();
		final Tree control = getWidget();
		switch (index) {
		case 0:
			return control.getItem(0);
		case 1:
			return control.getItem(0).getItem(0);
		case 2:
			return control.getItem(0).getItem(0).getItem(0);
		case 3:
			return control.getItem(0).getItem(1);
		default:
			throw new IndexOutOfBoundsException("index= " + index);
		}
	}

	private void removeNode(final ITreeNode parent, final ITreeNode toRemove) {
		final List<ITreeNode> children = parent.getChildren();
		final boolean removed = children.remove(toRemove);
		assertTrue("failed to remove " + toRemove, removed);
		parent.setChildren(children);
	}

	// helping classes
	// ////////////////

	/**
	 * Wraps a {@link Person} to make it compatible with the
	 * {@link ITreeTableRidget}.
	 */
	private static final class PersonNode extends TreeNode {

		public PersonNode(final Person value) {
			super(value);
		}

		public PersonNode(final PersonNode parent, final Person value) {
			super(parent, value);
		}

		public String getFirstname() {
			return ((Person) getValue()).getFirstname();
		}

		public String getLastname() {
			return ((Person) getValue()).getLastname();
		}

		@SuppressWarnings("unused")
		public String getEntry() {
			// 'Last Name' - 'First Name'
			return ((Person) getValue()).getListEntry();
		}

	}

}
