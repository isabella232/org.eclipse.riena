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
package org.eclipse.riena.monitor.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.RienaLocations;
import org.eclipse.riena.core.RienaStatus;
import org.eclipse.riena.core.logging.ConsoleLogger;
import org.eclipse.riena.core.util.CipherUtils;
import org.eclipse.riena.core.util.IOUtils;
import org.eclipse.riena.core.util.Literal;
import org.eclipse.riena.core.util.Millis;
import org.eclipse.riena.core.util.PropertiesUtils;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.core.util.VariableManagerUtil;
import org.eclipse.riena.internal.monitor.client.Activator;
import org.eclipse.riena.monitor.common.Collectible;

/**
 * This simple store implements a file based {@code IStore} for the client
 * monitoring. The store location is either given explicitly with the
 * configuration data or if not it stores its data in the data area of riena.
 * <p>
 * The simple store expects the following configuration that can be passed with
 * its definition in an extension:
 * <ul>
 * <li>cleanupDelay - defines the period between store cleanup steps (default
 * value is 1 hour if not defined)</li>
 * <li>storePath - an optional file path for the store (default value is within
 * the riena data area). The {@code VarableManagerUtil} will perform a string
 * substitution on storePath.</li>
 * </ul>
 * Periods of time can be specified as a string conforming to
 * {@link Millis#valueOf(String)}.<br>
 * Example extension:
 * 
 * <pre>
 * &lt;extension point=&quot;org.eclipse.riena.monitor.client.store&quot;&gt;
 *     &lt;store
 *           name=&quot;SimpleStore&quot;
 *           class=&quot;org.eclipse.riena.monitor.client.SimpleStore:cleanupDelay=120 s&quot;&gt;
 *     &lt;/store&gt;
 * &lt;/extension&gt;
 * </pre>
 */
public class SimpleStore implements IStore, IExecutableExtension {

	private File storeFolder;
	private long cleanupDelay;
	private String storePathName;
	private Cleaner cleaner;
	private Map<String, Category> categories = new HashMap<String, Category>();
	private Cipher encrypt;
	private Cipher decrypt;

	private static final String TRANSFER_FILE_EXTENSION = ".trans"; //$NON-NLS-1$
	private static final String COLLECT_FILE_EXTENSION = ".coll"; //$NON-NLS-1$
	private static final String DEL_FILE_EXTENSION = ".del"; //$NON-NLS-1$
	private static final String CATEGORY_DELIMITER = "#"; //$NON-NLS-1$

	private static final String CLEANUP_DELAY = "cleanupDelay"; //$NON-NLS-1$
	private static final String STORE_PATH = "storePath"; //$NON-NLS-1$
	private static final String CLEANUP_DELAY_DEFAULT = "1 h"; //$NON-NLS-1$
	private static final byte[] KEY = new byte[8];

	static {
		final long first = "This is not very clever :-)".hashCode(); //$NON-NLS-1$
		final long second = "And this neither!".hashCode(); //$NON-NLS-1$
		new Random(first * second).nextBytes(KEY);
	}

	private static final Logger LOGGER = Log4r.getLogger(Activator.getDefault(), SimpleStore.class);

	public void setInitializationData(final IConfigurationElement config, final String propertyName, final Object data)
			throws CoreException {
		Map<String, String> properties = null;
		try {
			properties = PropertiesUtils.asMap(data, Literal.map(CLEANUP_DELAY, CLEANUP_DELAY_DEFAULT));
			cleanupDelay = Millis.valueOf(properties.get(CLEANUP_DELAY));
			Assert.isLegal(cleanupDelay > 0, "cleanupDelay must be greater than 0."); //$NON-NLS-1$
			storePathName = VariableManagerUtil.substitute(properties.get(STORE_PATH));
		} catch (final IllegalArgumentException e) {
			throw configurationException("Bad configuration.", e); //$NON-NLS-1$
		}
		initStore();
		cleaner = new Cleaner();
	}

