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
package org.eclipse.riena.internal.core.test;

import java.io.File;

import junit.framework.TestCase;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

/**
 * Base class for test cases.<br>
 * It extends the {@link junit.framework.TestCase} with a few helpers.
 */
// this is for org.eclipse.core.internal.registry.ExtensionRegistry
@SuppressWarnings("restriction")
public abstract class RienaTestCase extends TestCase {
	protected final TestingTools tools = new TestingTools(new JUnit3Wrapper(this));

	/**
	 * 
	 */
	public RienaTestCase() {
		super();
	}

	/**
	 * @param name
	 */
	public RienaTestCase(final String name) {
		super(name);
	}

	/**
	 * A counterpart to Assert.fail() that may be invoked to indicate that everything is fine and that the test should continue. May be used e.g. in an
	 * otherwise empty catch block that handles an expected exception. In this use case its advantages over a comment are that it allows a more uniform way of
	 * documentation than the numerous variations of "// ignore" and that it avoids a Checkstyle warning about the empty block.
	 */
	protected void ok() {
		tools.ok();
	}

	/**
	 * A counterpart to Assert.fail(String) that may be invoked to indicate that everything is fine and that the test should continue.
	 * 
	 * @see #ok()
	 * 
	 * @param message
	 *            A message explaining why nothing is wrong.
	 */
	protected void ok(final String message) {
		tools.ok(message);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		tools.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		tools.tearDown();
		super.tearDown();
	}

	/**
	 * Return the bundle context. <br>
	 * <b>Note: </b>This method must not be called from a constructor of a test case!
	 * 
	 * @return
	 */
	protected BundleContext getContext() {
		return tools.getContext();
	}

	/**
	 * Get the resource located in the folder along with the test case.
	 * <p>
	 * <b>Note:</b> The resource will be copied into a temporary file and this file will be returned. This file will be deleted on JVM exit.
	 * 
	 * @param resource
	 * @return a file to the content of the resource
	 */
	protected File getFile(final String resource) {
		return tools.getFile(resource);
	}

	/**
	 * Check whether trace is switched on or not.
	 * 
	 * @return tracing?
	 */
	protected boolean isTrace() {
		return tools.isTrace();
	}

	/**
	 * Print the current test´s name.
	 */
	protected void printTestName() {
		tools.printTestName();
	}

	/**
	 * Print the string, no CR/LF.
	 * 
	 * @param string
	 */
	protected void print(final String string) {
		tools.print(string);
	}

	/**
	 * Print the string, with CR/LF.
	 * 
	 * @param string
	 */
	protected void println(final String string) {
		tools.println(string);
	}

	/**
	 * Add an extension/extension point defined within the ´plugin.xml´ (located along side the test class) given with the <code>pluginResource</code> to the
	 * extension registry.
	 * 
	 * @param pluginResource
	 * @throws InterruptedException
	 */
	protected void addPluginXml(final String pluginResource) {
		tools.addPluginXml(pluginResource);
	}

	/**
	 * Add an extension/extension point defined within the ´plugin.xml´ given with the <code>pluginResource</code> to the extension registry.
	 * 
	 * @param forLoad
	 * @param pluginResource
	 * @throws InterruptedException
	 */
	protected void addPluginXml(final Class<?> forLoad, final String pluginResource) {
		tools.addPluginXml(forLoad, pluginResource);
	}

	/**
	 * Remove the given extension from the extension registry.
	 * 
	 * @param extensionId
	 */
	protected void removeExtension(final String extensionId) {
		tools.removeExtension(extensionId);
	}

	/**
	 * Remove the given extension from the extension registry.
	 * 
	 * @param extensionPointId
	 */
	protected void removeExtensionPoint(final String extensionPointId) {
		tools.removeExtensionPoint(extensionPointId);
	}

	/**
	 * Get the service for the specified <code>serviceClass</code>.
	 * 
	 * @param serviceClass
	 * @return
	 */
	protected <T> T getService(final Class<T> serviceClass) {
		return tools.getService(serviceClass);
	}

	/**
	 * Unget the specified <code>service</code>.
	 * 
	 * @param service
	 */
	protected void ungetService(final Object service) {
		tools.ungetService(service);
	}

	/**
	 * Starts the bundle with the given <code>bundleName</code>.
	 * 
	 * @param bundleName
	 * @throws BundleException
	 */
	protected void startBundle(final String bundleName) throws BundleException {
		tools.startBundle(bundleName);
	}

	/**
	 * Starts all bundles that match the <code>includePattern</code> but not the <code>excludePattern</code>. The <code>excludePattern</code> may be
	 * <code>null</code>.
	 * 
	 * @param includePattern
	 * @param excludePattern
	 * @throws BundleException
	 */
	protected void startBundles(final String includePattern, final String excludePattern) throws BundleException {
		tools.startBundles(includePattern, excludePattern);
	}

	/**
	 * Stops the bundle with the given <code>bundleName</code>.
	 * 
	 * @param bundleName
	 * @throws BundleException
	 */
	protected void stopBundle(final String bundleName) throws BundleException {
		tools.stopBundle(bundleName);
	}

	/**
	 * Stops all bundles that match the <code>includePattern</code> but not the <code>excludePattern</code>. The <code>excludePattern</code> may be
	 * <code>null</code>.
	 * 
	 * @param includePattern
	 * @param excludePattern
	 * @throws BundleException
	 */
	protected void stopBundles(final String includePattern, final String excludePattern) throws BundleException {
		tools.stopBundles(includePattern, excludePattern);
	}

	/**
	 * IClosure with all bundles that match the <code>includePattern</code> but not the <code>excludePattern</code> what is specified within the
	 * <code>closure</code>. The <code>excludePattern</code> may be <code>null</code>.
	 * 
	 * @param includePattern
	 * @param excludePattern
	 * @param closure
	 * @throws BundleException
	 */
	protected void doWithBundles(final String includePattern, final String excludePattern, final IClosure closure) throws BundleException {
		tools.doWithBundles(includePattern, excludePattern, new org.eclipse.riena.internal.core.test.TestingTools.IClosure() {
			public void execute(final Bundle bundle) throws BundleException {
				closure.execute(bundle);
			}
		});
	}

	/**
	 * @deprecated use <tt>org.eclipse.riena.internal.core.test.TestingTools.IClosure</tt>
	 */
	@Deprecated
	public interface IClosure {
		void execute(Bundle bundle) throws BundleException;
	}
}
