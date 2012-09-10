package org.eclipse.riena.ui.javafx.utils;

import java.net.URL;

import javafx.scene.Scene;

import org.eclipse.riena.internal.ui.javafx.Activator;

public final class RienaCssLoader {

	private static RienaCssLoader loader;

	private static final String DEFAULT_CSS = "css/DefaultRiena.css";

	private RienaCssLoader() {
		// prevent instantiation
	}

	/**
	 * Returns an instance of this class.
	 * 
	 * @return
	 */
	public static RienaCssLoader getInstance() {
		if (loader == null) {
			loader = new RienaCssLoader();
		}
		return loader;
	}

	private URL getDefaultCssUrl() {
		URL defaultCssUrl = Activator.getDefault().getBundle()
				.getEntry(DEFAULT_CSS);
		return defaultCssUrl;
	}

	public void addStylesheets(Scene scene) {
		URL defaultCssUrl = getDefaultCssUrl();
		scene.getStylesheets().add(defaultCssUrl.toExternalForm());
	}

}
