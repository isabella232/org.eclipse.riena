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
package org.eclipse.riena;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.riena.core.test.collect.NonGatherableTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.core.test.collect.TestCollector;
import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.internal.tests.Activator;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Runs all test cases within this bundle according to the given configuration:
 * <ul>
 * <li>if non of the system properties 'includeTestsFile' or 'excludeTestsFile' is specified, all test cases within this bundle are run</li>
 * <li>if the system property 'includeTestsFile' refers to a file listing test classes line-by-line, only those test classes are run</li>
 * <li>if the system property 'excludeTestsFile' refers to a file listing test classes line-by-line, those test classes are skipped</li>
 * <li>if both of the just mentioned system properties are specified, only the tests listed in the includes file are run with the ones in the excludes file
 * being skipped.</li>
 * </ul>
 */
@NonGatherableTestCase("This is not a 'TestCase'!")
public class SelectedTests extends TestCase {

	public static Test suite() throws ClassNotFoundException, IOException {
		final String includeTestsFile = System.getProperty("includeTestsFile"); //$NON-NLS-1$
		final String excludeTestsFile = System.getProperty("excludeTestsFile"); //$NON-NLS-1$

		if (includeTestsFile != null && !includeTestsFile.isEmpty()) {
			return buildTestSuite(includeTestsFile, excludeTestsFile);
		} else {
			return buildTestSuite(excludeTestsFile);
		}
	}

	@SuppressWarnings("unchecked")
	public static Test buildTestSuite(final String includeTestsFile, final String excludeTestsFile) {
		BufferedReader includeTestsFileReader = null;
		final TestSuite testSuite = new TestSuite();
		Set<String> testclasses = new HashSet<String>();

		try {
			final List<String> excludedTests = (excludeTestsFile != null && !excludeTestsFile.isEmpty()) ? readFile(excludeTestsFile) : Collections.EMPTY_LIST;
			includeTestsFileReader = new BufferedReader(new FileReader(includeTestsFile));
			testclasses = readTestFromFile(includeTestsFileReader);
			testclasses = removeExcludedTestsFromTests(testclasses, excludedTests);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		for (final String testClassAsString : testclasses) {
			final Class<?> loadedClass = loadTestCaseFromFilePath(testClassAsString);
			if (loadedClass != null && TestCase.class.isAssignableFrom(loadedClass)) {
				testSuite.addTestSuite((Class<? extends TestCase>) loadedClass);
			} else {
				testSuite.addTest(new JUnit4TestAdapter(loadedClass));
			}
		}

		return testSuite;
	}

	/**
	 * Removes excluded tests from the collected testclasses.
	 * 
	 * @param testclasses
	 *            A set of testclasses that will be executed later on.
	 * @param excludedTests
	 *            A list of tests tha will be escluded.
	 * @return The new Set of testclasses
	 */
	private static Set<String> removeExcludedTestsFromTests(final Set<String> testclasses, final List<String> excludedTests) {
		for (String excludedTest : excludedTests) {
			excludedTest = processLine(excludedTest);
			if (testclasses.contains(excludedTest)) {
				testclasses.remove(excludedTest);
			}
		}
		return testclasses;
	}

	/**
	 * Loads a testclass from the given path.
	 * 
	 * @param pathToClass
	 *            the path to the class.
	 * @return The loaded testclass or null if the class could not be loaded.
	 */
	public static Class<?> loadTestCaseFromFilePath(final String pathToClass) {
		if (pathToClass != null && (!pathToClass.equals(""))) { //$NON-NLS-1$
			try {
				final String fullQualifiedClassName = pathToClass.replaceAll("/", "."); //$NON-NLS-1$ //$NON-NLS-2$
				final Class<?> testClass = SelectedTests.class.getClassLoader().loadClass(fullQualifiedClassName);
				return testClass;
			} catch (final ClassNotFoundException e1) {
				e1.printStackTrace();
			}

		}

		return null;
	}

	/**
	 * Loads testclasses from the given file reader.
	 * 
	 * @param includeTestsFileReader
	 * @return A set of Testclasses in Maven/Ant notation. Or an empty set if
	 * @throws IOException
	 */
	private static Set<String> readTestFromFile(final BufferedReader includeTestsFileReader) throws IOException {
		Set<String> testclasses = new HashSet<String>();
		String test = null;
		while ((test = includeTestsFileReader.readLine()) != null) {
			test = processLine(test);
			testclasses.add(test);
		}
		testclasses = removeSelfFromListOfTestcases(testclasses);

		return testclasses;
	}

	/**
	 * Removes this class(JenkinsTestCollector) from the set of classes.
	 * 
	 * @param testclasses
	 *            the new set of test classes without self.
	 */
	private static Set<String> removeSelfFromListOfTestcases(final Set<String> testclasses) {
		if (testclasses.remove(SelectedTests.class.getName().replaceAll("[.]", "/"))) { //$NON-NLS-1$ //$NON-NLS-2$
			System.out
					.println("Warning removed: " + SelectedTests.class.getName() + " from List of Tests because we dont want to add ourself to List of Tests"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return testclasses;
	}

	/**
	 * Process the given line and removes the .class/.java fileextension from this lines.
	 * 
	 * @param lineToBeProcessed
	 * @return the processed line. If the given String is null return empty String.
	 */
	public static String processLine(final String lineToBeProcessed) {
		if (lineToBeProcessed != null) {
			final String toBeProcessed = lineToBeProcessed;

			final String regex = "(\\w*)(\\W(class|java))"; //$NON-NLS-1$
			final String replacement = "$1"; //$NON-NLS-1$
			final String processed = toBeProcessed.replaceAll(regex, replacement);

			return processed;
		}
		return ""; //$NON-NLS-1$
	}

	@SuppressWarnings("unchecked")
	public static Test buildTestSuite(final String excludeTestsFile) throws IOException {
		TestSuite testSuite = TestCollector.createTestSuiteWithJUnit3And4(Activator.getDefault().getBundle(), null, UITestCase.class, NonUITestCase.class);

		if (excludeTestsFile != null && !excludeTestsFile.isEmpty()) {
			final Enumeration<Test> allTests = testSuite.tests();
			final List<String> testsToSkip = readFile(excludeTestsFile);

			testSuite = new TestSuite();
			while (allTests.hasMoreElements()) {
				final Test test = allTests.nextElement();
				if (!testsToSkip.contains(exchangeDotsWithSlashes(test.toString(), false))) {
					testSuite.addTest(test);
				}
			}
		}

		return testSuite;
	}

	/**
	 * Exchanges dots with slashes in the given string
	 * 
	 * @param stringToBeProcessed
	 * @param invertoperation
	 *            true: exchange slahes with dots false: exchange dots with slahses.
	 * @return the given String with dots and shlashes exhanged
	 */
	private static String exchangeDotsWithSlashes(final String stringToBeProcessed, final Boolean invertoperation) {
		if (invertoperation) {
			return stringToBeProcessed.replaceAll("/", "[.]"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return stringToBeProcessed.replaceAll("[.]", "/"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private static List<String> readFile(final String filename) throws IOException {
		final BufferedReader br = new BufferedReader(new FileReader(filename));
		try {
			final List<String> result = new LinkedList<String>();

			String line;
			while ((line = br.readLine()) != null) {
				line = processLine(line);
				result.add(line);
			}

			return result;
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}
}
