package org.eclipse.riena.ui.javafx.utils;

import javafx.scene.Node;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.Trace;
import org.eclipse.riena.internal.ui.javafx.Activator;
import org.osgi.service.log.LogService;

public class JavaFxNodeIdentificationSupport {

	public static final String RIENA_ID = "rienaid"; //$NON-NLS-1$
	private static final Logger LOGGER = Log4r.getLogger(
			Activator.getDefault(), JavaFxNodeIdentificationSupport.class);
	private static boolean debugOutput = Trace.isOn(
			JavaFxNodeIdentificationSupport.class, "debug"); //$NON-NLS-1$

	private JavaFxNodeIdentificationSupport() {
		// utility class
	}

	// TODO (?)
	// /**
	// * Sets rienaid for shell. If 'riena.testing.widgetid.mainshell' system
	// * property is set - it's value if used. otherwise rienaid is set to value
	// * 'default'
	// *
	// * @param aShell
	// * shell
	// */
	// public static void setIdentification(final Shell aShell) {
	// aShell.setData(RIENA_ID, System.getProperty(
	//				"riena.testing.widgetid.mainshell", "default")); //$NON-NLS-1$ //$NON-NLS-2$
	// }

	/**
	 * Sets rienaid for JavaFx Node. dot-separated concatenated id parts are
	 * used as a value. i.e. parts: ['a', 'b', 'c'], value: 'a.b.c'
	 * 
	 * @param node
	 *            JavaFx Node
	 * @param aParts
	 *            sequence of id parts
	 */
	public static void setIdentification(final Node node,
			final String... aParts) {
		final StringBuilder fullId = new StringBuilder();

		for (final String part : aParts) {
			if (fullId.length() != 0) {
				fullId.append('.');
			}

			fullId.append(part);
		}
		if (debugOutput) {
			LOGGER.log(
					LogService.LOG_DEBUG,
					String.format(
							"registering widget %s, (class: %s)", fullId, node.getClass())); //$NON-NLS-1$
		}
		node.getProperties().put(RIENA_ID, fullId.toString());
	}

	/**
	 * Sets default rienaid for a JavaFx Node. String representation of JavaFx
	 * Node class name is used as a value
	 * 
	 * @param node
	 *            JavaFx Node
	 */
	public static void setDefaultIdentification(final Node node) {
		setIdentification(node, node.getClass().getName());
	}

	/**
	 * Returns the Riena ID of the given widget.
	 * 
	 * @param aWidget
	 *            widget
	 * @return Riena ID or {@code null} if it has not been set
	 * @since 5.0
	 */
	public static String getIdentification(final Node node) {
		return (String) node.getProperties().get(RIENA_ID);
	}

}
