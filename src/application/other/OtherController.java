package application.other;

import java.io.File;
import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import application.infobar.InfoModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class OtherController {

	@FXML
	private AnchorPane root;

	@FXML
	private JFXButton settingsButton;

	@FXML
	private JFXButton moveButton;

	@FXML
	private RadioButton marcoButton;

	@FXML
	private RadioButton pemicButton;

	@FXML
	private ProgressIndicator progress;

	@FXML
	private RadioButton gradaButton;

	@FXML
	private RadioButton omegaButton;

	@FXML
	private RadioButton prescoButton;

	@FXML
	private ToggleGroup system;

	@FXML
	void handleMoveButton(ActionEvent event) {

		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				if (isFolderEmpty()) {
					InfoModel.getInstance().updateInfo("Složka je prázdná!");
				} else {
					if (gradaButton.isSelected()) {
						handleGradaButton();
					} else if (marcoButton.isSelected()) {
						handleMarcoButton();
					} else if (pemicButton.isSelected()) {
						handlePemicButon();
					} else if (omegaButton.isSelected()) {
						handleOmegaButton();
					} else if (prescoButton.isSelected()) {
						handlePrescoButton();
					}
				}
			}
		});
		t1.start();
	}

	public boolean isFolderEmpty() {
		File file = new File(OtherModel.getInstance().getFromPath());
		return file.listFiles().length == 0;
	}

	private void handlePrescoButton() {
		OtherModel.getInstance().handlePrescoButton();
		InfoModel.getInstance().updateInfo("Hotovo!");
	}

	private void handleOmegaButton() {
		OtherModel.getInstance().handleOmegaButton();
		InfoModel.getInstance().updateInfo("Hotovo!");
	}

	private void handlePemicButon() {
		OtherModel.getInstance().handlePemicButton();
		InfoModel.getInstance().updateInfo("Hotovo!");
	}

	private void handleGradaButton() {
		OtherModel.getInstance().handleGradaButton();
		InfoModel.getInstance().updateInfo("Hotovo!");
	}

	private void handleMarcoButton() {
		OtherModel.getInstance().handleMarcoButton();
		InfoModel.getInstance().updateInfo("Hotovo!");
	}

	@FXML
	void handleSettingsButtonAction(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("OtherSettings.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.setTitle("Nastavení ostatní");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
