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
package org.eclipse.riena.internal.core.test.collect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.Test;

import org.osgi.framework.Bundle;

import org.eclipse.riena.core.util.Iter;

/**
 * A helper for collecting test classes.
 */
public final class TestCollector {

	private static final String PLUS = " + "; //$NON-NLS-1$

	private TestCollector() {
		// utility
	}

	/**
	 * Create a {@code TestSuite} that contains all test cases in the given bundle.
	 * 
	 * @param bundle
	 *            bundle to collect all {@code TestCase}s
	 * @param withinPackage
	 *            if not <code>null</code>, only {@code TestCase}s within this package are collected
	 * @param annotationClasses
	 *            only {@code TestCase}s that have one of these annotations are collected
	 * @return
	 */
	public static TestSuite createTestSuiteWith(final Bundle bundle, final Package withinPackage, final Class<? extends Annotation>... annotationClasses) {
		return createTestSuiteWith(bundle, withinPackage, false, annotationClasses);
	}

	/**
	 * Create a {@code TestSuite} that contains all test cases in the given bundle.
	 * 
	 * @param bundle
	 *            bundle to collect all test cases
	 * @param withinPackage
	 *            if not <code>null</code>, only test cases within this package are collected
	 * @param annotationClasses
	 *            only test cases that have one of these annotations are collected
	 * @return
	 */
	public static TestSuite createTestSuiteWithJUnit3And4(final Bundle bundle, final Package withinPackage,
			final Class<? extends Annotation>... annotationClasses) {
		return createTestSuiteWithJUnit3And4(bundle, withinPackage, false, annotationClasses);
	}

	/**
	 * Create a {@code TestSuite} that contains all test cases in the given bundle.
	 * 
	 * @param bundle
	 *            bundle to collect all {@code TestCase}s
	 * @param withinPackage
	 *            if not <code>null</code>, only {@code TestCase}s within this package are collected
	 * @param annotationClasses
	 *            only {@code TestCase}s that have one of these annotations are collected
	 * @param subPackages
	 *            on <code>true</code> also collect sub-packages
	 * @return
	 */
	public static TestSuite createTestSuiteWith(final Bundle bundle, final Package withinPackage, final boolean subPackages,
			final Class<? extends Annotation>... annotationClasses) {
		return createTestSuiteWith(bundle, withinPackage, subPackages, true, annotationClasses);
	}

