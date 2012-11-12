package org.eclipse.riena.example.client.javafx.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import org.eclipse.riena.navigation.ui.javafx.views.JavaFxSubModuleView;
import org.eclipse.riena.ui.javafx.utils.JavaFxBindingPropertyLocator;

/**
 * @author sgo
 * 
 */
public class ToggleButtonView extends JavaFxSubModuleView {

	@Override
	protected Scene createScene() {

		final JavaFxBindingPropertyLocator propertyLocator = JavaFxBindingPropertyLocator
				.getInstance();

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));
		
		GridPane manToggleButtonPane = new GridPane();
		// manToggleButtonPane.setHgap(10);
		manToggleButtonPane.setVgap(10);
		manToggleButtonPane.setPadding(new Insets(0, 0, -10, 0));
		
		
		ToggleButton mandatoryToggleBtn1 = new ToggleButton("mandatory");
		// mandatoryToggleBtn1.setId("toggle-button");
		mandatoryToggleBtn1.setMinHeight(22);
		mandatoryToggleBtn1.setMinWidth(80);
		manToggleButtonPane.getChildren().add(mandatoryToggleBtn1);
		GridPane.setConstraints(mandatoryToggleBtn1, 1, 1, 1, 1);
		propertyLocator.setBindingProperty(mandatoryToggleBtn1, "manToggleBtn1");
		
		ToggleButton mandatoryToggleBtn2 = new ToggleButton("mandatory");
		// mandatoryToggleBtn2.setId("toggle-button");
		mandatoryToggleBtn2.setMinHeight(22);
		mandatoryToggleBtn2.setMinWidth(80);
		manToggleButtonPane.getChildren().add(mandatoryToggleBtn2);
		GridPane.setConstraints(mandatoryToggleBtn2, 1, 2, 1, 1);
		propertyLocator.setBindingProperty(mandatoryToggleBtn2, "manToggleBtn2");
			
		Button greenIconButton = new Button("");
		greenIconButton.setStyle("-fx-body-color: white; -fx-background-color: white; -fx-control-inner-background: white");
		manToggleButtonPane.getChildren().add(greenIconButton);
		GridPane.setConstraints(greenIconButton, 2, 1, 1, 1);
		propertyLocator.setBindingProperty(greenIconButton, "greenIconBtn");
		
		Button redIconButton = new Button("");
		redIconButton.setStyle("-fx-body-color: white; -fx-background-color: white; -fx-control-inner-background: white");
		manToggleButtonPane.getChildren().add(redIconButton);
		GridPane.setConstraints(redIconButton, 2, 2, 1, 1);
		propertyLocator.setBindingProperty(redIconButton, "redIconBtn");
		
		grid.getChildren().add(manToggleButtonPane);
		GridPane.setConstraints(manToggleButtonPane, 1, 1, 1, 1);
		
		
		ToggleButton hiddenToggleButton1 = new ToggleButton("toggle");
		hiddenToggleButton1.setMinHeight(22);
		hiddenToggleButton1.setMinWidth(80);
		grid.getChildren().add(hiddenToggleButton1);
		GridPane.setConstraints(hiddenToggleButton1, 1, 3, 1, 1);
		propertyLocator.setBindingProperty(hiddenToggleButton1, "hiddenToggleBtn1");
		
		ToggleButton hiddenToggleButton2 = new ToggleButton("toggle");
		hiddenToggleButton2.setMinHeight(22);
		hiddenToggleButton2.setMinWidth(80);
		grid.getChildren().add(hiddenToggleButton2);
		GridPane.setConstraints(hiddenToggleButton2, 1, 4, 1, 1);
		propertyLocator.setBindingProperty(hiddenToggleButton2, "hiddenToggleBtn2");
				
		Button toggleVisibility = new Button("Show/Hide");
		toggleVisibility.setMinHeight(22);
		toggleVisibility.setMinWidth(90);
		grid.getChildren().add(toggleVisibility);
		GridPane.setConstraints(toggleVisibility, 2, 3, 1, 1);
		propertyLocator.setBindingProperty(toggleVisibility, "toggleVisibilityBtn");
		
		
		ToggleButton disabledToggleButton1 = new ToggleButton("disabled");
		disabledToggleButton1.setMinHeight(22);
		disabledToggleButton1.setMinWidth(80);
		grid.getChildren().add(disabledToggleButton1);
		GridPane.setConstraints(disabledToggleButton1, 1, 5, 1, 1);
		propertyLocator.setBindingProperty(disabledToggleButton1, "disabledToggleBtn1");
		
		ToggleButton disabledToggleButton2 = new ToggleButton("disabled");
		disabledToggleButton2.setMinHeight(22);
		disabledToggleButton2.setMinWidth(80);
		grid.getChildren().add(disabledToggleButton2);
		GridPane.setConstraints(disabledToggleButton2, 1, 6, 1, 1);
		propertyLocator.setBindingProperty(disabledToggleButton2, "disabledToggleBtn2");
		
		Button toggleDisabled = new Button("Dis-/Enable");
		toggleDisabled.setMinHeight(22);
		toggleDisabled.setMinWidth(90);
		grid.getChildren().add(toggleDisabled);
		GridPane.setConstraints(toggleDisabled, 2, 5, 1, 1);
		propertyLocator.setBindingProperty(toggleDisabled, "toggleDisabledBtn");
		
		
		ToggleButton outputOnlyButton1 = new ToggleButton("output only");
		outputOnlyButton1.setMinHeight(22);
		outputOnlyButton1.setMinWidth(80);
		grid.getChildren().add(outputOnlyButton1);
		GridPane.setConstraints(outputOnlyButton1, 1, 7, 1, 1);
		propertyLocator.setBindingProperty(outputOnlyButton1, "outputOnlyBtn1");
		
		ToggleButton outputOnlyButton2 = new ToggleButton("output only");
		outputOnlyButton2.setMinHeight(22);
		outputOnlyButton2.setMinWidth(80);
		grid.getChildren().add(outputOnlyButton2);
		GridPane.setConstraints(outputOnlyButton2, 1, 8, 1, 1);
		propertyLocator.setBindingProperty(outputOnlyButton2, "outputOnlyBtn2");
		
		Button toggleModelState = new Button("toggleModel");
		toggleModelState.setMinHeight(22);
		toggleModelState.setMinWidth(90);
		grid.getChildren().add(toggleModelState);
		GridPane.setConstraints(toggleModelState, 2, 7, 1, 1);
		propertyLocator.setBindingProperty(toggleModelState, "toggleModelStateBtn");
		
		Label simpleLabel = new Label("model state: false");
		GridPane.setConstraints(simpleLabel, 2, 8, 1, 1);
		grid.getChildren().add(simpleLabel);
		propertyLocator.setBindingProperty(simpleLabel, "simpleLbl");
		
		
		
		
		
//		CheckBox checkBox = new CheckBox();
//		// button2.setStyle("-fx-body-color: white; -fx-background-color: white; -fx-control-inner-background: white");
//		grid.getChildren().add(checkBox);
//		GridPane.setConstraints(checkBox, 1, 3, 1, 1);
//		propertyLocator.setBindingProperty(checkBox, "checkBox");
//		
//		CheckBox checkBox2 = new CheckBox();
//		// button2.setStyle("-fx-body-color: white; -fx-background-color: white; -fx-control-inner-background: white");
//		grid.getChildren().add(checkBox2);
//		GridPane.setConstraints(checkBox2, 2, 3, 1, 1);
//		propertyLocator.setBindingProperty(checkBox2, "checkBox2");
		
		
		
		
//		Label simpleLabel = new Label("ToggleButton now active...");
//		GridPane.setConstraints(simpleLabel, 2, 1, 1, 1);
//		grid.getChildren().add(simpleLabel);
//		propertyLocator.setBindingProperty(simpleLabel, "simpleLbl");
		
		
			    
		return new Scene(grid);

	}

}