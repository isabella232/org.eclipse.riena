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
package org.eclipse.riena.ui.ridgets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.core.marker.IMarkerAttributeChangeListener;
import org.eclipse.riena.core.marker.Markable;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.core.marker.IMarkerPropertyChangeEvent;
import org.eclipse.riena.ui.core.marker.OutputMarker;

/**
 * Helper class for Ridgets to delegate their marker issues to.
 */
public abstract class AbstractMarkerSupport {

	private Collection<IMarker> markers;
	private Set<Class<? extends IMarker>> hiddenMarkerTypes;
	private IBasicMarkableRidget ridget;
	private PropertyChangeSupport propertyChangeSupport;
	private IMarkerAttributeChangeListener markerAttributeChangeListener;

	/**
	 * @since 2.0
	 */
	public AbstractMarkerSupport() {
		markerAttributeChangeListener = new MarkerAttributeChangeListener();
	}

	public AbstractMarkerSupport(final IBasicMarkableRidget ridget, final PropertyChangeSupport propertyChangeSupport) {
		super();
		init(ridget, propertyChangeSupport);
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IBasicMarkableRidget#addMarker(org.eclipse.riena.core.marker.IMarker)
	 */
	public void addMarker(final IMarker marker) {
		if (marker == null) {
			return;
		}

		initializeMarkers();
		if (markers.contains(marker)) {
			return;
		}
		final Collection<IMarker> oldValue = cloneMarkers();
		if (markers.add(marker)) {
			updateMarkers();
			final Collection<IMarker> newValue = getMarkers();
			fireMarkerPropertyChangeEvent(oldValue, newValue);
			fireOutputPropertyChangeEvent(oldValue, newValue);
			fireEnabledPropertyChangeEvent(oldValue, newValue);
			marker.addAttributeChangeListener(markerAttributeChangeListener);
		}
	}

	/**
	 * Callback method triggered when a control is bound to the {@link IRidget}.
	 * <p>
	 * The default implementation is empty. Subclasses may override.
	 * 
	 * @since 3.0
	 */
	public void bind() {
		// nop
	}

	/**
	 * Initializes this marker support with the Ridget and the property change
	 * support.
	 * 
	 * @param ridget
	 *            the Ridget that needs the marker support.
	 * @param propertyChangeSupport
	 * @since 2.0
	 */
	public void init(final IBasicMarkableRidget ridget, final PropertyChangeSupport propertyChangeSupport) {
		this.ridget = ridget;
		this.propertyChangeSupport = propertyChangeSupport;
	}

	public void fireShowingPropertyChangeEvent() {
		propertyChangeSupport.firePropertyChange(IRidget.PROPERTY_SHOWING, !ridget.isVisible(), ridget.isVisible());
		updateMarkers();
	}

	/**
	 * "Flashes" some kind of notification <b>asynchronously</b>. The notion and
	 * duration of "flashing" is implementation specific - it could flash a
	 * special color, an error decoration, etc.
	 * <p>
	 * The default implementation does nothing. Subclasses should override.
	 * Implementors shall check that the ui control is not null and that the
	 * flash is not already in progress.
	 * <p>
	 * <b>Flashing must not alter the marker state of the ridget!</b>
	 * 
	 * @since 3.0
	 */
	public void flash() {
		// does nothing - subclasses should override
	}

	/**
	 * Returns all hidden marker types.
	 * 
	 * @return the set of currently hidden marker types; never null; may be
	 *         empty
	 * 
	 * @since 3.0
	 */
	@SuppressWarnings("unchecked")
	public final Set<Class<IMarker>> getHiddenMarkerTypes() {
		if (hiddenMarkerTypes == null) {
			return Collections.EMPTY_SET;
		}
		final Set<Class<IMarker>> result = new HashSet<Class<IMarker>>();
		for (final Class<? extends IMarker> markerType : hiddenMarkerTypes) {
			result.add((Class<IMarker>) markerType);
		}
		return result;
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IBasicMarkableRidget#getMarkers()
	 */
	public Collection<IMarker> getMarkers() {
		//		if (markers != null) {
		//			return Collections.unmodifiableSet(markers);
		//		} else {
		//			return Collections.emptySet();
		//		}
		return cloneMarkers();
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IBasicMarkableRidget#getMarkersOfType(java.lang.Class)
	 */
	public <T extends IMarker> Collection<T> getMarkersOfType(final Class<T> type) {
		return Markable.getMarkersOfType(getMarkers(), type);
	}

	/**
	 * Hide markers of the given type. Hidden markers of a matching type shall
	 * be ignored in the UI (i.e. no feedback is shown by the ridget), but are
	 * not removed from the ridget.
	 * <p>
	 * Initially the set of hidden markers is empty. When calling this method
	 * the {@code type}-argument is added to the set.
	 * <p>
	 * <b>Note:</b> the set of marker types that can be successfully ignored is
	 * implementation detail of concrete implementations of this class.
	 * 
	 * @param types
	 *            the type of markers to hide. The matching includes sublasses
	 * @return the set of currently hidden marker types (including type)
	 * 
	 * @since 3.0
	 */
	public final Set<Class<IMarker>> hideMarkersOfType(final Class<? extends IMarker>... types) {
		if (hiddenMarkerTypes == null) {
			hiddenMarkerTypes = new HashSet<Class<? extends IMarker>>();
		}
		boolean changed = false;
		for (final Class<? extends IMarker> clazz : types) {
			changed |= hiddenMarkerTypes.add(clazz);
		}
		if (changed) {
			updateMarkers();
			fireMarkerHidingPropertyChangeEvent(null, null);
		}
		return getHiddenMarkerTypes();
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IBasicMarkableRidget#removeAllMarkers()
	 */
	public void removeAllMarkers() {
		if ((markers != null) && !markers.isEmpty()) {
			for (final IMarker marker : markers) {
				marker.removeAttributeChangeListener(markerAttributeChangeListener);
			}
			final Collection<IMarker> oldValue = cloneMarkers();
			clearMarkers();
			updateMarkers();
			final Collection<IMarker> newValue = getMarkers();
			fireMarkerPropertyChangeEvent(oldValue, newValue);
			fireOutputPropertyChangeEvent(oldValue, newValue);
			fireEnabledPropertyChangeEvent(oldValue, newValue);
		}
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IBasicMarkableRidget#removeMarker(org.eclipse.riena.core.marker.IMarker)
	 * @since 3.0
	 */
	public boolean removeMarker(final IMarker marker) {
		if ((markers != null) && !markers.isEmpty()) {
			final Collection<IMarker> oldValue = cloneMarkers();
			if (markers.remove(marker)) {
				updateMarkers();
				final Collection<IMarker> newValue = getMarkers();
				fireMarkerPropertyChangeEvent(oldValue, newValue);
				fireOutputPropertyChangeEvent(oldValue, newValue);
				fireEnabledPropertyChangeEvent(oldValue, newValue);
				marker.removeAttributeChangeListener(markerAttributeChangeListener);
				return true;
			}
		}
		return false;
	}

	/**
	 * TODO javadoc?
	 * 
	 * @param ridget
	 *            the ridget to set
	 * @since 3.0
	 */
	public void setRidget(final IBasicMarkableRidget ridget) {
		this.ridget = ridget;
	}

	/**
	 * Show markers of the given type. Hidden markers of a matching type shall
	 * be ignored in the UI (i.e. not feedback is shown by the ridget), but are
	 * not removed from the ridget.
	 * <p>
	 * Initially the set of hidden markers is empty. When calling this method
	 * the {@code type}-argument is removed from the set.
	 * <p>
	 * <b>Note:</b> the set of marker types that can be successfully ignored is
	 * implementation detail of concrete implementations of this class.
	 * 
	 * @param types
	 *            the type of markers to show (unhide). The matching includes
	 *            subclasses
	 * @return the set of currently hidden marker types (including type).
	 * 
	 * @since 3.0
	 */
	public final Set<Class<IMarker>> showMarkersOfType(final Class<? extends IMarker>... types) {
		if (hiddenMarkerTypes == null) {
			return new HashSet<Class<IMarker>>();
		}
		boolean changed = false;
		for (final Class<? extends IMarker> clazz : types) {
			changed |= hiddenMarkerTypes.remove(clazz);
		}
		if (changed) {
			updateMarkers();
			fireMarkerHidingPropertyChangeEvent(null, null);
		}
		return getHiddenMarkerTypes();
	}

	/**
	 * Callback method triggered when a control is unbound from the
	 * {@link IRidget}.
	 * <p>
	 * The default implementation is empty. Subclasses may override.
	 * 
	 * @since 3.0
	 */
	public void unbind() {
		// nop
	}

	// abstract methods
	///////////////////

	/**
	 * Updates the UI-control to display the current markers.
	 * 
	 * @see #getUIControl()
	 * @see #getMarkers()
	 */
	public abstract void updateMarkers();

	protected Object getUIControl() {
		return getRidget().getUIControl();
	}

	protected IBasicMarkableRidget getRidget() {
		return ridget;
	}

	protected void handleMarkerAttributesChanged() {
		propertyChangeSupport.firePropertyChange(new MarkerPropertyChangeEvent(true, getRidget(), getMarkers()));
	}

	protected boolean hasHiddenMarkers() {
		return !getRidget().getMarkersOfType(HiddenMarker.class).isEmpty();
	}

	// helping methods
	//////////////////

	private void clearMarkers() {
		markers.clear();
	}

	private Collection<IMarker> cloneMarkers() {
		if (markers != null) {
			return new ArrayList<IMarker>(markers);
		} else {
			return Collections.emptySet();
		}
	}

	private void initializeMarkers() {
		if (markers == null) {
			markers = new ArrayList<IMarker>(1);
		}
	}

	private Boolean isEnabled(final Collection<IMarker> markers) {
		boolean result = true;
		if (markers != null) {
			final Iterator<IMarker> iter = markers.iterator();
			while (result && iter.hasNext()) {
				result = !(iter.next() instanceof DisabledMarker);
			}
		}
		return Boolean.valueOf(result);
	}

	private Boolean isOutput(final Collection<IMarker> markers) {
		boolean result = false;
		if (markers != null) {
			final Iterator<IMarker> iter = markers.iterator();
			while (!result && iter.hasNext()) {
				result = (iter.next() instanceof OutputMarker);
			}
		}
		return Boolean.valueOf(result);
	}

	private void fireEnabledPropertyChangeEvent(final Collection<IMarker> oldMarkers,
			final Collection<IMarker> newMarkers) {
		final Boolean oldValue = isEnabled(oldMarkers);
		final Boolean newValue = isEnabled(newMarkers);
		if (!oldValue.equals(newValue)) {
			final PropertyChangeEvent evt = new PropertyChangeEvent(getRidget(), IRidget.PROPERTY_ENABLED, oldValue,
					newValue);
			propertyChangeSupport.firePropertyChange(evt);
		}
	}

	private void fireMarkerPropertyChangeEvent(final Collection<IMarker> oldMarkers,
			final Collection<IMarker> newMarkers) {
		propertyChangeSupport.firePropertyChange(new MarkerPropertyChangeEvent(oldMarkers, getRidget(), newMarkers));
	}

	private void fireMarkerHidingPropertyChangeEvent(final Collection<IMarker> oldMarkers,
			final Collection<IMarker> newMarkers) {
		propertyChangeSupport.firePropertyChange(IBasicMarkableRidget.PROPERTY_MARKER_HIDING, null, null);
	}

	private void fireOutputPropertyChangeEvent(final Collection<IMarker> oldMarkers,
			final Collection<IMarker> newMarkers) {
		final Boolean oldValue = isOutput(oldMarkers);
		final Boolean newValue = isOutput(newMarkers);
		if (!oldValue.equals(newValue)) {
			final PropertyChangeEvent evt = new PropertyChangeEvent(getRidget(), IMarkableRidget.PROPERTY_OUTPUT_ONLY,
					oldValue, newValue);
			propertyChangeSupport.firePropertyChange(evt);
		}
	}

	// helping classes
	//////////////////

	private static final class MarkerPropertyChangeEvent extends PropertyChangeEvent implements
			IMarkerPropertyChangeEvent {

		private static final long serialVersionUID = 1L;

		private boolean attributeRelated = false;

		private MarkerPropertyChangeEvent(final Object oldValue, final IBasicMarkableRidget source,
				final Object newValue) {
			super(source, IBasicMarkableRidget.PROPERTY_MARKER, oldValue, newValue);
		}

		private MarkerPropertyChangeEvent(final boolean attributeRelated, final IBasicMarkableRidget source,
				final Object newValue) {
			this(null, source, newValue);
			this.attributeRelated = attributeRelated;
		}

		public boolean isAttributeRelated() {
			return attributeRelated;
		}
	}

	private final class MarkerAttributeChangeListener implements IMarkerAttributeChangeListener {
		public void attributesChanged() {
			handleMarkerAttributesChanged();
		}
	}
}
