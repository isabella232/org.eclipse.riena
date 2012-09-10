package org.eclipse.riena.internal.ui.ridgets.javafx;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.riena.internal.ui.ridgets.swt.TextRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.javafx.AbstractJavaFxEditableRidget;
import org.eclipse.riena.ui.ridgets.swt.MarkerSupport;

public class JavaFxTextFieldRidget extends AbstractJavaFxEditableRidget
		implements ITextRidget {

	/**
	 * This property is used by the databinding to sync ridget and model. It is
	 * always fired before its sibling {@link ITextRidget#PROPERTY_TEXT} to
	 * ensure that the model is updated before any listeners try accessing it.
	 * <p>
	 * This property is not API. Do not use in client code.
	 */
	private static final String PROPERTY_TEXT_INTERNAL = "textInternal"; //$NON-NLS-1$

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private String textValue = EMPTY_STRING;
	private boolean isDirectWriting;
	private final EventHandler<KeyEvent> keyEventHandler;
	private final FocusChangeListener focusChangeListener;
	private final TextChangeListener textChangeListener;
	private IConverter inputConverter;
	private final static boolean DEFAULT_DIRECTWRITING = getDefaultTextRidgetDirectWritingEnabled();

	/**
	 * This system property controls
	 * {@code RienaStatus.getDefaultTextRidgetDirectWritingEnabled}
	 */
	private static final String RIENA_TEXT_RIDGET_DIRECTWRITING_PROPERTY = "riena.textridget.directwriting"; //$NON-NLS-1$

	private static final String DIRECTWRITING_DEFAULT = "false"; //$NON-NLS-1$

	/**
	 * Checks if the systemproperty <code>riena.textridget.directwriting</code>
	 * was given, that indicates that every TextRidget has per default
	 * directwriting enabled.
	 * 
	 * @return <code>true</code> if per default directwriting is enabled in
	 *         TextRidgets, otherwise <code>false</code>
	 */
	private static boolean getDefaultTextRidgetDirectWritingEnabled() {
		return Boolean.parseBoolean(System
				.getProperty(RIENA_TEXT_RIDGET_DIRECTWRITING_PROPERTY,
						DIRECTWRITING_DEFAULT));
	}

	public JavaFxTextFieldRidget() {

		keyEventHandler = new CRKeyListener();
		focusChangeListener = new FocusChangeListener();
		textChangeListener = new TextChangeListener();
		isDirectWriting = DEFAULT_DIRECTWRITING;

		addPropertyChangeListener(IRidget.PROPERTY_ENABLED,
				new PropertyChangeListener() {
					@Override
					public void propertyChange(final PropertyChangeEvent evt) {
						forceTextToControl(textValue);
					}
				});
		addPropertyChangeListener(IMarkableRidget.PROPERTY_OUTPUT_ONLY,
				new PropertyChangeListener() {
					@Override
					public void propertyChange(final PropertyChangeEvent evt) {
						updateEditable();
						forceTextToControl(textValue);
					}
				});

	}

	protected JavaFxTextFieldRidget(final String initialValue) {
		this();
		Assert.isNotNull(initialValue);
		textValue = initialValue;
	}

	@Override
	public boolean revalidate() {
		if (getUIControl() != null) {
			textValue = getUIText();
		}
		forceTextToControl(textValue);
		disableMandatoryMarkers(isNotEmpty(textValue));
		final IStatus status = checkAllRules(textValue, new ValidationCallback(
				false));
		if (status.isOK()) {
			getValueBindingSupport().updateFromTarget();
		}
		return !isErrorMarked();
	}

	protected String getUIText() {
		final TextInputControl control = getUIControl();
		Assert.isNotNull(control);
		return control.getText();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link TextRidget#getTextInternal()}
	 */
	public final synchronized String getTextInternal() {
		return getText();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link TextRidget#setTextInternal()}
	 */
	public final synchronized void setTextInternal(final String text) {
		setText(text);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link TextRidget#getText()}
	 */
	@Override
	public String getText() {
		return textValue;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link TextRidget#isDirectWriting()}
	 */
	@Override
	public synchronized boolean isDirectWriting() {
		return isDirectWriting;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link TextRidget#setDirectWriting()}
	 */
	@Override
	public synchronized void setDirectWriting(final boolean directWriting) {
		if (this.isDirectWriting != directWriting) {
			this.isDirectWriting = directWriting;
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link TextRidget#isDisableMandatoryMarker()}
	 */
	@Override
	public final boolean isDisableMandatoryMarker() {
		return isNotEmpty(textValue);
	}

	/**
	 * Copy of {@link TextRidget#isNotEmpty()}
	 */
	protected boolean isNotEmpty(final String input) {
		return input.length() > 0;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link TextRidget#setInputToUIControlConverter()}
	 */
	@Override
	public void setInputToUIControlConverter(IConverter converter) {
		if (converter != null) {
			Assert.isLegal(converter.getFromType() == String.class,
					"Invalid from-type. Need a String-to-String converter"); //$NON-NLS-1$
			Assert.isLegal(converter.getToType() == String.class,
					"Invalid to-type. Need a String-to-String converter"); //$NON-NLS-1$
		}
		this.inputConverter = converter;
	}

	@Override
	public void setText(String text) {
		final String oldValue = textValue;
		textValue = text != null ? text : EMPTY_STRING;
		forceTextToControl(textValue);
		disableMandatoryMarkers(isNotEmpty(textValue));
		final IStatus onEdit = checkOnEditRules(textValue,
				new ValidationCallback(false));
		if (onEdit.isOK()) {
			firePropertyChange(PROPERTY_TEXT_INTERNAL, oldValue, textValue);
			firePropertyChange(ITextRidget.PROPERTY_TEXT, oldValue, textValue);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link TextRidget#getRidgetObservable()}
	 */
	@Override
	protected IObservableValue getRidgetObservable() {
		return BeansObservables.observeValue(this, PROPERTY_TEXT_INTERNAL);
	}

	@Override
	protected void checkUIControl(final Object uiControl) {
		checkType(uiControl, TextInputControl.class);
	}

	@Override
	protected final synchronized void bindUIControl() {
		final TextInputControl control = getUIControl();
		if (control != null) {
			setUIText(textValue);
			updateEditable();
			addListeners(control);
		}
	}

	@Override
	protected final synchronized void unbindUIControl() {
		super.unbindUIControl();
		final TextInputControl control = getUIControl();
		if (control != null) {
			removeListeners(control);
		}
	}

	protected void setUIText(final String text) {
		final TextInputControl control = getUIControl();
		if (control != null) {
			control.setText(getTextBasedOnMarkerState(text));
			control.deselect();
			control.selectPositionCaret(0);
		}
	}

	@Override
	public TextInputControl getUIControl() {
		return (TextInputControl) super.getUIControl();
	}

	/**
	 * Copy of {@link TextRidget#getTextBasedOnMarkerState()}
	 */
	protected String getTextBasedOnMarkerState(final String value) {
		final boolean hideValue = !isEnabled()
				&& MarkerSupport.isHideDisabledRidgetContent();
		return hideValue ? EMPTY_STRING : value;
	}

	protected synchronized void addListeners(final TextInputControl control) {
		control.setOnKeyReleased(keyEventHandler);
		control.focusedProperty().addListener(focusChangeListener);
		control.textProperty().addListener(textChangeListener);
		// TODO !(ValidationListener?)
	}

	protected synchronized void removeListeners(final TextInputControl control) {
		control.focusedProperty().removeListener(focusChangeListener);
		control.textProperty().removeListener(textChangeListener);
		// TODO !(ValidationListener?)
	}

	protected void selectAll() {
		final TextInputControl control = getUIControl();
		// if not TextArea
		if (control != null && !(control instanceof TextArea)) {
			control.selectAll();
		}
	}

	private synchronized void forceTextToControl(final String newValue) {
		final TextInputControl control = getUIControl();
		if (control != null) {
			control.textProperty().addListener(textChangeListener);
			setUIText(newValue);
			control.textProperty().addListener(textChangeListener);
		}
	}

	protected void updateEditable() {
		final TextInputControl control = getUIControl();
		if (control != null) {
			final boolean isEditable = isOutputOnly() ? false : true;
			if (isEditable != control.isEditable()) {
				// final Color bgColor = control.getBackground();
				control.setEditable(isEditable);
				// workaround for Bug 315689 / 315691
				// control.setBackground(bgColor);
			}
		}
	}

	/**
	 * Copy of {@link TextRidget#updateTextValue()}
	 */
	private synchronized void updateTextValue() {
		if (isOutputOnly()) {
			return;
		}
		final String oldValue = textValue;
		final String newValue = getUIText();
		if (!oldValue.equals(newValue)) {
			textValue = newValue;
			if (checkOnEditRules(newValue, null).isOK()) {
				firePropertyChange(PROPERTY_TEXT_INTERNAL, oldValue, newValue);
				if (isExternalValueChange(oldValue, newValue)) {
					firePropertyChange(ITextRidget.PROPERTY_TEXT, oldValue,
							newValue);
				}
			}
		}
	}

	/**
	 * Copy of {@link TextRidget#isExternalValueChange()}
	 */
	protected boolean isExternalValueChange(final String oldValue,
			final String newValue) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Invoking this method will copy the model value into the ridget and the
	 * widget regardless of the validation outcome. If the model value does not
	 * pass validation, the error marker will be set.
	 */
	@Override
	public synchronized void updateFromModel() {
		super.updateFromModel();
		// As per Bug 319938 - we use getText() instead of textValue for this
		// check, to have it done on the String that the databinging sees. Some
		// subclasses such as NumericTextRidget override getText() and return a
		// value different from textInternal. In retrospect getText() should
		// have
		// been final.
		checkAllRules(getText(), new ValidationCallback(false));
	}

	private final class CRKeyListener implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent event) {
			if (event.getCode() == KeyCode.ENTER) {
				updateTextValue();
			}
		}

	}

	private final class FocusChangeListener implements ChangeListener<Boolean> {

		@Override
		public void changed(ObservableValue<? extends Boolean> observable,
				Boolean oldValue, Boolean focused) {
			if (oldValue != focused) {
				if (focused) {
					if (isFocusable() && !isOutputOnly()) {
						selectAll();
					}
				} else {
					updateTextValue();
				}
			}
		}

	}

	private final class TextChangeListener implements ChangeListener<String> {

		@Override
		public void changed(ObservableValue<? extends String> observable,
				String oldValue, String newValue) {
			if (isDirectWriting) {
				updateTextValue();
			}
			final String text = getUIText();
			disableMandatoryMarkers(isNotEmpty(text));
		}

	}

}
