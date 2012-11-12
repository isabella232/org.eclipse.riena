package org.eclipse.riena.internal.ui.ridgets.javafx;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.internal.databinding.property.value.SimplePropertyObservableValue;
import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.internal.ui.ridgets.swt.ActionRidget;
import org.eclipse.riena.ui.core.resource.IIconManager;
import org.eclipse.riena.ui.core.resource.IconManagerProvider;
import org.eclipse.riena.ui.core.resource.IconSize;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.javafx.AbstractJavaFxValueRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractActionRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.swt.MarkerSupport;
import org.eclipse.riena.ui.swt.utils.ImageStore;
import org.osgi.service.log.LogService;

public class JavaFxToggleButtonRidget extends AbstractJavaFxValueRidget
		implements IToggleButtonRidget {

	private final static Logger LOGGER = Log4r
			.getLogger(JavaFxActionRidget.class);
	private static final String PROPERTY_SELECTED_INTERNAL = "selectedInternal"; //$NON-NLS-1$
	/**
	 * ToggleButtonRidgets use this property to store a reference to themselves
	 * in their assigned control.
	 */
	private static final String TOGGLE_BUTTON_RIDGET = "tbr"; //$NON-NLS-1$

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private final JavaFxActionObserver actionObserver;
	private String text;
	private String iconID;
	private boolean textAlreadyInitialized;
	private boolean useRidgetIcon;
	private boolean selected;
	private Binding controlBinding;
	private SelectionObservableWithOutputOnly selectionObservableWithOutputOnly;

	public JavaFxToggleButtonRidget() {
		actionObserver = new JavaFxActionObserver(this);
		textAlreadyInitialized = false;
		useRidgetIcon = false;
	}

	@Override
	protected void checkUIControl(final Object uiControl) {
		checkType(uiControl, ToggleButton.class);
	}

	@Override
	public ToggleButton getUIControl() {
		return (ToggleButton) super.getUIControl();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractActionRidget#isDisableMandatoryMarker()}
	 */
	@Override
	public boolean isDisableMandatoryMarker() {
		boolean isSelected = isSelected();
		if (!isSelected && getUIControl() != null) {
			isSelected = siblingsAreSelected();
		}
		return isSelected;
	}

	private boolean siblingsAreSelected() {
		boolean result = false;
		final ToggleButton control = getUIControl();
		final ToggleButton[] siblings = getSiblings(control);
		for (int i = 0; !result && i < siblings.length; i++) {
			final ToggleButton sibling = siblings[i];
			if (sibling.isDisabled()) {
				result = sibling.isSelected();
			}
		}
		return result;
	}

	private ToggleButton[] getSiblings(final ToggleButton control) {
		final List<ToggleButton> result = new ArrayList<ToggleButton>();
		// final boolean isCheck = isCheck(control);
		// final boolean isRadio = isRadio(control);
		// final boolean isPush = isToggle(control);
		ObservableList<Node> children = control.getParent()
				.getChildrenUnmodifiable();
		for (final Node candidate : children) {
			/**
			 * deprecated isMandatory() used, stops all ToggleButtons becomming
			 * (internal) mandatory when one tb is set mandatory SGO
			 **/
			if (candidate != control && candidate instanceof ToggleButton) {
				// if ((isCheck && isCheck(candidate)) || (isRadio &&
				// isRadio(candidate)) || (isPush && isToggle(candidate))) {
				result.add((ToggleButton) candidate);
				// }
			}
		}
		return result.toArray(new ToggleButton[result.size()]);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractActionRidget#getText()}
	 */
	@Override
	public final String getText() {
		return text;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractActionRidget#getIcon()}
	 */
	@Override
	public String getIcon() {
		return this.iconID;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link #setText(String)}
	 */
	@Override
	public void setIcon(final String icon) {
		setIcon(icon, IconSize.NONE);
	}

	@Override
	public void setIcon(final String icon, final IconSize size) {
		final boolean oldUseRidgetIcon = useRidgetIcon;
		useRidgetIcon = true;
		final String oldIcon = this.iconID;
		final IIconManager manager = IconManagerProvider.getInstance()
				.getIconManager();
		this.iconID = manager.getIconID(icon, size);
		if (hasChanged(oldIcon, icon) || !oldUseRidgetIcon) {
			updateUIIcon();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractActionRidget#setText(String)}
	 */
	@Override
	public final void setText(final String newText) {
		final String oldText = this.text;
		this.text = newText;
		updateUIText();
		firePropertyChange(IActionRidget.PROPERTY_TEXT, oldText, this.text);
	}

	/**
	 * If the text of the ridget has no value, initialize it with the text of
	 * the UI control.
	 */
	protected void initText() {
		if (text == null && !textAlreadyInitialized) {
			final Control control = getUIControl();
			if (control != null) {
				text = getUIControlText();
				if (text == null) {
					text = EMPTY_STRING;
				}
				textAlreadyInitialized = true;
			}
		}
	}

	@Override
	protected void bindUIControl() {
		final ToggleButton control = getUIControl();
		if (control != null) {
			final DataBindingContext context = getValueBindingSupport()
					.getContext();
			controlBinding = context.bindValue(
					getUIControlSelectionObservable(), getRidgetObservable(),
					new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE),
					new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
			initText();
			updateUIText();
			updateSelection(isEnabled());
			updateUIIcon();
			control.getProperties().put(TOGGLE_BUTTON_RIDGET, this);
			updateMandatoryMarkers();
		}
	}

	protected void updateMandatoryMarkers() {
		final boolean disableMarker = isDisableMandatoryMarker();
		final ToggleButton control = getUIControl();
		if (control != null) {
			final ToggleButton[] siblings = getSiblings(control);
			for (final ToggleButton sibling : siblings) {
				final Object ridget = sibling.getProperties().get(
						TOGGLE_BUTTON_RIDGET);
				if (ridget instanceof JavaFxToggleButtonRidget) {
					((JavaFxToggleButtonRidget) ridget)
							.disableMandatoryMarkers(disableMarker);
				}
			}
		}
		disableMandatoryMarkers(disableMarker);
	}

	private IObservableValue getUIControlSelectionObservable() {
		if (selectionObservableWithOutputOnly == null) {
			selectionObservableWithOutputOnly = new SelectionObservableWithOutputOnly(
					getUIControl());
		}
		return selectionObservableWithOutputOnly;

	}

	/**
	 * Update the selection state of this ridget's control (button)
	 * 
	 * @param isRidgetEnabled
	 *            true if this ridget is enabled, false otherwise
	 */
	private void updateSelection(final boolean isRidgetEnabled) {
		if (getUIControl() != null
				&& MarkerSupport.isHideDisabledRidgetContent()) {
			if (!isRidgetEnabled) {
				setUIControlSelection(false);
			} else {
				setUIControlSelection(isSelected());
			}
		}
	}

	@Override
	protected void unbindUIControl() {
		super.unbindUIControl();
		if (controlBinding != null) {
			controlBinding.dispose();
			controlBinding = null;
		}
		selectionObservableWithOutputOnly = null;
	}

	// helping methods
	// ////////////////

	protected void updateUIText() {
		if (getUIControl() != null) {
			setUIControlText(getText());
		}
	}

	/**
	 * Return the text from the ui control.
	 * <p>
	 * Copy of {@link ActionRidget#getUIControlText()}
	 */
	protected String getUIControlText() {
		return getUIControl().getText();
	}

	protected void setUIControlImage(final Image image) {
		getUIControl().setGraphic(new ImageView(image));
	}

	protected void setUIControlSelection(final boolean selected) {
		getUIControl().setSelected(selected);
	}

	/**
	 * Apply a text to the ui control.
	 * <p>
	 * Copy of {@link ActionRidget#setUIControlText(String)}
	 */
	protected void setUIControlText(String text) {
		getUIControl().setText(text);
	}

	protected void updateUIIcon() {
		if (getUIControl() != null) {
			Image image = null;
			if (getIcon() != null) {
				URL url = ImageStore.getInstance().getImageUrl(getIcon());
				if (url != null) {
					try {
						image = new Image(url.openStream());
					} catch (IOException ex) {
						final String message = String.format(
								"Image resource '%s' not found", getIcon());
						LOGGER.log(LogService.LOG_DEBUG, message, ex);
					}
				}
			}
			if ((image != null) || useRidgetIcon) {
				setUIControlImage(image);
			}
		}
	}

	@Override
	public void fireAction() {
		if (isVisible() && isEnabled()) {
			actionObserver.handle(null);
		}
	}

	@Override
	public void addListener(IActionListener listener) {
		actionObserver.addListener(listener);
	}

	@Override
	public void removeListener(IActionListener listener) {
		actionObserver.removeListener(listener);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractToggleButtonRidget#isSelected()}
	 */
	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setSelected(final boolean selected) {
		if (this.selected != selected) {
			final boolean oldValue = this.selected;
			this.selected = selected;
			actionObserver.handle(null);
			firePropertyChange(PROPERTY_SELECTED_INTERNAL,
					Boolean.valueOf(oldValue), Boolean.valueOf(selected));
			firePropertyChange(IToggleButtonRidget.PROPERTY_SELECTED,
					Boolean.valueOf(oldValue), Boolean.valueOf(selected));
			updateMandatoryMarkers();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractToggleButtonRidget#getRidgetObservable()}
	 */
	@Override
	protected IObservableValue getRidgetObservable() {
		return BeansObservables.observeValue(this, PROPERTY_SELECTED_INTERNAL);
	}

	/**
	 * This method is not API. Do not use in client code.
	 * 
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public final boolean isSelectedInternal() {
		return isSelected();
	}

	/**
	 * This method is not API. Do not use in client code.
	 * 
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public final void setSelectedInternal(final boolean selected) {
		setSelected(selected);
	}

	public class JavaFxPropertyWrapper<T> implements ChangeListener<T> {

		private final PropertyChangeSupport propertyChangeSupport;
		private final Property<T> value;

		public JavaFxPropertyWrapper(Property<T> value) {
			super();
			propertyChangeSupport = new PropertyChangeSupport(this);
			this.value = value;
			this.value.addListener(this);
		}

		public void addPropertyChangeListener(final PropertyChangeListener l) {
			propertyChangeSupport.addPropertyChangeListener(l);
		}

		public void removePropertyChangeListener(final PropertyChangeListener l) {
			propertyChangeSupport.removePropertyChangeListener(l);
		}

		@Override
		public void changed(ObservableValue<? extends T> observable,
				T oldValue, T newValue) {
			propertyChangeSupport.firePropertyChange("value", oldValue,
					newValue);
		}

		public T getValue() {
			return value.getValue();
		}

		public void setValue(T newValue) {
			value.setValue(newValue);
		}

	}

	/**
	 * Custom IObservableValue that will revert selection changes when the
	 * ridget is output-only.
	 * 
	 * @see http://bugs.eclipse.org/271762
	 * @see http://bugs.eclipse.org/321935
	 */
	private final class SelectionObservableWithOutputOnly extends
			SimplePropertyObservableValue implements EventHandler<ActionEvent> {

		private final ToggleButton toggleButton;

		@SuppressWarnings("restriction")
		public SelectionObservableWithOutputOnly(final ToggleButton source) {
			super(getValueBindingSupport().getContext().getValidationRealm(),
					source, new JavaFxToggleButtonSelectedProperty());
			Assert.isNotNull(source);
			this.toggleButton = source;
			this.toggleButton.setOnAction(this);
		}

		@Override
		public void handle(ActionEvent e) {
			if (isOutputOnly()) {
				toggleButton.setSelected(isSelected());
			}
		}

		@Override
		protected Object doGetValue() {
			return isOutputOnly() ? Boolean.valueOf(isSelected()) : super
					.doGetValue();
		}

	}

}
