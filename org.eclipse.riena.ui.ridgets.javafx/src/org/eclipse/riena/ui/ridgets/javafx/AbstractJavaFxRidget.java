package org.eclipse.riena.ui.ridgets.javafx;

import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.core.util.ListenerList;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.ErrorMessageMarker;
import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.OutputMarker;
import org.eclipse.riena.ui.javafx.utils.JavaFxBindingPropertyLocator;
import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.AbstractRidget;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IBasicMarkableRidget;
import org.eclipse.riena.ui.ridgets.listener.ClickEvent;
import org.eclipse.riena.ui.ridgets.listener.IClickListener;
import org.eclipse.riena.ui.ridgets.swt.AbstractSWTRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractSWTWidgetRidget;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingPropertyLocator;
import org.eclipse.swt.widgets.Widget;
import org.osgi.service.log.LogService;

public abstract class AbstractJavaFxRidget extends AbstractRidget implements
		IBasicMarkableRidget {

	private final static Logger LOGGER = Log4r
			.getLogger(AbstractJavaFxRidget.class);

	private ListenerList<IClickListener> clickListeners;
	private ListenerList<IActionListener> doubleClickListeners;
	private Control uiControl;
	private String toolTip = null;
	private ErrorMessageMarker errorMarker;
	private DisabledMarker disabledMarker;
	private MandatoryMarker mandatoryMarker;
	private OutputMarker outputMarker;
	private HiddenMarker hiddenMarker;
	private AbstractMarkerSupport markerSupport;
	private ClickForwarder mouseListener;
	private boolean focusable;
	private final JavaFxFocusManager focusManager;

	public AbstractJavaFxRidget() {
		focusable = true;
		focusManager = new JavaFxFocusManager(this);
	}

	@Override
	public void setUIControl(Object uiControl) {
		checkUIControl(uiControl);
		uninstallListeners();
		unbindUIControl();
		unbindMarkerSupport();
		this.uiControl = (Control) uiControl;
		bindMarkerSupport();
		updateEnabled();
		updateMarkers();
		updateToolTip();
		updateFocusable();
		bindUIControl();
		installListeners();
	}

	protected void unbindUIControl() {
		// save the state
		savedVisibleState = isVisible();
	}

	@Override
	public Control getUIControl() {
		return uiControl;
	}

	@Override
	public String getID() {
		final IBindingPropertyLocator locator = JavaFxBindingPropertyLocator
				.getInstance();
		return locator.locateBindingProperty(getUIControl());
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#hasFocus()}
	 */
	@Override
	public boolean hasFocus() {
		final Control control = getUIControl();
		if (control != null) {
			return control.isFocused();
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#isEnabled()}
	 */
	@Override
	public boolean isEnabled() {
		return getMarkersOfType(DisabledMarker.class).isEmpty();
	}

	@Override
	public boolean isVisible() {
		// check for "hidden.marker". This marker overrules any other visibility
		// rule
		if (!getMarkersOfType(HiddenMarker.class).isEmpty()) {
			return false;
		}

		if (getUIControl() != null) {
			// // the swt control is bound
			// if (isChildOfSubModuleView(getUIControl())) {
			// return isControlVisible(getUIControl());
			// } else {
			// return getUIControl().isVisible();
			// }
			return getUIControl().isVisible();
		}
		// control is not bound
		return savedVisibleState;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#isErrorMarked()}
	 */
	public final boolean isErrorMarked() {
		return !getMarkersOfType(ErrorMarker.class).isEmpty();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#requestFocus()}
	 */
	@Override
	public void requestFocus() {
		// not supported
	}

	/**
	 * Adds listeners to the <tt>uiControl</tt> after it was bound to the
	 * ridget.
	 */
	protected void installListeners() {
		final Control control = getUIControl();
		if (control != null) {
			mouseListener = new ClickForwarder();
			control.setOnMouseClicked(mouseListener);
			control.focusedProperty().addListener(focusManager);
		}
	}

	/**
	 * Removes listeners from the <tt>uiControl</tt> when it is about to be
	 * unbound from the ridget.
	 */
	protected void uninstallListeners() {
		final Control control = getUIControl();
		if (control != null) {
			// remove mouseListener ?
			control.focusedProperty().removeListener(focusManager);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#setEnabled()}
	 */
	@Override
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

	/**
	 * Copy of {@link AbstractSWTWidgetRidget#setMandatory()}
	 */
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

	/**
	 * Copy of {@link AbstractSWTWidgetRidget#setOutputOnly()}
	 */
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

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#setErrorMarked()}
	 */
	public final void setErrorMarked(final boolean errorMarked) {
		setErrorMarked(errorMarked, null);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#setErrorMarked()}
	 */
	protected final void setErrorMarked(final boolean errorMarked,
			final String message) {
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

	public JavaFxFocusManager getFocusManager() {
		return focusManager;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTRidget#setFocusable()}
	 */
	@Override
	public final boolean isFocusable() {
		return focusable;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTRidget#setFocusable()}
	 */
	@Override
	public final void setFocusable(final boolean focusable) {
		if (this.focusable != focusable) {
			this.focusable = focusable;
			updateFocusable();
		}
	}

	private void updateFocusable() {
		if (getUIControl() != null) {
			boolean focusable = isFocusable() && !isOutputOnly();
			getUIControl().setFocusTraversable(focusable);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#setVisible()}
	 */
	@Override
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

	@Override
	public final String getToolTipText() {
		return toolTip;
	}

	@Override
	public final void setToolTipText(final String toolTipText) {
		final String oldValue = this.toolTip;
		this.toolTip = toolTipText;
		updateToolTip();
		firePropertyChange(PROPERTY_TOOLTIP, oldValue, this.toolTip);
	}

	protected final void updateToolTip() {
		if (getUIControl() != null) {
			String toolTipText = getToolTipText();
			if (StringUtils.isEmpty(toolTipText)) {
				getUIControl().setTooltip(null);
			} else {
				Tooltip tooltip = new Tooltip(toolTipText);
				getUIControl().setTooltip(tooltip);
			}
		}
	}

	/**
	 * Copy of {@link AbstractSWTWidgetRidget#bindMarkerSupport()}
	 */
	private void bindMarkerSupport() {
		if (markerSupport != null) {
			markerSupport.bind();
		}
	}

	/**
	 * Copy of {@link AbstractSWTWidgetRidget#unbindMarkerSupport()}
	 */
	private void unbindMarkerSupport() {
		if (markerSupport != null) {
			markerSupport.unbind();
		}
	}

	/**
	 * Copy of {@link AbstractSWTWidgetRidget#updateMarkers()}
	 */
	public void updateMarkers() {
		if (markerSupport != null) {
			markerSupport.updateMarkers();
		}
	}

	protected final void updateEnabled() {
		if (getUIControl() != null) {
			getUIControl().setDisable(!isEnabled());
		}
	}

	// abstract methods - subclasses must implement
	// ///////////////////////////////////////////////////////

	/**
	 * Performs checks on the control about to be bound by this ridget.
	 * <p>
	 * Implementors must make sure the given <tt>uiControl</tt> has the expected
	 * type.
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
	 * Implementors must call {@link #getUIControl()} to obtain the current
	 * control. If the control is non-null they must do whatever necessary to
	 * bind it to the ridget.
	 */
	abstract protected void bindUIControl();

	abstract public boolean isDisableMandatoryMarker();

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#hasChanged()}
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
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#getHiddenMarkerTypes()}
	 */
	@Override
	public Set<Class<IMarker>> getHiddenMarkerTypes() {
		if (markerSupport != null) {
			return markerSupport.getHiddenMarkerTypes();
		}
		return new HashSet<Class<IMarker>>();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#hideMarkersOfType()}
	 */
	@Override
	public Set<Class<IMarker>> hideMarkersOfType(
			Class<? extends IMarker>... types) {
		if (markerSupport == null) {
			markerSupport = createMarkerSupport();
			markerSupport.setRidget(this);
		}
		return markerSupport.hideMarkersOfType(types);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#showMarkersOfType()}
	 */
	@Override
	public Set<Class<IMarker>> showMarkersOfType(
			Class<? extends IMarker>... types) {
		if (markerSupport == null) {
			return new HashSet<Class<IMarker>>();
		}
		return markerSupport.showMarkersOfType(types);
	}

	@Override
	public boolean decorateVisibleControlArea() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#createMarkerSupport()}
	 */
	protected AbstractMarkerSupport createMarkerSupport() {
		// AbstractMarkerSupport lnfMarkerSupport = null;
		// // if (LnfManager.getLnf() != null) {
		// // // TODO ?
		// // lnfMarkerSupport = LnfManager.getLnf().getMarkerSupport(
		// // this.getClass());
		// // }
		// if (lnfMarkerSupport == null) {
		// // No MarkerSupport exits. Default MarkerSupport is used.
		// lnfMarkerSupport = new JavaFxBasicMarkerSupport();
		// }
		// lnfMarkerSupport.init(this, propertyChangeSupport);
		//		Assert.isNotNull(lnfMarkerSupport, "Marker support is null!"); //$NON-NLS-1$
		// return lnfMarkerSupport;
		return new JavaFxMarkerSupport(this, propertyChangeSupport);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#addMarker()}
	 */
	@Override
	public void addMarker(IMarker marker) {
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
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#removeMarker()}
	 */
	@Override
	public boolean removeMarker(IMarker marker) {
		if (markerSupport != null) {
			return markerSupport.removeMarker(marker);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#removeAllMarkers()}
	 */
	@Override
	public void removeAllMarkers() {
		if (markerSupport != null) {
			markerSupport.removeAllMarkers();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#getMarkersOfType()}
	 */
	@Override
	public <T extends IMarker> Collection<T> getMarkersOfType(Class<T> type) {
		if (markerSupport != null) {
			return markerSupport.getMarkersOfType(type);
		}
		return Collections.emptySet();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#getMarkers()}
	 */
	@Override
	public Collection<? extends IMarker> getMarkers() {
		if (markerSupport != null) {
			return markerSupport.getMarkers();
		}
		return Collections.emptySet();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#addClickListener}
	 */
	@Override
	public void addClickListener(IClickListener listener) {
		Assert.isNotNull(listener, "listener is null"); //$NON-NLS-1$
		if (clickListeners == null) {
			clickListeners = new ListenerList<IClickListener>(
					IClickListener.class);
		}
		clickListeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#removeClickListener}
	 */
	@Override
	public void removeClickListener(IClickListener listener) {
		if (clickListeners != null) {
			clickListeners.remove(listener);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#addDoubleClickListener}
	 */
	@Override
	public void addDoubleClickListener(IActionListener listener) {
		Assert.isNotNull(listener, "listener is null"); //$NON-NLS-1$
		if (doubleClickListeners == null) {
			doubleClickListeners = new ListenerList<IActionListener>(
					IActionListener.class);
		}
		doubleClickListeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractSWTWidgetRidget#removeDoubleClickListener}
	 */
	@Override
	public void removeDoubleClickListener(IActionListener listener) {
		if (doubleClickListeners != null) {
			doubleClickListeners.remove(listener);
		}
	}

	protected ClickEvent createClickEvent(final MouseEvent e) {
		int buttonNumber = getButtonNumber(e.getButton());
		return new ClickEvent(this, buttonNumber);
	}

	private int getButtonNumber(MouseButton button) {
		if (button == MouseButton.PRIMARY) {
			return 1;
		} else if (button == MouseButton.SECONDARY) {
			return 2;
		} else if (button == MouseButton.MIDDLE) {
			return 3;
		} else if (button == MouseButton.NONE) {
			return -1;
		} else {
			final String message = String.format("unknown mouse button: %s",
					button);
			LOGGER.log(LogService.LOG_WARNING, message);
			return -1;
		}
	}

	/**
	 * Copy of {@link AbstractSWTWidgetRidget#isMandatory}
	 */
	@Deprecated
	public final boolean isMandatory() {
		return !getMarkersOfType(MandatoryMarker.class).isEmpty();
	}

	/**
	 * Copy of {@link AbstractSWTWidgetRidget#isOutputOnly}
	 */
	public final boolean isOutputOnly() {
		return !getMarkersOfType(OutputMarker.class).isEmpty();
	}

	/**
	 * Copy of {@link AbstractSWTWidgetRidget#flash}
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

	/**
	 * Copy of {@link AbstractSWTWidgetRidget#disableMandatoryMarkers}
	 */
	protected final void disableMandatoryMarkers(final boolean disable) {
		for (final MandatoryMarker marker : getMarkersOfType(MandatoryMarker.class)) {
			marker.setDisabled(disable);
		}
	}

	/**
	 * Listener of mouse events.
	 */
	private final class ClickForwarder implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			if (event.getClickCount() == 1) {
				mouseClick(event);
			} else if (event.getClickCount() == 2) {
				if (event.getButton() == MouseButton.PRIMARY) {
					mouseDoubleClick(event);
				} else {
					final String message = String
							.format("double click and mouse button '%s' are not supported",
									event.getButton());
					LOGGER.log(LogService.LOG_INFO, message);
				}
			} else {
				final String message = String.format(
						"unsupported number of click counts: %s",
						event.getClickCount());
				LOGGER.log(LogService.LOG_INFO, message);
			}
		}

		private void mouseClick(final MouseEvent e) {
			if (!isEnabled() && (getUIControl() != null)) {
				return;
			}
			if (clickListeners != null) {
				final ClickEvent event = createClickEvent(e);
				for (final IClickListener listener : clickListeners
						.getListeners()) {
					listener.callback(event);
				}
			}
		}

		private void mouseDoubleClick(final MouseEvent e) {
			if (!isEnabled() && (getUIControl() != null)) {
				return;
			}
			if (doubleClickListeners != null) {
				for (final IActionListener listener : doubleClickListeners
						.getListeners()) {
					listener.callback();
				}
			}
		}

	}
	
	public static boolean isBean(final Class<?> clazz) {
		try {
			// next line throws NoSuchMethodException, if no matching method found
			clazz.getMethod("addPropertyChangeListener", PropertyChangeListener.class); //$NON-NLS-1$
			return true; // have bean
		} catch (final NoSuchMethodException e) {
			return false; // have pojo
		}
	}

	protected ClickEvent createClickEvent(org.eclipse.swt.events.MouseEvent e) {
		// TODO Auto-generated method stub
		return null;
	}

}
