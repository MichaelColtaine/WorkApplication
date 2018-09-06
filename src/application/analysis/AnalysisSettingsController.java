package application.analysis;

import java.io.File;
import java.io.FileInputStream;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import application.utils.AppUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class AnalysisSettingsController {

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
		pathInput.setText(AnalysisModel.getInstance().getSettings().getPath());

	}

	@FXML
	void handleFindPathButton(ActionEvent event) {
		try {
			DirectoryChooser dc = new DirectoryChooser();
			File path = dc.showDialog(pathInput.getScene().getWindow());
			if (path != null) {
				pathInput.setText(path.getAbsolutePath());
			}
		} catch (Exception NullPointerException) {
			System.out.println("NPE Žádná složka nebyla vybrána.");
		}
	}

	@FXML
	void handleSaveButton(ActionEvent event) {
		if (!AppUtils.isDirectory(pathInput.getText())) {
			errorLabel.setText("Složka neexistuje");
		} else {
			AnalysisModel.getInstance().getSettings().savePath(pathInput.getText());
			closeWindow();
		}
	}

	@FXML
	void handleCancelButton(ActionEvent event) {
		closeWindow();
	}

	private void closeWindow() {
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}

}