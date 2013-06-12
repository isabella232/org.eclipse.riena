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
package org.eclipse.riena.internal.core.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import org.eclipse.core.internal.registry.ExtensionRegistry;
import org.eclipse.core.internal.registry.osgi.ExtensionEventDispatcherJob;
import org.eclipse.core.runtime.ContributorFactoryOSGi;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;

import org.eclipse.riena.core.exception.MurphysLawFailure;
import org.eclipse.riena.core.util.IOUtils;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.core.util.Trace;
import org.eclipse.riena.internal.core.ignore.IgnoreFindBugs;

/**
 * Base class for test cases.<br>
 * It extends the {@link junit.framework.TestCase} with a few helpers.
 */
// this is for org.eclipse.core.internal.registry.ExtensionRegistry
@SuppressWarnings("restriction")
public class TestingTools {

	public interface IClosure {
		void execute(Bundle bundle) throws BundleException;
	}

	interface TestCaseWrapper {
		/**
		 * @return
		 */
		String getName();

		/**
		 * @param string
		 */
		void fail(String string);

		/**
		 * @param success
		 */
		void assertTrue(boolean success);

		/**
		 * @param o
		 */
		void assertNotNull(Object o);

		/**
		 * @return
		 */
		Class<?> getTestClass();

	}

	// Keep track of services and corresponding service references.
	private final Map<Object, ServiceReference> services = new HashMap<Object, ServiceReference>();
	// Do not access this field directly! Use the getter getContext() because this does a lazy initialization.
	private BundleContext context;

	private final ExtensionRegistryChangeJobTracker jobTracker = new ExtensionRegistryChangeJobTracker();
	private final TestCaseWrapper testCase;
	private final boolean trace;

	/**
	 * @param testClass
	 * 
	 */
	TestingTools(final TestCaseWrapper testCase) {
		this.testCase = testCase;
		trace = Trace.isOn(RienaTestCase.class, testCase.getTestClass(), "debug"); //$NON-NLS-1$
	}

	/**
	 * A counterpart to Assert.fail() that may be invoked to indicate that everything is fine and that the test should continue. May be used e.g. in an
	 * otherwise empty catch block that handles an expected exception. In this use case its advantages over a comment are that it allows a more uniform way of
	 * documentation than the numerous variations of "// ignore" and that it avoids a Checkstyle warning about the empty block.
	 */
	protected void ok() {
		// nothing to do, everything is OK...
	}

	/**
	 * A counterpart to Assert.fail(String) that may be invoked to indicate that everything is fine and that the test should continue.
	 * 
	 * @see #ok()
	 * 
	 * @param message
	 *            A message explaining why nothing is wrong.
	 */
	public void ok(final String message) {
		ok();
	}

	void setUp() {
		services.clear();
	}

	void tearDown() {
		for (final ServiceReference reference : services.values()) {
			getContext().ungetService(reference);
		}
		services.clear();
	}

	/**
	 * Return the bundle context. <br>
	 * <b>Note: </b>This method must not be called from a constructor of a test case!
	 * 
	 * @return
	 */
	public BundleContext getContext() {
		if (context == null) {
			try {
				final Bundle bundle = FrameworkUtil.getBundle(testCase.getTestClass());
				context = bundle.getBundleContext();
			} catch (final Throwable t) {
				Nop.reason("We don´t care. Maybe it is not running as a plugin test."); //$NON-NLS-1$
			}
		}
		return context;
	}

	/**
	 * Get the resource located in the folder along with the test case.
	 * <p>
	 * <b>Note:</b> The resource will be copied into a temporary file and this file will be returned. This file will be deleted on JVM exit.
	 * 
	 * @param resource
	 * @return a file to the content of the resource
	 */
	@IgnoreFindBugs(value = { "UI_INHERITANCE_UNSAFE_GETRESOURCE" }, justification = "testCase.getTestClass().getResource() shall return the resource relative to the sub-class, because this is the unit test which needs the 'local' resource.")
	public File getFile(final String resource) {
		final URL url = testCase.getTestClass().getResource(resource);
		try {
			final File tempFile = File.createTempFile(resource.replace('.', '-'), ".tmp"); //$NON-NLS-1$
			tempFile.deleteOnExit();
			IOUtils.copy(url.openStream(), new FileOutputStream(tempFile));
			return tempFile;
		} catch (final IOException e) {
			testCase.fail("IOException when trying to make a copy of " + resource + ": " + e); //$NON-NLS-1$ //$NON-NLS-2$
			return null; // make compiler happy
		}
	}

