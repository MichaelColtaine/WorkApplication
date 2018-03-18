package application.beta;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import application.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BetaController {

	@FXML
	private AnchorPane root;

	@FXML
	private JFXButton settingsButton;

	@FXML
	private JFXButton moveButton;

	@FXML
	private ProgressIndicator progress;

	@FXML
	private Label doneLabel;

	@FXML
	void handleSettingsButtonAction(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("BetaSettings.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.setTitle("Nastavení Beta");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML

	void handleMoveButton() {
		
			progress.setVisible(true);
			BetaModel.getInstance().moveAndRename();
			progress.setVisible(false);
			doneLabel.setText("Hotovo");
		
	}

}
