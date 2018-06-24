package application.albatros;

import java.io.File;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import application.euromedia.EuroModel;
import application.utils.AppUtils;
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
	private JFXTextField idInput;

	@FXML
	private JFXPasswordField passwordInput;

	@FXML
	private JFXButton saveButton;

	@FXML
	private JFXButton cancelButton;

	@FXML
	private Label errorLabel;

	@FXML
	void initialize() {
		this.pathInput.setText(AlbatrosModel.getInstance().getSettings().getPath());
		this.idInput.setText(AlbatrosModel.getInstance().getSettings().getId());
		this.passwordInput.setText(AlbatrosModel.getInstance().getSettings().getPassword());
	}

	@FXML
	void handleCancelButton(ActionEvent event) {
		closeWindow();
	}

	@FXML
	void handleFindPathButton(ActionEvent event) {
		try {
			File path = AlbatrosModel.getInstance().chooseDirectory(findPathButton.getScene().getWindow());
			pathInput.setText(path.getAbsolutePath());
		} catch (Exception NullPointerException) {
			System.out.println("NPE Žádná složka nebyla vybrána.");
		}
	}

	@FXML
	void handleSaveButton(ActionEvent event) {
		if (AppUtils.isInvalidInput(pathInput) || AppUtils.isInvalidInput(idInput)
				|| AppUtils.isInvalidInput(passwordInput)) {
			errorLabel.setText("Pole musí být vyplněné");
		} else if (!AppUtils.isDirectory(pathInput.getText())) {
			errorLabel.setText("Složka neexistuje");
		} else {
			AlbatrosModel.getInstance().saveSettings(pathInput.getText(), idInput.getText(), passwordInput.getText());
			closeWindow();
		}
	}

	private void closeWindow() {
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}

}
