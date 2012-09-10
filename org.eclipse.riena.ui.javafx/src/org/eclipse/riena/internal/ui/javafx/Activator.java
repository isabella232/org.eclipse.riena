package org.eclipse.riena.internal.ui.javafx;

import org.eclipse.riena.core.IRienaActivator;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin implements IRienaActivator {

	public static final String PLUGIN_ID = "org.eclipse.riena.ui.javafx"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	@Override
	public void start(BundleContext context) throws Exception {
		plugin = this;
		super.stop(context);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		super.start(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	@Override
	public BundleContext getContext() {
		return getDefault().getContext();
	}

}
