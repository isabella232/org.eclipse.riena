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
package org.eclipse.riena.navigation.ui.swt.views;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.swt.lnf.ColorLnfResource;
import org.eclipse.riena.ui.swt.lnf.ILnfCustomizer;
import org.eclipse.riena.ui.swt.lnf.ILnfTheme;
import org.eclipse.riena.ui.swt.lnf.IgnoreLnFUpdater;
import org.eclipse.riena.ui.swt.lnf.LnFUpdater;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Tests of the class {@link LnFUpdater}.
 */
@UITestCase
public class LnFUpdaterTest extends RienaTestCase {

	private Shell shell;
	private LnFUpdater lnFUpdater;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		shell = new Shell();
		lnFUpdater = ReflectionUtils.newInstanceHidden(LnFUpdater.class);
		final Map<String, Object> resourceCache = ReflectionUtils.getHidden(lnFUpdater, "RESOURCE_CACHE"); //$NON-NLS-1$
		resourceCache.clear();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		SwtUtilities.dispose(shell);
		lnFUpdater = null;
	}

	/**
	 * Tests the <i>private</i> method {@code getErrorMessage}.
	 * 
	 * @throws IntrospectionException
	 *             handled by jUnit
	 */
	public void testGetErrorMessage() throws IntrospectionException {

		final PropertyDescriptor property = new PropertyDescriptor("foreground", Label.class); //$NON-NLS-1$
		final Label label = new Label(shell, SWT.NONE);
		final String message = ReflectionUtils.invokeHidden(lnFUpdater, "getErrorMessage", label, property); //$NON-NLS-1$
		assertNotNull(message);
		assertTrue(message.indexOf(Label.class.getSimpleName()) > 0);
		assertTrue(message.indexOf(property.getName()) > 0);
		SwtUtilities.dispose(label);

	}

	/**
	 * Tests the <i>private</i> method {@code getLnfValue}.
	 * 
	 * @throws IntrospectionException
	 *             handled by jUnit
	 */
	public void testGetLnfValue() throws IntrospectionException {
		final RienaDefaultLnf oldLnf = LnfManager.getLnf();
		final RienaDefaultLnf lnf = new RienaDefaultLnf();
		LnfManager.setLnf(lnf);

		try {
			lnf.setTheme(new MyTheme());
			final Label label = new Label(shell, SWT.NONE);
			final PropertyDescriptor property = new PropertyDescriptor("foreground", Label.class); //$NON-NLS-1$
			Object value = ReflectionUtils.invokeHidden(lnFUpdater, "getLnfValue", label, property); //$NON-NLS-1$
			SwtUtilities.dispose(label);

			assertNotNull(value);
			assertTrue(value instanceof Color);
			final Color color = (Color) value;
			assertEquals(1, color.getRed());
			assertEquals(2, color.getGreen());
			assertEquals(3, color.getBlue());

			lnf.setTheme(new MyTheme());
			final Text text = new Text(shell, SWT.NONE);
			value = ReflectionUtils.invokeHidden(lnFUpdater, "getLnfValue", text, property); //$NON-NLS-1$
			SwtUtilities.dispose(text);

			assertNull(value);
		} finally {
			LnfManager.setLnf(oldLnf);
		}

	}

	/**
	 * Tests the <i>private</i> method {@code getLnfStyleValue}.
	 * 
	 * @throws IntrospectionException
	 *             handled by jUnit
	 */
	public void testGetLnfStyleValue() throws IntrospectionException {
		final RienaDefaultLnf oldLnf = LnfManager.getLnf();
		final RienaDefaultLnf lnf = new RienaDefaultLnf();
		LnfManager.setLnf(lnf);

		try {
			// Label widget with style (existing) "section"
			lnf.setTheme(new MyTheme());
			Label label = new Label(shell, SWT.NONE);
			label.setData(UIControlsFactory.KEY_LNF_STYLE, "section"); //$NON-NLS-1$
			final PropertyDescriptor property = new PropertyDescriptor("foreground", Label.class); //$NON-NLS-1$
			Object value = ReflectionUtils.invokeHidden(lnFUpdater, "getLnfStyleValue", label, property); //$NON-NLS-1$
			SwtUtilities.dispose(label);

			assertNotNull(value);
			assertTrue(value instanceof Color);
			Color color = (Color) value;
			assertEquals(111, color.getRed());
			assertEquals(22, color.getGreen());
			assertEquals(3, color.getBlue());

			// Label widget with style (not existing) "dummy"
			lnf.setTheme(new MyTheme());
			label = new Label(shell, SWT.NONE);
			label.setData(UIControlsFactory.KEY_LNF_STYLE, "dummy"); //$NON-NLS-1$
			value = ReflectionUtils.invokeHidden(lnFUpdater, "getLnfStyleValue", label, property); //$NON-NLS-1$
			SwtUtilities.dispose(label);

			assertNull(value);

			// Text widget with style (existing) "section"
			// (It also works for other widgets.)
			lnf.setTheme(new MyTheme());
			final Text text = new Text(shell, SWT.NONE);
			text.setData(UIControlsFactory.KEY_LNF_STYLE, "section"); //$NON-NLS-1$
			value = ReflectionUtils.invokeHidden(lnFUpdater, "getLnfStyleValue", text, property); //$NON-NLS-1$
			SwtUtilities.dispose(label);

			assertNotNull(value);
			assertTrue(value instanceof Color);
			color = (Color) value;
			assertEquals(111, color.getRed());
			assertEquals(22, color.getGreen());
			assertEquals(3, color.getBlue());
		} finally {
			LnfManager.setLnf(oldLnf);
		}
	}

	/**
	 * Tests the <i>private</i> method {@code checkPropertyUpdateView}.
	 */
	public void testCheckPropertyUpdateView() {

		boolean retValue = ReflectionUtils.invokeHidden(lnFUpdater, "checkPropertyUpdateView"); //$NON-NLS-1$
		assertFalse(retValue);

		System.setProperty("riena.lnf.update.view", "abc"); //$NON-NLS-1$ //$NON-NLS-2$
		retValue = ReflectionUtils.invokeHidden(lnFUpdater, "checkPropertyUpdateView"); //$NON-NLS-1$
		assertFalse(retValue);

		System.setProperty("riena.lnf.update.view", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		retValue = ReflectionUtils.invokeHidden(lnFUpdater, "checkPropertyUpdateView"); //$NON-NLS-1$
		assertTrue(retValue);

		System.setProperty("riena.lnf.update.view", "false"); //$NON-NLS-1$ //$NON-NLS-2$
		retValue = ReflectionUtils.invokeHidden(lnFUpdater, "checkPropertyUpdateView"); //$NON-NLS-1$
		assertFalse(retValue);

	}

	/**
	 * Tests the <i>private</i> method {@code updateUIControl}.
	 */
	public void testUpdateUIControl() {
		final String oldUpdateValue = System.getProperty("riena.lnf.update.view", ""); //$NON-NLS-1$ //$NON-NLS-2$
		final RienaDefaultLnf oldLnf = LnfManager.getLnf();
		try {
			System.setProperty("riena.lnf.update.view", "true"); //$NON-NLS-1$ //$NON-NLS-2$
			final RienaDefaultLnf lnf = new RienaDefaultLnf();
			LnfManager.setLnf(lnf);
			lnf.setTheme(new MyTheme());
			final Label label = new Label(shell, SWT.NONE);
			ReflectionUtils.invokeHidden(lnFUpdater, "updateUIControl", label); //$NON-NLS-1$
			final Color labelColor = label.getForeground();
			final Color themeColor = lnf.getColor("Label.foreground"); //$NON-NLS-1$
			assertEquals(themeColor, labelColor);
		} finally {
			LnfManager.setLnf(oldLnf);
			System.setProperty("riena.lnf.update.view", oldUpdateValue); //$NON-NLS-1$
		}
	}

	/**
	 * Tests the <i>private</i> method {@code getDefaultPropertyValue}.
	 * 
	 * @throws IntrospectionException
	 *             handled by jUnit
	 */
	public void testGetDefaultPropertyValue() throws IntrospectionException {

		final Label label = new Label(shell, SWT.NONE);
		final PropertyDescriptor foregroundProperty = new PropertyDescriptor("foreground", Label.class); //$NON-NLS-1$
		final Object value = ReflectionUtils.invokeHidden(lnFUpdater, "getDefaultPropertyValue", label, //$NON-NLS-1$
				foregroundProperty);
		assertEquals(label.getForeground().getRGB(), value);

	}

	/**
	 * Tests the <i>private</i> method {@code getPropertyValue}.
	 * 
	 * @throws IntrospectionException
	 *             handled by jUnit
	 */
	public void testGetPropertyValue() throws IntrospectionException {

		final Label label = new Label(shell, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		final PropertyDescriptor property = new PropertyDescriptor("alignment", Label.class); //$NON-NLS-1$
		final Object value = ReflectionUtils.invokeHidden(lnFUpdater, "getPropertyValue", label, property); //$NON-NLS-1$
		assertEquals(SWT.RIGHT, value);

	}

	/**
	 * Tests the <i>private</i> method {@code hasNoDefaultValue}.
	 * 
	 * @throws IntrospectionException
	 *             handled by jUnit
	 */
	public void testHasNoDefaultValue() throws IntrospectionException {

		final Label label = new Label(shell, SWT.NONE);
		final PropertyDescriptor property = new PropertyDescriptor("text", Label.class); //$NON-NLS-1$
		Boolean ret = ReflectionUtils.invokeHidden(lnFUpdater, "hasNoDefaultValue", label, property, label.getText()); //$NON-NLS-1$
		assertFalse(ret);

		label.setText("Hello!"); //$NON-NLS-1$
		ret = ReflectionUtils.invokeHidden(lnFUpdater, "hasNoDefaultValue", label, property, label.getText()); //$NON-NLS-1$
		assertTrue(ret);

	}

	/**
	 * Tests the <i>private</i> method {@code getSimpleClassName}.
	 */
	public void testGetSimpleClassName() {

		Class<? extends Control> controlClass = Label.class;
		String className = ReflectionUtils.invokeHidden(lnFUpdater, "getSimpleClassName", controlClass); //$NON-NLS-1$
		assertEquals(Label.class.getSimpleName(), className);

		final Control innerControl = new Composite(shell, SWT.NONE) {
			@Override
			public boolean setFocus() {
				return true;
			}
		};

		controlClass = innerControl.getClass();
		className = ReflectionUtils.invokeHidden(lnFUpdater, "getSimpleClassName", controlClass); //$NON-NLS-1$
		assertEquals(Composite.class.getSimpleName(), className);

	}

	/**
	 * Tests the <i>private</i> method {@code checkLnfKeys}.
	 */
	public void testCheckLnfKeys() {
		final RienaDefaultLnf oldLnf = LnfManager.getLnf();
		try {
			final RienaDefaultLnf lnf = new RienaDefaultLnf();
			LnfManager.setLnf(lnf);
			final ILnfTheme oldTheme = lnf.getTheme();
			final MyTheme myTheme = new MyTheme();
			lnf.setTheme(myTheme);
			final Label label = new Label(shell, SWT.NONE);
			boolean ret = ReflectionUtils.invokeHidden(lnFUpdater, "checkLnfKeys", label); //$NON-NLS-1$
			lnf.setTheme(oldTheme);
			SwtUtilities.dispose(label);

			assertTrue(ret);

			lnf.setTheme(myTheme);
			final Text text = new Text(shell, SWT.NONE);
			ret = ReflectionUtils.invokeHidden(lnFUpdater, "checkLnfKeys", text); //$NON-NLS-1$
			lnf.setTheme(oldTheme);
			SwtUtilities.dispose(text);

			assertFalse(ret);
		} finally {
			LnfManager.setLnf(oldLnf);
		}
	}

	/**
	 * Tests the <i>private</i> method {@code checkLnfClassKeys}.
	 */
	public void testCheckLnfClassKeys() {

		final RienaDefaultLnf oldLnf = LnfManager.getLnf();
		final RienaDefaultLnf lnf = new RienaDefaultLnf();
		LnfManager.setLnf(lnf);
		lnf.setTheme(new MyTheme());

		boolean ret = ReflectionUtils.invokeHidden(lnFUpdater, "checkLnfClassKeys", Label.class); //$NON-NLS-1$
		assertTrue(ret);

		ret = ReflectionUtils.invokeHidden(lnFUpdater, "checkLnfClassKeys", Text.class); //$NON-NLS-1$
		assertFalse(ret);

		ret = ReflectionUtils.invokeHidden(lnFUpdater, "checkLnfClassKeys", MyComposite.class); //$NON-NLS-1$
		assertTrue(ret);

		LnfManager.setLnf(oldLnf);

	}

	/**
	 * Tests the <i>private</i> method
	 * {@code ignoreProperty(Class<? extends Control>, PropertyDescriptor)}.
	 * 
	 * @throws IntrospectionException
	 *             handled by jUnit
	 */
	public void testIgnoreProperty() throws IntrospectionException {

		PropertyDescriptor property = new PropertyDescriptor("foreground", Label.class); //$NON-NLS-1$
		boolean ret = ReflectionUtils.invokeHidden(lnFUpdater, "ignoreProperty", Label.class, property); //$NON-NLS-1$
		assertFalse(ret);

		property = new PropertyDescriptor("foreground", MyComposite.class); //$NON-NLS-1$
		ret = ReflectionUtils.invokeHidden(lnFUpdater, "ignoreProperty", MyComposite.class, property); //$NON-NLS-1$
		assertFalse(ret);

		property = new PropertyDescriptor("background", MyComposite.class); //$NON-NLS-1$
		ret = ReflectionUtils.invokeHidden(lnFUpdater, "ignoreProperty", MyComposite.class, property); //$NON-NLS-1$
		assertTrue(ret);

		property = new PropertyDescriptor("background", Composite.class); //$NON-NLS-1$
		ret = ReflectionUtils.invokeHidden(lnFUpdater, "ignoreProperty", MyComposite.class, property); //$NON-NLS-1$
		assertTrue(ret);

	}

	/**
	 * Tests the <i>private</i> method {@code ignoreControl(Control)}.
	 * 
	 * @throws IntrospectionException
	 *             handled by jUnit
	 */
	public void testIgnoreControl() throws IntrospectionException {

		final Label label = new Label(shell, SWT.DEFAULT);
		boolean ret = ReflectionUtils.invokeHidden(lnFUpdater, "ignoreControl", label); //$NON-NLS-1$
		assertFalse(ret);
		label.dispose();

		Composite comp = new MyComposite(shell, SWT.DEFAULT);
		ret = ReflectionUtils.invokeHidden(lnFUpdater, "ignoreControl", comp); //$NON-NLS-1$
		assertFalse(ret);
		comp.dispose();

		comp = new MyIgnoreComposite(shell, SWT.DEFAULT);
		ret = ReflectionUtils.invokeHidden(lnFUpdater, "ignoreControl", comp); //$NON-NLS-1$
		assertTrue(ret);
		comp.dispose();

	}

	/**
	 * Tests the <i>private</i> method {@code getResourceData(Object)}.
	 */
	public void testGetResourceData() {

		final String strg = "strgValue"; //$NON-NLS-1$
		Object data = ReflectionUtils.invokeHidden(lnFUpdater, "getResourceData", strg); //$NON-NLS-1$
		assertEquals(strg, data);

		final Color red = new Color(null, 200, 10, 10);
		data = ReflectionUtils.invokeHidden(lnFUpdater, "getResourceData", red); //$NON-NLS-1$
		assertEquals(red.getRGB(), data);

		red.dispose();
		data = ReflectionUtils.invokeHidden(lnFUpdater, "getResourceData", red); //$NON-NLS-1$
		assertSame(red, data);

		final Font font = new Font(null, "Arial", 12, SWT.BOLD); //$NON-NLS-1$
		data = ReflectionUtils.invokeHidden(lnFUpdater, "getResourceData", font); //$NON-NLS-1$
		assertTrue(data instanceof FontData[]);
		final FontData[] fontData = (FontData[]) data;
		assertEquals(font.getFontData()[0], fontData[0]);

		font.dispose();
		data = ReflectionUtils.invokeHidden(lnFUpdater, "getResourceData", font); //$NON-NLS-1$
		assertSame(font, data);

	}

	/**
	 * Tests the <i>private</i> method {@code generateLnfKey}.
	 * 
	 * @throws IntrospectionException
	 *             handled by jUnit
	 */
	public void testGenerateLnfKey() throws IntrospectionException {

		final PropertyDescriptor property = new PropertyDescriptor("foreground", Label.class); //$NON-NLS-1$
		final String ret = ReflectionUtils.invokeHidden(lnFUpdater, "generateLnfKey", Label.class, property); //$NON-NLS-1$
		assertEquals("Label.foreground", ret); //$NON-NLS-1$

	}

	public void testValuesEquals() {

		Object value1 = new Integer(1);
		Object value2 = new Integer(1);
		boolean ret = ReflectionUtils.invokeHidden(lnFUpdater, "valuesEquals", value1, value2); //$NON-NLS-1$
		assertTrue(ret);

		value2 = new Integer(4711);
		ret = ReflectionUtils.invokeHidden(lnFUpdater, "valuesEquals", value1, value2); //$NON-NLS-1$
		assertFalse(ret);

		// colors
		final Color color1 = new Color(null, 200, 10, 10);
		final Color color2 = new Color(null, 200, 10, 10);
		ret = ReflectionUtils.invokeHidden(lnFUpdater, "valuesEquals", color1, color2); //$NON-NLS-1$
		assertTrue(ret);

		final Color color3 = new Color(null, 1, 20, 30);
		ret = ReflectionUtils.invokeHidden(lnFUpdater, "valuesEquals", color1, color3); //$NON-NLS-1$
		assertFalse(ret);

		value2 = new RGB(200, 10, 10);
		ret = ReflectionUtils.invokeHidden(lnFUpdater, "valuesEquals", color1, value2); //$NON-NLS-1$
		assertTrue(ret);

		value1 = new RGB(200, 10, 10);
		ret = ReflectionUtils.invokeHidden(lnFUpdater, "valuesEquals", value1, value2); //$NON-NLS-1$
		assertTrue(ret);

		value1 = new RGB(1, 20, 30);
		ret = ReflectionUtils.invokeHidden(lnFUpdater, "valuesEquals", value1, value2); //$NON-NLS-1$
		assertFalse(ret);

		value1 = new RGB(1, 20, 30);
		ret = ReflectionUtils.invokeHidden(lnFUpdater, "valuesEquals", color3, value1); //$NON-NLS-1$
		assertTrue(ret);
		color1.dispose();
		color2.dispose();
		color3.dispose();

		// fonts
		final Font font1 = new Font(null, "arial", 12, SWT.BOLD); //$NON-NLS-1$
		final Font font2 = new Font(null, "arial", 12, SWT.BOLD); //$NON-NLS-1$
		ret = ReflectionUtils.invokeHidden(lnFUpdater, "valuesEquals", font1, font2); //$NON-NLS-1$
		assertTrue(ret);

		final Font font3 = new Font(null, "arial", 72, SWT.BOLD); //$NON-NLS-1$
		ret = ReflectionUtils.invokeHidden(lnFUpdater, "valuesEquals", font1, font3); //$NON-NLS-1$
		assertFalse(ret);

		value2 = new FontData[] { new FontData("arial", 12, SWT.BOLD) }; //$NON-NLS-1$
		ret = ReflectionUtils.invokeHidden(lnFUpdater, "valuesEquals", font1, value2); //$NON-NLS-1$
		assertTrue(ret);

		value2 = new FontData[] { new FontData("arial", 12, SWT.BOLD) }; //$NON-NLS-1$
		ret = ReflectionUtils.invokeHidden(lnFUpdater, "valuesEquals", font3, value2); //$NON-NLS-1$
		assertFalse(ret);

		value1 = new FontData[] { new FontData("helvetica", 12, SWT.BOLD) }; //$NON-NLS-1$
		ret = ReflectionUtils.invokeHidden(lnFUpdater, "valuesEquals", value1, value2); //$NON-NLS-1$
		assertFalse(ret);

		value1 = new FontData[] { new FontData("arial", 12, SWT.BOLD) }; //$NON-NLS-1$
		ret = ReflectionUtils.invokeHidden(lnFUpdater, "valuesEquals", value1, value2); //$NON-NLS-1$
		assertTrue(ret);

		font1.dispose();
		font2.dispose();
		font3.dispose();

	}

	/**
	 * Simple Riena Look&Feel theme with only some colors.
	 */
	private static class MyTheme implements ILnfTheme {

		public void customizeLnf(final ILnfCustomizer lnf) {
			lnf.putLnfResource("Composite.background", new ColorLnfResource(47, 11, 15)); //$NON-NLS-1$
			lnf.putLnfResource("Label.foreground", new ColorLnfResource(1, 2, 3)); //$NON-NLS-1$
			lnf.putLnfResource("section.foreground", new ColorLnfResource(111, 22, 3)); //$NON-NLS-1$
		}

	}

	@IgnoreLnFUpdater("background")
	private static class MyComposite extends Composite {

		public MyComposite(final Composite parent, final int style) {
			super(parent, style);
		}

	}

	@IgnoreLnFUpdater("*")
	private static class MyIgnoreComposite extends Composite {

		public MyIgnoreComposite(final Composite parent, final int style) {
			super(parent, style);
		}

	}

}