	/**
	 * Check whether trace is switched on or not.
	 * 
	 * @return tracing?
	 */
	public boolean isTrace() {
		return trace;
	}

	/**
	 * Print the current test´s name.
	 */
	public void printTestName() {
		if (!isTrace()) {
			return;
		}
		System.out.println(testCase.getName());
		for (int i = 0; i < testCase.getName().length(); i++) {
			System.out.print('-');
		}
		System.out.println();
	}

	/**
	 * Print the string, no CR/LF.
	 * 
	 * @param string
	 */
	public void print(final String string) {
		if (!isTrace()) {
			return;
		}
		System.out.print(string);
	}

	/**
	 * Print the string, with CR/LF.
	 * 
	 * @param string
	 */
	public void println(final String string) {
		if (!isTrace()) {
			return;
		}
		System.out.println(string);
	}

	/**
	 * Add an extension/extension point defined within the ´plugin.xml´ (located along side the test class) given with the <code>pluginResource</code> to the
	 * extension registry.
	 * 
	 * @param pluginResource
	 * @throws InterruptedException
	 */
	public void addPluginXml(final String pluginResource) {
		addPluginXml(testCase.getTestClass(), pluginResource);
	}

	/**
	 * Add an extension/extension point defined within the ´plugin.xml´ given with the <code>pluginResource</code> to the extension registry.
	 * 
	 * @param forLoad
	 * @param pluginResource
	 * @throws InterruptedException
	 */
	public void addPluginXml(final Class<?> forLoad, final String pluginResource) {
		System.out.println("TestingTools.addPluginXml()#1");
		final IExtensionRegistry registry = RegistryFactory.getRegistry();
		System.out.println("TestingTools.addPluginXml()#2");
		@IgnoreFindBugs(value = "OBL_UNSATISFIED_OBLIGATION", justification = "stream will be closed by getResourceAsStream()")
		final InputStream inputStream = forLoad.getResourceAsStream(pluginResource);
		System.out.println("TestingTools.addPluginXml()#3");
		final IContributor contributor = ContributorFactoryOSGi.createContributor(getContext().getBundle());
		System.out.println("TestingTools.addPluginXml()#4");

		startJobTracking();
		System.out.println("TestingTools.addPluginXml()#5");
		final boolean success = registry.addContribution(inputStream, contributor, false, pluginResource, null,
				((ExtensionRegistry) registry).getTemporaryUserToken());
		stopJobTracking();
		testCase.assertTrue(success);
		joinTrackedJobs();
	}

	private void startJobTracking() {
		jobTracker.start();
	}

	private void stopJobTracking() {
		jobTracker.stop();
	}

	private void joinTrackedJobs() {
		jobTracker.joinJobs();
	}

	private class ExtensionRegistryChangeJobTracker extends JobChangeAdapter {

		private List<Job> jobs;

		protected void start() {
			jobs = new ArrayList<Job>();
			Job.getJobManager().addJobChangeListener(this);
		}

		protected void stop() {
			Job.getJobManager().removeJobChangeListener(this);
		}

		protected void joinJobs() {
			for (final Job job : jobs) {
				try {
					System.out.println("TestingTools.ExtensionRegistryChangeJobTracker.joinJobs() BEFORE: " + job.toString());
					job.join();
					System.out.println("TestingTools.ExtensionRegistryChangeJobTracker.joinJobs() AFTER: " + job.toString());
				} catch (final InterruptedException e) {
					throw new MurphysLawFailure("Joining jobs failed", e); //$NON-NLS-1$
				}
			}
		}

		@Override
		public void scheduled(final IJobChangeEvent event) {
			if (event.getJob() instanceof ExtensionEventDispatcherJob) {
				jobs.add(event.getJob());
			}
		}
	}

	/**
	 * Remove the given extension from the extension registry.
	 * 
	 * @param extensionId
	 */
	public void removeExtension(final String extensionId) {
		final IExtensionRegistry registry = RegistryFactory.getRegistry();
		final IExtension extension = registry.getExtension(extensionId);
		testCase.assertNotNull(extension);
		startJobTracking();
		final boolean success = registry.removeExtension(extension, ((ExtensionRegistry) registry).getTemporaryUserToken());
		stopJobTracking();
		testCase.assertTrue(success);
		joinTrackedJobs();
	}

