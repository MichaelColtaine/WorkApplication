package application.beta;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import application.euromedia.EuroModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class BetaSettingsController {

	@FXML
	private Label errorLabel;

	@FXML
	private JFXButton saveButton;

	@FXML
	private JFXButton cancelButton;

	@FXML
	private JFXTextField fromInput;

	@FXML
	private JFXButton findFromPath;

	@FXML
	private JFXTextField toInput;

	@FXML
	private JFXButton findToPath;

	@FXML
	void handleFromPathButton(ActionEvent event) {
		selectDirectory(fromInput, findFromPath);
	}

	@FXML
	void handleToPathButton(ActionEvent event) {
		selectDirectory(toInput, findToPath);
	}

	@FXML
	void initialize() {
		this.toInput.setText(BetaModel.getInstance().getToPath());
		this.fromInput.setText(BetaModel.getInstance().getFromPath());
	}

	private void selectDirectory(JFXTextField textField, JFXButton button) {
		try {
			File path = EuroModel.getInstance().chooseDirectory(button.getScene().getWindow());
			textField.setText(path.getAbsolutePath());
		} catch (Exception NullPointerException) {
			System.out.println("NPE Žádná složka nebyla vybrána.");
		}
	}

	@FXML
	void handleSaveButton(ActionEvent event) {
		if (isInvalidInput()) {
			errorLabel.setText("Obě pole musí být vyplněná!");
		} else {
			BetaModel.getInstance().savePaths(fromInput.getText(), toInput.getText());
			closeWindow();
		}
	}

	private boolean isInvalidInput() {
		return (fromInput.getText().isEmpty() || toInput.getText().isEmpty());
	}

	@FXML
	void handleCancelButton(ActionEvent event) {
		closeWindow();
	}

	private void closeWindow() {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}

}
