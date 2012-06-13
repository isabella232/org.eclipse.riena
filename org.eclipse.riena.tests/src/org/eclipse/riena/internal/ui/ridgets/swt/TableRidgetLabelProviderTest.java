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

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.beans.common.WordNode;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.ridgets.IColumnFormatter;
import org.eclipse.riena.ui.ridgets.swt.ColumnFormatter;

/**
 * Tests for the class {@link TableRidgetLabelProvider}.
 */
@UITestCase
public class TableRidgetLabelProviderTest extends TestCase {

	private WordNode elementA;
	private WordNode elementB;
	private Color colorA;
	private Color colorB;
	private Font fontA;
	private Font fontB;
	private IObservableMap[] attrMaps;
	private IColumnFormatter[] formatters;
	private IColumnFormatter[] noFormatters;

	@Override
	protected void setUp() throws Exception {
		final Display display = Display.getDefault();
		final Realm realm = SWTObservables.getRealm(display);
		ReflectionUtils.invokeHidden(realm, "setDefault", realm); //$NON-NLS-1$
		colorA = display.getSystemColor(SWT.COLOR_RED);
		colorB = display.getSystemColor(SWT.COLOR_GREEN);
		fontA = new Font(display, "Arial", 12, SWT.NORMAL); //$NON-NLS-1$
		fontB = new Font(display, "Courier", 12, SWT.NORMAL); //$NON-NLS-1$

		final IObservableSet elements = createElements();
		final String[] columnProperties = { "word", "upperCase" }; //$NON-NLS-1$ //$NON-NLS-2$
		attrMaps = BeansObservables.observeMaps(elements, WordNode.class, columnProperties);
		formatters = new IColumnFormatter[] { null, new TestColumnFormatter() };
		noFormatters = new IColumnFormatter[attrMaps.length];
	}

	@Override
	protected void tearDown() throws Exception {
		fontA.dispose();
		fontB.dispose();
	}

	public void testGetText() {
		final TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, noFormatters);

