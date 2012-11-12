/**
 * 
 */
package org.eclipse.riena.example.client.javafx.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import org.eclipse.riena.navigation.ui.javafx.views.JavaFxSubModuleView;
import org.eclipse.riena.ui.javafx.utils.JavaFxBindingPropertyLocator;

/**
 * @author tsc
 * 
 */
public class ButtonView extends JavaFxSubModuleView {

	@Override
	protected Scene createScene() {

		final JavaFxBindingPropertyLocator propertyLocator = JavaFxBindingPropertyLocator
				.getInstance();

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));

		int x = 0;
		int y = 0;
		Button simpleButton = new Button("Simple");
		grid.getChildren().add(simpleButton);
		GridPane.setConstraints(simpleButton, x, y, 1, 1);
		propertyLocator.setBindingProperty(simpleButton, "simpleBtn");
		
		x++;
		Label simpleLabel = new Label("Simple Button never clicked!");
		GridPane.setConstraints(simpleLabel, x, y, 1, 1);
		grid.getChildren().add(simpleLabel);
		propertyLocator.setBindingProperty(simpleLabel, "simpleLbl");

		x = 0;
		y++;
		Button imageButton = new Button("Image");
		grid.getChildren().add(imageButton);
		GridPane.setConstraints(imageButton, x, y, 1, 1);
		propertyLocator.setBindingProperty(imageButton, "imageBtn");

		return new Scene(grid);

	}

}