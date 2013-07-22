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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Tests of the class {@link TableRidgetEditingSupport}.
 */
@UITestCase
public class TableRidgetEditingSupportTest extends TestCase {

	private Shell shell;
	private TableRidget ridget;
	private Table table;

	@Override
	protected void setUp() throws Exception {

		final Display display = Display.getDefault();
		final Realm realm = SWTObservables.getRealm(display);
		assertNotNull(realm);
		ReflectionUtils.invokeHidden(realm, "setDefault", realm); //$NON-NLS-1$

		shell = new Shell(SWT.SYSTEM_MODAL | SWT.ON_TOP);
		table = new Table(shell, SWT.DEFAULT);
		ridget = new TableRidget();
		ridget.setUIControl(table);
		final List<PropHolder> rows = new ArrayList<PropHolder>();
		final PropHolder[] values = new PropHolder[] { new PropHolder(), new PropHolder() };
		for (final PropHolder value : values) {
			rows.add(value);
		}
		final String[] propNames = new String[] { "booleanValue", "intValue" };
		ridget.bindToModel(new WritableList(rows, PropHolder.class), PropHolder.class, propNames, propNames);

	}

	@Override
	protected void tearDown() throws Exception {
		ridget = null;
		shell.dispose();
		shell = null;

		super.tearDown();
	}

	/**
	 * Tests the <i>private</i> method {@code getAlignment(int)}.
	 */
	public void testGetAlignment() {

		final TableRidgetEditingSupport support = new TableRidgetEditingSupport(ridget, null, SWT.DEFAULT);

		int style = 0;
		int ret = ReflectionUtils.invokeHidden(support, "getAlignment", style); //$NON-NLS-1$
		assertEquals(SWT.DEFAULT, ret);

		style = SWT.SHORT;
		ret = ReflectionUtils.invokeHidden(support, "getAlignment", style); //$NON-NLS-1$
		assertEquals(SWT.DEFAULT, ret);

		style = SWT.LEFT;
		ret = ReflectionUtils.invokeHidden(support, "getAlignment", style); //$NON-NLS-1$
		assertEquals(SWT.LEFT, ret);

		style = SWT.LEFT | SWT.SHORT;
		ret = ReflectionUtils.invokeHidden(support, "getAlignment", style); //$NON-NLS-1$
		assertEquals(SWT.LEFT, ret);

		style = SWT.RIGHT;
		ret = ReflectionUtils.invokeHidden(support, "getAlignment", style); //$NON-NLS-1$
		assertEquals(SWT.RIGHT, ret);

		style = SWT.RIGHT | SWT.MEDIUM;
		ret = ReflectionUtils.invokeHidden(support, "getAlignment", style); //$NON-NLS-1$
		assertEquals(SWT.RIGHT, ret);

		style = SWT.CENTER;
		ret = ReflectionUtils.invokeHidden(support, "getAlignment", style); //$NON-NLS-1$
		assertEquals(SWT.CENTER, ret);

		style = SWT.CENTER | SWT.SHORT;
		ret = ReflectionUtils.invokeHidden(support, "getAlignment", style); //$NON-NLS-1$
		assertEquals(SWT.CENTER, ret);

		style = SWT.LEFT | SWT.RIGHT;
		ret = ReflectionUtils.invokeHidden(support, "getAlignment", style); //$NON-NLS-1$
		assertEquals(SWT.LEFT, ret);

		style = SWT.RIGHT | SWT.CENTER;
		ret = ReflectionUtils.invokeHidden(support, "getAlignment", style); //$NON-NLS-1$
		assertEquals(SWT.RIGHT, ret);

		style = SWT.LEFT | SWT.RIGHT | SWT.CENTER;
		ret = ReflectionUtils.invokeHidden(support, "getAlignment", style); //$NON-NLS-1$
		assertEquals(SWT.LEFT, ret);

	}