		assertEquals("Alpha", labelProvider.getText(elementA)); //$NON-NLS-1$
		assertEquals("BRAVO", labelProvider.getText(elementB)); //$NON-NLS-1$
	}

	public void testGetColumnText() {
		TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, noFormatters);

		assertEquals("Alpha", labelProvider.getColumnText(elementA, 0)); //$NON-NLS-1$
		assertEquals("BRAVO", labelProvider.getColumnText(elementB, 0)); //$NON-NLS-1$

		assertEquals("false", labelProvider.getColumnText(elementA, 1)); //$NON-NLS-1$
		assertEquals("true", labelProvider.getColumnText(elementB, 1)); //$NON-NLS-1$

		assertEquals(null, labelProvider.getColumnText(elementA, 99));

		labelProvider.setCheckBoxInFirstColumn(true);
		assertEquals("Alpha", labelProvider.getColumnText(elementA, 0)); //$NON-NLS-1$
		assertEquals("BRAVO", labelProvider.getColumnText(elementB, 0)); //$NON-NLS-1$

		assertEquals("false", labelProvider.getColumnText(elementA, 1)); //$NON-NLS-1$
		assertEquals("true", labelProvider.getColumnText(elementB, 1)); //$NON-NLS-1$

		final IObservableSet elements = createElements();
		final String[] columnProperties = { "upperCase", "word" }; //$NON-NLS-1$ //$NON-NLS-2$
		final IObservableMap[] attrMap = BeansObservables.observeMaps(elements, WordNode.class, columnProperties);
		labelProvider = new TableRidgetLabelProvider(attrMap, new IColumnFormatter[2]);

		labelProvider.setCheckBoxInFirstColumn(false);
		assertEquals("false", labelProvider.getColumnText(elementA, 0)); //$NON-NLS-1$
		assertEquals("Alpha", labelProvider.getColumnText(elementA, 1)); //$NON-NLS-1$

		assertEquals("true", labelProvider.getColumnText(elementB, 0)); //$NON-NLS-1$
		assertEquals("BRAVO", labelProvider.getColumnText(elementB, 1)); //$NON-NLS-1$

		labelProvider.setCheckBoxInFirstColumn(true);
		assertNull(labelProvider.getColumnText(elementA, 0));
		assertEquals("Alpha", labelProvider.getColumnText(elementA, 1)); //$NON-NLS-1$

		assertNull(labelProvider.getColumnText(elementB, 0));
		assertEquals("BRAVO", labelProvider.getColumnText(elementB, 1)); //$NON-NLS-1$

	}

	public void testGetImage() {
		TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, noFormatters);

		assertNull(labelProvider.getImage(elementA));
		assertNull(labelProvider.getImage(elementB));

		final IObservableSet elements = createElements();
		final String[] columnProperties = { "upperCase" }; //$NON-NLS-1$
		final IObservableMap[] attrMap = BeansObservables.observeMaps(elements, WordNode.class, columnProperties);
		labelProvider = new TableRidgetLabelProvider(attrMap, new IColumnFormatter[1]);

		final Image siUnchecked = Activator.getSharedImage(SharedImages.IMG_UNCHECKED);
		assertNotNull(siUnchecked);
		assertEquals(siUnchecked, labelProvider.getImage(elementA));

		final Image siChecked = Activator.getSharedImage(SharedImages.IMG_CHECKED);
		assertNotNull(siChecked);
		assertEquals(siChecked, labelProvider.getImage(elementB));

		labelProvider.setCheckBoxInFirstColumn(true);
		assertNull(labelProvider.getImage(elementB));

		assertNotSame(siChecked, siUnchecked);
	}

	public void testGetColumnImage() {
		TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, noFormatters);

		assertNull(labelProvider.getColumnImage(elementA, 0));
		assertNull(labelProvider.getColumnImage(elementB, 0));

		final Image siUnchecked = Activator.getSharedImage(SharedImages.IMG_UNCHECKED);
		assertNotNull(siUnchecked);
		assertEquals(siUnchecked, labelProvider.getColumnImage(elementA, 1));
		assertTrue(labelProvider.getColumnText(elementA, 1).length() > 0);

		final Image siChecked = Activator.getSharedImage(SharedImages.IMG_CHECKED);
		assertNotNull(siChecked);
		assertEquals(siChecked, labelProvider.getColumnImage(elementB, 1));

		assertNotSame(siChecked, siUnchecked);

		assertEquals(null, labelProvider.getColumnImage(elementA, 99));

		final ColumnFormatter emptyFormatter = new ColumnFormatter() {
			@Override
			public String getText(final Object element) {
				return ""; //$NON-NLS-1$
			};
		};
		final IColumnFormatter[] emptyTextFormatters = new IColumnFormatter[] { emptyFormatter, emptyFormatter };
		labelProvider = new TableRidgetLabelProvider(attrMaps, emptyTextFormatters);
		assertEquals(siUnchecked, labelProvider.getColumnImage(elementA, 1));
		assertEquals("", labelProvider.getColumnText(elementA, 1)); //$NON-NLS-1$

	}

	public void testSetFormatters() {
		final TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, formatters);

		assertEquals("no", labelProvider.getColumnText(elementA, 1)); //$NON-NLS-1$
		assertEquals("yes", labelProvider.getColumnText(elementB, 1)); //$NON-NLS-1$

		final Object arg2 = new IColumnFormatter[] { null, null };
		ReflectionUtils.invokeHidden(labelProvider, "setFormatters", arg2); //$NON-NLS-1$

		assertEquals("false", labelProvider.getColumnText(elementA, 1)); //$NON-NLS-1$
		assertEquals("true", labelProvider.getColumnText(elementB, 1)); //$NON-NLS-1$

		try {
			final Object arg3 = new IColumnFormatter[] { null, null, null };
			ReflectionUtils.invokeHidden(labelProvider, "setFormatters", arg3); //$NON-NLS-1$
			fail();
		} catch (final RuntimeException rex) {
			Nop.reason("ok"); //$NON-NLS-1$
		}
	}

	public void testGetColumnTextWithFormatter() {
		final TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, formatters);

		assertEquals("Alpha", labelProvider.getColumnText(elementA, 0)); //$NON-NLS-1$
		assertEquals("BRAVO", labelProvider.getColumnText(elementB, 0)); //$NON-NLS-1$

		assertEquals("no", labelProvider.getColumnText(elementA, 1)); //$NON-NLS-1$
		assertEquals("yes", labelProvider.getColumnText(elementB, 1)); //$NON-NLS-1$

		assertEquals(null, labelProvider.getColumnText(elementA, 99));
	}

	public void testGetColumnImageWithFormatter() {
		final TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, formatters);

		assertNull(labelProvider.getColumnImage(elementA, 0));
		assertNull(labelProvider.getColumnImage(elementB, 0));

		final Image siCollaped = Activator.getSharedImage(SharedImages.IMG_NODE_COLLAPSED);
		assertNotNull(siCollaped);
		assertEquals(siCollaped, labelProvider.getColumnImage(elementA, 1));

		final Image siExpanded = Activator.getSharedImage(SharedImages.IMG_NODE_EXPANDED);
		assertNotNull(siExpanded);
		assertEquals(siExpanded, labelProvider.getColumnImage(elementB, 1));

		assertNotSame(siExpanded, siCollaped);

		assertEquals(null, labelProvider.getColumnImage(elementA, 99));
	}

	public void testGetForegroundWithFormatter() {
		final TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, formatters);

		assertNull(labelProvider.getForeground(elementA, 0));
		assertNull(labelProvider.getForeground(elementB, 0));

		assertSame(colorA, labelProvider.getForeground(elementA, 1));
		assertSame(colorB, labelProvider.getForeground(elementB, 1));

		assertEquals(null, labelProvider.getForeground(elementA, 99));
	}

	public void testGetBackgroundWithFormatter() {
		final TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, formatters);

		assertNull(labelProvider.getBackground(elementA, 0));
		assertNull(labelProvider.getBackground(elementB, 0));

		assertSame(colorA, labelProvider.getBackground(elementA, 1));
		assertSame(colorB, labelProvider.getBackground(elementB, 1));

		assertEquals(null, labelProvider.getBackground(elementA, 99));
	}

	public void testGetFontWithFormatter() {
		final TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, formatters);

		assertNull(labelProvider.getFont(elementA, 0));
		assertNull(labelProvider.getFont(elementB, 0));

		assertSame(fontA, labelProvider.getFont(elementA, 1));
		assertSame(fontB, labelProvider.getFont(elementB, 1));

		assertEquals(null, labelProvider.getFont(elementA, 99));
	}

	// helping methods
	// ////////////////

	private IObservableSet createElements() {
		final Collection<WordNode> collection = new ArrayList<WordNode>();
		elementA = new WordNode("Alpha"); //$NON-NLS-1$
		elementB = new WordNode("Bravo"); //$NON-NLS-1$
		elementB.setUpperCase(true);
		collection.add(elementA);
		collection.add(elementB);
		final IObservableSet elements = new WritableSet(Realm.getDefault(), collection, WordNode.class);
		return elements;
	}

	private final class TestColumnFormatter extends ColumnFormatter {
		@Override
		public String getText(final Object element) {
			final WordNode wordNode = (WordNode) element;
			return wordNode.isUpperCase() ? "yes" : "no"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Override
		public Image getImage(final Object element) {
			final WordNode wordNode = (WordNode) element;
			final String key = "alpha".equalsIgnoreCase(wordNode.getWord()) ? SharedImages.IMG_NODE_COLLAPSED : SharedImages.IMG_NODE_EXPANDED; //$NON-NLS-1$
			return Activator.getSharedImage(key);
		}

		@Override
		public Color getForeground(final Object element) {
			final WordNode wordNode = (WordNode) element;
			return "alpha".equalsIgnoreCase(wordNode.getWord()) ? colorA : colorB; //$NON-NLS-1$
		}

		@Override
		public Color getBackground(final Object element) {
			final WordNode wordNode = (WordNode) element;
			return "alpha".equalsIgnoreCase(wordNode.getWord()) ? colorA : colorB; //$NON-NLS-1$
		}

		@Override
		public Font getFont(final Object element) {
			final WordNode wordNode = (WordNode) element;
			return "alpha".equalsIgnoreCase(wordNode.getWord()) ? fontA : fontB; //$NON-NLS-1$
		}
	}

}
