package application.scanner;

import java.io.File;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import application.utils.AppUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ScannerSettingsController {

	@FXML
	private VBox root;

	@FXML
	private JFXTextField pathInput;

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
		pathInput.setText(ScannerModel.getInstance().getSettings().getPath());
	}

	@FXML
	void handleFindPathButton(ActionEvent event) {
		try {
			DirectoryChooser dc = new DirectoryChooser();
			File path = dc.showDialog(pathInput.getScene().getWindow());
			if (path != null) {
				pathInput.setText(path.getAbsolutePath());
				ScannerModel.getInstance().getSettings().savePath(pathInput.getText());
			}
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
		if (!AppUtils.isDirectory(pathInput.getText())) {
			errorLabel.setText("Složka neexistuje");
		} else {
			ScannerModel.getInstance().getSettings().savePath(pathInput.getText());
			closeWindow();
		}
	}

	private void closeWindow() {
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}

}