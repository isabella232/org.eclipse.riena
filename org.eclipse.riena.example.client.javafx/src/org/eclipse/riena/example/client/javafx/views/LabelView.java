/**
 * 
 */
package org.eclipse.riena.example.client.javafx.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import org.eclipse.riena.navigation.ui.javafx.views.JavaFxSubModuleView;

/**
 * @author tsc
 * 
 */
public class LabelView extends JavaFxSubModuleView {

	@Override
	protected Scene createScene() {

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));

		int x = 0;
		int y = 0;
		Label label = new Label("Label bounded with String:");
		grid.getChildren().add(label);
		GridPane.setConstraints(label, x, y, 1, 1);

		x++;
		Label stringLabel = new Label();
		GridPane.setConstraints(stringLabel, x, y, 1, 1);
		grid.getChildren().add(stringLabel);
		addUIControl(stringLabel, "stringLabel");

		x = 0;
		y++;
		label = new Label("Label bounded with int:");
		grid.getChildren().add(label);
		GridPane.setConstraints(label, x, y, 1, 1);

		x++;
		Label intLabel = new Label();
		GridPane.setConstraints(intLabel, x, y, 1, 1);
		grid.getChildren().add(intLabel);
		addUIControl(intLabel, "intLabel");

		x++;
		label = new Label(
				"(increase with left click / decrease with right click)");
		grid.getChildren().add(label);
		GridPane.setConstraints(label, x, y, 1, 1);
		addUIControl(label, "incLabel");

		x = 0;
		y++;
		label = new Label("Label bounded with double:");
		grid.getChildren().add(label);
		GridPane.setConstraints(label, x, y, 1, 1);

		x++;
		Label doubleLabel = new Label();
		GridPane.setConstraints(doubleLabel, x, y, 1, 1);
		grid.getChildren().add(doubleLabel);
		addUIControl(doubleLabel, "doubleLabel");

		x++;
		label = new Label("(enable/disable with double click)");
		grid.getChildren().add(label);
		GridPane.setConstraints(label, x, y, 1, 1);
		addUIControl(label, "disableLabel");

		x = 0;
		y++;
		label = new Label("Label bounded with boolean:");
		grid.getChildren().add(label);
		GridPane.setConstraints(label, x, y, 1, 1);

		x++;
		Label booleanLabel = new Label();
		GridPane.setConstraints(booleanLabel, x, y, 1, 1);
		grid.getChildren().add(booleanLabel);
		addUIControl(booleanLabel, "booleanLabel");

		x++;
		label = new Label("(show/hidde with double click)");
		grid.getChildren().add(label);
		GridPane.setConstraints(label, x, y, 1, 1);
		addUIControl(label, "hideLabel");

		x = 0;
		y++;
		label = new Label("Label with image:");
		grid.getChildren().add(label);
		GridPane.setConstraints(label, x, y, 1, 1);

		x++;
		Label imageLabel = new Label("Image");
		GridPane.setConstraints(imageLabel, x, y, 1, 1);
		grid.getChildren().add(imageLabel);
		addUIControl(imageLabel, "imageLabel");

		x++;
		label = new Label("(cool image left click / eclipse image right click)");
		grid.getChildren().add(label);
		GridPane.setConstraints(label, x, y, 1, 1);
		addUIControl(label, "changeImageLabel");

		return new Scene(grid);

	}
}