	@SuppressWarnings("unchecked")
	private static TestSuite createTestSuiteWith(final Bundle bundle, final Package withinPackage, final boolean subPackages, final boolean excludeJUnit4Tests,
			final Class<? extends Annotation>... annotationClasses) {
		final StringBuilder bob = new StringBuilder("Tests within bundle '").append(bundle.getSymbolicName()).append( //$NON-NLS-1$
				"' and package '"); //$NON-NLS-1$
		bob.append(withinPackage == null ? "all" : withinPackage.getName()).append("'").append(" recursive '").append( //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				subPackages).append("'"); //$NON-NLS-1$
		bob.append(" and restricted to '"); //$NON-NLS-1$
		for (final Class<? extends Annotation> annotationClass : annotationClasses) {
			bob.append(annotationClass.getSimpleName()).append(PLUS);
		}
		if (annotationClasses.length > 0) {
			bob.setLength(bob.length() - PLUS.length());
		} else {
			bob.append("none"); //$NON-NLS-1$
		}
		bob.append("'."); //$NON-NLS-1$
		final TestSuite suite = new TestSuite(bob.toString());
		for (final Class<?> clazz : collectWith(bundle, withinPackage, subPackages, excludeJUnit4Tests, annotationClasses)) {
			if (TestCase.class.isAssignableFrom(clazz)) {
				suite.addTestSuite((Class<? extends TestCase>) clazz);
			} else {
				suite.addTest(new JUnit4TestAdapter(clazz));
			}
		}
		return suite;
	}

	/**
	 * Create a {@code TestSuite} that contains all test cases in the given bundle. The suite will contain JUnit3 and JUnit4 tests.
	 * 
	 * @param bundle
	 *            bundle to collect all test cases
	 * @param withinPackage
	 *            if not <code>null</code>, only test cases within this package are collected
	 * @param annotationClasses
	 *            only test cases that have one of these annotations are collected
	 * @param subPackages
	 *            on <code>true</code> also collect sub-packages
	 * @return
	 */
	public static TestSuite createTestSuiteWithJUnit3And4(final Bundle bundle, final Package withinPackage, final boolean subPackages,
			final Class<? extends Annotation>... annotationClasses) {
		return createTestSuiteWith(bundle, withinPackage, subPackages, false, annotationClasses);
	}

	/**
	 * Collect all {@code TestCase}e within the given bundle.
	 * 
	 * @param bundle
	 *            bundle to collect all {@code TestCase}s
	 * @param withinPackage
	 *            if not <code>null</code>, only {@code TestCase}s within this package are collected
	 * @param annotationClasses
	 *            only {@code TestCase}s that have one of these annotations are collected
	 * @param subPackages
	 *            on <code>true</code> also collect sub-packages
	 * @return
	 */
	public static List<Class<? extends TestCase>> collectWith(final Bundle bundle, final Package withinPackage, final boolean subPackages,
			final Class<? extends Annotation>... annotationClasses) {
		return convertToTestCaseList(collectWith(bundle, withinPackage, subPackages, true, annotationClasses));
	}

	private static List<Class<?>> collectWith(final Bundle bundle, final Package withinPackage, final boolean subPackages, final boolean excludeJUnit4Tests,
			final Class<? extends Annotation>... annotationClasses) {
		final List<Class<?>> testClasses = new ArrayList<Class<?>>();

		for (final Class<?> testClass : collect(bundle, withinPackage, subPackages, excludeJUnit4Tests)) {
			boolean collect = annotationClasses.length == 0 ? true : false;
			for (final Class<? extends Annotation> annotationClass : annotationClasses) {
				collect = collect || testClass.isAnnotationPresent(annotationClass);
			}
			if (collect) {
				testClasses.add(testClass);
			}
		}
		return testClasses;
	}

	/**
	 * Collect all test cases within the given bundle with at least one of the given annotations. This method considers JUnit3 and JUnit4 test cases.
	 * 
	 * @param bundle
	 *            bundle to collect all test cases
	 * @param withinPackage
	 *            if not <code>null</code>, only test cases within this package are collected
	 * @param subPackages
	 *            on <code>true</code> also collect sub-packages
	 * @param annotationClasses
	 *            only test cases that have one of these annotations are collected
	 * @return
	 */
	public static List<Class<?>> collectWithJUnit3And4(final Bundle bundle, final Package withinPackage, final boolean subPackages,
			final Class<? extends Annotation>... annotationClasses) {
		return collectWith(bundle, withinPackage, subPackages, false, annotationClasses);
	}

	/**
	 * Collect all unmarked {@code TestCase}s within the given bundle. This method considers JUnit3 tests only.
	 * 
	 * @param bundle
	 *            bundle to collect all {@code TestCase}s
	 * @param withinPackage
	 *            if not <code>null</code>, only {@code TestCase}s within this package are collected
	 * @return
	 */
	public static List<Class<? extends TestCase>> collectUnmarked(final Bundle bundle, final Package withinPackage) {
		return convertToTestCaseList(collectUnmarked(bundle, withinPackage, true));
	}

	private static List<Class<?>> collectUnmarked(final Bundle bundle, final Package withinPackage, final boolean excludeJUnit4Tests) {
		final List<Class<?>> testClasses = new ArrayList<Class<?>>();

		for (final Class<?> testClass : collect(bundle, withinPackage, true, excludeJUnit4Tests)) {
			if (testClass.getAnnotations().length == 0) {
				testClasses.add(testClass);
			}
		}
		return testClasses;
	}

	/**
	 * Collect all unmarked test cases within the given bundle. This method considers JUnit3 and JUnit4 test cases.
	 * 
	 * @param bundle
	 *            bundle to collect all test cases
	 * @param withinPackage
	 *            if not <code>null</code>, only test cases within this package are collected
	 * @return
	 */
	public static List<Class<?>> collectUnmarkedJUnit3And4(final Bundle bundle, final Package withinPackage) {
		return collectUnmarked(bundle, withinPackage, false);
	}

	/**
	 * Collect all badly named test cases within the given bundle (JUnit3 and JUnit4).
	 * 
	 * @param bundle
	 *            bundle to collect all test cases
	 * @param withinPackage
	 *            if not <code>null</code>, only test cases within this package are collected
	 * @return
	 */
	public static List<Class<?>> collectBadlyNamedJUnit3And4(final Bundle bundle, final Package withinPackage) {
		return collectBadlyNamed(bundle, withinPackage, false);
	}

	/**
	 * Collect all badly named {@code TestCase}e within the given bundle. This method collects JUnit3 tests only.
	 * 
	 * @param bundle
	 *            bundle to collect all {@code TestCase}s
	 * @param withinPackage
	 *            if not <code>null</code>, only {@code TestCase}s within this package are collected
	 * @return
	 */
	public static List<Class<? extends TestCase>> collectBadlyNamed(final Bundle bundle, final Package withinPackage) {
		return convertToTestCaseList(collectBadlyNamed(bundle, withinPackage, true));
	}

	private static List<Class<?>> collectBadlyNamed(final Bundle bundle, final Package withinPackage, final boolean excludeJUnit4Tests) {
		final List<Class<?>> testClasses = new ArrayList<Class<?>>();
		final Pattern testClassPattern = Pattern.compile(".*Test\\d*$"); //$NON-NLS-1$
		for (final Class<?> testClass : collect(bundle, withinPackage, true, excludeJUnit4Tests)) {
			if (!testClassPattern.matcher(testClass.getName()).matches()) {
				testClasses.add(testClass);
			}
		}

		return testClasses;
	}

	/**
	 * Collect all JUnit3 test cases within the given bundle.
	 * 
	 * @param bundle
	 *            bundle to collect all test cases
	 * @param withinPackage
	 *            if not <code>null</code>, only test cases within this package are collected
	 * @param subPackages
	 *            on <code>true</code> also collect sub-packages
	 * @return a list of {@link TestCase}s
	 */
	public static List<Class<? extends TestCase>> collect(final Bundle bundle, final Package withinPackage, final boolean subPackages) {
		return convertToTestCaseList(collect(bundle, withinPackage, subPackages, true));
	}

	/**
	 * @param classes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<Class<? extends TestCase>> convertToTestCaseList(final List<Class<?>> classes) {
		final List<Class<? extends TestCase>> result = new ArrayList<Class<? extends TestCase>>();
		for (final Class<?> c : classes) {
			result.add((Class<TestCase>) c);
		}
		return result;
	}

	/**
	 * Collect all JUnit3 and JUnit4 test cases within the given bundle.
	 * 
	 * @param bundle
	 *            bundle to collect all test cases
	 * @param withinPackage
	 *            if not <code>null</code>, only test cases within this package are collected
	 * @param subPackages
	 *            on <code>true</code> also collect sub-packages
	 * @return a list of test classes
	 */
	public static List<Class<?>> collectJUnit3And4(final Bundle bundle, final Package withinPackage, final boolean subPackages) {
		return collect(bundle, withinPackage, subPackages, false);
	}

	/**
	 * Collect all JUnit3 and JUnit4 test cases within the given bundle.
	 * 
	 * @param bundle
	 *            bundle to collect all test cases
	 * @param withinPackage
	 *            if not <code>null</code>, only test cases within this package are collected
	 * @param subPackages
	 *            on <code>true</code> also collect sub-packages
	 * @param excludeJUnit4Tests
	 *            <code>true</code> to scan for <strong>JUnit3 {@link TestCase}s only</strong> (support the legacy behavior)
	 * @return
	 */
	private static List<Class<?>> collect(final Bundle bundle, final Package withinPackage, final boolean subPackages, final boolean excludeJUnit4Tests) {
		final List<Class<?>> result = new ArrayList<Class<?>>();
		final Enumeration<URL> allClasses = bundle.findEntries("", "*.class", true); //$NON-NLS-1$ //$NON-NLS-2$
		for (final URL entryURL : Iter.able(allClasses)) {
			final String url = entryURL.toExternalForm();
			if (url.contains("$")) { //$NON-NLS-1$
				// Skip inner classes
				continue;
			}
			final Class<?> clazz = getClass(bundle, entryURL);
			if (clazz == null) {
				trace("Could not get class from ", url); //$NON-NLS-1$
				continue;
			}
			if (excludeJUnit4Tests && !TestCase.class.isAssignableFrom(clazz)) {
				trace("Not a JUnit3 TestCase: ", clazz.getName()); //$NON-NLS-1$
				continue;
			}
			final String className = clazz.getName();
			if (withinPackage != null) {
				if (subPackages) {
					if (!(className.startsWith(withinPackage.getName()) && className.endsWith(clazz.getSimpleName()))) {
						continue;
					}
				} else {
					if (!className.equals(withinPackage.getName() + "." + clazz.getSimpleName())) { //$NON-NLS-1$
						continue;
					}
				}
			}
			if (clazz.isAnnotationPresent(NonGatherableTestCase.class)) {
				continue;
			}
			if (Modifier.isAbstract(clazz.getModifiers())) {
				continue;
			}
			addToListIfApplicable(clazz, result);
		}
		return result;
	}

	/**
	 * Adds the given class to the list if it is either a JUnit3 (extends {@link TestCase}) or a JUnit4 (contains @{@link Test} annotated methods) test case.
	 * 
	 * @param clazz
	 *            the class to check
	 * @param testClasses
	 *            the test cases list, to which the class will be added if it is a test case
	 */
	private static void addToListIfApplicable(final Class<?> clazz, final List<Class<?>> testClasses) {
		if (TestCase.class.isAssignableFrom(clazz)) {
			// JUnit3
			testClasses.add(clazz);
		} else if (isJUnit4TestCase(clazz)) {
			testClasses.add(clazz);
		}
	}

	/**
	 * @param clazz
	 *            the class to check
	 * @return <code>true</code> if the class contains at least one method annotated with @{@link Test}
	 */
	private static boolean isJUnit4TestCase(final Class<?> clazz) {
		for (final Method method : clazz.getMethods()) {
			if (method.isAnnotationPresent(Test.class)) {
				return true;
			}
		}
		return false;
	}

	private static Class<?> getClass(final Bundle bundle, final URL entryURL) {
		final String entry = entryURL.toExternalForm().replace(".class", "").replace('/', '.'); //$NON-NLS-1$ //$NON-NLS-2$
		// Brute force detecting of how many chars we have to skip to find a class within the url
		final String name = entry;
		int dot = 0;
		while ((dot = name.indexOf('.', dot)) != -1) {
			final String className = name.substring(dot + 1);
			try {
				return bundle.loadClass(className);
			} catch (final ClassNotFoundException e) {
				dot++;
			} catch (final NoClassDefFoundError e) {
				dot++;
			}
		}
		return null;
	}

	private static void trace(final Object... objects) {
		final StringBuilder bob = new StringBuilder();
		for (final Object object : objects) {
			bob.append(object);
		}
		System.err.println(bob.toString());
	}
}
