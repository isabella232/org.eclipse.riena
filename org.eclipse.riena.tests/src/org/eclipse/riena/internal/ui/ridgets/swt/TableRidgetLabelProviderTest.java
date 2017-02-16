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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.beans.common.WordNode;
import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.ridgets.IColumnFormatter;
import org.eclipse.riena.ui.ridgets.swt.ColumnFormatter;
import org.eclipse.riena.ui.ridgets.swt.TableFormatter;

import junit.framework.TestCase;

/**
 * Tests for the class {@link TableRidgetLabelProvider}.
 */
@UITestCase
public class TableRidgetLabelProviderTest extends TestCase {

	private WordNode elementA;
	private WordNode elementB;
	private Color colorA;
	private Color colorB;
	private Color colorC;
	private Font fontA;
	private Font fontB;
	private Font fontC;
	private Point pointA;
	private Point pointB;
	private Point pointC;
	private int alignmentTop;
	private int alignmentBottom;
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
		colorC = display.getSystemColor(SWT.COLOR_BLUE);
		fontA = new Font(display, "Arial", 12, SWT.NORMAL); //$NON-NLS-1$
		fontB = new Font(display, "Courier", 12, SWT.NORMAL); //$NON-NLS-1$
		fontC = new Font(display, "Courier", 24, SWT.NORMAL); //$NON-NLS-1$
		pointA = new Point(1, 1);
		pointB = new Point(2, 2);
		pointC = new Point(3, 3);
		alignmentTop = SWT.TOP;
		alignmentBottom = SWT.BOTTOM;

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

	public void testGetHorizontalAligmnent() {
		final TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, formatters);
		assertEquals(SWT.TOP, labelProvider.getHorizontalAlignment(elementA, 1));
		assertEquals(SWT.BOTTOM, labelProvider.getHorizontalAlignment(elementB, 1));
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

		final Image siLeaf = Activator.getSharedImage(SharedImages.IMG_LEAF);
		assertNotNull(siLeaf);

		labelProvider.setTableFormatter(new TableFormatter() {
			@Override
			public Object getImage(final Object rowElement, final Object cellElement, final int columnIndex) {
				return columnIndex == 99 ? Activator.getSharedImage(SharedImages.IMG_LEAF) : super.getImage(rowElement, cellElement, columnIndex);
			}
		});

