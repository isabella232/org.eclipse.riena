/**
 * 
 */
package org.eclipse.riena.example.client.javafx.views;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import org.eclipse.riena.navigation.ui.javafx.views.JavaFxSubModuleView;

/**
 * @author tsc
 * 
 */
public class MarkerView extends JavaFxSubModuleView {

	@Override
	protected Scene createScene() {

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(5, 5, 5, 5));
		grid.setHgap(10);
		grid.setVgap(10);

		int x = 0;
		int y = 0;
		Node markerPane = createMarkerPane();
		grid.getChildren().add(markerPane);
		GridPane.setConstraints(markerPane, x, y, 1, 1, HPos.LEFT, VPos.CENTER,
				Priority.ALWAYS, Priority.NEVER);

		x++;
		Node visibilityPane = createVisbilityPane();
		grid.getChildren().add(visibilityPane);
		GridPane.setConstraints(visibilityPane, x, y, 1, 1, HPos.LEFT,
				VPos.CENTER, Priority.ALWAYS, Priority.NEVER);

		x = 0;
		y++;
		Node controlsPane = createControlsPane();
		grid.getChildren().add(controlsPane);
		GridPane.setConstraints(controlsPane, x, y, 2, 1, HPos.LEFT, VPos.TOP,
				Priority.ALWAYS, Priority.ALWAYS);

		return new Scene(grid);

	}

	private Node createMarkerPane() {

		TitledPane markerPane = new TitledPane();
		markerPane.setText("Marker Options");
		markerPane.setCollapsible(false);
		markerPane.setFocusTraversable(false);

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(5, 5, 5, 5));
		grid.setHgap(10);

		int x = 0;
		int y = 0;
		CheckBox mandatoryCheckBox = new CheckBox("mandatory");
		grid.add(mandatoryCheckBox, x, y);

		x++;
		CheckBox errorCheckBox = new CheckBox("error");
		grid.add(errorCheckBox, x, y);

		x++;
		CheckBox disabledCheckBox = new CheckBox("disabled");
		grid.add(disabledCheckBox, x, y);

		x++;
		CheckBox outputCheckBox = new CheckBox("output");
		grid.add(outputCheckBox, x, y);

		markerPane.setContent(grid);

		return markerPane;

	}

	private Node createVisbilityPane() {

		TitledPane visbilityPane = new TitledPane();
		visbilityPane.setText("Visbility Options");
		visbilityPane.setCollapsible(false);
		visbilityPane.setFocusTraversable(false);

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(5, 5, 5, 5));
		grid.setHgap(10);

		int x = 0;
		int y = 0;
		CheckBox hiddenCheckBox = new CheckBox("hidden");
		grid.add(hiddenCheckBox, x, y);

		x++;
		CheckBox hiddenParentCheckBox = new CheckBox("hidden parent");
		grid.add(hiddenParentCheckBox, x, y);

		visbilityPane.setContent(grid);

		return visbilityPane;

	}

	private Node createControlsPane() {

		TitledPane visbilityPane = new TitledPane();
		visbilityPane.setText("UI Controls");
		visbilityPane.setCollapsible(false);
		visbilityPane.setFocusTraversable(false);

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(5, 5, 5, 5));
		grid.setHgap(10);

		int x = 0;
		int y = 0;
		Label nameLabel = new Label("Name:");
		grid.add(nameLabel, x, y);

		x++;
		TextField nameTextField = new TextField();
		grid.getChildren().add(nameTextField);
		GridPane.setConstraints(nameTextField, x, y, 1, 1, HPos.LEFT,
				VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
		visbilityPane.setContent(grid);

		return visbilityPane;

	}

}