	/**
	 * Remove the given extension from the extension registry.
	 * 
	 * @param extensionPointId
	 */
	public void removeExtensionPoint(final String extensionPointId) {
		final IExtensionRegistry registry = RegistryFactory.getRegistry();
		final IExtensionPoint extensionPoint = registry.getExtensionPoint(extensionPointId);
		testCase.assertNotNull(extensionPoint);
		startJobTracking();
		final boolean success = registry.removeExtensionPoint(extensionPoint, ((ExtensionRegistry) registry).getTemporaryUserToken());
		stopJobTracking();
		testCase.assertTrue(success);
		joinTrackedJobs();
	}

	/**
	 * Get the service for the specified <code>serviceClass</code>.
	 * 
	 * @param serviceClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getService(final Class<T> serviceClass) {
		final ServiceReference reference = getContext().getServiceReference(serviceClass.getName());
		if (reference == null) {
			return null;
		}
		final Object service = getContext().getService(reference);
		if (service == null) {
			return null;
		}
		services.put(service, reference);
		return (T) service;
	}

	/**
	 * Unget the specified <code>service</code>.
	 * 
	 * @param service
	 */
	public void ungetService(final Object service) {
		final ServiceReference reference = services.get(service);
		if (reference == null) {
			return;
		}
		getContext().ungetService(reference);
	}

	/**
	 * Starts the bundle with the given <code>bundleName</code>.
	 * 
	 * @param bundleName
	 * @throws BundleException
	 */
	public void startBundle(final String bundleName) throws BundleException {
		startBundles(bundleName.replaceAll("\\.", "\\\\."), null); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Starts all bundles that match the <code>includePattern</code> but not the <code>excludePattern</code>. The <code>excludePattern</code> may be
	 * <code>null</code>.
	 * 
	 * @param includePattern
	 * @param excludePattern
	 * @throws BundleException
	 */
	public void startBundles(final String includePattern, final String excludePattern) throws BundleException {
		doWithBundles(includePattern, excludePattern, new IClosure() {

			public void execute(final Bundle bundle) throws BundleException {
				if (bundle.getState() == Bundle.RESOLVED || bundle.getState() == Bundle.STARTING /*
																								 * STARTING == LAZY
																								 */) {
					bundle.start();
				} else {
					if (bundle.getState() == Bundle.INSTALLED) {
						throw new RuntimeException("can't start required bundle because it is not RESOLVED but only INSTALLED : " //$NON-NLS-1$
								+ bundle.getSymbolicName());
					}
				}
			}
		});
	}

	/**
	 * Stops the bundle with the given <code>bundleName</code>.
	 * 
	 * @param bundleName
	 * @throws BundleException
	 */
	public void stopBundle(final String bundleName) throws BundleException {
		stopBundles(bundleName.replaceAll("\\.", "\\\\."), null); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Stops all bundles that match the <code>includePattern</code> but not the <code>excludePattern</code>. The <code>excludePattern</code> may be
	 * <code>null</code>.
	 * 
	 * @param includePattern
	 * @param excludePattern
	 * @throws BundleException
	 */
	public void stopBundles(final String includePattern, final String excludePattern) throws BundleException {
		doWithBundles(includePattern, excludePattern, new IClosure() {

			public void execute(final Bundle bundle) throws BundleException {
				if (bundle.getState() == Bundle.ACTIVE) {
					bundle.stop();
				} else {
					if (bundle.getState() != Bundle.UNINSTALLED) {
						Nop.reason("testcase tried to stop this bundle which did not run, but we can ignore this ==> bundle is stopped already"); //$NON-NLS-1$
					}
				}
			}
		});
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
	public void doWithBundles(final String includePattern, String excludePattern, final IClosure closure) throws BundleException {
		if (includePattern == null) {
			throw new UnsupportedOperationException("truePattern must be set"); //$NON-NLS-1$
		}
		if (excludePattern == null) {
			excludePattern = ""; //$NON-NLS-1$
		}
		final Pattern include = Pattern.compile(includePattern);
		final Pattern exclude = Pattern.compile(excludePattern);

		final Bundle[] bundles = getContext().getBundles();
		for (final Bundle bundle : bundles) {
			if (include.matcher(bundle.getSymbolicName()).matches() && !(exclude.matcher(bundle.getSymbolicName()).matches())) {
				closure.execute(bundle);
			}
		}
	}
}
