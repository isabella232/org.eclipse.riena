/**
 * 
 */
package org.eclipse.riena.example.client.javafx.views;

import javafx.beans.property.adapter.JavaBeanIntegerProperty;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanStringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.beans.property.adapter.ReadOnlyJavaBeanStringProperty;
import javafx.beans.property.adapter.ReadOnlyJavaBeanStringPropertyBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.converter.NumberStringConverter;

import org.eclipse.riena.navigation.ui.javafx.views.AbstractJavaFxSubModuleView;

/**
 * @author tsc
 * 
 */
public class JavaFxAdditionView extends AbstractJavaFxSubModuleView {

	@Override
	protected Scene createScene() {

		GridPane pane = new GridPane();
		pane.setPadding(new Insets(5.0));
		pane.setHgap(5.0);
		pane.setVgap(5.0);

		Scene scene = new Scene(pane);

		try {
			final CalcModel model = new CalcModel();
			final JavaBeanStringProperty value1Prop = JavaBeanStringPropertyBuilder
					.create().bean(model).name("value1").build();
			final JavaBeanStringProperty value2Prop = JavaBeanStringPropertyBuilder
					.create().bean(model).name("value2").build();
			final ReadOnlyJavaBeanStringProperty resultProp = ReadOnlyJavaBeanStringPropertyBuilder
					.create().bean(model).name("result").build();

			final JavaBeanIntegerProperty intValue1Prop = JavaBeanIntegerPropertyBuilder
					.create().bean(model).name("intValue1").build();

			final TextField value1TextField = new TextField();
			value1TextField.textProperty().bindBidirectional(intValue1Prop,
					new NumberStringConverter());
			pane.add(value1TextField, 1, 0);

			Label addLabel = new Label("+");
			pane.add(addLabel, 0, 1);

			final TextField value2TextField = new TextField();
			value2TextField.textProperty().bindBidirectional(value2Prop);
			pane.add(value2TextField, 1, 1);

			final Button calcButton = new Button("Calculate");
			calcButton.setMaxWidth(Double.MAX_VALUE);
			pane.add(calcButton, 1, 2);

			final Label resultLabel = new Label("=");
			GridPane.setConstraints(resultLabel, 0, 3);
			pane.getChildren().add(resultLabel);

			final TextField resultTextField = new TextField();
			resultTextField.textProperty().bind(resultProp);
			resultTextField.setEditable(false);
			pane.add(resultTextField, 1, 3);

			final Button clearButton = new Button("Clear all");
			clearButton.setMaxWidth(Double.MAX_VALUE);
			pane.add(clearButton, 1, 4);

			calcButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					System.out.println("value1Prop: " + value1Prop);
					System.out.println("value2Prop: " + value2Prop);
					model.calc();
					resultTextField.textProperty().unbind();
					resultTextField.textProperty().setValue(model.getResult());
				}

				public void dummy() {
					System.out.println("intValue1Prop: " + intValue1Prop);
				}

			});

			clearButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					model.clear();
				}

			});

			// addUIControl(button, "jfxButton");
			// JavaFxBindingPropertyLocator.getInstance().setBindingProperty(button,
			// "jfxButton");

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return scene;

	}

	public class CalcModel {

		private int intValue1 = 11;
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

		private void setResult(String result) {
			this.result = result;
		}

		public void calc() {

			double val1 = 0.0;
			try {
				val1 = Double.parseDouble(getValue1());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			double val2 = 0.0;
			try {
				val2 = Double.parseDouble(getValue2());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			val1 = getIntValue1();
			double res = val1 + val2;
			setResult(Double.toString(res));

		}

		public int getIntValue1() {
			return intValue1;
		}

		public void setIntValue1(int intValue1) {
			this.intValue1 = intValue1;
			System.out.println("JavaFxAdditionView.CalcModel.setIntValue1(): "
					+ this.intValue1);
		}

		public void clear() {
			setValue1("");
			setValue2("");
			setIntValue1(0);
			setResult("");
		}

	}

}