package application.kosmas;

import java.io.File;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import application.utils.AppUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class KosmasSettingsController {

	@FXML
	private VBox root;

	@FXML
	private JFXTextField pathInput;

	@FXML
	private JFXTextField emailInput;

	@FXML
	private JFXPasswordField passwordInput;

	@FXML
	private JFXButton findPathButton;

	@FXML
	private JFXButton saveButton;

	@FXML
	private JFXButton cancelButton;

	@FXML
	private Label errorLabel;

	@FXML
	void initialize() {
		pathInput.setText(KosmasModel.getInstance().getSettings().getPath());
		emailInput.setText(KosmasModel.getInstance().getSettings().getId());
		passwordInput.setText(KosmasModel.getInstance().getSettings().getPassword());
	}

	@FXML
	void handleFindPathButton(ActionEvent event) {
		try {
			File path = KosmasModel.getInstance().chooseDirectory(findPathButton.getScene().getWindow());
			pathInput.setText(path.getAbsolutePath());
		} catch (Exception NullPointerException) {
			System.out.println("NPE Žádná složka nebyla vybrána.");
		}
	}

	@FXML
	void handleCancelButton(ActionEvent event) {
		closeWindow();
	}

	@FXML
	void handleSaveButton(ActionEvent event) {
		if (AppUtils.isInvalidInput(pathInput) || AppUtils.isInvalidInput(emailInput)
				|| AppUtils.isInvalidInput(passwordInput)) {
			errorLabel.setText("Pole musí být vyplněné");
		} else if (!AppUtils.isDirectory(pathInput.getText())) {
			errorLabel.setText("Složka neexistuje");
		} else if (AppUtils.isInvalidLength(emailInput)) {
			errorLabel.setText("ID může mít maximalně 5 znaků");
		} else {
			KosmasModel.getInstance().saveSettings(pathInput.getText(), emailInput.getText(), passwordInput.getText());
			closeWindow();
		}
	}

	private void closeWindow() {
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}

}
