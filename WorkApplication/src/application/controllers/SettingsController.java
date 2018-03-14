package application.controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;

import application.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class SettingsController implements Initializable {

	@FXML
	private VBox root;

	@FXML
	private VBox rootKosmas;

	@FXML
	private JFXTextField pathInput;

	@FXML
	private JFXTextField emailInput;

	@FXML
	private JFXPasswordField passwordInput;

	@FXML
	private JFXButton saveButton;

	@FXML
	private JFXButton findPathButton;

	@FXML
	private JFXTextField pathInputKosmas;

	@FXML
	private JFXButton findPathButtonKosmas;

	@FXML
	private JFXTextField emailInputKosmas;

	@FXML
	private JFXPasswordField passwordImputKosmas;

	@FXML
	private JFXButton saveButtonKosmas;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		pathInput.setText(Model.getInstance().getSettigns().getPath());
		emailInput.setText(Model.getInstance().getSettigns().getEuroId());
		passwordInput.setText(Model.getInstance().getSettigns().getEuroPassword());

	}

	@FXML
	void handleSaveButton(ActionEvent event) {
		Model.getInstance().saveEuroSettings(pathInput.getText(), emailInput.getText(), passwordInput.getText());
		JFXSnackbar snackbar = new JFXSnackbar(root);
		snackbar.setPrefWidth(100);
		snackbar.show("Uloženo", 2000);
	}

	@FXML
	void handleFindPathButton(ActionEvent event) {
		try {
			File path = Model.getInstance().chooseDirectory(findPathButton.getScene().getWindow());
			pathInput.setText(path.getAbsolutePath());
		} catch (Exception NullPointerException) {
			System.out.println("NPE Žádná složka nebyla vybrána.");
		}
	}

	@FXML
	void handleFindPathButtonKosmas(ActionEvent event) {

	}

	@FXML
	void handleSaveButtonKosmas(ActionEvent event) {

	}
}