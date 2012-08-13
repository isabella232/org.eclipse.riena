/**
 * 
 */
package org.eclipse.riena.example.client.javafx.views;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import org.eclipse.riena.navigation.ui.javafx.views.AbstractJavaFxSubModuleView;
import org.eclipse.riena.ui.javafx.utils.JavaFxBindingPropertyLocator;

/**
 * @author tsc
 * 
 */
public class JavaFxAdditionView extends AbstractJavaFxSubModuleView {

	@Override
	protected Scene createScene() {

		HBox pane = new HBox();
		Scene scene = new Scene(pane);
		Button button = new Button("JFX Button");
		pane.getChildren().add(button);

		// addUIControl(button, "jfxButton");
		JavaFxBindingPropertyLocator.getInstance().setBindingProperty(button,
				"jfxButton");

		return scene;

	}
}