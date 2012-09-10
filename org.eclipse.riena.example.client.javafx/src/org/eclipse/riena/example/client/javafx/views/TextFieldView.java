/**
 * 
 */
package org.eclipse.riena.example.client.javafx.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import org.eclipse.riena.navigation.ui.javafx.views.JavaFxSubModuleView;

/**
 * @author tsc
 * 
 */
public class TextFieldView extends JavaFxSubModuleView {

	@Override
	protected Scene createScene() {

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));

		int x = 0;
		int y = 0;
		Label label = new Label("Text Field:");
		grid.getChildren().add(label);
		GridPane.setConstraints(label, x, y, 1, 1);

		x++;
		TextField textField = new TextField();
		grid.getChildren().add(textField);
		GridPane.setConstraints(textField, x, y, 1, 1);
		addUIControl(textField, "textField");

		y++;
		x = 0;
		label = new Label("Model:");
		grid.getChildren().add(label);
		GridPane.setConstraints(label, x, y, 1, 1);

		x++;
		TextField modelTextField = new TextField();
		grid.getChildren().add(modelTextField);
		GridPane.setConstraints(modelTextField, x, y, 1, 1);
		addUIControl(modelTextField, "modelTextField");

		y++;
		x = 0;
		label = new Label("Text Field (direct writing):");
		grid.getChildren().add(label);
		GridPane.setConstraints(label, x, y, 1, 1);

		x++;
		textField = new TextField();
		grid.getChildren().add(textField);
		GridPane.setConstraints(textField, x, y, 1, 1);
		addUIControl(textField, "textFieldDirect");

		y++;
		x = 0;
		label = new Label("Model:");
		grid.getChildren().add(label);
		GridPane.setConstraints(label, x, y, 1, 1);

		x++;
		modelTextField = new TextField();
		grid.getChildren().add(modelTextField);
		GridPane.setConstraints(modelTextField, x, y, 1, 1);
		addUIControl(modelTextField, "modelTextFieldDirect");

		y++;
		x = 0;
		label = new Label("Text Field (validation):");
		grid.getChildren().add(label);
		GridPane.setConstraints(label, x, y, 1, 1);

		x++;
		textField = new TextField();
		grid.getChildren().add(textField);
		GridPane.setConstraints(textField, x, y, 1, 1);
		addUIControl(textField, "textFieldValidation");

		y++;
		x = 0;
		label = new Label("Model:");
		grid.getChildren().add(label);
		GridPane.setConstraints(label, x, y, 1, 1);

		x++;
		modelTextField = new TextField();
		grid.getChildren().add(modelTextField);
		GridPane.setConstraints(modelTextField, x, y, 1, 1);
		addUIControl(modelTextField, "modelTextFieldValidation");

		return new Scene(grid);

	}

}