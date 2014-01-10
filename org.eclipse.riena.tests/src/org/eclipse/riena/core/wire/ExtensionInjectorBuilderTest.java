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
package org.eclipse.riena.core.wire;

import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.riena.core.injector.extension.ExtensionInjector;
import org.eclipse.riena.core.injector.extension.IData;
import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.core.util.WeakRef;
import org.eclipse.riena.internal.core.wire.ExtensionInjectorBuilder;

/**
 * Test the {@code ExtensionInjectorBuilder}.
 */
@NonUITestCase
public class ExtensionInjectorBuilderTest extends RienaTestCase {

	public void testBuildForUpdate1() throws NoSuchMethodException {
		final Method bindMethod = ExtensionInjectorBuilderTest.class.getDeclaredMethod("update1",
				new Class[] { IData.class });
		final ExtensionInjectorBuilder builder = new ExtensionInjectorBuilder(this, bindMethod);
		final ExtensionInjector injector = builder.build();
		assertNotNull(injector);
		assertEquals("testA", rawId(injector));
		assertEquals(IData.class, useType(injector));
		assertEquals(0, getMin(injector));
		assertEquals(1, getMax(injector));
		assertTrue(getHomogenious(injector));
		assertSame(this, getTarget(injector));
		assertEquals("update1", getUpdateMethodName(injector));
		assertFalse(getDoNotReplaceSymbols(injector));
		assertFalse(getSpecific(injector));
		assertFalse(getOnceOnly(injector));
	}

	@InjectExtension(id = "testA")
	public void update1(final IData data) {
	}

	public void testBuildForUpdate1Array() throws NoSuchMethodException {
		final Method bindMethod = ExtensionInjectorBuilderTest.class.getDeclaredMethod("update1Array",
				new Class[] { IData[].class });
		final ExtensionInjectorBuilder builder = new ExtensionInjectorBuilder(this, bindMethod);
		final ExtensionInjector injector = builder.build();
		assertNotNull(injector);
		assertEquals("testA[]", rawId(injector));
		assertEquals(IData.class, useType(injector));
		assertEquals(0, getMin(injector));
		assertEquals(Integer.MAX_VALUE, getMax(injector));
		assertTrue(getHomogenious(injector));
		assertSame(this, getTarget(injector));
		assertEquals("update1Array", getUpdateMethodName(injector));
		assertFalse(getDoNotReplaceSymbols(injector));
		assertFalse(getSpecific(injector));
		assertFalse(getOnceOnly(injector));
	}

	@InjectExtension(id = "testA[]")
	public void update1Array(final IData[] data) {
	}

	public void testBuildForUpdate2() throws NoSuchMethodException {
		final Method bindMethod = ExtensionInjectorBuilderTest.class.getDeclaredMethod("update2",
				new Class[] { IData[].class });
		final ExtensionInjectorBuilder builder = new ExtensionInjectorBuilder(this, bindMethod);
		final ExtensionInjector injector = builder.build();
		assertNotNull(injector);
		assertEquals("testB", rawId(injector));
		assertEquals(IData.class, useType(injector));
		assertEquals(2, getMin(injector));
		assertEquals(5, getMax(injector));
		assertFalse(getHomogenious(injector));
		assertSame(this, getTarget(injector));
		assertEquals("update2", getUpdateMethodName(injector));
		assertTrue(getDoNotReplaceSymbols(injector));
		assertTrue(getSpecific(injector));
		assertFalse(getOnceOnly(injector));
	}

	@InjectExtension(id = "testB", doNotReplaceSymbols = true, heterogeneous = true, specific = true, min = 2, max = 5)
	public void update2(final IData[] data) {
	}

	public void testBuildForUpdate3() throws NoSuchMethodException {
		final Method bindMethod = ExtensionInjectorBuilderTest.class.getDeclaredMethod("update3",
				new Class[] { IData.class });
		final ExtensionInjectorBuilder builder = new ExtensionInjectorBuilder(this, bindMethod);
		final ExtensionInjector injector = builder.build();
		assertNotNull(injector);
		assertEquals("testC", rawId(injector));
		assertEquals(IData.class, useType(injector));
		assertEquals(0, getMin(injector));
		assertEquals(1, getMax(injector));
		assertFalse(getHomogenious(injector));
		assertSame(this, getTarget(injector));
		assertEquals("update3", getUpdateMethodName(injector));
		assertFalse(getDoNotReplaceSymbols(injector));
		assertTrue(getSpecific(injector));
		assertFalse(getOnceOnly(injector));
	}

