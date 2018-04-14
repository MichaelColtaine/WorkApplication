package application.albatros;

import java.io.File;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import application.euromedia.EuroModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AlbatrosSettingsController {

	@FXML
	private AnchorPane root;

	@FXML
	private JFXTextField pathInput;

	@FXML
	private JFXButton findPathButton;

	@FXML
	private JFXTextField emailInput;

	@FXML
	private JFXPasswordField passwordInput;

	@FXML
	private JFXButton saveButton;

	@FXML
	private JFXButton cancelButton;

	@FXML
	private Label errorLabel;

	@FXML
	void handleCancelButton(ActionEvent event) {
		closeWindow();
	}

	@FXML
	void handleFindPathButton(ActionEvent event) {
		try {
			File path = EuroModel.getInstance().chooseDirectory(findPathButton.getScene().getWindow());
			pathInput.setText(path.getAbsolutePath());
		} catch (Exception NullPointerException) {
			System.out.println("NPE Žádná složka nebyla vybrána.");
		}
	}

	@FXML
	void handleSaveButton(ActionEvent event) {

	}
	
	private void closeWindow() {
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}

}
