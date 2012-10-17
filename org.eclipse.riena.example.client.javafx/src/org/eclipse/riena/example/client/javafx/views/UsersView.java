/**
 * 
 */
package org.eclipse.riena.example.client.javafx.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import org.eclipse.riena.navigation.ui.javafx.views.JavaFxSubModuleView;

/**
 * @author tsc
 * 
 */
public class UsersView extends JavaFxSubModuleView {

	private static final int BUTTON_WIDTH = 125;

	@Override
	protected Scene createScene() {

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(20, 20, 20, 20));

		Button button = new Button("Create User");
		button.setPrefWidth(BUTTON_WIDTH);
		grid.getChildren().add(button);
		GridPane.setConstraints(button, 0, 0);
		addUIControl(button, "btnCreate");

		return new Scene(grid);

	}

}