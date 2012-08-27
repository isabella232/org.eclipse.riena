package org.eclipse.riena.internal.ui.ridgets.javafx;

import java.io.IOException;
import java.net.URL;

import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.ui.core.resource.IIconManager;
import org.eclipse.riena.ui.core.resource.IconManagerProvider;
import org.eclipse.riena.ui.core.resource.IconSize;
import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.javafx.AbstractJavaFxValueRidget;
import org.eclipse.riena.ui.ridgets.javafx.JavaFxBasicMarkerSupport;
import org.eclipse.riena.ui.swt.utils.ImageStore;
import org.osgi.service.log.LogService;

public class JavaFxLabelRidget extends AbstractJavaFxValueRidget implements
		ILabelRidget {

	private final static Logger LOGGER = Log4r
			.getLogger(JavaFxLabelRidget.class);

	/**
	 * This property is used by the databinding to sync ridget and model. It is
	 * always fired before its sibling {@link ILabelRidget#PROPERTY_TEXT} to
	 * ensure that the model is updated before any listeners try accessing it.
	 * <p>
	 * This property is not API. Do not use in client code.
	 */
	private static final String PROPERTY_TEXT_INTERNAL = "textInternal"; //$NON-NLS-1$

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$
	private String text;
	private String iconID;
	private URL iconLocation;
	private boolean textAlreadyInitialized;
	private boolean useRidgetIcon;

	public JavaFxLabelRidget() {
		this(null);
	}

	public JavaFxLabelRidget(Label label) {
		setUIControl(label);
		textAlreadyInitialized = false;
		useRidgetIcon = false;
	}

	@Override
	public Label getUIControl() {
		return (Label) super.getUIControl();
	}

	@Override
	protected AbstractMarkerSupport createMarkerSupport() {
		return new JavaFxBasicMarkerSupport(this, propertyChangeSupport);
	}

	/**
	 * Always returns true because mandatory markers do not make sense for this
	 * ridget.
	 */
	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
	}

	@Override
	public String getText() {
		return text;
	}

	/**
	 * This method is not API. Do not use in client code.
	 * 
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public final String getTextInternal() {
		return getText();
	}

	@Override
	public void setText(final String text) {
		final String oldValue = this.text;
		this.text = text;
		updateUIText();
		firePropertyChange(PROPERTY_TEXT_INTERNAL, oldValue, this.text);
		firePropertyChange(ILabelRidget.PROPERTY_TEXT, oldValue, this.text);
	}

	/**
	 * This method is not API. Do not use in client code.
	 * 
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public final void setTextInternal(final String text) {
		setText(text);
	}

	@Override
	public URL getIconLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIconLocation(URL location) {
		useRidgetIcon = true;
		final URL oldUrl = this.iconLocation;
		this.iconLocation = location;
		if (hasChanged(oldUrl, location)) {
			updateUIIcon();
		}
	}

	@Override
	public String getIcon() {
		return iconID;
	}

	@Override
	public void setIcon(String icon) {
		setIcon(icon, IconSize.NONE);
	}

	@Override
	public void setIcon(String icon, IconSize size) {
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

	@Override
	protected void checkUIControl(final Object uiControl) {
		checkType(uiControl, Label.class);
	}

	@Override
	protected void bindUIControl() {
		initText();
		updateUIText();
		updateUIIcon();
	}

	@Override
	protected IObservableValue getRidgetObservable() {
		return BeansObservables.observeValue(this, PROPERTY_TEXT_INTERNAL);
	}

	/**
	 * If the text of the ridget has no value, initialize it with the text of
	 * the UI control.
	 */
	private void initText() {
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

	private String getUIControlText() {
		return getUIControl().getText();
	}

	private void setUIControlText(final String text) {
		getUIControl().setText(text);
	}

	private void updateUIText() {
		if (getUIControl() != null) {
			setUIControlText(text);
		}
	}

	protected void setUIControlImage(final Image image) {
		getUIControl().setGraphic(new ImageView(image));
	}

	private void updateUIIcon() {
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
			} else if (iconLocation != null) {
				try {
					image = new Image(iconLocation.openStream());
				} catch (IOException ex) {
					final String message = String.format(
							"Image resource '%s' not found", iconLocation);
					LOGGER.log(LogService.LOG_DEBUG, message, ex);
				}
			}
			if ((image != null) || useRidgetIcon) {
				setUIControlImage(image);
			}
		}
	}
}