	private CoreException configurationException(final String message, final Exception e) {
		return new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, e));
	}

	private void initStore() {
		storeFolder = StringUtils.isGiven(storePathName) ? new File(storePathName) : new File(
				RienaLocations.getDataArea(Activator.getDefault().getBundle()), "simplestore"); //$NON-NLS-1$
		if (!storeFolder.isDirectory()) {
			final boolean directoryCreated = storeFolder.mkdirs();
			Assert.isTrue(directoryCreated);
		}
		try {
			encrypt = CipherUtils.getCipher(KEY, Cipher.ENCRYPT_MODE);
			decrypt = CipherUtils.getCipher(KEY, Cipher.DECRYPT_MODE);
		} catch (final GeneralSecurityException e) {
			throw new IllegalArgumentException("Could not generate keys for encryption.", e); //$NON-NLS-1$
		}
		LOGGER.log(LogService.LOG_DEBUG, "SimpleStore at " + storeFolder); //$NON-NLS-1$
		if (RienaStatus.isDevelopment()) {
			// This only for debugging/development so that old files do not bother us
			LOGGER.log(LogService.LOG_DEBUG, "SimpleStore in development mode, trying to clean-up store."); //$NON-NLS-1$
			for (final File file : storeFolder.listFiles()) {
				if (!file.delete()) {
					LOGGER.log(LogService.LOG_DEBUG, " - failed deleting file: " + file); //$NON-NLS-1$
					file.deleteOnExit();
				}
			}
		}
	}

	public void open(final Map<String, Category> categories) {
		Assert.isNotNull(categories, "categories must not be null"); //$NON-NLS-1$
		this.categories = categories;
		cleaner.schedule(Millis.seconds(15));
	}

	public void close() {
		cleaner.cancel();
	}

	public void flush() {
		// nothing to do here
	}

	public synchronized boolean collect(final Collectible<?> collectible) {
		final File file = getFile(collectible, COLLECT_FILE_EXTENSION);
		putCollectible(collectible, file);

		return true;
	}

	public synchronized void prepareTransferables(final String category) {
		final File[] trans = storeFolder.listFiles(new FilenameFilter() {

			public boolean accept(final File dir, final String name) {
				return name.startsWith(category) && name.endsWith(COLLECT_FILE_EXTENSION);
			}
		});
		for (final File file : trans) {
			final String transferName = file.getName().replace(COLLECT_FILE_EXTENSION, TRANSFER_FILE_EXTENSION);
			final File transfer = new File(file.getParent(), transferName);
			if (transfer.exists()) {
				transfer.delete();
			}
			if (!file.renameTo(transfer)) {
				new ConsoleLogger(SimpleStore.class.getName()).log(LogService.LOG_WARNING,
						"Could not rename " + file + " to " + transfer + "."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}
	}

	public synchronized List<Collectible<?>> retrieveTransferables(final String category) {
		final File[] transferables = storeFolder.listFiles(new FilenameFilter() {

			public boolean accept(final File dir, final String name) {
				return name.startsWith(category) && name.endsWith(TRANSFER_FILE_EXTENSION);
			}
		});
		final List<Collectible<?>> collectibles = new ArrayList<Collectible<?>>();
		for (final File transferable : transferables) {
			final Collectible<?> collectible = getCollectible(transferable);
			if (collectible != null) {
				collectibles.add(collectible);
			}
		}
		return collectibles;
	}

	/**
	 * Get the decryptor for storing the collectibles.
	 * <p>
	 * <b>Note: </b>This hook method is intended to be overwritten to provide a
	 * better encrypted storage on the local file system on the client.
	 * Otherwise a simple encryption will be used.
	 * 
	 * @param is
	 * @return
	 */
	protected InputStream getDecryptor(final InputStream is) {
		return new CipherInputStream(is, decrypt);
	}

	/**
	 * 
	 * Get the encryptor for retrieving the collectibles.
	 * <p>
	 * <b>Note: </b>This hook method is intended to be overwritten to provide a
	 * better encrypted storage on the local file system on the client.
	 * Otherwise a simple encryption will be used.
	 * 
	 * @param os
	 * @return
	 */
	protected OutputStream getEncryptor(final OutputStream os) {
		return new CipherOutputStream(os, encrypt);
	}

	/**
	 * Get the compressor for storing the collectibles.
	 * <p>
	 * <b>Note: </b>This hook method may be overwritten to provide another
	 * compressing technology. This method uses
	 * DeflaterOutputStream/InputStream. <br>
	 * See also <a
	 * href="http://www.thatsjava.com/java-enterprise/44517/">GZIP/Cipher
	 * problem</a>
	 * 
	 * @param os
	 * @return
	 * @throws IOException
	 */
	protected OutputStream getCompressor(final OutputStream os) throws IOException {
		return new DeflaterOutputStream(os);
	}

	/**
	 * 
	 * Get the decompressor for retrieving the collectibles.
	 * <p>
	 * <b>Note: </b>This hook method is intended to be overwritten to provide a
	 * better encrypted storage on the local file system on the client.
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	protected InputStream getDecompressor(final InputStream is) throws IOException {
		return new InflaterInputStream(is);
	}

	public synchronized void commitTransferred(final List<Collectible<?>> collectibles) {
		for (final Collectible<?> collectible : collectibles) {
			delete(getFile(collectible, TRANSFER_FILE_EXTENSION));
		}
	}

	/**
	 * Try to delete the given file. If it is not deletable mark it deletable
	 * and try to delete on jvm exit.
	 * 
	 * @param file
	 */
	private void delete(final File file) {
		if (!file.delete()) {
			if (file.getName().endsWith(DEL_FILE_EXTENSION)) {
				file.deleteOnExit();
				return;
			}
			final File toDelete = new File(file, DEL_FILE_EXTENSION);
			if (file.renameTo(toDelete)) {
				toDelete.deleteOnExit();
			}
		}
	}

	/**
	 * Get collectible from file.
	 * 
	 * @param file
	 * @return
	 */
	private Collectible<?> getCollectible(final File file) {
		ObjectInputStream objectis = null;
		InputStream fis = null;
		try {
			fis = new FileInputStream(file);
			final InputStream decris = getDecryptor(fis);
			final InputStream gzipis = getDecompressor(decris);
			objectis = new ObjectInputStream(gzipis);
			return (Collectible<?>) objectis.readObject();
		} catch (final Exception e) {
			IOUtils.close(fis);
			LOGGER.log(LogService.LOG_DEBUG, "Error retrieving collectible: " + e.getMessage(), e); //$NON-NLS-1$
			if (file.exists() && !file.delete()) {
				file.deleteOnExit();
			}
			return null;
		} finally {
			IOUtils.close(objectis);
		}
	}

	/**
	 * Store collectible into file.
	 * 
	 * @param collectible
	 * @param file
	 */
	private void putCollectible(final Collectible<?> collectible, final File file) {
		ObjectOutputStream objectos = null;
		OutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			final OutputStream encos = getEncryptor(fos);
			final OutputStream gzipos = getCompressor(encos);
			objectos = new ObjectOutputStream(gzipos);
			objectos.writeObject(collectible);
		} catch (final IOException e) {
			IOUtils.close(fos);
			LOGGER.log(LogService.LOG_DEBUG, "Error storing collectible: " + e.getMessage(), e); //$NON-NLS-1$
			if (file.exists() && !file.delete()) {
				file.deleteOnExit();
			}
		} finally {
			IOUtils.close(objectos);
		}
	}

	private File getFile(final Collectible<?> collectible, final String extension) {
		return new File(storeFolder, collectible.getCategory() + CATEGORY_DELIMITER + collectible.getUUID().toString()
				+ extension);
	}

	/**
	 * Perform cleanup of the store as defined by the configuration properties.
	 */
	private class Cleaner extends Job {

		public Cleaner() {
			super("SimpleStoreCleaner"); //$NON-NLS-1$
			setUser(false);
			final Bundle bundle = FrameworkUtil.getBundle(Cleaner.class);
			if (bundle != null) {
				bundle.getBundleContext().addBundleListener(new StoppingListener(bundle));
			}
		}

		@Override
		protected IStatus run(final IProgressMonitor monitor) {
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}
			LOGGER.log(LogService.LOG_DEBUG, "Store Cleaner started"); //$NON-NLS-1$
			monitor.beginTask("Cleanup", IProgressMonitor.UNKNOWN); //$NON-NLS-1$
			clean();
			monitor.done();
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}
			// reschedule for periodic work
			schedule(cleanupDelay);
			LOGGER.log(LogService.LOG_DEBUG, "Store Cleaner ended"); //$NON-NLS-1$
			return Status.OK_STATUS;
		}

		private void clean() {
			final File[] scrutinizedFiles = storeFolder.listFiles(new FilenameFilter() {
				public boolean accept(final File dir, final String name) {
					return name.endsWith(COLLECT_FILE_EXTENSION) || name.endsWith(DEL_FILE_EXTENSION);
				}
			});
			if (scrutinizedFiles == null || scrutinizedFiles.length == 0) {
				return;
			}
			final Map<String, List<File>> categorizedScrutinized = new HashMap<String, List<File>>(
					scrutinizedFiles.length);
			for (final File scrutinizedFile : scrutinizedFiles) {
				final int categoryDelimiterIndex = scrutinizedFile.getName().indexOf(CATEGORY_DELIMITER);
				if (categoryDelimiterIndex == -1) {
					continue;
				}
				final String category = scrutinizedFile.getName().substring(0, categoryDelimiterIndex);
				List<File> files = categorizedScrutinized.get(category);
				if (files == null) {
					files = new ArrayList<File>();
					categorizedScrutinized.put(category, files);
				}
				files.add(scrutinizedFile);
			}
			for (final Map.Entry<String, List<File>> entry : categorizedScrutinized.entrySet()) {
				clean(entry.getValue(), categories.get(entry.getKey()).getMaxItems());
			}
		}

		private void clean(final List<File> files, final int maxItems) {
			if (files.size() < maxItems) {
				return;
			}
			// sort by time, so that we remove older ones
			Collections.sort(files, new Comparator<File>() {
				public int compare(final File file1, final File file2) {
					final Long time1 = file1.lastModified();
					final Long time2 = file2.lastModified();
					return time1.compareTo(time2);
				}
			});
			for (int i = 0; i < files.size() - maxItems; i++) {
				delete(files.get(i));
			}
		}

		private class StoppingListener implements SynchronousBundleListener {
			private final Bundle bundle;

			public StoppingListener(final Bundle bundle) {
				this.bundle = bundle;
			}

			public void bundleChanged(final BundleEvent event) {
				if (event.getBundle() == bundle && event.getType() == BundleEvent.STOPPING) {
					bundle.getBundleContext().removeBundleListener(this);
					cancel();
				}
			}
		}

	}

}
