/**
 * 
 */
package org.eclipse.riena.example.client.javafx.views;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.riena.navigation.ui.javafx.views.JavaFxSubModuleView;

/**
 * @author tsc
 * 
 */
public class JavaFxAdditionEclipseBindingView extends JavaFxSubModuleView {

	@Override
	protected Scene createScene() {

		GridPane pane = new GridPane();
		pane.setPadding(new Insets(5.0));
		pane.setHgap(5.0);
		pane.setVgap(5.0);

		Scene scene = new Scene(pane);

		final CalcModel model = new CalcModel();
		IObservableValue value1ObserveValue = PojoObservables.observeValue(
				model, "value1");
		IObservableValue value2ObserveValue = PojoObservables.observeValue(
				model, "value2");
		IObservableValue resultObserveValue = PojoObservables.observeValue(
				model, "result");
		final DataBindingContext bindingContext = new DataBindingContext();

		final TextField value1TextField = new TextField();
		pane.add(value1TextField, 1, 0);
		JavaFxPropertyWrapper<String> value1Wrapper = new JavaFxPropertyWrapper<String>(
				value1TextField.textProperty());
		IObservableValue value1TextObserveValue = BeansObservables
				.observeValue(value1Wrapper, "value");

		Label addLabel = new Label("+");
		pane.add(addLabel, 0, 1);

		final TextField value2TextField = new TextField();
		pane.add(value2TextField, 1, 1);
		JavaFxPropertyWrapper<String> value2Wrapper = new JavaFxPropertyWrapper<String>(
				value2TextField.textProperty());
		IObservableValue value2TextObserveValue = BeansObservables
				.observeValue(value2Wrapper, "value");

		final Button calcButton = new Button("Calculate");
		calcButton.setMaxWidth(Double.MAX_VALUE);
		pane.add(calcButton, 1, 2);

		final Label resultLabel = new Label("=");
		GridPane.setConstraints(resultLabel, 0, 3);
		pane.getChildren().add(resultLabel);

		final TextField resultTextField = new TextField();
		resultTextField.setEditable(false);
		pane.add(resultTextField, 1, 3);
		JavaFxPropertyWrapper<String> resultWrapper = new JavaFxPropertyWrapper<String>(
				resultTextField.textProperty());
		IObservableValue resultTextObserveValue = BeansObservables
				.observeValue(resultWrapper, "value");

		final Button clearButton = new Button("Clear all");
		clearButton.setMaxWidth(Double.MAX_VALUE);
		pane.add(clearButton, 1, 4);

		calcButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				model.calc();
				bindingContext.updateTargets();
			}
		});

		clearButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				model.clear();
				bindingContext.updateTargets();
			}

		});

		// addUIControl(button, "jfxButton");
		// JavaFxBindingPropertyLocator.getInstance().setBindingProperty(button,
		// "jfxButton");

		bindingContext.bindValue(value1TextObserveValue, value1ObserveValue);
		bindingContext.bindValue(value2TextObserveValue, value2ObserveValue);
		bindingContext.bindValue(resultTextObserveValue, resultObserveValue);
		bindingContext.updateTargets();

		return scene;

	}

	public class CalcModel {

		private String value1 = "1";
		private String value2 = "2";
		private String result = "3";

		public String getValue1() {
			return value1;
		}

		public void setValue1(String value1) {
			this.value1 = value1;
			System.out.println("JavaFxAdditionView.CalcModel.setValue1(): "
					+ this.value1);
		}

		public String getValue2() {
			return value2;
		}

		public void setValue2(String value2) {
			this.value2 = value2;
			System.out.println("JavaFxAdditionView.CalcModel.setValue2(): "
					+ this.value2);
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		public void calc() {

			int val1 = 0;
			try {
				val1 = Integer.parseInt(getValue1());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			int val2 = 0;
			try {
				val2 = Integer.parseInt(getValue2());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			int res = val1 + val2;
			setResult(Integer.toString(res));

		}

		public void clear() {
			setValue1("");
			setValue2("");
			setResult("");
		}

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

}