	@InjectExtension(id = "testC", heterogeneous = true, specific = true, min = 0, max = 1)
	public void update3(final IData data) {
	}

	public void testBuildForUpdateWithAnExtensionInterfaceWithID() throws NoSuchMethodException {
		final Method bindMethod = ExtensionInjectorBuilderTest.class.getDeclaredMethod("updateWithID",
				new Class[] { IDataWithID.class });
		final ExtensionInjectorBuilder builder = new ExtensionInjectorBuilder(this, bindMethod);
		final ExtensionInjector injector = builder.build();
		assertNotNull(injector);
		final String expectedId = getContext().getBundle().getSymbolicName() + ".testWithID";
		try {
			injector.andStart(getContext());
		} catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains(expectedId));
		}
		assertEquals(expectedId, firstNormalizedId(injector));
		assertEquals(IDataWithID.class, useType(injector));
		assertEquals(0, getMin(injector));
		assertEquals(1, getMax(injector));
		assertFalse(getHomogenious(injector));
		assertSame(this, getTarget(injector));
		assertEquals("updateWithID", getUpdateMethodName(injector));
		assertFalse(getDoNotReplaceSymbols(injector));
		assertTrue(getSpecific(injector));
		assertFalse(getOnceOnly(injector));
	}

	@InjectExtension(heterogeneous = true, specific = true, min = 0, max = 1)
	public void updateWithID(final IDataWithID data) {
	}

	public void testBuildForUpdateWithAnExtensionInterfaceWithIDinAnnotation() throws NoSuchMethodException {
		final Method bindMethod = ExtensionInjectorBuilderTest.class.getDeclaredMethod("updateWithIDinAnnotation",
				new Class[] { IDataWithIDinAnnotation.class });
		final ExtensionInjectorBuilder builder = new ExtensionInjectorBuilder(this, bindMethod);
		final ExtensionInjector injector = builder.build();
		assertNotNull(injector);
		final String expectedId = getContext().getBundle().getSymbolicName() + ".testWithIDinAnnotation";
		try {
			injector.andStart(getContext());
		} catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains(expectedId));
		}
		assertEquals(expectedId, firstNormalizedId(injector));
		assertEquals(IDataWithIDinAnnotation.class, useType(injector));
		assertEquals(0, getMin(injector));
		assertEquals(1, getMax(injector));
		assertFalse(getHomogenious(injector));
		assertSame(this, getTarget(injector));
		assertEquals("updateWithIDinAnnotation", getUpdateMethodName(injector));
		assertFalse(getDoNotReplaceSymbols(injector));
		assertTrue(getSpecific(injector));
		assertFalse(getOnceOnly(injector));
	}

	@InjectExtension(heterogeneous = true, specific = true, min = 0, max = 1)
	public void updateWithIDinAnnotation(final IDataWithIDinAnnotation data) {
	}

	public void testBuildForUpdate4WithAnExtensionInterfaceWithIDinAnnotationAndWithOnceOnlyViaStatic()
			throws NoSuchMethodException {
		final Method bindMethod = ExtensionInjectorBuilderTest.class.getDeclaredMethod("update4WithIDinAnnotation",
				new Class[] { IDataWithIDinAnnotation.class });
		final ExtensionInjectorBuilder builder = new ExtensionInjectorBuilder(this, bindMethod);
		final ExtensionInjector injector = builder.build();
		assertNotNull(injector);
		final String expectedId = getContext().getBundle().getSymbolicName() + ".testWithIDinAnnotation";
		try {
			injector.andStart(getContext());
		} catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains(expectedId));
		}
		assertEquals(expectedId, firstNormalizedId(injector));
		assertEquals(IDataWithIDinAnnotation.class, useType(injector));
		assertEquals(0, getMin(injector));
		assertEquals(1, getMax(injector));
		assertFalse(getHomogenious(injector));
		assertSame(this, getTarget(injector));
		assertEquals("update4WithIDinAnnotation", getUpdateMethodName(injector));
		assertFalse(getDoNotReplaceSymbols(injector));
		assertTrue(getSpecific(injector));
		assertTrue(getOnceOnly(injector));
	}

	@InjectExtension(heterogeneous = true, specific = true, min = 0, max = 1)
	public static void update4WithIDinAnnotation(final IDataWithIDinAnnotation data) {
	}

	public void testBuildForUpdate5WithAnExtensionInterfaceWithIDinAnnotationAndWithOnceOnlyViaAnnotation()
			throws NoSuchMethodException {
		final Method bindMethod = ExtensionInjectorBuilderTest.class.getDeclaredMethod("update5WithIDinAnnotation",
				new Class[] { IDataWithIDinAnnotation.class });
		final ExtensionInjectorBuilder builder = new ExtensionInjectorBuilder(this, bindMethod);
		final ExtensionInjector injector = builder.build();
		assertNotNull(injector);
		final String expectedId = getContext().getBundle().getSymbolicName() + ".testWithIDinAnnotation";
		try {
			injector.andStart(getContext());
		} catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains(expectedId));
		}
		assertEquals(expectedId, firstNormalizedId(injector));
		assertEquals(IDataWithIDinAnnotation.class, useType(injector));
		assertEquals(0, getMin(injector));
		assertEquals(1, getMax(injector));
		assertFalse(getHomogenious(injector));
		assertSame(this, getTarget(injector));
		assertEquals("update5WithIDinAnnotation", getUpdateMethodName(injector));
		assertFalse(getDoNotReplaceSymbols(injector));
		assertTrue(getSpecific(injector));
		assertTrue(getOnceOnly(injector));
	}

	@InjectExtension(heterogeneous = true, specific = true, min = 0, max = 1, onceOnly = true)
	public void update5WithIDinAnnotation(final IDataWithIDinAnnotation data) {
	}

	private String rawId(final ExtensionInjector injector) {
		final Object extensionDescriptor = ReflectionUtils.getHidden(injector, "extensionDesc");
		final Object extensionPointId = ReflectionUtils.getHidden(extensionDescriptor, "extensionPointId");
		return ReflectionUtils.getHidden(extensionPointId, "rawId");
	}

	private String firstNormalizedId(final ExtensionInjector injector) {
		final Object extensionDescriptor = ReflectionUtils.getHidden(injector, "extensionDesc");
		final Object extensionPointId = ReflectionUtils.getHidden(extensionDescriptor, "extensionPointId");
		final List<String> ids = ReflectionUtils.getHidden(extensionPointId, "normalizedIds");
		return ids.get(0);
	}

	private Object useType(final ExtensionInjector injector) {
		final Object extensionDescriptor = ReflectionUtils.getHidden(injector, "extensionDesc");
		return ReflectionUtils.getHidden(extensionDescriptor, "interfaceType");
	}

	private int getMin(final ExtensionInjector injector) {
		final Object extensionDescriptor = ReflectionUtils.getHidden(injector, "extensionDesc");
		return ReflectionUtils.getHidden(extensionDescriptor, "minOccurrences");
	}

	private int getMax(final ExtensionInjector injector) {
		final Object extensionDescriptor = ReflectionUtils.getHidden(injector, "extensionDesc");
		return ReflectionUtils.getHidden(extensionDescriptor, "maxOccurrences");
	}

	private boolean getHomogenious(final ExtensionInjector injector) {
		final Object extensionDescriptor = ReflectionUtils.getHidden(injector, "extensionDesc");
		return ReflectionUtils.getHidden(extensionDescriptor, "homogeneous");
	}

	private Object getTarget(final ExtensionInjector injector) {
		final WeakRef<?> ref = ReflectionUtils.getHidden(injector, "targetRef");
		return ref.get();
	}

	private String getUpdateMethodName(final ExtensionInjector injector) {
		return ReflectionUtils.getHidden(injector, "updateMethodName");
	}

	private boolean getDoNotReplaceSymbols(final ExtensionInjector injector) {
		return !(Boolean) ReflectionUtils.getHidden(injector, "symbolReplace");
	}

	private boolean getSpecific(final ExtensionInjector injector) {
		return !(Boolean) ReflectionUtils.getHidden(injector, "nonSpecific");
	}

	private boolean getOnceOnly(final ExtensionInjector injector) {
		return ReflectionUtils.getHidden(injector, "onceOnly");
	}

}
