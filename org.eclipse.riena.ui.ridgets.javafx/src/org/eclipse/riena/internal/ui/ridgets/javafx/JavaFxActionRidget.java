package org.eclipse.riena.internal.ui.ridgets.javafx;

import java.io.IOException;
import java.net.URL;

import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.internal.ui.ridgets.swt.ActionRidget;
import org.eclipse.riena.ui.core.resource.IIconManager;
import org.eclipse.riena.ui.core.resource.IconManagerProvider;
import org.eclipse.riena.ui.core.resource.IconSize;
import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.javafx.AbstractJavaFxRidget;
import org.eclipse.riena.ui.ridgets.javafx.JavaFxBasicMarkerSupport;
import org.eclipse.riena.ui.ridgets.swt.AbstractActionRidget;
import org.eclipse.riena.ui.swt.utils.ImageStore;
import org.osgi.service.log.LogService;

public class JavaFxActionRidget extends AbstractJavaFxRidget implements
		IActionRidget {

	private final static Logger LOGGER = Log4r
			.getLogger(JavaFxActionRidget.class);

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private final JavaFxActionObserver actionObserver;
	private String text;
	private String iconID;
	private boolean textAlreadyInitialized;
	private boolean useRidgetIcon;

	public JavaFxActionRidget() {
		actionObserver = new JavaFxActionObserver(this);
		textAlreadyInitialized = false;
		useRidgetIcon = false;
	}

	@Override
	protected void checkUIControl(final Object uiControl) {
		checkType(uiControl, Button.class);
	}

	@Override
	public Button getUIControl() {
		return (Button) super.getUIControl();
	}

	@Override
	protected AbstractMarkerSupport createMarkerSupport() {
		return new JavaFxBasicMarkerSupport(this, propertyChangeSupport);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractActionRidget#isDisableMandatoryMarker()}
	 */
	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
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
	 * Copy of {@link 
#setText(String)}
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
		final Button control = getUIControl();
		if (control != null) {
			initText();
			getUIControl().setOnAction(actionObserver);
			updateUIText();
			updateUIIcon();
			
		}
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

}
