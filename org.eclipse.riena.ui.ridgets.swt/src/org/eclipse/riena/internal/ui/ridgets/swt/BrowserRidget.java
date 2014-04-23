/*******************************************************************************
 * Copyright © 2009, 2011 Florian Pirchner and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Florian Pirchner – initial API and implementation (based on other ridgets of
 *                    compeople AG)
 * compeople AG     - adjustments for Riena v1.2 - 3.0
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;

import org.eclipse.riena.core.util.ListenerList;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.IBrowserRidget;
import org.eclipse.riena.ui.ridgets.listener.ILocationListener;
import org.eclipse.riena.ui.ridgets.swt.AbstractValueRidget;
import org.eclipse.riena.ui.ridgets.swt.BasicMarkerSupport;
import org.eclipse.riena.ui.swt.facades.BrowserFacade;

/**
 * Ridget for an SWT {@link Browser} widget.
 * <p>
 * Implementation note: because of SWT <a href="http://bugs.eclipse.org/84532">Bug 84532</a> the {@link #setFocusable(boolean)} methods has no effect.
 * 
 * @since 1.2
 */
public class BrowserRidget extends AbstractValueRidget implements IBrowserRidget {
	private static final String ABOUT_BLANK = "about:blank"; //$NON-NLS-1$

	/**
	 * This property is used by the databinding to sync ridget and model. It is always fired before its sibling {@link IBrowserRidget#PROPERTY_URL} to ensure
	 * that the model is updated before any listeners try accessing it.
	 * <p>
	 * This property is not API. Do not use in client code.
	 */
	private static final String PROPERTY_URL_INTERNAL = "urlInternal"; //$NON-NLS-1$

	private final InternalLocationListener internalLocationListener;
	private final Map<String, IBrowserRidgetFunction> scriptFunctionMappings;

	private String url;
	private String text;

	private final Map<String, BrowserFunction> browserFunctions;

	public BrowserRidget() {
		internalLocationListener = new InternalLocationListener();
		scriptFunctionMappings = Collections.synchronizedMap(new HashMap<String, IBrowserRidgetFunction>());
		browserFunctions = new HashMap<String, BrowserFunction>();
	}

	@Override
	protected void checkUIControl(final Object uiControl) {
		checkType(uiControl, Browser.class);
	}

	@Override
	protected void bindUIControl() {
		final Browser control = getUIControl();
		if (control != null) {
			updateUIControl();
			control.addLocationListener(internalLocationListener);

			for (final Entry<String, IBrowserRidgetFunction> mapping : scriptFunctionMappings.entrySet()) {
				final BrowserFunction swtBrowerFunction = addSWTBrowerFunction(mapping.getKey(), mapping.getValue());
				browserFunctions.put(mapping.getKey(), swtBrowerFunction);
			}
		}
	}

	@Override
	protected void unbindUIControl() {
		final Browser control = getUIControl();
		if (control != null) {
			control.removeLocationListener(internalLocationListener);
		}
		disposeBrowserFunctions();
		super.unbindUIControl();
	}

	private void disposeBrowserFunctions() {
		for (final BrowserFunction browserFunction : browserFunctions.values()) {
			browserFunction.dispose();
		}
		browserFunctions.clear();
	}

	@Override
	protected AbstractMarkerSupport createMarkerSupport() {
		return new BasicMarkerSupport(this, propertyChangeSupport);
	}

	@Override
	protected IObservableValue getRidgetObservable() {
		return BeansObservables.observeValue(this, PROPERTY_URL_INTERNAL);
	}

	public void addLocationListener(final ILocationListener listener) {
		internalLocationListener.addLocationListener(listener);
	}

	@Override
	public Browser getUIControl() {
		return (Browser) super.getUIControl();
	}

	public String getText() {
		return text;
	}

	public String getUrl() {
		return url;
	}

