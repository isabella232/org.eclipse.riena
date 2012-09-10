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
package org.eclipse.riena.ui.ridgets.swt;

import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.core.util.ListenerList;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.ErrorMessageMarker;
import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.OutputMarker;
import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.AbstractRidget;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IBasicMarkableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.listener.ClickEvent;
import org.eclipse.riena.ui.ridgets.listener.IClickListener;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingPropertyLocator;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.ImageStore;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Ridget for an SWT widget.
 */
public abstract class AbstractSWTWidgetRidget extends AbstractRidget implements IBasicMarkableRidget {

	private ListenerList<IClickListener> clickListeners;
	private ListenerList<IActionListener> doubleClickListeners;
	private Widget uiControl;
	private String toolTip = null;
	private ErrorMessageMarker errorMarker;
	private DisabledMarker disabledMarker;
	private MandatoryMarker mandatoryMarker;
	private OutputMarker outputMarker;
	private HiddenMarker hiddenMarker;
	private AbstractMarkerSupport markerSupport;
	private Listener visibilityListener;
	private ClickForwarder mouseListener;

	/**
	 * Return true if an instance of the given {@code clazz} is a bean, false otherwise.
	 * <p>
	 * Implementation note: currently an instance is assumed to be a bean, if it has an addPropertyListener(PropertyChangeListener) method.
	 * 
	 * @param clazz
	 *            a non-null class value
	 */
	public static boolean isBean(final Class<?> clazz) {
		try {
			// next line throws NoSuchMethodException, if no matching method found
			clazz.getMethod("addPropertyChangeListener", PropertyChangeListener.class); //$NON-NLS-1$
			return true; // have bean
		} catch (final NoSuchMethodException e) {
			return false; // have pojo
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Adding the same marker twice has no effect.
	 */
	public synchronized final void addMarker(final IMarker marker) {
		if (markerSupport == null) {
			markerSupport = createMarkerSupport();
			markerSupport.setRidget(this);
			unbindMarkerSupport();
		}
		if (marker instanceof MandatoryMarker) {
			((MandatoryMarker) marker).setDisabled(isDisableMandatoryMarker());
		}
		markerSupport.addMarker(marker);
	}

	/**
	 * @since 4.0
	 */
	public void addClickListener(final IClickListener listener) {
		Assert.isNotNull(listener, "listener is null"); //$NON-NLS-1$
		if (clickListeners == null) {
			clickListeners = new ListenerList<IClickListener>(IClickListener.class);
		}
		clickListeners.add(listener);
	}

	/**
	 * @since 4.0
	 */
	public void removeClickListener(final IClickListener listener) {
		if (clickListeners != null) {
			clickListeners.remove(listener);
		}
	}

	/**
	 * @since 4.0
	 */
	public void addDoubleClickListener(final IActionListener listener) {
		Assert.isNotNull(listener, "listener is null"); //$NON-NLS-1$
		if (doubleClickListeners == null) {
			doubleClickListeners = new ListenerList<IActionListener>(IActionListener.class);
		}
		doubleClickListeners.add(listener);
	}

	/**
	 * @since 4.0
	 */
	public void removeDoubleClickListener(final IActionListener listener) {
		if (doubleClickListeners != null) {
			doubleClickListeners.remove(listener);
		}
	}

	public String getID() {
		final IBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		return locator.locateBindingProperty(getUIControl());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.0
	 */
	public Set<Class<IMarker>> getHiddenMarkerTypes() {
		if (markerSupport != null) {
			return markerSupport.getHiddenMarkerTypes();
		}
		return new HashSet<Class<IMarker>>();
	}

	public final Collection<IMarker> getMarkers() {
		if (markerSupport != null) {
			return markerSupport.getMarkers();
		}
		return Collections.emptySet();
	}

	public final <T extends IMarker> Collection<T> getMarkersOfType(final Class<T> type) {
		if (markerSupport != null) {
			return markerSupport.getMarkersOfType(type);
		}
		return Collections.emptySet();
	}

	public final String getToolTipText() {
		return toolTip;
	}

	public Widget getUIControl() {
		return uiControl;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.0
	 */
	public Set<Class<IMarker>> hideMarkersOfType(final Class<? extends IMarker>... types) {
		if (markerSupport == null) {
			markerSupport = createMarkerSupport();
			markerSupport.setRidget(this);
		}
		return markerSupport.hideMarkersOfType(types);
	}

	public boolean hasFocus() {
		return false;
	}

	public boolean isEnabled() {
		return getMarkersOfType(DisabledMarker.class).isEmpty();
	}

	public final boolean isErrorMarked() {
		return !getMarkersOfType(ErrorMarker.class).isEmpty();
	}

	public boolean isFocusable() {
		return false;
	}

	/**
	 * FIXME: this method does not check whether the MandatoryMarkers that it finds might have a disabled flag set (in the marker). We also couldnt find code
	 * that calls this method.
	 * 
	 * @deprecated
	 */
	@Deprecated
	public final boolean isMandatory() {
		return !getMarkersOfType(MandatoryMarker.class).isEmpty();
	}

	public final boolean isOutputOnly() {
		return !getMarkersOfType(OutputMarker.class).isEmpty();
	}

	public final void removeAllMarkers() {
		if (markerSupport != null) {
			markerSupport.removeAllMarkers();
		}
	}

	/**
	 * @since 3.0
	 */
	public final boolean removeMarker(final IMarker marker) {
		if (markerSupport != null) {
			return markerSupport.removeMarker(marker);
		}
		return false;
	}

	public void requestFocus() {
		// not supported
	}

	public synchronized void setEnabled(final boolean enabled) {
		if (enabled) {
			if (disabledMarker != null) {
				removeMarker(disabledMarker);
			}
		} else {
			if (disabledMarker == null) {
				disabledMarker = new DisabledMarker();
			}
			addMarker(disabledMarker);
		}
	}

	public final void setErrorMarked(final boolean errorMarked) {
		setErrorMarked(errorMarked, null);
	}

	public void setFocusable(final boolean focusable) {
		// not supported
	}

	public final void setMandatory(final boolean mandatory) {
		if (!mandatory) {
			if (mandatoryMarker != null) {
				removeMarker(mandatoryMarker);
			}
		} else {
			if (mandatoryMarker == null) {
				mandatoryMarker = new MandatoryMarker();
			}
			addMarker(mandatoryMarker);
		}
	}

	public final void setOutputOnly(final boolean outputOnly) {
		if (!outputOnly) {
			if (outputMarker != null) {
				removeMarker(outputMarker);
			}
		} else {
			if (outputMarker == null) {
				outputMarker = new OutputMarker();
			}
			addMarker(outputMarker);
		}
	}

	public final void setToolTipText(final String toolTipText) {
		final String oldValue = this.toolTip;
		this.toolTip = toolTipText;
		updateToolTip();
		firePropertyChange(PROPERTY_TOOLTIP, oldValue, this.toolTip);
	}

	/*
	 * Do not override. Template Method Pattern: Subclasses may implement {@code unbindUIControl()} and {@code bindUIControl}, if they need to manipulate the
	 * control when it is bound/unbound, for example to add/remove listeners.
	 */
	public final void setUIControl(final Object uiControl) {
		checkUIControl(uiControl);
		uninstallListeners();
		unbindUIControl();
		unbindMarkerSupport();
		if (!SwtUtilities.isDisposed(this.uiControl)) {
			// clean up the old UI control data
			this.uiControl.setData(IRidget.class.getName(), null);
		}
		this.uiControl = (Widget) uiControl;
		if (this.uiControl != null) {
			// set ridget as UI control data
			this.uiControl.setData(IRidget.class.getName(), this);
		}
		bindMarkerSupport();
		updateEnabled();
		updateMarkers();
		updateToolTip();
		bindUIControl();
		installListeners();
	}

	public final void setVisible(final boolean visible) {
		if (hiddenMarker == null) {
			hiddenMarker = new HiddenMarker();
		}

		if (visible) {
			removeMarker(hiddenMarker);
		} else {
			addMarker(hiddenMarker);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.0
	 */
	public Set<Class<IMarker>> showMarkersOfType(final Class<? extends IMarker>... types) {
		if (markerSupport == null) {
			return new HashSet<Class<IMarker>>();
		}
		return markerSupport.showMarkersOfType(types);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IBasicMarkableRidget#decorateVisibleControlArea()
	 */
	/**
	 * @since 5.0
	 */
	public boolean decorateVisibleControlArea() {
		return true;
	}

	// abstract methods - subclasses must implement
	/////////////////////////////////////////////////////////

	/**
	 * Performs checks on the control about to be bound by this ridget.
	 * <p>
	 * Implementors must make sure the given <tt>uiControl</tt> has the expected type.
	 * 
	 * @param uiControl
	 *            a {@link Widget} instance or null
	 * @throws BindingException
	 *             if the <tt>uiControl</tt> fails the check
	 */
	abstract protected void checkUIControl(Object uiControl);

	/**
	 * Bind the current <tt>uiControl</tt> to the ridget.
	 * <p>
	 * Implementors must call {@link #getUIControl()} to obtain the current control. If the control is non-null they must do whatever necessary to bind it to
	 * the ridget.
	 */
	abstract protected void bindUIControl();

	/**
	 * Unbind the current <tt>uiControl</tt> from the ridget.
	 * <p>
	 * Implementors ensure they dispose the control-to-ridget binding and dispose any data structures that are not necessary in an unbound state.
	 */
	abstract protected void unbindUIControl();

	/**
	 * Returns true if mandatory markers should be disabled (i.e. if the ridget has non empty input).
	 */
	// TODO [ev] why is this public?
	abstract public boolean isDisableMandatoryMarker();

	/**
	 * Update the enabled state for the ridget's control, based on the information from {@link #isEnabled()}
	 * <p>
	 * Implementation note: if a ridget has markers, the marker support logic may compute a different enabled state for this widget. The marker support will be
	 * invoked after this method is invoked.
	 * 
	 * @see #setUIControl(Object)
	 */
	abstract protected void updateEnabled();

	/**
	 * Update the tooltip for the ridget's control.
	 */
	abstract protected void updateToolTip();

	// protected methods
	////////////////////

	/**
	 * Creates and initializes the marker support for this Ridget.
	 * <p>
	 * The Look&Feel returns the marker support according of the Look&Feel setting and the type of this Ridget. If the Look&Feel can not return a appropriate
	 * marker support, this method creates and returns the default marker support.
	 * 
	 * @return marker support
	 */
	protected AbstractMarkerSupport createMarkerSupport() {
		AbstractMarkerSupport lnfMarkerSupport = null;
		if (LnfManager.getLnf() != null) {
			lnfMarkerSupport = LnfManager.getLnf().getMarkerSupport(this.getClass());
		}
		if (lnfMarkerSupport == null) {
			// No MarkerSupport exits. Default MarkerSupport is used.
			lnfMarkerSupport = new MarkerSupport();
		}
		lnfMarkerSupport.init(this, propertyChangeSupport);
		Assert.isNotNull(lnfMarkerSupport, "Marker support is null!"); //$NON-NLS-1$
		return lnfMarkerSupport;
	}

	/**
	 * Iterates over the MandatoryMarker instances held by this ridget changing their disabled state to given value.
	 * 
	 * @param disable
	 *            the new disabled state
	 */
	protected final void disableMandatoryMarkers(final boolean disable) {
		for (final MandatoryMarker marker : getMarkersOfType(MandatoryMarker.class)) {
			marker.setDisabled(disable);
		}
	}

	/**
	 * "Flashes" the error marker.
	 * <p>
	 * 
	 * Note: this is designed to be delegated to {@link AbstractMarkerSupport} and may vary depending on the concrete implementation.
	 * 
	 * @since 3.0
	 */
	protected synchronized final void flash() {
		if (getUIControl() != null) {
			if (markerSupport == null) {
				markerSupport = createMarkerSupport();
				markerSupport.setRidget(this);
			}
			markerSupport.flash();
		}
	}

	protected Image getManagedImage(final String key) {
		Image image = ImageStore.getInstance().getImage(key);
		if (image == null) {
			image = ImageStore.getInstance().getMissingImage();
		}
		return image;
	}

	/**
	 * Compares the two given values.
	 * 
	 * @param oldValue
	 *            old value
	 * @param newValue
	 *            new value
	 * @return true, if value has changed; otherwise false
	 */
	protected boolean hasChanged(final Object oldValue, final Object newValue) {
		if (oldValue == null && newValue == null) {
			return false;
		}
		if (oldValue == null || newValue == null) {
			return true;
		}
		return !oldValue.equals(newValue);
	}

	/**
	 * Adds listeners to the <tt>uiControl</tt> after it was bound to the ridget.
	 */
	protected void installListeners() {
		if (getUIControl() != null) {
			if (visibilityListener == null) {
				visibilityListener = new VisibilityListener();
			}
			// TODO [ev] can we move this one class down? there it is always a Control instance
			if (getUIControl() instanceof Control) {
				addHierarchyVisibilityListener(((Control) getUIControl()).getParent(), visibilityListener);
			}
			mouseListener = new ClickForwarder();
			if (getUIControl() instanceof Control) {
				final Control control = (Control) getUIControl();
				control.addMouseListener(mouseListener);
			}
		}
	}

	protected final void setErrorMarked(final boolean errorMarked, final String message) {
		if (!errorMarked) {
			if (errorMarker != null) {
				removeMarker(errorMarker);
			}
		} else {
			if (errorMarker == null) {
				errorMarker = new ErrorMessageMarker(message);
			} else {
				errorMarker.setMessage(message);
			}
			addMarker(errorMarker);
		}
	}

	/**
	 * Removes listeners from the <tt>uiControl</tt> when it is about to be unbound from the ridget.
	 */
	protected void uninstallListeners() {
		if (getUIControl() instanceof Control) {
			final Control control = (Control) getUIControl();
			if (visibilityListener != null) {
				removeHierarchyVisibilityListener(control.getParent(), visibilityListener);
			}
			control.removeMouseListener(mouseListener);
		}
	}

	/**
	 * @since 3.0
	 */
	public void updateMarkers() {
		if (markerSupport != null) {
			markerSupport.updateMarkers();
		}
	}

	/**
	 * @since 4.0
	 */
	protected ClickEvent createClickEvent(final MouseEvent e) {
		return new ClickEvent(this, e.button);
	}

	/**
	 * @since 4.0
	 */
	protected MouseListener getClickForwarder() {
		return mouseListener;
	}

	// helping methods
	//////////////////

	private void addHierarchyVisibilityListener(final Composite parent, final Listener listener) {
		if (parent != null && !parent.isDisposed()) {
			parent.addListener(SWT.Show, listener);
			parent.addListener(SWT.Hide, listener);
			addHierarchyVisibilityListener(parent.getParent(), listener);
		}
	}

	private void bindMarkerSupport() {
		if (markerSupport != null) {
			markerSupport.bind();
		}
	}

	private void removeHierarchyVisibilityListener(final Composite parent, final Listener listener) {
		if (parent != null && !parent.isDisposed()) {
			parent.removeListener(SWT.Show, listener);
			parent.removeListener(SWT.Hide, listener);
			removeHierarchyVisibilityListener(parent.getParent(), listener);
		}
	}

	private void unbindMarkerSupport() {
		if (markerSupport != null) {
			markerSupport.unbind();
		}
	}

	// helping classes
	// ////////////////

	private final class VisibilityListener implements Listener {
		public void handleEvent(final Event event) {
			// fire a showing event for Ridgets with markers whose visibility
			// changes because of a parent widget so that markers can be
			// updated (bug 261980)
			final Widget control = getUIControl();
			if (markerSupport != null && !getMarkers().isEmpty() && !SwtUtilities.isDisposed(control)) {
				control.getDisplay().asyncExec(new Runnable() {
					public void run() {
						if (!SwtUtilities.isDisposed(control)) {
							markerSupport.fireShowingPropertyChangeEvent();
						}
					}
				});
			}
		}

	}

	/**
	 * Listener of mouse events.
	 */
	private final class ClickForwarder implements MouseListener {

		private int pressedBtn;
		private boolean doubleClickInProcess = false;

		public void mouseDoubleClick(final MouseEvent e) {
			pressedBtn = -1;
			if (!isEnabled()) {
				return;
			}
			if (doubleClickInProcess) {
				return;
			}
			try {
				doubleClickInProcess = true;
				if (doubleClickListeners != null) {
					for (final IActionListener listener : doubleClickListeners.getListeners()) {
						listener.callback();
					}
				}
			} finally {
				doubleClickInProcess = false;
			}
		}

		public void mouseDown(final MouseEvent e) {
			pressedBtn = -1;
			if (!isEnabled()) {
				return;
			}
			final Point point = new Point(e.x, e.y);
			if (isOverWidget(point)) {
				pressedBtn = e.button;
			}
		}

		public void mouseUp(final MouseEvent e) {
			if (!isEnabled()) {
				pressedBtn = -1;
				return;
			}
			final Point point = new Point(e.x, e.y);
			if ((e.button == pressedBtn) && (isOverWidget(point))) {
				fireClickEvent(e);
			}
			pressedBtn = -1;
		}

		/**
		 * Returns whether the given point is inside or outside the bounds of the widget.
		 * 
		 * @param point
		 *            position of the mouse pointer
		 * @return {@code true} if point is inside the widget; otherwise {@code false}
		 */
		private boolean isOverWidget(final Point point) {
			if (SwtUtilities.isDisposed(getUIControl())) {
				return false;
			}
			if (getUIControl() instanceof Control) {
				final Control control = (Control) getUIControl();
				final Rectangle widgetBounds = control.getBounds();
				return (point.x <= widgetBounds.width && point.x >= 0) && (point.y <= widgetBounds.height && point.y >= 0);
			} else {
				return true;
			}
		}

		private void fireClickEvent(final MouseEvent e) {
			if (clickListeners != null) {
				final ClickEvent event = createClickEvent(e);
				for (final IClickListener listener : clickListeners.getListeners()) {
					listener.callback(event);
				}
			}
		}

	}
}