		assertEquals(null, labelProvider.getColumnImage(elementA, 98));
		assertEquals(siLeaf, labelProvider.getColumnImage(elementA, 99));

	}

	public void testGetForegroundWithFormatter() {
		final TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, formatters);

		assertNull(labelProvider.getForeground(elementA, 0));
		assertNull(labelProvider.getForeground(elementB, 0));

		assertSame(colorA, labelProvider.getForeground(elementA, 1));
		assertSame(colorB, labelProvider.getForeground(elementB, 1));

		assertEquals(null, labelProvider.getForeground(elementA, 99));

		labelProvider.setTableFormatter(new TableFormatter() {
			@Override
			public Object getForeground(final Object rowElement, final Object cellElement, final int columnIndex) {
				return columnIndex == 99 ? colorC : super.getForeground(rowElement, cellElement, columnIndex);
			}
		});

		assertEquals(null, labelProvider.getForeground(elementA, 98));
		assertEquals(colorC, labelProvider.getForeground(elementA, 99));

	}

	public void testGetBackgroundWithFormatter() {
		final TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, formatters);

		assertNull(labelProvider.getBackground(elementA, 0));
		assertNull(labelProvider.getBackground(elementB, 0));

		assertSame(colorA, labelProvider.getBackground(elementA, 1));
		assertSame(colorB, labelProvider.getBackground(elementB, 1));

		assertEquals(null, labelProvider.getBackground(elementA, 99));

		labelProvider.setTableFormatter(new TableFormatter() {
			@Override
			public Object getBackground(final Object rowElement, final Object cellElement, final int columnIndex) {
				return columnIndex == 99 ? colorC : super.getBackground(rowElement, cellElement, columnIndex);
			}
		});

		assertEquals(null, labelProvider.getBackground(elementA, 98));
		assertEquals(colorC, labelProvider.getBackground(elementA, 99));

	}

	public void testGetFontWithFormatter() {
		final TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, formatters);

		assertNull(labelProvider.getFont(elementA, 0));
		assertNull(labelProvider.getFont(elementB, 0));

		assertSame(fontA, labelProvider.getFont(elementA, 1));
		assertSame(fontB, labelProvider.getFont(elementB, 1));

		assertEquals(null, labelProvider.getFont(elementA, 99));

		labelProvider.setTableFormatter(new TableFormatter() {
			@Override
			public Object getFont(final Object rowElement, final Object cellElement, final int columnIndex) {
				return columnIndex == 99 ? fontC : super.getFont(rowElement, cellElement, columnIndex);
			}
		});

		assertEquals(null, labelProvider.getFont(elementA, 98));
		assertEquals(fontC, labelProvider.getFont(elementA, 99));

	}

	public void testGetToolTipBackgroundColor() {
		final TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, formatters);

		assertNull(labelProvider.getToolTipBackgroundColor(elementA, 0));
		assertNull(labelProvider.getToolTipBackgroundColor(elementB, 0));

		assertSame(colorC, labelProvider.getToolTipBackgroundColor(elementA, 1));
		assertSame(colorB, labelProvider.getToolTipBackgroundColor(elementB, 1));

		assertEquals(null, labelProvider.getToolTipBackgroundColor(elementA, 99));

		labelProvider.setTableFormatter(new TableFormatter() {
			@Override
			public Object getToolTipBackgroundColor(final Object rowElement, final Object cellElement, final int columnIndex) {
				return columnIndex == 99 ? colorA : super.getToolTipBackgroundColor(rowElement, cellElement, columnIndex);
			}
		});

		assertEquals(null, labelProvider.getToolTipBackgroundColor(elementA, 98));
		assertEquals(colorA, labelProvider.getToolTipBackgroundColor(elementA, 99));

	}

	public void testGetToolTipForegroundColor() {
		final TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, formatters);

		assertNull(labelProvider.getToolTipForegroundColor(elementA, 0));
		assertNull(labelProvider.getToolTipForegroundColor(elementB, 0));

		assertSame(colorC, labelProvider.getToolTipForegroundColor(elementA, 1));
		assertSame(colorB, labelProvider.getToolTipForegroundColor(elementB, 1));

		assertEquals(null, labelProvider.getToolTipForegroundColor(elementA, 99));

		labelProvider.setTableFormatter(new TableFormatter() {
			@Override
			public Object getToolTipForegroundColor(final Object rowElement, final Object cellElement, final int columnIndex) {
				return columnIndex == 99 ? colorA : super.getToolTipForegroundColor(rowElement, cellElement, columnIndex);
			}
		});

		assertEquals(null, labelProvider.getToolTipForegroundColor(elementA, 98));
		assertEquals(colorA, labelProvider.getToolTipForegroundColor(elementA, 99));

	}

	public void testGetToolTipFont() {
		final TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, formatters);

		assertNull(labelProvider.getToolTipFont(elementA, 0));
		assertNull(labelProvider.getToolTipFont(elementB, 0));

		assertSame(fontC, labelProvider.getToolTipFont(elementA, 1));
		assertSame(fontB, labelProvider.getToolTipFont(elementB, 1));

		assertEquals(null, labelProvider.getToolTipFont(elementA, 99));

		labelProvider.setTableFormatter(new TableFormatter() {
			@Override
			public Object getToolTipFont(final Object rowElement, final Object cellElement, final int columnIndex) {
				return columnIndex == 99 ? fontA : super.getToolTipFont(rowElement, cellElement, columnIndex);
			}
		});

		assertEquals(null, labelProvider.getToolTipFont(elementA, 98));
		assertEquals(fontA, labelProvider.getToolTipFont(elementA, 99));

	}

	public void testGetToolTipShift() {
		final TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, formatters);

		assertNull(labelProvider.getToolTipShift(elementA, 0));
		assertNull(labelProvider.getToolTipShift(elementB, 0));

		assertSame(pointA, labelProvider.getToolTipShift(elementA, 1));
		assertSame(pointB, labelProvider.getToolTipShift(elementB, 1));

		assertEquals(null, labelProvider.getToolTipShift(elementA, 99));

		labelProvider.setTableFormatter(new TableFormatter() {
			@Override
			public Object getToolTipShift(final Object rowElement, final Object cellElement, final int columnIndex) {
				return columnIndex == 99 ? pointC : super.getToolTipShift(rowElement, cellElement, columnIndex);
			}
		});

		assertEquals(null, labelProvider.getToolTipShift(elementA, 98));
		assertEquals(pointC, labelProvider.getToolTipShift(elementA, 99));

	}

	public void testGetToolTipTimeDisplayed() {
		final TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, formatters);

		assertEquals(0, labelProvider.getToolTipTimeDisplayed(elementA, 0));
		assertEquals(0, labelProvider.getToolTipTimeDisplayed(elementB, 0));

		assertEquals(4711, labelProvider.getToolTipTimeDisplayed(elementA, 1));
		assertEquals(815, labelProvider.getToolTipTimeDisplayed(elementB, 1));

		assertEquals(0, labelProvider.getToolTipTimeDisplayed(elementA, 99));

		labelProvider.setTableFormatter(new TableFormatter() {
			@Override
			public int getToolTipTimeDisplayed(final Object rowElement, final Object cellElement, final int columnIndex) {
				return columnIndex == 99 ? 123 : super.getToolTipTimeDisplayed(rowElement, cellElement, columnIndex);
			}
		});

		assertEquals(0, labelProvider.getToolTipTimeDisplayed(elementA, 98));
		assertEquals(123, labelProvider.getToolTipTimeDisplayed(elementA, 99));

	}

	public void testGetToolTipDisplayDelayTime() {
		final TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, formatters);

		assertEquals(0, labelProvider.getToolTipDisplayDelayTime(elementA, 0));
		assertEquals(0, labelProvider.getToolTipDisplayDelayTime(elementB, 0));

		assertEquals(11, labelProvider.getToolTipDisplayDelayTime(elementA, 1));
		assertEquals(22, labelProvider.getToolTipDisplayDelayTime(elementB, 1));

		assertEquals(0, labelProvider.getToolTipDisplayDelayTime(elementA, 99));

		labelProvider.setTableFormatter(new TableFormatter() {
			@Override
			public int getToolTipDisplayDelayTime(final Object rowElement, final Object cellElement, final int columnIndex) {
				return columnIndex == 99 ? 33 : super.getToolTipDisplayDelayTime(rowElement, cellElement, columnIndex);
			}
		});

		assertEquals(0, labelProvider.getToolTipDisplayDelayTime(elementA, 98));
		assertEquals(33, labelProvider.getToolTipDisplayDelayTime(elementA, 99));

	}

	public void testGetToolTipStyle() {
		final TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, formatters);

		assertEquals(SWT.SHADOW_NONE, labelProvider.getToolTipStyle(elementA, 0));
		assertEquals(SWT.SHADOW_NONE, labelProvider.getToolTipStyle(elementB, 0));

		assertEquals(SWT.SHADOW_IN, labelProvider.getToolTipStyle(elementA, 1));
		assertEquals(SWT.SHADOW_OUT, labelProvider.getToolTipStyle(elementB, 1));

		assertEquals(SWT.SHADOW_NONE, labelProvider.getToolTipStyle(elementA, 99));

		labelProvider.setTableFormatter(new TableFormatter() {
			@Override
			public int getToolTipStyle(final Object rowElement, final Object cellElement, final int columnIndex) {
				return columnIndex == 99 ? SWT.LEFT : super.getToolTipStyle(rowElement, cellElement, columnIndex);
			}
		});

		assertEquals(SWT.SHADOW_NONE, labelProvider.getToolTipStyle(elementA, 98));
		assertEquals(SWT.LEFT, labelProvider.getToolTipStyle(elementA, 99));

	}

	public void testGetToolTipText() {
		final TableRidgetLabelProvider labelProvider = new TableRidgetLabelProvider(attrMaps, formatters);

		assertEquals(null, labelProvider.getToolTipText(elementA, 0));
		assertEquals(null, labelProvider.getToolTipText(elementB, 0));

		assertEquals("a", labelProvider.getToolTipText(elementA, 1)); //$NON-NLS-1$
		assertEquals("bb", labelProvider.getToolTipText(elementB, 1)); //$NON-NLS-1$

		assertEquals(null, labelProvider.getToolTipText(elementA, 99));

		labelProvider.setTableFormatter(new TableFormatter() {
			@Override
			public String getToolTip(final Object rowElement, final Object cellElement, final int columnIndex) {
				return columnIndex == 99 ? "cCc" : super.getToolTip(rowElement, cellElement, columnIndex); //$NON-NLS-1$
			}
		});

		assertEquals(null, labelProvider.getToolTipText(elementA, 98));
		assertEquals("cCc", labelProvider.getToolTipText(elementA, 99)); //$NON-NLS-1$

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

		@Override
		public String getToolTip(final Object element) {
			final WordNode wordNode = (WordNode) element;
			return "alpha".equalsIgnoreCase(wordNode.getWord()) ? "a" : "bb"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		@Override
		public int getToolTipDisplayDelayTime(final Object element) {
			final WordNode wordNode = (WordNode) element;
			return "alpha".equalsIgnoreCase(wordNode.getWord()) ? 11 : 22; //$NON-NLS-1$
		}

		@Override
		public Point getToolTipShift(final Object element) {
			final WordNode wordNode = (WordNode) element;
			return "alpha".equalsIgnoreCase(wordNode.getWord()) ? pointA : pointB; //$NON-NLS-1$
		}

		@Override
		public int getToolTipStyle(final Object element) {
			final WordNode wordNode = (WordNode) element;
			return "alpha".equalsIgnoreCase(wordNode.getWord()) ? SWT.SHADOW_IN : SWT.SHADOW_OUT; //$NON-NLS-1$
		}

		@Override
		public Color getToolTipBackgroundColor(final Object element) {
			final WordNode wordNode = (WordNode) element;
			return "alpha".equalsIgnoreCase(wordNode.getWord()) ? colorC : colorB; //$NON-NLS-1$
		}

		@Override
		public Object getToolTipFont(final Object element) {
			final WordNode wordNode = (WordNode) element;
			return "alpha".equalsIgnoreCase(wordNode.getWord()) ? fontC : fontB; //$NON-NLS-1$
		}

		@Override
		public Color getToolTipForegroundColor(final Object element) {
			final WordNode wordNode = (WordNode) element;
			return "alpha".equalsIgnoreCase(wordNode.getWord()) ? colorC : colorB; //$NON-NLS-1$
		}

		@Override
		public int getToolTipTimeDisplayed(final Object element) {
			final WordNode wordNode = (WordNode) element;
			return "alpha".equalsIgnoreCase(wordNode.getWord()) ? 4711 : 815; //$NON-NLS-1$
		}

		@Override
		public int getHorizontalAlignment(final Object element) {
			final WordNode wordNode = (WordNode) element;
			return "alpha".equalsIgnoreCase(wordNode.getWord()) ? alignmentTop : alignmentBottom; //$NON-NLS-1$
		}

	}

}
