/**
 * 
 */
package org.eclipse.riena.example.client.javafx.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import org.eclipse.riena.navigation.ui.javafx.views.JavaFxSubModuleView;
import org.eclipse.riena.ui.swt.MessageBox;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.widgets.Composite;

/**
 * @author tsc
 * 
 */
public class UserView extends JavaFxSubModuleView {

	private static final int TEXT_FIELD_WIDTH = 200;
	private static final int LIGHTS_WIDTH = 25;
	private static final int BUTTON_WIDTH = 75;

	@Override
	protected void basicCreatePartControl(Composite parent) {

		super.basicCreatePartControl(parent);

		final MessageBox messageBox = UIControlsFactory
				.createMessageBox(parent);
		addUIControl(messageBox, "messageBox"); //$NON-NLS-1$

	}

	@Override
	protected Scene createScene() {

		BorderPane pane = new BorderPane();

		Pane top = createTop();
		pane.setTop(top);

		Pane bottom = createBottom();
		pane.setBottom(bottom);

		return new Scene(pane);

	}

	private Pane createTop() {

		GridPane top = new GridPane();
		top.setHgap(10);
		top.setVgap(10);
		top.setPadding(new Insets(20, 20, 20, 20));

		int x = 0;
		int y = 0;
		Label label = new Label("Firstname:"); //$NON-NLS-1$
		top.getChildren().add(label);
		GridPane.setConstraints(label, x, y);
		x++;
		TextField txtFirst = new TextField();
		txtFirst.setPrefWidth(TEXT_FIELD_WIDTH);
		top.getChildren().add(txtFirst);
		GridPane.setConstraints(txtFirst, x, y);
		addUIControl(txtFirst, "txtFirst");

		x = 0;
		y++;
		label = new Label("Lastname:"); //$NON-NLS-1$
		top.getChildren().add(label);
		GridPane.setConstraints(label, x, y);
		x++;
		TextField txtLast = new TextField();
		txtLast.setPrefWidth(TEXT_FIELD_WIDTH);
		top.getChildren().add(txtLast);
		GridPane.setConstraints(txtLast, x, y);
		addUIControl(txtLast, "txtLast");

		x = 0;
		y++;
		label = new Label("Username:"); //$NON-NLS-1$
		top.getChildren().add(label);
		GridPane.setConstraints(label, x, y);
		x++;
		TextField txtUser = new TextField();
		txtUser.setPrefWidth(TEXT_FIELD_WIDTH);
		top.getChildren().add(txtUser);
		GridPane.setConstraints(txtUser, x, y);
		addUIControl(txtUser, "txtUser");

		x = 0;
		y++;
		label = new Label("Password:"); //$NON-NLS-1$
		top.getChildren().add(label);
		GridPane.setConstraints(label, x, y);
		x++;
		TextField txtPassword = new PasswordField();
		txtPassword.setPrefWidth(TEXT_FIELD_WIDTH);
		top.getChildren().add(txtPassword);
		GridPane.setConstraints(txtPassword, x, y);
		addUIControl(txtPassword, "txtPassword");
		x++;
		Label lblLights = new Label("a");
		lblLights.setPrefWidth(LIGHTS_WIDTH);
		top.getChildren().add(lblLights);
		GridPane.setConstraints(lblLights, x, y);
		addUIControl(lblLights, "lblLights");

		x = 0;
		y++;
		label = new Label("Comfirm:"); //$NON-NLS-1$
		top.getChildren().add(label);
		GridPane.setConstraints(label, x, y);
		x++;
		TextField txtConfirm = new PasswordField();
		txtConfirm.setPrefWidth(TEXT_FIELD_WIDTH);
		top.getChildren().add(txtConfirm);
		GridPane.setConstraints(txtConfirm, x, y);
		addUIControl(txtConfirm, "txtConfirm");

		return top;

	}

	private Pane createBottom() {

		BorderPane bottom = new BorderPane();

		bottom.setTop(new Separator());

		HBox btnPane = new HBox();
		btnPane.setSpacing(10);
		btnPane.setPadding(new Insets(10, 20, 10, 20));

		Button btnSave = new Button("Save");
		btnPane.getChildren().add(btnSave);
		btnSave.setPrefWidth(BUTTON_WIDTH);
		addUIControl(btnSave, "btnSave");

		Button btnEdit = new Button("Edit");
		btnPane.getChildren().add(btnEdit);
		btnEdit.setPrefWidth(BUTTON_WIDTH);
		addUIControl(btnEdit, "btnEdit");

		Button btnDelete = new Button("Delete");
		btnPane.getChildren().add(btnDelete);
		btnDelete.setPrefWidth(BUTTON_WIDTH);
		addUIControl(btnDelete, "btnDelete");

		bottom.setLeft(btnPane);

		return bottom;

	}

}