package application.other;

import java.io.File;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import application.euromedia.EuroModel;
import application.utils.AppUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class OtherSettingsController {

	@FXML
	private Label errorLabel;

	@FXML
	private JFXButton saveButton;

	@FXML
	private JFXButton cancelButton;

	@FXML
	private JFXTextField fromInput;

	@FXML
	private JFXTextField toInput;

	@FXML
	private JFXButton findFromPath;

	@FXML
	private JFXButton findToPath;

	@FXML
	void initialize() {
		this.toInput.setText(OtherModel.getInstance().getSettings().getToPath());
		this.fromInput.setText(OtherModel.getInstance().getSettings().getFromPath());
	}

	@FXML
	void handleFromPathButton(ActionEvent event) {
		selectDirectory(fromInput, findFromPath);
	}

	@FXML
	void handleToPathButton(ActionEvent event) {
		selectDirectory(toInput, findToPath);
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
		if (AppUtils.isInvalidInput(fromInput) || AppUtils.isInvalidInput(toInput)) {
			errorLabel.setText("Obě pole musí být vyplněná!");
		} else if (!AppUtils.isDirectory(fromInput.getText()) || !AppUtils.isDirectory(toInput.getText())) {
			errorLabel.setText("Složka neexistuje");
		} else {
			OtherModel.getInstance().savePaths(fromInput.getText(), toInput.getText());
			closeWindow();
		}
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