	/**
	 * This method is not API. Do not use in client code.
	 * 
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public final String getUrlInternal() {
		return getUrl();
	}

	/**
	 * Always returns true because mandatory markers do not make sense for this ridget.
	 */
	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
	}

	public void removeLocationListener(final ILocationListener listener) {
		internalLocationListener.removeLocationListener(listener);
	}

	public void setText(final String text) {
		if (!StringUtils.equals(this.text, text)) {
			this.text = text;
			final String oldUrl = this.url;
			this.url = null;
			updateUIControl();
			firePropertyChange(PROPERTY_URL_INTERNAL, oldUrl, this.url);
			firePropertyChange(IBrowserRidget.PROPERTY_URL, oldUrl, this.url);
		}
	}

	public void setUrl(final String url) {
		if (!StringUtils.equals(this.url, url)) {
			final String oldUrl = this.getUrl();
			this.text = null;
			this.url = url;
			updateUIControl();
			firePropertyChange(PROPERTY_URL_INTERNAL, oldUrl, this.url);
			firePropertyChange(IBrowserRidget.PROPERTY_URL, oldUrl, this.url);
		}
	}

	/**
	 * This method is not API. Do not use in client code.
	 * <p>
	 * Do not remove - used by the data binding.
	 * 
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public final void setUrlInternal(final String url) {
		setUrl(url);
	}

	// helping methods
	//////////////////

	/**
	 * Note that this method does not guarantee the result validity. If an invalid (URL) and non-empty parameter is passed, then it will simply be returned and
	 * remain invalid.
	 */
	private String convertBlankToValid(final String string) {
		return StringUtils.isDeepEmpty(string) ? ABOUT_BLANK : string;
	}

	private void updateUIControl() {
		final Browser browser = getUIControl();
		if (browser != null) {
			if (text != null) {
				final String browserText = BrowserFacade.getDefault().getText(browser);
				if (!text.equals(browserText)) {
					internalLocationListener.unblock();
					browser.setText(text);
				}
			} else {
				final String urlToSet = convertBlankToValid(url);
				if (!urlToSet.equals(browser.getUrl())) {
					internalLocationListener.unblock();
					browser.setUrl(urlToSet);
				}
			}
		}
	}

	// helping classes
	//////////////////

	/**
	 * Listens to location changes in the Browser widget and update's the Ridget's URL if necessary.
	 */
	private final class InternalLocationListener implements LocationListener {

		private ListenerList<ILocationListener> listeners;
		private boolean canBlock;

		InternalLocationListener() {
			canBlock = true;
		}

		void addLocationListener(final ILocationListener listener) {
			Assert.isNotNull(listener);
			if (listeners == null) {
				listeners = new ListenerList<ILocationListener>(ILocationListener.class);
			}
			listeners.add(listener);
		}

		void removeLocationListener(final ILocationListener listener) {
			if (listeners != null) {
				listeners.remove(listener);
			}
		}

		/**
		 * Allow the next url-change, even if output-only marker is set.
		 * <p>
		 * This is used by updateUIControl() to permit updating a widget on rebind, setText, setUrl.
		 * <p>
		 * Implementation notes: {@link #changing(LocationEvent)} is invoked an undefined time after {@link #unblock()}, since the page load happens
		 * asynchronously. Currently there is no synchronisation build in - we simply allow the next change. This is not likely to cause problems, however it
		 * could allow another change to happen, if it is processed before the intended LocationEvent. The event.location value is formatted by the browser and
		 * may have things added (parameters, http://www prefix) so checking for BrowserRidget.url equality is not an option for identifying which url to
		 * unblock.
		 */
		void unblock() {
			canBlock = false;
		}

		public void changing(final LocationEvent event) {
			if (canBlock) {
				if (isOutputOnly()) {
					event.doit = false;
				}
				if (listeners != null && event.doit) {
					for (final ILocationListener listener : listeners) {
						final org.eclipse.riena.ui.ridgets.listener.LocationEvent locEvent = new org.eclipse.riena.ui.ridgets.listener.LocationEvent(
								event.location, event.doit, event.top);
						event.doit &= listener.locationChanging(locEvent);
					}
				}
			}
			canBlock = true;
		}

		public void changed(final LocationEvent event) {
			if (event.top && !isNullOrAboutBlank(event.location)) {
				if (!StringUtils.equals(url, event.location)) {
					setUrl(event.location);
					if (listeners != null) {
						final org.eclipse.riena.ui.ridgets.listener.LocationEvent locEvent = new org.eclipse.riena.ui.ridgets.listener.LocationEvent(
								event.location, event.doit, event.top);
						for (final ILocationListener listener : listeners) {
							listener.locationChanged(locEvent);
						}
					}
				}
			}
		}

		private boolean isNullOrAboutBlank(final String url) {
			return url == null || "about:blank".equals(url); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IBrowserRidget#execute(java.lang.String)
	 */
	@Override
	public boolean execute(final String script) {
		if (getUIControl() != null) {
			return getUIControl().execute(script);
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IBrowserRidget#bindScriptFunction(java.lang.String, java.lang.Object)
	 */
	@Override
	public void mapScriptFunction(final String functionName, final IBrowserRidgetFunction function) {
		scriptFunctionMappings.put(functionName, function);
		if (getUIControl() != null) {
			final BrowserFunction swtBrowerFunction = addSWTBrowerFunction(functionName, function);
			browserFunctions.put(functionName, swtBrowerFunction);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IBrowserRidget#unmapScriptFunction(java.lang.String)
	 */
	@Override
	public void unmapScriptFunction(final String functionName) {
		if (scriptFunctionMappings.remove(functionName) != null && getUIControl() != null) {
			browserFunctions.remove(functionName).dispose();
		}
	}

	private BrowserFunction addSWTBrowerFunction(final String functionName, final IBrowserRidgetFunction controller) {
		return new BrowserFunction(getUIControl(), functionName) {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.browser.BrowserFunction#function(java.lang.Object[])
			 */
			@Override
			public Object function(final Object[] arguments) {
				return controller.execute(arguments);
			}
		};
	}
}
