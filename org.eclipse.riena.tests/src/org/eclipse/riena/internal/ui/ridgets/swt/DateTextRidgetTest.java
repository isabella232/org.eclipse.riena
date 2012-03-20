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

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.beans.common.DateBean;
import org.eclipse.riena.beans.common.StringBean;
import org.eclipse.riena.internal.ui.swt.test.UITestHelper;
import org.eclipse.riena.internal.ui.swt.utils.TestUtils;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.ridgets.IDateTextRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.swt.DatePickerComposite;
import org.eclipse.riena.ui.swt.DatePickerComposite.IDateConverterStrategy;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Tests for the class {@link DateTextRidget}.
 */
public class DateTextRidgetTest extends AbstractSWTRidgetTest {

	@Override
	protected IRidget createRidget() {
		final DateTextRidget result = new DateTextRidget();
		result.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		return result;
	}

	@Override
	protected IDateTextRidget getRidget() {
		return (IDateTextRidget) super.getRidget();
	}

	@Override
	protected Control createWidget(final Composite parent) {
		final Control result = new Text(getShell(), SWT.RIGHT | SWT.BORDER | SWT.SINGLE);
		result.setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_DATE);
		result.setLayoutData(new RowData(100, SWT.DEFAULT));
		return result;
	}

	@Override
	protected Text getWidget() {
		return (Text) super.getWidget();
	}

	// test methods
	///////////////

	public void testEmptyText() {
		final IDateTextRidget ridget = getRidget();

		ridget.setFormat(IDateTextRidget.FORMAT_DDMM);
		ridget.updateFromModel();
		assertTrue(ridget.getMarkersOfType(ErrorMarker.class).isEmpty());

		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYY);
		ridget.updateFromModel();
		assertTrue(ridget.getMarkersOfType(ErrorMarker.class).isEmpty());

		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		ridget.updateFromModel();
		assertTrue(ridget.getMarkersOfType(ErrorMarker.class).isEmpty());

		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYYHHMM);
		ridget.updateFromModel();
		assertTrue(ridget.getMarkersOfType(ErrorMarker.class).isEmpty());
	}

	public void testRidgetMapping() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(DateTextRidget.class, mapper.getRidgetClass(getWidget()));
	}

	public void testDelete() {
		final IDateTextRidget ridget = getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);

		assertText("^01.10.2008", UITestHelper.KC_DEL, " ^1.10.2008");
		assertText("0^1.10.2008", UITestHelper.KC_DEL, " 0^.10.2008");
		assertText("01.10.200^8", UITestHelper.KC_DEL, "01.10. 200^");
		assertText("01.10.2008^", UITestHelper.KC_DEL, "01.10.2008^");
		assertText("01.10^.2008", UITestHelper.KC_DEL, "01.10. ^008");
		assertText("01.10^. 008", UITestHelper.KC_DEL, "01.10.  ^08");
		assertText("01.10^.^2008", UITestHelper.KC_DEL, "01.10^.2008");
		assertText("01.10^.2^008", UITestHelper.KC_DEL, "01.10^. 008");
		assertText("01.1^0.2^008", UITestHelper.KC_DEL, "01. 1^. 008");
		assertText("^01.10.2008^", UITestHelper.KC_DEL, "  ^.  .    ");

		assertText("^01.10.2008", "\b", "^01.10.2008");
		assertText("0^1.10.2008", "\b", " ^1.10.2008");
		assertText("01.10.200^8", "\b", "01.10. 20^8");
		assertText("01.10.2008^", "\b", "01.10. 200^");
		assertText("01.10.^2008", "\b", "01. 1^.2008");
		assertText("01. 1.^2008", "\b", "01.  ^.2008");
		assertText("01.10^.^2008", "\b", "01.10^.2008");
		assertText("01.10^.2^008", "\b", "01.10^. 008");
		assertText("01.1^0.2^008", "\b", "01. 1^. 008");
		assertText("^01.10.2008^", "\b", "  ^.  .    ");

		assertText("^  .  .    ", UITestHelper.KC_DEL, "  ^.  .    ");
		assertText("  ^.  .    ", UITestHelper.KC_DEL, "  .  ^.    ");
		assertText("  . ^ .    ", UITestHelper.KC_DEL, "  .  ^.    ");
		assertText("  .  ^.    ", UITestHelper.KC_DEL, "  .  .    ^");
		assertText("  .  .    ^", "\b", "  .  ^.    ");
		assertText("  . ^ .    ", "\b", "  ^.  .    ");
		assertText("  ^.  .    ", "\b", "  ^.  .    ");
		assertText(" ^ .  .    ", "\b", " ^ .  .    ");
	}

	public void testReplace() {
		final IDateTextRidget ridget = getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);

		assertText("01.10^.^2008", "1", "01.10^.2008");
		assertText("01.10^.^2008", ".", "01.10.^2008");
		assertText("01.10^.2^008", "1", "01.10.1^008");
		assertText("01.1^0.^2008", "3", "01.13^.2008");
		assertText("01.1^0.2^008", "3", "01.13^. 008");
		assertText("^01^.10.2008", "3", " 3^.10.2008");
		assertText("^01.^10.2008", "3", " 3^.10.2008");
		assertText("^01.1^0.2008", "3", " 3^. 0.2008");
		assertText("^01.10.2^008", "3", " 3^.  . 008");
		assertText("^01.10.2008^", "3", " 3^.  .    ");
	}

	public void testInsert() {
		final IDateTextRidget ridget = getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);

		assertText("  ^.  .    ", "01102008", "01.10.2008^");
		assertText(" ^ .  .    ", "01.10.2008", "01.10.2008^");
		assertText("^  .  .    ", "01.10.20081234", "01.10.2008^");
		assertText("  ^.10.2008", "0123", "01^.10.2008");
		assertText("  ^.  .2008", "1208", "12.08^.2008");
		assertText("  .  ^.2008", "1208", "  .12^.2008");
		assertText("01.  .^2008", "10", "01.  .^2008");
		assertText("  .  .    ^", "2008", "  .  .2008^");
		assertText("  .  ^.    ", "102008", "  .10.2008^");
	}

	public void testSetText() {
		final IDateTextRidget ridget = getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);

		ridget.setText("01.10.2008");
		assertEquals("01.10.2008", ridget.getText());

		ridget.setText("01.10");
		assertEquals("01.10.    ", ridget.getText());

		ridget.setText("22.22.2222");
		assertEquals("22.22.2222", ridget.getText());

		ridget.setText("");
		assertEquals("  .  .    ", ridget.getText());

		ridget.setText("  .10.");
		assertEquals("  .10.    ", ridget.getText());

		ridget.setText("  .  .");
		assertEquals("  .  .    ", ridget.getText());

		try {
			ridget.setText("abc");
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		try {
			ridget.setText("12102008");
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		try {
			ridget.setText("12/10/2008");
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}

		try {
			ridget.setText("12.ab");
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
	}

	/**
	 * Tests that setText(null) clears the ridget (i.e. results in an empty
	 * pattern with just the separators)
	 */
	public void testSetTextNull() {
		final IDateTextRidget ridget = getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);

		ridget.setText("01.10.2008");

		assertEquals("01.10.2008", ridget.getText());

		ridget.setText(null);

		assertEquals("  .  .    ", ridget.getText());
	}

	public void testSetFormatAfterSetText() {
		final IDateTextRidget ridget = getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		final Text control = getWidget();
		final StringBean bean = new StringBean();
		ridget.bindToModel(bean, StringBean.PROP_VALUE);

		ridget.setText("01.10.2008");

		assertEquals("01.10.2008", control.getText());
		assertEquals("01.10.2008", ridget.getText());
		assertEquals("01.10.2008", bean.getValue());

		// As per Bug 289535. See also #testSetFormatWithStringBean
		ridget.setFormat(IDateTextRidget.FORMAT_HHMM);

		assertEquals("  :  ", control.getText());
		assertEquals("  :  ", ridget.getText());
		assertEquals("  :  ", bean.getValue());
	}

	public void testUpdateFromModel() {
		final IDateTextRidget ridget = getRidget();
		final Text control = getWidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		final StringBean bean = new StringBean("12.10.2008");
		ridget.bindToModel(bean, StringBean.PROP_VALUE);

		// value fully matches pattern
		ridget.updateFromModel();

		assertEquals("12.10.2008", control.getText());
		assertEquals("12.10.2008", ridget.getText());
		assertEquals("12.10.2008", bean.getValue());

		// value matches sub-pattern
		bean.setValue("  .12");
		ridget.updateFromModel();

		assertEquals("  .12.    ", control.getText());
		assertEquals("  .12.    ", ridget.getText());
		assertEquals("  .12", bean.getValue());

		// value does not match patter; control and ridget unchanged
		bean.setValue("abc");
		ridget.updateFromModel();

		assertEquals("  .12.    ", control.getText());
		assertEquals("  .12.    ", control.getText());
		assertEquals("abc", bean.getValue());
	}

	public void testBindToDate() {
		final IDateTextRidget ridget = getRidget();
		final Text control = getWidget();
		final DateBean bean = new DateBean(localize(0L));

		ridget.bindToModel(bean, DateBean.DATE_PROPERTY);
		ridget.updateFromModel();

		assertEquals("01.01.1970", control.getText());
		assertEquals("01.01.1970", ridget.getText());
		assertEquals(localize(0L), bean.getValue());
	}

	/**
	 * As per Bug 289535
	 * <p>
	 * When the ridget / control value on setFormat(...) it will be overwritten
	 * with the freshly formatted data from the model. If there is no model it
	 * will be cleared.
	 */
	public void testSetFormatWithDateBean() {
		final IDateTextRidget ridget = getRidget();
		final Text control = getWidget();
		final DateBean bean = new DateBean(localize(0L));

		ridget.bindToModel(bean, DateBean.DATE_PROPERTY);
		ridget.updateFromModel();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYY);

		assertEquals("01.01.70", ridget.getText());
		assertEquals("01.01.70", control.getText());
		assertEquals(localize(0L), bean.getValue());

		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);

		assertEquals("01.01.1970", ridget.getText());
		assertEquals("01.01.1970", control.getText());
		assertEquals(localize(0L), bean.getValue());
	}

	/**
	 * As per Bug 289535
	 * <p>
	 * When the format is incompatible with the model (this can never happen
	 * when the model is backed by a Date. It can only happen when the model is
	 * backed by a String), the format will be changed and the value of the
	 * model will be applied as is to the ridget / widget. If the value is
	 * longer than the format pattern, it will be truncated. In any case the
	 * resulting value will most likely be incorrect (since it is based on the
	 * old format) and may cause an error marker to appear.
	 */
	public void testSetFormatWithStringBean() {
		final IDateTextRidget ridget = getRidget();
		final Text control = getWidget();
		final StringBean bean = new StringBean("01.01.1970");

		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		ridget.bindToModel(bean, StringBean.PROP_VALUE);
		ridget.updateFromModel();

		assertEquals("01.01.1970", ridget.getText());
		assertEquals("01.01.1970", control.getText());
		assertEquals("01.01.1970", bean.getValue());

		ridget.setFormat("dd/MM/yy");

		assertEquals("  /  /  ", ridget.getText());
		assertEquals("  /  /  ", control.getText());
		assertEquals("  /  /  ", bean.getValue());
		assertFalse(ridget.isErrorMarked());

		control.setFocus();

		UITestHelper.sendString(control.getDisplay(), "01/01/70\t");

		assertEquals("01/01/70", ridget.getText());
		assertEquals("01/01/70", control.getText());
		assertEquals("01/01/70", bean.getValue());
		assertFalse(ridget.isErrorMarked());
	}

	/**
	 * As per Bug 289535
	 * <p>
	 * When the ridget / control value on setFormat(...) it will be overwritten
	 * with the freshly formatted data from the model. If there is no model it
	 * will be cleared.
	 */
	public void testSetFormatWithNoBean() {
		final IDateTextRidget ridget = getRidget();
		final Text control = getWidget();

		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		ridget.setText("01.01.1970");

		assertEquals("01.01.1970", ridget.getText());
		assertEquals("01.01.1970", control.getText());

		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYY);

		assertEquals("  .  .  ", ridget.getText());
		assertEquals("  .  .  ", control.getText());
	}

	public void testAutoFillYYYY() {
		final IDateTextRidget ridget = getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);

		assertText("  .  .    ^", "00\t", "  .  .2000");
		assertText("  .  .^    ", "01\t", "  .  .2001");
		assertText("  .  . ^   ", "29\t", "  .  .2029");
		assertText("  .  .    ^", "30\t", "  .  .1930");
		assertText("  .  .    ^", "99\t", "  .  .1999");
	}

	public void testAutoFillYYYYWithError() {
		final IDateTextRidget ridget = getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);

		ridget.setText("31.10.2008");
		assertFalse(ridget.isErrorMarked());
		assertText("31.10.    ^", "8\t", "31.10.   8");
		assertTrue(ridget.isErrorMarked());

		ridget.setText("31.10.2008");
		assertFalse(ridget.isErrorMarked());
		assertText("31.10.    ^", "008\t", "31.10. 008");
		assertTrue(ridget.isErrorMarked());
	}

	public void testDoNotFillYY() {
		final IDateTextRidget ridget = getRidget();

		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYY);
		assertText("  .  .  ^", "00\t", "  .  .00");

		ridget.setFormat("ddMMyy");
		assertText("      ^", "00\t", "    00");
	}

	public void testMandatoryMarker() {
		final IDateTextRidget ridget = getRidget();
		ridget.setMandatory(true);
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);

		ridget.setText("31.10.2008");

		TestUtils.assertMandatoryMarker(ridget, 1, true);

		ridget.setText(null);

		TestUtils.assertMandatoryMarker(ridget, 1, false);

		ridget.setMandatory(false);

		TestUtils.assertMandatoryMarker(ridget, 0, false);
	}

	public void testDateConverterStrategyGetter() {
		final DateTextRidget ridget = (DateTextRidget) getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		final IDateConverterStrategy strategy = ridget.new RidgetAwareDateConverterStrategy();

		assertNull(strategy.getDateFromTextField(null));
		assertNull(strategy.getDateFromTextField(""));
		assertNull(strategy.getDateFromTextField("abcd"));
		assertNull(strategy.getDateFromTextField("12.12"));

		final Calendar calendar = Calendar.getInstance();
		calendar.set(2009, 11, 24, 0, 0, 0);
		final Date expected = calendar.getTime();

		final Date result = strategy.getDateFromTextField("24.12.2009");
		assertEquals(expected.toString(), result.toString());
	}

	public void testDateConverterStrategySetter() {
		final DateTextRidget ridget = (DateTextRidget) getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYYHHMM);
		final IDateConverterStrategy strategy = ridget.new RidgetAwareDateConverterStrategy();

		final Calendar calendar = Calendar.getInstance();
		calendar.set(2009, 11, 24, 0, 0, 0);
		final Date date = calendar.getTime();

		strategy.setDateToTextField(date);

		assertEquals("24.12.2009 00:00", ridget.getText());
	}

	/**
	 * As per Bug 305056.
	 */
	public void testMandatoryWithDatePickerWidget() {
		final IDateTextRidget ridget = getRidget();
		ridget.setMandatory(true);
		final DatePickerComposite control = new DatePickerComposite(getShell(), SWT.BORDER);
		ridget.setUIControl(control);

		final Display display = control.getDisplay();
		final Color colorWhite = display.getSystemColor(SWT.COLOR_WHITE);
		final Color colorMandatory = Activator.getSharedColor(display, SharedColors.COLOR_MANDATORY);
		assertFalse(colorWhite.equals(colorMandatory)); // sanity check

		ridget.setText("01.10.2008");

		assertEquals("01.10.2008", ridget.getText());
		assertEquals("01.10.2008", control.getTextfield().getText());
		assertMandatoryMarker(ridget, 1, true);
		assertEquals(colorWhite, control.getTextfield().getBackground());

		ridget.setText("  .  .    ");

		assertEquals("  .  .    ", ridget.getText());
		assertEquals("  .  .    ", control.getTextfield().getText());
		assertMandatoryMarker(ridget, 1, false);
		assertEquals(colorMandatory, control.getTextfield().getBackground());

		ridget.setMandatory(false);

		assertEquals("  .  .    ", ridget.getText());
		assertEquals("  .  .    ", control.getTextfield().getText());
		assertMandatoryMarker(ridget, 0, false);
		assertEquals(colorWhite, control.getTextfield().getBackground());
	}

	/**
	 * As per Bug 313254
	 */
	public void testHideEmptyValueWhenOutputOnly() {
		final IDateTextRidget ridget = getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		final Text control = getWidget();

		assertEquals(false, ridget.isOutputOnly());
		assertEquals("  .  .    ", ridget.getText());
		assertEquals("  .  .    ", control.getText());

		ridget.setOutputOnly(true);

		assertEquals(true, ridget.isOutputOnly());
		assertEquals("  .  .    ", ridget.getText());
		assertEquals("", control.getText());

		ridget.setOutputOnly(false);

		assertEquals(false, ridget.isOutputOnly());
		assertEquals("  .  .    ", ridget.getText());
		assertEquals("  .  .    ", control.getText());
	}

	/**
	 * As per Bug 317028.
	 */
	public void testTogglingEnabledWithDirectWritingPreservesContent() {
		final IDateTextRidget ridget = getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		final Text control = getWidget();

		ridget.setDirectWriting(true);

		assertTrue(ridget.isDirectWriting());

		final StringBean bean = new StringBean("  .  .    ");
		ridget.bindToModel(bean, StringBean.PROP_VALUE);
		ridget.updateFromModel();

		assertEquals("  .  .    ", control.getText());
		assertEquals("  .  .    ", ridget.getText());
		assertEquals("  .  .    ", bean.getValue());

		ridget.setEnabled(false);

		assertEquals("", control.getText());
		assertEquals("  .  .    ", ridget.getText());
		assertEquals("  .  .    ", bean.getValue());

		ridget.setEnabled(true);

		assertEquals("  .  .    ", control.getText());
		assertEquals("  .  .    ", ridget.getText());
		assertEquals("  .  .    ", bean.getValue());
	}

	/**
	 * As per Bug 317028.
	 */
	public void testTogglingOutputOnlyWithDirectWritingPreservesContent() {
		final IDateTextRidget ridget = getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		final Text control = getWidget();

		ridget.setDirectWriting(true);

		assertTrue(ridget.isDirectWriting());

		final StringBean bean = new StringBean("  .  .    ");
		ridget.bindToModel(bean, StringBean.PROP_VALUE);
		ridget.updateFromModel();

		assertEquals("  .  .    ", control.getText());
		assertEquals("  .  .    ", ridget.getText());
		assertEquals("  .  .    ", bean.getValue());

		ridget.setOutputOnly(true);

		assertEquals("", control.getText());
		assertEquals("  .  .    ", ridget.getText());
		assertEquals("  .  .    ", bean.getValue());

		ridget.setOutputOnly(false);

		assertEquals("  .  .    ", control.getText());
		assertEquals("  .  .    ", ridget.getText());
		assertEquals("  .  .    ", bean.getValue());
	}

	// helping methods
	//////////////////

	private void assertText(final String before, final String keySeq, final String after) {
		TestUtils.assertText(getWidget(), before, keySeq, after);
	}

	private void assertText(final String before, final int keyCode, final String after) {
		TestUtils.assertText(getWidget(), before, keyCode, after);
	}

	private void assertMandatoryMarker(final IMarkableRidget ridget, final int count, final boolean isDisabled) {
		final Collection<MandatoryMarker> markers = ridget.getMarkersOfType(MandatoryMarker.class);
		assertEquals(count, markers.size());
		for (final MandatoryMarker marker : markers) {
			assertEquals(isDisabled, marker.isDisabled());
		}
	}

	@SuppressWarnings("deprecation")
	private Date localize(final long msSinceEpochUtc) {
		final Date localDate = new Date(msSinceEpochUtc);
		return new Date(localDate.getTime() + (60 * 1000 * localDate.getTimezoneOffset()));
	}

}