	/**
	 * Tests the <i>private</i> method {@code createCellEditort}.
	 * 
	 * @throws IntrospectionException
	 */
	public void testCreateCellEditort() throws IntrospectionException {

		final TableRidgetEditingSupport support = new TableRidgetEditingSupport(ridget, null, SWT.DEFAULT);

		PropertyDescriptor property = null;
		CellEditor editor = ReflectionUtils.invokeHidden(support, "createCellEditort", property, SWT.LEFT); //$NON-NLS-1$
		assertNull(editor);

		property = new PropertyDescriptor("booleanValue", PropHolder.class); //$NON-NLS-1$
		editor = ReflectionUtils.invokeHidden(support, "createCellEditort", property, SWT.LEFT); //$NON-NLS-1$
		assertNull(editor);

		property = new PropertyDescriptor("intValue", PropHolder.class); //$NON-NLS-1$
		editor = ReflectionUtils.invokeHidden(support, "createCellEditort", property, SWT.LEFT); //$NON-NLS-1$
		assertNotNull(editor);
		Object data = editor.getControl().getData(UIControlsFactory.KEY_TYPE);
		assertEquals(UIControlsFactory.TYPE_NUMERIC, data);
		int style = editor.getControl().getStyle();
		assertEquals(SWT.LEFT, style & SWT.LEFT);

		property = new PropertyDescriptor("longValue", PropHolder.class); //$NON-NLS-1$
		editor = ReflectionUtils.invokeHidden(support, "createCellEditort", property, SWT.RIGHT); //$NON-NLS-1$
		assertNotNull(editor);
		data = editor.getControl().getData(UIControlsFactory.KEY_TYPE);
		assertEquals(UIControlsFactory.TYPE_NUMERIC, data);
		style = editor.getControl().getStyle();
		assertEquals(SWT.RIGHT, style & SWT.RIGHT);

		property = new PropertyDescriptor("floatValue", PropHolder.class); //$NON-NLS-1$
		editor = ReflectionUtils.invokeHidden(support, "createCellEditort", property, SWT.CENTER); //$NON-NLS-1$
		assertNotNull(editor);
		data = editor.getControl().getData(UIControlsFactory.KEY_TYPE);
		assertEquals(UIControlsFactory.TYPE_DECIMAL, data);
		style = editor.getControl().getStyle();
		assertEquals(SWT.CENTER, style & SWT.CENTER);

		property = new PropertyDescriptor("doubleValue", PropHolder.class); //$NON-NLS-1$
		editor = ReflectionUtils.invokeHidden(support, "createCellEditort", property, SWT.LEFT); //$NON-NLS-1$
		assertNotNull(editor);
		data = editor.getControl().getData(UIControlsFactory.KEY_TYPE);
		assertEquals(UIControlsFactory.TYPE_DECIMAL, data);

		property = new PropertyDescriptor("bigIntegerValue", PropHolder.class); //$NON-NLS-1$
		editor = ReflectionUtils.invokeHidden(support, "createCellEditort", property, SWT.LEFT); //$NON-NLS-1$
		assertNotNull(editor);
		data = editor.getControl().getData(UIControlsFactory.KEY_TYPE);
		assertEquals(UIControlsFactory.TYPE_NUMERIC, data);

		property = new PropertyDescriptor("bigDecimalValue", PropHolder.class); //$NON-NLS-1$
		editor = ReflectionUtils.invokeHidden(support, "createCellEditort", property, SWT.LEFT); //$NON-NLS-1$
		assertNotNull(editor);
		data = editor.getControl().getData(UIControlsFactory.KEY_TYPE);
		assertEquals(UIControlsFactory.TYPE_DECIMAL, data);

		property = new PropertyDescriptor("integerValue", PropHolder.class); //$NON-NLS-1$
		editor = ReflectionUtils.invokeHidden(support, "createCellEditort", property, SWT.LEFT); //$NON-NLS-1$
		assertNotNull(editor);
		data = editor.getControl().getData(UIControlsFactory.KEY_TYPE);
		assertEquals(UIControlsFactory.TYPE_NUMERIC, data);

		property = new PropertyDescriptor("dateValue", PropHolder.class); //$NON-NLS-1$
		editor = ReflectionUtils.invokeHidden(support, "createCellEditort", property, SWT.LEFT); //$NON-NLS-1$
		assertNotNull(editor);
		data = editor.getControl().getData(UIControlsFactory.KEY_TYPE);
		assertEquals(UIControlsFactory.TYPE_DATE, data);

		property = new PropertyDescriptor("someValue", PropHolder.class); //$NON-NLS-1$
		editor = ReflectionUtils.invokeHidden(support, "createCellEditort", property, SWT.LEFT); //$NON-NLS-1$
		assertNotNull(editor);
		data = editor.getControl().getData(UIControlsFactory.KEY_TYPE);
		assertNull(data);

		property = new PropertyDescriptor("stringValue", PropHolder.class); //$NON-NLS-1$
		editor = ReflectionUtils.invokeHidden(support, "createCellEditort", property, SWT.LEFT); //$NON-NLS-1$
		assertNotNull(editor);
		data = editor.getControl().getData(UIControlsFactory.KEY_TYPE);
		assertNull(data);

	}

	private class PropHolder {

		private boolean booleanValue;
		private int intValue;
		private long longValue;
		private float floatValue;
		private double doubleValue;
		private BigInteger bigIntegerValue;
		private BigDecimal bigDecimalValue;
		private Integer integerValue;
		private Date dateValue;
		private Object someValue;
		private String stringValue;

		public boolean isBooleanValue() {
			return booleanValue;
		}

		public void setBooleanValue(final boolean booleanValue) {
			this.booleanValue = booleanValue;
		}

		public int getIntValue() {
			return intValue;
		}

		public void setIntValue(final int intValue) {
			this.intValue = intValue;
		}

		public long getLongValue() {
			return longValue;
		}

		public void setLongValue(final long longValue) {
			this.longValue = longValue;
		}

		public float getFloatValue() {
			return floatValue;
		}

		public void setFloatValue(final float floatValue) {
			this.floatValue = floatValue;
		}

		public double getDoubleValue() {
			return doubleValue;
		}

		public void setDoubleValue(final double doubleValue) {
			this.doubleValue = doubleValue;
		}

		public BigInteger getBigIntegerValue() {
			return bigIntegerValue;
		}

		public void setBigIntegerValue(final BigInteger bigIntegerValue) {
			this.bigIntegerValue = bigIntegerValue;
		}

		public BigDecimal getBigDecimalValue() {
			return bigDecimalValue;
		}

		public void setBigDecimalValue(final BigDecimal bigDecimalValue) {
			this.bigDecimalValue = bigDecimalValue;
		}

		public Integer getIntegerValue() {
			return integerValue;
		}

		public void setIntegerValue(final Integer integerValue) {
			this.integerValue = integerValue;
		}

		public Date getDateValue() {
			return dateValue;
		}

		public void setDateValue(final Date dateValue) {
			this.dateValue = dateValue;
		}

		public Object getSomeValue() {
			return someValue;
		}

		/**
		 * @param someValue
		 *            the someValue to set
		 */
		public void setSomeValue(final Object someValue) {
			this.someValue = someValue;
		}

		/**
		 * @return the stringValue
		 */
		public String getStringValue() {
			return stringValue;
		}

		/**
		 * @param stringValue
		 *            the stringValue to set
		 */
		public void setStringValue(final String stringValue) {
			this.stringValue = stringValue;
		}

	}

}
